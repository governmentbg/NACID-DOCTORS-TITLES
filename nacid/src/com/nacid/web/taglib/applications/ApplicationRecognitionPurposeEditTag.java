package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class ApplicationRecognitionPurposeEditTag extends SimpleTagSupport {
  private boolean input;
  private ApplicationWebModel webmodel;
  public void setInput(boolean input) {
    this.input = input;
  }
  public void doTag() throws JspException, IOException {
    webmodel = (ApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
    if (webmodel == null) {
      return;
    }
    if (!input) {
      generateRadios();  
    } else {
      generateInput();
    }
    
  }
  public void generateRadios()throws JspException, IOException{
  //ApplicationPersonEditTag parent = (ApplicationPersonEditTag)getParent();
    List<FlatNomenclatureWebModel> recognitionPurposeNomenclatures = (List<FlatNomenclatureWebModel>) getJspContext().getAttribute(WebKeys.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE, PageContext.REQUEST_SCOPE);
    if (recognitionPurposeNomenclatures == null || recognitionPurposeNomenclatures.size() == 0) {
      recognitionPurposeNomenclatures = new ArrayList<FlatNomenclatureWebModel>();
    }
    //Dobavqne na reda za "друго"
    recognitionPurposeNomenclatures.add(new FlatNomenclatureWebModel(null, "друго", null, null, null, null, 0));
    for (int i = 0; i < recognitionPurposeNomenclatures.size(); i++) {
      FlatNomenclatureWebModel fn = recognitionPurposeNomenclatures.get(i);
      getJspContext().setAttribute("onclick", fn.getIntegerId() == null ? "$('recognition_purpose_other').toggle();" : "");
      getJspContext().setAttribute("label", fn.getName());
      getJspContext().setAttribute("value_id", fn.getIntegerId());
      getJspContext().setAttribute("id", fn.getIntegerId() == null ? "other" : fn.getId());
      
      boolean condition = (webmodel.getApplicationRecognitionPurposes().containsKey(fn.getIntegerId()));
      getJspContext().setAttribute("checked", FormInputUtils.getCheckBoxCheckedText(condition));
      getJspBody().invoke(null);  
    }
  }
  public void generateInput() throws JspException, IOException{
    if (webmodel.getApplicationRecognitionPurposes().containsKey(null)) {
      getJspContext().setAttribute("value", webmodel.getApplicationRecognitionPurposes().get(null));
      getJspContext().setAttribute("style", "display:block");
    } else {
      getJspContext().setAttribute("value", "");
      getJspContext().setAttribute("style", "display:none");
    }
    getJspBody().invoke(null);
  }
}
