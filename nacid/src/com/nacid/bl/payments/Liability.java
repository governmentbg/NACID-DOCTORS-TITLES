package com.nacid.bl.payments;

import java.math.BigDecimal;
import java.util.Date;

public interface Liability {
    public static final String CURRENCY_BGN = "BGN";
    public static final int LIABILITY_NOT_PAID = 1;
    public static final int LIABILITY_PAID = 2;
    
    
    public static final int PAYMENT_TYPE_EPAY = 5;
    public static final int PAYMENT_TYPE_CASH = 1;
    public int getId();
    public BigDecimal getAmount();
    public int getStatus();
    public Date getDateGenerated();
    public Date getDatePayment();
    public String getCurrency();
    public int getPaymentType();
    public boolean isPaid();
}
