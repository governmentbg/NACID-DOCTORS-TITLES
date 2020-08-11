package com.nacid.bl.regprof;

import java.util.List;

import com.nacid.bl.impl.regprof.ProfessionalInstitutionImpl;
import com.nacid.bl.impl.regprof.ProfessionalInstitutionValidityImpl;
import com.nacid.data.regprof.ProfessionalInstitutionNamesRecord;
//RayaWritten--------------------------------------------------------
public interface ProfessionalInstitutionDataProvider {

    public static final int SECONDARY_PROFESSIONAL_INSTITUTION_TYPE = 1;
    public static final int PROFESSIONAL_EDUCATION_CENTER_INSTITUTION_TYPE = 2;
    public static final int HIGHER_INSTITUTION_TYPE = 3;
    public static final int SDK_INSTITUTION_TYPE = 4;
    
    public ProfessionalInstitution saveProfessionalInstitutionRecord(ProfessionalInstitution info);
    public ProfessionalInstitution getProfessionalInstitution(int id);
    public List<ProfessionalInstitutionImpl> getProfessionalInstitutions();
    public List<ProfessionalInstitutionImpl> getProfessionalInstitutions(String partOfBgName, Integer institutionType, Integer educationType);
    public void disableProfessionalInstitution(int id);
    public List<ProfessionalInstitutionValidityImpl> getProfessionalInstitutionValidities(Integer institutionId,Integer qualificationId, Integer qualificationType);
    public ProfessionalInstitutionValidity saveProfessionalInstitutionValidity(ProfessionalInstitutionValidity record);
    public ProfessionalInstitutionValidity getProfessionalInstitutionValidityById(Integer validityId);
    public String getProfessionalInstitutionFormerName(Integer formerNameId);
    public List<ProfessionalInstitutionNamesRecord> getProfessionalInstitutionNames(Integer professionalInstitutionId, String partOfName, boolean onlyActive);
    public void deleteProfessionalInstitutionNames(Integer professionalInstitutionId);
    public Integer saveProfessionalInstitutionName(ProfessionalInstitutionNamesRecord record);
    public void disableProfessionalInstitutionName(ProfessionalInstitutionNamesRecord record);
    public Integer getInstitutionType(Integer educationType);
}
//------------------------------------------------------------------------