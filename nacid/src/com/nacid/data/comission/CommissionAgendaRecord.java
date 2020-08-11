package com.nacid.data.comission;

public class CommissionAgendaRecord {
	private int id;
	private int applicationId;
	private int sessionId;
	
	public CommissionAgendaRecord(){
	}
	public CommissionAgendaRecord(int id, int applicationId, int sessionId) {
		this.id = id;
		this.applicationId = applicationId;
		this.sessionId = sessionId;

	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
}
