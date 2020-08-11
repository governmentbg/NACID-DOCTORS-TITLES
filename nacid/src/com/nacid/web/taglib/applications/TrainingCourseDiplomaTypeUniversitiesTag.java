package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.applications.UniversityWithFacultyWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;

import static com.nacid.web.taglib.applications.TrainingCourseUniversityTag.setUniversitiesData;

public class TrainingCourseDiplomaTypeUniversitiesTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		List<UniversityWithFacultyWebModel> universities = (List<UniversityWithFacultyWebModel>) getJspContext().getAttribute(WebKeys.UNIVERSITY_WITH_FACULTY_WEB_MODEL, PageContext.REQUEST_SCOPE);
		ComboBoxWebModel countryCombo = (ComboBoxWebModel) getJspContext().getAttribute(WebKeys.UNIVERSITY_COUNTRY, PageContext.REQUEST_SCOPE);

		setUniversitiesData(universities, countryCombo, getJspContext(), getJspBody());

	}


}
