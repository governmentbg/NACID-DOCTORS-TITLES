package com.nacid.data.users;

import java.sql.Timestamp;

public class UserSysLogRecord {
	
	private int recordId;
	private int userId;
	private String sessionId;
	private String remoteAddress;
	private String remoteHost;
	private Timestamp timeLogin;
	private Timestamp timeLogout;
	private int webApplicationId;
	public UserSysLogRecord() {
	}
	public UserSysLogRecord(int recordId, int userId, String sessionId, String remoteAddress, String remoteHost, Timestamp timeLogin,
			Timestamp timeLogout) {
		this.recordId = recordId;
		this.userId = userId;
		this.sessionId = sessionId;
		this.remoteAddress = remoteAddress;
		this.remoteHost = remoteHost;
		this.timeLogin = timeLogin;
		this.timeLogout = timeLogout;
	}
	public int getRecordId() {
		return recordId;
	}
	public int getUserId() {
		return userId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public String getRemoteHost() {
		return remoteHost;
	}
	public Timestamp getTimeLogin() {
		return timeLogin;
	}
	public Timestamp getTimeLogout() {
		return timeLogout;
	}
	public int getWebApplicationId() {
	    return webApplicationId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	public void setTimeLogin(Timestamp timeLogin) {
		this.timeLogin = timeLogin;
	}
	public void setTimeLogout(Timestamp timeLogout) {
		this.timeLogout = timeLogout;
	}
	public void setWebApplicationId(int webApplicationId) {
	    this.webApplicationId = webApplicationId;
	}
	
}
