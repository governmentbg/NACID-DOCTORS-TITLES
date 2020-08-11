package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.ComboBoxWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DiplomaTypeUniversityFacultyHandler extends NacidBaseRequestHandler {

	public DiplomaTypeUniversityFacultyHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer uniId = DataConverter.parseInteger(request.getParameter("university_id"), 0);


		generateUniversityFacultyComboBox(uniId, null, getNacidDataProvider().getUniversityDataProvider(), request);
		request.setAttribute("university_number", request.getParameter("university_number"));
		request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_university_faculty");
	}
	public static ComboBoxWebModel generateUniversityFacultyComboBox(Integer activeUniversityId,  Integer activeUniversityFacultyId, UniversityDataProvider unvDP,
														 HttpServletRequest request/*, boolean addEmptyElement*/) {

		ComboBoxWebModel combobox = new ComboBoxWebModel(activeUniversityFacultyId == null ? null : activeUniversityFacultyId + "", true);
		List<UniversityFaculty> faculties = activeUniversityId == null ? null : unvDP.getUniversityFaculties(activeUniversityId, null, false);
		if (faculties != null) {
            for (UniversityFaculty f : faculties) {
                combobox.addItem(f.getId() + "", f.getName() + (f.isActive() ? "" : " (inactive)"));
            }
        }
        if (request != null) {
			request.setAttribute("universityFacultyCombo", combobox);
		}
		return combobox;
    }
}
