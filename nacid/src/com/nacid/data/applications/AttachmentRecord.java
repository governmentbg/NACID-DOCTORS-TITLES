package com.nacid.data.applications;

import java.io.InputStream;
import java.sql.Date;

public class AttachmentRecord {

	private int id;
	private int parentId;
	private String docDescr;
	private Integer docTypeId;
	private String contentType;
	private String fileName;
	private InputStream contentStream;
	private Integer copyTypeId;

    private String docflowId;
    private Date docflowDate;
    

    private String scannedContentType;
    private String scannedFileName;
    private InputStream scannedContentStream;

    public AttachmentRecord() {
    }

    public AttachmentRecord(int id, int parentId, String docDescr, Integer docTypeId, String contentType, String fileName, InputStream contentStream, Integer copyTypeId, String docflowId, Date docflowDate, String scannedContentType, String scannedFileName, InputStream scannedContentStream) {
        this.id = id;
        this.parentId = parentId;
        this.docDescr = docDescr;
        this.docTypeId = docTypeId;
        this.contentType = contentType;
        this.fileName = fileName;
        this.contentStream = contentStream;
        this.copyTypeId = copyTypeId;
        this.docflowId = docflowId;
        this.docflowDate = docflowDate;
        this.scannedContentType = scannedContentType;
        this.scannedFileName = scannedFileName;
        this.scannedContentStream = scannedContentStream;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public String getDocDescr() {
        return docDescr;
    }
    public void setDocDescr(String docDescr) {
        this.docDescr = docDescr;
    }
    public Integer getDocTypeId() {
        return docTypeId;
    }
    public void setDocTypeId(Integer docTypeId) {
        this.docTypeId = docTypeId;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public InputStream getContentStream() {
        return contentStream;
    }
    public void setContentStream(InputStream contentStream) {
        this.contentStream = contentStream;
    }
    public Integer getCopyTypeId() {
        return copyTypeId;
    }
    public void setCopyTypeId(Integer copyTypeId) {
        this.copyTypeId = copyTypeId;
    }
    public String getDocflowId() {
        return docflowId;
    }
    public void setDocflowId(String docflowId) {
        this.docflowId = docflowId;
    }
    public Date getDocflowDate() {
        return docflowDate;
    }
    public void setDocflowDate(Date docflowDate) {
        this.docflowDate = docflowDate;
    }
    public String getScannedContentType() {
        return scannedContentType;
    }
    public void setScannedContentType(String scannedContentType) {
        this.scannedContentType = scannedContentType;
    }
    public String getScannedFileName() {
        return scannedFileName;
    }
    public void setScannedFileName(String scannedFileName) {
        this.scannedFileName = scannedFileName;
    }
    public InputStream getScannedContentStream() {
        return scannedContentStream;
    }
    public void setScannedContentStream(InputStream scannedContentStream) {
        this.scannedContentStream = scannedContentStream;
    }

	

}
