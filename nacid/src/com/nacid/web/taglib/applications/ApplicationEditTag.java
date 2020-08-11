package com.nacid.web.taglib.applications;

import com.nacid.web.ApplicantTypeHelper;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.applications.TrainingCourseWebModel;
import com.nacid.web.taglib.FormInputUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ApplicationEditTag extends SimpleTagSupport {
  ApplicationWebModel webmodel;

  @Override
  public void doTag() throws JspException, IOException {
    webmodel = (ApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
    if (webmodel != null) {
      getJspContext().setAttribute("application_id", webmodel.getId());
      getJspContext().setAttribute("email", webmodel.getEmail());
      getJspContext().setAttribute("official_email_communication", FormInputUtils.getCheckBoxCheckedText(webmodel.isOfficialEmailCommunication()));
      getJspContext().setAttribute("personal_data_usage", FormInputUtils.getCheckBoxCheckedText(webmodel.getPersonalDataUsage() == null ? false : webmodel.getPersonalDataUsage()));
      getJspContext().setAttribute("home_city", webmodel.getHomeCity());
      getJspContext().setAttribute("home_post_code", webmodel.getHomePostCode());
      getJspContext().setAttribute("home_address_details", webmodel.getHomeAddressDetails());
      getJspContext().setAttribute("bg_city", webmodel.getBgCity());
      getJspContext().setAttribute("bg_post_code", webmodel.getBgPostCode());
      getJspContext().setAttribute("bg_address_details", webmodel.getBgAddressDetails());
      getJspContext().setAttribute("representative_selected", FormInputUtils.getCheckBoxCheckedText(webmodel.showRepresentative()));
      getJspContext().setAttribute("represent_is_company", FormInputUtils.getCheckBoxCheckedText(webmodel.isRepresentativeIsCompany()));
      getJspContext().setAttribute("data_authentic", FormInputUtils.getCheckBoxCheckedText(webmodel.isDataAuthentic()));
      getJspContext().setAttribute("representative_authorized", FormInputUtils.getCheckBoxCheckedText(webmodel.isRepresentativeAuthorized()));
      
      getJspContext().setAttribute("diploma_names_selected", FormInputUtils.getCheckBoxCheckedText(webmodel.showDiplomaNames()));
       
      getJspContext().setAttribute("diploma_names_selected", FormInputUtils.getCheckBoxCheckedText(webmodel.showDiplomaNames()));
      getJspContext().setAttribute("bg_address_owner_selected",FormInputUtils.getCheckBoxCheckedText(webmodel.isBgAddressOwnerChecked()));
      
      getJspContext().setAttribute("bgPhone", stringToParameter(webmodel.getBgPhone()));
      getJspContext().setAttribute("homePhone", stringToParameter(webmodel.getHomePhone()));
      getJspContext().setAttribute("reprPhone", stringToParameter(webmodel.getReprPhone()));
      getJspContext().setAttribute("reprAddressDetails", stringToParameter(webmodel.getReprAddressDetails()));
      getJspContext().setAttribute("reprPcode", stringToParameter(webmodel.getReprPcode()));
      getJspContext().setAttribute("reprCity", stringToParameter(webmodel.getReprCity()));
      getJspContext().setAttribute("hasMoreApplicationsWithSameApplicant", webmodel.hasMoreApplicationsWithSameApplicant());
      getJspContext().setAttribute("notes", webmodel.getNotes());
        getJspContext().setAttribute("applicant_type", webmodel.getApplicantTypeId());
        getJspContext().setAttribute("outgoing_number", webmodel.getOutgoingNumber());
        getJspContext().setAttribute("internal_number", webmodel.getInternalNumber());

    } else {
      getJspContext().setAttribute("application_id", "");
      getJspContext().setAttribute("email", "");
      getJspContext().setAttribute("official_email_communication", FormInputUtils.getCheckBoxCheckedText(false));
      getJspContext().setAttribute("personal_data_usage", FormInputUtils.getCheckBoxCheckedText(true));
      
      getJspContext().setAttribute("home_city", "");
      getJspContext().setAttribute("home_post_code", "");
      getJspContext().setAttribute("home_address_details", "");
      // getJspContext().setAttribute("home_is_bg", webmodel.getHomeAddressDetails());
      getJspContext().setAttribute("bg_city", "");
      getJspContext().setAttribute("bg_post_code", "");
      getJspContext().setAttribute("bg_address_details", "");
      getJspContext().setAttribute("representative_selected", FormInputUtils.getCheckBoxCheckedText(false));
      getJspContext().setAttribute("represent_is_company", FormInputUtils.getCheckBoxCheckedText(false));
      
      getJspContext().setAttribute("diploma_names_selected", FormInputUtils.getCheckBoxCheckedText(false));
      getJspContext().setAttribute("bg_address_owner_selected", FormInputUtils.getCheckBoxCheckedText(false));
      getJspContext().setAttribute("data_authentic", FormInputUtils.getCheckBoxCheckedText(false));
      getJspContext().setAttribute("representative_authorized", FormInputUtils.getCheckBoxCheckedText(false));
      
      getJspContext().setAttribute("bgPhone", "");
      getJspContext().setAttribute("homePhone", "");
      getJspContext().setAttribute("reprPhone", "");
      getJspContext().setAttribute("reprAddressDetails", "");
      getJspContext().setAttribute("reprPcode", "");
      getJspContext().setAttribute("reprCity", "");
      getJspContext().setAttribute("hasMoreApplicationsWithSameApplicant", false);
      getJspContext().setAttribute("notes", "");
        getJspContext().setAttribute("applicant_type", ApplicantTypeHelper.APPLICANT_TYPE_PHYSICAL_PERSON);

    }
    getJspContext().setAttribute("application_header", webmodel == null || webmodel.isNewRecord() ? "Добавяне на ново заявление" : webmodel.getApplicationHeader());
    
    /**
     * adding data for diploma names.....
     */
    TrainingCourseWebModel trainingCourseWebModel = webmodel == null ? null : webmodel.getTrainingCourseWebModel();
    if (trainingCourseWebModel != null) {
      getJspContext().setAttribute("diploma_firstName", trainingCourseWebModel.getFirstName());
      getJspContext().setAttribute("diploma_middleName", trainingCourseWebModel.getSurName());
      getJspContext().setAttribute("diploma_lastName", trainingCourseWebModel.getLastName());
    } else {
      getJspContext().setAttribute("diploma_firstName", "");
      getJspContext().setAttribute("diploma_middleName", "");
      getJspContext().setAttribute("diploma_lastName", "");
    }
    getJspBody().invoke(null);
  }

  public ApplicationWebModel getWebModel() {
    return webmodel;
  }
  
  private static String stringToParameter(String s) {
      return s == null ? "" : s;
  }
}
