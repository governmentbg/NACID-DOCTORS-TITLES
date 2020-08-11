package com.nacid.data.regprof.applications;

import com.nacid.bl.applications.regprof.RegprofApplicationDetailsForList;

import java.util.Date;

//RayaWritten-------------------------------------------
public class RegprofApplicationDetailsForListRecord implements RegprofApplicationDetailsForList{
    private Integer id;
    private String appNum;
    private Date appDate;
    private String fullName;
    private Integer status;
    private String statusName;
    private Integer profInstId;
    private String profInstName;
    private Integer qualificationId;
    private String qualificationName;
    private Integer extSignedDocId;
    private Integer specialityId;
    private String specialityName;
    private Integer serviceTypeId; 
    private Date endDate;
    private String imiCorrespondence;
    private int docflowStatusId;
    private String docflowStatusName;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getAppNum() {
        return appNum;
    }
    public void setAppNum(String appNum) {
        this.appNum = appNum;
    }
    public Date getAppDate() {
        return appDate;
    }
    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public Integer getProfInstId() {
        return profInstId;
    }
    public void setProfInstId(Integer profInstId) {
        this.profInstId = profInstId;
    }
    public String getProfInstName() {
        return profInstName;
    }
    public void setProfInstName(String profInstName) {
        this.profInstName = profInstName;
    }
    public Integer getQualificationId() {
        return qualificationId;
    }
    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }
    public String getQualificationName() {
        return qualificationName;
    }
    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }
    public Integer getExtSignedDocId() {
        return extSignedDocId;
    }
    public void setExtSignedDocId(Integer extSignedDocId) {
        this.extSignedDocId = extSignedDocId;
    }
    public Integer getSpecialityId() {
        return specialityId;
    }
    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }
    public String getSpecialityName() {
        return specialityName;
    }
    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }
    public Integer getServiceTypeId() {
        return serviceTypeId;
    }
    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getImiCorrespondence() {
        return imiCorrespondence;
    }
    public void setImiCorrespondence(String imiCorrespondence) {
        this.imiCorrespondence = imiCorrespondence;
    }

    public int getDocflowStatusId() {
        return docflowStatusId;
    }

    public void setDocflowStatusId(int docflowStatusId) {
        this.docflowStatusId = docflowStatusId;
    }

    public String getDocflowStatusName() {
        return docflowStatusName;
    }

    public void setDocflowStatusName(String docflowStatusName) {
        this.docflowStatusName = docflowStatusName;
    }
}
//---------------------------------------------------------