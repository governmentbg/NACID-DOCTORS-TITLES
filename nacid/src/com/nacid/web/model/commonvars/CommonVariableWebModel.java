package com.nacid.web.model.commonvars;

import com.nacid.bl.utils.CommonVariable;

public class CommonVariableWebModel {
	private String id;
	private String name;
	private String value;
	private String description;
	
	public CommonVariableWebModel(String id, String name, String value, String description) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.description = description;
	}
	public CommonVariableWebModel(CommonVariable variable) {
		this.id = variable.getId() + "";
		this.name = variable.getVariableName();
		this.value = variable.getVariableValue();
		this.description = variable.getDescription();
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public String getDescription() {
		return description;
	}
}
