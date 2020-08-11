package com.nacid.web.handlers.impl.suggestions;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingInstitution;
import com.nacid.bl.applications.TrainingInstitutionDataProvider;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstitutionSuggestHandler extends NacidBaseRequestHandler{

	public InstitutionSuggestHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer countryId = DataConverter.parseInteger(request.getParameter("country_id"), null);
		String partOfName = DataConverter.parseString(request.getParameter("query"), "");
		//String partOfName = "б";
		System.out.println("partOfName:" + partOfName);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		TrainingInstitutionDataProvider trainingInstitutionDataProvider = nacidDataProvider.getTrainingInstitutionDataProvider();
		List<TrainingInstitution> trainingInstitutions = countryId == null ? null : trainingInstitutionDataProvider.selectTrainingInstitutionsByCountry(countryId);

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("query", request.getParameter("query"));
			List<String> suggestions = new ArrayList<String>();
			List<String> inputvalues = new ArrayList<String>();

			boolean longInputs = DataConverter.parseBoolean(request.getParameter("long_inputs"));
			List<String> data = new ArrayList<String>();
			if (trainingInstitutions != null) {
				for (TrainingInstitution i: trainingInstitutions) {

					String name = i.getName() ;
					String longName = Stream.of(i.getName(), i.getCity(), i.getCountry() == null ? null : i.getCountry().getName()).filter(StringUtils::isNotEmpty).collect(Collectors.joining(", "));

					suggestions.add(longName);
					inputvalues.add(longInputs ? longName : name);

					JSONObject d = new JSONObject();
					fillJsonObject(i, d);
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
	private void fillJsonObject(TrainingInstitution i, JSONObject d) {
		d.put("id", i.getId());
		d.put("countryId", i.getCountryId());
		d.put("countryName", i.getCountry() == null ? "" : i.getCountry().getName());
		d.put("name", i.getName());
		d.put("city", i.getCity());
		d.put("address", i.getAddrDetails());
		d.put("pcode", i.getPcode());
		d.put("dateFrom", i.getDateFrom());
		d.put("dateTo", i.getDateTo());
		d.put("universityIds", i.getUnivIds());
	}

	

}
