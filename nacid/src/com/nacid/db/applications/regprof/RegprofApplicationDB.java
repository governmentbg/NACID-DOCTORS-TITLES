package com.nacid.db.applications.regprof;

import com.nacid.bl.applications.PersonDocument;
import com.nacid.bl.applications.regprof.RegprofApplicationDetails;
import com.nacid.bl.applications.regprof.RegprofTrainingCourseDetailsBase;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.applications.AppStatusHistoryRecord;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.applications.PersonRecord;
import com.nacid.data.common.ResultRow;
import com.nacid.data.regprof.RegprofProfessionExperienceDatesRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceExaminationRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.data.regprof.applications.*;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AutoPopulatingList;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
//RayaWritten----------------------------------------------------------------------------------------------------
public class RegprofApplicationDB extends DatabaseService {

    private static final String SELECT_REGPROF_APP_DETAILS_FOR_LIST = "SELECT ra.id, ra.app_num, ra.app_date, ra.applicant_id, ra.status, ra.service_type_id, ra.end_date, " +
    " p.fName||coalesce(' '||p.sName,'')||coalesce(' '||p.lName,'') full_name, ras.name  status_name, null prof_inst_id, null prof_inst_name, null speciality_id, null speciality_name," +
    " null qualification_id, null qualification_name, ersd.id ext_signed_doc_id, ra.imi_correspondence, ra.docflow_status_id, dss.name as docflow_status_name " +
    " FROM regprof.regprof_application ra " +
    " LEFT JOIN person p ON ra.applicant_id = p.id " +
    " LEFT JOIN nomenclatures.app_status ras ON ras.id = ra.status " +
    " LEFT JOIN nomenclatures.app_status_docflow dss ON dss.id = ra.docflow_status_id " +
    " LEFT JOIN eservices.regprof_signed_docs ersd ON ersd.ext_app_id = (SELECT id FROM eservices.regprof_application WHERE regprof_application_id = ra.id)"+
    " WHERE 1=1 ";

    private static final String SELECT_REGPROF_APP_STATUSES_HISTORY = "SELECT rash.id, rash.application_id, rash.status_id, rash.date_assigned, " +
    		" rash.user_assigned, nras.name status_name, ens.is_legal as is_legal_status " +
    		" FROM regprof.app_status_history rash " +
            " JOIN nomenclatures.app_status nras ON rash.status_id = nras.id " +
            " JOIN backoffice.app_status_entry_num_series ens ON (ens.app_status_id = nras.id ) " +
    		" WHERE ens.entry_num_series_id = ? and rash.application_id = ? " +
    		" ORDER BY rash.id DESC, rash.date_assigned DESC";


    private static final String SELECT_REGPROF_FINAL_STATUS_HISTORY_RECORD = "SELECT rash.id, rash.application_id, rash.status_id, rash.date_assigned, " +
            " rash.user_assigned, nras.name status_name, ens.is_legal as is_legal_status " +
            " FROM regprof.app_status_history rash " +
            " JOIN nomenclatures.app_status nras ON rash.status_id = nras.id " +
            " JOIN backoffice.app_status_entry_num_series ens ON (ens.app_status_id = nras.id) " +
            " WHERE ens.entry_num_series_id = ? and rash.id = (select final_status_history_id from regprof.regprof_application where id = ?) " +
            " ORDER BY rash.id DESC, rash.date_assigned DESC";

    private static final String SELECT_REGPROF_TC_PERSON_INFO = "SELECT rtc.civil_id_type, rtc.civil_id, rtc.fname, rtc.sname, rtc.lname "+
            " FROM regprof.training_course rtc " +
            " WHERE rtc.id = ? ";
    private static final String SELECT_APPLICATION_DOCFLOW_STATUS_HISTORY = "select asdh.*, asd.name as application_docflow_status_name from regprof.app_status_docflow_history   asdh\n" +
            "join nomenclatures.app_status_docflow asd on asd.id = asdh.app_status_docflow_id ";

