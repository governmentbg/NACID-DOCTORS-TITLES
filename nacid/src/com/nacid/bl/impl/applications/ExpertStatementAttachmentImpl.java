package com.nacid.bl.impl.applications;

import java.io.InputStream;

import com.nacid.bl.applications.ExpertStatementAttachment;
import com.nacid.data.applications.ExpertStatementAttachmentRecord;

public class ExpertStatementAttachmentImpl implements ExpertStatementAttachment {

    private int id;
    private int applicationId;
    private int expertId;
    private String docDescr;
    private int docTypeId;
    private InputStream contentStream;
    private String fileName;
    private String contentType;
    
    public ExpertStatementAttachmentImpl(int id, int applicationId, int expertId, String docDescr, int docTypeId, InputStream contentStream,
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
    
    public ExpertStatementAttachmentImpl(ExpertStatementAttachmentRecord rec) {
        this.id = rec.getId();
        this.applicationId = rec.getApplicationId();
        this.expertId = rec.getExpertId();
        this.docDescr = rec.getDocDescr();
        this.docTypeId = rec.getDocTypeId();
        this.contentStream = rec.getContentStream();
        this.fileName = rec.getFileName();
        this.contentType = rec.getContentType();
    }

    @Override
    public int getApplicationId() {
        return applicationId;
    }

    @Override
    public InputStream getContentStream() {
        return contentStream;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getDocDescr() {
        return docDescr;
    }

    @Override
    public int getDocTypeId() {
        return docTypeId;
    }

    @Override
    public int getExpertId() {
        return expertId;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public int getId() {
        return id;
    }
}
