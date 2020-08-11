package com.nacid.web.taglib.nomenclatures;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.nomenclatures.ProfessionGroupWebModel;

public class ProfessionGroupTag extends SimpleTagSupport {

  public void doTag() throws JspException, IOException {
    ProfessionGroupWebModel webmodel = (ProfessionGroupWebModel) getJspContext().getAttribute( WebKeys.PROFESSION_GROUP, PageContext.REQUEST_SCOPE);
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
