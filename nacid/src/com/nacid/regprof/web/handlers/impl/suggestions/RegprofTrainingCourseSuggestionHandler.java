package com.nacid.regprof.web.handlers.impl.suggestions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;

public class RegprofTrainingCourseSuggestionHandler extends RegProfBaseRequestHandler {
 
    public RegprofTrainingCourseSuggestionHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        if (type == null || type.isEmpty()) {
            return;
        } else if (type.equals("sec_speciality")) {
            Integer professionalQualificationId = DataConverter.parseInteger(request.getParameter("prof_qual_id"), -1);
            if (professionalQualificationId != -1) {
                List<SecondarySpeciality> specialities = nomenclaturesDataProvider.getSecondarySpecialities(request.getParameter("query"), null, null, professionalQualificationId);
                JSONObject jsonObj = new JSONObject();
                List<String> data = new ArrayList<String>();
                try {
                    jsonObj.put("query", request.getParameter("query"));
                } catch (JSONException e1) {
                    Utils.logException(e1);
                }
                List<String> suggestions = new ArrayList<String>();
                List<String> inputvalues = new ArrayList<String>();
                if (specialities != null) {
                    
                    try {
                        for (SecondarySpeciality speciality : specialities) {
                            JSONObject d = new JSONObject();
                            suggestions.add(speciality.getName());
                            inputvalues.add(speciality.getName());
                            d.put("id", speciality.getId());
                            d.put("name", speciality.getName());
                            data.add(d.toString());
                        }
                        jsonObj.put("suggestions", suggestions);
                        jsonObj.put("inputvalues", inputvalues);
                        jsonObj.put("data", data);
                        writeToResponse(response, jsonObj.toString());
                    } catch (JSONException e) {
                        Utils.logException(e);
                    }
                }
                else {
                    try {
                        jsonObj.put("error", true);
                        writeToResponse(response, jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (type.equals("sec_degree")) {
            Integer specialityId = DataConverter.parseInteger(request.getParameter("sec_speciality_id"), -1);
            if (specialityId != -1) {
                JSONObject jsonObj = new JSONObject();
                SecondarySpeciality speciality = nomenclaturesDataProvider.getSecondarySpeciality(specialityId);
                if (speciality.getQualificationDegreeId() != null) {
                    int degreeId = speciality.getQualificationDegreeId();
                    FlatNomenclature degree = nomenclaturesDataProvider.getFlatNomenclature(nomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, degreeId);
                    if (degree != null) {
                        try {
                            jsonObj.put("degree", degree.getName());
                            writeToResponse(response, jsonObj.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        jsonObj.put("degree", "Няма информация за степен");
                        writeToResponse(response, jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
