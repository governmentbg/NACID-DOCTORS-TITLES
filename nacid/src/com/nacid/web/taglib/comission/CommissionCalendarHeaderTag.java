package com.nacid.web.taglib.comission;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.comission.CommissionCalendarHeaderWebModel;

public class CommissionCalendarHeaderTag extends SimpleTagSupport {
	private int formid;
	private static Set<Integer> permanentActiveFomrs = new HashSet<Integer>();
	static {
		permanentActiveFomrs.add(CommissionCalendarHeaderWebModel.ACTIVE_FORM_CALENDAR_EDIT);
	}
	public void setFormid(int formid) {
		this.formid = formid;
	}

	public void doTag() throws JspException, IOException {
		CommissionCalendarHeaderWebModel webmodel = (CommissionCalendarHeaderWebModel)getJspContext().getAttribute(WebKeys.COMMISSION_CALENDAR_HEADER_WEB_MODEL, PageContext.REQUEST_SCOPE);
		int activeFormId = webmodel == null ? CommissionCalendarHeaderWebModel.ACTIVE_FORM_CALENDAR_EDIT : webmodel.getActiveFormId();
		
		boolean isNew = webmodel == null || webmodel.getSessionId() == 0;
		if (isNew && !(permanentActiveFomrs.contains(formid))) {
			return;
		}
		getJspContext().setAttribute("action", webmodel == null ? "new" : webmodel.getAction());
		getJspContext().setAttribute("session_id", webmodel == null ? "0" : webmodel.getSessionId() + "");
		if (formid == activeFormId) {
			getJspContext().setAttribute("formHeaderClass", "selected");
			getJspContext().setAttribute("onclick", "return false;");
			
		} else {
			getJspContext().setAttribute("formHeaderClass", "");
			getJspContext().setAttribute("onclick", "return true;");
		}
		getJspBody().invoke(null);
	}

}
