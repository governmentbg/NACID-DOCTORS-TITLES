package com.nacid.web.handlers.impl.ajax;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
//RayaWritten--------------------------------------------------------------
public class SpecialityAjaxHandler extends NacidBaseRequestHandler{

    public SpecialityAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response){
        Integer specialityId = DataConverter.parseInteger(request.getParameter("specialityId"), 0);
        String specName = request.getParameter("name");
        Integer professionGroupId = DataConverter.parseInteger(request.getParameter("group"), null);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        Boolean present = false;
        if(specialityId == 0){
            Speciality spec = specialityNameIsPresent(specName, nomenclaturesDataProvider);
            if(specName != null && specName != "" && spec == null){
                specName = DataConverter.removeRedundantWhitespaces(specName);
                specialityId = nomenclaturesDataProvider.saveSpeciality(specialityId, specName, professionGroupId, null, null);

            } else if(specName != null && specName != "" && spec != null){
                specialityId = spec.getId();
                specName = spec.getName();
                present = true;
            }
        } else {
            present = true;
        }
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("specialityId", specialityId);
            jsonObj.put("specialityName", specName);
            jsonObj.put("present", present);
        } catch (JSONException e) {
            throw Utils.logException(e);
        }
        writeToResponse(response, jsonObj.toString());
    }

    private static Speciality specialityNameIsPresent(String specName, NomenclaturesDataProvider nomenclaturesDataProvider) {             
        String newName = DataConverter.removeRedundantWhitespaces(specName);
        List<Speciality> specialities = nomenclaturesDataProvider.getSpecialities(null, null, null);
        for(Speciality s: specialities){
            if(s.getName().equalsIgnoreCase(newName)){
                return s;
            }
        }
        return null;
    }

}
//-------------------------------------------------------------------
