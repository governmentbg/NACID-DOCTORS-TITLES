package com.nacid.web.model.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.EducationLevel;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.DataConverter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingCourseWebModel {
	private String id;

    private List<Speciality> recognSpecs;
    private String recognSpecIds;

    protected String diplomaSeries;
	protected String diplomaNumber;
	protected String diplomaRegistrationNumber;
    protected String diplomaDate;
    protected String firstName;
    protected String surName;
    protected String lastName;
    protected boolean isJointDegree;
    //protected String trainingLocationCity;
    protected String trainingStart;
    protected String trainingEnd;
    protected String trainingDuration;
    protected String credits;
    protected String educationLevelName;
    protected UniversityWithFacultyWebModel baseUniversityWithFaculty;
    protected List<UniversityWithFacultyWebModel> jointUniversityWithFaculties = new ArrayList<>();
    protected List<UniversityWithFacultyWebModel> allUniversityWithFaculties = new ArrayList<>();
    protected int universitiesCount;
    protected DiplomaTypeWebModel diplomaTypeWebModel;
    protected String schoolCity;
    protected String schoolName;
    protected String schoolGraduationDate;
    protected String schoolNotes;
    protected String prevDiplGraduationDate;
    protected String prevDiplNotes;
    protected String prevDiplomaSpeciality;
    protected Integer prevDiplomaSpecialityId;
    protected UniversityWithFacultyWebModel prevDiplUniversityWithFaculty;
    protected Integer schoolCountryId;
    //protected Integer trainingSpecialityId;
    //protected String trainingSpeciality;
    protected List<Speciality> trainingSpecialities;
    protected List<FlatNomenclature> trainingOriginalSpecialities;
    protected Integer trainingQualificationId;
    protected String trainingQualification;
    protected Integer trainingOriginalQualificationId;
    protected String trainingOriginalQualification;

	protected DiplomaExaminationWebModel diplomaExaminationWebModel;
    private Integer graduationDocumentTypeId;
    private String creditHours;
    private String ectsCredits;
    private String thesisTopic;
    private String thesisTopicEn;
    private String thesisDefenceDate;
    private String thesisBibliography;
    private String thesisVolume;
    private String thesisAnnotation;
    private String thesisAnnotationEn;
    private String recognizedQualificationId;
    private String recognizedQualification;
    private boolean hasEducationPeriodInformation;




    /**
     * trainingCourseTrainingForms
     * key - trainingFormId
     * value - notes
     */
    protected Map<Integer, String> trainingCourseTrainingForms = new HashMap<Integer, String>();

    /**
     * trainingCourseGraduationWays
     * key - graduationWayId
     * value - notes
     */
    protected Map<Integer, String> trainingCourseGraduationWays = new HashMap<Integer, String>();
    
    private List<TrainingCourseTrainingLocationWebModel> trainingLocations = null;
    private PersonWebModel owner;
    public TrainingCourseWebModel(NacidDataProvider nacidDataProvider, TrainingCourse course) {
        this.id = course.getId() + "";
        this.diplomaSeries = course.getDiplomaSeries();
    	this.diplomaNumber = course.getDiplomaNumber();
    	this.diplomaRegistrationNumber = course.getDiplomaRegistrationNumber();
        this.firstName = course.getFName();
        this.surName = course.getSName();
        this.lastName = course.getLName();
        this.isJointDegree = course.isJointDegree();
        List<? extends TrainingCourseTrainingLocation> tLocs = course.getTrainingCourseTrainingLocations();
        if (tLocs != null) {
        	trainingLocations = new ArrayList<TrainingCourseTrainingLocationWebModel>();
        	for (TrainingCourseTrainingLocation tl:tLocs) {
            	trainingLocations.add(new TrainingCourseTrainingLocationWebModel(tl, true));
            }	
        }
        
        this.trainingDuration = DataConverter.formatFloatingNumber(course.getTrainingDuration());
        this.credits = DataConverter.formatFloatingNumber(course.getCredits(), 2);
        FlatNomenclature educationLevel = course.getEducationLevel();
        this.educationLevelName = educationLevel == null ? "-" : educationLevel.getName();
        this.hasEducationPeriodInformation = course.hasEducationPeriodInformation();
        UniversityWithFaculty baseUniversityWithFaculty = course.getBaseUniversityWithFaculty();
        this.baseUniversityWithFaculty = baseUniversityWithFaculty == null ? null : new UniversityWithFacultyWebModel(baseUniversityWithFaculty);
        if (baseUniversityWithFaculty != null) {
            this.allUniversityWithFaculties.add(this.baseUniversityWithFaculty);
        }
        
        universitiesCount = baseUniversityWithFaculty == null ? 0 : 1 ;
        List<? extends UniversityWithFaculty> jointUniversities = course.getJointUniversityWithFaculties();
        if (jointUniversities != null && jointUniversities.size() > 0) {
            this.jointUniversityWithFaculties = new ArrayList<>();
            for (UniversityWithFaculty u : jointUniversities) {
                UniversityWithFacultyWebModel m = new UniversityWithFacultyWebModel(u);
            	this.jointUniversityWithFaculties.add(m);
                this.allUniversityWithFaculties.add(m);
                universitiesCount ++;
            }
        }
        
        this.schoolCity = course.getSchoolCity();
        this.schoolName = course.getSchoolName();
        this.schoolNotes = course.getSchoolNotes();
        this.prevDiplNotes = course.getPrevDiplomaNotes();

        University prevDiplUni = course.getPrevDiplomaUniversity();
        this.prevDiplUniversityWithFaculty = prevDiplUni == null ? null : new UniversityWithFacultyWebModel(prevDiplUni, null);
        
        this.diplomaDate = DataConverter.formatDate(course.getDiplomaDate(), "дд.мм.гггг");
        this.trainingStart = DataConverter.formatYear(course.getTrainingStart(), "гггг");
        this.trainingEnd = DataConverter.formatYear(course.getTrainingEnd(), "гггг");
        this.schoolGraduationDate = DataConverter.formatYear(course.getSchoolGraduationDate(), "гггг");;
        this.prevDiplGraduationDate = DataConverter.formatYear(course.getPrevDiplomaGraduationDate(), "гггг");
        this.prevDiplomaSpeciality = course.getPrevDiplomaSpeciality() == null ? "" : course.getPrevDiplomaSpeciality().getName();
        this.prevDiplomaSpecialityId = course.getPrevDiplomaSpecialityId();
        DiplomaType dt = course.getDiplomaType();
        this.diplomaTypeWebModel = dt == null ? null : new DiplomaTypeWebModel(course.getDiplomaType());
        
        
        TrainingCourseTrainingForm trainingForm = course.getTrainingCourseTrainingForm();
        if (trainingForm != null) {
        	this.trainingCourseTrainingForms.put(trainingForm.getTrainingFormId(), trainingForm.getNotes());
        }
        List<TrainingCourseGraduationWay> graduationWays = course.getTrainingCourseGraduationWays();
        if (graduationWays != null) {
            for (TrainingCourseGraduationWay gw:graduationWays) {
            	this.trainingCourseGraduationWays.put(gw.getGraduationWayId(), gw.getNotes());
            }
        }
        this.diplomaExaminationWebModel = new DiplomaExaminationWebModel(course.getDiplomaExamination());
        
        this.schoolCountryId = course.getSchoolCountryId();


        List<TrainingCourseSpeciality> specialities = course.getSpecialities();
        if (specialities != null && specialities.size() > 0) {
            trainingSpecialities = new ArrayList<Speciality>();
            trainingOriginalSpecialities = new ArrayList<FlatNomenclature>();
            NomenclaturesDataProvider ndp = nacidDataProvider.getNomenclaturesDataProvider();
            for (TrainingCourseSpeciality speciality : specialities) {
                trainingSpecialities.add(ndp.getSpeciality(speciality.getSpecialityId()));
                trainingOriginalSpecialities.add(speciality.getOriginalSpecialityId() == null ? null : ndp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY, speciality.getOriginalSpecialityId()));
            }
        }


        this.trainingQualificationId = course.getQualificationId();
        this.trainingQualification = course.getQualification() == null ? "" : course.getQualification().getName();
        this.trainingOriginalQualification = course.getOriginalQualification() == null ? "" : course.getOriginalQualification().getName();
        this.trainingOriginalQualificationId = course.getOriginalQualificationId();
        this.graduationDocumentTypeId = course.getGraduationDocumentTypeId();
        creditHours = DataConverter.formatInteger(course.getCreditHours());
        this.ectsCredits = DataConverter.formatInteger(course.getEctsCredits());
        this.owner = new PersonWebModel(course.getOwner());
        this.thesisTopic = course.getThesisTopic() == null ? "" : course.getThesisTopic();
        this.thesisTopicEn = course.getThesisTopicEn() == null ? "" : course.getThesisTopicEn();

        this.recognizedQualificationId = course.getRecognizedQualificationId() == null ? "" : course.getRecognizedQualificationId().toString();
        this.recognizedQualification = course.getRecognizedQualificationId() == null ? "" : course.getRecognizedQualification().getName();
        this.thesisDefenceDate = DataConverter.formatDate(course.getThesisDefenceDate(), "дд.мм.гггг");
        this.thesisAnnotation = course.getThesisAnnotation();
        this.thesisAnnotationEn = course.getThesisAnnotationEn();
        this.thesisBibliography = course.getThesisBibliography() == null ? "" : course.getThesisBibliography() + "";
        this.thesisVolume = course.getThesisVolume() == null ? "" : course.getThesisVolume() + "";

        recognSpecs = new ArrayList<Speciality>()/*<RecognizedSpecsWebModel>()*/; //RayaChanges
        recognSpecIds = "";


        List<Speciality> recognizedSpecialities = course.getRecognizedSpecialities();
        if (recognizedSpecialities != null) {
            for(Speciality s : recognizedSpecialities) {
                recognSpecs.add(s);//new RecognizedSpecsWebModel(s.getId(), s.getName())); //RayaChanges
                recognSpecIds += (recognSpecIds.length() == 0 ? "" : ";") + s.getId();
            }
        }
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
    public boolean isJointDegree() {
        return isJointDegree;
    }
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
    public Map<Integer, String> getTrainingCourseTrainingForms() {
        return trainingCourseTrainingForms;
    }
    public Map<Integer, String> getTrainingCourseGraduationWays() {
        return trainingCourseGraduationWays;
    }
    
    public DiplomaTypeWebModel getDiplomaTypeWebModel() {
        return (DiplomaTypeWebModel)diplomaTypeWebModel;
    }
    
    public DiplomaExaminationWebModel getDiplomaExaminationWebModel() {
        return diplomaExaminationWebModel;
    }
	public List<TrainingCourseTrainingLocationWebModel> getTrainingLocations() {
		return trainingLocations;
	}
	public int getUniversitiesCount() {
		return universitiesCount;
	}
	public String getId() {
		return id;
	}
	public Integer getSchoolCountryId() {
		return schoolCountryId;
	}

	public List<Speciality> getTrainingSpecialities() {
        return trainingSpecialities;
    }

    public List<FlatNomenclature> getTrainingOriginalSpecialities() {
        return trainingOriginalSpecialities;
    }

    public Integer getTrainingQualificationId() {
		return trainingQualificationId;
	}	
    public String getTrainingQualification() {
		return trainingQualification;
	}
	public String getPrevDiplomaSpeciality() {
		return prevDiplomaSpeciality;
	}
	public Integer getPrevDiplomaSpecialityId() {
		return prevDiplomaSpecialityId;
	}

    public Integer getGraduationDocumentTypeId() {
        return graduationDocumentTypeId;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public String getEctsCredits() {
        return ectsCredits;
    }

    public PersonWebModel getOwner() {
        return owner;
    }

    public UniversityWithFacultyWebModel getBaseUniversityWithFaculty() {
        return baseUniversityWithFaculty;
    }

    public List<UniversityWithFacultyWebModel> getJointUniversityWithFaculties() {
        return jointUniversityWithFaculties;
    }

    public List<UniversityWithFacultyWebModel> getAllUniversityWithFaculties() {
        return allUniversityWithFaculties;
    }

    public UniversityWithFacultyWebModel getPrevDiplUniversityWithFaculty() {
        return prevDiplUniversityWithFaculty;
    }

    public String getThesisTopic() {
        return thesisTopic;
    }

    public String getThesisTopicEn() {
        return thesisTopicEn;
    }

    public boolean hasEducationPeriodInformation() {
        return hasEducationPeriodInformation;
    }

    public List<Speciality> getRecognSpecs() {
        return recognSpecs;
    }

    public String getRecognSpecIds() {
        return recognSpecIds;
    }

    public String getRecognizedQualificationId() {
        return recognizedQualificationId;
    }

    public String getRecognizedQualification() {
        return recognizedQualification;
    }

    public String getDiplomaSeries() {
        return diplomaSeries;
    }

    public String getDiplomaRegistrationNumber() {
        return diplomaRegistrationNumber;
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

    public String getThesisAnnotation() {
        return thesisAnnotation;
    }

    public String getThesisAnnotationEn() {
        return thesisAnnotationEn;
    }

    public Integer getTrainingOriginalQualificationId() {
        return trainingOriginalQualificationId;
    }

    public String getTrainingOriginalQualification() {
        return trainingOriginalQualification;
    }
}
