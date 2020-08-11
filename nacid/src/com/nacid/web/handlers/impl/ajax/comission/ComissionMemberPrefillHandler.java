package com.nacid.web.handlers.impl.ajax.comission;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;

public class ComissionMemberPrefillHandler extends NacidBaseRequestHandler {

	public ComissionMemberPrefillHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		String partOfCivilId = DataConverter.parseString(request
				.getParameter("query"), "");
		ComissionMemberDataProvider cmDP = getNacidDataProvider().getComissionMemberDataProvider();
		List<ComissionMember> cms = cmDP.getComissionMembersByEGNPart(partOfCivilId);
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("query", partOfCivilId);
			List<String> suggestions = new ArrayList<String>();
			List<String> inputvalues = new ArrayList<String>();
			List<String> data = new ArrayList<String>();
			for (ComissionMember c : cms) {

				JSONObject d = new JSONObject();
				inputvalues.add(c.getEgn());
				suggestions.add(c.getEgn() + " - " + c.getFname() + "  " + c.getLname());

				//d.put("id", c.getId());
				d.put("fname", fieldToJson(c.getFname()));
				d.put("sname", fieldToJson(c.getSname()));
				d.put("lname", fieldToJson(c.getLname()));
				d.put("degree", fieldToJson(c.getDegree()));
				d.put("institution", fieldToJson(c.getInstitution()));
				d.put("division", fieldToJson(c.getDivision()));
				d.put("title", fieldToJson(c.getTitle()));
				//d.put("egn", c.getEgn());
				d.put("homeCity", fieldToJson(c.getHomeCity()));
				d.put("homePcode", fieldToJson(c.getHomePcode()));
				d.put("homeAddress", fieldToJson(c.getHomeAddress()));
				d.put("phone", fieldToJson(c.getPhone()));
				d.put("email", fieldToJson(c.getEmail()));
				d.put("gsm", fieldToJson(c.getGsm()));
				d.put("iban", fieldToJson(c.getIban()));
				d.put("bic", fieldToJson(c.getBic()));
				d.put("comissionPosId", c.getComissionPos().getId());
				
				int prGr = 0;
				if(c.getProfGroup() != null) {
					 prGr = c.getProfGroup().getId();
				}
				d.put("profGroupId", prGr);
				
				long uId = 0;
				if(c.getUser() != null) {
					uId = c.getUser().getUserId();
				}
				d.put("userId", uId); 
				
				data.add(d.toString());

			}
			jsonObj.put("suggestions", suggestions);
			jsonObj.put("inputvalues", inputvalues);
			jsonObj.put("data", data);

			// System.out.println(jsonObj.toString());
			// reponse.setHeader("Content-Type", "application/html");
			// response.setHeader("X-JSON", jsonObj.toString());
			
			writeToResponse(response, jsonObj.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private String fieldToJson(String s) {
		if(s == null)
			return "";
		return s;
	}
}
