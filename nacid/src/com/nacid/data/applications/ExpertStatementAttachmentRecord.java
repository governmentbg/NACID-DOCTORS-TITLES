package com.nacid.data.applications;

import java.io.InputStream;

public class ExpertStatementAttachmentRecord {

    private int id;
    private int applicationId;
    private int expertId;
    private String docDescr;
    private int docTypeId;
    private InputStream contentStream;
    private String fileName;
    private String contentType;
    
    public ExpertStatementAttachmentRecord(int id, int applicationId, int expertId, String docDescr, int docTypeId, InputStream contentStream,
            String fileName, String contentType) {
        this.id = id;
        this.applicationId = applicationId;
        this.expertId = expertId;
        this.docDescr = docDescr;
        this.docTypeId = docTypeId;
        this.contentStream = contentStream;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getExpertId() {
        return expertId;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    public String getDocDescr() {
        return docDescr;
    }

    public void setDocDescr(String docDescr) {
        this.docDescr = docDescr;
    }

    public int getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(int docTypeId) {
        this.docTypeId = docTypeId;
    }

    public InputStream getContentStream() {
        return contentStream;
    }

    public void setContentStream(InputStream contentStream) {
        this.contentStream = contentStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    
	
}
