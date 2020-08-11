package com.nacid.data.regprof.applications;


import java.util.UUID;

public class RegprofCertificateNumberToAttachedDocRecord {
    
    private int id;
    private String certificateNumber;
    private Integer attachedDocId;
    private int applicationId;
    private int invalidated;
    private UUID uuid;
    
    public RegprofCertificateNumberToAttachedDocRecord() {
        
    }

    public RegprofCertificateNumberToAttachedDocRecord(int id, String certificateNumber, Integer attachedDocId, int applicationId, int invalidated, UUID uuid) {
        this.id = id;
        this.certificateNumber = certificateNumber;
        this.attachedDocId = attachedDocId;
        this.applicationId = applicationId;
        this.invalidated = invalidated;
        this.uuid = uuid;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCertificateNumber() {
        return certificateNumber;
    }
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
    public Integer getAttachedDocId() {
        return attachedDocId;
    }
    public void setAttachedDocId(Integer attachedDocId) {
        this.attachedDocId = attachedDocId;
    }
    public int getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }
    public int getInvalidated() {
        return invalidated;
    }
    public void setInvalidated(int invalidated) {
        this.invalidated = invalidated;
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
