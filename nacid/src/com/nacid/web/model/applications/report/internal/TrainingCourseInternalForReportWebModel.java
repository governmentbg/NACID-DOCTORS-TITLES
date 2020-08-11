package com.nacid.web.model.applications.report.internal;

import com.nacid.bl.applications.*;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.Qualification;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.applications.report.base.TrainingCourseForReportBaseWebModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TrainingCourseInternalForReportWebModel extends TrainingCourseForReportBaseWebModel {
	protected DiplomaTypeInternalForReportWebModel diplomaTypeWebModel;
    private boolean recognized;
	private List<UniversityWebModel> universities = new ArrayList<UniversityWebModel>();
	private String recognizedSpecialities;
	private String recognizedQualification;
	private String recognizedEduLevel;
	
    public TrainingCourseInternalForReportWebModel(TrainingCourse course) {
		super(course);
		
        DiplomaType dt = course.getDiplomaType();
		if (dt != null) {
			this.diplomaTypeWebModel = new DiplomaTypeInternalForReportWebModel(dt);	
		}
		this.recognized = course.isRecognized();
		
		//nadolu sa parametrite opisani v BaseClass-a no specifi4ni za TrainingCourseInternal, t.k. TrainingCourseExternal ima spalicity_txt, qualification_txt i t.n.  
		TrainingCourseTrainingForm trainingCourseTrainingForm = course.getTrainingCourseTrainingForm();
		super.trainingForm = trainingCourseTrainingForm == null ? "" : trainingCourseTrainingForm.getTrainingFormName();
		
		List<String> lst = new ArrayList<String>();
		List<TrainingCourseGraduationWay> graduationWaysLst = course.getTrainingCourseGraduationWays();
		if (graduationWaysLst == null) {
			super.graduationWays = "";
		} else {
			for (TrainingCourseGraduationWay gw:graduationWaysLst) {
				lst.add(gw.getGraduationWayName());
			}
			super.graduationWays = StringUtils.join(lst, ", ");	
		}
		List<? extends UniversityWithFaculty> universities = course.getUniversityWithFaculties();
		if (universities != null) {
			for (UniversityWithFaculty u:universities) {
				this.universities.add(new UniversityWebModel(u.getUniversity()));
			}
		}
		List<Speciality> recognizedSpecs = course.getRecognizedSpecialities();
		if (recognizedSpecs != null) {
			List<String> recognizedSpecialities = new ArrayList<String>();
			for (Speciality s:recognizedSpecs) {
				recognizedSpecialities.add(s.getName());
			}
			this.recognizedSpecialities = StringUtils.join(recognizedSpecialities, "<br />");
		}
		Qualification rq = course.getRecognizedQualification();
		this.recognizedQualification = rq == null ? null : rq.getName();
		FlatNomenclature rel = course.getRecognizedEducationLevel();
		this.recognizedEduLevel = rel == null ? null : rel.getName();

        this.owner = new PersonInternalForReportWebModel(course.getOwner());
	}
	
	
    
    public DiplomaTypeInternalForReportWebModel getDiplomaTypeWebModel() {
		return (DiplomaTypeInternalForReportWebModel) diplomaTypeWebModel;
	}
	public boolean isRecognized() {
		return recognized;
	}
	
	public List<? extends UniversityWebModel> getUniversities() {
		return universities;
	}
	public String getRecognizedSpecialities() {
		return recognizedSpecialities;
	}
	public String getRecognizedQualification() {
		return recognizedQualification;
	}
	public String getRecognizedEduLevel() {
		return recognizedEduLevel;
	}

    @Override
    public PersonInternalForReportWebModel getOwner() {
        return (PersonInternalForReportWebModel)owner;
    }
}
