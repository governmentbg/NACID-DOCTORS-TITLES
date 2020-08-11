package com.nacid.web.model.applications;

public class ExpertStatementAttachmentWebModel {

    private int id;
    private String docDescr;
    private String fileName;
    private int applicationId;

    
    
    public ExpertStatementAttachmentWebModel(int id, String docDescr, String fileName, int applicationId) {
        this.id = id;
        this.docDescr = docDescr;
        this.fileName = fileName;
        this.applicationId = applicationId;
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

}
