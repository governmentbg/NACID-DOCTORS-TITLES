package com.nacid.bl.applications;

import java.io.InputStream;
import java.util.Date;

import com.nacid.bl.nomenclatures.DocumentType;

public interface Attachment {

    public String getScannedContentType();

    public String getScannedFileName();

    public InputStream getScannedContentStream();

    public int getId();

    public int getParentId();

    public String getDocDescr();

    public String getContentType();

    public int getDocTypeId();

    public String getFileName();

    public InputStream getContentStream();

    public Date getDocflowDate();

    public int getCopyTypeId();

    public String getDocflowId();

    public DocumentType getDocType();

    public String getDocflowNum();

}
