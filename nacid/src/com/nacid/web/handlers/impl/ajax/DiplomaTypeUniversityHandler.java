package com.nacid.web.handlers.impl.ajax;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.ComboBoxWebModel;

public class DiplomaTypeUniversityHandler extends NacidBaseRequestHandler {

	public DiplomaTypeUniversityHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer countryId = DataConverter.parseInteger(request.getParameter("countryId"), null);
		generateUniversityComboBox(null, countryId, getNacidDataProvider().getUniversityDataProvider(), request);
		request.setAttribute("university_number", request.getParameter("university_number"));
		request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_university");
	}
	public static void generateUniversityComboBox(Integer activeUniversityId, Integer countryId, UniversityDataProvider unvDP,
            HttpServletRequest request/*, boolean addEmptyElement*/) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeUniversityId == null ? null : activeUniversityId + "", true);
        List<University> universities = countryId == null ? null : unvDP.getUniversities(countryId, false);
        if (universities != null) {
            for (University u : universities) {
                combobox.addItem(u.getId() + "", u.getBgName() + (u.isActive() ? "" : " (inactive)"));
            }

        }
        request.setAttribute("universityCombo", combobox);
    }
}
