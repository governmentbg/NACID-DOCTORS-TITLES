package com.nacid.regprof.web.taglib.applications;

import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.impl.applications.RegprofApplicationHandler;
import com.nacid.web.WebKeys;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RegprofApplicationFormHeaderTag extends SimpleTagSupport {
    private int formid;
    private static Set<Integer> permanentActiveForms = new HashSet<Integer>();

    static {
        permanentActiveForms.add(RegprofApplicationHandler.FORM_ID_APPLICATION_DATA);
    }
    public void setFormid(int formid) {
        this.formid = formid;
    }

    public void doTag() throws JspException, IOException {
        String activeForm = (String) getJspContext().getAttribute(WebKeys.ACTIVE_FORM, PageContext.PAGE_SCOPE);
        Integer activeFormId = DataConverter.parseInteger(activeForm, null);
        if (activeFormId == null || activeFormId < 1 || activeFormId > 5) {
            activeFormId = RegprofApplicationHandler.FORM_ID_APPLICATION_DATA;
        }
        String operationType = (String) getJspContext().getAttribute(WebKeys.REGPROF_OPERATION_TYPE, PageContext.REQUEST_SCOPE);
        boolean isNew = false;
        if (operationType != null && operationType.equals("new")) {
            isNew = true;
        }
        
        //Ako formata e nova formId-to ne e izbroeno v id-tata na postoqnnite formi togava ne se vry6ta izhod!!!
        if (isNew && !permanentActiveForms.contains(formid)) {
            return;
        }
        //ako formId-to e za "danni ot zaqvitel", tozi tab se podava samo kogato v request-a ima atribut extregprofApplicatioAttachmentForReportWebModel.
        if (formid == RegprofApplicationHandler.FORM_ID_APPLIED_DATA && getJspContext().getAttribute("extregprofApplicatioForReportWebModel", PageContext.REQUEST_SCOPE) == null) {
            return;
        }
        //ako nqma posocheni otgovornici, se skriva taba za obuchenie/staj
        if (getJspContext().getAttribute("responsibleUsers", PageContext.REQUEST_SCOPE) == null) {
            getJspContext().setAttribute("hideTrainingCourse", "display:none;");
        }
        
        Integer applicationStatusId = (Integer) getJspContext().getAttribute("app_status", PageContext.REQUEST_SCOPE);

        if (applicationStatusId == null || ApplicationStatus.REGPROF_NON_FINALIZATION_STATUS_CODES.contains(applicationStatusId)) {
            getJspContext().setAttribute("hideFinalization", "display:none;");
        }

        if (formid == activeFormId) {
            getJspContext().setAttribute("formHeaderClass", "selected");
            getJspContext().setAttribute("formDivStyle", "display:block;");
        } else {
            getJspContext().setAttribute("formHeaderClass", "");
            getJspContext().setAttribute("formDivStyle", "display:none;");
        }
        getJspBody().invoke(null);
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}