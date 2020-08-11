package com.nacid.bl.applications;

import com.nacid.bl.nomenclatures.FlatNomenclature;

public interface TrainingCourseTrainingForm {
  public int getId();
  public int getTrainingCourseId();
  public Integer getTrainingFormId();
  public FlatNomenclature getTrainingForm();
  public String getNotes();
  /**
   * tozi method vry6ta notes, ako trainingFormId == null, ili trainingForm.getName(), ako trainingFormId != null
   * @return
   */
  public String getTrainingFormName();
}
