package com.nacid.bl.impl.esoed;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.log4j.Logger;
import org.jaxen.JaxenException;

import com.nacid.bl.esoed.EsoedErrors;
import com.nacid.bl.esoed.EsoedException;
import com.nacid.bl.esoed.EsoedMessage;
/**
 * This is the basic interface with access methods for ESOED message
 *
 */

public class BaseEsoedMessageImpl implements EsoedMessage {
	protected static final Logger log = Logger.getLogger(BaseEsoedMessageImpl.class);

	private static AXIOMXPath xpathSessionID;
	private static AXIOMXPath xpathDocUri;
	private static AXIOMXPath xpathDocTypeUri;
	private static AXIOMXPath xpathSender;
	private static AXIOMXPath xpathRecipient;
	private static AXIOMXPath xpathEsoedCorrelationId;
	private static AXIOMXPath xpathXMessageType;
	private static AXIOMXPath xpatheServiceURI;
	private static AXIOMXPath xpathReceiveTime;
	private static AXIOMXPath xpathBody;
	private static AXIOMXPath xpathOriginatorExtraData;
	private static AXIOMXPath xpathEsoedSessionId;
	private static AXIOMXPath xpathCorrelationId;
	private static AXIOMXPath xpathEnvelopeVersion;
	private static AXIOMXPath xpathErrors;

	private OMElement document          = null;
	private Connection conn             = null;

	private Integer id                  = null; // database id, if save to database
	private String transport            = DEFAULT_TRANSPORT;
	private String createdOut           = null;
	private Integer createdUser         = DEFAULT_USER;
	private String envelopeVersion      = null;
	private String xMessageType         = null;
	private String esoedSessionId       = null;
	private String eServiceURI          = null;
	private String esoedCorrelationId   = null;
	private String originatorExtraData  = null;
	private String senderURI            = null;
	private String documentURI          = null;
	private String documentTypeURI      = null;
	private String recipientURI         = null;
	private String sessionId            = null;
	private String correlationId        = null;
	private Date receiveTime          = null;
	private List<EsoedErrors> errors    = null;
	private List<String> additionAttributes= null;
	private OMElement body                 = null;

	static{
		init();
	}

