package com.nacid.web.handlers.impl.suggestions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;

public class FlatNomenclatureSuggestHandler extends NacidBaseRequestHandler{

	public FlatNomenclatureSuggestHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer type = DataConverter.parseInteger(request.getParameter("type"), null);
		if (type == null) {
			writeToResponse(response, "");
			return;
		}
		String partOfName = DataConverter.parseString(request.getParameter("query"), "");
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		boolean onlyActive = DataConverter.parseBoolean(request.getParameter("only_active"));
		List<FlatNomenclature> nomenclatures = nomenclaturesDataProvider.getFlatNomenclaturesContainingNameLike(type, partOfName, onlyActive ? new Date() : null, null);
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("query", request.getParameter("query"));
			List<String> suggestions = new ArrayList<String>();
			List<String> inputvalues = new ArrayList<String>();

			List<String> data = new ArrayList<String>();
			if (nomenclatures != null) {
				for (FlatNomenclature s: nomenclatures) {

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
