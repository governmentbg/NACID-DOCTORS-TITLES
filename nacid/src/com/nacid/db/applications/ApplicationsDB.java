package com.nacid.db.applications;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.ApplicationOrderCriteria;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.data.applications.*;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.common.StringValue;
import com.nacid.data.external.applications.ExtApplicationRecord;
import com.nacid.data.nomenclatures.SpecialityRecord;
import com.nacid.db.nomenclatures.NomenclaturesDB;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


public class ApplicationsDB extends DatabaseService {

    
	private static final String TRAINING_COURSE_TABLE = "training_course";
	private static final String SELECT_APPLICATIONS_FOR_LIST_SQL = "select apn.id, app_num, app_date, \n" +
                                                                        "case when apn.applicant_type = 0 THEN apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') \n" +
                                                                        "ELSE company.name END\n" +
                                                                        "apt_name, \n" +
                                                                        "uny.bg_name uni_name, ucy.name as uni_country_name, \n" +
                                                                        "(select array_to_string(array(select spec.name from training_course_specialities tcs join nomenclatures.speciality spec on (spec.id = tcs.speciality_id) where tcs.training_course_id = tce.id order by tcs.id) , ', ')) as speciality_name, \n" +
                                                                        "ass.name as apn_status_name, ass.id apn_status_id, esd.id as ext_signed_doc_id, apn.docflow_status_id, dss.name as docflow_status_name,\n" +
                                                                        "(select array(select entry_num_series_id from application_kind ak where ak.application_id = apn.id)) as entry_num_series,\n" +
                                                                        "(select array(select session_id from comission_agenda where application_id = apn.id)) commission_sessions,\n" +
                                                                        "(select count(*)::integer from application_expert where application_id = apn.id) experts_count,\n" +
                                                                        "coalesce((select 0 from application_expert where application_id = apn.id and process_stat = 0 limit 1), 1) experts_processed_status,\n" +
                                                                        "rpgp.name as recognized_prof_group_name, rqn.name as recognized_qualification_name, ell.name as edu_level_name\n" +
                                                                        "\tfrom application apn\n" +
                                                                        "\tleft join training_course tce on (tce.id = apn.training_course_id)\n" +
                                                                        "\tleft join person apt on (apt.id = apn.applicant_id)\n" +
                                                                        "\tleft join backoffice.company on (company.id = apn.applicant_company_id)\n" +
                                                                        "\tleft join diploma_issuer dir on (dir.diploma_id = tce.id and ord_num = 1)\n" +
                                                                        "\tleft join university uny on (uny.id = dir.university_id)\n" +
                                                                        "\tleft join nomenclatures.country ucy on (ucy.id = uny.country_id)\n" +
                                                                        "\tleft join nomenclatures.app_status ass on (ass.id = apn.app_status_id)\n" +
                                                                        "\tleft join nomenclatures.app_status_docflow dss on (dss.id = apn.docflow_status_id)\n" +
                                                                        "\tleft join eservices.rudi_application eapn on (eapn.application_id = apn.id)\n" +
                                                                        "\tleft join eservices.rudi_signed_docs esd on (esd.ext_app_id = eapn.id)\n" +
                                                                        "\tleft join nomenclatures.prof_group rpgp on rpgp.id = tce.recognized_prof_group_id\n" +
                                                                        "\tleft join nomenclatures.qualification rqn on rqn.id = tce.recognized_qualification_id\n" +
                                                                        "\tleft join nomenclatures.edu_level ell on ell.id = tce.edu_level_id";
	private static final String SELECT_APPLICATIONS_FOR_PUBLIC_REGISTER_SQL = "select apn.id, apn.app_num, apn.app_date, \n" +
                                                                     //" apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') applicant_name , "+
                                                                     " CASE WHEN apn.diff_diploma_names = 1 THEN tce.fname||coalesce(' '||tce.sname,'')||coalesce(' '||tce.lname,'') ELSE apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') END as applicant_name, \n" +
                                                                     " uny.bg_name university_name, ucy.name as university_country, \n" +
	        
