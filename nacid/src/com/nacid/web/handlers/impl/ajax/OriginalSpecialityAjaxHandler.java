package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//RayaWritten--------------------------------------------------------------
public class OriginalSpecialityAjaxHandler extends NacidBaseRequestHandler{

    public OriginalSpecialityAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response){
        Integer specialityId = 0;
        String specName = request.getParameter("name");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        Boolean present = false;
        if(specialityId == 0){
            FlatNomenclature spec = originalSpecialityNameIsPresent(specName, nomenclaturesDataProvider);
            if(specName != null && specName != "" && spec == null){
                specName = DataConverter.removeRedundantWhitespaces(specName);
                specialityId = nomenclaturesDataProvider.saveFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY, specialityId, specName, null, null);
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

    private static FlatNomenclature originalSpecialityNameIsPresent(String specName, NomenclaturesDataProvider nomenclaturesDataProvider) {
        String newName = DataConverter.removeRedundantWhitespaces(specName);
        List<FlatNomenclature> specialities = nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY, specName, null, null);
        if (specialities != null) {
            for(FlatNomenclature s: specialities){
                if(s.getName().equalsIgnoreCase(newName)){
                    return s;
                }
            }
        }

        return null;
    }

}
//-------------------------------------------------------------------
