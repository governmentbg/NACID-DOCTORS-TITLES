package com.ext.nacid.web.model.expert;

public class ExpertStatementWebModel {

    private int id;
    private String docDescr;
    private String fileName;
    private int applicationId;

    
    
    public ExpertStatementWebModel(int id, String docDescr, String fileName, int applicationId) {
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
