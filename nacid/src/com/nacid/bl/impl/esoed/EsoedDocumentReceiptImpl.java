package com.nacid.bl.impl.esoed;

import org.apache.axiom.om.OMElement;

import com.nacid.bl.esoed.EsoedMessage;

/**
* This class constructs DocumentReceipt from incomming Document message
*
*/

public class EsoedDocumentReceiptImpl extends BaseEsoedMessageImpl {

    /** Constructs empty DocumentReceipt message */
    public EsoedDocumentReceiptImpl(){
    }

    /** Constructs DocumentReceipt message from incomming XML message */
    public EsoedDocumentReceiptImpl(OMElement document){
        super(document);
    }

    /** Constructs DocumentReceipt message from incomming Document message */
    public EsoedDocumentReceiptImpl(EsoedMessage documentMessage){
        setXMessageType( XMESSAGE_TYPE_DOCUMENT_RECEIPT );
        setEsoedSessionId( documentMessage.getSessionId() );
        setEsoedCorrelationId( documentMessage.getCorrelationId() );
        setOriginatorExtraData( documentMessage.getOriginatorExtraData() );
        setSenderURI( documentMessage.getRecipientURI() );
        setRecipientURI( documentMessage.getSenderURI() );
        setDocumentURI( documentMessage.getDocumentURI() );
        setDocumentTypeURI( documentMessage.getDocumentTypeURI() );
    }

}