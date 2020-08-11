package com.nacid.bl.impl.esoed;

import org.apache.axiom.om.OMElement;

import com.nacid.bl.esoed.EsoedErrors;

/**
* This is simple presenation of ESOED error
*
*/

public class EsoedErrorsImpl implements EsoedErrors {
    private Integer errNumber;
    private String errText;
    public EsoedErrorsImpl(Integer errNumber){
        this(errNumber, null);
    }
    public EsoedErrorsImpl(String errText){
        this(null, errText);
    }

    public EsoedErrorsImpl(Integer errNumber, String errText){
        this.errNumber = errNumber;
        this.errText = errText;
    }

    public Integer getErrNumber(){
        return errNumber;
    }

    public String getErrText(){
        return this.errText;
    }

}