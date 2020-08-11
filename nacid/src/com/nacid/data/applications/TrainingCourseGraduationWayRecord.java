package com.nacid.data.applications;


public class TrainingCourseGraduationWayRecord  {
  private int id;
  private int trainingCourseId;
  private Integer graduationWayId;
  private String notes;
  public TrainingCourseGraduationWayRecord(){}
  public TrainingCourseGraduationWayRecord(int id, int trainingCourseId, Integer graduationWayId, String notes) {
    this.id = id;
    this.trainingCourseId = trainingCourseId;
    this.graduationWayId = graduationWayId;
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
  public Integer getGraduationWayId() {
    return graduationWayId;
  }
  public void setGraduationWayId(Integer graduationWayId) {
    this.graduationWayId = graduationWayId;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
}