                                                                     " (select array_to_string(array(select spec.name from recognized_specs tcs join nomenclatures.speciality spec on (spec.id = tcs.spec_id) where tcs.training_course_id = tce.id order by tcs.id) , ', ')) as recognized_speciality_name, \n" +
                                                                     " null as cert_number, null as invalidated, \n" +
                                                                     " (select cert_number from cert_number_to_attached_doc where application_id = apn.id and invalidated = 0 order by id desc limit 1 ) validated_cert_number,\n" + 
                                                                     " (select array(select cert_number from cert_number_to_attached_doc where application_id = apn.id and invalidated = 1)) invalidated_cert_numbers,\n" +
                                                                     " fsh.status_id as final_status_id\n" +
                                                                     " from application apn \n" +
                                                                     " join training_course tce on (tce.id = apn.training_course_id) \n" +
                                                                     " join diploma_issuer dir on (dir.diploma_id = tce.id and ord_num = 1) \n" + 
                                                                     " join university uny on (uny.id = dir.university_id) \n" +
                                                                     " join person apt on (apt.id = apn.applicant_id) \n" +
                                                                     " left join nomenclatures.country ucy on (ucy.id = uny.country_id) \n" +
                                                                     " join app_status_history fsh on fsh.id = apn.final_status_history_id\n";/* +
                                                                     " left join cert_number_to_attached_doc cnad on (cnad.application_id = apn.id)";*/
	private static final String SELECT_APPLICATIONID_BY_DIPLOMA_EXAMINATIONID = "select app.id as value" +  
                                                                                " from application app " + 
                                                                                " join diploma_examination exam on (exam.training_course_id = app.training_course_id) " + 
                                                                                " where exam.id = ? ";
    private static final String SELECT_APPLICATION_DOCFLOW_STATUS_HISTORY = "select asdh.*, asd.name as application_docflow_status_name from backoffice.app_status_docflow_history   asdh\n" +
                                                                            "join nomenclatures.app_status_docflow asd on asd.id = asdh.app_status_docflow_id ";
    private static final String SELECT_SIMILAR_DIPLOMAS = "select distinct a.id,  diploma_date, ell.name as edu_level_name, tc.edu_level_id, app_num, app_date, owr.civil_id_type, owr.civil_id,\n" +
                                                            "        owr.fname, owr.sname, owr.lname, \n" +
                                                            "        (select array(select name from nomenclatures.speciality s join training_course_specialities s2 on s2.speciality_id = s.id where s2.training_course_id = tc.id)) as speciality_names,\n" +
                                                            "        (select array(select bg_name from university u join diploma_type_issuer i on u.id = i.university_id where i.diploma_type_id = tc.diploma_type_id order by i.id)) as university_names,\n" +
                                                            "        (select array(select name from nomenclatures.country c join university u on u.country_id = c.id join diploma_type_issuer i on u.id = i.university_id where i.diploma_type_id = tc.diploma_type_id order by i.id)) as university_country_names, \n" +
                                                            "        (select array(select country_id from university u join diploma_type_issuer i on u.id = i.university_id where i.diploma_type_id = tc.diploma_type_id order by i.id)) as university_country_ids \n" +
                                                            "            from application a\n" +
                                                            "            join training_course tc on a.training_course_id = tc.id\n" +
                                                            "            join person owr on owr.id = tc.owner_id\n" +
                                                            "            join diploma_type dt on dt.id = tc.diploma_type_id\n" +
                                                            "            join diploma_type_issuer dti on dti.diploma_type_id = dt.id\n" +
                                                            "            join university uni on uni.id = dti.university_id\n" +
                                                            "            join training_course_specialities tcs on tcs.training_course_id = tc.id\n" +
                                                            "            join nomenclatures.speciality spy on spy.id = tcs.speciality_id\n" +
                                                            "            join nomenclatures.edu_level ell on ell.id = tc.edu_level_id \n" +
                                                            "            where 1 = 1";
    private static StandAloneDataSource ds;

    private NomenclaturesDB nomenclaturesDB;
    public ApplicationsDB(DataSource ds, NomenclaturesDB nomenclaturesDB) {
        super(ds);
        this.nomenclaturesDB = nomenclaturesDB;
    }
    public Integer getApplicationType(int applicationId) throws SQLException {
        List<IntegerValue> res = selectRecords("SELECT application_type as value from application where id = ?", IntegerValue.class, applicationId);
        return res.size() == 0 ? null : res.get(0).getValue();
    }
    public List<ApplicationRecognitionPurposeRecord> getApplicationRecognitionPurposeRecords(int applicationId) throws SQLException {
        return selectRecords(ApplicationRecognitionPurposeRecord.class, "application_id = ?", applicationId);
    }
    public List<TrainingCourseGraduationWayRecord> getApplicationGraduationWayRecords(int trainingCourseId) throws SQLException {
        return selectRecords(TrainingCourseGraduationWayRecord.class, "training_course_id = ?", trainingCourseId);
    }
    public List<TrainingCourseTrainingFormRecord> getApplicationTrainingFormRecords(int trainingCourseId) throws SQLException {
        return selectRecords(TrainingCourseTrainingFormRecord.class, "training_course_id = ?", trainingCourseId);
    }
    public void deleteTrainingCourseGraduationWayRecords(int trainingCourseId) throws SQLException{
        deleteRecords(TrainingCourseGraduationWayRecord.class, "training_course_id = ? ", trainingCourseId);
    }
    public void deleteApplicationRecognitionPurposeRecord(int applicationId) throws SQLException{
        deleteRecords(ApplicationRecognitionPurposeRecord.class, "application_id = ? ", applicationId);
    }
    public void deleteTrainingCourseTrainingFormRecords(int trainingCourseId) throws SQLException{
        deleteRecords(TrainingCourseTrainingFormRecord.class, "training_course_id = ? ", trainingCourseId);
    }
    public void deleteDiplomaIssuerRecords(int trainingCourseId) throws SQLException{
        deleteRecords(DiplomaIssuerRecord.class, "diploma_id = ? ", trainingCourseId);
    }
    /*public void deleteUniversityExaminationRecords(int trainingCourseId) throws SQLException{
        deleteRecords(UniversityExaminationRecord.class, "training_course_id = ? ", trainingCourseId);
    }*/
    public List<DiplomaExaminationRecord> getDiplomaExaminationRecords(int trainingCourseId) throws SQLException {
        return selectRecords(DiplomaExaminationRecord.class, "training_course_id = ?", trainingCourseId);
    }
    /**
     * iztriva universityExamination za daden trainingCourseId + universityId
     * @param trainingCourseId
     * @param universityId
     */
    public void deleteUniversityExaminationRecords(int trainingCourseId, int universityId) throws SQLException {
    	deleteRecords(UniversityExaminationRecord.class, " training_course_id = ? and university_validity_id in (select id from university_validity where university_id = ?)", trainingCourseId, universityId);
    }

