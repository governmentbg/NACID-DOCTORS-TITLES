package com.nacid.web.taglib.applications;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.CompanyWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class CompanyEditTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        CompanyWebModel webmodel = (CompanyWebModel) getJspContext()
            .getAttribute( WebKeys.COMPANY_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
        
        getJspContext().setAttribute("id", webmodel.getId());
        getJspContext().setAttribute("companyName", webmodel.getName());
        getJspContext().setAttribute("companyEik", webmodel.getEik());
        getJspContext().setAttribute("pcode", webmodel.getPcode());
        getJspContext().setAttribute("city", webmodel.getCity());
        getJspContext().setAttribute("addressDetails", webmodel.getAddressDetails());
        getJspContext().setAttribute("phone", webmodel.getPhone());
        getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
        getJspContext().setAttribute("dateTo", webmodel.getDateTo());
        
        getJspBody().invoke(null);
    }
}
