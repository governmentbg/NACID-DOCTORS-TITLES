package com.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.UniversityValidityWebModel;

public class UniversityValidityEditTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        UniversityValidityWebModel webmodel = (UniversityValidityWebModel) getJspContext()
                .getAttribute(WebKeys.UNIVERSITY_VALIDITY_WEB_MODEL, PageContext.REQUEST_SCOPE);
        getJspContext().setAttribute("id", webmodel.getId());
        getJspContext().setAttribute("examinationDate", webmodel.getExaminationDate());
        getJspContext().setAttribute("university", webmodel.getUniversity());
        getJspContext().setAttribute("recognized", webmodel.getRecognized());
        getJspContext().setAttribute("communicated", webmodel.getCommunicated());
        getJspContext().setAttribute("trainingLocations", webmodel.getTrainingLocations());
        getJspContext().setAttribute("trainingForms", webmodel.getTrainingForms());
        getJspContext().setAttribute("hasJoinedDegrees", webmodel.getHasJoinedDegrees());
        getJspContext().setAttribute("trainingFormOtherText", webmodel.getTrainingFormOtherText());
        getJspContext().setAttribute("institution", webmodel.getInstitution());
        getJspContext().setAttribute("otherFormSelected", webmodel.getOtherFormSelected());
        getJspContext().setAttribute("universityId", webmodel.getUniversityId());
        getJspContext().setAttribute("notes", webmodel.getNotes());
        getJspContext().setAttribute("appId", webmodel.getAppId());
        getJspContext().setAttribute("appOper", webmodel.getAppOper());
        getJspContext().setAttribute("appGroup", webmodel.getGroupName());
        
        getJspBody().invoke(null);
    }
}
