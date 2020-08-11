package com.nacid.data.payments;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.nacid.bl.payments.Liability;
import com.nacid.data.annotations.Table;

@Table(name="liabilities")
public class LiabilityRecord implements Liability {
    private int id;
    private BigDecimal amount;
    private int status;
    private Timestamp dateGenerated;
    private Timestamp datePayment;
    private String currency;
    private int paymentType;
    
    public LiabilityRecord() {
    }
    public LiabilityRecord(int id, BigDecimal amount, int status,
            Timestamp dateGenerated, Timestamp datePayment, String currency,
            int paymentType) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.dateGenerated = dateGenerated;
        this.datePayment = datePayment;
        this.currency = currency;
        this.paymentType = paymentType;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Timestamp getDateGenerated() {
        return dateGenerated;
    }
    public void setDateGenerated(Timestamp dateGenerated) {
        this.dateGenerated = dateGenerated;
    }
    public Timestamp getDatePayment() {
        return datePayment;
    }
    public void setDatePayment(Timestamp datePayment) {
        this.datePayment = datePayment;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public int getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }
    @Override
    public boolean isPaid() {
        return Liability.LIABILITY_PAID == getStatus();
    }
}
