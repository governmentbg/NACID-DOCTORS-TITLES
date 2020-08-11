package com.nacid.web.taglib.nomenclatures;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.nomenclatures.CountryWebModel;

public class CountryTag extends SimpleTagSupport {

  public void doTag() throws JspException, IOException {
    CountryWebModel webmodel = (CountryWebModel) getJspContext().getAttribute( WebKeys.COUNTRY, PageContext.REQUEST_SCOPE);
    if (webmodel != null) {
      getJspContext().setAttribute("id", webmodel.getId());
      getJspContext().setAttribute("name", webmodel.getName());
      getJspContext().setAttribute("iso3166Code", webmodel.getIso3166Code());
      getJspContext().setAttribute("officialName", webmodel.getOfficialName());
      getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
      getJspContext().setAttribute("dateTo", webmodel.getDateTo());
    } else {
      getJspContext().setAttribute("id", "");
      getJspContext().setAttribute("name", "");
      getJspContext().setAttribute("iso3166Code", "");
      getJspContext().setAttribute("officialName", "");
      getJspContext().setAttribute("dateFrom", DataConverter.formatDate(Utils.getToday()));
      getJspContext().setAttribute("dateTo", "дд.мм.гггг");
    }
    getJspBody().invoke(null);
  }
}