	private static  void init() {
		try {
			xpathSessionID          = new AXIOMXPath (XPATH_SESSION_ID);
			xpathDocUri             = new AXIOMXPath (XPATH_ESOED_DOCUMENT_URI);
			xpathDocTypeUri         = new AXIOMXPath (XPATH_ESOED_DOCUMENT_TYPE_URI);
			xpathSender             = new AXIOMXPath (XPATH_SENDER_DETAILS_SENDER);
			xpathRecipient          = new AXIOMXPath (XPATH_ESOED_RECIPIENT);
			xpathEsoedCorrelationId = new AXIOMXPath (XPATH_ESOED_CORRELATION_ID);
			xpathCorrelationId      = new AXIOMXPath (XPATH_CORRELATION_ID);
			xpathXMessageType       = new AXIOMXPath (XPATH_X_MESSAGE_TYPE);
			xpatheServiceURI        = new AXIOMXPath (XPATH_ESOED_ESERVICE_URI);
			xpathReceiveTime        = new AXIOMXPath (XPATH_ESOED_RECEIVE_TIME);
			xpathBody               = new AXIOMXPath (XPATH_ESOED_BODY);
			xpathOriginatorExtraData= new AXIOMXPath (XPATH_ESOED_ORIGINATOR_EXTRA_DATA);
			xpathEsoedSessionId     = new AXIOMXPath (XPATH_ESOED_SESSION_ID);
			xpathEnvelopeVersion    = new AXIOMXPath (XPATH_ESOED_ENVELOPE_VERSION);
			xpathErrors             = new AXIOMXPath (XPATH_ESOED_ERRORS);


			xpathSessionID.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathDocUri.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathDocTypeUri.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathSender.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathRecipient.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathEsoedCorrelationId.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathCorrelationId.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathXMessageType.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpatheServiceURI.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathReceiveTime.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathBody.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathOriginatorExtraData.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathEsoedSessionId.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathCorrelationId.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathEnvelopeVersion.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);
			xpathErrors.addNamespace(XPATH_ESOED_NS_PREFIX, NS_ESOED);

		} catch(JaxenException jax) {
			log.error("Cannot initialize xpats", jax);
		}
	}



	/** Default constructor */
	public BaseEsoedMessageImpl(){
		setEnvelopeVersion(ENVELOPE_VERSION);
	}

	/** Constructs the object from XML document */
	public BaseEsoedMessageImpl(OMElement document){
		this.document = document;
		initializeFromXML( document );
	}

	/**  Returns ID of message (msg_messages.id) from table in database (if saved or loaded from datbase) */
	public Integer getId(){
		return this.id;
	}

	public String getTransport(){
		return this.transport;
	}

	public Connection getConnection(){
		return this.conn;
	}

	public String getCreatedOut(){
		return this.createdOut;
	}

	public Integer getCreatedUser(){
		return this.createdUser;
	}

	/**  Returns SenderDetails/EnvelopeVersion */
	public String getEnvelopeVersion(){
		return this.envelopeVersion;
	}

	/**  Returns SenderDetails/xMessageType - see XMESSAGE_TYPE_* constants */
	public String getXMessageType(){
		return this.xMessageType;
	}

	/** Returns SenderDetails/EsoedSessionId*/
	public String getEsoedSessionId(){
		return this.esoedSessionId;
	}

	/** Returns SenderDetails/eServiceURI*/
	public String getEServiceURI(){
		return this.eServiceURI;
	}

	/** Returns SenderDetails/EsoedCorrelationId*/
	public String getEsoedCorrelationId(){
		return this.esoedCorrelationId;
	}

	/** Returns SenderDetails/OriginatorExtraData*/
	public String getOriginatorExtraData(){
		return this.originatorExtraData;
	}

	/** Returns SenderDetails/Sender/URI*/
	public String getSenderURI(){
		return this.senderURI;
	}

	/** Returns SenderDetails/Sender/DocumentUri*/
	public String getDocumentURI(){
		return this.documentURI;
	}

	/** Returns SenderDetails/Sender/DocumentTypeUri*/
	public String getDocumentTypeURI(){
		return this.documentTypeURI;
	}

	/** Returns SenderDetails/Recipient/URI*/
	public String getRecipientURI(){
		return this.recipientURI;
	}

	/** Returns EseodDetails/SessionId*/
	public String getSessionId(){
		return this.sessionId;
	}

	/** Returns EsoedDetails/CorrelationId*/
	public String getCorrelationId(){
		return this.correlationId;
	}

	/** Returns EsoedDetails/ReceiveTime*/
	public Date getReceiveTime(){
		return this.receiveTime;
	}

	/** Returns EsoedDetails/Errors*/
	public List<EsoedErrors> getErrors(){
		return this.errors;
	}

	/** Returns EsoedDetails/AdditionalAttributes - ???*/
	public List<String> getAdditionalAttributes(){
		return this.additionAttributes;
	}

	/** Returns Body element as String*/
	public OMElement getBody(){
		return this.body;
	}

	public void setId( Integer id ){
		this.id = id;
	}

	public void setTransport(String transport){
		this.transport = transport;
	}

	public void setCreatedOut(String createdOut){
		this.createdOut = createdOut;
	}

	public void setCreatedUser(Integer createdUser){
		this.createdUser = createdUser;
	}

	/**  Set SenderDetails/EnvelopeVersion */
	public void setEnvelopeVersion(String version){
		this.envelopeVersion = version;
	}

	/**  Set SenderDetails/xMessageType - see XMESSAGE_TYPE_* constants */
	public void setXMessageType(String xMessageType){
		this.xMessageType = xMessageType;
	}

	/** Returns SenderDetails/EsoedSessionId*/
	public void setEsoedSessionId(String esoedSessionId){
		this.esoedSessionId = esoedSessionId;
	}

	/** Set SenderDetails/eServiceURI*/
	public void setEServiceURI(String eServiceURI){
		this.eServiceURI = eServiceURI;
	}

	/** Set SenderDetails/EsoedCorrelationId*/
	public void setEsoedCorrelationId(String esoedCorrelationId){
		this.esoedCorrelationId = esoedCorrelationId;
	}

	/** Set SenderDetails/OriginatorExtraData*/
	public void setOriginatorExtraData(String originatorExtraData){
		this.originatorExtraData = originatorExtraData;
	}

	/** Set SenderDetails/Sender/URI*/
	public void setSenderURI(String senderURI){
		this.senderURI = senderURI;
	}

	/** Set SenderDetails/Sender/DocumentUri*/
	public void setDocumentURI(String documentURI){
		this.documentURI = documentURI;
	}

	/** Set SenderDetails/Sender/DocumentTypeUri*/
	public void setDocumentTypeURI(String documentTypeURI){
		this.documentTypeURI = documentTypeURI;
	}

	/** Set SenderDetails/Recipient/URI*/
	public void setRecipientURI(String recipientURI){
		this.recipientURI = recipientURI;
	}

	/** Set EsoedDetails/ReceiveTime*/
	public void setReceiveTime(Date receiveTime){
		this.receiveTime = receiveTime;
	}

	/** Set Body element as String*/
	public void setBody(OMElement body){
		this.body = body;
	}

	public void setConnection(Connection conn){
		this.conn = conn;
	}

	
	public String toString(){
		String result = "";
		result += ("transport = "+transport);
		result += ("envelopeVersion = "+envelopeVersion);
		result += ("xMessageType = "+xMessageType);
		result += ("esoedSessionId = "+esoedSessionId);
		result += ("eServiceURI = "+eServiceURI);
		result += ("esoedCorrelationId = "+esoedCorrelationId);
		result += ("originatorExtraData = "+originatorExtraData);
		result += ("senderURI = "+senderURI);
		result += ("documentURI = "+documentURI);
		result += ("documentTypeURI = "+documentTypeURI);
		result += ("recipientURI = "+recipientURI);
		result += ("sessionId = "+sessionId);
		result += ("correlationId = "+correlationId);
		result += ("receiveTime = "+receiveTime);
		try {
			if (body != null){
				OutputStream os = new ByteArrayOutputStream();
				body.serialize( os );
				result += ("body = "+os.toString() );
				os.close();
			}
		} catch (Exception e){
			log.error("Error serializing body", e);
		}
		return result;
	}

	/** Constructs XML document with current attributes and returns it*/
	public OMElement asXML(){
		if (document == null ) constructXML();
		return this.document;
	}

	/** Constructs XML document with current attributes and returns the XML as serialized string*/
	public String asXMLString(){
		asXML();
		OutputStream os = new ByteArrayOutputStream();
		try {
			this.document.serialize( os );
		} catch (Exception ignore) {}
		return os.toString();
	}

	

	/** Load message from database */
	public void load(Integer id) throws EsoedException {
	}


	// ======== PRIVATE METHODS ============
	private String getXPathText( AXIOMXPath path, OMElement document ){
		try{
			Object o = path.selectSingleNode(document);
			if ( o == null ) return null;
			else return ((OMElement) o).getText();
		} catch (org.jaxen.JaxenException e){
			return null;
		}
	}
	private OMElement getXPathElement( AXIOMXPath path, OMElement document ){
		try{
			return (OMElement) path.selectSingleNode(document);
		} catch (org.jaxen.JaxenException e){
			return null;
		}
	}

	private void initializeFromXML(OMElement document){
		envelopeVersion         = getXPathText( xpathEnvelopeVersion, document );
		xMessageType            = getXPathText( xpathXMessageType, document);
		esoedSessionId          = getXPathText( xpathEsoedSessionId, document );
		eServiceURI             = getXPathText( xpatheServiceURI, document );
		esoedCorrelationId      = getXPathText( xpathEsoedCorrelationId, document);
		originatorExtraData     = getXPathText( xpathOriginatorExtraData, document);
		senderURI               = getXPathText( xpathSender, document);
		documentURI             = getXPathText( xpathDocUri, document);
		documentTypeURI         = getXPathText( xpathDocTypeUri, document);
		recipientURI            = getXPathText( xpathRecipient, document);
		sessionId               = getXPathText( xpathSessionID, document);
		correlationId           = getXPathText( xpathCorrelationId, document);
		receiveTime             = null;//getXPathText( xpathReceiveTime, document);//TODO
		body                    = getXPathElement( xpathBody, document).getFirstElement();

		OMElement errNode = getXPathElement( xpathErrors, document );
		if ( errNode != null ){
			Iterator i = errNode.getChildElements();
			this.errors = new ArrayList<EsoedErrors>();
			while ( i.hasNext() ){
				OMElement e = (OMElement) i.next();
				Iterator i2 = e.getChildElements();
				while ( i2.hasNext() ){
					OMElement e2 = (OMElement) i2.next();
					EsoedErrorsImpl err = null;
					if ( e2.getLocalName().equals("Number") ){
						err = new EsoedErrorsImpl( new Integer(e2.getText()) );
					} else if ( e2.getLocalName().equals("Text") ) {
						err = new EsoedErrorsImpl( e2.getText() );
					}
					if ( err != null ) errors.add( err );
				}
			}
		}
	}

	private void constructXML(){
		OMFactory fac      = OMAbstractFactory.getOMFactory();
		OMNamespace nsEsoed   = fac.createOMNamespace(NS_ESOED,"");

		OMElement elEsoed = fac.createOMElement("Esoed", nsEsoed);

		// Esoed/SenderDetails
		OMElement elSenderDetails = fac.createOMElement("SenderDetails", nsEsoed);
		elEsoed.addChild(elSenderDetails);

		// Esoed/SenderDetails/EnvelopeVersion
		OMElement elEnvelopeVersion = fac.createOMElement("EnvelopeVersion", nsEsoed);
		elEnvelopeVersion.setText( envelopeVersion );
		elSenderDetails.addChild( elEnvelopeVersion );

		// Esoed/SenderDetails/xMessageType
		OMElement elMessageType = fac.createOMElement("xMessageType", nsEsoed);
		elMessageType.setText( xMessageType );
		elSenderDetails.addChild( elMessageType );

		// Esoed/SenderDetails/EsoedSessionId
		if ( esoedSessionId != null){
			OMElement elSessionId = fac.createOMElement("EsoedSessionId", nsEsoed);
			elSessionId.setText( esoedSessionId );
			elSenderDetails.addChild( elSessionId );
		}

		// Esoed/SenderDetails/Correlation
		if ( eServiceURI != null || esoedCorrelationId != null || originatorExtraData != null ){
			OMElement elCorrelation = fac.createOMElement("Correlation", nsEsoed);
			elSenderDetails.addChild( elCorrelation );
			// Esoed/SenderDetails/Correlation/eServiceURI
			if ( eServiceURI != null ) {
				OMElement elEServiceURI = fac.createOMElement("eServiceURI", nsEsoed);
				elEServiceURI.setText( eServiceURI );
				elCorrelation.addChild( elEServiceURI );
			}
			// Esoed/SenderDetails/Correlation/EsoedCorrelationId
			if ( esoedCorrelationId != null ) {
				OMElement elEsoedCorrelationId = fac.createOMElement("EsoedCorrelationId", nsEsoed);
				elEsoedCorrelationId.setText( esoedCorrelationId );
				elCorrelation.addChild( elEsoedCorrelationId );
			}
			// Esoed/SenderDetails/Correlation/OriginatorExtraData
			if ( originatorExtraData != null ) {
				OMElement elOriginatorExtraData = fac.createOMElement("OriginatorExtraData", nsEsoed);
				elOriginatorExtraData.setText( originatorExtraData);
				elCorrelation.addChild( elOriginatorExtraData );
			}
		}

		// Esoed/SenderDetails/Sender
		OMElement elSender = fac.createOMElement("Sender",nsEsoed);
		elSenderDetails.addChild( elSender );

		// Esoed/SenderDetails/Sender/URI
		OMElement elSenderURI = fac.createOMElement("URI", nsEsoed);
		elSenderURI.setText( senderURI );
		elSender.addChild( elSenderURI );

		// Esoed/SenderDetails/Sender/DocumentUri
		OMElement elDocumentURI = fac.createOMElement("DocumentUri", nsEsoed);
		elDocumentURI.setText( documentURI );
		elSender.addChild( elDocumentURI );

		// Esoed/SenderDetails/Sender/DocumentTypeUri
		OMElement elDocumentTypeURI = fac.createOMElement("DocumentTypeUri", nsEsoed);
		elDocumentTypeURI.setText( documentTypeURI );
		elSender.addChild( elDocumentTypeURI );

		// Esoed/SenderDetails/Recipient
		OMElement elRecipient = fac.createOMElement("Recipient", nsEsoed);
		elSenderDetails.addChild( elRecipient );

		// Esoed/SenderDetails/Recipient/URI
		OMElement elRecipientURI = fac.createOMElement("URI", nsEsoed);
		elRecipientURI.setText( recipientURI );
		elRecipient.addChild( elRecipientURI );

		// Esoed/Body
		OMElement elBody = fac.createOMElement("Body", nsEsoed);
		elEsoed.addChild( elBody );
		if (body != null ){
			elBody.addChild( body );
		}

		this.document = elEsoed ;

	}



}