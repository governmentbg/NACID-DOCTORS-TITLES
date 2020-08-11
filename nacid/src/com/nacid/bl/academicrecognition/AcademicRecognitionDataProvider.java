package com.nacid.bl.academicrecognition;

import java.util.List;

import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionExtendedImpl;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl;


public interface AcademicRecognitionDataProvider {
    public BGAcademicRecognitionInfoImpl saveBGAcademicRecognitionRecord(BGAcademicRecognitionInfoImpl info);
    public BGAcademicRecognitionInfoImpl getAcademicRecognition(int id);
    public List<BGAcademicRecognitionInfoImpl> getAcademicRecognitions();
    public void deleteBGAcademicRecognitionRecord(int id);
    public List<BGAcademicRecognitionExtendedImpl> getAcademicReconitionExtended();
    public List<BGAcademicRecognitionExtendedImpl> getSimilarAcademicRecognitions(String applicant, String university,
    		String universityCountry, String oks, String diplomaSpeciality, Integer withoutInteger);
}
