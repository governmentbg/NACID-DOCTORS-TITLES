package com.nacid.regprof.web.model.applications;

import java.util.List;

public class RegprofApplicationAttachmentWebModel {

    private int id;
    private int applicationId;
    private String docDescr = "";
    private String fileName = "";
    private String scannedFileName;
    
    private String docflowNum;
    private String docflowUrl;
    
    private List<Integer> appStatusesToAllowCertNumberAssign;

    public RegprofApplicationAttachmentWebModel(int applicationId, List<Integer> appStatusesToAllowCertNumberAssign) {
        this.id = 0;
        this.applicationId = applicationId;
        this.docDescr = "";
        this.fileName = "";
        this.scannedFileName = null;
        this.docflowNum = null;
        this.docflowUrl = null;
        this.appStatusesToAllowCertNumberAssign = appStatusesToAllowCertNumberAssign;
    }
    
    public RegprofApplicationAttachmentWebModel(int id, int applicationId, String docDescr, String fileName, String scannedFileName, String docflowNum, String docflowUrl) {
        this.id = id;
        this.applicationId = applicationId;
        this.docDescr = docDescr;
        this.fileName = fileName;
        this.scannedFileName = scannedFileName;
        this.docflowNum = docflowNum;
        this.docflowUrl = docflowUrl;
        this.appStatusesToAllowCertNumberAssign = null;
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

    public int getApplicationId() {
        return applicationId;
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
    
    public List<Integer> getAppStatusesToAllowCertNumberAssign() {
        return appStatusesToAllowCertNumberAssign;
    }
}