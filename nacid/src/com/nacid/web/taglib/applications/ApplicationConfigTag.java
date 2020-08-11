package com.nacid.web.taglib.applications;

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
public class ApplicationConfigTag extends SimpleTagSupport {
    public void doTag() throws JspException, IOException {
        ApplicationWebModel webmodel = (ApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
        int applicationType = webmodel == null ? DataConverter.parseInteger(((PageContext) getJspContext()).getRequest().getParameter("application_type"), null) : webmodel.getApplicationType();
        getJspContext().setAttribute("application_type", applicationType, PageContext.REQUEST_SCOPE);

        if (applicationType == DOCTORATE_APPLICATION_TYPE) {
            getJspContext().setAttribute("universityLabel", "Висше училище или научна организация", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("theUniversityLabel", "Висшето училище или научната организация", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("trainingTabLabel", "Обучение/Дисертация", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("expertTabLabel", "Приключване", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("eduLevelLabel", "Степен", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("previousBoardDecisionsLabel", MessagesBundle.getMessagesBundle().getValue("previousBoardDecisionsDoctorate"), PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("hideOnlyForDoctorateStyle", "display:none", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("showOnlyForDoctorateStyle", "display:block", PageContext.REQUEST_SCOPE);
        } else {
            getJspContext().setAttribute("universityLabel", "Висше училище", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("theUniversityLabel", "Висшето училище", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("trainingTabLabel", "Обучение", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("expertTabLabel", "Експерт", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("eduLevelLabel", "ОКС", PageContext.REQUEST_SCOPE);
            getJspContext().setAttribute("previousBoardDecisionsLabel", MessagesBundle.getMessagesBundle().getValue("previousBoardDecisions"), PageContext.REQUEST_SCOPE);
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
