package com.nacid.web.model.comission;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.comision.ComissionMember;

public class CommissionMembersInformWebModel {
	private List<ComissionMemberWebModel> commissionMembers ;
	private int calendarId;
	public CommissionMembersInformWebModel(int calendarId) {
		this.calendarId = calendarId;
	}
	public void addCommissionMemberWebModel(ComissionMember commissionMember) {
		if (commissionMembers == null) {
			commissionMembers = new ArrayList<ComissionMemberWebModel>();
		}
		commissionMembers.add(new ComissionMemberWebModel(commissionMember));
	}
	public List<ComissionMemberWebModel> getCommissionMembers() {
		return commissionMembers;
	}
	public int getCalendarId() {
		return calendarId;
	}
}
