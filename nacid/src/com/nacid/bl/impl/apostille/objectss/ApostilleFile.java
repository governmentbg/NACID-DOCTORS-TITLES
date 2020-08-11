package com.nacid.bl.impl.apostille.objectss;

/**
 * User: Georgi
 * Date: 1.4.2020 г.
 * Time: 12:29
 */
public class ApostilleFile {
    private byte[] content;
    private String mimeType;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
