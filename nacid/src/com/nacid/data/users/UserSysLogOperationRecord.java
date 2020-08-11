package com.nacid.data.users;

import java.sql.Timestamp;

import com.nacid.bl.users.UserSysLogOperation;

public class UserSysLogOperationRecord implements UserSysLogOperation {
	private int recordId;
	private int userSysLogRecordId;
	private String groupName;
	private String operationName;
	private String queryString;
	private Timestamp dateCreated;
	public UserSysLogOperationRecord() {
	}
	public UserSysLogOperationRecord(int recordId, int userSysLogRecordId, String groupName, String operationName, String queryString, Timestamp dateCreated) {
		this.recordId = recordId;
		this.userSysLogRecordId = userSysLogRecordId;
		this.groupName = groupName;
		this.operationName = operationName;
		this.queryString = queryString;
		this.dateCreated = dateCreated;
	}
	public int getRecordId() {
		return recordId;
	}
	public int getUserSysLogRecordId() {
		return userSysLogRecordId;
	}
	public String getGroupName() {
		return groupName;
	}
	public String getOperationName() {
		return operationName;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public void setUserSysLogRecordId(int userSysLogRecordId) {
		this.userSysLogRecordId = userSysLogRecordId;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public Timestamp getDateCreated() {
        return dateCreated;
    }
	public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
    public String toString() {
	    final String TAB = "\n";
	
	    StringBuilder retValue = new StringBuilder();
	    
	    retValue.append("UserSysLogOperationRecord ( ")
	        .append(super.toString()).append(TAB)
	        .append("\trecordId = ").append(this.recordId).append(TAB)
	        .append("\tuserSysLogRecordId = ").append(this.userSysLogRecordId).append(TAB)
	        .append("\tgroupName = ").append(this.groupName).append(TAB)
	        .append("\toperationName = ").append(this.operationName).append(TAB)
	        .append("\tqueryString = ").append(this.queryString).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
}
