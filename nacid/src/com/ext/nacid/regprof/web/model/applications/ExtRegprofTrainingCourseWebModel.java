package com.ext.nacid.regprof.web.model.applications;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofTrainingCourse;
import com.nacid.data.regprof.external.ExtRegprofProfessionExperienceRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseSpecialitiesRecord;

public class ExtRegprofTrainingCourseWebModel {
    private String profInstitutionNameTxt;
    private String profInstitutionOrgNameTxt;
    private String highProfQualificationTxt;
    private String sdkProfInstitutionNameTxt;
    private String sdkProfInstitutionOrgNameTxt;
    private String sdkProfQualificationTxt;
    private String secProfQualificationTxt;
    private String professionExperienceTxt;
    private String certificateProfQualificationTxt;
    private boolean high;
    private boolean sdk;
    private boolean secondary;
    private List<ExtRegprofTrainingCourseSpecialitiesWebModel> secondarySpecialities = new ArrayList<ExtRegprofTrainingCourseSpecialitiesWebModel>();
    private List<ExtRegprofTrainingCourseSpecialitiesWebModel> higherSpecialities = new ArrayList<ExtRegprofTrainingCourseSpecialitiesWebModel>();;
    private List<ExtRegprofTrainingCourseSpecialitiesWebModel> sdkSpecialities = new ArrayList<ExtRegprofTrainingCourseSpecialitiesWebModel>();
    private int specialitiesCount;

    public ExtRegprofTrainingCourseWebModel(NacidDataProvider nDP, ExtRegprofTrainingCourse record) {
        ExtRegprofTrainingCourseRecord details = record.getDetails();
        ExtRegprofProfessionExperienceRecord experienceRecord = record.getExperienceRecord();
        ProfessionalInstitutionDataProvider professionalInstitutionDataProvider = nDP.getProfessionalInstitutionDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nDP.getNomenclaturesDataProvider();
        if (details != null) {
            
            profInstitutionNameTxt = getProfessionalInstitutionName(professionalInstitutionDataProvider, details.getProfInstitutionId(), details.getProfInstitutionNameTxt());
            profInstitutionOrgNameTxt = getInstitutionOriginalName(professionalInstitutionDataProvider, details.getProfInstitutionOrgNameId(), details.getProfInstitutionOrgNameTxt());
            highProfQualificationTxt = getFlatNomenclatureValue(nomenclaturesDataProvider, NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getHighProfQualificationId(), details.getHighProfQualificationTxt());

            sdkProfInstitutionNameTxt = getProfessionalInstitutionName(professionalInstitutionDataProvider, details.getSdkProfInstitutionId(), details.getSdkProfInstitutionNameTxt());
            sdkProfInstitutionOrgNameTxt = getInstitutionOriginalName(professionalInstitutionDataProvider, details.getSdkProfInstitutionOrgNameId(), details.getSdkProfInstitutionOrgNameTxt());
            sdkProfQualificationTxt = getFlatNomenclatureValue(nomenclaturesDataProvider, NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getSdkProfQualificationId(), details.getSdkProfQualificationTxt());
            secProfQualificationTxt = getSecondaryProfQualificationValue(nomenclaturesDataProvider, details.getSecProfQualificationId(), details.getSecProfQualificationTxt());
            certificateProfQualificationTxt = getFlatNomenclatureValue(nomenclaturesDataProvider, NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, details.getCertificateProfQualificationId(), details.getCertificateProfQualificationTxt());

            if (experienceRecord != null) {
                professionExperienceTxt = getFlatNomenclatureValue(nomenclaturesDataProvider, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, experienceRecord.getNomenclatureProfessionExperienceId(), experienceRecord.getProfessionExperienceTxt());    
            }
            if (details.getEducationTypeId() != null) {
                high = details.getEducationTypeId() == EducationType.EDU_TYPE_HIGH;
                sdk = details.getEducationTypeId() == EducationType.EDU_TYPE_SDK;
                secondary = details.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY || details.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL;    
            }
            
            
        }
        
        List<ExtRegprofTrainingCourseSpecialitiesRecord> specs = record.getSpecialities();
        if (specs != null) {
            specialitiesCount = specs.size();
            for (ExtRegprofTrainingCourseSpecialitiesRecord r:specs) {
                ExtRegprofTrainingCourseSpecialitiesWebModel wm = new ExtRegprofTrainingCourseSpecialitiesWebModel(nomenclaturesDataProvider, r);
                if (r.getSecondarySpecialityId() != null || !StringUtils.isEmpty(r.getSecondarySpecialityTxt())) {
                    secondarySpecialities.add(wm);
                } else if (r.getHigherSpecialityId() != null || !StringUtils.isEmpty(r.getHigherSpecialityTxt())) {
                    higherSpecialities.add(wm);
                } else if (r.getSdkSpecialityId() != null || !StringUtils.isEmpty(r.getSdkSpecialityTxt())) {
                    sdkSpecialities.add(wm);
                } else {
                    throw new RuntimeException("v speciality record trqbva da ima vyvedena pone edna ot specialnostite sredno/vishe/sdk");
                }
            }
        }
        
    }
    
