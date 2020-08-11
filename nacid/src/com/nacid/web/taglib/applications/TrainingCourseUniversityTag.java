package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.TrainingCourseWebModel;
import com.nacid.web.model.applications.UniversityFacultyWebModel;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.applications.UniversityWithFacultyWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;

public class TrainingCourseUniversityTag extends SimpleTagSupport {
	private TrainingCourseWebModel trainingCourseWebModel;
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void doTag() throws JspException, IOException {
		TrainingCourseEditTag parent = (TrainingCourseEditTag) findAncestorWithClass(this, TrainingCourseEditTag.class);
		if (parent == null) {
			return;
		}
		trainingCourseWebModel = parent.getTrainingCourseWebModel();
		ComboBoxWebModel countryCombo = (ComboBoxWebModel) getJspContext().getAttribute(WebKeys.UNIVERSITY_COUNTRY, PageContext.REQUEST_SCOPE);

		String defaultCountryId = "-";// NomenclaturesDataProvider.COUNTRY_ID_BULGARIA
										// + "";

		if (type.equals("prevDipl")) {
			UniversityWithFacultyWebModel university = trainingCourseWebModel.getPrevDiplUniversityWithFaculty();
			countryCombo.setSelectedKey(university == null || university.getUniversity().getCountryId() == null ? defaultCountryId : university.getUniversity().getCountryId());
			setUniversityData(university, getJspContext(), getJspBody());
		} else if ("base".equals(type)) {
			UniversityWithFacultyWebModel university = trainingCourseWebModel.getBaseUniversityWithFaculty();
			countryCombo.setSelectedKey(university == null || university.getUniversity().getCountryId() == null ? defaultCountryId : university.getUniversity().getCountryId());
			setUniversityData(university, getJspContext(), getJspBody());
		} else if ("jointempty".equals(type)) {
			countryCombo.setSelectedKey(defaultCountryId);
			setUniversityData(null, getJspContext(), getJspBody());
		} else if ("joint".equals(type)) { //all - znachi obedinenie na base + joint!
			List<UniversityWithFacultyWebModel> universities = trainingCourseWebModel.getJointUniversityWithFaculties();
			setUniversitiesData(universities, countryCombo, getJspContext(), getJspBody());
		} else if ("all".equals(type)) {
			List<UniversityWithFacultyWebModel> universities = trainingCourseWebModel.getAllUniversityWithFaculties();
			setUniversitiesData(universities, countryCombo, getJspContext(), getJspBody());
		}
	}
	public static void setUniversitiesData(List<UniversityWithFacultyWebModel> universities, ComboBoxWebModel countryCombo, JspContext context, JspFragment body) throws IOException, JspException {
		if (universities != null) {
			for (int i = 0; i < universities.size(); i++) {
				UniversityWithFacultyWebModel u = universities.get(i);
				countryCombo.setSelectedKey(u.getUniversity().getCountryId());
				context.setAttribute("id", i);
				setUniversityData(u, context, body);
			}
		}
	}


	public static void setUniversityData(UniversityWithFacultyWebModel webmodel, JspContext context, JspFragment body) throws JspException, IOException {
		UniversityWebModel uni = webmodel == null ? null : webmodel.getUniversity();
		UniversityFacultyWebModel fac = webmodel == null ? null : webmodel.getFaculty();
		context.setAttribute("university_id", uni == null ? "" : uni.getId());
		context.setAttribute("university_name_bg", uni == null ? "" : uni.getBgName());
		context.setAttribute("university_original_name", uni == null ? "" : uni.getOrgName());
		context.setAttribute("university_city", uni == null ? "" : uni.getCity());
		context.setAttribute("university_address", uni == null ? "" : uni.getAddrDetails());
		context.setAttribute("university_url_diploma_register", uni == null ? "" : uni.getUrlDiplomaRegister());
		context.setAttribute("university_url_diploma_register_link", uni == null ? "" : uni.getUrlDiplomaRegisterLink());
		context.setAttribute("university_faculty_id", fac == null ? "" : fac.getId());
		context.setAttribute("university_faculty_name", fac == null ? "" : fac.getName());
		context.setAttribute("university_generic_name", uni == null ? "" : uni.getGenericName());
		context.setAttribute("university_generic_name_id", uni == null ? "" : uni.getGenericNameId());

		body.invoke(null);

	}
}
