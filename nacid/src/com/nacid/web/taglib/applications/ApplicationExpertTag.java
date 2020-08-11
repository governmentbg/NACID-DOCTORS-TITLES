package com.nacid.web.taglib.applications;

import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationExpertWebModel;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.taglib.FormInputUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;

public class ApplicationExpertTag extends SimpleTagSupport {
    private  String type;
    
    public void setType(String type) {
        this.type = type;
    }

    public void doTag() throws JspException, IOException {
        ApplicationWebModel webmodel = (ApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel == null) {
            return;
        }
        ComboBoxWebModel applicationExpertsCombo ;
        ComboBoxWebModel eduLevelsCombo = (ComboBoxWebModel) getJspBody().getJspContext().getAttribute("eduLevelsCombo", PageContext.REQUEST_SCOPE);
        ComboBoxWebModel expertPositionsCombo = (ComboBoxWebModel) getJspBody().getJspContext().getAttribute("expertPositionsCombo", PageContext.REQUEST_SCOPE);

        if ("empty".equals(type)) {
            applicationExpertsCombo = (ComboBoxWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_EXPERTS_COMBO, PageContext.REQUEST_SCOPE);
            //Za generirane na prazniq appliction_expert, tozi kojto shte se dobavq s javascripta!
            if (applicationExpertsCombo != null) {
                applicationExpertsCombo.setSelectedKey(null);
            }

            //za generirane na prazniq applicationExpert, eduLevel-a se slaga null
            eduLevelsCombo.setSelectedKey(null);
            expertPositionsCombo.setSelectedKey(null);

            getJspBody().invoke(null);
            return;
        }
        
        List<ApplicationExpertWebModel> applicationExperts = webmodel.getApplicationExperts();
        if (applicationExperts != null) {
            int i = 0;
            for (ApplicationExpertWebModel ae:applicationExperts) {
                getJspContext().setAttribute("row_id", i++);
                getJspContext().setAttribute("application_expert_notes", ae.getNotes());
                getJspContext().setAttribute("application_expert_course_content", ae.getCourseContent());

                List<Speciality> specialities = ae.getExpertSpecialities();
                getJspContext().setAttribute("application_expert_specialities_count", specialities == null ? 0 : specialities.size());
                getJspContext().setAttribute("applicationExpertSpecialities", ae.getExpertSpecialities());
                getJspContext().setAttribute("application_expert_qualification", ae.getQualificationName());
                getJspContext().setAttribute("application_expert_qualification_id", ae.getQualificationId());
                getJspContext().setAttribute("application_expert_previous_board_decisions", ae.getPreviousBoardDecisions());
                getJspContext().setAttribute("application_expert_similar_bulgarian_programs", ae.getSimilarBulgarianPrograms());



                getJspContext().setAttribute("applExpProcessStatVal", FormInputUtils.getCheckBoxCheckedText(DataConverter.parseIntegerToBoolean(ae.getProcessStat())));
                getJspContext().setAttribute("applExpProcessStatTxt", 
                        ae.getProcessStat() != 0 ? "Експертът е приключил работата по заявлението" : "Експертът има още работа по заявлението");
                applicationExpertsCombo = ae.getCombo();
                if (applicationExpertsCombo != null) {
                    applicationExpertsCombo.setSelectedKey(ae.getExpertId() + "");
                }






                eduLevelsCombo.setSelectedKey(ae.getEduLevelId());
                expertPositionsCombo.setSelectedKey(ae.getExpertPositionId());



                getJspBody().getJspContext().setAttribute("aeCombo", applicationExpertsCombo, PageContext.REQUEST_SCOPE);
                getJspBody().getJspContext().setAttribute("ellCombo", eduLevelsCombo, PageContext.REQUEST_SCOPE);
                getJspBody().getJspContext().setAttribute("expertLegalReasonsCombo", ae.getLegalReasonsCombo(), PageContext.REQUEST_SCOPE);





                getJspBody().invoke(null);
            }
        }
    }

}
