package com.nacid.bl.impl.applications;

import java.sql.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.UniversityExamination;
import com.nacid.bl.applications.UniversityValidity;
import com.nacid.bl.applications.UniversityValidityDataProvider;
import com.nacid.data.applications.UniversityExaminationRecord;

public class UniversityExaminationImpl implements UniversityExamination{
    private int id;
    private int trainingCourseId;
    private int universityValidityId;
    private int userId;
    private Date examinationDate;
    private boolean isRecognized;
    private String notes;
    private NacidDataProvider nacidDataProvider;
    public UniversityExaminationImpl(UniversityExaminationRecord record, NacidDataProvider nacidDataProvider) {
        this.id = record.getId();
        this.trainingCourseId = record.getTrainingCourseId();
        this.universityValidityId = record.getUniversityValidityId();
        this.userId = record.getUserId();
        this.examinationDate = record.getExaminationDate();
        this.isRecognized = record.isRecognized();
        this.notes = record.getNotes();
        this.nacidDataProvider = nacidDataProvider;
    }
    public int getId() {
        return id;
    }
    public int getTrainingCourseId() {
        return trainingCourseId;
    }
    public int getUniversityValidityId() {
        return universityValidityId;
    }
    public UniversityValidity getUniversityValidity() {
    	UniversityValidityDataProvider universityValidityDataProvider = nacidDataProvider.getUniversityValidityDataProvider();
    	return universityValidityDataProvider.getUniversityValidity(getUniversityValidityId());
    }
    public int getUserId() {
        return userId;
    }
    public Date getExaminationDate() {
        return examinationDate;
    }
    
    public String getNotes() {
        return notes;
    }
    public boolean isRecognized() {
        return isRecognized;
    }
    
    
}
