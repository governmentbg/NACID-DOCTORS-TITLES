package com.nacid.db.external.applications;

import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.external.applications.*;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtApplicationsDB extends DatabaseService {
	public static final String UPDATE_APPLICATION_XML_SQL = "UPDATE eservices.rudi_application set app_xml = ? WHERE id = ?";
    public static final String SELECT_EAPPLIED_APPLICATIONS = "select distinct apn.id, applicant_type,apn.company_id,\n" +
            "            (case\n" +
            "            WHEN applicant_type = 0 THEN (SELECT fname||' '||(CASE WHEN SNAME = '' THEN '' ELSE coalesce(sname||' ','') END)||lname from eservices.person where applicant_id = person.id) \n" +
            "            ELSE (SELECT name from eservices.company where company.id = apn.company_id) END) as names, (case when sdc.id is not null THEN 1 ELSE 0 END) as esigned, \n" +
            "            (array(select series_name from numgenerator.entry_num_series ens join eservices.rudi_application_kind akd on ens.id = akd.entry_num_series_id where akd.application_id = apn.id)),\n" +
            "            (array(select entry_num_series_id from eservices.rudi_application_kind akd where akd.application_id = apn.id))\n" +
            "             as application_kinds, time_of_creation, app_status as application_status,\n" +
            "            coalesce((select 1 from eservices.rudi_application_comments where application_id = apn.id and system_message = 0 limit 1),0) as communicated\n"+
            "            from eservices.rudi_application apn\n" +
            "            left join eservices.rudi_signed_docs sdc on sdc.ext_app_id = apn.id\n" +
            "            left join eservices.rudi_application_kind akd on akd.application_id = apn.id\n" +
            "            WHERE apn.app_status in (0,1,4)\n" +
            "            and akd.entry_num_series_id in ({SERIES_IDS})\n" +
            "            order by time_of_creation desc";
    public static final String SELECT_APPLICATION_COMMENT = "select ac.*, processed as int_email_processed, in_out as int_email_incoming, sent_to as email_recipient, subject as email_subject, system_message\n" +
            "from eservices.rudi_application_comments  ac\n" +
            "left join email e on e.id = ac.email_id\n";

    public ExtApplicationsDB(DataSource ds) {
        super(ds);
    }

    public void markApplicationFinished(int applicationId) throws SQLException {
        execute("UPDATE eservices.rudi_application set app_status = ? where id = ?", ExtApplication.STATUS_FINISHED, applicationId);
    }
    
    public List<ExtApplicationRecord> getApplicationRecordsByRepresentativeId(int applicantId, List<Integer> entryNumSeriesId) throws SQLException {
        List<String> sql = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        objects.add(applicantId);
        sql.add("representative_id = ?");
        if (!CollectionUtils.isEmpty(entryNumSeriesId)) {
            objects.addAll(entryNumSeriesId);
            sql.add("id in (select application_id from eservices.rudi_application_kind where entry_num_series_id in (" + SQLUtils.parametersCountToParameterList(entryNumSeriesId.size()) + "))");
        }
        sql.add("app_status != ?");
        objects.add(ExtApplication.STATUS_FINISHED);
    	return selectRecords(ExtApplicationRecord.class, StringUtils.join(sql, " AND "), objects.toArray());
    }

    public List<ExtApplicationRecord> getApplicationRecordsByInternalApplicationId(int internalApplicationId) throws SQLException {
    	return selectRecords(ExtApplicationRecord.class, " application_id = ? ", internalApplicationId);
    }
    
    public List<ExtApplicationRecord> getApplicationsByStatus(List<Integer> statuses) throws SQLException {
        String where = "app_status in (";
        for(int i = 0; i < statuses.size(); i++) {
            if(i > 0) {
                where += ",";
            }
            where += "?";
        }
        where += ")";
        return selectRecords(ExtApplicationRecord.class, where, statuses.toArray());
    }
    public void updateApplicationXml(int applicationId, String xml) throws SQLException {
    	Connection connection = getConnection();
    	try {
  		  PreparedStatement p = connection.prepareStatement(UPDATE_APPLICATION_XML_SQL);
  		  try {
  			  p.setString(1, xml);
  			  p.setInt(2, applicationId);
  			  p.executeUpdate();
  		  } finally {
  			  p.close();
  		  }
  	  } finally {
  		  release(connection);
  	  }
    }


    public ExtESignedInformationRecord getESignedInformationRecord(int applicationId) throws SQLException{
    	List<ExtESignedInformationRecord> result = selectRecords(ExtESignedInformationRecord.class, " ext_app_id = ? ", applicationId);
    	return result.size() == 0 ? null : result.get(0);
    }

    public List<EApplyApplicationRecord> getEapplyApplicationRecords(List<Integer> numSeriesIds) throws SQLException {
        String sql =SELECT_EAPPLIED_APPLICATIONS.replace("{SERIES_IDS}", SQLUtils.columnsToParameterList(StringUtils.join(numSeriesIds, ", ")));
        List<Object> objects = new ArrayList<Object>();
        objects.addAll(numSeriesIds);
        List<EApplyApplicationRecord> result = selectRecords(sql, EApplyApplicationRecord.class, objects.toArray());
        return result;
    }
    public List<ExtApplicationKindRecord> getApplicationKindsPerApplication(int extApplicationId) throws SQLException {
        List<ExtApplicationKindRecord> result = selectRecords(ExtApplicationKindRecord.class, " application_id = ? order by id", extApplicationId);
        return result;
    }
    public ExtDocumentRecipientRecord getDocumentRecipient(int applicationId) throws SQLException {
        return selectRecord(ExtDocumentRecipientRecord.class, "application_id = ?", applicationId);
    }
    public void saveDocumentRecipient(int applicationId, String name, int countryId, String city, String district, String postCode, String address, String mobilePhone) throws SQLException {
        ExtDocumentRecipientRecord rec = getDocumentRecipient(applicationId);
        if (rec == null) {
            rec = new ExtDocumentRecipientRecord();
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
    public void deleteDocumentRecipient(int applicationId) throws SQLException {
        execute("delete from eservices.rudi_document_recipient where application_id = ?", applicationId);
    }

    public List<ExtApplicationCommentExtendedRecord> getApplicationCommentExtendedRecords(int applicationId) throws SQLException {
        return selectRecords(SELECT_APPLICATION_COMMENT + " WHERE application_id = ?", ExtApplicationCommentExtendedRecord.class, applicationId);
    }

    public ExtApplicationCommentExtendedRecord getApplicationCommentExtendedRecord(int id) throws SQLException {
        return selectRecords(SELECT_APPLICATION_COMMENT + " WHERE ac.id = ?", ExtApplicationCommentExtendedRecord.class, id).get(0);
    }

    public static void main(String[] args) throws SQLException {
    	ExtApplicationsDB db = new ExtApplicationsDB(new StandAloneDataSource());
    	//db.updateApplicationXml(10, "alabala");
    	//db.getESignedInformationRecord(20);
        System.out.println(db.getEapplyApplicationRecords(Arrays.asList(4)));
    }
    
    
    
    

    
}
