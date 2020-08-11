package com.nacid.regprof.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDetails;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.RegulatedProfessionValidityImpl;
import com.nacid.bl.regprof.RegulatedProfessionValidity;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.exceptions.UnknownRecordException;

//RayaWritten---------------------------------------------------
public class RegulatedProfessionValidityAjaxHandler extends RegProfBaseRequestHandler {
    private static final String attributeName = "com.nacid.bl.impl.regprof.RegulatedProfessionValidityImpl";

    public RegulatedProfessionValidityAjaxHandler(ServletContext servletContext){
        super(servletContext);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response){
        int appId = DataConverter.parseInt(request.getParameter("appId"), -1);
        if(appId < 0){
            appId = DataConverter.parseInt(request.getParameter("id"), -1);
            if(appId < 0){
                throw new UnknownRecordException("Unknown regprof application ID:" + appId);
            }
        }
        RegprofApplication application = getNacidDataProvider().getRegprofApplicationDataProvider().getRegprofApplication(appId);
        RegprofApplicationDetails details = application.getApplicationDetails();
        RegulatedProfessionValidityImpl record = (RegulatedProfessionValidityImpl) request.getAttribute(attributeName);
        record.setCountryId(details.getApplicationCountryId());
        
        Integer userCreated = getLoggedUser(request, response).getUserId();
        record.setUserCreated(userCreated);      
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();
        RegulatedProfessionValidity validity = appDp.saveRegulatedProfessionValidity(record);    
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("validation_returned_id", validity.getId());
        } catch (JSONException e) {
            throw Utils.logException(e);
        }
        writeToResponse(response, jsonObj.toString());
    }

}
//----------------------------------------------------------------
