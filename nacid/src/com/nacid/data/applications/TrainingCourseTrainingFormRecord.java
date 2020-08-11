package com.nacid.data.applications;


public class TrainingCourseTrainingFormRecord {
  private int id;
  private int trainingCourseId;
  private Integer trainingFormId;
  private String notes;
  public TrainingCourseTrainingFormRecord() {
  }
  public TrainingCourseTrainingFormRecord(int id, int trainingCourseId, Integer trainingFormId, String notes) {
    this.id = id;
    this.trainingCourseId = trainingCourseId;
    this.trainingFormId = trainingFormId;
    this.notes = notes;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getTrainingCourseId() {
    return trainingCourseId;
  }
  public void setTrainingCourseId(int trainingCourseId) {
    this.trainingCourseId = trainingCourseId;
  }
  public Integer getTrainingFormId() {
    return trainingFormId;
  }
  public void setTrainingFormId(Integer trainingFormId) {
    this.trainingFormId = trainingFormId;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
}
