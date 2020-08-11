package com.nacid.web.taglib.comission;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.comission.CommissionParticipationWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class CommissionParticipationListTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		List<CommissionParticipationWebModel> webmodel = (List<CommissionParticipationWebModel>) getJspContext().getAttribute( WebKeys.COMMISSION_PARTICIPATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
	    if (webmodel != null) {
	    	for (int i = 0; i < webmodel.size(); i ++) {
	    		CommissionParticipationWebModel w = webmodel.get(i);
	    		getJspContext().setAttribute("row_id", i+1);
	    		getJspContext().setAttribute("id", w.getCommissionMember().getId());
		    	getJspContext().setAttribute("fname", w.getCommissionMember().getFname());
		    	getJspContext().setAttribute("lname", w.getCommissionMember().getLname());
		    	getJspContext().setAttribute("pgname", w.getCommissionMember().getProfGroupName());
		    	getJspContext().setAttribute("posname", w.getCommissionMember().getCommissionPositionName());
		    	getJspContext().setAttribute("phone", w.getCommissionMember().getPhone());
		    	//getJspContext().setAttribute("email", w.getCommissionMember().getEmail());
		    	//getJspContext().setAttribute("gsm", w.getCommissionMember().getGsm());
		    	getJspContext().setAttribute("notified_checked", FormInputUtils.getCheckBoxCheckedText(w.isNotified()));
		    	getJspContext().setAttribute("participated_checked", FormInputUtils.getCheckBoxCheckedText(w.isParticipated()));
		    	getJspBody().invoke(null);
	    	}
	    	
		} 
		
	}
}
