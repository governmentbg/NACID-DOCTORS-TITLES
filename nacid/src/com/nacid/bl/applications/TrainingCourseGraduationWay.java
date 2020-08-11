package com.nacid.bl.applications;

public interface TrainingCourseGraduationWay {
  public int getId();
  public int getTrainingCourseId();
  public Integer getGraduationWayId();
  public String getNotes();
  /**
   * @return ako graduationWay == null, vry6ta notes, ako ne - vry6ta graduationWay.getName();
   */
  public String getGraduationWayName();
}
