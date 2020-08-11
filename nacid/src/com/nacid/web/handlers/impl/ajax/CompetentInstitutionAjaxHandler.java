package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.applications.CompetentInstitutionHandler;
import com.nacid.web.handlers.impl.applications.UniversityValidityHandler;
import com.nacid.web.model.common.ComboBoxWebModel;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by georgi.georgiev on 13.08.2015.
 */
public class CompetentInstitutionAjaxHandler  extends NacidBaseRequestHandler {
    public CompetentInstitutionAjaxHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public CompetentInstitutionAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {

        Integer countryId = DataConverter.parseInteger(request.getParameter("countryId"), null);
        ComboBoxWebModel wm = UniversityValidityHandler.generateInstitutionsCombo(getNacidDataProvider(), countryId);
        String elementId = request.getParameter("elementId");
        String elementName = request.getParameter("elementName");


        request.setAttribute(WebKeys.COMBO, wm);
        request.setAttribute("comboName", elementName);
        request.setAttribute("comboId", elementId);
        request.setAttribute("clazz", "brd w500");

        request.setAttribute(WebKeys.NEXT_SCREEN, "ajax_combo");






    }

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        CompetentInstitution competentInstitution = CompetentInstitutionHandler.getCompetentInstitution(request, nacidDataProvider);
        JSONObject jsonObject = toJsonObject(competentInstitution);
        writeToResponse(response, jsonObject.toString());
    }


    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        CompetentInstitution competentInstitution = CompetentInstitutionHandler.saveCompetentInstitution(request, getNacidDataProvider());
        JSONObject jsonObject = toJsonObject(competentInstitution);
        writeToResponse(response, jsonObject.toString());
    }

    private static JSONObject toJsonObject(CompetentInstitution competentInstitution) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("institution_id", competentInstitution.getId());
        jsonObject.put("institution_country_id", competentInstitution.getCountryId());
        jsonObject.put("institution_country_name", competentInstitution.getCountry() == null ? ""  : competentInstitution.getCountry().getName());
        jsonObject.put("institution_name", DataConverter.parseString(competentInstitution.getName(), ""));
        jsonObject.put("institution_original_name", DataConverter.parseString(competentInstitution.getOriginalName(), ""));
        jsonObject.put("institution_address_details", DataConverter.parseString(competentInstitution.getAddressDetails(), ""));
        jsonObject.put("institution_email", DataConverter.parseString(competentInstitution.getEmail(), ""));
        jsonObject.put("institution_fax", DataConverter.parseString(competentInstitution.getFax(), ""));
        jsonObject.put("institution_phone", DataConverter.parseString(competentInstitution.getPhone(), ""));
        jsonObject.put("institution_url", DataConverter.parseString(competentInstitution.getUrl(), ""));
        jsonObject.put("institution_date_from", DataConverter.formatDate(competentInstitution.getDateFrom(), ""));
        jsonObject.put("institution_date_to", DataConverter.formatDate(competentInstitution.getDateTo(), ""));
        return jsonObject;

    }

}
