package com.nacid.web.taglib.nomenclatures;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.nomenclatures.SpecialityWebModel;

public class SpecialityTag extends SimpleTagSupport {

  public void doTag() throws JspException, IOException {
    SpecialityWebModel webmodel = (SpecialityWebModel) getJspContext().getAttribute( WebKeys.SPECIALITY, PageContext.REQUEST_SCOPE);
    if (webmodel != null) {
      getJspContext().setAttribute("id", webmodel.getId());
      getJspContext().setAttribute("name", webmodel.getName());
      getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
      getJspContext().setAttribute("dateTo", webmodel.getDateTo());
    } else {
      getJspContext().setAttribute("id", "");
      getJspContext().setAttribute("name", "");
      getJspContext().setAttribute("dateFrom", DataConverter.formatDate(Utils.getToday()));
      getJspContext().setAttribute("dateTo", "дд.мм.гггг");
    }
    getJspBody().invoke(null);
  }
}
