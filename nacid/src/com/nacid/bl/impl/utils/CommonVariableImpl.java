package com.nacid.bl.impl.utils;

import com.nacid.bl.utils.CommonVariable;
import com.nacid.data.utils.CommonVariableRecord;

public class CommonVariableImpl implements CommonVariable{
	private CommonVariableRecord record;
	public CommonVariableImpl(CommonVariableRecord record) {
		this.record = record;
	}
	public int getId() {
		return record.getId();
	}
	public String getVariableName() {
		return record.getVariableName();
	}
	public String getVariableValue() {
		return record.getVariableValue();
	}
	public String getDescription() {
		return  record.getDescription();
	}
	
}
