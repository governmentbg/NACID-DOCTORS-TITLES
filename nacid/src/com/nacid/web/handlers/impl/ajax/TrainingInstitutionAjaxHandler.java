package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingInstitutionAjaxHandler extends NacidBaseRequestHandler{
    public TrainingInstitutionAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        Integer institutionId = DataConverter.parseInteger(request.getParameter("id"), null);
        writeTrainingInstitutionToResponse(response, institutionId);
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {

        Integer courseId = DataConverter.parseInteger(request.getParameter("trainingCourseId"), null);
        String elementId = request.getParameter("elementId");
        String elementName = request.getParameter("elementName");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        TrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getTrainingCourseDataProvider();
        TrainingCourse trainingCourse = trainingCourseDataProvider.getTrainingCourse(courseId);
        
        request.setAttribute(WebKeys.COMBO, ApplicationsHandler.generateTrainingInstCombo(null, trainingCourse.getUniversityIds(), nacidDataProvider));
        request.setAttribute("comboName", elementName);
        request.setAttribute("comboId", elementId);
        request.setAttribute("clazz", "brd w400");

        request.setAttribute(WebKeys.NEXT_SCREEN, "ajax_combo");

    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int id = DataConverter.parseInteger(request.getParameter("id"), 0);
        String name = DataConverter.parseString(request.getParameter("name"), null);
        Integer countryId = DataConverter.parseInteger(request.getParameter("country"), null);
        String city = DataConverter.parseString(request.getParameter("city"), null);
        String postCode = DataConverter.parseString(request.getParameter("postCode"), null);
        String address = DataConverter.parseString(request.getParameter("address"), null);
        String phone = DataConverter.parseString(request.getParameter("phone"), null);
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        List<Integer> universityIds = RequestParametersUtils.convertRequestParameterToIntegerList(request.getParameter("universityIds"));
        TrainingInstitutionDataProvider tidp = getNacidDataProvider().getTrainingInstitutionDataProvider();


        int[] uniIds = null;
        if (universityIds != null && universityIds.size() > 0) {
            uniIds = new int[universityIds.size()];
            for (int i = 0; i < universityIds.size(); i++) {
                uniIds[i] = universityIds.get(i);
            }
        }

        id = tidp.saveTrainingInstitution(id, name, countryId, city, postCode, address, phone, dateFrom, dateTo, uniIds);
        writeTrainingInstitutionToResponse(response, id);

    }


    private void writeTrainingInstitutionToResponse(HttpServletResponse response, Integer institutionId) {
        JSONObject jsonObj = new JSONObject();
        if (institutionId != null) {
            NacidDataProvider nacidDataProvider = getNacidDataProvider();
            TrainingInstitutionDataProvider tidp = nacidDataProvider.getTrainingInstitutionDataProvider();
            TrainingInstitution ti = tidp.selectTrainingInstitution(institutionId);
            UniversityDataProvider udp = nacidDataProvider.getUniversityDataProvider();
            jsonObj.put("id", ti.getId());
            jsonObj.put("name", DataConverter.parseString(ti.getName(), ""));
            jsonObj.put("address", DataConverter.parseString(ti.getAddrDetails(), ""));
            jsonObj.put("city", DataConverter.parseString(ti.getCity(), ""));
            jsonObj.put("countryId", ti.getCountryId());
            jsonObj.put("postCode", DataConverter.parseString(ti.getPcode(), ""));
            jsonObj.put("phone", DataConverter.parseString(ti.getPhone(), ""));

            if (ti.getUnivIds() != null && ti.getUnivIds().length > 0) {
                List<String> names = new ArrayList<String>();
                for (int uId : ti.getUnivIds()) {
                    University u = udp.getUniversity(uId);
                    names.add(u.getBgName() + ", " + u.getCountry().getName());
                }
                jsonObj.put("uniIds", ti.getUnivIds());
                jsonObj.put("uniNames", names.toArray(new String[]{}));

            }


            jsonObj.put("dateFrom", DataConverter.formatDate(ti.getDateFrom()));
            jsonObj.put("dateTo", DataConverter.formatDate(ti.getDateTo()));

        }
        writeToResponse(response, jsonObj.toString());
    }
}
