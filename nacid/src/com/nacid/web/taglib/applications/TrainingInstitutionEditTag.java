package com.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.TrainingInstitutionWebModel;

public class TrainingInstitutionEditTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        TrainingInstitutionWebModel webmodel = (TrainingInstitutionWebModel) getJspContext()
            .getAttribute( WebKeys.TRAINING_INSTITUTION_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
        
        getJspContext().setAttribute("id", webmodel.getId());
        getJspContext().setAttribute("name", webmodel.getName());
        getJspContext().setAttribute("pcode", webmodel.getPcode());
        getJspContext().setAttribute("city", webmodel.getCity());
        getJspContext().setAttribute("addrDetails", webmodel.getAddrDetails());
        getJspContext().setAttribute("phone", webmodel.getPhone());
        getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
        getJspContext().setAttribute("dateTo", webmodel.getDateTo());
        getJspContext().setAttribute("universityIds", webmodel.getUniversityIds());
        getJspContext().setAttribute("universities", webmodel.getUniversities());
        
        getJspBody().invoke(null);
    }
}
