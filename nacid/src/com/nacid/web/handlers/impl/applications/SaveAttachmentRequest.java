package com.nacid.web.handlers.impl.applications;

import com.nacid.data.DataConverter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SaveAttachmentRequest {
    private int id = 0;
    private int applicationId = 0;
    private String docDescr = null;
    private Integer docTypeId = null;
    private String contentType = null;
    private InputStream is = null;
    private String fileName = null;
    private int copyTypeId = 0;
    private int fileSize = 0;
    private String certificateNumber = null;
    private InputStream scannedIs = null;
    private String scannedContentType = null;
    private String scannedFileName = null;
    private int scannedFileSize = 0;

    private String docflowUrl = null;
    private String docflowNum = null;

    private Integer eventStatus = null;
    private Integer eventType = null;

    private boolean generate = false;

    public SaveAttachmentRequest(List uploadedItems, HttpServletRequest request, String suffix) throws FileUploadException, IOException {
        suffix = suffix == null ? "" : suffix;

        Iterator iter = uploadedItems.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();

            if (item.isFormField()) {

                if (item.getFieldName().equals("id" + suffix))
                    id = DataConverter.parseInt(item.getString("UTF-8"), 0);
                else if (item.getFieldName().equals("applicationId"))
                    applicationId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                else if (item.getFieldName().equals("docDescr" + suffix))
                    docDescr = item.getString("UTF-8");
                else if (item.getFieldName().equals("docTypeId" + suffix))
                    docTypeId = DataConverter.parseInteger(item.getString("UTF-8"), null);
                else if (item.getFieldName().equals("copyTypeId" + suffix))
                    copyTypeId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                else if (item.getFieldName().equals("docflowNum" + suffix))
                    docflowNum = item.getString("UTF-8");
                else if (item.getFieldName().equals("generate")) {//generate button-a e samo edin i nqma suffix
                    generate = DataConverter.parseBoolean(item.getString("UTF-8"));
                } else if (item.getFieldName().equals("docflowUrl" + suffix)) {
                    docflowUrl = item.getString("UTF-8");
                } else if (item.getFieldName().equals("eventStatus" + suffix)) {
                    eventStatus = DataConverter.parseInteger(item.getString("UTF-8"), null);
                } else if (item.getFieldName().equals("eventType" + suffix)) {
                    eventType = DataConverter.parseInteger(item.getString("UTF-8"), null);
                } else if (item.getFieldName().equals("certNumber" + suffix)) {
                    certificateNumber = DataConverter.parseString(item.getString("UTF-8"), null);
                    if (!StringUtils.isEmpty(certificateNumber)) {
                        if (!certificateNumber.contains("/")) {
                            certificateNumber += "/" + DataConverter.formatDate(new Date());
                        }
                    }
                }

            } else {
                if (item.getFieldName().equals("doc_content" + suffix)) {
                    fileSize = (int) item.getSize();
                    if (fileSize > 0) {
                        is = item.getInputStream();
                        fileName = prepareFileName(item.getName());
                        contentType = item.getContentType();
                    }
                } else if (item.getFieldName().equals("scanned_content" + suffix)) {
                    scannedFileSize = (int) item.getSize();
                    if (scannedFileSize > 0) {
                        scannedIs = item.getInputStream();
                        scannedFileName = prepareFileName(item.getName());
                        scannedContentType = item.getContentType();
                    }
                }
            }
        }
    }

    public static String prepareFileName(String fileName) {
        int index;
        if ((index = fileName.lastIndexOf('\\')) != -1) {
            fileName = fileName.substring(index + 1, fileName.length());
        }
        if ((index = fileName.lastIndexOf('/')) != -1) {
            fileName = fileName.substring(index + 1, fileName.length());
        }
        if (fileName.equals("")) {
            fileName = "file";
        }
        return fileName;
    }

    public int getId() {
        return id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getDocDescr() {
        return docDescr;
    }

    public Integer getDocTypeId() {
        return docTypeId;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getIs() {
        return is;
    }

    public String getFileName() {
        return fileName;
    }

    public int getCopyTypeId() {
        return copyTypeId;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public InputStream getScannedIs() {
        return scannedIs;
    }

    public String getScannedContentType() {
        return scannedContentType;
    }

    public String getScannedFileName() {
        return scannedFileName;
    }

    public int getScannedFileSize() {
        return scannedFileSize;
    }

    public String getDocflowUrl() {
        return docflowUrl;
    }

    public String getDocflowNum() {
        return docflowNum;
    }

    public Integer getEventStatus() {
        return eventStatus;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }
    public boolean isGenerate() {
        return generate;
    }
}
