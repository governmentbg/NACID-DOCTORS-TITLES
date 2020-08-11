package com.nacid.data.regprof.applications;

import com.nacid.data.annotations.Table;

@Table(name = "regprof.app_status_doc_type")
public class RegprofAppStatusDocTypeRecord {
    private int id;
    private int appStatusId;
    private int docTypeId;
    
    public RegprofAppStatusDocTypeRecord() {
    }
    public RegprofAppStatusDocTypeRecord(int id, int applicationStatusId, int documentTypeId) {
        this.id = id;
        this.appStatusId = applicationStatusId;
        this.docTypeId = documentTypeId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAppStatusId() {
        return appStatusId;
    }
    public void setAppStatusId(int appStatusId) {
        this.appStatusId = appStatusId;
    }
    public int getDocTypeId() {
        return docTypeId;
    }
    public void setDocTypeId(int docTypeId) {
        this.docTypeId = docTypeId;
    }

}