    private static String getProfessionalInstitutionName(ProfessionalInstitutionDataProvider dp, Integer institutionId, String institutionTxt) {
        if (institutionId == null) {
            return institutionTxt;
        } else {
            ProfessionalInstitution institution = dp.getProfessionalInstitution(institutionId);
            return institution == null ? null : institution.getBgName();
        }
        
    }
    
    static String getInstitutionOriginalName(ProfessionalInstitutionDataProvider dp, Integer originalId, String originalName) {
        if (originalId == null) {
            return originalName;
        } else {
            return dp.getProfessionalInstitutionFormerName(originalId);
        }
    }
    static String getFlatNomenclatureValue(NomenclaturesDataProvider nDP, int nomenclatureType, Integer nomenclatureId, String nomenclatureTxt) {
        if (nomenclatureId == null) {
            return nomenclatureTxt;
        } else {
            FlatNomenclature nom = nDP.getFlatNomenclature(nomenclatureType, nomenclatureId);
            return nom == null ? null : nom.getName();
        }
    }
    
    static String getSecondaryProfQualificationValue (NomenclaturesDataProvider nDP, Integer nomenclatureId, String nomenclatureTxt) {
        SecondaryProfessionalQualification profQual = null;
        if (nomenclatureId == null) {
            return nomenclatureTxt;
        } else {
            profQual = nDP.getSecondaryProfessionalQualification(nomenclatureId);
            return profQual == null ? null : profQual.getName();
        }
    }
    public String getProfInstitutionNameTxt() {
        return profInstitutionNameTxt;
    }

    public String getProfInstitutionOrgNameTxt() {
        return profInstitutionOrgNameTxt;
    }

    public String getHighProfQualificationTxt() {
        return highProfQualificationTxt;
    }

    public String getSdkProfInstitutionNameTxt() {
        return sdkProfInstitutionNameTxt;
    }

    public String getSdkProfInstitutionOrgNameTxt() {
        return sdkProfInstitutionOrgNameTxt;
    }

    public String getSdkProfQualificationTxt() {
        return sdkProfQualificationTxt;
    }
    public String getSecProfQualificationTxt() {
        return secProfQualificationTxt;
    }

    public String getProfessionExperienceTxt() {
        return professionExperienceTxt;
    }

    public boolean isHigh() {
        return high;
    }

    public boolean isSdk() {
        return sdk;
    }

    public boolean isSecondary() {
        return secondary;
    }
    public List<ExtRegprofTrainingCourseSpecialitiesWebModel> getSecondarySpecialities() {
        return secondarySpecialities;
    }
    public int getSecondarySpecialitiesCount() {
        return secondarySpecialities.size();
    }
    public List<ExtRegprofTrainingCourseSpecialitiesWebModel> getHigherSpecialities() {
        return higherSpecialities;
    }
    public int getHigherSpecialitiesCount() {
        return higherSpecialities.size();
    }
    public List<ExtRegprofTrainingCourseSpecialitiesWebModel> getSdkSpecialities() {
        return sdkSpecialities;
    }
    public int getSdkSpecialitiesCount() {
        return sdkSpecialities.size();
    }
    public int getSpecialitiesCount() {
        return specialitiesCount;
    }
    public String getHigherSpecialityId() {
        return higherSpecialities.size() == 1 ? higherSpecialities.get(0).getHigherSpecialityId() : null;
    }
    public String getHigherSpeciality() {
        return higherSpecialities.size() == 1 ? higherSpecialities.get(0).getHigherSpecialityTxtToDisplay() : null;
    }
    public String getSecondarySpecialityId() {
        return secondarySpecialities.size() == 1 ? secondarySpecialities.get(0).getSecondarySpecialityId() : null;
    }
    public String getSecondarySpeciality(){
        int commaIndex = secondarySpecialities.get(0).getSecondarySpecialityTxtToDisplay().indexOf(',');
        if(commaIndex >0){
            return secondarySpecialities.size() == 1 ? secondarySpecialities.get(0).getSecondarySpecialityTxtToDisplay().
                substring(0, commaIndex) : null;
        } else {
            return secondarySpecialities.size() == 1 ? secondarySpecialities.get(0).getSecondarySpecialityTxtToDisplay() : null;  
        }
    }

    public String getSdkSpecialityId() {
        return sdkSpecialities.size() == 1 ? sdkSpecialities.get(0).getSdkSpecialityId() : null;
    }
    public String getSdkSpeciality() {
        return sdkSpecialities.size() == 1 ? sdkSpecialities.get(0).getSdkSpecialityTxtToDisplay() : null;
    }

    public String getCertificateProfQualificationTxt() {
        return certificateProfQualificationTxt;
    }
}
