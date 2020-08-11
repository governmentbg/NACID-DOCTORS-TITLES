package com.nacid.bl.impl.esoed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.jaxen.JaxenException;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.esoed.EsoedDataProvider;
import com.nacid.bl.esoed.EsoedException;
import com.nacid.bl.esoed.EsoedMessage;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.data.esoed.EsoedMessageRecord;
import com.nacid.db.esoed.EsoedDb;
import com.nacid.db.utils.StandAloneDataSource;

public class EsoedDataProviderImpl implements EsoedDataProvider {
	private EsoedDb db;
	private AXIOMXPath xpathXMessageType = null;

	public EsoedDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		try {
			this.db = new EsoedDb(nacidDataProvider.getDataSource());
			xpathXMessageType= new AXIOMXPath (EsoedMessage.XPATH_X_MESSAGE_TYPE);
			xpathXMessageType.addNamespace(EsoedMessage.XPATH_ESOED_NS_PREFIX, EsoedMessage.NS_ESOED);
		} catch(JaxenException jax) {
			throw Utils.logException(jax);
		}
	}
	/*public int insertMessage(String inOut, String transport, String envelopeVersion, String messageType, String senderUri,
			String documentUri, String documentTypeUri, String recipientUri, String content, String esoedSessionId, String eserviceUri,
			String esoedCorrelationId, String originatorExtraData, String sessionId, String correlationId, String receiveTime) {
		try {
			EsoedMessageRecord record = new EsoedMessageRecord(0, inOut, transport, envelopeVersion, messageType, senderUri, documentUri, documentTypeUri, recipientUri, content, esoedSessionId, eserviceUri, esoedCorrelationId, originatorExtraData, sessionId, correlationId, receiveTime);
			record = db.insertRecord(record);
			return record.getId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}*/

	
	/** Creates EsoedMessage object instance from received XML document */
	public EsoedMessage createEsoedMessage(OMElement document) throws EsoedException {
		try{
			Object o = xpathXMessageType.selectSingleNode(document);
			if ( o == null || !(o instanceof OMElement) ) throw new EsoedException("xMessageType not found in ESOED message.");

			String messageType = ((OMElement) o).getText();
			EsoedMessage message = null;

			if ( messageType.equals( EsoedMessage.XMESSAGE_TYPE_DOCUMENT) ) {
				message = new EsoedDocumentImpl( document );
			} else if ( messageType.equals(EsoedMessage.XMESSAGE_TYPE_DOCUMENT_RECEIPT) ) {
				message = new EsoedDocumentReceiptImpl( document );
			} else if ( messageType.equals(EsoedMessage.XMESSAGE_TYPE_ESOED_RESPONSE) ) {
				message = new EsoedResponseImpl( document );
			} else if ( messageType.equals(EsoedMessage.XMESSAGE_TYPE_ESOED_ERROR) ) {
				message = new EsoedErrorImpl( document );
			} else {
				throw new EsoedException ("Unknown xMessageType: "+messageType);
			}

			return message;

		} catch (Exception e){
			throw new EsoedException("Error creating EsoedMessage", e);
		}
	}

	/** Creates EsoedMessage object instance from received XML document */
	public EsoedMessage createEsoedMessage(String documentString) throws EsoedException{
		OMElement document = null;
		try{
			InputStream is = new ByteArrayInputStream( documentString.getBytes() );
			XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(is);
			StAXOMBuilder   builder = new StAXOMBuilder(parser);
			document =  builder.getDocumentElement();
		} catch (Exception e){
			throw new EsoedException("Error parsing string to XML for EsoedMessage creation.",e);
		}
		return createEsoedMessage( document );
	}

	/** Save message to database */
	public void saveEsoedMessage(String inOutFlag, EsoedMessage msg) throws EsoedException{
		try{
			OutputStream os = new ByteArrayOutputStream();
			OMElement document = msg.asXML();
			document.serialize(os);
			EsoedMessageRecord record = new EsoedMessageRecord(0, inOutFlag, msg.getTransport(), msg.getEnvelopeVersion(), msg.getXMessageType(), msg.getSenderURI(), msg.getDocumentURI(), msg.getDocumentTypeURI(), msg.getRecipientURI(), os.toString(), msg.getEsoedSessionId(), msg.getSenderURI(), msg.getEsoedCorrelationId(), msg.getOriginatorExtraData(), msg.getSessionId(), msg.getCorrelationId(), DataConverter.toTimestamp(msg.getReceiveTime()));
			record = db.insertRecord(record);
			msg.setId(record.getId());
		} catch (Exception e) {
			throw new EsoedException("Can not save to database", e);
		}
	}


	/** Update sent message from returned response messsage */
	public void updateFromResponse(EsoedMessage msg, EsoedMessage response) throws EsoedException{
		try{
			String status = (response instanceof EsoedErrorImpl)?"ERR":(response instanceof EsoedResponseImpl?"OK":"ERR");
			int errors = response.getErrors()==null?0:response.getErrors().size();
			msg.setEsoedSessionId( response.getEsoedSessionId() );
			msg.setEsoedCorrelationId( response.getEsoedCorrelationId() );
			msg.setReceiveTime( response.getReceiveTime() );

			EsoedMessageRecord record = db.selectRecord(new EsoedMessageRecord(), msg.getId());
			record.setEsoedSessionId(response.getEsoedSessionId());
			record.setEsoedCorrelationId(response.getEsoedCorrelationId());

			record.setReceiveTime(DataConverter.toTimestamp(response.getReceiveTime()));
			record.setSendStatus(status);
			record.setErrors(errors);
			db.updateRecord(record);
		} catch (Exception e) {
			throw new EsoedException("Can not save to database", e);
		}
	}

	/** Update status and receipt date in original send document - identify by esoedSessionId and esoedCorrelationId*/
	public void updateFromReceipt(EsoedMessage msg) throws EsoedException{
		try{

			List<EsoedMessageRecord> recs = db.selectRecords(EsoedMessageRecord.class, "esoed_session_id=? and esoed_correlation_id=? and in_out='OUT' and message_type='Document'", msg.getSessionId(), msg.getEsoedCorrelationId(), "OUT", msg.getXMessageType());
			if (recs.size() > 0) {
				EsoedMessageRecord rec = recs.get(0);
				rec.setSendStatus("CFD");
				rec.setReceiveTime(DataConverter.toTimestamp(msg.getReceiveTime()));
				db.updateRecord(rec);
			}
		} catch (SQLException e) {
			throw new EsoedException("Can not save to database", e);
		}
	}


	public static void main(String[] args) throws EsoedException {
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		EsoedDataProvider dataProvider = nacidDataProvider.getEsoedDataProvider();
		EsoedMessage message = dataProvider.createEsoedMessage("<Esoed xmlns=\"http://esoed.egov.bg/2008/05/Envelope/v1\">"+
				"<SenderDetails>"+
				"<EnvelopeVersion>1.0</EnvelopeVersion>"+
				"<xMessageType>Document</xMessageType>"+
				"<Correlation>"+
				"<OriginatorExtraData>test</OriginatorExtraData>"+
				"</Correlation>"+
				"<Sender>"+
				"<URI>0002-94</URI>"+
				"<DocumentUri>0000-83</DocumentUri>"+
				"<DocumentTypeUri>000R-02</DocumentTypeUri>"+
				"</Sender>"+
				"<Recipient>"+
				"<URI>0002-94</URI>"+
				"</Recipient>"+
				"</SenderDetails>"+
				"<Body>"+
				"<testMessage>"+
				"<data>DDD</data>"+
				"</testMessage>"+
				"</Body>"+
		"</Esoed>");



		System.out.println(message.asXMLString() + "...End....");
	}
}