    private static final String SELECT_APPLICATIONS_FOR_PUBLIC_REGISTER_SQL = "select apn.id, apn.app_num, apn.app_date, \n" +
                                                                                    " CASE WHEN apn.names_dont_match = 1 THEN tce.document_fname||coalesce(' '||tce.document_sname,'')||coalesce(' '||tce.document_lname,'') ELSE apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') END as applicant_name, \n" +
                                                                                    " acy.name as application_country_name, \n" +
                                                                                    " null as cert_number, null as invalidated, \n" +
                                                                                    " (select cert_number from regprof.cert_number_to_attached_doc where application_id = apn.id and invalidated = 0 order by id desc limit 1 ) validated_cert_number,\n" + 
                                                                                    " (select array(select cert_number from regprof.cert_number_to_attached_doc where application_id = apn.id and invalidated = 1)) invalidated_cert_numbers," +
                                                                                    " fss.status_id as final_status_id\n" +
                                                                                    " from regprof.regprof_application apn \n" +
                                                                                    " join regprof.app_status_history as fss on (fss.id = apn.final_status_history_id) \n" +
                                                                                    " join regprof.training_course as tce on (tce.id = apn.training_course_id) \n" +
                                                                                    " join person apt on (apt.id = apn.applicant_id) \n" +
                                                                                    " left join nomenclatures.country acy on (acy.id = apn.application_country_id) \n";
    /*public static final String UPDATE_REGPROF_TC_PERSON_INFO = "UPDATE regprof.training_course SET civil_id_type = ?, civil_id = ?, fname = ?, sname=?, lname = ? " +
    		" WHERE id = ? ";*/
    public RegprofApplicationDB(DataSource ds){
        super(ds);
    }

    public List<RegprofCertificateNumberToAttachedDocRecord> getCertificateNumberToAttachedDocRecords(int applicationId, Integer invalidated) throws SQLException {
        String sql = " application_id = ? ";
        List<Object> objects = new ArrayList<Object>();
        objects.add(applicationId);
        if (invalidated != null) {
            sql += " AND invalidated = ? ";
            objects.add(invalidated);
        }
        sql += " ORDER BY id desc";
        return selectRecords(RegprofCertificateNumberToAttachedDocRecord.class, sql , objects.toArray());
    }

