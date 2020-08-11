package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class FlatNomenclatureAjaxHandler extends RegProfBaseRequestHandler {

    public FlatNomenclatureAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {

        Integer type = DataConverter.parseInteger(request.getParameter("type"), null);
        NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();
        boolean addEmpty = DataConverter.parseBoolean(request.getParameter("addEmpty"));
        Integer defaultValue = DataConverter.parseInteger(request.getParameter("defaultValue"), null);
        ComboBoxUtils.generateNomenclaturesComboBox(defaultValue, type, nDP, false, request, WebKeys.COMBO, OrderCriteria.createOrderCriteria(NomenclatureOrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), addEmpty);
        request.setAttribute(WebKeys.NEXT_SCREEN, "flat_nomenclature_combo");

    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        Integer type = DataConverter.parseInteger(request.getParameter("type"), -1);
        if (type != null && type > 0) {
            String name = DataConverter.parseString(request.getParameter("name"), "");
            name = name.trim();
            if (name.length() > 1) {
                NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();
                List<FlatNomenclature> existingNomenclatures = nDP.getFlatNomenclatures(type, null, null);
                boolean nomenclatureExists = false;
                if (existingNomenclatures != null && !existingNomenclatures.isEmpty()) {
                    for (FlatNomenclature nomenclature : existingNomenclatures) {
                        if (nomenclature.getName().equalsIgnoreCase(name)) {
                            nomenclatureExists = true;
                            break;
                        }
                    }
                }
                int newId = nomenclatureExists ? 0 : nDP.saveFlatNomenclature(type, 0, name, null, null);
                JSONObject jsonObj = new JSONObject();
                try {
                    if (newId > 0) {
                        jsonObj.put("id", newId);
                    }
                    else {
                        jsonObj.put("id", 0);
                    }
                } catch (JSONException e) {
                    throw Utils.logException(e);
                }
                writeToResponse(response, jsonObj.toString());
            }
        }
    }
}
