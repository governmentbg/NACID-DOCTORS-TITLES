package com.nacid.data.comission;

public class CommissionParticipationRecord {
	private int id;
	private int expertId;
	private int sessionId;
	private int intNotified;
	private int intParticipated;
	private String notes;
	public CommissionParticipationRecord() {
	}
	public CommissionParticipationRecord(int id, int expertId, int sessionId, int intNotified, int intParticipated, String notes) {
		this.id = id;
		this.expertId = expertId;
		this.sessionId = sessionId;
		this.intNotified = intNotified;
		this.intParticipated = intParticipated;
		this.notes = notes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExpertId() {
		return expertId;
	}
	public void setExpertId(int expertId) {
		this.expertId = expertId;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	public int getIntNotified() {
		return intNotified;
	}
	public void setIntNotified(int intNotified) {
		this.intNotified = intNotified;
	}
	public int getIntParticipated() {
		return intParticipated;
	}
	public void setIntParticipated(int intParticipated) {
		this.intParticipated = intParticipated;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	
	
}
