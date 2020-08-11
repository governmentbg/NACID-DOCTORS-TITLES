package com.nacid.web.model.applications.report.base;

import com.nacid.bl.applications.base.TrainingCourseBase;
import com.nacid.bl.applications.base.TrainingCourseTrainingLocationBase;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;
import com.nacid.web.model.applications.UniversityWebModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TrainingCourseForReportBaseWebModel {

    protected String diplomaSeries;
	protected String diplomaNumber;
	protected String diplomaRegistrationNumber;
    protected String diplomaDate;
    protected String firstName;
    protected String surName;
    protected String lastName;
    //protected String trainingLocationCity;
    protected String trainingStart;
    protected String trainingEnd;
    protected String trainingDuration;
    protected String credits;
    protected String educationLevelName;
    //protected UniversityWebModel baseUniversity;
    //protected List<? extends UniversityWebModel> universities;
    protected String schoolCity;
    protected String schoolName;
    protected String schoolGraduationDate;
    protected String schoolNotes;
    protected String prevDiplGraduationDate;
    protected String prevDiplNotes;
    protected UniversityWebModel prevDiplUniversity;
	//protected String trainingLocationCountry;
	protected String speciality;
	protected String qualification;
	protected boolean isSchoolDiplomaPreset;
	protected String schoolCountry;
	protected String prevDiplomaEduLevel;
	protected String trainingForm;
	protected String graduationWays;
	protected String prevDiplomaSpeciality;
	//protected String trainingInstitution;
	protected List<TrainingCourseTrainingLocationForReportBaseWebModel> trainingLocations = null;
    protected PersonForReportBaseWebModel owner;

    private String thesisTopic;
    private String thesisTopicEn;
    private String thesisDefenceDate;
    private String thesisBibliography;
    private String thesisVolume;
    private String thesisLanguage;
    private String thesisAnnotation;
    private String thesisAnnotationEn;
    private String profGroupName;
    private final String eduAreaName;

	public TrainingCourseForReportBaseWebModel(TrainingCourseBase course) {
	    this.diplomaSeries = course.getDiplomaSeries();
		this.diplomaNumber = course.getDiplomaNumber();
		this.diplomaRegistrationNumber = course.getDiplomaRegistrationNumber();
        this.firstName = course.getFName();
        this.surName = course.getSName();
        this.lastName = course.getLName();

        this.trainingDuration = DataConverter.formatFloatingNumber(course.getTrainingDuration());
        this.credits = DataConverter.formatFloatingNumber(course.getCredits(), 2);
        FlatNomenclature educationLevel = course.getEducationLevel();
        this.educationLevelName = educationLevel == null ? "-" : educationLevel.getName();
        //University baseUniversity = course.getBaseUniversity();
        //this.baseUniversity = baseUniversity == null ? null : new UniversityWebModel(baseUniversity);
        
        this.schoolCity = course.getSchoolCity();
        this.schoolName = course.getSchoolName();
        this.schoolNotes = course.getSchoolNotes();
        this.prevDiplNotes = course.getPrevDiplomaNotes();
        
        this.prevDiplUniversity = new UniversityWebModel(course.getPrevDiplomaUniversity());
		diplomaDate = DataConverter.formatDate(course.getDiplomaDate(), "");
		trainingStart = DataConverter.formatYear(course.getTrainingStart(), "");
		trainingEnd = DataConverter.formatYear(course.getTrainingEnd(), "");
		schoolGraduationDate = DataConverter.formatYear(course.getSchoolGraduationDate(), "");
		FlatNomenclature durationUnit = course.getDurationUnit();
		trainingDuration += (durationUnit == null ? "" : "  " + durationUnit.getName());
		prevDiplGraduationDate = DataConverter.formatYear(course.getPrevDiplomaGraduationDate(), "");
		prevDiplomaSpeciality = course.getPrevDiplomaSpeciality() == null ? "" : course.getPrevDiplomaSpeciality().getName();
        
		//this.trainingLocationCity = course.getTrainingLocationCity();		
		//Country trainingLocationCountry = course.getTrainingLocationCountry();
		//this.trainingLocationCountry = trainingLocationCountry == null ? "" : trainingLocationCountry.getName();
		List<? extends TrainingCourseTrainingLocationBase> trainingCourseLocations = course.getTrainingCourseTrainingLocations();
		if (trainingCourseLocations != null) {
			trainingLocations = new ArrayList<TrainingCourseTrainingLocationForReportBaseWebModel>();
			for (TrainingCourseTrainingLocationBase tl:trainingCourseLocations) {
				trainingLocations.add(new TrainingCourseTrainingLocationForReportBaseWebModel(tl));
			}
		}
		
		this.isSchoolDiplomaPreset = !StringUtils.isEmpty(course.getSchoolName());
		Country schoolCountry = course.getSchoolCountry();
		this.schoolCountry = schoolCountry == null ? "" : schoolCountry.getName();
		
		//Speciality s = course.getSpeciality();
		this.speciality = course.getSpecialityNamesSeparatedBySemiColon();
		FlatNomenclature qualification = course.getQualification();
		this.qualification = qualification == null ? "" : qualification.getName();
		
		FlatNomenclature eduLevel = course.getPrevDiplomaEduLevel();
		this.prevDiplomaEduLevel = eduLevel == null ? "" : eduLevel.getName();
		/*TrainingInstitution trainingInstitution = course.getTrainingInstitution();
		List<String> lst = new ArrayList<String>();
		if (trainingInstitution != null) {
			lst.add(trainingInstitution.getName());
			if (!StringUtils.isEmpty(trainingInstitution.getCityName())) {
				lst.add(trainingInstitution.getCityName());
			}
			Country trainingInstitutionCountry = trainingInstitution == null ? null : trainingInstitution.getCountry();
			if (trainingInstitutionCountry != null) {
				lst.add(trainingInstitutionCountry.getName());
			}
			this.trainingInstitution = StringUtils.join(lst, ", ");
			
		} else {
			this.trainingInstitution = "";
		}*/

        this.thesisTopic = course.getThesisTopic();
        this.thesisTopicEn = course.getThesisTopicEn();
        this.thesisDefenceDate = DataConverter.formatDate(course.getThesisDefenceDate(), "дд.мм.гггг");
        this.thesisAnnotation = course.getThesisAnnotation();
        this.thesisAnnotationEn = course.getThesisAnnotationEn();
        this.thesisBibliography = course.getThesisBibliography() == null ? "" : course.getThesisBibliography() + "";
        this.thesisVolume = course.getThesisVolume() == null ? "" : course.getThesisVolume() + "";
        this.thesisLanguage = course.getThesisLanguage() == null ? null : course.getThesisLanguage().getName();
        this.profGroupName = course.getProfGroup() == null ? "" : course.getProfGroup().getName();
        this.eduAreaName = course.getProfGroup() == null ? "" : course.getProfGroup().getEducationAreaName();

	}
	public String getDiplomaNumber() {
        return diplomaNumber;
    }
    public String getDiplomaDate() {
        return diplomaDate;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSurName() {
        return surName;
    }
    public String getLastName() {
        return lastName;
    }
    /*public boolean isJointDegree() {
        return isJointDegree;
    }*/
    
    public String getTrainingStart() {
        return trainingStart;
    }
    public String getTrainingEnd() {
        return trainingEnd;
    }
    public String getTrainingDuration() {
        return trainingDuration;
    }
    public String getCredits() {
        return credits;
    }
    /*public UniversityWebModel getBaseUniversity() {
        return baseUniversity;
    }*/
    public String getEducationLevelName() {
        return educationLevelName;
    }
    public String getSchoolCity() {
        return schoolCity;
    }
    public String getSchoolName() {
        return schoolName;
    }
    public String getSchoolGraduationDate() {
        return schoolGraduationDate;
    }
    public String getSchoolNotes() {
        return schoolNotes;
    }
    public String getPrevDiplGraduationDate() {
        return prevDiplGraduationDate;
    }
    public String getPrevDiplNotes() {
        return prevDiplNotes;
    }
    public UniversityWebModel getPrevDiplUniversity() {
        return prevDiplUniversity;
    }
    /*public String getTrainingLocationCity() {
        return trainingLocationCity;
    }
	public String getTrainingLocationCountry() {
		return trainingLocationCountry;
	}*/
    public List<TrainingCourseTrainingLocationForReportBaseWebModel> getTrainingLocations() {
    	return trainingLocations;
    }
	public String getSpeciality() {
		return speciality;
	}
	public String getQualification() {
		return qualification;
	}
	public boolean isSchoolDiplomaPreset() {
		return isSchoolDiplomaPreset;
	}
	public String getSchoolCountry() {
		return schoolCountry;
	}
	public String getPrevDiplomaEduLevel() {
		return prevDiplomaEduLevel;
	}
	
	/*public String getTrainingInstitution() {
		return trainingInstitution;
	}*/
	public String getTrainingForm() {
		return trainingForm;
	}
	public String getGraduationWays() {
		return graduationWays;
	}
	public List<? extends UniversityWebModel> getUniversities() {
		return null;
	}
	public String getPrevDiplomaSpeciality() {
		return prevDiplomaSpeciality;
	}

    public PersonForReportBaseWebModel getOwner() {
        return owner;
    }

    public String getDiplomaSeries() {
        return diplomaSeries;
    }

    public String getDiplomaRegistrationNumber() {
        return diplomaRegistrationNumber;
    }

    public String getThesisTopic() {
        return thesisTopic;
    }

    public String getThesisTopicEn() {
        return thesisTopicEn;
    }

    public String getThesisDefenceDate() {
        return thesisDefenceDate;
    }

    public String getThesisBibliography() {
        return thesisBibliography;
    }

    public String getThesisVolume() {
        return thesisVolume;
    }

    public String getThesisLanguage() {
        return thesisLanguage;
    }

    public String getThesisAnnotation() {
        return thesisAnnotation;
    }

    public String getThesisAnnotationEn() {
        return thesisAnnotationEn;
    }

    public String getProfGroupName() {
        return profGroupName;
    }

    public String getEduAreaName() {
        return eduAreaName;
    }
}
