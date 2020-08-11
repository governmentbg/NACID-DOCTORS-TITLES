package com.nacid.bl.payments;

import java.math.BigDecimal;
import java.util.Date;

public interface ExtApplicationPaymentInformation {
    public String getEmail();
    public BigDecimal getAmount();
    public int getPaymentStatus();
    public Date getDateSubmitted();
    public Date getPaymentDatePayment();
    public String getFullName();
}
