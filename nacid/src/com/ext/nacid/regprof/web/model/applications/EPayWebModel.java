package com.ext.nacid.regprof.web.model.applications;

import com.nacid.bl.nomenclatures.regprof.ServiceType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EPayWebModel {
    private String id;
    private String url;
    private String amount;
    private String serviceType;
    private String cin;
    private boolean paid;
    private boolean waitingForEpay;
    public EPayWebModel(String id, ServiceType serviceType, String url, BigDecimal amount, String cin, boolean isPaid, boolean waitingForEpay) {
        this.id = id;
        this.url = url;
        this.amount = amount == null ? "" : amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
        this.serviceType = serviceType.getName();
        this.cin = cin;
        this.paid = isPaid;
        this.waitingForEpay = waitingForEpay;
        
    }
    public String getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public String getAmount() {
        return amount;
    }
    public String getServiceType() {
        return serviceType;
    }
    public String getCin() {
        return cin;
    }
    public boolean isPaid() {
        return paid;
    }
    public boolean isWaitingForEpay() {
        return waitingForEpay;
    }
    public String getPaidText() {
        return isPaid() ? "Платено" : "Неплатено";
    }
}
