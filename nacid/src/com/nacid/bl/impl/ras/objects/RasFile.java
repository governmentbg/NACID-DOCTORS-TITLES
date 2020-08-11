package com.nacid.bl.impl.ras.objects;

/**
 * User: Georgi
 * Date: 2.3.2020 г.
 * Time: 16:49
 */
public class RasFile {
    private byte[] content;
    private String fileName;
    private String mimeType;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
