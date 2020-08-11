package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.OrderCriteria;
import com.nacid.bl.nomenclatures.NationalQualificationsFramework;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by georgi.georgiev on 05.08.2015.
 */
public class NationalQualificationsFrameworkAjaxHandler extends NacidBaseRequestHandler {
    public NationalQualificationsFrameworkAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }


    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        Integer countryId = DataConverter.parseInteger(request.getParameter("countryId"), null);
        Integer defaultValue = DataConverter.parseInteger(request.getParameter("defaultValue"), null);
        List<NationalQualificationsFramework> noms = null;
        if (countryId != null) {
            noms = getNacidDataProvider().getNomenclaturesDataProvider().getNationalQualificationsFrameworks(null, countryId, OrderCriteria.createOrderCriteria(NomenclatureOrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        }
        ComboBoxUtils.generateNomenclaturesComboBox(defaultValue, noms, false, request, WebKeys.COMBO, true);
        request.setAttribute(WebKeys.NEXT_SCREEN, "flat_nomenclature_combo");
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        Integer countryId = DataConverter.parseInteger(request.getParameter("countryId"), null);
        String name = DataConverter.parseString(request.getParameter("name"), null);

        JSONObject jsonObj = new JSONObject();
        boolean hasError = false;
        String errorMessage = null;
        if (name == null || countryId == null) {
            if (name == null) {
                errorMessage = "няма въведено име";
                hasError = true;
            }
            if (countryId == null) {
                errorMessage = "няма въведена държава";
                hasError = true;
            }

        }


        if (!hasError) {
            NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
            List<NationalQualificationsFramework> nqfs  = nomenclaturesDataProvider.getNationalQualificationsFrameworks(null, countryId, null);
            if (nqfs != null) {
                for (NationalQualificationsFramework nqf : nqfs) {
                    if (nqf.getName().equalsIgnoreCase(name.trim())) {
                        errorMessage = "съществува НКР с това име за тази държава";
                        hasError = true;
                        break;
                    }
                }
            }

            if (!hasError) {
                int newId = nomenclaturesDataProvider.saveNationalQualificationsFramework(0, name, countryId, new Date(), null);
                jsonObj.put("id", newId);
            }
        }
        jsonObj.put("error", hasError);
        jsonObj.put("errorMessage", errorMessage);
        writeToResponse(response, jsonObj.toString());

    }
}
