package com.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.CompetentInstitutionWebModel;

public class CompetentInstitutionEditTag extends SimpleTagSupport {

  @Override
  public void doTag() throws JspException, IOException {
    CompetentInstitutionWebModel webmodel = (CompetentInstitutionWebModel) getJspContext().getAttribute(WebKeys.COMPETENT_INSTITUTION_WEB_MODEL,
        PageContext.REQUEST_SCOPE);
    Boolean operationView = (Boolean) getJspContext().getAttribute( WebKeys.OPERATION_VIEW, PageContext.REQUEST_SCOPE);
	if (operationView == null) {
		operationView = false;
	}
    getJspContext().setAttribute("id", webmodel == null ? "0" : webmodel.getId());
    getJspContext().setAttribute("name", webmodel == null ? "" : webmodel.getName());
    getJspContext().setAttribute("original_name", webmodel == null ? "" : webmodel.getOriginalName());
    getJspContext().setAttribute("address_details", webmodel == null ? "" : webmodel.getAddressDetails());
    getJspContext().setAttribute("phone", webmodel == null ? "" : webmodel.getPhone());
    getJspContext().setAttribute("fax", webmodel == null ? "" : webmodel.getFax());
    getJspContext().setAttribute("email", webmodel == null ? "" : webmodel.getEmail());
    getJspContext().setAttribute("url", webmodel == null ? "" : webmodel.getUrl());
    if (operationView && !StringUtils.isEmpty(webmodel.getUrl())) {
		getJspContext().setAttribute("urlLink", "<a href=\"" + webmodel.getUrl() + "\" target=\"_blank\">Отвори страницата</a>");
	}
    getJspContext().setAttribute("dateFrom", webmodel == null ? DataConverter.formatDate(Utils.getToday()) : webmodel.getDateFrom());
    getJspContext().setAttribute("dateTo", webmodel == null ? "дд.мм.гггг" : webmodel.getDateTo());

    getJspBody().invoke(null);
  }
}
