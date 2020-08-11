package com.nacid.web.taglib.comission;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.comission.CommissionCalendarWebModel;

public class CommissionCalendarEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		CommissionCalendarWebModel webmodel = (CommissionCalendarWebModel) getJspContext()
			.getAttribute( WebKeys.COMMISSION_CALENDAR_WEB_MODEL, PageContext.REQUEST_SCOPE);
	    if (webmodel != null) {
	    	getJspContext().setAttribute("id", webmodel.getId());
	    	getJspContext().setAttribute("dateTime", webmodel.getDateTime());
	    	getJspContext().setAttribute("notes", webmodel.getNotes());
	    	//getJspContext().setAttribute("sessionNumber", webmodel.getSessionNumber());
		} else {
			getJspContext().setAttribute("id", "");
			getJspContext().setAttribute("dateTime", "дд.мм.гггг чч:мм");
			getJspContext().setAttribute("notes", "");
			//getJspContext().setAttribute("sessionNumber", "");
		}
		getJspBody().invoke(null);
	}
}
