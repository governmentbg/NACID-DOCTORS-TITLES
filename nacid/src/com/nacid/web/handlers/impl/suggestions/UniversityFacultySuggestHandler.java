package com.nacid.web.handlers.impl.suggestions;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class UniversityFacultySuggestHandler extends NacidBaseRequestHandler{

	public UniversityFacultySuggestHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer universityId = DataConverter.parseInteger(request.getParameter("university_id"), 0);
		String partOfName = DataConverter.parseString(request.getParameter("query"), "");
		//String partOfName = "б";
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
		List<UniversityFaculty> faculties = universityDataProvider.getUniversityFaculties(universityId, partOfName,false);


		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("query", request.getParameter("query"));
			List<String> suggestions = new ArrayList<String>();
			List<String> inputvalues = new ArrayList<String>();

			List<String> data = new ArrayList<String>();
			if (faculties != null) {
				for (UniversityFaculty f : faculties) {

					JSONObject d = new JSONObject();
					String name = f.getName();
					suggestions.add(name);
					inputvalues.add(name);

					d.put("id", f.getId());
					d.put("name", DataConverter.parseString(f.getName(), ""));
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
