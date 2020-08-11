package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationUniversityValidityWebModel;
import com.nacid.web.model.applications.UniversityWebModel;

public class ApplicationUniversityValidityTag extends SimpleTagSupport {
	public void doTag() throws JspException, IOException {
		List<ApplicationUniversityValidityWebModel> universityValidities = (List<ApplicationUniversityValidityWebModel>) getJspContext().getAttribute(WebKeys.APPLICATION_UNIVERSITY_VALIDITY, PageContext.REQUEST_SCOPE);
		if (universityValidities != null) {
			int i = 0;
			for (ApplicationUniversityValidityWebModel universityValidity:universityValidities) {
				getJspContext().setAttribute("universityValidityWebModel", universityValidity.getTable(), PageContext.REQUEST_SCOPE);
				getJspContext().setAttribute("notes", universityValidity.getUniversityExamination() == null ? "" : universityValidity.getUniversityExamination().getNotes());
				
				UniversityWebModel university = universityValidity.getUniversity();
				getJspContext().setAttribute("university_id", university.getId());
				getJspContext().setAttribute("university_name_bg", university.getBgName());
				getJspContext().setAttribute("university_original_name", university.getOrgName());
				getJspContext().setAttribute("university_country", university.getCountry());
				getJspContext().setAttribute("university_city", university.getCity());
				
				
				getJspContext().setAttribute("row_id", i++);
				getJspBody().invoke(null);	
			}
				
			
			
		}
		
		
	}
	
}
