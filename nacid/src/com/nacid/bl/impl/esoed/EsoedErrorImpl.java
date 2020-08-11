package com.nacid.bl.impl.esoed;

import org.apache.axiom.om.OMElement;


/**
* This class constructs EsoedError instance.
*
*/

public class EsoedErrorImpl extends BaseEsoedMessageImpl {

    /** Constructs empty EsoedError message */
    public EsoedErrorImpl(){
    }

    /** Constructs empty EsoedError message from received XML */
    public EsoedErrorImpl(OMElement document){
        super( document );
    }

}