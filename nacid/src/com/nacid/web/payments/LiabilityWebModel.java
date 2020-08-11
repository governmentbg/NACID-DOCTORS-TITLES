package com.nacid.web.payments;

import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.payments.Liability;
import com.nacid.data.DataConverter;

public class LiabilityWebModel {
    public String amount;
    public String paymentType;
    public String status;
    public String paymentDate;
    public LiabilityWebModel(Liability l, FlatNomenclature pt) {
        this.amount = l.getAmount().toPlainString();
        this.paymentType = pt.getName();
        this.status = l.getStatus() == Liability.LIABILITY_NOT_PAID ? "Неплатено" : "Платено";
        this.paymentDate = DataConverter.formatDateTime(l.getDatePayment(), false);
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
