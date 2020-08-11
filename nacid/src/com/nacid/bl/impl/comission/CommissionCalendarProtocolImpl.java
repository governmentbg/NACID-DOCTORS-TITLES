package com.nacid.bl.impl.comission;

import java.io.InputStream;

import com.nacid.bl.comision.CommissionCalendarProtocol;
import com.nacid.data.comission.CommissionCalendarProtocolRecord;

public class CommissionCalendarProtocolImpl implements CommissionCalendarProtocol {

    
    
    private InputStream content;
    private String contentType;
    private String fileName;
    private int id;
    
    

    public CommissionCalendarProtocolImpl(int id, InputStream content, String contentType, String fileName) {
        this.id = id;
        this.content = content;
        this.contentType = contentType;
        this.fileName = fileName;
    }
    
    public CommissionCalendarProtocolImpl(CommissionCalendarProtocolRecord rec) {
        this.id = rec.getId();
        this.content = rec.getContent();
        this.contentType = rec.getContentType();
        this.fileName = rec.getFileName();
    }

    @Override
    public InputStream getContent() {
        return content;
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
    public int getId() {
        return id;
    }

}
