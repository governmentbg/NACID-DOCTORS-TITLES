package com.nacid.web.handlers.impl.suggestions;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Company;
import com.nacid.bl.applications.CompanyDataProvider;
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
 * Created by georgi.georgiev on 28.09.2015.
 */
public class CompanySuggestionHandler extends NacidBaseRequestHandler {


    public CompanySuggestionHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public CompanySuggestionHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String type = DataConverter.parseString(request.getParameter("type"), null);
        if (type == null) {
            return ;
        }


        String query = DataConverter.parseString(request.getParameter("query"), "");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        CompanyDataProvider companyDataProvider = nacidDataProvider.getCompanyDataProvider();
        List<Company> companies;
        if ("eik".equals(type)) {
            companies = companyDataProvider.getCompaniesByPartOfEik(query);
        } else if ("name".equals(type)) {
            companies = companyDataProvider.getCompaniesByPartOfName(query, false);
        } else {
            throw new RuntimeException("Unknown company search type");
        }

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("query", query);
            List<String> suggestions = new ArrayList<>();
            List<String> inputvalues = new ArrayList<>();
            List<String> data = new ArrayList<>();
            if (companies != null) {
                companies.stream().forEach(company -> {
                    JSONObject d = new JSONObject();
                    inputvalues.add(DataConverter.parseString(company.getEik(), "-") );
                    suggestions.add((company.getEik() == null ? "-" : company.getEik() + " - ") + company.getName());

                    d.put("id", company.getId());
                    d.put("name", DataConverter.parseString(company.getName(), ""));
                    d.put("cityId", DataConverter.parseString(company.getCityId() == null ? "" : company.getCityId() + "", ""));
                    d.put("cityName", DataConverter.parseString(company.getCity() == null ? company.getCityName() : company.getCity().getName(), ""));
                    d.put("addressDetails", DataConverter.parseString(company.getAddressDetails(), ""));
                    d.put("pcode", DataConverter.parseString(company.getPcode(), ""));
                    d.put("phone", DataConverter.parseString(company.getPhone(), ""));
                    d.put("countryId", DataConverter.parseString(company.getCountryId() + "", ""));
                    d.put("countryName", DataConverter.parseString(company.getCountry().getName(), ""));
                    d.put("eik", DataConverter.parseString(company.getEik(), ""));
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