    @Override
    public void updateRecord(Object o) throws SQLException {
        /*if(o instanceof ApplicationRecord) {
            ApplicationRecord rec = (ApplicationRecord)o;
            ApplicationStatusRecordExtended statusRecord = nomenclaturesDB.getApplicationStatusRecord(NumgeneratorDataProvider.NACID_SERIES_ID, rec.getApplicationStatusId());
            if (statusRecord.getIsLegal() == 1) {
                rec.setFinalStatus(rec.getApplicationStatusId());
            }
        }*/
        super.updateRecord(o);
    }

    public void updateApplicationFinalStatusHistoryRecordId(int applicationId, int historyRecordId) throws SQLException {
        execute("UPDATE application set final_status_history_id = ? WHERE id = ?", historyRecordId, applicationId);
    }


    public void updateApplicationKindFinalStatusHistoryRecordId(int applicationKindId,  int historyRecordId) throws SQLException {
        execute("UPDATE application_kind set final_status_history_id = ? WHERE id = ?", historyRecordId, applicationKindId);
    }


    /*public List<ApplicationRecord> getApplicationsByFinalStatus(int finalStatus) throws SQLException {
        if (finalStatus == ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE) {
        	Calendar cal = new GregorianCalendar(2009, Calendar.APRIL, 9, 0, 0, 0);
        	return selectRecords(ApplicationRecord.class, "(final_status = ? or app_status_id = ?) and app_date >= ?", finalStatus, finalStatus, Utils.getSqlDate(cal.getTime()));	
        }
    	return selectRecords(ApplicationRecord.class, "final_status = ? or app_status_id = ?", finalStatus, finalStatus);
    }*/
    public List<ApplicationRecord> getApplicationRecordsFromAppStatusHistoryContainingStatus(int applicationStatusId) throws SQLException {
    	String sql = " select distinct application_id as value from app_status_history  where status_id = ? ";
    	List<IntegerValue> records = selectRecords(sql, IntegerValue.class, applicationStatusId);
    	Set<Integer> result = new HashSet<Integer>();
    	for (IntegerValue r:records) {
    		result.add(r.getValue());
    	}
    	return result.size() == 0 ? new ArrayList<ApplicationRecord>() : getApplicationRecords(new ArrayList<Integer>(result), null);

    }
    
    public List<ApplicationRecord> getApplicationRecords(int applicationType, boolean onlyInProcedure) throws SQLException {
    	String str;
		List<Object> objects = new ArrayList<Object>();

        if (onlyInProcedure) {
            str = " docflow_status_id = ?";
            objects.add(ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE);
        } else {
            str = " 1 = 1 ";
        }

        str += " AND application_type = ?";
        objects.add(applicationType);
		
    	return selectRecords(ApplicationRecord.class, str, objects.size() == 0 ? null : objects.toArray());

    }
    /*public List<ApplicationRecordForList> getApplicationRecordsForList(List<Integer> statuses, boolean onlySelected) throws SQLException {
        String str;
        List<Object> objects = new ArrayList<Object>();
        if (statuses != null && statuses.size() > 0) {
            str = " app_status_id " + (onlySelected ? "" : " not ") +" in (" + SQLUtils.columnsToParameterList(StringUtils.join(statuses, ",")) + ") ";
            objects.addAll(statuses);

            
        } else {
            str = " 1 = 1 ";
        }
        
        return selectRecords(SELECT_APPLICATIONS_FOR_LIST_SQL + " WHERE " + str, ApplicationRecordForList.class, objects.size() == 0 ? null : objects.toArray());
        
    }*/
    public List<ApplicationRecordForList> getApplicationRecordsForList(int applicationType, Integer applicationStatus, Integer commissionId, boolean onlyInProcedure) throws SQLException {

        List<Object> objects = new ArrayList<>();

        List<String> str = new ArrayList<>();
        str.add(" 1 = 1 ");
        if (onlyInProcedure) {
            str.add(" docflow_status_id = ?");
            objects.add(ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE);
        }
        str.add(" apn.application_type = ? ");
        objects.add(applicationType);

        if (applicationStatus != null) {
            str.add(" apn.app_status_id = ?");
            objects.add(applicationStatus);
        }
        if (commissionId != null) {
            str.add( " apn.id in (select application_id from comission_agenda where session_id = ?)");
            objects.add(commissionId);
        }

        return selectRecords(SELECT_APPLICATIONS_FOR_LIST_SQL + " WHERE " + str.stream().collect(Collectors.joining(" AND ")), ApplicationRecordForList.class, objects.size() == 0 ? null : objects.toArray());

    }
    public List<ApplicationRecordForPublicRegister> getApplicationRecordForPublicRegisterByFinalStatus(int finalStatus) throws SQLException {
        String sql = SELECT_APPLICATIONS_FOR_PUBLIC_REGISTER_SQL + " WHERE ";
        List<Object> params = new ArrayList<>();

        List<String> where = new ArrayList<>();
        where.add("docflow_status_id != ?");
        params.add(ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE);

        where.add("fsh.status_id = ?");
        params.add(finalStatus);

        if (finalStatus == ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE) {
            Calendar cal = new GregorianCalendar(2009, Calendar.APRIL, 9, 0, 0, 0);
            where.add("app_date >= ?");
            params.add( Utils.getSqlDate(cal.getTime()));
        }

        sql += "(" + StringUtils.join(where, ") AND (") + ")";
        return selectRecords(sql, ApplicationRecordForPublicRegister.class, params.toArray());
    }
    public List<ApplicationRecord> getApplicationRecords(List<Integer> ids, ApplicationOrderCriteria orderCriteria) throws SQLException {
    	String str;
		List<Object> objects = new ArrayList<Object>();
        if (ids != null && ids.size() > 0) {
            str = "id in (" + SQLUtils.columnsToParameterList(StringUtils.join(ids, ",")) + ") ";
            for (Integer i:ids) {
                objects.add(i);
            }
            
        } else {
            str = " 1 = 1 ";
        }
		if (orderCriteria != null) {
    		str += orderCriteria.getOrderSqlString();
    	}
    	return selectRecords(ApplicationRecord.class, str, objects.size() == 0 ? null : objects.toArray());
    }

