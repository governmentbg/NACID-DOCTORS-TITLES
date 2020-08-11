package com.nacid.bl.comision;

public interface CommissionParticipation {
	public ComissionMember getCommissionMember();
	public int getCommissionMemberId();
	public int getCalendarId();
	public boolean isNotified();
	public boolean isParticipated();
	public String getNotes();
	
	
}
