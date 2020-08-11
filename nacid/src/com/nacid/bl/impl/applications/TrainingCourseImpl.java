package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.data.applications.TrainingCourseRecord;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;


public class TrainingCourseImpl implements TrainingCourse {
    private NacidDataProviderImpl nacidDataProvider;
    private TrainingCourseRecord trainingCourseRecord;
    private TrainingCourseDataProviderImpl trainingCourseDataProvider;
    private List<UniversityWithFaculty> universityWithFaculties = null;
    
    private List<Integer> recognizedSpecialityIds;
    private List<TrainingCourseSpeciality> specialities;
    private boolean isSpecialityIdsRead;
    private List<Speciality> recognizedSpecialities;
    private Map<Integer, UniversityExamination> universityExaminations = new LinkedHashMap<Integer, UniversityExamination>();
    private boolean isUniversityExaminationRead = false;
    private boolean isRecognizedSpecialitiesFilled = false;
    private Person owner;
    
    public TrainingCourseImpl(NacidDataProviderImpl nacidDataProvider, TrainingCourseDataProviderImpl trainingCourseDataProvider, TrainingCourseRecord trainingCourseRecord) {
        this.nacidDataProvider = nacidDataProvider;
        this.trainingCourseRecord = trainingCourseRecord;
        this.trainingCourseDataProvider = trainingCourseDataProvider;

    }
    
    @Override
    public boolean isRecognized() {
        return trainingCourseRecord.getRecognized() != 0;
    }
    
    public int getId() {
        return trainingCourseRecord.getId();
    }

    @Override
    public String getDiplomaSeries() {
        return trainingCourseRecord.getDiplomaSeries();
    }

