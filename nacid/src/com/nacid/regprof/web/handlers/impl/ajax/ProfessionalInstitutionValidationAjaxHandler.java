package com.nacid.regprof.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.ProfessionalInstitutionValidityImpl;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitutionValidity;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.model.common.SystemMessageWebModel;

//RayaWritten-----------------------------------------------------
public class ProfessionalInstitutionValidationAjaxHandler extends RegProfBaseRequestHandler {

    private static final String attributeName = "com.nacid.bl.impl.regprof.ProfessionalInstitutionValidityImpl";
    
    public ProfessionalInstitutionValidationAjaxHandler(
            ServletContext servletContext) {
        super(servletContext);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        int appId = DataConverter.parseInt(request.getParameter("appId"), -1);
        if(appId < 0){
            appId = DataConverter.parseInt(request.getParameter("id"), -1);
            if(appId < 0){
                throw new UnknownRecordException("Unknown regprof application ID:" + appId);
            }
        }
        ProfessionalInstitutionValidityImpl record = (ProfessionalInstitutionValidityImpl) request.getAttribute(attributeName);
        Integer userCreated = getLoggedUser(request, response).getUserId();
        record.setUserCreated(userCreated);      
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ProfessionalInstitutionDataProvider pidp = nacidDataProvider.getProfessionalInstitutionDataProvider();
        ProfessionalInstitutionValidity validity = pidp.saveProfessionalInstitutionValidity(record);    
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("validation_returned_id", validity.getId());
        } catch (JSONException e) {
            throw Utils.logException(e);
        }
        writeToResponse(response, jsonObj.toString());
    }
}
//------------------------------------------------------------
