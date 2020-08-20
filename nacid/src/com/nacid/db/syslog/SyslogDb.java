package com.nacid.db.syslog;

import com.nacid.bl.impl.Utils;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.users.UserSysLogOperationRecordExtended;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Georgi
 * Date: 20.8.2020 г.
 * Time: 9:23
 */
public class SyslogDb extends DatabaseService {
    private static final String SYSLOG_COLUMNS = "slo.record_id as id, sl.user_id, u.username, u.fullname as user_full_name, session_id, remote_addr as remote_address, remote_host, time_login, time_logout, web_app as web_application_id, group_name, operation_name, query_string, slo.date_created, null::varchar as description";
    private static final String SELECT_SYSLOG_RECORD = "select {0} from users_sys_log_operations slo\n" +
            "join users_sys_log sl on sl.record_id = slo.syslog_record_id\n" +
            "join users u on u.id = sl.user_id\n" +
            "WHERE slo.group_name != ''syslog'' AND \n";
    public SyslogDb(DataSource ds) {
        super(ds);
    }
    public UserSysLogOperationRecordExtended getUserSysLogOperationRecordExtended(int id) throws SQLException {
        String sql = MessageFormat.format(SELECT_SYSLOG_RECORD, SYSLOG_COLUMNS);
        sql += "slo.record_id = ?";
        List<UserSysLogOperationRecordExtended> res = selectRecords(sql, UserSysLogOperationRecordExtended.class, id);
        return res.size() == 0 ? null : res.get(0);
    }
    public int countUserSysLogOperationRecords(Integer userId, Timestamp dateFrom, Timestamp dateTo, Integer webAppId, String groupName, String operationName, String queryString, String sessionId ) throws SQLException {
        String sql = MessageFormat.format(SELECT_SYSLOG_RECORD, "count(*)::int as value");
        List<String> where = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        addSearchParams(where, params, userId, dateFrom, dateTo, webAppId, groupName, operationName, queryString, sessionId);
        sql += where.stream().collect(Collectors.joining(") AND (", "(", ")"));
        return selectRecords(sql, IntegerValue.class, params.toArray()).get(0).getValue();
    }
    public List<UserSysLogOperationRecordExtended> getUserSysLogOperationRecordsExtended(Integer userId, Timestamp dateFrom, Timestamp dateTo, Integer webAppId, String groupName, String operationName, String queryString, String sessionId ) throws SQLException {
        String sql = MessageFormat.format(SELECT_SYSLOG_RECORD, SYSLOG_COLUMNS);
        List<String> where = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        addSearchParams(where, params, userId, dateFrom, dateTo, webAppId, groupName, operationName, queryString, sessionId);
        sql += where.stream().collect(Collectors.joining(") AND (", "(", ")"));
        return selectRecords(sql, UserSysLogOperationRecordExtended.class, params.toArray());
    }

    private void addSearchParams(List<String> where, List<Object> objects, Integer userId, Timestamp dateFrom, Timestamp dateTo, Integer webAppId, String groupName, String operationName, String queryString, String sessionId) {
        if (userId != null) {
            where.add("sl.user_id = ?");
            objects.add(userId);
        }
        if (dateFrom != null) {
            where.add("slo.date_created >= ?");
            objects.add(dateFrom);
        }
        if (dateTo != null) {
            where.add("slo.date_created <= ?");
            objects.add(dateTo);
        }
        if (webAppId != null) {
            where.add("sl.web_app = ?");
            objects.add(webAppId);
        }
        if (!StringUtils.isEmpty(groupName)) {
            where.add("group_name = ?");
            objects.add(groupName);
        }
        if (!StringUtils.isEmpty(operationName)) {
            where.add("operation_name = ?");
            objects.add(operationName);
        }
        if (!StringUtils.isEmpty(queryString)) {
            List<String> or = new ArrayList<>();
            or.add("query_string = ?");
            objects.add(queryString);//tochno syvpadenie
            or.add("query_string ilike ?");
            objects.add(queryString + "&%");//v nachaloto
            or.add("query_string ilike ?");
            objects.add("%&" + queryString + "&%");//po sredata
            or.add("query_string ilike ?");
            objects.add("%&" + queryString);//v kraq
            where.add(or.stream().collect(Collectors.joining(") OR (", "(", ")")));
        }
        if (!StringUtils.isEmpty(sessionId)) {
            where.add("session_id = ?");
            objects.add(sessionId);
        }
    }

    public static void main(String[] args) throws SQLException {
        BasicConfigurator.configure();
        StandAloneDataSource ds = new StandAloneDataSource();
        SyslogDb db = new SyslogDb(ds);
        Timestamp from = new Timestamp(Utils.localDateToDate(LocalDate.of(2020, 1, 1)).getTime());
        Timestamp to = new Timestamp(new Date().getTime());
        List<UserSysLogOperationRecordExtended> recs = db.getUserSysLogOperationRecordsExtended(null, from, to, 1, null, null, "id=5", "123");

        int cnt = db.countUserSysLogOperationRecords(null, from, to, 1, null, null, "id=5", "123");
        System.out.println(recs.size());
        System.out.println(cnt);
    }
}
