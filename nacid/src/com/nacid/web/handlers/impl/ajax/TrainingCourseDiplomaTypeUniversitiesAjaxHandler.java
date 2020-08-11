package com.nacid.web.handlers.impl.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.applications.UniversityWithFacultyWebModel;

public class TrainingCourseDiplomaTypeUniversitiesAjaxHandler extends NacidBaseRequestHandler {

	public TrainingCourseDiplomaTypeUniversitiesAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer diplomaTypeId = DataConverter.parseInteger(request.getParameter("diplomaTypeId"), null);
		Integer baseUniversityId = DataConverter.parseInteger(request.getParameter("baseUniversityId"), 0);
//		System.out.println(diplomaTypeId + " baseUniversityId:" + baseUniversityId);
		generateUniversitiesWebModel(request, getNacidDataProvider(), diplomaTypeId, baseUniversityId );
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), true, request, WebKeys.UNIVERSITY_COUNTRY, null, true);
		request.setAttribute(WebKeys.NEXT_SCREEN, "training_course_diploma_type_universities");
	}
	public static void generateUniversitiesWebModel(HttpServletRequest request, NacidDataProvider nacidDataProvider,  Integer diplomaTypeId, Integer baseUniversityId) {
	    
	    if (diplomaTypeId != null) {
	        
	    	DiplomaTypeDataProvider diplomaTypeDataProvider = nacidDataProvider.getDiplomaTypeDataProvider();
	    	UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
            DiplomaType diplomaType = diplomaTypeDataProvider.getDiplomaType(diplomaTypeId);
            if (diplomaType != null) {
            	List<UniversityWithFacultyWebModel> webmodel = new ArrayList<>();
				Set<UniversityIdWithFacultyId> uniWithFacultyIds = diplomaType.getUniversityWithFacultyIds();
            	if (uniWithFacultyIds != null) {
					for ( UniversityIdWithFacultyId uniWithFacultyId : uniWithFacultyIds) {
						if (!Objects.equals(uniWithFacultyId.getUniversityId(), baseUniversityId) ) {
							UniversityFaculty f = uniWithFacultyId.getFacultyId() == null ? null : universityDataProvider.getUniversityFaculty(uniWithFacultyId.getFacultyId());
							University uni = universityDataProvider.getUniversity(uniWithFacultyId.getUniversityId());
							webmodel.add(new UniversityWithFacultyWebModel(uni, f));
						}
					}

            	}
            	if (webmodel.size() > 0) {
            		request.setAttribute(WebKeys.UNIVERSITY_WITH_FACULTY_WEB_MODEL, webmodel);
            	}
            }
            
	    }
	    
	}
}
