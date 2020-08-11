package com.nacid.web.model.comission;

import java.util.List;

import com.nacid.bl.comision.CommissionParticipation;

public class CommissionParticipationBaseDataWebModel {
	private String commissionMemberIds = "";
	private String calendarId;
	private int commissionMemberIdsCount = 0;
	public CommissionParticipationBaseDataWebModel(int calendarId, List<CommissionParticipation> commissionParticipations) {
		if (commissionParticipations != null) {
			for (CommissionParticipation cpwm:commissionParticipations) {
				this.commissionMemberIds += cpwm.getCommissionMember().getId() + ";";
			}
			//this.commissionMemberIds = StringUtils.join(commissionParticipations, ";");
			//V javascripta razchitam 4e shte ima ";" sled poslednoto id (primerno "1;3;")
			//this.commissionMemberIds += ";";
			commissionMemberIdsCount = commissionParticipations.size();
		}
		this.calendarId = calendarId + "";

		
	}
	public String getCommissionMemberIds() {
		return commissionMemberIds;
	}
	public String getCalendarId() {
		return calendarId;
	}
	public int getCommissionMemberIdsCount() {
		return commissionMemberIdsCount;
	}
	
	
}
