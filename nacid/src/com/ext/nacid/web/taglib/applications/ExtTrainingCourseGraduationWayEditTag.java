package com.ext.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.model.applications.ExtTrainingCourseWebModel;
import com.nacid.bl.nomenclatures.GraduationWay;
import com.nacid.web.WebKeys;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class ExtTrainingCourseGraduationWayEditTag extends SimpleTagSupport {
    private boolean input;
    private ExtTrainingCourseWebModel trainingCourseWebModel;

    public void setInput(boolean input) {
        this.input = input;
    }

    public void doTag() throws JspException, IOException {

        trainingCourseWebModel = (ExtTrainingCourseWebModel) getJspContext().getAttribute(WebKeys.EXT_TRAINING_COURSE_WEB_MODEL,
                PageContext.REQUEST_SCOPE);

        if (!input) {
            generateRadios();
        } else {
            generateInput();
        }

    }

    public void generateRadios() throws JspException, IOException {
        // ApplicationPersonEditTag parent =
        // (ApplicationPersonEditTag)getParent();
        List<FlatNomenclatureWebModel> graduationWayNomenclatures = (List<FlatNomenclatureWebModel>) getJspContext().getAttribute(
                WebKeys.FLAT_NOMENCLATURE_GRADUATION_WAY, PageContext.REQUEST_SCOPE);
        if (graduationWayNomenclatures == null || graduationWayNomenclatures.size() == 0) {
            return;
        }
        // Dobavqne na reda za "друго"
        graduationWayNomenclatures.add(new FlatNomenclatureWebModel(null, "друго", null, null, null, null, 0));
        for (int i = 0; i < graduationWayNomenclatures.size(); i++) {
            FlatNomenclatureWebModel fn = graduationWayNomenclatures.get(i);
            String func = "";
            boolean isDissertationGraduationWay = Objects.equals(fn.getIntegerId(), GraduationWay.GRADUATION_WAY_DISSERTATION);
            func += isDissertationGraduationWay ? "toggleDissertationDiv(this.checked);" : "";
            boolean isOtherGraduationWay = fn.getIntegerId() == null;
            func += isOtherGraduationWay ?  "$('graduation_way_other').toggle();" : "";
            getJspContext().setAttribute("onclick", func);
            getJspContext().setAttribute("label", fn.getName());
            getJspContext().setAttribute("value_id", fn.getIntegerId());
            getJspContext().setAttribute("id", fn.getIntegerId() == null ? "other" : fn.getId());

            boolean condition = (trainingCourseWebModel != null && trainingCourseWebModel.getTrainingCourseGraduationWays().containsKey(
                    fn.getIntegerId()));
            getJspContext().setAttribute("checked", FormInputUtils.getCheckBoxCheckedText(condition));
            // System.out.println("id = " + fn.getIntegerId() +
            // "   trainingCourseWebModel.getTrainingCourseGraduationWays().containsKey(fn.getId())? "
            // +
            // trainingCourseWebModel.getTrainingCourseGraduationWays().containsKey(fn.getId())
            // + "checked attribute:" +
            // getJspContext().getAttribute("checked"));
            getJspBody().invoke(null);
        }
    }

    public void generateInput() throws JspException, IOException {
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
