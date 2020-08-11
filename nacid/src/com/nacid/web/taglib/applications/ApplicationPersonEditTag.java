package com.nacid.web.taglib.applications;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.applications.PersonDocumentWebModel;
import com.nacid.web.model.applications.PersonWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ApplicationPersonEditTag extends SimpleTagSupport {
  private String person;
  private PersonWebModel personWebModel;
  private PersonDocumentWebModel personDocumentWebModel;
  public void setPerson(String person) {
    this.person = person;
  }
  public void doTag() throws JspException, IOException {
	personWebModel = (PersonWebModel)getJspContext().getAttribute( 
			WebKeys.PERSON_WEB_MODEL, PageContext.REQUEST_SCOPE);
	if(personWebModel == null) {
		ApplicationEditTag parent = (ApplicationEditTag)findAncestorWithClass(this, ApplicationEditTag.class);
		if (parent == null) {
			return;
		}
		ApplicationWebModel webmodel = parent.getWebModel();
		if (webmodel != null) {
			if ("applicant".equals(person)) {
				personWebModel = webmodel.getApplicant();
                personDocumentWebModel = webmodel.getApplicantDocument();
			} else if ("representative".equals(person)) {
				personWebModel = webmodel.getRepresentative();
			} else if ("owner".equals(person)) {
                personWebModel = webmodel.getTrainingCourseWebModel().getOwner();
            }
		}
	}
    if (personWebModel != null) {
      getJspContext().setAttribute("person_record_id", personWebModel.getId());
      getJspContext().setAttribute("person_first_name", personWebModel.getFirstName());
      getJspContext().setAttribute("person_middle_name", personWebModel.getSurName());
      getJspContext().setAttribute("person_last_name", personWebModel.getLastName());
      getJspContext().setAttribute("person_civil_id", personWebModel.getCivilId());
      getJspContext().setAttribute("person_birth_city", personWebModel.getBirthCity());
      getJspContext().setAttribute("personBirthDate", personWebModel.getBirthDate());
    } else {
      getJspContext().setAttribute("person_record_id", "");
      getJspContext().setAttribute("person_first_name", "");
      getJspContext().setAttribute("person_middle_name", "");
      getJspContext().setAttribute("person_last_name", "");
      getJspContext().setAttribute("person_civil_id", "");
      getJspContext().setAttribute("person_birth_city", "");
      getJspContext().setAttribute("personBirthDate", "дд.мм.гггг");
    }

    if (personDocumentWebModel != null) {
	    getJspContext().setAttribute("person_document_id", personDocumentWebModel.getId());
	    getJspContext().setAttribute("person_document_person_id", personDocumentWebModel.getPersonId());
	    getJspContext().setAttribute("person_document_date_of_issue", personDocumentWebModel.getDateOfIssue());
	    getJspContext().setAttribute("person_document_issued_by", personDocumentWebModel.getIssuedBy());
	    getJspContext().setAttribute("person_document_number", personDocumentWebModel.getNumber());
    }

    getJspBody().invoke(null);
  }
  public PersonWebModel getPersonWebModel() {
    return personWebModel;
  }
}
