package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.applications.CompetentInstitutionHandler;
import com.nacid.web.handlers.impl.applications.UniversityValidityHandler;
import com.nacid.web.model.applications.UniversityFacultyWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by georgi.georgiev on 24.10.2018
 */
public class UniversityFacultyAjaxHandler extends NacidBaseRequestHandler {
    public UniversityFacultyAjaxHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public UniversityFacultyAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {

        Integer universityId = DataConverter.parseInteger(request.getParameter("universityId"), null);

        UniversityDataProvider universityDataProvider = getNacidDataProvider().getUniversityDataProvider();
        List<UniversityFaculty> univeristyFaculties = universityDataProvider.getUniversityFaculties(universityId, null, false);
        request.setAttribute(WebKeys.UNIVERSITY_FACULTIES_WEB_MODEL, univeristyFaculties.stream().map(UniversityFacultyWebModel::new).collect(Collectors.toList()));

        request.setAttribute(WebKeys.NEXT_SCREEN, "university_faculties_table");






    }

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        Integer id = DataConverter.parseInteger(request.getParameter("id"), null);
        UniversityFaculty universityFaculty = nacidDataProvider.getUniversityDataProvider().getUniversityFaculty(id);

        JSONObject jsonObject = toJsonObject(universityFaculty);
        writeToResponse(response, jsonObject.toString());
    }


    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        try {
            NacidDataProvider nacidDataProvider = getNacidDataProvider();
            UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
            int id = DataConverter.parseInt(request.getParameter("id"), 0);
            int universityId = DataConverter.parseInt(request.getParameter("universityId"), 0);
            String name = DataConverter.parseString(request.getParameter("name"), null);
            String originalName = DataConverter.parseString(request.getParameter("originalName"), null);
            Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
            Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
            id = universityDataProvider.saveUniveristyFaculty(id, universityId, name, originalName, dateFrom, dateTo);
            JSONObject jsonObject = toJsonObject(universityDataProvider.getUniversityFaculty(id));
            writeToResponse(response, jsonObject.toString());
        } catch (Exception e) {
            JSONObject jsonObject = createError(e.getMessage());
            writeToResponse(response, jsonObject.toString());
        }
    }

    private static JSONObject toJsonObject(UniversityFaculty faculty) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("faculty_id", faculty.getId());
        jsonObject.put("faculty_name", DataConverter.parseString(faculty.getName(), ""));
        jsonObject.put("faculty_original_name", DataConverter.parseString(faculty.getOriginalName(), ""));
        jsonObject.put("faculty_date_from", DataConverter.formatDate(faculty.getDateFrom(), ""));
        jsonObject.put("faculty_date_to", DataConverter.formatDate(faculty.getDateTo(), ""));
        return jsonObject;

    }

    private static JSONObject createError(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", true);
        jsonObject.put("errorMessage", message);
        return jsonObject;
    }

}
