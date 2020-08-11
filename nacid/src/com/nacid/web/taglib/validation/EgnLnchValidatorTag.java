package com.nacid.web.taglib.validation;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class EgnLnchValidatorTag extends AbstractValidatorTag {

	private String input;
	private String required;
	
	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		JspWriter w = getJspContext().getOut();
		
		String cbName = input + "IgnoreEGNValidation";
		
		w.println("<p style=\"display: none;\" class=\"ignoreError\" ><span class=\"fieldlabel2\">&nbsp;</span>");
		w.println("<input id=\""+cbName+"\" name=\""+cbName+"\" type=\"checkbox\" value=\"1\" /><label for=\"" + cbName + "\" class=\"ignoreError\">Искам да запазя въпреки грешното ЕГН</label></p>");
		w.println("<div class=\"clr\"><!--  --></div>");

		cbName = input + "IgnoreLNCHValidation";
		w.println("<p style=\"display: none;\" class=\"ignoreError\" ><span class=\"fieldlabel2\">&nbsp;</span>");
		w.println("<input id=\""+cbName+"\" name=\""+cbName+"\" type=\"checkbox\" value=\"1\" /><label for=\"" + cbName + "\" class=\"ignoreError\">Искам да запазя въпреки грешното ЛНЧ</label></p>");
		w.println("<div class=\"clr\"><!--  --></div>");
		
	}
	
	@Override
	public String generateValidationFunction() {
		String ret = "(validateEGN(" + getFormName() + "['" + input + "']," + required + ", " + getFormName() + "['"+input+"IgnoreEGNValidation']) && " + "validateLNCH(" + getFormName() + "['" + input + "']," + required + ", " + getFormName() + "['"+input+"IgnoreLNCHValidation']))";
		return ret;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setRequired(String required) {
		this.required = required;
	}

}
