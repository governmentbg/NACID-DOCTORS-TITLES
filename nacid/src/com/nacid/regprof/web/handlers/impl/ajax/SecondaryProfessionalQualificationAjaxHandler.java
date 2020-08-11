package com.nacid.regprof.web.handlers.impl.ajax;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.handlers.impl.nomenclatures.FlatNomenclatureHandler;

public class SecondaryProfessionalQualificationAjaxHandler extends RegProfBaseRequestHandler {

    public SecondaryProfessionalQualificationAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        String name = DataConverter.parseString(request.getParameter("name"), "");
        name = name.trim();
        JSONObject jsonObj = new JSONObject();
        if (name.length() > 1) {
            NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();
            List<SecondaryProfessionalQualification> existingNomenclatures = nDP.getSecondaryProfessionalQualifications(null, null, null);

            boolean nomenclatureExists = false;
            if (existingNomenclatures != null && !existingNomenclatures.isEmpty()) {
                for (SecondaryProfessionalQualification nomenclature : existingNomenclatures) {
                    if (nomenclature.getName().equalsIgnoreCase(name)) {
                        nomenclatureExists = true;
                        break;
                    }
                }
            }
            int newId = nomenclatureExists ? 0 : nDP.saveSecondaryProfessionalQualification(0, name, null, null, null, null);
            
            try {
                if (newId > 0) {
                    FlatNomenclatureHandler.refreshCachedNomenclatures(request);
                    jsonObj.put("id", newId);
                }
                else {
                    jsonObj.put("id", 0);
                }
            } catch (JSONException e) {
                throw Utils.logException(e);
            }
            
        } else {
            try {
                jsonObj.put("id", -1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        writeToResponse(response, jsonObj.toString());
    }
}
