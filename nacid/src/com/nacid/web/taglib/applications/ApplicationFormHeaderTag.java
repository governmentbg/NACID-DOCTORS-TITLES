


package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.applications.TrainingCourseWebModel;

public class ApplicationFormHeaderTag extends SimpleTagSupport {
  private int formid;
  private static Set<Integer> permanentActiveForms = new HashSet<Integer>();
  
  static {
    permanentActiveForms.add(ApplicationsHandler.FORM_ID_APPLICATION_DATA);
  }
  public void setFormid(int formid) {
    this.formid = formid;
  }

  public void doTag() throws JspException, IOException {
    ApplicationWebModel webmodel = (ApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
    int activeFormId = webmodel == null ? ApplicationsHandler.FORM_ID_APPLICATION_DATA : webmodel.getActiveFormId();
   
    boolean isNew = webmodel == null ? true : webmodel.isNewRecord();
    //Ako formata e nova formId-to ne e izbroeno v id-tata na postoqnnite formi togava ne se vry6ta izhod!!!
    if (isNew && !permanentActiveForms.contains(formid)) {
      return;
    }
    
    //Ako nqma vyveden base university, табовете "СТАТУС" и "Експерт" се неактивни!!!
    if (!isNew && webmodel.isMigrated() && (formid == ApplicationsHandler.FORM_ID_STATUS_DATA || formid == ApplicationsHandler.FORM_ID_EXPERT_DATA)) {
    	//Ako zaqvlenieto e migrirano, togava tabovete status i expert se pokazvat nezavisimo ot tova dali ima vyvedeno traningCourse/universityWM/diplomaTypeWM
    } else if (formid == ApplicationsHandler.FORM_ID_STATUS_DATA || formid == ApplicationsHandler.FORM_ID_EXPERT_DATA) {
        
        if(webmodel == null) {
            return;
        }
        TrainingCourseWebModel trainingCourseWM = webmodel.getTrainingCourseWebModel();
        if(trainingCourseWM == null) {
            return;
        }
        
        //UniversityWebModel universityWM = trainingCourseWM.getBaseUniversity();
        //DiplomaTypeWebModel diplomaTypeWM = trainingCourseWM.getDiplomaTypeWebModel();

        //ako statusa e nqkoi ot tezi RUDI_APPLCATION_STATUSES_FOR_CHANGE_TO_FOR_EXAMINATION, ne trqbva da se pokazvat poslednite 2 taba!
        if (ApplicationStatus.RUDI_APPLCATION_STATUSES_FOR_CHANGE_TO_FOR_EXAMINATION.contains(webmodel.getIntAppStatusId())/*universityWM == null || diplomaTypeWM == null
                //|| isEmpty(trainingCourseWM.getDiplomaNumber())
                //|| DataConverter.parseDate(trainingCourseWM.getDiplomaDate()) == null
                || (trainingCourseWM.getTrainingLocations() == null || isEmpty(trainingCourseWM.getTrainingLocations().get(0).getTrainingLocationTrainingCity())) 
                || DataConverter.parseYear(trainingCourseWM.getTrainingStart()) == null
                || DataConverter.parseYear(trainingCourseWM.getTrainingEnd()) == null
                //|| trainingCourseWM.getTrainingCourseGraduationWays() == null
                //|| trainingCourseWM.getTrainingCourseGraduationWays().isEmpty()
                //|| isEmpty(trainingCourseWM.getSchoolCity())
                //|| isEmpty(trainingCourseWM.getSchoolName())
                //|| trainingCourseWM.getSchoolCountryId() == null
              */  ) {
            return;
        }
    }
    //taba E_APPLYING se pokazva samo ako se editva vyvedeno zaqvlenie, koeto e vyvedeno ot potrebitel
    if (formid == ApplicationsHandler.FORM_ID_E_APPLYING && (isNew || !webmodel.isElectronicallyApplied())) {
    	return;
    }
    if (formid == activeFormId) {
      getJspContext().setAttribute("formHeaderClass", "selected");
      getJspContext().setAttribute("formDivStyle", "display:block;");
    } else {
      getJspContext().setAttribute("formHeaderClass", "");
      getJspContext().setAttribute("formDivStyle", "display:none;");
    }
    getJspBody().invoke(null);
  }
  
  private boolean isEmpty(String str) {
      return str == null || str.length() == 0;
  }
}
