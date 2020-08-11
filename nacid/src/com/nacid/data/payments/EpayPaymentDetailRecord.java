package com.nacid.data.payments;

import java.sql.Timestamp;

import com.nacid.bl.payments.EpayPaymentDetail;
import com.nacid.data.annotations.Table;
@Table(name="epay_payment_details")
public class EpayPaymentDetailRecord implements EpayPaymentDetail {
    private int id;
    private int liabilityId;
    private int status;
    private String refNumber;
    private Timestamp dateGenerated;
    private Timestamp expirationDate;
    private Timestamp datePayment;
    private String idn;
    
    public EpayPaymentDetailRecord() {
    }
    public EpayPaymentDetailRecord(int id, int liabilityId, int status,
            Timestamp dateGenerated,
            Timestamp expirationTime, Timestamp datePayment, String idn) {
        this.id = id;
        this.liabilityId = liabilityId;
        this.status = status;
        this.dateGenerated = dateGenerated;
        this.expirationDate = expirationTime;
        this.datePayment = datePayment;
        this.idn = idn;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getLiabilityId() {
        return liabilityId;
    }
    public void setLiabilityId(int liabilityId) {
        this.liabilityId = liabilityId;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getRefNumber() {
        return refNumber;
    }
    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }
    public Timestamp getDateGenerated() {
        return dateGenerated;
    }
    public void setDateGenerated(Timestamp dateGenerated) {
        this.dateGenerated = dateGenerated;
    }
    public Timestamp getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
    public Timestamp getDatePayment() {
        return datePayment;
    }
    public void setDatePayment(Timestamp datePayment) {
        this.datePayment = datePayment;
    }
    public String getIdn() {
        return idn;
    }
    public void setIdn(String idn) {
        this.idn = idn;
    }
    
    
}
