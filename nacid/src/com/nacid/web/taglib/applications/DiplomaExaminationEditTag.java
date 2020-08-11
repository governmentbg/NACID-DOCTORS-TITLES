package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.model.applications.*;
import com.nacid.web.taglib.FormInputUtils;

public class DiplomaExaminationEditTag extends SimpleTagSupport {
    private TrainingCourseWebModel trainingCourseWebModel;
    public void doTag() throws JspException, IOException {
        TrainingCourseEditTag parent = (TrainingCourseEditTag) findAncestorWithClass(this, TrainingCourseEditTag.class);
        trainingCourseWebModel = parent == null ? null : parent.getTrainingCourseWebModel();
        if (trainingCourseWebModel == null) {
            return;
        }
        DiplomaExaminationWebModel webmodel = trainingCourseWebModel.getDiplomaExaminationWebModel();
        DiplomaTypeWebModel diplomaType = trainingCourseWebModel.getDiplomaTypeWebModel();


        if (webmodel != null /*&& !webmodel.isEmpty()*/) {
            getJspContext().setAttribute("diploma_type_title", diplomaType == null ? "-" : diplomaType.getTitle());
            getJspContext().setAttribute("diploma_examination_id", webmodel.getId());
            getJspContext().setAttribute("diploma_examination_date", webmodel.getExaminationDate());
            getJspContext().setAttribute("diploma_examination_notes", webmodel.getNotes());
            getJspContext().setAttribute("diploma_examination_recognized", FormInputUtils.getCheckBoxCheckedText(webmodel.isRecognized()));
            getJspContext().setAttribute("diploma_examination_universityCommunicated", FormInputUtils.getCheckBoxCheckedText(webmodel.isUniversityCommunicated()));
            getJspContext().setAttribute("diploma_examination_foundInRegister", FormInputUtils.getCheckBoxCheckedText(webmodel.isFoundInRegister()));
            getJspContext().setAttribute("diploma_examination_institutionCommunicated", FormInputUtils.getCheckBoxCheckedText(webmodel.isInstitutionCommunicated()));
            getJspContext().setAttribute("diploma_examination_competent_institution_id", webmodel.getCompetentInstitutionId());
            getJspContext().setAttribute("diploma_examination_competent_institution_style", webmodel.getCompetentInstitutionId() == null ? "display:none;" : "display:block;");

            List<UniversityWithFacultyWebModel> universitiesWebModel = trainingCourseWebModel.getAllUniversityWithFaculties();
            if (universitiesWebModel != null) {
            	List<String> universityNames = new ArrayList<String>();
            	for (UniversityWithFacultyWebModel uf:universitiesWebModel) {
                    UniversityWebModel u = uf.getUniversity();
            	    String uniName = u.getBgName();
                    if (u.getUrlDiplomaRegister() != null && !u.getUrlDiplomaRegister().equals("")) {
                        uniName = "<a target=\"_blank\" href=\"" + u.getUrlDiplomaRegister() + "\">" + uniName + "</a>";
                    }
                    universityNames.add(uniName);
            	}
            	getJspContext().setAttribute("diploma_examination_universities", universityNames);
            }




            //getJspContext().setAttribute("diploma_examination_base_university_register_url", universityWebModel.getUrlDiplomaRegister());
            getJspBody().invoke(null);
        }


    }

}
