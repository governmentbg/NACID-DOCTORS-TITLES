package com.nacid.bl.impl.applications.regprof;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.applications.regprof.RegprofAppStatusHistory;
import com.nacid.data.DataConverter;
import com.nacid.data.annotations.Table;
//RayaWritten----------------------------------------------------------------
@Table(name="regprof.app_status_history")
public class RegprofAppStatusHistoryImpl implements RegprofAppStatusHistory {
    private Integer id;
    private Integer applicationId;
    private Integer statusId;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date dateAssigned;
    private Integer userAssigned;
    
    
    public RegprofAppStatusHistoryImpl() {}

    public RegprofAppStatusHistoryImpl(Integer id, Integer applicationId,
            Integer statusId, Date dateAssigned,
            Integer startLegalReasonId, Integer sessionId, Integer userAssigned) {
        super();
        this.id = id;
        this.applicationId = applicationId;
        this.statusId = statusId;
        this.dateAssigned = dateAssigned;
        this.userAssigned = userAssigned;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
    public Integer getStatusId() {
        return statusId;
    }
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public Date getDateAssigned() {
        return dateAssigned;
    }
    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }
    public Integer getUserAssigned() {
        return userAssigned;
    }
    public void setUserAssigned(Integer userAssigned) {
        this.userAssigned = userAssigned;
    }
    public String getDateAssignedFormatted(){
        return DataConverter.formatDate(getDateAssigned());
    }
       
}
//-----------------------------------------------------------------------------