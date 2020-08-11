package com.nacid.bl.impl.esoed;

import org.apache.axiom.om.OMElement;


/**
* This class constructs ESOED Document instance.
*
*/

public class EsoedDocumentImpl extends BaseEsoedMessageImpl {

    /** Constructs empty Document message */
    public EsoedDocumentImpl(){
        setXMessageType( XMESSAGE_TYPE_DOCUMENT );
    }

    /** Constructs Document message from incomming XML message */
    public EsoedDocumentImpl(OMElement document){
        super( document );
    }
}