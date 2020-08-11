package com.ext.nacid.web.model.applications;

import java.util.*;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.external.applications.*;
import com.nacid.bl.nomenclatures.*;
import com.nacid.data.DataConverter;
import com.nacid.data.external.applications.ExtTrainingCourseSpecialityRecord;

public class ExtTrainingCourseWebModel {


    private String applicationId;
    private List<ExtUniversityWithFacultyWebModel> universities = new ArrayList<ExtUniversityWithFacultyWebModel>();
    private String diplomaSeries;
    private String diplomaNumber;
    private String diplomaRegistrationNumber;
    private String diplomaDate;
    //private String trainingLocationCity;
    private String trainingStart;
    private String trainingEnd;
    private String credits;
    private String schoolCity;
    private String schoolName;
    private String schoolGraduationDate;
    private String schoolNotes;
    private String prevDiplName;
    private String prevDiplUniversityId;
    private String prevDiplGraduationDate;
    private String prevDiplNotes;
    private String trainingDuration;
    private String prevDiplomaSpecialityName;
    private Integer prevDiplomaSpecialityId;
    //private String trainingSpecialityTxt;
    private Integer qualificationId;
    private String qualificationName;

    private String thesisTopic;
    private String thesisTopicEn;
    private String thesisDefenceDate;
    private String thesisBibliography;
    private String thesisVolume;
    private String thesisAnnotation;
    private String thesisAnnotationEn;
    private boolean hasEducationPeriodInformation;


    private Map<Integer, String> trainingCourseTrainingForms = new HashMap<Integer, String>();
    private Map<Integer, String> trainingCourseGraduationWays = new HashMap<Integer, String>();
    private Map<Integer, String> applicationRecognitionPurposes = new HashMap<Integer, String>();
    
