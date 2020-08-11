package com.nacid.web.taglib.validation;


public class DateValidatorTag extends AbstractValidatorTag {
    private String format;
    private String input;
    private String required;
    private String beforeToday;
    private String emptyString;

    public String generateValidationFunction()  {
        
    	String val = "validateDate(" + getFormName() + "['" + input + "'],";
        val += "'" + format/*.toUpperCase()*/ + "',";
        val += emptyString == null ? "null," : "'" + emptyString + "',";
        val += required + ",";
        val += beforeToday + ")";
        return val;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public void setInput(String input) {
        this.input = input;
    }
    public void setBeforeToday(String beforeToday) {
        this.beforeToday = beforeToday;
    }
    public void setRequired(String required) {
        this.required = required == null ? "false" : required;
    }
	public void setEmptyString(String emptyString) {
		this.emptyString = "".equals(emptyString) ? null : emptyString;
	}
}
