package com.nacid.bl.impl.regprof;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitutionValidity;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.regprof.ProfessionalInstitutionNamesRecord;
import com.nacid.db.regprof.ProfessionalInstitutionDB;
//RayaWritten----------------------------------------------------------------------------------------
public class ProfessionalInstitutionDataProviderImpl implements ProfessionalInstitutionDataProvider {
    
    private NacidDataProviderImpl nacidDataProvider;
    private ProfessionalInstitutionDB db;
    
    public ProfessionalInstitutionDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new ProfessionalInstitutionDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    
    @Override
    public ProfessionalInstitution saveProfessionalInstitutionRecord(ProfessionalInstitution info) {
        try {
            if (info.getId() == null || info.getId() == 0) {
                info = db.insertRecord(info);
            } else {
                db.updateRecord(info);
            }
            return info;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public ProfessionalInstitution getProfessionalInstitution(int id) {
        try {
            return db.selectRecord(new ProfessionalInstitutionImpl(), id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<ProfessionalInstitutionImpl> getProfessionalInstitutions() {
        try {
            return db.selectRecords(ProfessionalInstitutionImpl.class, null);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<ProfessionalInstitutionImpl> getProfessionalInstitutions(String partOfBgName, Integer institutionType, Integer educationType) {
        try{
            List<ProfessionalInstitutionImpl> institutions = db.getProfessionalInstitutions(partOfBgName, institutionType, educationType);
            return institutions;
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    @Override
    public void disableProfessionalInstitution(int id) {
        try {
            ProfessionalInstitution record = db.getProfessionalInstitutionRecord(id);
            record.setDateTo(new Date());
            db.updateRecord(record);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public void disableProfessionalInstitutionName(ProfessionalInstitutionNamesRecord record) {
        try {
            List<RegprofTrainingCourseDetailsImpl> list = 
                    db.selectRecords(RegprofTrainingCourseDetailsImpl.class, " prof_institution_org_name_id = ? OR sdk_prof_institution_org_name_id = ? ", record.getId(), record.getId());
            if (list == null || list.isEmpty()) { // not referenced from regprof.training_course
                db.deleteRecord(ProfessionalInstitutionNamesRecord.class, record.getId());
            } else { // referenced from regprof.training_course
                record.setActive(0);
                db.updateRecord(record);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<ProfessionalInstitutionValidityImpl> getProfessionalInstitutionValidities(Integer institutionId, Integer qualificationId, Integer qualificationType) {
        List<ProfessionalInstitutionValidityImpl> validities = null;
        String condition = " professional_institution_id = ? ";
        if (qualificationType == EducationType.EDU_TYPE_HIGH || qualificationType == EducationType.EDU_TYPE_SDK) {
            condition += " AND qualification_bulgaria_high_sdk_id = ? ";
        } else if (qualificationType == EducationType.EDU_TYPE_SECONDARY || qualificationType == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL) {
            condition += " AND qualification_bulgaria_sec_id = ? ";
        } else {
            return null;
        }
        try {
            validities = db.selectRecords(ProfessionalInstitutionValidityImpl.class, condition, institutionId, qualificationId);
            return validities;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }       
    }
    
    @Override
    public ProfessionalInstitutionValidity saveProfessionalInstitutionValidity(ProfessionalInstitutionValidity record){
        try{
            if(record.getId() == null){
                record = db.insertRecord(record);
            } else {
                db.updateRecord(record);
            }
            return record;
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    public ProfessionalInstitutionValidity getProfessionalInstitutionValidityById(Integer validityId){
        try{
            return db.selectRecord(new ProfessionalInstitutionValidityImpl(), validityId);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    public List<ProfessionalInstitutionNamesRecord> getProfessionalInstitutionNames(Integer professionalInstitutionId, String partOfName, boolean onlyActive) {
        try {
            return db.getProfessionalInstitutionNames(professionalInstitutionId, partOfName, onlyActive);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    public void deleteProfessionalInstitutionNames(Integer professionalInstitutionId) {
        try {
            db.deleteRecords(ProfessionalInstitutionNamesRecord.class, " professional_institution_id = ? ", professionalInstitutionId);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    public Integer saveProfessionalInstitutionName(ProfessionalInstitutionNamesRecord record) {
        try {
            ProfessionalInstitutionNamesRecord newRecord = db.insertRecord(record);
            return newRecord != null ? newRecord.getId() : null;
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    @Override
    public String getProfessionalInstitutionFormerName(Integer formerNameId) {
        try {
            ProfessionalInstitutionNamesRecord record = db.selectRecord(new ProfessionalInstitutionNamesRecord(), formerNameId);
            return record != null ? record.getFormerName() : "";
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    public Integer getInstitutionType(Integer educationType) {
        try {
            List<IntegerValue> institutionTypeAsList =
                    db.selectRecords("SELECT institution_type AS value FROM regprof.education_type_to_institution_type WHERE education_type = ?", IntegerValue.class, educationType);
            if (institutionTypeAsList != null && !institutionTypeAsList.isEmpty()) {
                IntegerValue institutionType = Utils.getListFirstElement(institutionTypeAsList);
                return institutionType.getValue();
            }
        } catch(SQLException e){
            throw Utils.logException(e);
        }
        return null;
    }
}
//-----------------------------------------------------------------------
