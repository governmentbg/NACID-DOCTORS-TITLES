package com.nacid.web.handlers.impl.suggestions;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OriginalSpecialitySuggestHandler extends NacidBaseRequestHandler{

	public OriginalSpecialitySuggestHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		int professionGroupId = DataConverter.parseInt(request.getParameter("group_id"), 0);
		String partOfName = DataConverter.parseString(request.getParameter("query"), "");
		//String partOfName = "б";
		//System.out.println("partOfName:" + partOfName);
		//System.out.println("professionGroupId:" + professionGroupId);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		boolean onlyActive = DataConverter.parseBoolean(request.getParameter("only_active"));
		List<FlatNomenclature> specialities = nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY, partOfName, onlyActive ? new Date() : null, null);
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("query", request.getParameter("query"));
			List<String> suggestions = new ArrayList<String>();
			List<String> inputvalues = new ArrayList<String>();

			List<String> data = new ArrayList<String>();
			if (specialities != null) {
				for (FlatNomenclature s: specialities) {

					JSONObject d = new JSONObject();
					String name = s.getName();;
					suggestions.add(name);
					inputvalues.add(name);
					d.put("id", s.getId());
					d.put("name", name);
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