    private List<ExtTrainingCourseTrainingLocationWebModel> trainingLocations = null;
    private List<ExtTrainingCourseSpecialityWebModel> trainingSpecialities = null;
    
    
    public ExtTrainingCourseWebModel(ExtApplication application, NacidDataProvider nacidDataProvider) {
        ExtTrainingCourseDataProvider tcDP = nacidDataProvider.getExtTrainingCourseDataProvider();
        ExtTrainingCourse extTC = tcDP.getExtTrainingCourse(application.getTrainingCourseId());
        UniversityDataProvider univDP = nacidDataProvider.getUniversityDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        
        applicationId = application.getId() + "";

        List<ExtUniversityWithFaculty> extUniversities = extTC.getUniversityWithFaculties();
        if  (extUniversities != null) {
        	
        	for (ExtUniversityWithFaculty uf : extUniversities) {
                ExtUniversity u = uf.getUniversity();
                ExtUniversityFaculty fac = uf.getFaculty();
        	    String universityId;
        		String universityName;
        		String facultyId;
        		String facultyName;
        		if (u.isStandartUniversity()) {
        			universityId = u.getId() + "";
        			Country c = u.getCountry();
        			String cName = c == null ? "" : c.getName();
        			universityName = u.getBgName() + ", " + (u.getCity() == null ? " " : u.getCity()) + ", " + cName;
        		} else {
        			universityId = "";
        			universityName = u.getUniversityTxt();
        		}
        		if (fac.isStandardFaculty()) {
        		    facultyId = fac.getId() + "";
        		    facultyName = fac.getName();
                } else {
        		    facultyId = "";
        		    facultyName = fac.getFacultyTxt();
                }
        		universities.add(new ExtUniversityWithFacultyWebModel(universityId, universityName, facultyId, facultyName));
        	}
        } else {
        	universities.add(new ExtUniversityWithFacultyWebModel("", "", "", ""));
        }
        diplomaSeries = extTC.getDiplomaSeries();
        diplomaNumber = extTC.getDiplomaNumber();
        diplomaRegistrationNumber = extTC.getDiplomaRegistrationNumber();
        diplomaDate = DataConverter.formatDate(extTC.getDiplomaDate(), "дд.мм.гггг");
        trainingStart = DataConverter.formatYear(extTC.getTrainingStart(), "гггг");
        trainingEnd  = DataConverter.formatYear(extTC.getTrainingEnd(), "гггг");
        credits = extTC.getCredits() != null ? extTC.getCredits().toString() : "";
        schoolCity = extTC.getSchoolCity();
        schoolName = extTC.getSchoolName();
        schoolGraduationDate = DataConverter.formatYear(extTC.getSchoolGraduationDate(), "гггг");
        schoolNotes = extTC.getSchoolNotes();
        trainingDuration = extTC.getTrainingDuration() == null ? "" : extTC.getTrainingDuration().toString();
        //trainingSpecialityTxt = extTC.getSpecialityTxt();
        qualificationName = extTC.getQualificationId() == null ? extTC.getQualificationTxt() : extTC.getQualification().getName();
        qualificationId = extTC.getQualificationId();
        prevDiplomaSpecialityName = extTC.getPrevDiplomaSpecialityId() == null ? extTC.getPrevDiplomaSpecialityTxt() : extTC.getPrevDiplomaSpeciality().getName();
        prevDiplomaSpecialityId = extTC.getPrevDiplomaSpecialityId();
        List<? extends ExtTrainingCourseTrainingLocation> tLocs = extTC.getTrainingCourseTrainingLocations();
        if (tLocs != null) {
        	trainingLocations = new ArrayList<ExtTrainingCourseTrainingLocationWebModel>();
        	for (ExtTrainingCourseTrainingLocation tl:tLocs) {
            	trainingLocations.add(new ExtTrainingCourseTrainingLocationWebModel(tl));
            }	
        }
        
        
        if(extTC.getPrevDiplomaUniversityId() == null) {
            prevDiplUniversityId = "";
            prevDiplName = extTC.getPrevDiplomaUiniversityTxt();
        }
        else {
            prevDiplUniversityId = extTC.getPrevDiplomaUniversityId().toString();
            University u = univDP.getUniversity(extTC.getPrevDiplomaUniversityId());
            String c = nomDP.getCountry(u.getCountryId()).getName();
            prevDiplName = u.getBgName() + ", " + u.getCity() + ", " + c;
        }
        prevDiplGraduationDate = DataConverter.formatYear(extTC.getPrevDiplomaGraduationDate(), "гггг");
        prevDiplNotes = extTC.getPrevDiplomaNotes();
        
        List<ExtPurposeOfRecognition> pors = tcDP.getExtPurposesOfRecognition(application.getId());
        if (pors != null) {
            for (ExtPurposeOfRecognition rp:pors) {
                applicationRecognitionPurposes.put(rp.getPurposeOfRecognitionId(), rp.getNotes());
            }
        }
        
        List<ExtTrainingForm> trForms = tcDP.getExtTrainingForms(extTC.getId());
        if (trForms != null && trForms.size() > 0) {
            ExtTrainingForm tf = trForms.get(0);
            trainingCourseTrainingForms.put(tf.getTrainingFormId(), tf.getNotes());
        }
        
        List<ExtGraduationWay> graduationWays = tcDP.getExtGraduationWays(extTC.getId());
        if (graduationWays != null) {
            for (ExtGraduationWay gw:graduationWays) {
                trainingCourseGraduationWays.put(gw.getGraduationWayId(), gw.getNotes());
            }
        }
        
        List<ExtTrainingCourseSpecialityRecord> specialityRecords = extTC.getTrainingCourseSpecialities();
        if (specialityRecords != null) {
            trainingSpecialities = new ArrayList<ExtTrainingCourseSpecialityWebModel>();
            for (ExtTrainingCourseSpecialityRecord specialityRecord : specialityRecords) {
                if (specialityRecord.getSpecialityId() != null) {
                    Speciality speciality = nacidDataProvider.getNomenclaturesDataProvider().getSpeciality(specialityRecord.getSpecialityId());
                    ExtTrainingCourseSpecialityWebModel specialityWebModel = new ExtTrainingCourseSpecialityWebModel(speciality.getId(), null, speciality.getName());
                    trainingSpecialities.add(specialityWebModel);
                }
                else if (specialityRecord.getSpecialityTxt() != null) {
                    ExtTrainingCourseSpecialityWebModel specialityWebModel = new ExtTrainingCourseSpecialityWebModel(null, specialityRecord.getSpecialityTxt(), specialityRecord.getSpecialityTxt());
                    trainingSpecialities.add(specialityWebModel);
                }
            }
        }
        this.thesisTopic = extTC.getThesisTopic();
        this.thesisTopicEn = extTC.getThesisTopicEn();
        this.thesisDefenceDate = DataConverter.formatDate(extTC.getThesisDefenceDate(), "дд.мм.гггг");
        this.thesisAnnotation = extTC.getThesisAnnotation();
        this.thesisAnnotationEn = extTC.getThesisAnnotationEn();
        this.thesisBibliography = extTC.getThesisBibliography() == null ? "" : extTC.getThesisBibliography() + "";
        this.thesisVolume = extTC.getThesisVolume() == null ? "" : extTC.getThesisVolume() + "";
        hasEducationPeriodInformation = extTC.hasEducationPeriodInformation();

    }


    public String getApplicationId() {
        return applicationId;
    }

    public String getDiplomaNumber() {
        return diplomaNumber;
    }

    public String getDiplomaDate() {
        return diplomaDate;
    }

    public String getTrainingStart() {
        return trainingStart;
    }

    public String getTrainingEnd() {
        return trainingEnd;
    }

    public String getCredits() {
        return credits;
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

    public String getPrevDiplName() {
        return prevDiplName;
    }

    public String getPrevDiplUniversityId() {
        return prevDiplUniversityId;
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

    public Map<Integer, String> getApplicationRecognitionPurposes() {
        return applicationRecognitionPurposes;
    }

    public String getTrainingDuration() {
        return trainingDuration;
    }

    /*public String getTrainingSpecialityTxt() {
        return trainingSpecialityTxt;
    }*/

    public Integer getQualificationId() {
        return qualificationId;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public List<ExtTrainingCourseTrainingLocationWebModel> getTrainingLocations() {
		return trainingLocations;
	}
	public List<ExtUniversityWithFacultyWebModel> getUniversities() {
		return universities;
	}

    public String getPrevDiplomaSpecialityName() {
        return prevDiplomaSpecialityName;
    }

    public Integer getPrevDiplomaSpecialityId() {
        return prevDiplomaSpecialityId;
    }

    public List<ExtTrainingCourseSpecialityWebModel> getTrainingSpecialities() {
	    return trainingSpecialities;
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

    public boolean isHasDissertationGraduationWay() {
        return trainingCourseGraduationWays == null ? false : trainingCourseGraduationWays.keySet().stream().filter(gw -> Objects.equals(gw, GraduationWay.GRADUATION_WAY_DISSERTATION)).findFirst().isPresent();
    }
}
