package com.ext.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.model.applications.ExtTrainingCourseWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class ExtTrainingCourseTrainingFormEditTag extends SimpleTagSupport {
    private boolean input;
    private ExtTrainingCourseWebModel trainingCourseWebModel;

    public void setInput(boolean input) {
        this.input = input;
    }

    public void doTag() throws JspException, IOException {
        
        trainingCourseWebModel = (ExtTrainingCourseWebModel) getJspContext().getAttribute(WebKeys.EXT_TRAINING_COURSE_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (!input) {
            generateRadios();
        } else {
            generateInput();
        }

    }

    public void generateRadios() throws JspException, IOException {
        // ApplicationPersonEditTag parent =
        // (ApplicationPersonEditTag)getParent();
        List<FlatNomenclatureWebModel> traningFormNomenclatures = (List<FlatNomenclatureWebModel>) getJspContext().getAttribute(
                WebKeys.FLAT_NOMENCLATURE_TRAINING_FORM, PageContext.REQUEST_SCOPE);
        if (traningFormNomenclatures == null || traningFormNomenclatures.size() == 0) {
            return;
        }
        // Dobavqne na reda za "друго"
        traningFormNomenclatures.add(new FlatNomenclatureWebModel(null, "друго", null, null, null, null, 0));
        for (int i = 0; i < traningFormNomenclatures.size(); i++) {
            FlatNomenclatureWebModel fn = traningFormNomenclatures.get(i);
            getJspContext().setAttribute("onclick",
                    fn.getIntegerId() == null ? "$('training_form_other').show();" : "$('training_form_other').hide();");
            getJspContext().setAttribute("label", fn.getName());
            getJspContext().setAttribute("value_id", fn.getId());
            getJspContext().setAttribute("id", fn.getIntegerId() == null ? "other" : fn.getId());

            boolean condition = (trainingCourseWebModel == null || trainingCourseWebModel.getTrainingCourseTrainingForms().size() == 0) && i == 0;
            condition = condition
                    || (trainingCourseWebModel != null && trainingCourseWebModel.getTrainingCourseTrainingForms().containsKey(fn.getIntegerId()));
            getJspContext().setAttribute("checked", FormInputUtils.getRadioButtionCheckedText(condition));
            getJspBody().invoke(null);
        }
    }

    public void generateInput() throws JspException, IOException {
        if (trainingCourseWebModel != null && trainingCourseWebModel.getTrainingCourseTrainingForms().containsKey(null)) {
            getJspContext().setAttribute("value", trainingCourseWebModel.getTrainingCourseTrainingForms().get(null));
            getJspContext().setAttribute("style", "display:block;");
        } else {
            getJspContext().setAttribute("value", "");
            getJspContext().setAttribute("style", "display:none;");
        }
        getJspBody().invoke(null);
    }
}
