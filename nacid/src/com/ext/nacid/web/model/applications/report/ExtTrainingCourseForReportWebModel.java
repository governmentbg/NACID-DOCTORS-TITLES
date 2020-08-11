package com.ext.nacid.web.model.applications.report;

import com.nacid.bl.external.applications.*;
import com.nacid.web.model.applications.report.base.TrainingCourseForReportBaseWebModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ExtTrainingCourseForReportWebModel extends TrainingCourseForReportBaseWebModel {
	//private String universityTxt;
	private String specialityTxt;
	private String qualificationTxt;
	private String prevDiplomaUniversityTxt;
	private String prevDiplomaSpecialityTxt;
	private List<ExtUniversityForReportWebModel> universities = new ArrayList<ExtUniversityForReportWebModel>();

	//private String trainingInstitutionTxt;
	public ExtTrainingCourseForReportWebModel(ExtTrainingCourse course, ExtTrainingCourseDataProvider extTrainingCourseDataProvider) {
		super(course);
		//TODO:zasega go zakyrpvam da pro4ita imeto na pyrviq universitet
		//ExtUniversity u = course.getBaseUniversity();
		//this.universityTxt = u == null ? "" : u.getUniversityTxt();
		//this.specialityTxt = course.getSpecialityTxt();
		//this.specialityTxt = course.getTrainingCourseSpecialities().get(0).getSpecialityTxt(); //TODO: za momenta se vzema samo 1-vata specialnost
		this.specialityTxt = course.getSpecialityTxtSeparatedBySemicolon();
		this.qualificationTxt = course.getQualificationTxt();
		this.prevDiplomaUniversityTxt = course.getPrevDiplomaUiniversityTxt();
		this.prevDiplomaSpecialityTxt = course.getPrevDiplomaSpecialityTxt();
		//this.trainingInstitutionTxt = course.getTrainingInstTxt();
		
		
		List<ExtTrainingForm> trainingForms = extTrainingCourseDataProvider.getExtTrainingForms(course.getId());
		super.trainingForm = trainingForms == null || trainingForms.size() < 1 ? "" : trainingForms.get(0).getTrainingFormName();
		
		
		List<String> lst = new ArrayList<String>();
		List<ExtGraduationWay> graduationWaysLst = extTrainingCourseDataProvider.getExtGraduationWays(course.getId());
		if (graduationWaysLst == null) {
			super.graduationWays = "";
		} else {
			for (ExtGraduationWay gw:graduationWaysLst) {
				lst.add(gw.getGraduationWayName());
			}
			super.graduationWays = StringUtils.join(lst, ", ");	
		}
		
		List<? extends ExtUniversityWithFaculty> universities = course.getUniversityWithFaculties();
		if (universities != null) {
			for (ExtUniversityWithFaculty uf:universities) {
				ExtUniversity u = uf.getUniversity();
				this.universities.add(new ExtUniversityForReportWebModel(u));
			}
		}
        this.owner = new ExtPersonForReportWebModel(course.getOwner());


	}
	/*public String getUniversityTxt() {
		return universityTxt;
	}*/
	public String getSpecialityTxt() {
		return specialityTxt;
	}
	public String getQualificationTxt() {
		return qualificationTxt;
	}
	public String getPrevDiplomaUniversityTxt() {
		return prevDiplomaUniversityTxt;
	}
	/*public String getTrainingInstitutionTxt() {
		return trainingInstitutionTxt;
	}*/
	
	public List<? extends ExtUniversityForReportWebModel> getUniversities() {
		return universities;
	}
	public String getPrevDiplomaSpecialityTxt() {
		return prevDiplomaSpecialityTxt;
	}

    public ExtPersonForReportWebModel getOwner() {
        return (ExtPersonForReportWebModel) owner;
    }
}
