package com.ext.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.handlers.impl.applications.ExtApplicationsHandler;
import com.ext.nacid.web.model.applications.ExtApplicationWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;

public class ExtApplicationFormHeaderTag extends SimpleTagSupport {
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
        
        ExtApplicationWebModel webmodel = (ExtApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
        int activeFormId = webmodel == null ? ApplicationsHandler.FORM_ID_APPLICATION_DATA : webmodel.getActiveFormId();
        
        if (webmodel != null ) {
        	//Ako tab-a e report, i ima zadaden internalApplication, to se vizualizirat samo dvata taba FORM_ID_INTERNAL_APPLICATION_REPORT, FORM_ID_EXTERNAL_APPLICATION_REPORT
        	if (webmodel.hasInternalApplication()) {
        		activeFormId = ExtApplicationsHandler.FORM_ID_EXTERNAL_APPLICATION_REPORT;
        		if (!reportForms.contains(formid)) {
            		return;
            	}	
        	} else { //inache dvata taba ne se vizualizirat!
        		if (reportForms.contains(formid)) {
                	return;
                }
        	}
        	
        } 
        
        boolean isNew = webmodel == null ? true : webmodel.isNew();
		if (isNew && !permanentActiveForms.contains(formid)) {
			return;
		}
		
		if (formid == ExtApplicationsHandler.FORM_ID_APPLYING && (webmodel != null && webmodel.isApplied() && getJspContext().getAttribute("applyingStatusMessage", PageContext.REQUEST_SCOPE) == null)) {
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
