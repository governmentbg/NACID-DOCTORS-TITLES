package com.nacid.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.nacid.data.DataConverter;
import com.nacid.data.applications.ApplicationRecord;
import com.nacid.data.users.ApplicationChangesHistoryRecord;
import com.nacid.data.users._SysLogAndOperationRecord;
import com.nacid.db.utils.StandAloneDataSource;

public class UpdateApplicationChangesHistory {
    public static final String SELECT_SYS_LOG_OPERATIONS = "select op.group_name, op.operation_name, op.query_string, log.user_id, op.date_created as operation_date, op.syslog_record_id " + 
                                " from users_sys_log_operations  op " +
                                " join users_sys_log log on (op.syslog_record_id = log.record_id)";
    public static void main(String[] args) throws SQLException {
        //DataSource ds = new StandAloneDataSource("jdbc:postgresql://fly.nacid.bg:5432/NACID/", "postgres", "postgres");
        DataSource ds = new StandAloneDataSource();
        ds.getConnection().setAutoCommit(false);
        MigratinDBService service = new MigratinDBService(ds);
        
        
        /**
         * key - imeto na parametera - nqkyde e id, nqkyde application_id, nqkyde appID...
         * value - logging zapisi...
         */
        List<Map<String, List<_SysLogAndOperationRecord>>> allRecords = new ArrayList<Map<String, List<_SysLogAndOperationRecord>>>();
        
        //applications-save
        Map<String, List<_SysLogAndOperationRecord>> m = new HashMap<String, List<_SysLogAndOperationRecord>>();
        m.put("id", service.selectRecords(SELECT_SYS_LOG_OPERATIONS + " WHERE group_name = ? and operation_name = ?", _SysLogAndOperationRecord.class, "applications", "save"));
        allRecords.add(m);
        
        //applications_status - save
        m = new HashMap<String, List<_SysLogAndOperationRecord>>();
        m.put("application_id", service.selectRecords(SELECT_SYS_LOG_OPERATIONS + " WHERE group_name = ? and operation_name = ?", _SysLogAndOperationRecord.class, "applications_status", "save"));
        allRecords.add(m);
        //application_motives - save
        m = new HashMap<String, List<_SysLogAndOperationRecord>>();
        m.put("id", service.selectRecords(SELECT_SYS_LOG_OPERATIONS + " WHERE group_name = ? and operation_name = ?", _SysLogAndOperationRecord.class, "application_motives", "save"));
        allRecords.add(m);
        
        //application_attachment - new
        m = new HashMap<String, List<_SysLogAndOperationRecord>>();
        m.put("applID", service.selectRecords(SELECT_SYS_LOG_OPERATIONS + " WHERE group_name = ? and operation_name = ?", _SysLogAndOperationRecord.class, "application_attachment", "new"));
        allRecords.add(m);

        
        //applications_expert - save
        m = new HashMap<String, List<_SysLogAndOperationRecord>>();
        m.put("application_id", service.selectRecords(SELECT_SYS_LOG_OPERATIONS + " WHERE group_name = ? and operation_name = ?", _SysLogAndOperationRecord.class, "applications_expert", "save"));
        allRecords.add(m);
        
        
        //dipl_exam_attachment - new
        m = new HashMap<String, List<_SysLogAndOperationRecord>>();
        m.put("applID", service.selectRecords(SELECT_SYS_LOG_OPERATIONS + " WHERE group_name = ? and operation_name = ?", _SysLogAndOperationRecord.class, "dipl_exam_attachment", "new"));
        allRecords.add(m);
        
        
        //expert_statement_attachment - new
        m = new HashMap<String, List<_SysLogAndOperationRecord>>();
        m.put("applID", service.selectRecords(SELECT_SYS_LOG_OPERATIONS + " WHERE group_name = ? and operation_name = ?", _SysLogAndOperationRecord.class, "expert_statement_attachment", "new"));
        allRecords.add(m);
        
        List<ApplicationChangesHistoryRecord> appChangesHistoryRecords = new ArrayList<ApplicationChangesHistoryRecord>();
        for (Map<String, List<_SysLogAndOperationRecord>> map:allRecords) {
            for (String param:map.keySet()) {
                List<_SysLogAndOperationRecord> records = map.get(param);
                for (_SysLogAndOperationRecord r:records) {
                    List<String> lst = getUrlParameters(r.getQueryString()).get(param);
                    Integer appId = lst == null || lst.size() == 0 ? null : DataConverter.parseInteger(lst.get(0), null);
                    if (appId == null) {
                        continue;
                    }
                    appChangesHistoryRecords.add(generateApplicationChangesHistoryRecord(r, appId));
                }    
            }    
        }
        
        
        List<ApplicationRecord> applications = service.selectRecords(ApplicationRecord.class, null);
        for (ApplicationRecord a:applications) {
            appChangesHistoryRecords.add(new ApplicationChangesHistoryRecord(null, a.getId(), a.getCreatedByUserId(), a.getTimeOfCreation(), "save", "applications"));
        }
        
        Collections.sort(appChangesHistoryRecords, new Comparator<ApplicationChangesHistoryRecord>() {
            public int compare(ApplicationChangesHistoryRecord o1, ApplicationChangesHistoryRecord o2) {
                if (o1.getDate() == null) {
                    return 1;
                } else if (o2.getDate() == null) {
                    return -1;
                }
                return o1.getDate().compareTo(o2.getDate());
            }
        
        });
        for (ApplicationChangesHistoryRecord r:appChangesHistoryRecords) {
            System.out.println(r);
            if (r.getDate() != null) {
                service.insertRecord(r);    
            }
        }
        
        ds.getConnection().commit();
    }
    private static ApplicationChangesHistoryRecord generateApplicationChangesHistoryRecord(_SysLogAndOperationRecord r, Integer applicationId) {
        ApplicationChangesHistoryRecord result = new ApplicationChangesHistoryRecord(null, applicationId, r.getUserId(), r.getOperationDate(), r.getOperationName(), r.getGroupName());
        return result;
    }
    public static Map<String, List<String>> getUrlParameters(String query)  {
        Map<String, List<String>> params = new HashMap<String, List<String>>();

        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            String key = pair[0];
            String value = "";
            if (pair.length > 1) {
                value = pair[1];
            }
            List<String> values = params.get(key);
            if (values == null) {
                values = new ArrayList<String>();
                params.put(key, values);
            }
            values.add(value);
        }

        return params;
    }
}
