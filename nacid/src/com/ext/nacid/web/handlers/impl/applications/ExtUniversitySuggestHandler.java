package com.ext.nacid.web.handlers.impl.applications;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;

public class ExtUniversitySuggestHandler extends NacidExtBaseRequestHandler {

    public ExtUniversitySuggestHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        // int nameType =
        // DataConverter.parseInt(request.getParameter("nametype"),
        // UniversityDataProvider.NAME_TYPE_BG);
    	Integer countryId = DataConverter.parseInteger(request.getParameter("country_id"), null);
        String partOfName = DataConverter.parseString(request.getParameter("query"), "");
        // String partOfName = "б";
        // System.out.println(partOfName);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        List<University> bgUniversities = universityDataProvider.getUniversities(
                    countryId, UniversityDataProvider.NAME_TYPE_BG, false,  partOfName);
        List<University> origUniversities = universityDataProvider.getUniversities(countryId,
                UniversityDataProvider.NAME_TYPE_ORIGINAL, false, partOfName);
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("query", request.getParameter("query"));
            List<String> suggestions = new ArrayList<String>();
            List<String> inputvalues = new ArrayList<String>();
            List<String> data = new ArrayList<String>();
            if (bgUniversities != null) {
                for (University u : bgUniversities) {

                    JSONObject d = new JSONObject();
                    List<String> lst = new ArrayList<String>();
                    if (u.getBgName() != null) {
                    	lst.add(u.getBgName());
                    }
                    if (u.getCity() != null) {
                    	lst.add(u.getCity());
                    }
                    if (u.getCountry() != null) {
                    	lst.add(u.getCountry().getName());
                    }
                    String longName = StringUtils.join(lst, ", ");
                    inputvalues.add(longName);
                    suggestions.add(longName);

                    d.put("id", u.getId());
                    /*
                     * d.put("bgname", u.getBgName()); d.put("orgname",
                     * u.getOrgName()); d.put("countryid", u.getCountryId());
                     * d.put("city", u.getCityName()); d.put("address",
                     * u.getAddrDetails());
                     */
                    data.add(d.toString());
                }
            }
            
            if (origUniversities != null) {
                for (University u : origUniversities) {

                    JSONObject d = new JSONObject();
                    String longName = u.getOrgName() + "," + u.getCity() + ",  " + nomenclaturesDataProvider.getCountry(u.getCountryId()).getName();
                    inputvalues.add(longName);
                    suggestions.add(longName);

                    d.put("id", u.getId());
                    /*
                     * d.put("bgname", u.getBgName()); d.put("orgname",
                     * u.getOrgName()); d.put("countryid", u.getCountryId());
                     * d.put("city", u.getCityName()); d.put("address",
                     * u.getAddrDetails());
                     */
                    data.add(d.toString());
                }
            }

            jsonObj.put("suggestions", suggestions);
            jsonObj.put("inputvalues", inputvalues);
            jsonObj.put("data", data);

            writeToResponse(response, jsonObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
        //UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        //System.out.println(universityDataProvider.getUniversities(null, 1, "Б"));
    }

}
