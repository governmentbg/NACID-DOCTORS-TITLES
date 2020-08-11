package com.nacid.bl.impl.external.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.applications.ExtTrainingCourse;
import com.nacid.bl.external.applications.ExtTrainingCourseTrainingLocation;
import com.nacid.bl.external.applications.ExtUniversityWithFaculty;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.data.external.applications.ExtDiplomaIssuerRecord;
import com.nacid.data.external.applications.ExtTrainingCourseRecord;
import com.nacid.data.external.applications.ExtTrainingCourseSpecialityRecord;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class ExtTrainingCourseImpl implements ExtTrainingCourse {

    private int id;
    private String diplomaSeries;
    private String diplomaNum;
    private String diplomaRegistrationNumber;
    private Date diplomaDate;
    //private Integer universityId;
    //private String universityTxt;
    private String fname;
    private String sname;
    private String lname;
    private Date trainingStart;
    private Date trainingEnd;
    private Double trainingDuration;
    private Integer durationUnitId;
    private BigDecimal credits;
    //private Integer specialityId;
    //private String specialityTxt;
    private Integer eduLevelId;
    private Integer qualificationId;
    private String qualificationTxt;
    private Integer schoolCountry;
    private String schoolCity;
    private String schoolName;
    private Date schoolGraduationDate;
    private String schoolNotes;
    private Integer prevDiplomaCountry;
    private String prevDiplomaCity;
    private Integer prevDiplomaUniversityId;
    private String prevDiplomaUiniversityTxt;
    private Integer prevDiplomaEduLevelId;
    private Date prevDiplomaGraduationDate;
    private String prevDiplomaNotes;
    //private Integer trainingInstId;
    //private String trainingInstTxt;
    private List<ExtDiplomaIssuerRecord> extDiplomaIssuers;
    private NacidDataProviderImpl nacidDataProvider;
    private boolean jointDegree;
    private Integer prevDiplomaSpecialityId;
    private String prevDiplomaSpecialityTxt;

    boolean isSpecialityIdsRead;
    private List<Speciality> specialities;
    private List<Integer> specialityIds;
    private List<ExtTrainingCourseSpecialityRecord> trainingCourseSpecialities;
    private Integer graduationDocumentTypeId;
    private Integer creditHours;
    private Integer ectsCredits;
    private int ownerId;
    private ExtPerson owner;
    private String thesisTopic;
    private String thesisTopicEn;
    private Integer profGroupId;
    private Date thesisDefenceDate;
    private Integer thesisBibliography;
    private Integer thesisVolume;
    private Integer thesisLanguageId;
    private String thesisAnnotation;
    private String thesisAnnotationEn;


    public ExtTrainingCourseImpl(ExtTrainingCourseRecord rec, List<ExtDiplomaIssuerRecord> extDiplomaIssuers, List<ExtTrainingCourseSpecialityRecord> trainingCourseSpecialities,
                                 NacidDataProviderImpl nacidDataProvider) {
        this.id = rec.getId();
        this.diplomaSeries = rec.getDiplomaSeries();
        this.diplomaNum = rec.getDiplomaNum();
        this.diplomaRegistrationNumber = rec.getDiplomaRegistrationNumber();
        this.diplomaDate = rec.getDiplomaDate();
        //this.universityId = rec.getUniversityId();
        //this.universityTxt = rec.getUniversityTxt();
        this.fname = rec.getFname();
        this.sname = rec.getSname();
        this.lname = rec.getLname();
        this.trainingStart = rec.getTrainingStart();
        this.trainingEnd = rec.getTrainingEnd();
        this.trainingDuration = rec.getTrainingDuration();
        this.durationUnitId = rec.getDurationUnitId();
        this.credits = rec.getCredits();
        //this.specialityId = rec.getSpecialityId();
        //this.specialityTxt = rec.getSpecialityTxt();
        this.eduLevelId = rec.getEduLevelId();
        this.qualificationId = rec.getQualificationId();
        this.qualificationTxt = rec.getQualificationTxt();
        this.schoolCountry = rec.getSchoolCountry();
        this.schoolCity = rec.getSchoolCity();
        this.schoolName = rec.getSchoolName();
        this.schoolGraduationDate = rec.getSchoolGraduationDate();
        this.schoolNotes = rec.getSchoolNotes();
        this.prevDiplomaCountry = rec.getPrevDiplomaCountry();
        this.prevDiplomaCity = rec.getPrevDiplomaCity();
        this.prevDiplomaUniversityId = rec.getPrevDiplomaUniversityId();
        this.prevDiplomaUiniversityTxt = rec.getPrevDiplomaUiniversityTxt();
        this.prevDiplomaEduLevelId = rec.getPrevDiplomaEduLevelId();
        this.prevDiplomaGraduationDate = rec.getPrevDiplomaGraduationDate();
        this.prevDiplomaNotes = rec.getPrevDiplomaNotes();
        this.prevDiplomaSpecialityId = rec.getPrevDiplomaSpecialityId();
        this.prevDiplomaSpecialityTxt = rec.getPrevDiplomaSpecialityTxt();
        //this.trainingInstId = rec.getTrainingInstId();
        //this.trainingInstTxt = rec.getTrainingInstTxt();
        this.nacidDataProvider = nacidDataProvider;
        this.extDiplomaIssuers = extDiplomaIssuers;
        this.jointDegree = rec.getIsJointDegree() == null || rec.getIsJointDegree() != 1 ? false : true;
        this.trainingCourseSpecialities = trainingCourseSpecialities;
        this.graduationDocumentTypeId = rec.getGraduationDocumentTypeId();
        this.creditHours = rec.getCreditHours();
        this.ectsCredits = rec.getEctsCredits();
        this.ownerId = rec.getOwnerId();
        this.thesisTopic = rec.getThesisTopic();
        this.thesisTopicEn = rec.getThesisTopicEn();
        this.profGroupId = rec.getProfGroupId();
        this.thesisDefenceDate = rec.getThesisDefenceDate();
        this.thesisBibliography = rec.getThesisBibliography();
        this.thesisLanguageId = rec.getThesisLanguageId();
        this.thesisVolume = rec.getThesisVolume();
        this.thesisAnnotation = rec.getThesisAnnotation();
        this.thesisAnnotationEn = rec.getThesisAnnotationEn();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDiplomaNumber() {
        return diplomaNum;
    }

    @Override
    public String getDiplomaSeries() {
        return diplomaSeries;
    }

    @Override
    public String getDiplomaRegistrationNumber() {
        return diplomaRegistrationNumber;
    }

    @Override
    public Date getDiplomaDate() {
        return diplomaDate;
    }

    /*@Override
    public String getBaseUniversityTxt() {
        return universityTxt;
    }*/

    @Override
    public String getFName() {
        return fname;
    }

    @Override
    public String getSName() {
        return sname;
    }

    @Override
    public String getLName() {
        return lname;
    }

    public String getFullName() {
        return (StringUtils.isEmpty(getFName()) ? "" : getFName() + " ") + (StringUtils.isEmpty(getSName()) ? "" : getSName() + " ") + (StringUtils.isEmpty(getLName()) ? "" : getLName());
    }

    /*@Override
    public Integer getTrainingLocationCountryId() {
        return trainingLocationCountryId;
    }

    @Override
    public String getTrainingLocationCity() {
        return trainingLocationCity;
    }
    public Country getTrainingLocationCountry() {
		return getTrainingLocationCountryId() == null ? null :nacidDataProvider.getNomenclaturesDataProvider().getCountry(getTrainingLocationCountryId());
	}
    */
    public List<? extends ExtTrainingCourseTrainingLocation> getTrainingCourseTrainingLocations() {
        return nacidDataProvider.getExtTrainingCourseDataProvider().getTrainingCourseTrainingLocations(getId());

    }

    @Override
    public Date getTrainingStart() {
        return trainingStart;
    }

    @Override
    public Date getTrainingEnd() {
        return trainingEnd;
    }

    @Override
    public Double getTrainingDuration() {
        return trainingDuration;
    }

    @Override
    public Integer getDurationUnitId() {
        return durationUnitId;
    }

    @Override
    public java.math.BigDecimal getCredits() {
        return credits;
    }

    /*@Override
    public Integer getSpecialityId() {
        return specialityId;
    }*/

/*    @Override
    public String getSpecialityTxt() {
        return specialityTxt;
    }*/

    @Override
    public Integer getEducationLevelId() {
        return eduLevelId;
    }

    @Override
    public Integer getQualificationId() {
        return qualificationId;
    }

    @Override
    public String getQualificationTxt() {
        return qualificationTxt;
    }

    @Override
    public Integer getSchoolCountryId() {
        return schoolCountry;
    }

    @Override
    public String getSchoolCity() {
        return schoolCity;
    }

    @Override
    public String getSchoolName() {
        return schoolName;
    }

    @Override
    public Date getSchoolGraduationDate() {
        return schoolGraduationDate;
    }

    @Override
    public String getSchoolNotes() {
        return schoolNotes;
    }

    @Override
    public Integer getPrevDiplomaCountry() {
        return prevDiplomaCountry;
    }

    @Override
    public String getPrevDiplomaCity() {
        return prevDiplomaCity;
    }

    @Override
    public Integer getPrevDiplomaUniversityId() {
        return prevDiplomaUniversityId;
    }

    @Override
    public String getPrevDiplomaUiniversityTxt() {
        return prevDiplomaUiniversityTxt;
    }

    @Override
    public Integer getPrevDiplomaEduLevelId() {
        return prevDiplomaEduLevelId;
    }

    @Override
    public Date getPrevDiplomaGraduationDate() {
        return prevDiplomaGraduationDate;
    }

    @Override
    public String getPrevDiplomaNotes() {
        return prevDiplomaNotes;
    }

    public Integer getPrevDiplomaSpecialityId() {
        return prevDiplomaSpecialityId;
    }

    public Speciality getPrevDiplomaSpeciality() {
        return getPrevDiplomaSpecialityId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getSpeciality(getPrevDiplomaSpecialityId());
    }

    public String getPrevDiplomaSpecialityTxt() {
        return prevDiplomaSpecialityTxt;
    }
    /*@Override
    public Integer getTrainingInstId() {
        return trainingInstId;
    }
    public TrainingInstitution getTrainingInstitution() {
    	return getTrainingInstId() == null ? null : nacidDataProvider.getTrainingInstitutionDataProvider().selectTrainingInstitution(getTrainingInstId());
    }

    public String getTrainingInstTxt() {
        return trainingInstTxt;
    }*/


    public FlatNomenclature getDurationUnit() {
        return getDurationUnitId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_DURATION_UNIT, getDurationUnitId());
    }

    public FlatNomenclature getEducationLevel() {
        return getEducationLevelId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getEducationLevelId());
    }

    public FlatNomenclature getPrevDiplomaEduLevel() {
        return getPrevDiplomaEduLevelId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getPrevDiplomaEduLevelId());
    }

    public University getPrevDiplomaUniversity() {
        return getPrevDiplomaUniversityId() == null ? null : nacidDataProvider.getUniversityDataProvider().getUniversity(getPrevDiplomaUniversityId());
    }

    public FlatNomenclature getQualification() {
        return getQualificationId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION, getQualificationId());
    }

    public Country getSchoolCountry() {
        return getSchoolCountryId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(getSchoolCountryId());
    }

	/*public Speciality getSpeciality() {
		return getSpecialityId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getSpeciality(getSpecialityId());
	}*/


    public boolean isJointDegree() {
        return jointDegree;
    }

    public List<ExtTrainingCourseSpecialityRecord> getTrainingCourseSpecialities() {
        return trainingCourseSpecialities;
    }

    @Override
    public synchronized List<Speciality> getSpecialities() {
        if (!isSpecialityIdsRead) {
            specialities = nacidDataProvider.getExtTrainingCourseDataProvider().loadSpecialities(getId());
            if (specialities != null) {
                specialityIds = new ArrayList<Integer>();
                for (Speciality s : specialities) {
                    specialityIds.add(s.getId());
                }
            }
            isSpecialityIdsRead = true;
        }
        return specialities;
    }

    @Override
    public synchronized List<Integer> getSpecialityIds() {
        if (!isSpecialityIdsRead) {
            getSpecialities();
        }
        return specialityIds;
    }

    public String getSpecialityNamesSeparatedBySemiColon() {
        List<Speciality> specs = getSpecialities();
        if (specs == null || specs.size() == 0) {
            return "";
        }
        List<String> result = new ArrayList<String>();
        for (Speciality s : specs) {
            result.add(s.getName());
        }
        return StringUtils.join(result, "; ");
    }

    public String getSpecialityTxtSeparatedBySemicolon() {
        List<ExtTrainingCourseSpecialityRecord> specialities = nacidDataProvider.getExtTrainingCourseDataProvider().loadTxtSpecialities(getId());
        if (specialities == null || specialities.size() == 0) {
            return "";
        }
        List<String> result = new ArrayList<String>();
        for (ExtTrainingCourseSpecialityRecord speciality : specialities) {
            result.add(speciality.getSpecialityTxt());
        }
        return StringUtils.join(result, "; ");
    }

    @Override
    public Integer getGraduationDocumentTypeId() {
        return graduationDocumentTypeId;
    }

    @Override
    public FlatNomenclature getGraduationDocumentType() {
        return graduationDocumentTypeId == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE, graduationDocumentTypeId);
    }

    @Override
    public Integer getCreditHours() {
        return creditHours;
    }

    public Integer getEctsCredits() {
        return ectsCredits;
    }

    @Override
    public ExtPerson getOwner() {
        if (owner == null) {
            synchronized (this) {
                if (owner == null) {
                    owner = nacidDataProvider.getExtPersonDataProvider().getExtPerson(getOwnerId());
                }
            }
        }
        return owner;
    }

    @Override
    public int getOwnerId() {
        return ownerId;
    }


    @Override
    public ExtUniversityWithFaculty getBaseUniversityWithFaculty() {
        ExtDiplomaIssuerRecord r = extDiplomaIssuers == null ? null : extDiplomaIssuers.get(0);
        if (r == null) {
            return null;
        }
        return toExtUniversityWithFaculty(r, nacidDataProvider);
    }

    @Override
    public List<ExtUniversityWithFaculty> getJointUniversityWithFaculties() {
        if (!isJointDegree() || extDiplomaIssuers == null || extDiplomaIssuers.size() <= 1) {
            return null;
        }
        return prepareExtUnis(extDiplomaIssuers.subList(1, extDiplomaIssuers.size()), nacidDataProvider);
    }

    @Override
    public List<ExtUniversityWithFaculty> getUniversityWithFaculties() {
        return prepareExtUnis(extDiplomaIssuers, nacidDataProvider);
    }

    private static List<ExtUniversityWithFaculty> prepareExtUnis(List<ExtDiplomaIssuerRecord> issuers, NacidDataProvider nacidDataProvider) {
        if (issuers == null) {
            return null;
        }
        List<ExtUniversityWithFaculty> result = new ArrayList<>();
        for (ExtDiplomaIssuerRecord r : issuers) {
            result.add(toExtUniversityWithFaculty(r, nacidDataProvider));
        }
        return result;
    }
    private static ExtUniversityWithFaculty toExtUniversityWithFaculty(ExtDiplomaIssuerRecord r, NacidDataProvider nacidDataProvider) {
        University u = r.getUniversityId() == null ? null : nacidDataProvider.getUniversityDataProvider().getUniversity(r.getUniversityId());
        UniversityFaculty f = r.getFacultyId() == null ? null : nacidDataProvider.getUniversityDataProvider().getUniversityFaculty(r.getFacultyId());
        ExtUniversityImpl extUni = new ExtUniversityImpl(u, r);
        ExtUniversityFacultyImpl extFac = new ExtUniversityFacultyImpl(f, r);
        return new ExtUniversityWithFaculty(extUni, extFac);
    }

    @Override
    public String getThesisTopic() {
        return thesisTopic;
    }

    @Override
    public String getThesisTopicEn() {
        return thesisTopicEn;
    }

    @Override
    public Integer getProfGroupId() {
        return profGroupId;
    }

    @Override
    public ProfessionGroup getProfGroup() {
        return getProfGroupId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getProfessionGroup(getProfGroupId());
    }

    @Override
    public boolean hasEducationPeriodInformation() {
        return getEducationLevelId() == null || getEducationLevelId() != EducationLevel.EDUCATION_LEVEL_DOCTOR_OF_SCIENCE;
    }

    @Override
    public Date getThesisDefenceDate() {
        return thesisDefenceDate;
    }

    @Override
    public Integer getThesisBibliography() {
        return thesisBibliography;
    }

    @Override
    public Integer getThesisVolume() {
        return thesisVolume;
    }

    @Override
    public Integer getThesisLanguageId() {
        return thesisLanguageId;
    }

    public Language getThesisLanguage() {
        return thesisLanguageId == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getLanguage(thesisLanguageId);
    }

    @Override
    public String getThesisAnnotation() {
        return thesisAnnotation;
    }

    @Override
    public String getThesisAnnotationEn() {
        return thesisAnnotationEn;
    }

    @Override
    public Integer getOriginalQualificationId() {
        return null;
    }

    @Override
    public FlatNomenclature getOriginalQualification() {
        return null;
    }
}
