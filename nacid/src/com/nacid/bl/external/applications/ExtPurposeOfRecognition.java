package com.nacid.bl.external.applications;

public interface ExtPurposeOfRecognition {

    public Integer getPurposeOfRecognitionId();

    public int getApplicationId();

    public int getId();

    public String getNotes();
    /**
     * ako getPurposeOfRecognitionId != null vry6ta imeto na ploskata nomenklatura, 
     * inache vry6ta notes
     * @return
     */
    public String getRecognitionPurposeNotes();

}
