package com.nacid.data.regprof.external.applications;

import com.nacid.bl.regprof.external.ExtRegprofApplicationForList;

import java.util.Date;

public class ExtRegprofApplicationForListRecord implements ExtRegprofApplicationForList{
    private int id;
    private String applicantName;
    private Date date;
    private String docflowNumber;
    //private String statusName;
    private int statusId;
    private Integer internalStatusId;
    private boolean esigned;
    private String finalStatusName;
    private int communicated;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getApplicantName() {
        return applicantName;
    }
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getDocflowNumber() {
        return docflowNumber;
    }
    public void setDocflowNumber(String docflowNumber) {
        this.docflowNumber = docflowNumber;
    }
    /*public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }*/
    public boolean isEsigned() {
        return esigned;
    }
    public void setEsigned(boolean esigned) {
        this.esigned = esigned;
    }
    public int getStatusId() {
        return statusId;
    }
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
    public Integer getInternalStatusId() {
        return internalStatusId;
    }
    public void setInternalStatusId(Integer internalStatusId) {
        this.internalStatusId = internalStatusId;
    }

    public String getFinalStatusName() {
        return finalStatusName;
    }

    public void setFinalStatusName(String finalStatusName) {
        this.finalStatusName = finalStatusName;
    }

    public int getCommunicated() {
        return communicated;
    }

    public void setCommunicated(int communicated) {
        this.communicated = communicated;
    }
    public boolean isCommunicated() {
        return communicated == 1;
    }
}
