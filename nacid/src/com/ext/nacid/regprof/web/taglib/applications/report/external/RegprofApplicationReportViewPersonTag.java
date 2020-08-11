package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.ext.nacid.web.model.applications.report.ExtPersonForReportWebModel;
//RayaWritten----------------------------------------------------------------
public class RegprofApplicationReportViewPersonTag extends SimpleTagSupport{
    public void doTag() throws JspException, IOException {
        
        RegprofApplicationReportViewTag parent = (RegprofApplicationReportViewTag)findAncestorWithClass(this, RegprofApplicationReportViewTag.class);
        if (parent == null) {
            return;
        }
        ExtRegprofApplicationForReportWebModel webmodel = parent.getWebModel();
        ExtPersonForReportWebModel personWebModel = (ExtPersonForReportWebModel) webmodel.getApplicant();
        if (personWebModel != null) {
            getJspContext().setAttribute("person_first_name", personWebModel.getFirstName());
            getJspContext().setAttribute("person_middle_name", personWebModel.getSurName());
            getJspContext().setAttribute("person_last_name", personWebModel.getLastName());
            getJspContext().setAttribute("person_civil_id", personWebModel.getCivilId());
            getJspContext().setAttribute("person_birth_city", personWebModel.getBirthCity());
            getJspContext().setAttribute("person_birth_country", personWebModel.getBirthCountry());
            getJspContext().setAttribute("person_citizenship", personWebModel.getCitizenship());
            getJspContext().setAttribute("person_civilid_type", personWebModel.getCivilIdType());
            getJspContext().setAttribute("personBirthDate", personWebModel.getBirthDate());
            
        }
        getJspBody().invoke(null);
        
    }
}
//--------------------------------------------------------------------------
