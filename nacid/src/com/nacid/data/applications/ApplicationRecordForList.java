package com.nacid.data.applications;

import com.nacid.bl.applications.ApplicationForList;

import java.sql.Date;
import java.util.List;

public class ApplicationRecordForList implements ApplicationForList {
    private int id;
    private String appNum;
    private Date appDate;
    private String aptName;
    private String uniName;
    private String uniCountryName;
    private String specialityName;
    private String apnStatusName;
    private Integer apnStatusId;
    private Integer extSignedDocId;
    private String docflowStatusName;
    private Integer docflowStatusId;
    private List<Integer> entryNumSeries;
    private List<Integer> commissionSessions;
    private int expertsProcessedStatus;
    private int expertsCount;
    private String recognizedProfGroupName;
    private String recognizedQualificationName;
    private String eduLevelName;
    public int getId() {
        return id;
    }
    public void setId(int id) {
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
    public String getAptName() {
        return aptName;
    }
    public void setAptName(String aptName) {
        this.aptName = aptName;
    }
    public String getUniName() {
        return uniName;
    }
    public void setUniName(String uniName) {
        this.uniName = uniName;
    }
    public String getUniCountryName() {
        return uniCountryName;
    }
    public void setUniCountryName(String uniCountryName) {
        this.uniCountryName = uniCountryName;
    }
    public String getSpecialityName() {
        return specialityName;
    }
    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }
    public String getApnStatusName() {
        return apnStatusName;
    }
    public void setApnStatusName(String apnStatusName) {
        this.apnStatusName = apnStatusName;
    }
    public Integer getApnStatusId() {
        return apnStatusId;
    }
    public void setApnStatusId(Integer apnStatusId) {
        this.apnStatusId = apnStatusId;
    }
    public Integer getExtSignedDocId() {
        return extSignedDocId;
    }
    public void setExtSignedDocId(Integer extSignedDocId) {
        this.extSignedDocId = extSignedDocId;
    }
    public Boolean isExternalApplicationEsigned() {
        return getExtSignedDocId() != null;
    }

    public String getDocflowStatusName() {
        return docflowStatusName;
    }

    public void setDocflowStatusName(String docflowStatusName) {
        this.docflowStatusName = docflowStatusName;
    }

    public Integer getDocflowStatusId() {
        return docflowStatusId;
    }

    public void setDocflowStatusId(Integer docflowStatusId) {
        this.docflowStatusId = docflowStatusId;
    }

    public List<Integer> getEntryNumSeries() {
        return entryNumSeries;
    }

    public void setEntryNumSeries(List<Integer> entryNumSeries) {
        this.entryNumSeries = entryNumSeries;
    }

    public int getExpertsProcessedStatus() {
        return expertsProcessedStatus;
    }

    public void setExpertsProcessedStatus(int expertsProcessedStatus) {
        this.expertsProcessedStatus = expertsProcessedStatus;
    }

    public int getExpertsCount() {
        return expertsCount;
    }

    public void setExpertsCount(int expertsCount) {
        this.expertsCount = expertsCount;
    }

    public List<Integer> getCommissionSessions() {
        return commissionSessions;
    }

    public void setCommissionSessions(List<Integer> commissionSessions) {
        this.commissionSessions = commissionSessions;
    }

    @Override
    public String getEduLevelName() {
        return eduLevelName;
    }

    public void setEduLevelName(String eduLevelName) {
        this.eduLevelName = eduLevelName;
    }

    public String getRecognizedProfGroupName() {
        return recognizedProfGroupName;
    }

    public void setRecognizedProfGroupName(String recognizedProfGroupName) {
        this.recognizedProfGroupName = recognizedProfGroupName;
    }

    public String getRecognizedQualificationName() {
        return recognizedQualificationName;
    }

    public void setRecognizedQualificationName(String recognizedQualificationName) {
        this.recognizedQualificationName = recognizedQualificationName;
    }
}
