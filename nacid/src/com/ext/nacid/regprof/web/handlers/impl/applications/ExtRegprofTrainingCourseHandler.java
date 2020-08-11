package com.ext.nacid.regprof.web.handlers.impl.applications;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.ext.nacid.regprof.web.model.applications.ExtRegprofTrainingCourseWebModel;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofTrainingCourseImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofTrainingCourse;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseRecord;
import com.nacid.regprof.web.handlers.impl.applications.RegprofTrainingCourseHandler;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.common.SystemMessageWebModel;

public class ExtRegprofTrainingCourseHandler extends NacidExtBaseRequestHandler{
    
    private static final String ATTRIBUTE_NAME = ExtRegprofTrainingCourseImpl.class.getName();
    
    public ExtRegprofTrainingCourseHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInt(request.getParameter("appId"), -1); // regprof application id
        if (applicationId <= 0) {
            applicationId = (Integer) request.getAttribute("id");
            if (applicationId == null || applicationId <= 0) {
                throw new UnknownRecordException("Unknown regprof application ID:" + applicationId);
            }
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider dp = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtRegprofTrainingCourse trainingCourse = dp.getTrainingCourse(applicationId);

        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        ExtRegprofTrainingCourseRecord details = trainingCourse == null ? null : trainingCourse.getDetails();
        if (details != null) {
            Integer educationTypeId = details.getEducationTypeId();
            Integer professionalQualificationId = details.getSecProfQualificationId();
            Integer highEducationLevelId = details.getHighEduLevelId();
            Integer documentType = details.getDocumentType();
            Integer sdkDocumentType = details.getSdkDocumentType();
            Integer secCaliberId = details.getSecCaliberId();
            //Integer profExperienceDocTypeId = trainingCourse.getExperienceRecord().getNomenclatureProfExperienceDocTypeId();

            Integer secSpecialityId = trainingCourse.getSpecialities() == null || trainingCourse.getSpecialities().size() == 0 ? null : trainingCourse.getSpecialities().get(0).getSecondarySpecialityId();
            RegprofTrainingCourseHandler.generateComboBoxes(request, nDP, educationTypeId, professionalQualificationId, secSpecialityId, highEducationLevelId, documentType, 
                    sdkDocumentType, secCaliberId/*, profExperienceDocTypeId*/);   
            ComboBoxUtils.generateNomenclaturesComboBox(null, nDP.getCountries(null, null), true, request, "countries", true);
            request.setAttribute("model", new ExtRegprofTrainingCourseWebModel(nacidDataProvider, trainingCourse));
        } else {
            RegprofTrainingCourseHandler.generateComboBoxes(request, nDP, null, null, null, null, null, null, null);    
        }

        //Integer secondarySpecialityId = null;

        request.setAttribute(WebKeys.REGPROF_OPERATION_TYPE, "edit");
        request.setAttribute(ATTRIBUTE_NAME, trainingCourse);
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
      
        Integer applicationId = DataConverter.parseInteger(request.getParameter("appId"), null);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider dp = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationDetailsImpl appDetails = dp.getApplicationDetails(applicationId);
        try {
            dp.getApplicationDetails(applicationId);
            ExtRegprofUserAccessUtils.checkApplicantActionAccess(ExtRegprofUserAccessUtils.USER_ACTION_CHANGE, getExtPerson(request, response), appDetails);
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        ExtRegprofTrainingCourseImpl tce = (ExtRegprofTrainingCourseImpl) request.getAttribute(ATTRIBUTE_NAME);
        if (tce.getDetails().getId().intValue() != appDetails.getTrainingCourseId().intValue()) {
            throw new RuntimeException("TrainingCourse's id is not equal to application's trainingCourse id!!!");
        }
        
        dp.saveExtRegprofTrainingCourse(tce);
        request.setAttribute("trainingCourseSystemMessage", SystemMessageWebModel.createDataInsertedWebMessage());
        ExtPerson person = getExtPerson(request, response);
        ExtRegprofApplicationImpl rec = dp.getExtRegprofApplication(applicationId);
        ExtRegprofApplicationsHandler.fillData(request, response, getNacidDataProvider(), person, rec, ExtRegprofApplicationsHandler.FORM_ID_TRAINING_DATA);
        
    }
}
