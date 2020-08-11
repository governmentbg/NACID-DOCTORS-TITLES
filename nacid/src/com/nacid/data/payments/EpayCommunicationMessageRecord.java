package com.nacid.data.payments;

import java.sql.Timestamp;

import com.nacid.data.annotations.Table;
@Table(name="epay_messages")
public class EpayCommunicationMessageRecord {
    private int id;
    private Timestamp date;
    private String inOut;
    private String content;
    private String correlationId;
    private String checksum;
    public EpayCommunicationMessageRecord() {
    }
    public EpayCommunicationMessageRecord(int id, Timestamp date, String inOut,
            String content, String correlationId, String checksum) {
        this.id = id;
        this.date = date;
        this.inOut = inOut;
        this.content = content;
        this.correlationId = correlationId;
        this.checksum = checksum;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public String getInOut() {
        return inOut;
    }
    public void setInOut(String inOut) {
        this.inOut = inOut;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
    public String getChecksum() {
        return checksum;
    }
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    
    
}