    public List<RegprofCertificateNumberToAttachedDocRecord> getCertificateNumberToAttachedDocRecords(List<Integer> applicationIds) throws SQLException {
        String sql = " application_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(applicationIds, ",")) + ") ";
        return selectRecords(RegprofCertificateNumberToAttachedDocRecord.class, sql , applicationIds.toArray());
    }

    public List<PersonDocumentRecord> getDocumentRecordsByPersonId(int personId) throws SQLException {
       return selectRecords(PersonDocumentRecord.class, "person_id = ? and active = 1", personId);
    }

    public List<RegprofApplicationDetailsForListRecord> getRegprofApplicationDetailsForList(boolean onlyInProcedure) {
        try {
            String sql = SELECT_REGPROF_APP_DETAILS_FOR_LIST;
            List<Object> params = new ArrayList<Object>();
            if (onlyInProcedure) {
                sql += " AND ra.docflow_status_id = ?";
                params.add(ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE);
            }
            sql += " ORDER BY ra.id desc";
            return selectRecords(sql, RegprofApplicationDetailsForListRecord.class, params.size() == 0 ? null : params.toArray());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public PersonRecord getPerson(Integer id) throws SQLException {
        return selectRecord(new PersonRecord(), id);
    }

    public RegprofApplicationDetails getRegprofApplicationDetails(Integer id) throws SQLException {
        return selectRecord(new RegprofApplicationDetailsImpl(), id);
    }

    public PersonDocumentRecord getPersonDocumentById(int id) throws SQLException {
        return selectRecord(PersonDocumentRecord.class, "id = ?", id);
    }
    public PersonDocument getPersonDocument(Integer personId) throws SQLException {
        List<PersonDocumentRecord> applicantDocuments = getDocumentRecordsByPersonId(personId);
        if (applicantDocuments.size() > 1) {
            return applicantDocuments.get(applicantDocuments.size()-1);
        } else if (applicantDocuments.size() == 1) {
            return applicantDocuments.get(0);
        } else {
            return null;
        }
    }

    public void setOldDocumentsInactive(Integer personId) throws SQLException{
        List<PersonDocumentRecord> applicantDocuments = getDocumentRecordsByPersonId(personId);
        for(PersonDocument applD: applicantDocuments){
            applD.setActive(0);
            updateRecord(applD);
        }
    }

    public RegprofProfessionExperienceRecord getRegprofProfessionExperience(Integer applicationId) throws SQLException {
        List<RegprofProfessionExperienceRecord> list = selectRecords(RegprofProfessionExperienceRecord.class, " training_course_id = (select training_course_id from regprof.regprof_application where id = ? ) ", applicationId);
        if (list.isEmpty()) return null;
        else return list.get(0);
    }

    public List<RegprofProfessionExperienceDocumentRecord> getRegprofProfessionExperienceDocumentRecords(Integer professionExperienceId) throws SQLException {
        List<RegprofProfessionExperienceDocumentRecord> lst = new AutoPopulatingList<RegprofProfessionExperienceDocumentRecord>(selectRecords(RegprofProfessionExperienceDocumentRecord.class, " prof_experience_id = ? order by id", professionExperienceId), RegprofProfessionExperienceDocumentRecord.class);
        for (RegprofProfessionExperienceDocumentRecord rec:lst) {
            rec.setDates(getRegprofProfessionExperienceDates(rec.getId()));
        }
        return lst;
    }

    private List<RegprofProfessionExperienceDatesRecord> getRegprofProfessionExperienceDates(Integer professionExperienceDocumentId) throws SQLException {
        return new AutoPopulatingList<RegprofProfessionExperienceDatesRecord>(
                selectRecords(RegprofProfessionExperienceDatesRecord.class, " prof_experience_document_id = ? order by id", professionExperienceDocumentId), 
                RegprofProfessionExperienceDatesRecord.class);
    }

    public void deleteAllProfessionExperienceDocumentRecords(Integer professionExperienceId)  throws SQLException {
        execute("DELETE FROM regprof.profession_experience_exam_attached_docs WHERE parent_id IN (SELECT id from regprof.profession_experience_documents WHERE prof_experience_id = ?)", professionExperienceId);
        execute("DELETE FROM regprof.profession_experience_dates WHERE prof_experience_document_id in (SELECT id from regprof.profession_experience_documents WHERE prof_experience_id = ?)", professionExperienceId);
        execute("DELETE FROM regprof.profession_experience_documents WHERE prof_experience_id = ?", professionExperienceId);
    }

    public void deleteProfessionExperienceRecords(int trainingCourseId) throws SQLException {
        execute("DELETE FROM regprof.profession_experience_exam_attached_docs WHERE parent_id IN (SELECT id from regprof.profession_experience_documents WHERE prof_experience_id in (select id from regprof.profession_experience where training_course_id = ?))", trainingCourseId);
        execute("DELETE FROM regprof.profession_experience_dates where prof_experience_document_id in (select id from regprof.profession_experience_documents  where prof_experience_id in (select id from regprof.profession_experience  where training_course_id = ?))", trainingCourseId);
        execute("DELETE from regprof.profession_experience_documents  where prof_experience_id in (select id from regprof.profession_experience  where training_course_id = ?)", trainingCourseId);
        execute("DELETE FROM regprof.profession_experience_examination WHERE regprof_profession_experience_id = (select id from regprof.profession_experience where training_course_id = ?)", trainingCourseId);
        execute("DELETE FROM regprof.profession_experience  where training_course_id = ?", trainingCourseId);
    }

    public void deleteProfessionExperienceDocumentRecords(Integer professionExperienceId, List<Integer> docIdsNotToDelete) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        String sql = " prof_experience_id = ?";
        objects.add(professionExperienceId);
        String idsNotToDelete = StringUtils.join(docIdsNotToDelete, ", ");
        if (docIdsNotToDelete != null && docIdsNotToDelete.size() > 0) {
            sql += " AND id not in ( " + idsNotToDelete + ") "; 
        }
        execute("DELETE FROM regprof.profession_experience_exam_attached_docs WHERE parent_id IN (SELECT id from regprof.profession_experience_documents WHERE (" + sql + "))", objects.toArray());
        execute("DELETE FROM regprof.profession_experience_dates  WHERE prof_experience_document_id in (SELECT id from regprof.profession_experience_documents WHERE (" + sql + "))", objects.toArray());
        deleteRecords(RegprofProfessionExperienceDocumentRecord.class, sql, objects.toArray());
    }

    public void deleteProfessionExperienceDatesRecords(Integer professionExperienceDocumentId, List<Integer> dateIdsNotToDelete) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        String sql = "prof_experience_document_id = ?";
        objects.add(professionExperienceDocumentId);
        if (dateIdsNotToDelete != null && dateIdsNotToDelete.size() > 0) {
            sql += " AND id not in ( " + StringUtils.join(dateIdsNotToDelete, ", ") + ") "; 
        }
        deleteRecords(RegprofProfessionExperienceDatesRecord.class, sql, objects.toArray());
    }

    public RegprofAppStatusHistoryForListRecord getFinalStatusHistoryRecord(Integer appId) throws SQLException {
        List<RegprofAppStatusHistoryForListRecord> result = selectRecords(SELECT_REGPROF_FINAL_STATUS_HISTORY_RECORD, RegprofAppStatusHistoryForListRecord.class, NumgeneratorDataProvider.REGPROF_SERIES_ID, appId);
        return result.size() == 0 ? null : result.get(0);
    }

    public List<RegprofAppStatusHistoryForListRecord> getStatusHistoryForList(Integer appId) throws SQLException {
        return selectRecords(SELECT_REGPROF_APP_STATUSES_HISTORY, RegprofAppStatusHistoryForListRecord.class, NumgeneratorDataProvider.REGPROF_SERIES_ID, appId);
    }

    public RegprofProfessionExperienceExaminationRecord getProfessionExperienceExamination(Integer regprofProfessionExperienceId) throws SQLException {
        List<RegprofProfessionExperienceExaminationRecord> list = 
                selectRecords(RegprofProfessionExperienceExaminationRecord.class, " regprof_profession_experience_id = ? ", regprofProfessionExperienceId);
        if (list.isEmpty()) {
            return null;
        }
        else return list.get(0);
    }

    public void updateTrainingCourseDetails(RegprofTrainingCourseDetailsBase details) throws SQLException {
        updateRecord(details,
                "prof_institution_id",
                "document_number",
                "document_date",
                "sec_prof_qualification_id",
                "sec_qualification_degree_id",
                "high_prof_qualification_id",
                "high_edu_level_id",
                "sdk_prof_institution_id",
                "sdk_prof_qualification_id",
                "sdk_document_number",
                "sdk_document_date",
                "education_type_id",
                "sdk_edu_level_id",
                "has_experience",
                "has_education",
                "document_type",
                "sdk_document_type",
                "prof_institution_org_name_id",
                "sdk_prof_institution_org_name_id",
                "sec_caliber_id",
                "document_series",
                "document_reg_number",
                "sdk_document_series",
                "sdk_document_reg_number",
                "certificate_prof_qualification_id",
                "not_restricted",
                "regulated_education_training");
    }

    public RegprofTrainingCourseDetailsImpl getTrainingCourseDetails(int trainingCourseId) throws SQLException {
        return selectRecord(new RegprofTrainingCourseDetailsImpl(), trainingCourseId);
    }

    public List<RegprofApplicationRecordForPublicRegister> getRegprofApplicationRecordsForPublicRegisterByFinalStatuses(List<Integer> finalStatuses) throws SQLException {
        String sql = SELECT_APPLICATIONS_FOR_PUBLIC_REGISTER_SQL + " WHERE ";

        List<Object> params = new ArrayList<Object>();
        List<String> fs = new ArrayList<String>();
        for (Integer finalStatus : finalStatuses) {
            fs.add("fss.status_id = ?");
            params.add(finalStatus);
        }
        sql += "(" + StringUtils.join(fs, " OR ") + ")";

        return selectRecords(sql, RegprofApplicationRecordForPublicRegister.class, params.toArray());
    }



    public Integer getApplicationIdForExtReport(String docflowId, java.sql.Date docFlowDate, String personalId) throws SQLException {
        Connection con = getConnection();
        String sql = "select ra.id from regprof.regprof_application ra, person "
            + "where ra.app_num=? and ra.app_date=? and person.id = ra.applicant_id and person.civil_id=?";
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

    public Integer getRegprofApplicationIdForExtReport(String certNumber, String personalId) throws SQLException {
        Connection con = getConnection();
        String sql = "select ra.id from regprof.regprof_application ra "+
        " left join person on (person.id = ra.applicant_id) " +
        " left join regprof.cert_number_to_attached_doc cnum on (cnum.application_id = ra.id) " + 
        " where cnum.cert_number = ? and person.civil_id= ? ";
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

    public void invalidateOldCertificateNumbers(int applicationId, int invalidatedType) throws SQLException {
        execute("UPDATE regprof.cert_number_to_attached_doc SET invalidated = ? WHERE application_id = ? and invalidated = 0", invalidatedType, applicationId);
    }


    public void updateApplicationFinalStatusHistoryRecordId(int applicationId, int historyRecordId) throws SQLException {
        execute("UPDATE regprof.regprof_application set final_status_history_id = ? WHERE id = ?", historyRecordId, applicationId);
    }
    public List<AppStatusHistoryRecord> getAppStatusHistoryRecords(int applicationId) throws SQLException {
        return selectRecords(AppStatusHistoryRecord.class, "application_id=? order by date_assigned desc, id desc", applicationId);
    }
    public AppStatusHistoryRecord getAppStatusHistoryRecord(int id) throws SQLException {
        return selectRecord(AppStatusHistoryRecord.class, "id = ?", id);
    }

    public List<RegprofAppDocflowStatusHistoryRecordExtended> getAppDocflowStatusHistoryRecords(int applicationId) throws SQLException {
        return selectRecords(SELECT_APPLICATION_DOCFLOW_STATUS_HISTORY + " WHERE application_id=? order by date_assigned desc, id desc", RegprofAppDocflowStatusHistoryRecordExtended.class, applicationId);
    }

    public RegprofDocumentRecipientRecord getDocumentRecipient(int applicationId) throws SQLException {
        return selectRecord(RegprofDocumentRecipientRecord.class, "application_id = ?", applicationId);
    }
    public void saveDocumentRecipient(RegprofDocumentRecipientRecord rec) throws SQLException {
        if (rec.getId() == 0) {
            insertRecord(rec);
        } else {
            updateRecord(rec);
        }
    }
    public void deleteDocumentRecipient(int applicationId) throws SQLException {
        execute("delete from regprof.regprof_document_recipient where application_id = ?", applicationId);
    }

    public boolean isApplicationTransferredInApostilleSystem(int applicationId) throws SQLException {
        List<ResultRow> res = selectRecords("select apostille_transfer_date from regprof.regprof_application where id = ?", ResultRow.class,  applicationId);
        return res.size() != 0 && res.get(0).get("apostille_transfer_date") != null;
    }
    public void updateApostilleTransferDate(int applicationId, Date date) throws SQLException {
        execute("UPDATE regprof.regprof_application set apostille_transfer_date = ? where id = ? ", date, applicationId);
    }

    public static void main(String[] args) throws SQLException {
        StandAloneDataSource ds = new StandAloneDataSource();
        RegprofApplicationDB db = new RegprofApplicationDB(ds);
//        db.getRegprofApplicationRecordsForPublicRegisterByFinalStatuses(Arrays.asList(39));
        System.out.println(db.isApplicationTransferredInApostilleSystem(5));
    }

}
//----------------------------------------------------------------------------------------------------------------