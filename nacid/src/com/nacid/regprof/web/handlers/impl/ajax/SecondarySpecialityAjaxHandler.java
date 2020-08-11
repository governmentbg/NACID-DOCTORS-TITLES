package com.nacid.regprof.web.handlers.impl.ajax;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.handlers.impl.nomenclatures.FlatNomenclatureHandler;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SecondarySpecialityAjaxHandler extends RegProfBaseRequestHandler {

    public SecondarySpecialityAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        String name = DataConverter.parseString(request.getParameter("name"), "");
        name = name.trim();
        Integer profQualId = DataConverter.parseInteger(request.getParameter("prof_qual_id"), null);
        JSONObject jsonObj = new JSONObject();
        if (name.length() > 1 && profQualId != null && profQualId > 0) {
            NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();
            List<SecondarySpeciality> existingNomenclatures = nDP.getSecondarySpecialities(null, null, null, profQualId);

            boolean nomenclatureExists = false;
            if (existingNomenclatures != null && !existingNomenclatures.isEmpty()) {
                for (SecondarySpeciality nomenclature : existingNomenclatures) {
                    if (nomenclature.getName().equalsIgnoreCase(name)) {
                        nomenclatureExists = true;
                        break;
                    }
                }
            }
            int newId = nomenclatureExists ? 0 : nDP.saveSecondarySpeciality(0, name, profQualId, null, null, null, null);
            
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