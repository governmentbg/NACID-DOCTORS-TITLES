package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.PersonWebModel;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;

public class ApplicationCivilIdRadioTag extends SimpleTagSupport {
  
  public void doTag() throws JspException, IOException {
    ApplicationPersonEditTag parent = (ApplicationPersonEditTag)getParent();
    //System.out.println("Inside ApplicationCivilIdRadioTag... parent=" + parent);
    if (parent == null) {
      return;
    }
    PersonWebModel personWebModel = parent.getPersonWebModel();
    List<FlatNomenclatureWebModel> civilIdTypes = (List<FlatNomenclatureWebModel>) getJspContext().getAttribute(WebKeys.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, PageContext.REQUEST_SCOPE);
    //System.out.println("civilIdTypes=" + civilIdTypes);
    if (civilIdTypes == null || civilIdTypes.size() == 0) {
      return;
    }
    for (int i = 0; i < civilIdTypes.size(); i++) {
      FlatNomenclatureWebModel fn = civilIdTypes.get(i);
      getJspContext().setAttribute("value_id", i + "");
      getJspContext().setAttribute("label", fn.getName());
      getJspContext().setAttribute("radio_id", fn.getId());
      String checked = "";
      
      boolean condition = (i == 0 && personWebModel == null);
      condition = condition || (i==0 && personWebModel!= null && personWebModel.getCivilIdType().equals(""));
      condition = condition || (personWebModel!= null && personWebModel.getCivilIdType().equals(fn.getId()));
      
      //if ((i == 0 && personWebModel == null) || (personWebModel!= null && personWebModel.getCivilIdType().equals(fn.getId()))) {
      if (condition) {
        checked = "checked=\"checked\"";
      }
      getJspContext().setAttribute("checked", checked);
      getJspBody().invoke(null);  
    }
  }
}
