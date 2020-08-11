package com.nacid.bl.external.applications;

public interface ExtGraduationWay {

    public Integer getGraduationWayId();

    public int getTrainingCourseId();

    public int getId();

    public String getNotes();
    /**
     * @return ako graduationWay == null, vry6ta notes, ako ne - vry6ta graduationWay.getName();
     */
    public String getGraduationWayName();
}
