package com.nacid.bl.impl.applications.regprof;

import java.io.InputStream;
import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.applications.RegprofApplicationAttachmentRecord;

public class RegprofApplicationAttachmentImpl implements RegprofApplicationAttachment {
    private int id;
    private int parentId;
    private String docDescr;
    private int docTypeId;
    private String contentType;
    private String fileName;
    private InputStream contentStream;
    private int copyTypeId;
    private String docflowId;
    private Date docflowDate;
    private String docflowNum;
    private String scannedContentType;
    private String scannedFileName;
    private InputStream scannedContentStream;
    private NacidDataProvider nacidDataProvider;

    RegprofApplicationAttachmentImpl(RegprofApplicationAttachmentRecord rec, NacidDataProvider nacidDataProvider) {

        this.id = rec.getId();
        this.parentId = rec.getParentId();
        this.docDescr = rec.getDocDescr();
        this.docTypeId = rec.getDocTypeId() != null ? rec.getDocTypeId() : 0;
        this.contentType = rec.getContentType();
        this.fileName = rec.getFileName();
        this.contentStream = rec.getContentStream();
        this.copyTypeId = rec.getCopyTypeId() != null ? rec.getCopyTypeId() : 0;
        this.docflowId = rec.getDocflowId();
        this.docflowDate = rec.getDocflowDate();
        this.scannedContentStream = rec.getScannedContentStream();
        this.scannedContentType = rec.getScannedContentType();
        this.scannedFileName = rec.getScannedFileName();
        this.docflowNum = generateDocFlowNum(docflowId, docflowDate);
        this.nacidDataProvider = nacidDataProvider;
    }

    @Override
    public String getScannedContentType() {
        return scannedContentType;
    }

    @Override
    public String getScannedFileName() {
        return scannedFileName;
    }

    @Override
    public InputStream getScannedContentStream() {
        return scannedContentStream;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getParentId() {
        return parentId;
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
    public DocumentType getDocType() {
        return nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(getDocTypeId());
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public InputStream getContentStream() {
        return contentStream;
    }

    @Override
    public int getCopyTypeId() {
        return copyTypeId;
    }

    @Override
    public Date getDocflowDate() {
        return docflowDate;
    }

    @Override
    public String getDocflowId() {
        return docflowId;
    }

    @Override
    public String getDocflowNum() {
        return docflowNum;
    }
    public static String generateDocFlowNum(String docflowId, Date docflowDate) {
        if ((docflowId == null || docflowId.equals("")) && docflowDate == null) {
            return "";
        }
        if (docflowId == null || docflowId.equals("")) {
            return DataConverter.formatDate(docflowDate);
        }
        if (docflowDate == null) {
            return docflowId;
        }
        return docflowId + "/" + DataConverter.formatDate(docflowDate);
        
    }   
}
