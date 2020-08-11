package com.ext.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.ExtPersonDocument;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: ggeorgiev
 * Date: 17.10.2019 г.
 * Time: 11:15
 */
public class ExtPersonAjaxHandler extends NacidBaseRequestHandler {

    public ExtPersonAjaxHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public ExtPersonAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {

        boolean loggedUser = DataConverter.parseBoolean(request.getParameter("loggedUser"));
        ExtPersonDataProvider dp = getNacidDataProvider().getExtPersonDataProvider();
        ExtPerson p = null;
        if (loggedUser) {
            p = dp.getExtPersonByUserId(getLoggedUser(request, response).getUserId());
        } else {
            Integer civilIdType = DataConverter.parseInteger(request.getParameter("civilIdType"), null);
            String civilId = DataConverter.parseString(request.getParameter("civilId"), null);
            if (!StringUtils.isEmpty(civilId) && civilIdType != null) {
                p = dp.getExtPerson(civilIdType, civilId);
            }
        }

        JSONObject json = new JSONObject();
        fillExtPersonJsonOject(json, p);
        writeToResponse(response, json.toString());
    }
    private static void fillExtPersonJsonOject(JSONObject json, ExtPerson p) {
        if (p == null ) {
            return;
        }
        json.put("id", p.getId());
        json.put("fullName", p.getFullName());
        json.put("fname", DataConverter.parseString(p.getFName(), ""));
        json.put("sname", DataConverter.parseString(p.getSName(), ""));
        json.put("lname", DataConverter.parseString(p.getLName(), ""));
        json.put("civilId", p.getCivilId());
        json.put("civilIdType", p.getCivilIdTypeId());
        json.put("email", DataConverter.parseString(p.getEmail(), ""));
        json.put("birthDate", DataConverter.formatDate(p.getBirthDate()));
        json.put("citizenshipId", p.getCitizenshipId());
        json.put("birthCountryId", p.getBirthCountryId());
        if (p.getActiveExtPersonDocument() != null) {
            ExtPersonDocument d = p.getActiveExtPersonDocument();
            json.put("document.id", d.getId());
            json.put("document.issuedBy", DataConverter.parseString(d.getIssuedBy(), ""));
            json.put("document.number", DataConverter.parseString(d.getNumber(), ""));
            json.put("document.active", d.getActive());
            json.put("document.dateOfIssue", DataConverter.formatDate(d.getDateOfIssue()));
        }
    }
}
