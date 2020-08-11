package com.nacid.web.taglib.validation;

public class ComboBoxValidatorTag extends AbstractValidatorTag {

    private String input;
    private String required;
    

    public String generateValidationFunction() {
        String val = "validateComboBox(" + getFormName() + "['" + input + "'],";
        val += required + ",'" + errormessage +"')";
        return val;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setRequired(String required) {
        this.required = required == null ? "false" : required;
    }
}
