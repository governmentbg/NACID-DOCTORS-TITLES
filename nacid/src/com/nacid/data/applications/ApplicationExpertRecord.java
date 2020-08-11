package com.nacid.data.applications;

public class ApplicationExpertRecord {
    private int id;
    private int applicationId;
    private int expertId;
    private String notes;
    private int processStat;

    private String courseContent;
    private Integer expertPosition;
    private Integer legalReasonId;
    private Integer qualificationId;
    private Integer eduLevelId;
    private String previousBoardDecisions;
    private String similarBulgarianPrograms;

    public ApplicationExpertRecord() {
    }

    public ApplicationExpertRecord(int id, int applicationId, int expertId, String notes, int processStat, String courseContent, Integer expertPosition, Integer legalReasonId, Integer qualificationId, Integer eduLevelId, String previousBoardDecisions, String similarBulgarianPrograms) {
        this.id = id;
        this.applicationId = applicationId;
        this.expertId = expertId;
        this.notes = notes;
        this.processStat = processStat;
        this.courseContent = courseContent;
        this.expertPosition = expertPosition;
        this.legalReasonId = legalReasonId;
        this.qualificationId = qualificationId;
        this.eduLevelId = eduLevelId;
        this.previousBoardDecisions = previousBoardDecisions;
        this.similarBulgarianPrograms = similarBulgarianPrograms;
    }

    public int getId() {
        return id;
    }
    public int getApplicationId() {
        return applicationId;
    }
    public int getExpertId() {
        return expertId;
    }
    public String getNotes() {
        return notes;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }
    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public int getProcessStat() {
        return processStat;
    }
    public void setProcessStat(int processStat) {
        this.processStat = processStat;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public Integer getExpertPosition() {
        return expertPosition;
    }

    public void setExpertPosition(Integer expertPosition) {
        this.expertPosition = expertPosition;
    }

    public Integer getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }

    public Integer getEduLevelId() {
        return eduLevelId;
    }

    public void setEduLevelId(Integer eduLevelId) {
        this.eduLevelId = eduLevelId;
    }

    public String getPreviousBoardDecisions() {
        return previousBoardDecisions;
    }

    public void setPreviousBoardDecisions(String previousBoardDecisions) {
        this.previousBoardDecisions = previousBoardDecisions;
    }

    public String getSimilarBulgarianPrograms() {
        return similarBulgarianPrograms;
    }

    public void setSimilarBulgarianPrograms(String similarBulgarianPrograms) {
        this.similarBulgarianPrograms = similarBulgarianPrograms;
    }

    public Integer getLegalReasonId() {
        return legalReasonId;
    }

    public void setLegalReasonId(Integer legalReasonId) {
        this.legalReasonId = legalReasonId;
    }
}
