package com.nacid.web.handlers.impl.academicrecognition;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgi.georgiev on 14.09.2016.
 */
public class BgAcademicRecognitionSuggestionHandler extends NacidBaseRequestHandler {

    public BgAcademicRecognitionSuggestionHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public BgAcademicRecognitionSuggestionHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String type = DataConverter.parseString(request.getParameter("type"), null);
        String query = DataConverter.parseString(request.getParameter("query"), "");

        NomenclaturesDataProvider nomeclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
        List<String> result;
        result = nomeclaturesDataProvider.getBgAcademicRecognitionSuggestion(type, query);

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("query", query);
            List<String> suggestions = new ArrayList<>();
            List<String> inputvalues = new ArrayList<>();
            List<String> data = new ArrayList<>();
            if (result != null) {
                result.stream().forEach(suggestion -> {
                    JSONObject d = new JSONObject();
                    suggestions.add(suggestion);
                    inputvalues.add(suggestion);
                    d.put("id", suggestion);
                    d.put("name", suggestion);
                    data.add(d.toString());
                });
            }
            jsonObj.put("suggestions", suggestions);
            jsonObj.put("inputvalues", inputvalues);
            jsonObj.put("data", data);
            writeToResponse(response, jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
