package com.nacid.data.regprof.applications;

import com.nacid.data.DataConverter;

import java.util.Date;
//RayaWritten----------------------------------------
public class RegprofAppStatusHistoryForListRecord {
    private Integer id;
    private Date dateAssigned;
    private Integer statusId;
    private String statusName;
    private int isLegalStatus;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getDateAssigned() {
        return dateAssigned;
    }
    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }
    public Integer getStatusId() {
        return statusId;
    }
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getDateAssignedFormatted(){
        return DataConverter.formatDate(dateAssigned);
    }

    public int getIsLegalStatus() {
        return isLegalStatus;
    }

    public void setIsLegalStatus(int isLegalStatus) {
        this.isLegalStatus = isLegalStatus;
    }
    public boolean isLegalStatus() {
        return isLegalStatus == 1;
    }
}
//-----------------------------------------------------