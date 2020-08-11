package com.ext.nacid.regprof.web.taglib.applications;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.regprof.web.handlers.impl.applications.ExtRegprofApplicationsHandler;
import com.ext.nacid.regprof.web.model.applications.EPayWebModel;
import com.ext.nacid.web.handlers.impl.applications.ExtApplicationsHandler;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;

public class ExtRegprofApplicationFormHeaderTag extends SimpleTagSupport {
    private int formid;
    private static Set<Integer> permanentActiveForms = new HashSet<Integer>();

    static {
        permanentActiveForms.add(ExtApplicationsHandler.FORM_ID_APPLICATION_DATA);
    }

    //Tova sa tabovete, koito se vijdat samo ako zaqvlenieto e prehvyrleno vyv vytre6nata baza!  
    private static Set<Integer> reportForms = new HashSet<Integer>();
    static {
    	reportForms.add(ExtApplicationsHandler.FORM_ID_INTERNAL_APPLICATION_REPORT);
    	reportForms.add(ExtApplicationsHandler.FORM_ID_EXTERNAL_APPLICATION_REPORT);
    }
    
    public void setFormid(int formid) {
        this.formid = formid;
    }

    @Override
    public void doTag() throws JspException, IOException {

        ExtRegprofApplicationImpl app = (ExtRegprofApplicationImpl) getJspContext().getAttribute(ExtRegprofApplicationImpl.class.getName(), PageContext.REQUEST_SCOPE);
        EPayWebModel epaywm = (EPayWebModel) getJspContext().getAttribute("epaymodel", PageContext.REQUEST_SCOPE);
        boolean isNew = app == null || app.getApplicationDetails() == null || app.getApplicationDetails().getId() == null;
        int activeFormId = isNew ? ApplicationsHandler.FORM_ID_APPLICATION_DATA : (Integer) getJspContext().getAttribute(WebKeys.ACTIVE_FORM, PageContext.REQUEST_SCOPE);
        
        
        if (app != null && app.getApplicationDetails() != null ) {
        	//Ako tab-a e report, i ima zadaden internalRegprofApplication, to se vizualizirat samo dvata taba FORM_ID_INTERNAL_APPLICATION_REPORT, FORM_ID_EXTERNAL_APPLICATION_REPORT
        	if (app.getApplicationDetails().getRegprofApplicationId() != null) {
        		activeFormId = ExtRegprofApplicationsHandler.FORM_ID_EXTERNAL_APPLICATION_REPORT;
        		if (!reportForms.contains(formid)) {
            		return;
            	}	
        	} else { //inache dvata taba ne se vizualizirat!
        		if (reportForms.contains(formid)) {
                	return;
                }
        	}
        	
        } 

        if (isNew && !permanentActiveForms.contains(formid)) {
			return;
		}
		
        if (formid == ExtRegprofApplicationsHandler.FORM_ID_APPLYING && !isNew && (app.getApplicationDetails().getStatus() != ExtRegprofApplicationImpl.STATUS_EDITABLE && getJspContext().getAttribute("applyingStatusMessage", PageContext.REQUEST_SCOPE) == null)) {
            return;
        }
        if (formid == ExtRegprofApplicationsHandler.FORM_ID_EPAY_PAYMENT && epaywm == null) {
            return;
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
}
