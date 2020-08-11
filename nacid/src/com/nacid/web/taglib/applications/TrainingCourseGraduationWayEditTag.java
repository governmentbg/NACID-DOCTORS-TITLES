package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.TrainingCourseWebModel;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class TrainingCourseGraduationWayEditTag extends SimpleTagSupport {
  private boolean input;
  private TrainingCourseWebModel trainingCourseWebModel;
  public void setInput(boolean input) {
    this.input = input;
  }
  public void doTag() throws JspException, IOException {
    TrainingCourseEditTag parent = (TrainingCourseEditTag) findAncestorWithClass(this, TrainingCourseEditTag.class);
    if (parent == null) {
      return;
    }
    trainingCourseWebModel = parent.getTrainingCourseWebModel();
    if (!input) {
      generateRadios();  
    } else {
      generateInput();
    }
    
  }
  public void generateRadios()throws JspException, IOException{
  //ApplicationPersonEditTag parent = (ApplicationPersonEditTag)getParent();
    List<FlatNomenclatureWebModel> graduationWayNomenclatures = (List<FlatNomenclatureWebModel>) getJspContext().getAttribute(WebKeys.FLAT_NOMENCLATURE_GRADUATION_WAY, PageContext.REQUEST_SCOPE);
    if (graduationWayNomenclatures == null || graduationWayNomenclatures.size() == 0) {
      return;
    }
    //Dobavqne na reda za "друго"
    graduationWayNomenclatures.add(new FlatNomenclatureWebModel(null, "друго", null, null, null, null, 0));
    for (int i = 0; i < graduationWayNomenclatures.size(); i++) {
      FlatNomenclatureWebModel fn = graduationWayNomenclatures.get(i);
      getJspContext().setAttribute("onclick", fn.getIntegerId() == null ? "$('graduation_way_other').toggle();" : "");
      getJspContext().setAttribute("label", fn.getName());
      getJspContext().setAttribute("value_id", fn.getIntegerId());
      getJspContext().setAttribute("id", fn.getIntegerId() == null ? "other" : fn.getId());
      
      
      boolean condition = (trainingCourseWebModel != null && trainingCourseWebModel.getTrainingCourseGraduationWays().containsKey(fn.getIntegerId()));
      getJspContext().setAttribute("checked", FormInputUtils.getCheckBoxCheckedText(condition));
      //System.out.println("id = " + fn.getIntegerId() + "   trainingCourseWebModel.getTrainingCourseGraduationWays().containsKey(fn.getId())? " + trainingCourseWebModel.getTrainingCourseGraduationWays().containsKey(fn.getId()) + "checked attribute:" + getJspContext().getAttribute("checked"));
      getJspBody().invoke(null);  
    }
  }
  public void generateInput() throws JspException, IOException{
    if (trainingCourseWebModel != null && trainingCourseWebModel.getTrainingCourseGraduationWays().containsKey(null)) {
      getJspContext().setAttribute("value", trainingCourseWebModel.getTrainingCourseGraduationWays().get(null));
      getJspContext().setAttribute("style", "display:block");
    } else {
      getJspContext().setAttribute("value", "");
      getJspContext().setAttribute("style", "display:none");
    }
    getJspBody().invoke(null);
  }
}
