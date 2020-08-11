package com.nacid.web.handlers.impl.suggestions;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.ajax.UniversityAjaxHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class UniversitySuggestHandler extends NacidBaseRequestHandler{

	public UniversitySuggestHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer countryId = DataConverter.parseInteger(request.getParameter("country_id"), null);
		int nameType = DataConverter.parseInt(request.getParameter("nametype"), UniversityDataProvider.NAME_TYPE_BG);
		if (nameType == -1) {
			return;
		}
		String partOfName = DataConverter.parseString(request.getParameter("query"), "");
		//String partOfName = "б";
		System.out.println("partOfName:" + partOfName);
		System.out.println("nameType:" + nameType);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
		List<University> universities = universityDataProvider.getUniversities(countryId, nameType,false, partOfName);
		//NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("query", request.getParameter("query"));
			List<String> suggestions = new ArrayList<String>();
			List<String> inputvalues = new ArrayList<String>();
			/**
			 * ako e true, togava i v input values se pishe name, city, country na universiteta
			 * ina4e v input-a se slaga samo imeto na universiteta
			 */
			boolean longInputs = DataConverter.parseBoolean(request.getParameter("long_inputs"));
			List<String> data = new ArrayList<String>();
			if (universities != null) {
				for (University u: universities) {


					String name;
					if (nameType == 0) {
						name = u.getBgName() + " / " + u.getOrgName();
					} else if (nameType == UniversityDataProvider.NAME_TYPE_BG) {
						name = u.getBgName();
					} else if (nameType == UniversityDataProvider.NAME_TYPE_ORIGINAL) {
						name = u.getOrgName();
					} else {
						throw new IllegalArgumentException("nameType must be 0, or one of the defined UniversityDataProvider.NAME_TYPE_*");
					}
					//String name = nameType == UniversityDataProvider.NAME_TYPE_ORIGINAL ? u.getOrgName() : u.getBgName();
					List<String> lst = new ArrayList<String>();

                    if (nameType == 0 || nameType == UniversityDataProvider.NAME_TYPE_BG) {
                        lst.add(u.getBgName());
                    } else if (nameType == UniversityDataProvider.NAME_TYPE_ORIGINAL) {
                        lst.add(u.getOrgName());
                    } else {
                        throw new IllegalArgumentException("nameType must be 0, or one of the defined UniversityDataProvider.NAME_TYPE_*");
                    }

					if (u.getCity() != null) {
						lst.add(u.getCity());
					}
					if (u.getCountry() != null) {
						lst.add(u.getCountry().getName());
					}
					String longName = StringUtils.join(lst, ", ");

					suggestions.add(longName);
					inputvalues.add(longInputs ? longName : name);

					JSONObject d = new JSONObject();
					UniversityAjaxHandler.fillJsonObject(u, d);
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
	

}
