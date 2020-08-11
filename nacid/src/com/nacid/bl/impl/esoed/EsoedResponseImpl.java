package com.nacid.bl.impl.esoed;

import org.apache.axiom.om.OMElement;


/**
* This class constructs EsoedResponse instance.
*
*/

public class EsoedResponseImpl extends BaseEsoedMessageImpl {

    /** Constructs empty EsoedResponse message */
    public EsoedResponseImpl(){
    }

    /** Constructs empty EsoedResponse message from received XML */
    public EsoedResponseImpl(OMElement document){
        super( document );
    }

}