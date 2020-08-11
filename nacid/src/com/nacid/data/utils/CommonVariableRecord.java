package com.nacid.data.utils;


public class CommonVariableRecord {
	private int id;
	private String variableName;
	private String variableValue;
	private String description;
	public CommonVariableRecord() {
	}
	public CommonVariableRecord(int id, String variableName, String variableValue, String description) {
		this.id = id;
		this.variableName = variableName;
		this.variableValue = variableValue;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getVariableValue() {
		return variableValue;
	}
	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
