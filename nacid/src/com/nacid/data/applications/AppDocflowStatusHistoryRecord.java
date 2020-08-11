package com.nacid.data.applications;


import com.nacid.data.annotations.Table;

import java.sql.Date;
@Table(name="backoffice.app_status_docflow_history", sequence = "backoffice.app_status_docflow_history_id_seq")
public class AppDocflowStatusHistoryRecord {

    private int id;
    private int applicationId;
    private int appStatusDocflowId;
    private Date dateAssigned;
    private int userAssigned;


    public AppDocflowStatusHistoryRecord(){

    }

    public AppDocflowStatusHistoryRecord(int id, int applicationId, int appStatusDocflowId, Date dateAssigned, int userAssigned) {
        this.id = id;
        this.applicationId = applicationId;
        this.appStatusDocflowId = appStatusDocflowId;
        this.dateAssigned = dateAssigned;
        this.userAssigned = userAssigned;
    }

   	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getAppStatusDocflowId() {
        return appStatusDocflowId;
    }

    public void setAppStatusDocflowId(int appStatusDocflowId) {
        this.appStatusDocflowId = appStatusDocflowId;
    }

    public Date getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

	public int getUserAssigned() {
        return userAssigned;
    }

    public void setUserAssigned(int userAssigned) {
        this.userAssigned = userAssigned;
    }
    
    
}
