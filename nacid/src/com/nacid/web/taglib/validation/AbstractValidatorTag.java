package com.nacid.web.taglib.validation;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public abstract class AbstractValidatorTag extends SimpleTagSupport {
	protected String formName;
	protected String errormessage = "";
	
	@Override
	public void doTag() throws JspException, IOException {
		FormTag form = (FormTag) findAncestorWithClass(this, FormTag.class);
		
		if(form == null)
			throw new JspException("Can't find parent form tag");
		
		
		this.formName = form.getFormName();
		String val = generateValidationFunction();
		form.addInputValidation(val);
	}
	
	public abstract String generateValidationFunction();
	public String getFormName() {
	    return formName;
	}

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage == null ? "" : errormessage;
    }
	
}
