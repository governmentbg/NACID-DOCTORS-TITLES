package com.ext.nacid.web.taglib.applications;

import com.ext.nacid.web.model.applications.ExtApplicationWebModel;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import static com.nacid.bl.nomenclatures.ApplicationType.*;

/***
 * parametri, koito shte vajat za vsichki tabs!
 */
public class ExtApplicationConfigTag extends SimpleTagSupport {
    public void doTag() throws JspException, IOException {
        ExtApplicationWebModel webmodel = (ExtApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
        int applicationType = webmodel.getApplicationType();
        getJspContext().setAttribute("application_type",  applicationType, PageContext.REQUEST_SCOPE);

        if (applicationType == DOCTORATE_APPLICATION_TYPE) {
            getJspContext().setAttribute("universityLabel", "Висше училище или научна организация", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("theUniversityLabel", "Висшето училище или научната организация", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("trainingTabLabel", "Обучение/Дисертация", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("eduLevelLabel", "Степен", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("hideOnlyForDoctorateStyle", "display:none", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("showOnlyForDoctorateStyle", "display:block", PageContext.REQUEST_SCOPE);
        } else {
            getJspContext().setAttribute("universityLabel", "Висше училище", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("theUniversityLabel", "Висшето училище", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("trainingTabLabel", "Обучение", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("eduLevelLabel", "ОКС", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("hideOnlyForDoctorateStyle", "display:block", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("showOnlyForDoctorateStyle", "display:none", PageContext.REQUEST_SCOPE);

        }
        getJspContext().setAttribute("isDoctorateApplicationType", applicationType == DOCTORATE_APPLICATION_TYPE);
        getJspContext().setAttribute("isRudiApplicationType", applicationType == RUDI_APPLICATION_TYPE);
        getJspContext().setAttribute("isSARApplicationType", applicationType == STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE);
        if (getJspBody() != null) {
            getJspBody().invoke(null);
        }
    }
}
