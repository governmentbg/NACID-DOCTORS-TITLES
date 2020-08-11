package com.nacid.data.applications;


public class ApplicationRecognitionPurposeRecord {
  private int id;
  private int applicationId;
  private Integer recognitionPurposeId;
  private String notes;
  public ApplicationRecognitionPurposeRecord(){}
  public ApplicationRecognitionPurposeRecord(int id, int applicationId, Integer recognitionPurposeId, String notes) {
    this.id = id;
    this.applicationId = applicationId;
    this.recognitionPurposeId = recognitionPurposeId;
    this.notes = notes;
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
  public Integer getRecognitionPurposeId() {
    return recognitionPurposeId;
  }
  public void setRecognitionPurposeId(Integer recognitionPurposeId) {
    this.recognitionPurposeId = recognitionPurposeId;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
}
