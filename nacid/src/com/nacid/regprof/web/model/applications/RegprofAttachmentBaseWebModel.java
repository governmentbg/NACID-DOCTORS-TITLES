package com.nacid.regprof.web.model.applications;

import org.apache.commons.lang.StringUtils;
//RayaWritten------------------------------------
public class RegprofAttachmentBaseWebModel {
    private int id;
    private String docDescr = "";
    private String fileName = "";
    private String scannedFileName;
    
    private String docflowNum;
    private String docflowUrl;
    
    public RegprofAttachmentBaseWebModel(){}
    public RegprofAttachmentBaseWebModel(int id,  String docDescr, String fileName, String scannedFileName,
            String docflowNum, String docflowUrl) {
        this.id = id;
        this.docDescr = docDescr;
        this.fileName = fileName;
        this.scannedFileName = scannedFileName;
        this.docflowNum = StringUtils.trimToNull(docflowNum);
        this.docflowUrl = docflowUrl;
    }

    public int getId() {
        return id;
    }

    public String getDocDescr() {
        return docDescr;
    }
    
    public String getFileName() {
        return fileName;
    }
    public String getScannedFileName() {
        return scannedFileName;
    }
    public String getDocflowNum() {
        return docflowNum;
    }
    public String getDocflowUrl() {
        return docflowUrl;
    }
}
//------------------------------------------------