    public Integer getApplicationsByApplicantCount(int applicantId, int excludingApplicationId) throws SQLException {
        return selectRecords("select count(*)::integer as value from application where applicant_id = ? and id != ?", IntegerValue.class, applicantId, excludingApplicationId).get(0).getValue();
    }
    public List<ApplicationRecord> getApplicationRecordsByApplicantId(int applicantId) throws SQLException {
    	return selectRecords(ApplicationRecord.class, " applicant_id = ? ", applicantId);
    }
    public List<PersonRecord> getPersonRecords(int civilIdType, String civilId, boolean isPartOfCivilId) throws SQLException{
        List<Object> objects = new ArrayList<Object>();
        String str = " 1 = 1 ";
        if (civilIdType != 0) {
            str += " AND civil_id_type = ?";
            objects.add(civilIdType);
        }
        if (civilId != null) {
            if (isPartOfCivilId) {
                str += " AND civil_id like ?";
                objects.add(civilId + "%");
            } else {
                str += " AND civil_id = ?";
                objects.add(civilId);
            }

        }
        if (objects.size() > 0) {
            return selectRecords(PersonRecord.class , str, objects.toArray());
        } else {
            return selectRecords(PersonRecord.class, null);
        }
    }
    public PersonDocumentRecord savePersonDocumentRecord(PersonDocumentRecord rec) throws SQLException {
        execute("UPDATE person_document SET active=0 where person_id = ?", rec.getPersonId());
        if (rec.getId() == null || rec.getId() == 0) {
            rec = insertRecord(rec);
        } else {
            updateRecord(rec);
        }
        return rec;
    }
    public List<TrainingCourseRecord> getTrainingCourseRecords(List<Integer> trainingCourseIds) throws SQLException {
    	if ((trainingCourseIds != null && trainingCourseIds.size() > 0)) {
    		String sql = " id in (" + SQLUtils.columnsToParameterList(StringUtils.join(trainingCourseIds, ",")) + ") ";
        	return selectRecords(TrainingCourseRecord.class, sql, trainingCourseIds.toArray());
    	} else {
    		return new ArrayList<TrainingCourseRecord>();
    	}
    }
    public List<PersonRecord> getPersonRecords(List<Integer> personIds) throws SQLException {
    	if ((personIds != null && personIds.size() > 0)) {
    		String sql = " id in (" + SQLUtils.columnsToParameterList(StringUtils.join(personIds, ",")) + ") ";
        	return selectRecords(PersonRecord.class, sql, personIds.toArray());
    	} else {
    		return new ArrayList<PersonRecord>();
    	}
    }
    public List<ExtApplicationRecord> getExtApplicationRecords(List<Integer> applicationIds) throws SQLException {
    	if ((applicationIds != null && applicationIds.size() > 0)) {
    		String sql = " application_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(applicationIds, ",")) + ") ";
        	return selectRecords(ExtApplicationRecord.class, sql, applicationIds.toArray());
    	} else {
    		return new ArrayList<ExtApplicationRecord>();
    	}
    }
    public List<UniversityExaminationRecord> getUniversityExaminationRecords(int trainingCourseId) throws SQLException{
        return selectRecords(UniversityExaminationRecord.class , "training_course_id = ?", trainingCourseId);
    }
    
    public List<ApplicationExpertRecord> getApplicationExperts(int applicationId) throws SQLException {
        if (applicationId == 0) {
        	return selectRecords(ApplicationExpertRecord.class, null);
        }
    	return selectRecords(ApplicationExpertRecord.class, "application_id = ? order by id", applicationId);
    }

    public PersonDocumentRecord getPersonDocument(Integer personId) throws SQLException {
        PersonDocumentRecord rec = selectRecord(PersonDocumentRecord.class, "person_id = ? and active = 1 order by id desc limit 1", personId);
        return  rec;
    }

    public List<ApplicationExpertSpecialityRecord> getApplicationExpertSpecialities(int applicationId, int expertId) throws SQLException {
        return selectRecords(ApplicationExpertSpecialityRecord.class, "application_id = ? and expert_id = ? order by id", applicationId, expertId);

    }
    
    public ApplicationExpertRecord getApplicationExpert(int applicationId, int expertId) throws SQLException {
        
        List<ApplicationExpertRecord> rec = selectRecords(ApplicationExpertRecord.class, 
                "application_id = ? and expert_id = ?", applicationId, expertId);
        
        if(rec != null && !rec.isEmpty())
            return rec.get(0);
        return null;
    }
    
    public void deleteApplicationExperts(int applicationId) throws SQLException {
        deleteRecords(ApplicationExpertSpecialityRecord.class, "application_id=?", applicationId);
        deleteRecords(ApplicationExpertRecord.class, "application_id=?", applicationId);
    }


    public void updateTrainingForm(int id, String fname, String sname, String lname, int ownerId) throws SQLException {
        Connection con = getConnection();
        String sql = "UPDATE " + TRAINING_COURSE_TABLE + " SET fname = ?, sname = ?, lname = ?, owner_id = ? WHERE id = ?";
        try {
            PreparedStatement p = con.prepareStatement(sql);
            try {
                p.setString(1, fname);
                p.setString(2, sname);
                p.setString(3, lname);
                p.setInt(4, ownerId);
                p.setInt(5, id);

                p.executeUpdate();
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }
    public int insertTrainingForm(String fname, String sname, String lname, int ownerId) throws SQLException {
        Connection con = getConnection();
        String sql = "INSERT INTO " + TRAINING_COURSE_TABLE + " (fname, sname, lname, owner_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement p = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            try {
                p.setString(1, fname);
                p.setString(2, sname);
                p.setString(3, lname);
                p.setInt(4, ownerId);

                if (p.executeUpdate() > 0) {
                    ResultSet rs = p.getGeneratedKeys();
                    try {
                        if (rs.next()) {
                            return rs.getInt("id");
                        }
                    } finally {
                        rs.close();
                    }
                }
                return 0;
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }
    public List<TrainingCourseRecord> getTrainingCourseRecords(int diplomaTypeId) throws SQLException {
    	return selectRecords(TrainingCourseRecord.class, " diploma_type_id = ? ", diplomaTypeId);
    }
    
    public List<ApplicationRecord> getApplicationsForExpert(int expertId) throws SQLException {
        
        String className = ApplicationRecord.class.getName();
        String appExpClassName = ApplicationExpertRecord.class.getName();
        String appTableName = dataConfigFactory.getTableName(className);
        String appExpTableName = dataConfigFactory.getTableName(appExpClassName);
        
        String columns = "";
        String coma = "";
        for(String c : dataConfigFactory.getColumnNames(className)) {
            columns +=  coma + appTableName + "." + c;
            coma = ",";
        }
        
        String sql = " SELECT " + columns
            + " FROM " + appTableName + ", " + appExpTableName 
            + " where " + appTableName + ".id=" + appExpTableName + ".application_id"
            + " and " + appExpTableName + ".expert_id=? "
            + " ORDER BY app_date";
        
        return selectRecords(sql, ApplicationRecord.class, expertId);
    }

    
    public boolean hasExpertAccessToApplication(int applicationId, int expertId) throws SQLException {
        List<ApplicationExpertRecord> recs = selectRecords(ApplicationExpertRecord.class, 
                "application_id = ? and expert_id = ?", applicationId, expertId);
        return recs != null && !recs.isEmpty();
    }
    public Integer getApplicationIdForExtReport(String docflowId, java.sql.Date docFlowDate, String personalId) throws SQLException {
        Connection con = getConnection();
        String sql = "select application.id from application, person "
            + "where application.app_num=? and application.app_date=? and person.id = application.applicant_id and person.civil_id=?";
        try {
            PreparedStatement p = con.prepareStatement(sql);
            try {
                p.setString(1, docflowId);
                p.setDate(2, docFlowDate);
                p.setString(3, personalId);

                ResultSet rs = p.executeQuery();
                try {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                } finally {
                    rs.close();
                }

                return null;
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }
    
    public void deleteRecognizedSpecialities(int trainingCourseId) throws SQLException {
        Connection con = getConnection();
        String sql = "delete from recognized_specs where training_course_id = ?";
        try {
            PreparedStatement p = con.prepareStatement(sql);
            try {
                p.setInt(1, trainingCourseId);

                p.executeUpdate();
                
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }
    
    public void saveRecognizedSpecialities(int trainingCourseId, List<Integer> recognizedSpecialities) throws SQLException {
    	if(recognizedSpecialities == null || recognizedSpecialities.isEmpty()) {
            return;
        }
        Connection con = getConnection();
        String sql = "insert into recognized_specs (training_course_id, spec_id) values (?, ?)";
        try {
            PreparedStatement p = con.prepareStatement(sql);
            try {
                for(int r : recognizedSpecialities) {
                    p.clearParameters();
                    p.setInt(1, trainingCourseId);
                    p.setInt(2, r);
                    p.executeUpdate();
                }
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }

    public List<SpecialityRecord> loadRecognizedSpecialities(int trainingCourseId) throws SQLException {
        return selectRecords( "SELECT spy.* " + 
                        "FROM nomenclatures.speciality  spy "+
                        "join recognized_specs rs on (spy.id = rs.spec_id) "+
                        "where training_course_id = ?  "+
                        "order by rs.id", SpecialityRecord.class, trainingCourseId);
    }
    /*public List<RecognizedSpecsRecord> getRecognizedSpecialities (List<Integer> applicationIds) throws SQLException {
        String sql = " training_course_id in ( select training_course_id from application where id in (" + SQLUtils.columnsToParameterList(StringUtils.join(applicationIds, ",")) + ") )";
        return selectRecords(RecognizedSpecsRecord.class, sql, applicationIds.toArray());
    }*/
    public ApplicationRecord getApplicationByEMail(String email) throws SQLException {
        List<ApplicationRecord> recs = selectRecords(ApplicationRecord.class, 
                "email=? order by id desc limit 1" , email);
        return (recs == null || recs.isEmpty()) ? null : recs.get(0);
    }
    
    public List<TrainingCourseTrainingLocationRecord> getTrainingCourseTrainingLocationRecords(int trainingCourseId) throws SQLException {
    	return selectRecords(TrainingCourseTrainingLocationRecord.class, " training_course_id = ? ORDER BY ID", trainingCourseId);
    }
    
    public List<AppStatusHistoryRecord> getAppStatusHistoryRecords(int applicationId) throws SQLException {
    	return selectRecords(AppStatusHistoryRecord.class, "application_id=? order by date_assigned desc, id desc", applicationId);
    }
    public AppStatusHistoryRecord getAppStatusHistoryRecord(int id) throws SQLException {
        return selectRecord(AppStatusHistoryRecord.class, "id = ?", id);
    }


    public List<AppDocflowStatusHistoryRecordExtended> getAppDocflowStatusHistoryRecords(int applicationId) throws SQLException {
        return selectRecords(SELECT_APPLICATION_DOCFLOW_STATUS_HISTORY + " WHERE application_id=? order by date_assigned desc, id desc", AppDocflowStatusHistoryRecordExtended.class, applicationId);
    }

    /**
     * @param applicationId
     * @param invalidated - null - ne u4astva vyv filtriraneto. 1 - samo nevalidnite udostovereniq. 0 - samo validnite
     * @return zapisite podredeni v nizhodqsh red po ID
     * @throws SQLException
     */
    public List<CertificateNumberToAttachedDocRecord> getCertificateNumberToAttachedDocRecords(int applicationId, Integer invalidated) throws SQLException {
    	String sql = " application_id = ? ";
    	List<Object> objects = new ArrayList<Object>();
    	objects.add(applicationId);
    	if (invalidated != null) {
    		sql += " AND invalidated = ? ";
    		objects.add(invalidated);
    	}
    	sql += " ORDER BY id desc";
    	return selectRecords(CertificateNumberToAttachedDocRecord.class, sql , objects.toArray());
    }
    public List<CertificateNumberToAttachedDocRecord> getCertificateNumberToAttachedDocRecords(List<Integer> applicationIds) throws SQLException {
        String sql = " application_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(applicationIds, ",")) + ") ";
        return selectRecords(CertificateNumberToAttachedDocRecord.class, sql , applicationIds.toArray());
    }
    /**
     * slaga status invalidated na vsi4ki certificateNumbers za dadenoto zaqvlenie
     * @param applicationId
     * @throws SQLException
     */
    public void invalidateOldCertificateNumbers(int applicationId, int invalidatedStatusId) throws SQLException {
    	execute("update cert_number_to_attached_doc set invalidated = ? where application_id = ? and invalidated = 0", invalidatedStatusId, applicationId);
    }
    /*public List<CertificateNumberToAttachedDocRecord> getCertificateNumberToAttachedDocRecordsByCertNumber(String certNumber) throws SQLException {
    	return selectRecords(CertificateNumberToAttachedDocRecord.class, " cert_number = ? ", certNumber);
    }*/
    public Integer getApplicationIdForExtReport(String certNumber, String personalId) throws SQLException {
        Connection con = getConnection();
        String sql = "select application.id from application " +
        		" left join person on (person.id = application.applicant_id) " +
        		" left join cert_number_to_attached_doc on (cert_number_to_attached_doc.application_id = application.id) " + 
        		" where cert_number_to_attached_doc.cert_number = ? and person.civil_id= ? ";
        try {
            PreparedStatement p = con.prepareStatement(sql);
            try {
                p.setString(1, certNumber);
                p.setString(2, personalId);
                //System.out.println(p);
                ResultSet rs = p.executeQuery();
                try {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                    return null;
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }

    /**
     * 
     * vry6ta vsi4ki AppStatusHistoryRecords za vsi4ki zaqvleniq, koito imat posleden appStatusId i legalReasonIds  
     * primer - zaqvleniq s id = 3,4 imat posleden status priznato, tozi method vry6ta absoliutno vsi4ki statuses na tezi 2 zaqvleniq!
     * 
     * 
     */
    /*public List<AppStatusHistoryRecord> getRecordsWithLastAppStatusFromHistory(int appStatusId, List<Integer> legalReasonIds) throws SQLException {
    	String sql = " application_id in ( select application_id from app_status_history t1 " + 
    	" where id in (select max(id) from app_status_history t2 where t1.application_id = t2.application_id) ";
    	List<Object> objects = new ArrayList<Object>();
    	objects.add(appStatusId);
    	sql += " AND status_id = ? ";
    	if (legalReasonIds != null && legalReasonIds.size() > 0) {
    		sql += " and  stat_legal_reason_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(legalReasonIds, ",")) + ") " ;
    		objects.addAll(legalReasonIds);
    	}
    	//System.out.println(sql);
    	sql += ") ORDER BY application_id, date_assigned desc, id desc";
    	return selectRecords(AppStatusHistoryRecord.class, sql, objects.toArray());
    }*/
    
    /**
     * vry6ta vsi4ki spisyk ot {@link AppStatusHistoryRecord} zapisi, koito imat/nqmat daden status+legalReasons 
     * @param appStatusId
     * @param legalReasonIds
     * @param contains - true - zaqvleniqta imat appStatusId. false - zaqvleniqta nqmat appStatusId
     * @return
     * @throws SQLException
     */
    public List<Integer> getAppStatusHistoryRecords(Date dateFrom, Date dateTo, Integer startSessionId, Integer endSessionId, int appStatusId, List<Integer> legalReasonIds, boolean contains) throws SQLException {
    	
    	String s = "select distinct application_id as value from app_status_history where ";
    	List<Object> objects = new ArrayList<>();
    	if (!contains) {
    		s += " application_id not in (select distinct application_id from app_status_history where ";
    	}
    	s += "status_id = ? ";
    	objects.add(appStatusId);

    	if (legalReasonIds != null && legalReasonIds.size() > 0) {
    		s += " and  stat_legal_reason_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(legalReasonIds, ",")) + ") " ;
    		objects.addAll(legalReasonIds);
    	}
    	if (!contains) {
    		s += ")";
    	}
    	
    	if (startSessionId != null || endSessionId != null || dateFrom != null || dateTo != null) {
    		s += " and session_id in (select id from comission_calendar where 1 = 1 ";
    		if (startSessionId != null) {
    			s += " AND session_num >= ? ";
    			objects.add(startSessionId);
    		}
    		if (endSessionId != null) {
    			s += " AND session_num <= ? ";
    			objects.add(endSessionId);
    		}
    		
    		if (dateFrom != null) {
    			s += " AND session_time >= ? ";
    			objects.add(dateFrom);
    		}
    		if (dateTo != null) {
    			s += " AND session_time <= ? ";
                objects.add(dateTo);
    		}
    		
    		s += " ) ";
    	}
    	return selectRecords(s, IntegerValue.class, objects.size() == 0 ? null : objects.toArray()).stream().map(IntegerValue::getValue).collect(Collectors.toList());
    }
    
    public List<TrainingCourseSpecialityRecord> getSpecialitiesByTrainingCourse(int trainingCourseId) throws SQLException {
        return selectRecords(TrainingCourseSpecialityRecord.class, "training_course_id = ?", trainingCourseId);
    }
    public void deleteSpecliaitiesByTrainingCourse(int trainingCourseId) throws SQLException {
        deleteRecords(TrainingCourseSpecialityRecord.class, "training_course_id = ?", trainingCourseId);
    }
    public void updateRecognizedDetails(int trainingCourseId, Integer recognizedEduLevelId, Integer recognizedQualificationId, Integer recognizedProfGroupId, List<Integer> recognizedSpecialities) throws SQLException {
        deleteRecognizedSpecialities(trainingCourseId);
        saveRecognizedSpecialities(trainingCourseId, recognizedSpecialities);
        execute("UPDATE training_course set recognized_edu_level_id = ?, recognized_qualification_id = ?, recognized_prof_group_id = ? WHERE id = ?", recognizedEduLevelId, recognizedQualificationId, recognizedProfGroupId, trainingCourseId);
    }




    public int getApplicationIdByDiplomaExaminationId(int diplomaExaminationId) throws SQLException {
        return selectRecords(SELECT_APPLICATIONID_BY_DIPLOMA_EXAMINATIONID, IntegerValue.class, diplomaExaminationId).get(0).getValue();
    }

    public void linkApplications(int oldApplicationId, int newApplicationId) throws SQLException {
        execute("INSERT INTO application_links (old_application_id, new_application_id) VALUES (?, ?)", oldApplicationId, newApplicationId);
    }

    public static void main(String[] args) throws SQLException{
        DataSource ds = new StandAloneDataSource();
        ApplicationsDB db = new ApplicationsDB(ds, new NomenclaturesDB(ds));
        /*List<AppDocflowStatusHistoryRecordExtended> apnRecords = db.getAppDocflowStatusHistoryRecords(1200);
        System.out.println(StringUtils.join(apnRecords, "\n"));*/
        System.out.println(db.getApplicationRecordsForList(1, null, null, false));
        //System.out.println(db.getApplicationRecordsForList(ApplicationStatus.APPLICATIONS_ONLY_ACTIVE_STATUSES, true));
        //db.getCertificateNumberToAttachedDocRecords(Arrays.asList(1,2));
        
    }

    public List<ApplicationKindRecord> getApplicationKindsPerApplication(int applicationId) throws SQLException {
        List<ApplicationKindRecord> result = selectRecords(ApplicationKindRecord.class, " application_id = ? order by id", applicationId);
        return result;
    }
    public void deleteApplicationKindRecords(int applicationId) throws SQLException {
        execute("DELETE FROM application_kind where application_id = ?", applicationId);
    }
    public ApplicationKindRecord addApplicationKindRecord(int applicationId, int entryNumSeriesId, BigDecimal price, String entryNum, Integer finalStatusHistoryId) throws SQLException {
        ApplicationKindRecord rec = new ApplicationKindRecord(0, applicationId, entryNumSeriesId, price, entryNum, finalStatusHistoryId);
        rec = insertRecord(rec);
        return rec;
    }

    public List<SimilarDiplomaRecord> getSimilarDiplomas(String civilId, String firstName, List<Integer> universityCountryIds, List<String> universityCountryNames, Integer eduLevelId, Integer diplomaYear, Integer skipApplicationId) throws SQLException {
        String sql = SELECT_SIMILAR_DIPLOMAS ;
        List<String> where = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        //diplomaYear/edulevelId/universityCountries sa zadyljitelni poleta!!!!
        if (diplomaYear == null || eduLevelId == null || ((universityCountryIds == null || universityCountryIds.size() == 0) && (universityCountryNames == null || universityCountryNames.size() == 0))
                || (civilId == null && firstName == null)
                ) {
            return new ArrayList<>();
        }

        List<String> personWhere = new ArrayList<>();
        if (civilId != null) {
            personWhere.add(" (owr.civil_id = ?) ");
            objects.add(civilId);
        }
        if (firstName != null) {
            personWhere.add("owr.fname ilike ?");
            objects.add(firstName);
        }

        where.add(personWhere.stream().collect(Collectors.joining(" OR ", "(", ")")));
        if (eduLevelId != null) {
            where.add(" tc.edu_level_id = ?");
            objects.add(eduLevelId);
        }
        if (diplomaYear != null) {
            where.add(" date_part('year', diploma_date) = ?");
            objects.add(diplomaYear);
        }
        if (universityCountryIds != null && universityCountryIds.size() > 0) {
            where.add("uni.country_id in (" + SQLUtils.parametersCountToParameterList(universityCountryIds.size()) + ")");
            objects.addAll(universityCountryIds);
        }
        if (universityCountryNames != null && universityCountryNames.size() > 0) {
            where.add("uni.bg_name in (" + SQLUtils.parametersCountToParameterList(universityCountryNames.size()) + ")");
            objects.addAll(universityCountryNames);
        }

        if (skipApplicationId != null) {
            where.add(" a.id != ?");
            objects.add(skipApplicationId);
        }

        sql += where.size() > 1 ? " AND " + where.stream().collect(Collectors.joining(" AND ")) : "";
        return selectRecords(sql, SimilarDiplomaRecord.class, objects.toArray());
    }
    public void updateTrainingCourseDiplomaType(int trainingCourseId, int diplomaTypeId) throws SQLException {
        execute("UPDATE training_course set diploma_type_id = ? where id = ?", diplomaTypeId, trainingCourseId);
    }
    public DocumentRecipientRecord getDocumentRecipient(int applicationId) throws SQLException {
        return selectRecord(DocumentRecipientRecord.class, "application_id = ?", applicationId);
    }
    public void saveDocumentRecipient(int applicationId, String name, int countryId, String city, String district, String postCode, String address, String mobilePhone) throws SQLException {
        DocumentRecipientRecord rec = getDocumentRecipient(applicationId);
        if (rec == null) {
            rec = new DocumentRecipientRecord();
        }
        rec.setApplicationId(applicationId);
        rec.setName(name);

        rec.setCountryId(countryId);
        rec.setCity(city);

        rec.setDistrict(district);
        rec.setPostCode(postCode);
        rec.setAddress(address);
        rec.setMobilePhone(mobilePhone);

        if (rec.getId() == 0) {
            insertRecord(rec);
        } else {
            updateRecord(rec);
        }
    }
    public boolean isApplicationTransferredInRas(int applicationId) throws SQLException {
        return !StringUtils.isEmpty(selectRecords("SELECT ras_key as value from application where id = ?", StringValue.class, applicationId).get(0).getValue());
    }
    public void updateRasKey(int applicationId, String key) throws SQLException {
        execute("UPDATE application SET ras_key = ? where id = ?", key, applicationId);
    }
    public void deleteDocumentRecipient(int applicationId) throws SQLException {
        execute("delete from document_recipient where application_id = ?", applicationId);
    }
}
