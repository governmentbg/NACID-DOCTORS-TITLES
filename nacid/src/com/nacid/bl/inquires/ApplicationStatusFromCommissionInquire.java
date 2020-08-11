package com.nacid.bl.inquires;

import com.nacid.web.model.common.ComboBoxWebModel;

public class ApplicationStatusFromCommissionInquire {

	public static final Integer JOIN_TYPE_NONE = null;
	public static final Integer JOIN_TYPE_AND = 1;
	public static final Integer JOIN_TYPE_NOT = 2;
	
	private ApplicationStatusAndLegalReasons status;
	private Integer joinType;
	private ApplicationStatusAndLegalReasons joinStatus;
	public ApplicationStatusFromCommissionInquire(ApplicationStatusAndLegalReasons status, Integer joinType, ApplicationStatusAndLegalReasons joinStatus) {
		this.status = status;
		this.joinType = joinType;
		this.joinStatus = joinStatus;
	}
	public ApplicationStatusAndLegalReasons getStatus() {
		return status;
	}
	public Integer getJoinType() {
		return joinType;
	}
	public ApplicationStatusAndLegalReasons getJoinStatus() {
		return joinStatus;
	}
	public static ComboBoxWebModel generataJoinTypeCombo(Integer selectedKey) {
		ComboBoxWebModel webmodel = new ComboBoxWebModel(selectedKey == null ? null : selectedKey.toString(), true);
		webmodel.addItem(JOIN_TYPE_AND + "", "И");
		webmodel.addItem(JOIN_TYPE_NOT + "", "НЕ");
		return webmodel;
	}
}
