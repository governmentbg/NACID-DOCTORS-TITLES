package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.OriginalEducationLevel;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.ComboBoxWebModel;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class DiplomaTypeAjaxHandler extends NacidBaseRequestHandler {

	public DiplomaTypeAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
	    /*String operationName = getOperationName(request);
	    int operationId = UserOperationsUtils.getOperationId(operationName);
	    if (operationId == UserOperationsUtils.OPERATION_LEVEL_SAVE) {
	        handleSave(request, response);
	    } else {*/
    		Integer universityId = DataConverter.parseInteger(request.getParameter("universityId"), null);
    		Integer applicationId = DataConverter.parseInteger(request.getParameter("application_id"), null);
    		Integer diplomaType = DataConverter.parseInteger(request.getParameter("type"), null);
    		Application application = null;
    		if (applicationId != null) {
    			ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
    			application = applicationsDataProvider.getApplication(applicationId);
    		}
    		generateDiplomaTypeCombo(application, request, getNacidDataProvider(), null, universityId == null ? new ArrayList<Integer>() : Arrays.asList(universityId), diplomaType);
    		request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_ajax");
	    //}
	}
	public static void generateDiplomaTypeCombo(Application application, HttpServletRequest request, NacidDataProvider nacidDataProvider, Integer activeDiplomaId, Collection<Integer> universityId, Integer type) {
	    boolean migrated = application != null && application.isMigrated();
	    ComboBoxWebModel webmodel = new ComboBoxWebModel(activeDiplomaId == null ? null : activeDiplomaId + "", true);
        DiplomaTypeDataProvider diplomaTypeDataProvider = nacidDataProvider.getDiplomaTypeDataProvider();
	    List<DiplomaType> diplomaTypes = null;


	    if (type == DiplomaType.TYPE_DOCTORATE) {//pri doctorate napravo se zarejda diplomaType-a, koito e selectnat, inache se selectvat vsichki diploma types po university!
	        diplomaTypes = Arrays.asList(diplomaTypeDataProvider.getDiplomaType(activeDiplomaId));
        } else if (universityId != null) {
            diplomaTypes = diplomaTypeDataProvider.getDiplomaTypes(universityId, type);
            if (migrated) {
            	DiplomaType migratedDT = diplomaTypeDataProvider.getDiplomaType(DiplomaType.MIGRATED_DIPLOMA_TYPE_ID);
            	FlatNomenclature educationLevel = application.getTrainingCourse().getEducationLevel();
            	webmodel.addItem(migratedDT.getId() + "", migratedDT.getTitle() + " / " + (educationLevel == null ? "" : educationLevel.getName()) + (migratedDT.isJointDegree() ? " / съвместна" : "") );
            }
	    }
        if (diplomaTypes != null) {
            for (DiplomaType dt:diplomaTypes) {
                FlatNomenclature educationLevel = dt.getEducationLevel();
                OriginalEducationLevel originalEduLevel = dt.getOriginalEducationLevel();
                webmodel.addItem(dt.getId() + "", dt.getTitle() + (Utils.isRecordActiveText(dt.isActive())) + " / " + (educationLevel == null ? "" : educationLevel.getName()) +
                        (originalEduLevel == null ? "" : " / " + originalEduLevel.getName()) + (dt.isJointDegree() ? " / съвместна" : ""));
            }
        }
	    request.setAttribute(WebKeys.DIPLOMA_TYPE_COMBO, webmodel);
	}
	
	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
	    String title = DataConverter.parseString(request.getParameter("title"), "");
	    Boolean jointDegree = DataConverter.parseBoolean(request.getParameter("jointDegree"));

	    String[] universitiesIds = request.getParameterValues("universitiesIds");
	    String[] facultiesIds = request.getParameterValues("facultiesIds");
	    Set<UniversityIdWithFacultyId> universityIdWithFacultyIds = new LinkedHashSet<>();
	    for (int i = 0; i < universitiesIds.length; i++) {
            String id = universitiesIds[i];
            String fId = facultiesIds[i];
            int universityId = DataConverter.parseInt(id, -1);
            if (universityId > 0) {
                Integer facultyId = DataConverter.parseInteger(fId, null);
                universityIdWithFacultyIds.add(new UniversityIdWithFacultyId(universityId, facultyId));
            }
        }
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
	    Integer eduLevelId = DataConverter.parseInteger(request.getParameter("eduLevel"), 0);
        Integer originalEducationLevelId = DataConverter.parseInteger(request.getParameter("originalEduLevel"), null);
	    String visualElementsDescr = DataConverter.parseString(request.getParameter("visualElementsDescr"), "");
	    String protectionElementsDescr = DataConverter.parseString(request.getParameter("protectionElementsDescr"), "");
	    String numberFormatDescr = DataConverter.parseString(request.getParameter("numberFormatDescr"), "");
	    String notes = DataConverter.parseString(request.getParameter("notes"), "");
	    Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
	    Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));

        Integer bolognaCycleId = DataConverter.parseInteger(request.getParameter("bolognaCycleId"), null);
        Integer nationalQualificationsFrameworkId = DataConverter.parseInteger(request.getParameter("nationalQualificationsFrameworkId"), null);
        Integer europeanQualificationsFrameworkId = DataConverter.parseInteger(request.getParameter("europeanQualificationsFrameworkId"), null);
        Integer bolognaCycleAccessId = DataConverter.parseInteger(request.getParameter("bolognaCycleAccessId"), null);
        Integer nationalQualificationsFrameworkAccessId = DataConverter.parseInteger(request.getParameter("nationalQualificationsFrameworkAccessId"), null);
        Integer europeanQualificationsFrameworkAccessId = DataConverter.parseInteger(request.getParameter("europeanQualificationsFrameworkAccessId"), null);
        Integer type = DataConverter.parseInteger(request.getParameter("type"), null);

	    DiplomaTypeDataProvider diplomaTypeDataProvider = getNacidDataProvider().getDiplomaTypeDataProvider();


        JSONObject jsonObj = new JSONObject();
	    boolean error = false;
        if (!universityIdWithFacultyIds.isEmpty() && !title.isEmpty() && title.length() < 256) {
	        try {
	            int newId = diplomaTypeDataProvider.saveDiplomaType(id, universityIdWithFacultyIds, visualElementsDescr, protectionElementsDescr, numberFormatDescr, notes,
	                    dateFrom, dateTo, title, eduLevelId, originalEducationLevelId, jointDegree,
                        bolognaCycleId, nationalQualificationsFrameworkId, europeanQualificationsFrameworkId, bolognaCycleAccessId, nationalQualificationsFrameworkAccessId, europeanQualificationsFrameworkAccessId, type);
                DiplomaType dt = diplomaTypeDataProvider.getDiplomaType(newId);
                fillDiplomaType(dt, jsonObj, getNacidDataProvider());
	        } catch (DiplomaTypeEditException e) {
	            error = true;
                jsonObj.put("errorMessage", e.getMessages());
	        }
	    }
        jsonObj.put("error", error);
        writeToResponse(response, jsonObj.toString());
	}

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {

        Integer id = DataConverter.parseInteger(request.getParameter("id"), 0);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        DiplomaTypeDataProvider diplomaTypeDataProvider = nacidDataProvider.getDiplomaTypeDataProvider();
        DiplomaType diplomaType = diplomaTypeDataProvider.getDiplomaType(id);
        JSONObject jsonObject = new JSONObject();
        fillDiplomaType(diplomaType, jsonObject, nacidDataProvider);
        writeToResponse(response, jsonObject.toString());

    }
    private void fillDiplomaType(DiplomaType diplomaType, JSONObject jsonObject, NacidDataProvider nacidDataProvider) {

        UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        jsonObject.put("id", diplomaType.getId());
        jsonObject.put("title", diplomaType.getTitle());
        jsonObject.put("isJointDegree", diplomaType.isJointDegree());
        Set<UniversityIdWithFacultyId> universityWithFacultyIds = diplomaType.getUniversityWithFacultyIds();
        List<DiplomaTypeUniversityWebModel> wm = new ArrayList<DiplomaTypeUniversityWebModel>();
        if (universityWithFacultyIds != null) {
            for (UniversityIdWithFacultyId uf : universityWithFacultyIds) {
                University u = universityDataProvider.getUniversity(uf.getUniversityId());
                UniversityFaculty f = uf.getFacultyId() == null ? null : universityDataProvider.getUniversityFaculty(uf.getFacultyId());
                wm.add(new DiplomaTypeUniversityWebModel(u, f));
            }
        }

        jsonObject.put("universities", wm);
        jsonObject.put("originalEducationLevel", diplomaType.getOriginalEducationLevelId());
        jsonObject.put("originalEducationLevelName", diplomaType.getOriginalEducationLevel() == null ? "-" : diplomaType.getOriginalEducationLevel().getName());
        jsonObject.put("originalEducationLevelTranslatedName", diplomaType.getOriginalEducationLevel() == null ? "-" : diplomaType.getOriginalEducationLevel().getNameTranslated());
        jsonObject.put("educationLevel", diplomaType.getEducationLevel() == null ? "-" : diplomaType.getEducationLevel().getId());
        jsonObject.put("educationLevelName", diplomaType.getEducationLevel() == null ? "-" : diplomaType.getEducationLevel().getName());

        jsonObject.put("visualElementsDescr", DataConverter.parseString(diplomaType.getVisualElementsDescr(), ""));
        jsonObject.put("protectionElementsDescr", DataConverter.parseString(diplomaType.getProtectionElementsDescr(), ""));
        jsonObject.put("numberFormatDescr", DataConverter.parseString(diplomaType.getNumberFormatDescr(), ""));
        jsonObject.put("notes", DataConverter.parseString(diplomaType.getNotes(), ""));
        jsonObject.put("dateFrom", DataConverter.formatDate(diplomaType.getDateFrom()));
        jsonObject.put("dateTo", DataConverter.formatDate(diplomaType.getDateTo()));
        jsonObject.put("bolognaCycleId", diplomaType.getBolognaCycleId());
        jsonObject.put("nationalQualificationsFrameworkId", diplomaType.getNationalQualificationsFrameworkId());
        jsonObject.put("europeanQualificationsFrameworkId", diplomaType.getEuropeanQualificationsFrameworkId());


        jsonObject.put("bolognaCycleAccessId", diplomaType.getBolognaCycleAccessId());
        jsonObject.put("nationalQualificationsFrameworkAccessId", diplomaType.getNationalQualificationsFrameworkAccessId());
        jsonObject.put("europeanQualificationsFrameworkAccessId", diplomaType.getEuropeanQualificationsFrameworkAccessId());
        jsonObject.put("type", diplomaType.getType());

    }



}
