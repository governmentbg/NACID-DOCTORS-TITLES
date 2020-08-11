package com.ext.nacid.regprof.web.taglib.applications.report.internal;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import com.nacid.regprof.web.model.applications.report.internal.RegprofApplicationInternalForReportWebModel;
import com.nacid.web.model.applications.report.internal.PersonInternalForReportWebModel;

//RayaWRitten--------------------------------------------------------
public class RegprofApplicationReportViewPersonTag extends SimpleTagSupport{
        private String person;
        private PersonInternalForReportWebModel personWebModel;
        public void setPerson(String person) {
            this.person = person;
        }
        public void doTag() throws JspException, IOException {
            personWebModel = null;

            RegprofApplicationReportViewTag parent = (RegprofApplicationReportViewTag)findAncestorWithClass(this, RegprofApplicationReportViewTag.class);
            if (parent == null) {
                return;
            }
            RegprofApplicationInternalForReportWebModel webmodel = parent.getWebModel();
            if (webmodel != null) {
                if ("applicant".equals(person)) {
                    personWebModel = webmodel.getApplicant();
                } else if ("representative".equals(person)) {
                    personWebModel = webmodel.getRepresentative();
                }
            }
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
//-------------------------------------------------------------
