package com.nacid.web.model.applications.report.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.applications.TrainingCourseTrainingLocation;
import com.nacid.bl.applications.TrainingInstitution;
import com.nacid.bl.nomenclatures.Country;

public class UniversityExaminationByPlaceInternalForReportWebModel {
	private String trainingLocation;
	private String trainingInstitution;
	
	public UniversityExaminationByPlaceInternalForReportWebModel(TrainingCourseTrainingLocation trainingLocation) {
		Country tlCountry = trainingLocation.getTrainingLocationCountry();
		List<String> lst = new ArrayList<String>();
		if (tlCountry != null) {
			lst.add(tlCountry.getName());
		}
		if (trainingLocation.getTrainingLocationCity() != null) {
			lst.add(trainingLocation.getTrainingLocationCity());
		}
		this.trainingLocation = StringUtils.join(lst, ", ");
		TrainingInstitution trInst = trainingLocation.getTrainingInstitution(); 
		this.trainingInstitution = trInst == null ? "" : trInst.getName();
		
	}

	public String getTrainingLocation() {
		return trainingLocation;
	}

	public String getTrainingInstitution() {
		return trainingInstitution;
	}
	
	
}
