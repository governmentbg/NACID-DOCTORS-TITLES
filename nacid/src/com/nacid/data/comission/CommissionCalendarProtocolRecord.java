package com.nacid.data.comission;

import java.io.InputStream;

public class CommissionCalendarProtocolRecord {

    private int id;
    private InputStream content;
    private String contentType;
    private String fileName;
    public CommissionCalendarProtocolRecord(int id, InputStream content, String contentType, String fileName) {
        this.id = id;
        this.content = content;
        this.contentType = contentType;
        this.fileName = fileName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public InputStream getContent() {
        return content;
    }
    public void setContent(InputStream content) {
        this.content = content;
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
    
    
}
