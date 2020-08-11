package com.nacid.db.regprof;

import com.nacid.bl.impl.regprof.ProfessionalInstitutionImpl;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.data.regprof.ProfessionalInstitutionNamesRecord;
import com.nacid.db.utils.DatabaseService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//RayaWritten------------------------------------------------------
public class ProfessionalInstitutionDB extends DatabaseService {
    
    private static final String SELECT_PROFESSIONAL_INSTITUTIONS = "SELECT * FROM regprof.professional_institution ORDER BY id DESC";
        /*"SELECT pi.*, nomenclatures.country.name as country_name, pit.name as professional_institution_type_name " +
                                                                                    " FROM professional_institution pi " +
                                                                                    " join nomenclatures.country on (n_country.id = pi.country_id) " +
                                                                                    " join nomenclatures.professional_institution_type as pit on(pi.professional_institution_type_id = pit.id) " +
                                                                                    " ORDER BY pi.id DESC ";*/
    public ProfessionalInstitutionDB(DataSource ds) {
        super(ds);
    }
    public List<ProfessionalInstitutionImpl> getProfessionalInstitutionRecords() throws SQLException {
        return selectRecords(SELECT_PROFESSIONAL_INSTITUTIONS, ProfessionalInstitutionImpl.class);
    }
    public ProfessionalInstitution getProfessionalInstitutionRecord(int id) throws SQLException {
        return selectRecord(new ProfessionalInstitutionImpl(), id);
    }
    
    public List<ProfessionalInstitutionImpl> getProfessionalInstitutions(String partOfBgName, Integer institutionType, Integer educationType) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        String str = " 1 = 1 ";
        if (institutionType != null) {
            str += " AND professional_institution_type_id = ?";
            objects.add(institutionType);
        }
        if (educationType != null) {
            str += " AND professional_institution_type_id IN (SELECT institution_type FROM regprof.education_type_to_institution_type WHERE education_type = ?)";
            objects.add(educationType);
        }
        if (partOfBgName != null) {
            str += " AND bg_name ilike ?";
            objects.add("%" + partOfBgName + "%");
        }
        if (objects.size() > 0) {
            return selectRecords(ProfessionalInstitutionImpl.class , str, objects.toArray());
        } else {
            return selectRecords(ProfessionalInstitutionImpl.class, null);
        }
    }
    
    public List<ProfessionalInstitutionNamesRecord> getProfessionalInstitutionNames(Integer professionalInstitutionId, String partOfName, boolean onlyActive) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        String sql = " 1 = 1 ";
        if (professionalInstitutionId != null) {
            sql += " AND professional_institution_id = ? ";
            objects.add(professionalInstitutionId);
        }
        if (partOfName != null) {
            sql += " AND former_name ilike ? ";
            objects.add("%" + partOfName + "%");
        }
        if (onlyActive) {
            sql += " and active = 1 ";
        }
        return selectRecords(ProfessionalInstitutionNamesRecord.class , sql, objects.toArray());
    }
   
}
//--------------------------------------------------------------------------------------------------
