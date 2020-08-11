package com.ext.nacid.web.taglib.applications;

import com.ext.nacid.web.model.ExtPersonWebModel;
import com.ext.nacid.web.model.applications.ExtApplicationWebModel;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.web.WebKeys;
import com.nacid.web.taglib.FormInputUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ExtApplicationEditTag extends SimpleTagSupport {
	ExtApplicationWebModel webmodel;

	@Override
	public void doTag() throws JspException, IOException {
		webmodel = (ExtApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
		String appicationHeader = "Добавяне на ново заявление";
		if (webmodel != null) {

			getJspContext().setAttribute("application_id", webmodel.getId());
			getJspContext().setAttribute("home_city", webmodel.getHomeCity());
			getJspContext().setAttribute("home_post_code", webmodel.getHomePostCode());
			getJspContext().setAttribute("home_address_details", webmodel.getHomeAddressDetails());
			getJspContext().setAttribute("bg_city", webmodel.getBgCity());
			getJspContext().setAttribute("bg_post_code", webmodel.getBgPostCode());
			getJspContext().setAttribute("bg_address_details", webmodel.getBgAddressDetails());

			getJspContext().setAttribute("diploma_names_selected", FormInputUtils.getCheckBoxCheckedText(webmodel.showDiplomaNames()));
			getJspContext().setAttribute("bulgaria_contact_show", webmodel.getHomeCountryId() == Country.COUNTRY_ID_BULGARIA ? " style= \"display:none\" " : " style=\"display:block\" ");
			getJspContext().setAttribute("diploma_names_div_show", !webmodel.showDiplomaNames() ? " style= \"display:none\" " : " style=\"display:block\" ");

			getJspContext().setAttribute("is_different_applicant_representative", webmodel.isDifferentApplicantRepresentative());
			getJspContext().setAttribute("different_applicant_representative", FormInputUtils.getCheckBoxCheckedText(webmodel.isDifferentApplicantRepresentative()));
			getJspContext().setAttribute("different_applicant_representative_div_show", !webmodel.isDifferentApplicantRepresentative() ? " style= \"display:none\" " : " style=\"display:block\" ");

			getJspContext().setAttribute("personal_data_usage", FormInputUtils.getCheckBoxCheckedText(webmodel.isPersonalDataUsage()));
			getJspContext().setAttribute("data_authentic", FormInputUtils.getCheckBoxCheckedText(webmodel.isDataAuthentic()));

			getJspContext().setAttribute("bgPhone", stringToParameter(webmodel.getBgPhone()));
			getJspContext().setAttribute("homePhone", stringToParameter(webmodel.getHomePhone()));
			getJspContext().setAttribute("status", webmodel.getStatus());


			ExtPersonWebModel representative = webmodel.getRepresentative();


			getJspContext().setAttribute("representative_first_name", StringUtils.isEmpty(representative.getFname()) ? "&nbsp;" : representative.getFname());
			getJspContext().setAttribute("representative_middle_name", StringUtils.isEmpty(representative.getSname()) ? "&nbsp;" : representative.getSname());
			getJspContext().setAttribute("representative_last_name", StringUtils.isEmpty(representative.getLname()) ? "&nbsp;" : representative.getLname());
			getJspContext().setAttribute("representative_full_name", representative.getFullName());


			getJspContext().setAttribute("representative_personal_id_type", representative.getPersonalIdType());
			getJspContext().setAttribute("representative_personal_id", representative.getPersonalId());
			getJspContext().setAttribute("edit_representative_personal_id", representative.isEditPersonlId());


			getJspContext().setAttribute("representative_email", representative.getEmail());
			getJspContext().setAttribute("representative_birth_date", representative.getBirthDate());
			getJspContext().setAttribute("representative_birth_city", representative.getBirthCity());



			ExtPersonWebModel applicant = webmodel.getApplicant();
			if (applicant != null && webmodel.isDifferentApplicantRepresentative()) {
				getJspContext().setAttribute("applicant_id", applicant.getId());

				getJspContext().setAttribute("applicant_first_name", StringUtils.isEmpty(applicant.getFname()) ? "&nbsp;" : applicant.getFname());
				getJspContext().setAttribute("applicant_second_name", StringUtils.isEmpty(applicant.getSname()) ? "&nbsp;" : applicant.getSname());
				getJspContext().setAttribute("applicant_last_name", StringUtils.isEmpty(applicant.getLname()) ? "&nbsp;" : applicant.getLname());
				getJspContext().setAttribute("applicant_full_name", applicant.getFullName());


				getJspContext().setAttribute("applicant_personal_id_type", applicant.getPersonalIdType());
				getJspContext().setAttribute("applicant_personal_id", applicant.getPersonalId());


				getJspContext().setAttribute("applicant_email", applicant.getEmail());
				getJspContext().setAttribute("applicant_birth_date", applicant.getBirthDate());
				getJspContext().setAttribute("applicant_birth_city", applicant.getBirthCity());
			}

			
			
			getJspContext().setAttribute("diploma_firstName", webmodel.getDiplFName());
			getJspContext().setAttribute("diploma_middleName", webmodel.getDiplSName());
			getJspContext().setAttribute("diploma_lastName", webmodel.getDiplLName());
			getJspContext().setAttribute("application_type", webmodel.getApplicationType());
			
			
			
		} else {
			throw new RuntimeException("ExtApplicationWebModel must be set to the request in order to view applications_edit");
		}
		getJspContext().setAttribute("application_header", appicationHeader);

		
		
		
		getJspBody().invoke(null);
	}

	public ExtApplicationWebModel getWebModel() {
		return webmodel;
	}

	private static String stringToParameter(String s) {
		return s == null ? "" : s;
	}
}
