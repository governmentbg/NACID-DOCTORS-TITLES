package com.nacid.bl.payments;

import java.util.Date;

public interface EpayPaymentDetail {
    public static final int PAYMENT_DETAILS_STATUS_GENERATED = 0;
    public static final int PAYMENT_DETAILS_STATUS_PAID = 1;
    public static final int PAYMENT_DETAILS_STATUS_CANCELED = 2;
    
    public int getId();
    public int getLiabilityId();
    public int getStatus();
    public String getRefNumber();
    public Date getDateGenerated();
    public Date getExpirationDate();
    public Date getDatePayment();
    public String getIdn();
}
