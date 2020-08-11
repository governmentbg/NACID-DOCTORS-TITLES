package com.nacid.web.model.comission;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.TrainingCourse;
import com.nacid.bl.nomenclatures.Qualification;
import com.nacid.bl.nomenclatures.Speciality;

public class ApplicationMotivesWebModel {

    private int calendarId;
    private String applicationMotives;
    private int applicationId;
    private List<Speciality>/*<RecognizedSpecsWebModel>*/ recognSpecs; //RayaChanges
    private String recognSpecIds;
    private String applicationHeader;
    private String recognizedQualification;
    private Integer recognizedQualificationId;
    private String applicantInfo;
    private Integer applicationType;
    public ApplicationMotivesWebModel(Application application, int calendarId, String applicationMotives,
            TrainingCourse tc) {
        this.calendarId = calendarId;
        this.applicationMotives = applicationMotives;
        this.applicationId = application.getId();
        
        recognSpecs = new ArrayList<Speciality>()/*<RecognizedSpecsWebModel>()*/; //RayaChanges
        recognSpecIds = "";
        
        
        List<Speciality> recognizedSpecialities = tc.getRecognizedSpecialities();
        if (recognizedSpecialities != null) {
        	for(Speciality s : recognizedSpecialities) {
                recognSpecs.add(s);//new RecognizedSpecsWebModel(s.getId(), s.getName())); //RayaChanges
                recognSpecIds += (recognSpecIds.length() == 0 ? "" : ";") + s.getId();
            }	
        }
        
        applicationHeader = application.getApplicant().getFullName() + " - Деловоден № " + application.getDocFlowNumber();
        String certNumber = application.getCertificateNumber();
        if (!StringUtils.isEmpty(certNumber)) {
        	applicationHeader += "<br />" + "Удостоверение № " + certNumber;
        }
        Qualification recognizedQualification = tc.getRecognizedQualification();
        this.recognizedQualificationId = recognizedQualification == null ? null : recognizedQualification.getId();
        this.recognizedQualification = recognizedQualification == null ? "" : recognizedQualification.getName();
        this.applicantInfo = application.getApplicantInfo();
        this.applicationType = application.getApplicationType();
    }
    public int getCalendarId() {
        return calendarId;
    }
    public String getApplicationMotives() {
        return applicationMotives;
    }
    public int getApplicationId() {
        return applicationId;
    }
    public List<Speciality>/*<RecognizedSpecsWebModel>*/ getRecognSpecs() { //RayaChanges
        return recognSpecs;
    }
    public String getRecognSpecIds() {
        return recognSpecIds;
    }
	public String getApplicationHeader() {
		return applicationHeader;
	}
	public String getRecognizedQualification() {
		return recognizedQualification;
	}
	public Integer getRecognizedQualificationId() {
		return recognizedQualificationId;
	}
	public String getApplicantInfo() {
		return applicantInfo;
	}

    public Integer getApplicationType() {
        return applicationType;
    }
}
