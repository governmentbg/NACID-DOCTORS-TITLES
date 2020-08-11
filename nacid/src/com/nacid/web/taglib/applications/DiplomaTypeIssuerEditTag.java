package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.model.applications.DiplomaTypeIssuerWebModel;
import com.nacid.web.model.applications.DiplomaTypeWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;

public class DiplomaTypeIssuerEditTag extends SimpleTagSupport {
	private DiplomaTypeWebModel webmodel;
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void doTag() throws JspException, IOException {
		
		ComboBoxWebModel countryCombo = (ComboBoxWebModel) getJspContext().getAttribute("countryCombo", PageContext.REQUEST_SCOPE);
		String defaultCountryId = null;//NomenclaturesDataProvider.COUNTRY_ID_BULGARIA + "";
		if ("empty".equals(type)) {
			countryCombo.setSelectedKey(defaultCountryId);
			getJspContext().setAttribute("university_number", "");
			setDiplomaTypeIssuerData(new ComboBoxWebModel(null, true), new ComboBoxWebModel(null, true), getJspContext(), getJspBody());
		} else if ("other".equals(type)) {
			DiplomaTypeEditTag parent = (DiplomaTypeEditTag) findAncestorWithClass(this, DiplomaTypeEditTag.class);
			if (parent == null) {
				return;
			}
			webmodel = parent.getWebmodel();
			List<DiplomaTypeIssuerWebModel> diplomaTypeIssuers = webmodel.getDiplomaTypeIssuers();
			if (diplomaTypeIssuers != null) {
				for (int i = 0; i < diplomaTypeIssuers.size(); i++) {
					DiplomaTypeIssuerWebModel m = diplomaTypeIssuers.get(i);
					countryCombo.setSelectedKey(m.getCountryId() == null ? null : m.getCountryId() + "");
					getJspContext().setAttribute("university_number", i);
					setDiplomaTypeIssuerData(m.getUniversityCombo(), m.getFacultyCombo(), getJspContext(), getJspBody());
				}
			}
		}
	}
	private static void setDiplomaTypeIssuerData(ComboBoxWebModel uniWebModel, ComboBoxWebModel facultyWebModel, JspContext context, JspFragment body) throws JspException, IOException {
		context.setAttribute("universityCombo", uniWebModel, PageContext.REQUEST_SCOPE);
		context.setAttribute("universityFacultyCombo", facultyWebModel, PageContext.REQUEST_SCOPE);
		body.invoke(null);
	}
}
