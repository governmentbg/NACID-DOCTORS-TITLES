package com.nacid.epay;

import java.util.Date;

public class EpayResponse {
    private String invoice;
    private String status;
    private Date paymentTime;
    private String stan;
    private String bcode;
    EpayResponse(String invoice, String status, Date paymentTime,
            String stan, String bcode) {
        this.invoice = invoice;
        this.status = status;
        this.paymentTime = paymentTime;
        this.stan = stan;
        this.bcode = bcode;
    }
    public String getInvoice() {
        return invoice;
    }
    public String getStatus() {
        return status;
    }
    public Date getPaymentTime() {
        return paymentTime;
    }
    public String getStan() {
        return stan;
    }
    public String getBcode() {
        return bcode;
    }
    public boolean isPaidStatus() {
        return "PAID".equals(status);
    }
    public boolean isExpiredStatus() {
        return "EXPIRED".equals(status);
    }
    public boolean isDeniedStatus() {
        return "DENIED".equals(status);
    }
    
}
