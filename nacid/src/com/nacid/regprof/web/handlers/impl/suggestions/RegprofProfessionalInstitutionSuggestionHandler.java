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
import com.nacid.bl.impl.regprof.ProfessionalInstitutionImpl;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.ProfessionalInstitutionNamesRecord;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;

public class RegprofProfessionalInstitutionSuggestionHandler extends RegProfBaseRequestHandler {

    public RegprofProfessionalInstitutionSuggestionHandler(NacidDataProvider nacidDataProvider, Integer groupId,ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public RegprofProfessionalInstitutionSuggestionHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        ProfessionalInstitutionDataProvider professionalInstitutionDataProvider = getNacidDataProvider().getProfessionalInstitutionDataProvider();
        String partOfName = request.getParameter("query");
        Integer institutionId = DataConverter.parseInteger(request.getParameter("institution"), null);
        Integer institutionTypeId = DataConverter.parseInteger(request.getParameter("institution_type"), null);
        Integer educationTypeId = DataConverter.parseInteger(request.getParameter("education_type"), null);
        boolean isFormerName = DataConverter.parseBoolean(request.getParameter("formername"));
        if (isFormerName && institutionId != null) {
            setFormerName(response, professionalInstitutionDataProvider, institutionId, partOfName);
        } else if (!isFormerName) {
            setInstitutions(response, professionalInstitutionDataProvider, institutionTypeId, educationTypeId, partOfName);
        }
    }
    
    private static void setInstitutions(HttpServletResponse response, ProfessionalInstitutionDataProvider professionalInstitutionDataProvider, 
            Integer institutionType, Integer educationTypeId, String partOfName) {
        try {
            List<ProfessionalInstitutionImpl> institutions = professionalInstitutionDataProvider.getProfessionalInstitutions(partOfName, institutionType, educationTypeId);
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("query", partOfName);
            List<String> suggestions = new ArrayList<String>();
            List<String> inputvalues = new ArrayList<String>();
            List<String> data = new ArrayList<String>();
            if (institutions != null) {
                for (int i = 0; i < institutions.size(); i++) {
                    JSONObject d = new JSONObject();

                    inputvalues.add(institutions.get(i).getBgName());
                    suggestions.add(institutions.get(i).getBgName());

                    d.put("id", institutions.get(i).getId());
                    d.put("name", institutions.get(i).getBgName());
                    d.put("type", institutions.get(i).getProfessionalInstitutionTypeId());

                    data.add(d.toString());
                }
            }
            jsonObj.put("suggestions", suggestions);
            jsonObj.put("inputvalues", inputvalues);
            jsonObj.put("data", data);

            writeToResponse(response, jsonObj.toString());
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
    private static void setFormerName(HttpServletResponse response, ProfessionalInstitutionDataProvider professionalInstitutionDataProvider, Integer institutionId, String partOfName) {
        List<ProfessionalInstitutionNamesRecord> formerNames = professionalInstitutionDataProvider.getProfessionalInstitutionNames(institutionId, partOfName, true);
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("query", partOfName);
            List<String> suggestions = new ArrayList<String>();
            List<String> inputvalues = new ArrayList<String>();
            List<String> data = new ArrayList<String>();
            
            for (ProfessionalInstitutionNamesRecord record : formerNames) {
                JSONObject d = new JSONObject();
                
                inputvalues.add(record.getFormerName());
                suggestions.add(record.getFormerName());
                
                d.put("id", record.getId());
                d.put("name", record.getFormerName());
                
                data.add(d.toString());
            }
            jsonObj.put("suggestions", suggestions);
            jsonObj.put("inputvalues", inputvalues);
            jsonObj.put("data", data);    
            
            writeToResponse(response, jsonObj.toString());
        } catch (JSONException e) {
            throw Utils.logException(e);
        }
    }

}
