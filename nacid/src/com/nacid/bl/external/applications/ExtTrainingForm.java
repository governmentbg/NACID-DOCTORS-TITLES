package com.nacid.bl.external.applications;

public interface ExtTrainingForm {

    public Integer getTrainingFormId();

    public int getTrainingCourseId();

    public int getId();

    public String getNotes();
    
    /**
     * tozi method vry6ta notes, ako trainingFormId == null, ili trainingForm.getName(), ako trainingFormId != null
     * @return
     */
    public String getTrainingFormName();

    

}
