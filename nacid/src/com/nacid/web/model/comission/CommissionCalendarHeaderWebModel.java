package com.nacid.web.model.comission;


public class CommissionCalendarHeaderWebModel {
	public static final int ACTIVE_FORM_CALENDAR_EDIT = 1;
	public static final int ACTIVE_FORM_CALENDAR_APPLICATIONS_EDIT = 2;
	public static final int ACTIVE_FORM_CALENDAR_COMMISSION_MEMBERS_EDIT = 3;
	public static final int ACTIVE_FORM_CALENDAR_PROCESS_EDIT = 4;
	
	private int sessionId;
	private String action;
	private int activeFormId;
	public CommissionCalendarHeaderWebModel(int sessionId, String action, int activeFormId) {
		this.sessionId = sessionId;
		this.action = action;
		this.activeFormId = activeFormId ;
	}
	public int getSessionId() {
		return sessionId;
	}
	public String getAction() {
		return action;
	}
	public int getActiveFormId() {
		return activeFormId;
	}
	
}
