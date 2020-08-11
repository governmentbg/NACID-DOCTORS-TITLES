package com.nacid.data.payments;

import java.math.BigDecimal;
import java.util.Date;

import com.nacid.bl.payments.ExtApplicationPaymentInformation;

public class ExtApplicationPaymentInformationRecord implements ExtApplicationPaymentInformation {
    private String email;
    private BigDecimal amount;
    private int paymentStatus;
    private Date dateSubmitted;
    private Date paymentDatePayment;
    private String fullName;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public int getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public Date getDateSubmitted() {
        return dateSubmitted;
    }
    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }
    public Date getPaymentDatePayment() {
        return paymentDatePayment;
    }
    public void setPaymentDatePayment(Date paymentDatePayment) {
        this.paymentDatePayment = paymentDatePayment;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
}
