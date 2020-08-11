package com.nacid.web.model.comission;

import com.nacid.bl.comision.CommissionParticipation;

public class CommissionParticipationWebModel{
	private ComissionMemberWebModel commissionMember;
	private boolean notified;
	private boolean participated;
	public CommissionParticipationWebModel(CommissionParticipation commissionParticipation) {
		this.commissionMember = new ComissionMemberWebModel(commissionParticipation.getCommissionMember());
		this.notified = commissionParticipation.isNotified();
		this.participated = commissionParticipation.isParticipated();
	}
	public ComissionMemberWebModel getCommissionMember() {
		return commissionMember;
	}
	public boolean isNotified() {
		return notified;
	}
	public boolean isParticipated() {
		return participated;
	}

}
