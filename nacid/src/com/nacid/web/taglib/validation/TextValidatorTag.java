package com.nacid.web.taglib.validation;


public class TextValidatorTag extends AbstractValidatorTag {
	
	private String input;
	private String minLength;
	private String maxLength;
	private String regex;
	private String required;
	
	@Override
	public String generateValidationFunction() {
		String ret = "validateText(" + getFormName() + "['" + input + "']," + required + ",";
		String min = minLength != null ? minLength : "-1";
		String max = maxLength != null ? maxLength : "-1";
		ret += min + "," + max + ",";
		ret += ((regex == null ? "null" : regex) + ",");
		ret += ("'" + errormessage + "'");
		ret += ")";
		return ret;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setRequired(String required) {
		this.required = required;
	}

}
