package com.nacid.bl.esoed;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.axiom.om.OMElement;


/**
 * This is the basic interface with access methods for ESOED message
 *
 */

public interface EsoedMessage {
	/* Constants begin*/
	// SenderDetails/xMessageType
	public static String XMESSAGE_TYPE_DOCUMENT             = "Document";
	public static String XMESSAGE_TYPE_DOCUMENT_RECEIPT     = "DocumentReceipt";
	public static String XMESSAGE_TYPE_ESOED_RESPONSE       = "EsoedResponse";
	public static String XMESSAGE_TYPE_ESOED_ERROR          = "EsoedError";
	public static String XMESSAGE_TYPE_CC_ERROR             = "CCError";

	public static final String NS_SOAP  = "http://esoed.egov.bg/2008/05/SoapEntryPoint/v1";
	public static final String NS_ESOED = "http://esoed.egov.bg/2008/05/Envelope/v1";
	public static final String ENVELOPE_VERSION             = "1.0";

	public static final String  DEFAULT_TRANSPORT            = "ESOED";
	public static final Integer DEFAULT_USER                 = 0;

	public static final String XPATH_ESOED_NS_PREFIX        = "e";

	public static final String XPATH_ESOED_ENVELOPE_VERSION         = "/e:Esoed/e:SenderDetails/e:EnvelopeVersion";
	public static final String XPATH_X_MESSAGE_TYPE                 = "/e:Esoed/e:SenderDetails/e:xMessageType";
	public static final String XPATH_ESOED_SESSION_ID               = "/e:Esoed/e:SenderDetails/e:EsoedSessionId";
	public static final String XPATH_ESOED_ESERVICE_URI             = "/e:Esoed/e:SenderDetails/e:Correlation/e:eServiceURI";
	public static final String XPATH_ESOED_CORRELATION_ID           = "/e:Esoed/e:SenderDetails/e:Correlation/e:EsoedCorrelationId";
	public static final String XPATH_ESOED_ORIGINATOR_EXTRA_DATA    = "/e:Esoed/e:SenderDetails/e:Correlation/e:OriginatorExtraData";
	public static final String XPATH_SENDER_DETAILS_SENDER          = "/e:Esoed/e:SenderDetails/e:Sender/e:URI";
	public static final String XPATH_ESOED_DOCUMENT_URI             = "/e:Esoed/e:SenderDetails/e:Sender/e:DocumentUri";
	public static final String XPATH_ESOED_DOCUMENT_TYPE_URI        = "/e:Esoed/e:SenderDetails/e:Sender/e:DocumentTypeUri";
	public static final String XPATH_ESOED_RECIPIENT                = "/e:Esoed/e:SenderDetails/e:Recipient/e:URI";
	public static final String XPATH_SESSION_ID                     = "/e:Esoed/e:EsoedDetails/e:SessionId";
	public static final String XPATH_CORRELATION_ID                 = "/e:Esoed/e:EsoedDetails/e:CorrelationId";
	public static final String XPATH_ESOED_RECEIVE_TIME             = "/e:Esoed/e:EsoedDetails/e:ReceiveTime";
	public static final String XPATH_ESOED_ERRORS                   = "/e:Esoed/e:EsoedDetails/e:Errors";
	public static final String XPATH_ESOED_BODY                     = "/e:Esoed/e:Body";

	/* Constants end*/

	/**  Returns ID of message (msg_messages.id) from table in database (if saved or loaded from datbase) */
	public Integer getId();

	/**  Returns transport - default is ESOED */
	public String getTransport();

	/**  Returns connection to database if any. */
	public Connection getConnection();

	/**  Returns created organizatoin unit code*/
	public String getCreatedOut();

	/**  Returns created user id*/
	public Integer getCreatedUser();

	/**  Returns SenderDetails/EnvelopeVersion */
	public String getEnvelopeVersion();

	/**  Returns SenderDetails/xMessageType - see XMESSAGE_TYPE_* constants */
	public String getXMessageType();

	/** Returns SenderDetails/EsoedSessionId*/
	public String getEsoedSessionId();

	/** Returns SenderDetails/eServiceURI*/
	public String getEServiceURI();

	/** Returns SenderDetails/EsoedCorrelationId*/
	public String getEsoedCorrelationId();

	/** Returns SenderDetails/OriginatorExtraData*/
	public String getOriginatorExtraData();

	/** Returns SenderDetails/Sender/URI*/
	public String getSenderURI();

	/** Returns SenderDetails/Sender/DocumentUri*/
	public String getDocumentURI();

	/** Returns SenderDetails/Sender/DocumentTypeUri*/
	public String getDocumentTypeURI();

	/** Returns SenderDetails/Recipient/URI*/
	public String getRecipientURI();

	/** Returns EseodDetails/SessionId*/
	public String getSessionId();

	/** Returns EsoedDetails/CorrelationId*/
	public String getCorrelationId();

	/** Returns EsoedDetails/ReceiveTime*/
	public Date getReceiveTime();

	/** Returns EsoedDetails/Errors*/
	public List<EsoedErrors> getErrors();

	/** Returns EsoedDetails/AdditionalAttributes - ???*/
	public List<String> getAdditionalAttributes();

	/** Returns Body element as String*/
	public OMElement getBody();

	/**  Sets ID of message (msg_messages.id)*/
	public void setId(Integer id);

	/**  Set transport */
	public void setTransport(String transport);

	/**  Set created organization unit */
	public void setCreatedOut(String createdOut);

	/**  Set created user id */
	public void setCreatedUser(Integer createdUser);

	/**  Set SenderDetails/EnvelopeVersion */
	public void setEnvelopeVersion(String version);

	/**  Set SenderDetails/xMessageType - see XMESSAGE_TYPE_* constants */
	public void setXMessageType(String xMessageType);

	/** Returns SenderDetails/EsoedSessionId*/
	public void setEsoedSessionId(String esoedSessionId);

	/** Set SenderDetails/eServiceURI*/
	public void setEServiceURI(String eServiceURI);

	/** Set SenderDetails/EsoedCorrelationId*/
	public void setEsoedCorrelationId(String esoedCorrelationId);

	/** Set SenderDetails/OriginatorExtraData*/
	public void setOriginatorExtraData(String originatorExtraData);

	/** Set SenderDetails/Sender/URI*/
	public void setSenderURI(String senderURI);

	/** Set SenderDetails/Sender/DocumentUri*/
	public void setDocumentURI(String documentURI);

	/** Set SenderDetails/Sender/DocumentTypeUri*/
	public void setDocumentTypeURI(String documentTypeURI);

	/** Set SenderDetails/Recipient/URI*/
	public void setRecipientURI(String recipientURI);

	/** Set EsoedDetails/ReceiveTime*/
	public void setReceiveTime(Date receiveTime);

	/** Set Body element as String*/
	public void setBody(OMElement body);

	/** Constructs XML document with current attributes and returns it*/
	public OMElement asXML();

	/** Constructs XML document with current attributes and returns the XML as serialized string*/
	public String asXMLString();

	/** Save message to database */
	//public void save(String inOutFlag) throws EsoedException;

	/** Loads message from database */
	//public void load(Integer id) throws EsoedException;

	/** Update sent message from returned response messsage */
	//public void updateFromResponse(EsoedMessage response) throws EsoedException;

	/** Update status and receipt date in original send document - identify by esoedSessionId and esoedCorrelationId*/
	//public void updateFromReceipt() throws EsoedException;

	/** Set connection to database. If no connection is set, new connection will be temporary open for save/update operations. */
	//public void setConnection(Connection conn);

}