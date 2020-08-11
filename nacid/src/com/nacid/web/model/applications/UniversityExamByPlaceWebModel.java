package com.nacid.web.model.applications;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingCourse;
import com.nacid.bl.applications.UniversityValidity;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.web.taglib.FormInputUtils;

public class UniversityExamByPlaceWebModel {
	private List<UniversityExamByPlaceUniversityWebModel> universities;
    private List<UniversityExamByPlaceTrainingLocationWebModel> trainingLocations = new ArrayList<UniversityExamByPlaceTrainingLocationWebModel>();
    private String recognized;
    private boolean examinationAvailable;
    public UniversityExamByPlaceWebModel(ApplicationWebModel application, TrainingCourse tc, NacidDataProvider nacidDataProvider) {
    	this.examinationAvailable = tc.isRecognizedByHeadQuarter();
    	//Ako ne trqbva da se vijda examination by place frame-a, nqma nujda da se pylnqt i webmodelite
    	if (!examinationAvailable) {
    		return;
    	}
    	TrainingCourseWebModel tcwm = application.getTrainingCourseWebModel();
    	List<UniversityWithFacultyWebModel> universities = tcwm.getAllUniversityWithFaculties();
        if (universities != null) {
        	this.universities = new ArrayList<>();
        	for (UniversityWithFacultyWebModel uf:universities) {
				UniversityWebModel u = uf.getUniversity();
        		UniversityValidity uv = tc.getUniversityValidity(u.getIntId());
            	if (uv != null) {
            		FlatNomenclature trainingLocation = uv.getTrainingLocation();
            		this.universities.add(new UniversityExamByPlaceUniversityWebModel(u, uv.isHasJoinedDegrees(), trainingLocation == null ? "" : trainingLocation.getName()));
            	}
            }	
        }
        List<TrainingCourseTrainingLocationWebModel> tLocs = tcwm.getTrainingLocations();
        if (tLocs != null) {
        	for (TrainingCourseTrainingLocationWebModel tl:tLocs) {
        		this.trainingLocations.add(new UniversityExamByPlaceTrainingLocationWebModel(tl, tl.getTrainingInstitutionId()));
        	}
        }
        this.recognized = FormInputUtils.getCheckBoxCheckedText(tc.isRecognized());
    }
    public boolean isExaminationAvailable() {
        return examinationAvailable;
    }
    public String getRecognized() {
        return recognized;
    }

    public List<UniversityExamByPlaceUniversityWebModel> getUniversities() {
		return universities;
	}

	public List<UniversityExamByPlaceTrainingLocationWebModel> getTrainingLocations() {
		return trainingLocations;
	}
    
    
}