    public String getDiplomaNumber() {
        return trainingCourseRecord.getDiplomaNumber();
    }
    @Override
    public String getDiplomaRegistrationNumber() {
        return trainingCourseRecord.getDiplomaRegistrationNumber();
    }
    public Date getDiplomaDate() {
        return trainingCourseRecord.getDiplomaDate();
    }
    public DiplomaType getDiplomaType() {
        if (trainingCourseRecord.getDiplomaTypeId() == null) {
            return null;
        }
        DiplomaTypeDataProvider diplomaTypeDataProvider = nacidDataProvider.getDiplomaTypeDataProvider();
        return diplomaTypeDataProvider.getDiplomaType(trainingCourseRecord.getDiplomaTypeId());
    }
    public String getFName() {
        return trainingCourseRecord.getFirstName();
    }
    public String getSName() {
        return trainingCourseRecord.getSurName();
    }
    public String getLName() {
        return trainingCourseRecord.getLastName();
    }
    public String getFullName() {
    	return (StringUtils.isEmpty(getFName()) ? "" : getFName() + " ") + (StringUtils.isEmpty(getSName()) ? "" : getSName() + " ") + (StringUtils.isEmpty(getLName()) ? "" : getLName());
    }
    public boolean isJointDegree() {
        return trainingCourseRecord.getIsJointDegree() == null || trainingCourseRecord.getIsJointDegree() != 1 ? false : true;
    }
    /*public Country getTrainingLocationCountry() {
        if (trainingCourseRecord.getTrainingLocationCountryId() == null) {
            return null;
        }
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getCountry(trainingCourseRecord.getTrainingLocationCountryId());
    }
    public String getTrainingLocationCity() {
        return trainingCourseRecord.getTrainingLocationCity();
    }
    public Integer getTrainingLocationCountryId() {
        return trainingCourseRecord.getTrainingLocationCountryId();
    }
    */
    public List<? extends TrainingCourseTrainingLocation> getTrainingCourseTrainingLocations() {
		return trainingCourseDataProvider.getTrainingCourseTrainingLocations(getId());
	}
    public Date getTrainingStart() {
        return trainingCourseRecord.getTrainingStart();
    }
    public Date getTrainingEnd() {
        return trainingCourseRecord.getTrainingEnd();
    }
    public Double getTrainingDuration() {
        return trainingCourseRecord.getTrainingDuration();
    }
    public FlatNomenclature getDurationUnit() {
        if (trainingCourseRecord.getDurationUnitId() == null) {
            return null;
        }
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_DURATION_UNIT, trainingCourseRecord.getDurationUnitId());
    }
    public BigDecimal getCredits() {
        return trainingCourseRecord.getCredits();
    }
    /*public Speciality getSpeciality() {
        if (trainingCourseRecord.getSpecialityId() == null) {
            return null;
        }
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getSpeciality(trainingCourseRecord.getSpecialityId());
    }*/
    public FlatNomenclature getEducationLevel() {
        if (trainingCourseRecord.getEducationLevelId() == null) {
            return null;
        }
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, trainingCourseRecord.getEducationLevelId());

    }
    public FlatNomenclature getQualification() {
        if (trainingCourseRecord.getQualificationId() == null) {
            return null;
        }
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION, trainingCourseRecord.getQualificationId());
    }
    public Integer getBaseUniversityId() {
    	UniversityDataProviderImpl universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        List<Integer> universities = universityDataProvider.getUniversityIds(getId());
        if (universities == null || universities.size() == 0) {
            return null;  
        }
        return universities.get(0);
    }

    public List<Integer> getJointUniversityIds() {
    	UniversityDataProviderImpl universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        List<Integer> universities = universityDataProvider.getUniversityIds(getId());
        if (universities == null || universities.size() <= 1) {
            return null;  
        }
        return universities.subList(1, universities.size());
    }
    public List<Integer> getUniversityIds() {
    	UniversityDataProviderImpl universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        return universityDataProvider.getUniversityIds(getId());
    }
    public Integer getDiplomaTypeId() {
        return trainingCourseRecord.getDiplomaTypeId();
    }
    public Integer getDurationUnitId() {
        return trainingCourseRecord.getDurationUnitId();
    }

    public Integer getEducationLevelId() {
        return trainingCourseRecord.getEducationLevelId();
    }
    public Integer getQualificationId() {
        return trainingCourseRecord.getQualificationId();
    }
    /*public Integer getSpecialityId() {
        return trainingCourseRecord.getSpecialityId();
    }*/
    
    public List<TrainingCourseGraduationWay> getTrainingCourseGraduationWays() {
        return trainingCourseDataProvider.getTrainingCourseGraduationWays(getId());
    }
    public TrainingCourseTrainingForm getTrainingCourseTrainingForm() {
        return trainingCourseDataProvider.getTrainingCourseTrainingForm(getId());
    }

    public Collection<UniversityExamination> getUniversityExaminations() {
        synchronized (universityExaminations) {
        	if (!isUniversityExaminationRead) {
            	List<UniversityExamination> uniExam = trainingCourseDataProvider.getUniversityExaminations(getId()); 
            	if (uniExam != null) {
            		for (UniversityExamination exam : uniExam) {
            			universityExaminations.put(exam.getUniversityValidity().getUniversityId(), exam);
            		}
            	}
            	isUniversityExaminationRead = true;
            }
            return universityExaminations.size() == 0 ? null : universityExaminations.values();	
		}
    	
    }
    /*public List<UniversityValidity> getUniversityValidities() {
        List<UniversityExamination> universityExamination = getUniversityExaminations();
        if (universityExamination == null) {
            return null;
        }
        return nacidDataProvider.getUniversityValidityDataProvider().getUniversityValidity(universityExamination.getUniversityValidityId());
    }*/
    
    /*public Map<Integer, UniversityExamination> getUniversityExaminationsAsMap() {
        return trainingCourseDataProvider.getUniversityExaminationsAsMap(getId());
    }*/
    @Override
    public DiplomaExamination getDiplomaExamination() {
       return trainingCourseDataProvider.getDiplomaExamination(getId());
    }

    @Override
    public Integer getPrevDiplomaEduLevelId() {
        return trainingCourseRecord.getPrevDiplEduLevelId();
    }
    @Override
    public FlatNomenclature getPrevDiplomaEduLevel() {
        return getPrevDiplomaEduLevelId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getPrevDiplomaEduLevelId());
    }

    @Override
    public Date getPrevDiplomaGraduationDate() {
        return trainingCourseRecord.getPrevDiplGraduationDate();
    }

    @Override
    public String getPrevDiplomaNotes() {
        return trainingCourseRecord.getPrevDiplNotes();
    }
    public Integer getPrevDiplomaSpecialityId() {
    	return trainingCourseRecord.getPrevDiplSpecialityId();
    }
    public Speciality getPrevDiplomaSpeciality() {
    	return getPrevDiplomaSpecialityId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getSpeciality(getPrevDiplomaSpecialityId());
    }
    @Override
    public University getPrevDiplomaUniversity() {
        Integer uniId = trainingCourseRecord.getPrevDiplUniversityId();
        if(uniId == null) {
            return null;
        }
        UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        return universityDataProvider.getUniversity(uniId);
    }
    
    @Override
    public Integer getPrevDiplomaUniversityId() {
        return trainingCourseRecord.getPrevDiplUniversityId();
    }

    @Override
    public Integer getSchoolCountryId() {
        return trainingCourseRecord.getSchoolCountryId();
    }
    
    public Country getSchoolCountry() {
        return trainingCourseRecord.getSchoolCountryId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(getSchoolCountryId());
    }

    @Override
    public Date getSchoolGraduationDate() {
        return trainingCourseRecord.getSchoolGraduationDate();
    }

    @Override
    public String getSchoolName() {
        return trainingCourseRecord.getSchoolName();
    }

    @Override
    public String getSchoolNotes() {
        return trainingCourseRecord.getSchoolNotes();
    }

    @Override
    public String getSchoolCity() {
        return trainingCourseRecord.getSchoolCity();
    }

    @Override
    public synchronized List<Integer> getRecognizedSpecialityIds() {
        
        if(!isRecognizedSpecialitiesFilled) {
            getRecognizedSpecialities();
        }
        return recognizedSpecialityIds;
    }
    @Override
    public synchronized List<Speciality> getRecognizedSpecialities() {
    	if (!isRecognizedSpecialitiesFilled) {
    	    recognizedSpecialities = nacidDataProvider.getTrainingCourseDataProvider().loadRecognizedSpecialities(getId());
    	    recognizedSpecialityIds = new ArrayList<Integer>();
            if (recognizedSpecialities != null) {
                for(Speciality s : recognizedSpecialities) {
                    recognizedSpecialityIds.add(s.getId());
                }    
            }
    	    isRecognizedSpecialitiesFilled = true;
        }
        return recognizedSpecialities;
    }
    
    /**
     * inicializira recognized specialities...
     * @param specialityIds
     */
    public synchronized void fillRecognizedSpecialities(List<Integer> specialityIds) {
        isRecognizedSpecialitiesFilled = true;
        if (specialityIds != null) {
            recognizedSpecialities = new ArrayList<Speciality>();
            for (Integer i:specialityIds) {
                recognizedSpecialities.add(nacidDataProvider.getNomenclaturesDataProvider().getSpeciality(i));
            }
            recognizedSpecialityIds = specialityIds;
        }
    }

    @Override
    public Integer getRecognizedEduLevelId() {
        return trainingCourseRecord.getRecognizedEduLevel();
    }
    public FlatNomenclature getRecognizedEducationLevel() {
    	return getRecognizedEduLevelId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getRecognizedEduLevelId());
    }

    /*@Override
    public Integer getTrainingInstId() {
        return trainingCourseRecord.getTrainingInstId();
    }
    
    public TrainingInstitution getTrainingInstitution() {
    	return getTrainingInstId() == null ? null : nacidDataProvider.getTrainingInstitutionDataProvider().selectTrainingInstitution(getTrainingInstId());
    }*/

    @Override
    public Integer getRecognizedQualificationId() {
        return trainingCourseRecord.getRecognizedQualification();
    }

    @Override
    public Qualification getRecognizedQualification() {
        if (getRecognizedQualificationId() == null) {
            return null;
        }
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return (Qualification)nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION, getRecognizedQualificationId());
    }

	/*public List<University> getUniversities() {
		return nacidDataProvider.getUniversityDataProvider().getUniversities(getId());
	}*/

	
	public UniversityExamination getUniversityExaminationByUniversity(int universityId) {
		synchronized (universityExaminations) {
        	if (!isUniversityExaminationRead) {
            	getUniversityExaminations();
            	
            }
        	return universityExaminations.get(universityId);
		}
	}

	public boolean isRecognizedByHeadQuarter() {
		boolean universityExaminationIsRecognized = false;
		List<UniversityWithFaculty> universities = getUniversityWithFaculties();
        //zaqvlenieto e priznato samo ako za vseki vyveden University ima vyveden UniversityExamination i negoviq status isRecognized e true
        if (universities != null) {
        	universityExaminationIsRecognized = true;
        	for (UniversityWithFaculty u : universities) {
        		UniversityExamination examination = getUniversityExaminationByUniversity(u.getUniversity().getId());
        		//UniversityValidity validity = examination == null ? null : examination.getUniversityValidity();
        		if (examination == null || !examination.isRecognized()) {
        			universityExaminationIsRecognized = false;
        			break;
        		}
        	}
        }
        return universityExaminationIsRecognized;
	}

	public UniversityValidity getUniversityValidity(int universityId) {
		UniversityExamination ex = getUniversityExaminationByUniversity(universityId);
		return ex == null ? null : ex.getUniversityValidity();
	}
	public synchronized List<TrainingCourseSpeciality> getSpecialities() {
	    if (!isSpecialityIdsRead) {
	        specialities = trainingCourseDataProvider.loadSpecialities(getId());
	        isSpecialityIdsRead = true;
	    }
	    return specialities;
	}

    @Override
    public String getSpecialityNamesSeparatedBySemiColon() {
        List<TrainingCourseSpeciality> specs = getSpecialities();
        if (specs == null || specs.size() == 0) {
            return "";
        }
        List<String> result = new ArrayList<String>();
        NomenclaturesDataProvider ndp = nacidDataProvider.getNomenclaturesDataProvider();
        for (TrainingCourseSpeciality tcs:specs) {
            Speciality s = ndp.getSpeciality(tcs.getSpecialityId());
            FlatNomenclature originalSpeciality = tcs.getOriginalSpecialityId() == null ? null : ndp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY, tcs.getOriginalSpecialityId());
            result.add(s.getName() + (originalSpeciality == null ? "" : " / " + originalSpeciality.getName()));
        }
        return StringUtils.join(result, "; ");
    }

    @Override
    public Integer getGraduationDocumentTypeId() {
        return trainingCourseRecord.getGraduationDocumentTypeId();
    }

    @Override
    public FlatNomenclature getGraduationDocumentType() {
        return trainingCourseRecord.getGraduationDocumentTypeId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE, trainingCourseRecord.getGraduationDocumentTypeId());
    }

    @Override
    public Integer getCreditHours() {
        return trainingCourseRecord.getCreditHours();
    }

    @Override
    public Integer getEctsCredits() {
        return trainingCourseRecord.getEctsCredits();
    }

    @Override
    public int getOwnerId() {
        return trainingCourseRecord.getOwnerId();
    }

    @Override
    public Person getOwner() {
        if (owner == null) {
            synchronized (this) {
                if (owner == null) {
                    owner = nacidDataProvider.getApplicationsDataProvider().getPerson(getOwnerId());
                }
            }
        }
        return owner;
    }

    @Override
    public UniversityWithFaculty getBaseUniversityWithFaculty() {
        List<UniversityWithFaculty> unis = getUniversityWithFaculties();
        return unis.size() == 0 ? null : unis.get(0);
    }

    @Override
    public List<UniversityWithFaculty> getJointUniversityWithFaculties() {
        List<UniversityWithFaculty> unis = getUniversityWithFaculties();
        return unis.size() <= 1 ? null : unis.subList(1, unis.size());
    }

    public List<UniversityWithFaculty> getUniversityWithFaculties() {
	    if (universityWithFaculties == null) {
            UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
            universityWithFaculties =  universityDataProvider.getUniversityWithFaculties(getId());
        }
        return universityWithFaculties;
    }

    @Override
    public String getThesisTopic() {
        return trainingCourseRecord.getThesisTopic();
    }

    @Override
    public String getThesisTopicEn() {
        return trainingCourseRecord.getThesisTopicEn();
    }

    @Override
    public Integer getProfGroupId() {
        return trainingCourseRecord.getProfGroupId();
    }

    @Override
    public ProfessionGroup getProfGroup() {
        return getProfGroupId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getProfessionGroup(getProfGroupId());
    }

    @Override
    public Integer getRecognizedProfGroupId() {
        return trainingCourseRecord.getRecognizedProfGroupId();
    }

    @Override
    public ProfessionGroup getRecognizedProfGroup() {
        return getRecognizedProfGroupId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getProfessionGroup(getRecognizedProfGroupId());
    }

    @Override
    public boolean hasEducationPeriodInformation() {
        return getEducationLevelId() == null || getEducationLevelId() != EducationLevel.EDUCATION_LEVEL_DOCTOR_OF_SCIENCE;
    }

    @Override
    public Date getThesisDefenceDate() {
        return trainingCourseRecord.getThesisDefenceDate();
    }
    @Override
    public Integer getThesisBibliography() {
        return trainingCourseRecord.getThesisBibliography();
    }

    @Override
    public Integer getThesisVolume() {
        return trainingCourseRecord.getThesisVolume();
    }

    @Override
    public Integer getThesisLanguageId() {
        return trainingCourseRecord.getThesisLanguageId();
    }
    public Language getThesisLanguage() {
	    return trainingCourseRecord.getThesisLanguageId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getLanguage(trainingCourseRecord.getThesisLanguageId());
    }

    @Override
    public String getThesisAnnotation() {
        return trainingCourseRecord.getThesisAnnotation();
    }

    @Override
    public String getThesisAnnotationEn() {
        return trainingCourseRecord.getThesisAnnotationEn();
    }

    @Override
    public Integer getOriginalQualificationId() {
        return trainingCourseRecord.getOriginalQualificationId();
    }

    @Override
    public FlatNomenclature getOriginalQualification() {
        return trainingCourseRecord.getOriginalQualificationId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION, trainingCourseRecord.getOriginalQualificationId());
    }
}
