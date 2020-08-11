package com.nacid.web.taglib.comission;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.comission.ComissionMemberWebModel;

public class ComissionMemberEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		ComissionMemberWebModel webmodel = (ComissionMemberWebModel) getJspContext()
			.getAttribute( WebKeys.COMISSION_MEMBER_WEB_MODEL, PageContext.REQUEST_SCOPE);
	    if (webmodel != null) {
	    	getJspContext().setAttribute("id", webmodel.getId());
	    	getJspContext().setAttribute("fname", webmodel.getFname());
	    	getJspContext().setAttribute("sname", webmodel.getSname());
	    	getJspContext().setAttribute("lname", webmodel.getLname());
	    	getJspContext().setAttribute("degree", webmodel.getDegree());
	    	getJspContext().setAttribute("institution", webmodel.getInstitution());
	    	getJspContext().setAttribute("division", webmodel.getDivision());
	    	getJspContext().setAttribute("title", webmodel.getTitle());
	    	getJspContext().setAttribute("egn", webmodel.getEgn());
	    	getJspContext().setAttribute("homeCity", webmodel.getHomeCity());
	    	getJspContext().setAttribute("homePcode", webmodel.getHomePcode());
	    	getJspContext().setAttribute("homeAddress", webmodel.getHomeAddress());
	    	getJspContext().setAttribute("phone", webmodel.getPhone());
	    	getJspContext().setAttribute("email", webmodel.getEmail());
	    	getJspContext().setAttribute("gsm", webmodel.getGsm());
	    	getJspContext().setAttribute("iban", webmodel.getIban());
	    	getJspContext().setAttribute("bic", webmodel.getBic());
	    	getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
	    	getJspContext().setAttribute("dateTo", webmodel.getDateTo());
	    	getJspContext().setAttribute("userName", webmodel.getUserName());
		} else {
			getJspContext().setAttribute("id", "");
			getJspContext().setAttribute("fname", "");
	    	getJspContext().setAttribute("sname", "");
	    	getJspContext().setAttribute("lname", "");
	    	getJspContext().setAttribute("degree", "");
	    	getJspContext().setAttribute("institution", "");
	    	getJspContext().setAttribute("division", "");
	    	getJspContext().setAttribute("title", "");
	    	getJspContext().setAttribute("egn", "");
	    	getJspContext().setAttribute("homeCity", "");
	    	getJspContext().setAttribute("homePcode", "");
	    	getJspContext().setAttribute("homeAddress", "");
	    	getJspContext().setAttribute("phone", "");
	    	getJspContext().setAttribute("email", "");
	    	getJspContext().setAttribute("gsm", "");
	    	getJspContext().setAttribute("iban", "");
	    	getJspContext().setAttribute("bic", "");
	    	getJspContext().setAttribute("dateFrom", "дд.мм.гггг");
	    	getJspContext().setAttribute("dateTo", "дд.мм.гггг");
	    	getJspContext().setAttribute("userName", "");
		}
		getJspBody().invoke(null);
	}
}
