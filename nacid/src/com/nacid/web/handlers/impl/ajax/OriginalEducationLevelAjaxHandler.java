package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.EducationLevel;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.OriginalEducationLevel;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.ComboBoxWebModel;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OriginalEducationLevelAjaxHandler extends NacidBaseRequestHandler {

	public OriginalEducationLevelAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        if ("combo".equals(type)) {
            generateCombo(request, response);
        } else {
            getOriginalEducationLevelData(request, response);
        }
    }

    private void getOriginalEducationLevelData(HttpServletRequest request, HttpServletResponse response) {
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        JSONObject jsonObject = new JSONObject();
        fillOriginalEducationLevelData(id, jsonObject);
        writeToResponse(response, jsonObject.toString());
    }
    private void fillOriginalEducationLevelData(int id, JSONObject jsonObject) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        OriginalEducationLevel originalEduLevel = nacidDataProvider.getNomenclaturesDataProvider().getOriginalEducationLevel(id);
        jsonObject.put("id", originalEduLevel.getId());
        jsonObject.put("name", originalEduLevel.getName());
        jsonObject.put("nameTranslated", DataConverter.parseString(originalEduLevel.getNameTranslated(), ""));
        jsonObject.put("country", originalEduLevel.getCountryId());
        jsonObject.put("educationLevel", originalEduLevel.getEducationLevelId());
        jsonObject.put("dateFrom", DataConverter.formatDate(originalEduLevel.getDateFrom()));
        jsonObject.put("dateTo", DataConverter.formatDate(originalEduLevel.getDateTo()));
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        String name = DataConverter.parseString(request.getParameter("name"), null);
        String nameTranslated = DataConverter.parseString(request.getParameter("nameTranslated"), null);
        int eduLevel = DataConverter.parseInt(request.getParameter("eduLevel"), 0);
        int country = DataConverter.parseInt(request.getParameter("country"), 0);
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        if (dateFrom == null) {
            dateFrom = new Date();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));


        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        id = nomenclaturesDataProvider.saveOriginalEducationLevel(id, name, nameTranslated, eduLevel, country, dateFrom, dateTo);
        JSONObject jsonObject = new JSONObject();
        fillOriginalEducationLevelData(id, jsonObject);
        writeToResponse(response, jsonObject.toString());

    }

    private void generateCombo(HttpServletRequest request, HttpServletResponse response) {
		int countryId = DataConverter.parseInt(request.getParameter("countryId"), 0);
		Integer eduLevelId = DataConverter.parseInteger(request.getParameter("eduLevelId"), null);
		Integer applicationType = DataConverter.parseInteger(request.getParameter("applicationType"), null);

        generateOriginalEducationLevelsByCountryComboBox(applicationType, null, countryId, eduLevelId, getNacidDataProvider().getNomenclaturesDataProvider(), request);

		request.setAttribute(WebKeys.NEXT_SCREEN, "original_education_level_combo");
	}
	public static void generateOriginalEducationLevelsByCountryComboBox(Integer applicationType, Integer activeOriginalEducationLevelId, int countryId, Integer eduLevelId, NomenclaturesDataProvider nDP, HttpServletRequest request) {

	    //if there is no eduLevelId, then the eduLevels per applicationType are getting used in order to filter the originalEduLevels   .
	    List<Integer> eduLevels = new ArrayList<>();
	    if (eduLevelId != null) {
	        eduLevels.add(eduLevelId);
        } else if (applicationType != null) {
            List<EducationLevel> el = nDP.getEducationLevels(applicationType, null, null);
            if (el != null) {
                eduLevels.addAll(el.stream().map(FlatNomenclature::getId).collect(Collectors.toList()));
            }
        }
	    List<OriginalEducationLevel> originalEducationLevels = nDP.getOriginalEducationLevels(countryId, eduLevels.size() == 0 ? null : eduLevels, null, null);
        ComboBoxWebModel wm = ComboBoxUtils.generateNomenclaturesComboBox(activeOriginalEducationLevelId, originalEducationLevels, false, null, null, true);
        request.setAttribute("originalEduLevelCombo", wm);
    }
}
