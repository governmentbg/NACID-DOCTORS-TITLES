package com.nacid.bl.impl.comission;

import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.CommissionParticipation;
import com.nacid.data.comission.CommissionParticipationRecord;

public class CommissionParticipationImpl implements CommissionParticipation {
	private ComissionMember commissionMember;
	private CommissionParticipationRecord record;
	
	public CommissionParticipationImpl(ComissionMember commissionMember, CommissionParticipationRecord record) {
		this.commissionMember = commissionMember;
		this.record = record;
	}

	public int getCalendarId() {
		return record.getSessionId();
	}

	public ComissionMember getCommissionMember() {
		return commissionMember;
	}

	public int getCommissionMemberId() {
		return record.getExpertId();
	}

	public String getNotes() {
		return record.getNotes();
	}

	public boolean isNotified() {
		return record.getIntNotified() > 0 ? true : false;
	}

	public boolean isParticipated() {
		return record.getIntParticipated() > 0 ? true : false;
	}

}
