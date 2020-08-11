package com.nacid.epay;

public class EpayResponseException extends Exception {
    private EpayResponse epayResponse;
    public EpayResponseException(String message, EpayResponse epayResponse) {
        super(message);
        this.epayResponse = epayResponse;
    }
    public EpayResponse getEpayResponse() {
        return epayResponse;
    }
    
}
