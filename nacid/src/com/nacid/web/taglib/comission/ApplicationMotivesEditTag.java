package com.nacid.web.taglib.comission;

import com.nacid.web.WebKeys;
import com.nacid.web.model.comission.ApplicationMotivesWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ApplicationMotivesEditTag extends SimpleTagSupport {
    
    @Override
    public void doTag() throws JspException, IOException {
        ApplicationMotivesWebModel webModel = (ApplicationMotivesWebModel)getJspContext().getAttribute(WebKeys.APPLICATION_MOTIVES_WEB_MODEL, PageContext.REQUEST_SCOPE);
        
        getJspContext().setAttribute("calendarId", webModel.getCalendarId());
        getJspContext().setAttribute("motives", webModel.getApplicationMotives());
        getJspContext().setAttribute("applicationId", webModel.getApplicationId());
        
        getJspContext().setAttribute("recognSpecs", webModel.getRecognSpecs());
        getJspContext().setAttribute("recognSpecsCount", webModel.getRecognSpecs() == null ? 0 : webModel.getRecognSpecs().size());
        getJspContext().setAttribute("recognSpecIds", webModel.getRecognSpecIds());
        getJspContext().setAttribute("application_header", webModel.getApplicationHeader());
        
        getJspContext().setAttribute("recognizedQualification", webModel.getRecognizedQualification());
        getJspContext().setAttribute("recognizedQualificationId", webModel.getRecognizedQualificationId());
        getJspContext().setAttribute("applicantInfo", webModel.getApplicantInfo());

        getJspContext().setAttribute("applicationType", webModel.getApplicationType());
        
        getJspBody().invoke(null);
    }
}
