package com.nacid.db.regprof.external;

import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.common.StringValue;
import com.nacid.data.external.applications.ExtApplicationCommentExtendedRecord;
import com.nacid.data.regprof.applications.RegprofDocumentRecipientRecord;
import com.nacid.data.regprof.external.*;
import com.nacid.data.regprof.external.applications.ExtRegprofApplicationCommentExtendedRecord;
import com.nacid.data.regprof.external.applications.ExtRegprofApplicationForListRecord;
import com.nacid.data.regprof.external.applications.ExtRegprofApplicationLiabilityRecord;
import com.nacid.data.regprof.external.applications.ExtRegprofDocumentRecipientRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AutoPopulatingList;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExtRegprofApplicationsDB extends DatabaseService {
	public static final String SELECT_EXT_REGPROF_APPS_BY_PERSON = "select eapn.id, eapn.time_of_creation as date, \n" +  
                                                                        "CASE WHEN eapn.names_dont_match = 1 THEN etce.document_fname||coalesce(' '||etce.document_sname,'')||coalesce(' '||etce.document_lname,'') ELSE eapt.fname||coalesce(' '||eapt.sname,'')||coalesce(' '||eapt.lname,'') END as applicant_name,\n" +  
                                                                        "apn.app_num||'/'||to_char(app_date, 'DD.MM.YYYY') as docflow_number, eapn.status as status_id, apn.status as internal_status_id, ersd.id IS NOT NULL as esigned, fsn.name as final_status_name,\n" +
                                                                        "coalesce((select 1 from eservices.regprof_application_comments where application_id = eapn.id and system_message = 0 limit 1),0) as communicated\n" +
                                                                        "from eservices.regprof_application  eapn\n" +
                                                                        "join eservices.regprof_training_course etce on (etce.id = eapn.training_course_id)\n" +
                                                                        "join eservices.person eapt on (eapt.id = eapn.applicant_id)\n" +
                                                                        " LEFT JOIN eservices.regprof_signed_docs ersd ON ersd.ext_app_id = eapn.id \n"+
                                                                        "left join regprof.regprof_application apn on (apn.id = eapn.regprof_application_id)\n" +
                                                                        "left join regprof.app_status_history fss on (fss.id = apn.final_status_history_id)\n" +
                                                                        "left join nomenclatures.app_status fsn on (fsn.id = fss.status_id) ";

    public static final String SELECT_APPLICATION_COMMENT = "select ac.*, processed as int_email_processed, in_out as int_email_incoming, sent_to as email_recipient, subject as email_subject\n" +
            "from eservices.regprof_application_comments  ac\n" +
            "left join email e on e.id = ac.email_id\n";
    public ExtRegprofApplicationsDB(DataSource ds) {
        super(ds);
    }
    public String getExternalSystemId(int applicationId) throws SQLException {
        return selectRecords("select external_system_id as value from eservices.regprof_application where id = ?", StringValue.class, applicationId).get(0).getValue();
    }
    public ExtRegprofApplicationDetailsImpl getExtRegprofApplicationByInternalApplicationId(int internalApplicationId) throws SQLException {
        List<ExtRegprofApplicationDetailsImpl> res = selectRecords(ExtRegprofApplicationDetailsImpl.class, "regprof_application_id = ?", internalApplicationId);
        return res.size() == 0 ? null : res.get(0);
    }
    public List<ExtRegprofApplicationForListRecord> getExtRegprofApplicationsByExtPerson(int extPersonId) throws SQLException {
        return selectRecords(SELECT_EXT_REGPROF_APPS_BY_PERSON + "  WHERE eapn.representative_id = ? and eapn.status != ? ORDER BY eapn.time_of_creation DESC", ExtRegprofApplicationForListRecord.class, extPersonId, ExtApplication.STATUS_FINISHED);
    }
    public List<ExtRegprofApplicationForListRecord> getExtRegprofApplicationsByStatuses(List<Integer> statuses) throws SQLException {
        return selectRecords(SELECT_EXT_REGPROF_APPS_BY_PERSON + "  WHERE eapn.status in ( " + StringUtils.join(statuses, ", ") + ") ORDER BY eapn.time_of_creation DESC", ExtRegprofApplicationForListRecord.class);
    }
    public void updateBaseApplicationDetails(ExtRegprofApplicationDetailsImpl appDetails) throws SQLException {
        updateRecord(appDetails, "names_dont_match", "applicant_country_id", "applicant_phone", "applicant_addr_details", "personal_data_usage", "applicant_documents_id", "applicant_city", "document_receive_method_id", "applicant_id", "applicant_personal_id_document_type");
    }
    public void updateBaseTrainingCourseDetails(ExtRegprofTrainingCourseRecord record) throws SQLException {
        updateRecord(record, "document_fname", "document_sname", "document_lname", "document_civil_id", "document_civil_id_type");
    }
    public ExtRegprofTrainingCourseRecord getExtRegprofTrainingCourseRecord(int applicationId) throws SQLException {
        return selectRecords(ExtRegprofTrainingCourseRecord.class, "id = (select training_course_id from eservices.regprof_application where id = ?)", applicationId).get(0);
    }
    public ExtRegprofProfessionExperienceRecord getExtRegprofExperienceRecordByTrainingCourse(int trainingCourseId) throws SQLException {
        List<ExtRegprofProfessionExperienceRecord> records = selectRecords(ExtRegprofProfessionExperienceRecord.class, "training_course_id = ?", trainingCourseId);
        return records.size() == 0 ? null : records.get(0);
    }
    public void deleteSpecialitiesByTrainingCourse(int extTrainingCourseId) throws SQLException {
        deleteRecords(ExtRegprofTrainingCourseSpecialitiesRecord.class, "training_course_id = ?", extTrainingCourseId);
    }

    public void updateApplicationCountryAndApostille(Integer trainingCourseId, Integer countryId, int apostilleApplication) throws SQLException {
        execute("UPDATE eservices.regprof_application set application_country_id = ?, apostille_application = ? WHERE training_course_id = ?", countryId, apostilleApplication, trainingCourseId);
    }

    public List<ExtRegprofProfessionExperienceDocumentRecord> getRegprofProfessionExperienceDocumentRecordsByExperienceId(Integer professionExperienceId) throws SQLException {
        List<ExtRegprofProfessionExperienceDocumentRecord> lst = new AutoPopulatingList<ExtRegprofProfessionExperienceDocumentRecord>(selectRecords(ExtRegprofProfessionExperienceDocumentRecord.class, " prof_experience_id = ? order by id", professionExperienceId), ExtRegprofProfessionExperienceDocumentRecord.class);
        for (ExtRegprofProfessionExperienceDocumentRecord rec:lst) {
            rec.setDates(getRegprofProfessionExperienceDates(rec.getId()));
        }
        return lst;
        
    }
    public void deteleProfessionalExperienceRecord(int trainingCourseId) throws SQLException {
        execute("DELETE FROM eservices.regprof_profession_experience_dates where prof_experience_document_id in (select id from eservices.regprof_profession_experience_documents  where prof_experience_id in (select id from eservices.regprof_profession_experience  where training_course_id = ?))", trainingCourseId);
        execute("DELETE from eservices.regprof_profession_experience_documents where prof_experience_id in (select id from eservices.regprof_profession_experience where training_course_id = ?)", trainingCourseId);
        execute("DELETE FROM eservices.regprof_profession_experience where training_course_id = ?", trainingCourseId);
    }
    public void deleteAllProfessionExperienceDocumentRecords(Integer professionExperienceId)  throws SQLException {
        execute("DELETE FROM eservices.regprof_profession_experience_dates WHERE prof_experience_document_id in (SELECT id from eservices.regprof_profession_experience_documents WHERE prof_experience_id = ?)", professionExperienceId);
        execute("DELETE FROM eservices.regprof_profession_experience_documents WHERE prof_experience_id = ?", professionExperienceId);
    }
    public void deleteProfessionExperienceDocumentRecords(Integer professionExperienceId, List<Integer> docIdsNotToDelete) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        String sql = " prof_experience_id = ?";
        objects.add(professionExperienceId);
        String idsNotToDelete = StringUtils.join(docIdsNotToDelete, ", ");
        if (docIdsNotToDelete != null && docIdsNotToDelete.size() > 0) {
            sql += " AND id not in ( " + idsNotToDelete + ") "; 
        }
        execute("DELETE FROM eservices.regprof_profession_experience_dates  WHERE prof_experience_document_id in (SELECT id from eservices.regprof_profession_experience_documents WHERE (" + sql + "))", objects.toArray());
        deleteRecords(ExtRegprofProfessionExperienceDocumentRecord.class, sql, objects.toArray());
    }
    public void deleteProfessionExperienceDatesRecords(Integer professionExperienceDocumentId, List<Integer> dateIdsNotToDelete) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        String sql = "prof_experience_document_id = ?";
        objects.add(professionExperienceDocumentId);
        if (dateIdsNotToDelete != null && dateIdsNotToDelete.size() > 0) {
            sql += " AND id not in ( " + StringUtils.join(dateIdsNotToDelete, ", ") + ") "; 
        }
        deleteRecords(ExtRegprofProfessionExperienceDatesRecord.class, sql, objects.toArray());
    }
    public List<ExtRegprofTrainingCourseSpecialitiesRecord> getSpeicialitiesByTrainingCourse(Integer trainingCourseId) throws SQLException {
        List<ExtRegprofTrainingCourseSpecialitiesRecord> result = selectRecords(ExtRegprofTrainingCourseSpecialitiesRecord.class, "training_course_id = ?", trainingCourseId);
        return result.size() == 0 ? null : result;
    }
    private List<ExtRegprofProfessionExperienceDatesRecord> getRegprofProfessionExperienceDates(Integer professionExperienceDocumentId) throws SQLException {
        return new AutoPopulatingList<ExtRegprofProfessionExperienceDatesRecord>(
                selectRecords(ExtRegprofProfessionExperienceDatesRecord.class, " prof_experience_document_id = ? order by id", professionExperienceDocumentId), 
                ExtRegprofProfessionExperienceDatesRecord.class);
    }
    public Integer getApplicationStatus(int applicationId, int userId) throws SQLException {
        List<IntegerValue> res = selectRecords("select status as value from eservices.regprof_application where representative_id in (select id from eservices.person where user_id = ?) and id = ?", IntegerValue.class, userId, applicationId);
        if (res.size() == 0) {
            return null;
        }
        return res.get(0).getValue();
    }
    public void updateApplicationXml(int applicationId, String xml) throws SQLException {
        execute("UPDATE eservices.regprof_application set app_xml = ? where id = ?", xml, applicationId);
    }
    public void linkLiabilityToExternalApplication(int extApplicationId, int liabilityId) throws SQLException {
        ExtRegprofApplicationLiabilityRecord rec = new ExtRegprofApplicationLiabilityRecord(0, extApplicationId, liabilityId);
        insertRecord(rec);
        
    }
    public void updateApplicationStatusOnApplicationApplying(int applicationId, int statusId, java.sql.Timestamp date, Integer serviceType) throws SQLException {
        execute("UPDATE eservices.regprof_application set status = ?, date_submitted = ?, service_type_id = ? where id = ?", statusId, date, serviceType, applicationId);
    }
    
    public void updateApplicationStatusOnApplicationSubmition(int applicationId, int internalApplicationId, int statusId) throws SQLException {
        execute("UPDATE eservices.regprof_application set status = ?, regprof_application_id = ? where id = ?", statusId, internalApplicationId,  applicationId);
    }

    public ExtRegprofDocumentRecipientRecord getDocumentRecipient(int applicationId) throws SQLException {
        return selectRecord(ExtRegprofDocumentRecipientRecord.class, "application_id = ?", applicationId);
    }
    public void saveDocumentRecipient(ExtRegprofDocumentRecipientRecord rec) throws SQLException {
        if (rec.getId() == 0) {
            insertRecord(rec);
        } else {
            updateRecord(rec);
        }
    }
    public void deleteDocumentRecipient(int applicationId) throws SQLException {
        execute("delete from eservices.regprof_document_recipient where application_id = ?", applicationId);
    }


    public List<ExtRegprofApplicationCommentExtendedRecord> getApplicationCommentExtendedRecords(int applicationId) throws SQLException {
        return selectRecords(SELECT_APPLICATION_COMMENT + " WHERE application_id = ?", ExtRegprofApplicationCommentExtendedRecord.class, applicationId);
    }
    public ExtRegprofApplicationCommentExtendedRecord getApplicationCommentExtendedRecord(int id) throws SQLException {
        return selectRecords(SELECT_APPLICATION_COMMENT + " WHERE ac.id = ?", ExtRegprofApplicationCommentExtendedRecord.class, id).get(0);
    }

    public void markApplicationFinished(int applicationId) throws SQLException {
        execute("UPDATE eservices.regprof_application set status = ? where id = ?", ExtApplication.STATUS_FINISHED, applicationId);
    }

    public static void main(String[] args) throws SQLException {
    	ExtRegprofApplicationsDB db = new ExtRegprofApplicationsDB(new StandAloneDataSource());
    	db.updateApplicationStatusOnApplicationApplying(18, 1, new java.sql.Timestamp(new java.util.Date().getTime()), 1);
    }
}
