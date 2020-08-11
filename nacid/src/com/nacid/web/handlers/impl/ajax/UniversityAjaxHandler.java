package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.applications.UniversityWebModel;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class UniversityAjaxHandler extends NacidBaseRequestHandler {

	public UniversityAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        int countryId = DataConverter.parseInt(request.getParameter("country"), 0);
        String bgName = DataConverter.parseString(request.getParameter("bgName"), null);
        String orgName = DataConverter.parseString(request.getParameter("orgName"), null);
        String city = DataConverter.parseString(request.getParameter("city"), null);
        String address = DataConverter.parseString(request.getParameter("address"), null);
        String phone = DataConverter.parseString(request.getParameter("phone"), null);
        String fax = DataConverter.parseString(request.getParameter("fax"), null);
        String email = DataConverter.parseString(request.getParameter("email"), null);
        String webSite = DataConverter.convertUrl(request.getParameter("webSite"));
        Integer genericNameId = DataConverter.parseInteger(request.getParameter("genericNameId"), null);
        String urlDiplomaRegister = DataConverter.convertUrl(request.getParameter("urlDiplomaRegister"));


        Date dateFromDate = DataConverter.parseDate(request.getParameter("dateFrom"));
        if (dateFromDate == null ) {
            dateFromDate = Utils.getToday();
        }
        Date dateToDate = DataConverter.parseDate(request.getParameter("dateTo"));




        UniversityDataProvider unvDP = getNacidDataProvider().getUniversityDataProvider();
        JSONObject jsonObj = new JSONObject();
        boolean error = false;

        if (id == 0) {
            List<University> universities = unvDP.getUniversities(countryId, UniversityDataProvider.NAME_TYPE_BG, true, bgName);
            if (universities != null && universities.size() > 0) {
                for (University university : universities) {
                    if (university.getBgName().equals(bgName)) {
                        error = true;
                        jsonObj.put("errorMessage", "В базата съществува висше училище с даденото наименование на български!");
                    }
                }

            }

            universities = unvDP.getUniversities(countryId, UniversityDataProvider.NAME_TYPE_ORIGINAL, true, orgName);
            if (universities != null && universities.size() > 0) {
                for (University university : universities) {
                    if (university.getOrgName().equals(orgName)) {
                        error = true;
                        jsonObj.put("errorMessage", "В базата съществува висше училище с даденото оригинално наименование!");
                    }
                }

            }
        }


        if (!error) {
            int newId = unvDP.saveUniversity(id, countryId, bgName, orgName, city, address, phone, fax, email, webSite, urlDiplomaRegister,
                    dateFromDate, dateToDate, genericNameId);
            University u = unvDP.getUniversity(newId);
            fillJsonObject(u, jsonObj);

        }

        jsonObj.put("error", error);
        writeToResponse(response, jsonObj.toString());

	}

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObj = new JSONObject();
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        UniversityDataProvider universityDataProvider = getNacidDataProvider().getUniversityDataProvider();
        University university = universityDataProvider.getUniversity(id);
        if (university == null) {
            jsonObj.put("errorMessage", "В базата не съществува университет с избраното ID !" + request.getParameter("id"));
        } else {
            fillJsonObject(university, jsonObj);
        }
        writeToResponse(response, jsonObj.toString());
    }

    public static void fillJsonObject(University university, JSONObject jsonObj) {
        jsonObj.put("id", university.getId());
        jsonObj.put("bgName", getValueOrEmptyString(university.getBgName()));
        jsonObj.put("orgName", getValueOrEmptyString(university.getOrgName()));
        jsonObj.put("countryId", university.getCountry().getId());
        jsonObj.put("city", getValueOrEmptyString(university.getCity()));
        jsonObj.put("address", getValueOrEmptyString(university.getAddrDetails()));
        jsonObj.put("phone", getValueOrEmptyString(university.getPhone()));
        jsonObj.put("fax", getValueOrEmptyString(university.getFax()));
        jsonObj.put("email", getValueOrEmptyString(university.getEmail()));
        jsonObj.put("webSite", getValueOrEmptyString(university.getWebSite()));
        jsonObj.put("urlDiplomaRegister", getValueOrEmptyString(university.getUrlDiplomaRegister()));
        jsonObj.put("urlDiplomaRegisterLink", getValueOrEmptyString(UniversityWebModel.getUrlDiplomaRegisterLink(university.getUrlDiplomaRegister())));
        jsonObj.put("dateFrom", DataConverter.formatDate(university.getDateFrom()));
        jsonObj.put("dateFrom", DataConverter.formatDate(university.getDateFrom()));
        jsonObj.put("genericName", getValueOrEmptyString(university.getGenericName() == null ? null : university.getGenericName().getName()));
        jsonObj.put("genericNameId", getValueOrEmptyString(university.getGenericNameId() == null ? null : university.getGenericNameId() + ""));
    }
    private static final String getValueOrEmptyString(String value) {
        return value == null ? "" : value;
    }
}
