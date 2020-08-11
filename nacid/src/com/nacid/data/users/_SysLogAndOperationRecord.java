package com.nacid.data.users;
import java.sql.Timestamp;

public class _SysLogAndOperationRecord {
    private int userId;
    private int sysLogRecordId;
    private String groupName;
    private String operationName;
    private String queryString;
    private Timestamp operationDate;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public String getQueryString() {
        return queryString;
    }
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    public Timestamp getOperationDate() {
        return operationDate;
    }
    public void setOperationDate(Timestamp operationDate) {
        this.operationDate = operationDate;
    }
    public int getSysLogRecordId() {
        return sysLogRecordId;
    }
    public void setSysLogRecordId(int sysLogRecordId) {
        this.sysLogRecordId = sysLogRecordId;
    }
}
