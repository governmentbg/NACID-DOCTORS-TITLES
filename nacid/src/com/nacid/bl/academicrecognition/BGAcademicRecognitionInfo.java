package com.nacid.bl.academicrecognition;

import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionExtendedImpl;

import java.util.Date;
import java.util.List;



public interface BGAcademicRecognitionInfo {
    //tipovete syotvetstvat na imenata na kolonite v bazata !!!!
    public static final String TYPE_CITIZENSHIP = "citizenship";
    public static final String TYPE_UNIVERSITY = "university";


    public Integer getId();
    public String getApplicant();
    public Integer getRecognizedUniversityId();
    public String getCitizenship();
    public String getUniversity();
    public String getUniversityCountry();
    public String getEducationLevel();
    public String getDiplomaSpeciality();
    public String getDiplomaNumber();
    public String getDiplomaDate();
    public String getProtocolNumber();
    public String getDenialProtocolNumber();
    public String getRecognizedSpeciality();
    public String getInputNumber();
    public String getOutputNumber();
    public Date getCreatedDate();
    public String getNotes();
    public Integer getRecognitionStatusId();
    public Integer getRelatedRecognitionId();
    public List<BGAcademicRecognitionExtendedImpl> getSimilarRecognitions();
    public boolean getHasSimilar();
}