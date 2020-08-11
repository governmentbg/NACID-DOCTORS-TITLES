package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.HandlerException;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.ApplicationUniversityValidityWebModel;
import com.nacid.web.model.applications.UniversityExaminationWebModel;
import com.nacid.web.model.applications.UniversityValidityWebModel;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.CellFormatter;
import com.nacid.web.model.table.TableWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class UniversityValidityHandler extends NacidBaseRequestHandler {
    // Ako se promeni podredbata na pyrvite 2 koloni, shte trqbva da se promenqt
    // i javascript-ovete za manipulirane na tablicata v app_form4
    private static final String COLUMN_NAME_SELECTED = "маркиран";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_DATE = "дата";
    private static final String COLUMN_NAME_RECOGNIZED = "признат/непризнат";
    private static final String COLUMN_NAME_NOTES = "Забележки";

    public static final String APPLICATION_ID_PARAM = "applID";
    public static final String APPLICATION_OPERATION_PARAM = "applOper";
    public static final String APPLICATION_GROUP_PARAM = "applGroup";
    
    public static final String TABLE_PREFIX = "univ_validity";
    
    ServletContext servletContext;
    public UniversityValidityHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        int universityId = DataConverter.parseInt(request.getParameter("universityId"), -1);
        if (universityId <= 0) {
            throw new UnknownRecordException("Unknown university ID:" + universityId);
        }

        int appId = Integer.parseInt(request.getParameter(APPLICATION_ID_PARAM));
        String appOper = request.getParameter(APPLICATION_OPERATION_PARAM);
        String appGroup = request.getParameter(APPLICATION_GROUP_PARAM);
        if ("save".equals(appGroup)) {
        	appGroup = "edit";
        }
        University university = getUniversity(universityId);
	    UniversityValidityWebModel wm = new UniversityValidityWebModel(
	            getNacidDataProvider(), null, university, appId, appOper, appGroup);
        request.setAttribute(WebKeys.UNIVERSITY_VALIDITY_WEB_MODEL, wm);
        generateInstitutionsCombo(getNacidDataProvider(), request, university);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "newInstitutionCountry", null, true);
        request.setAttribute(WebKeys.NEXT_SCREEN, "university_validity_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id <= 0) {
            throw new UnknownRecordException("Unknown university edit ID:" + id);
        }
        int appId = Integer.parseInt(request.getParameter(APPLICATION_ID_PARAM));
        String appOper = request.getParameter(APPLICATION_OPERATION_PARAM);
        String appGroup = request.getParameter(APPLICATION_GROUP_PARAM);
        if ("save".equals(appGroup)) {
        	appGroup = "edit";
        }
        UniversityValidityDataProvider unvValDP = getNacidDataProvider().getUniversityValidityDataProvider();
        UniversityValidity unvVal = unvValDP.getUniversityValidity(id);
        if (unvVal == null) {
            throw new UnknownRecordException("Unknown university edit ID:" + id);
        }

        University university = getUniversity(unvVal.getUniversityId());
	    UniversityValidityWebModel wm = new UniversityValidityWebModel(
	            getNacidDataProvider(), unvVal, 
	            university, appId, appOper, appGroup);
	    request.setAttribute(WebKeys.UNIVERSITY_VALIDITY_WEB_MODEL, wm);
	    generateInstitutionsCombo(getNacidDataProvider(), request, university);
	    request.setAttribute(WebKeys.NEXT_SCREEN, "university_validity_edit");
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "newInstitutionCountry", null, true);
	    new UniExamAttachmentHandler(servletContext).handleList(request, response);
	}
	
	@Override
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
	    super.handleView(request, response);
	    new UniExamAttachmentHandler(servletContext).handleList(request, response);
	}
	
	private University getUniversity(int universityId) {
	    UniversityDataProvider unDP = getNacidDataProvider().getUniversityDataProvider();
	    University u = unDP.getUniversity(universityId);
	    if(u != null) {
	        return u;
	    }
	    else {
	        throw new UnknownRecordException("Unknown university ID:" + universityId);
	    }
	}

	public static void generateUniversityValiditiesForApplication(Application application, NacidDataProvider nacidDataProvider, HttpServletRequest request, HttpServletResponse response) {
	    Table table;

        if (true) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_SELECTED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_RECOGNIZED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            table.addColumnHeader(COLUMN_NAME_NOTES, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.setUniqueColmn(COLUMN_NAME_ID);
        }
        
        UniversityValidityDataProvider universityValidityDataProvider = nacidDataProvider.getUniversityValidityDataProvider();
        if (application == null) {
            return;
        }
        TrainingCourse trainingCourse = application.getTrainingCourse();
        
        
        List<? extends UniversityWithFaculty> universities = trainingCourse.getUniversityWithFaculties();
        if (universities != null) {
            //int i = 0;
            List<ApplicationUniversityValidityWebModel> webmodel = new ArrayList<ApplicationUniversityValidityWebModel>();
        	for (UniversityWithFaculty uf:universities) {
        	    University university = uf.getUniversity();
        		table.emtyTableData();
        		List<UniversityValidity> universityValidies = universityValidityDataProvider.getUniversityValiditiesByUniversity(university.getId());
        		UniversityExamination universityExamination = trainingCourse.getUniversityExaminationByUniversity(university.getId());
        		if (universityValidies != null && universityValidies.size() > 0) {
                	for (UniversityValidity uv : universityValidies) {
                    	try {
                            table.addRow((universityExamination != null && universityExamination.getUniversityValidityId() == uv.getId()), uv.getId(), 
                                    uv.getExaminationDate(), uv.isRecognized(), uv.getNotes());
                        } catch (CellCreationException e) {
                            throw Utils.logException(e);
                        }
                    }
                }
                TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, TABLE_PREFIX);
                tableState.setOrderCriteria(COLUMN_NAME_DATE, false);

                // TableWebModel SETTINGS
                TableWebModel tableWebmodel = new TableWebModel(null);
                tableWebmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, APPLICATION_ID_PARAM, application.getId() + "");
                tableWebmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, APPLICATION_OPERATION_PARAM, getOperationName(request));
                tableWebmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, APPLICATION_GROUP_PARAM, getGroupName(request));
                tableWebmodel.setColumnFormatter(COLUMN_NAME_SELECTED, CellFormatter.BOOLEAN_AS_RADIO_FORMATTER);
                tableWebmodel.setGroupName("university_validity");
                tableWebmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
                tableWebmodel.insertTableData(table, tableState);
                webmodel.add(new ApplicationUniversityValidityWebModel(tableWebmodel, universityExamination == null ? null : new UniversityExaminationWebModel(universityExamination), new UniversityWebModel(university)));
                
        	}
        	request.setAttribute(WebKeys.APPLICATION_UNIVERSITY_VALIDITY, webmodel);
        	
        
        }

        
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }

        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        String examinationDateString = request.getParameter("examinationDate");
        Date examinationDate = null;
        if (examinationDateString != null) {
            java.util.Date d = DataConverter.parseDate(examinationDateString);
            if (d != null)
                examinationDate = new Date(d.getTime());
        }
        
        int appId = Integer.parseInt(request.getParameter(APPLICATION_ID_PARAM));
        String appOper = request.getParameter(APPLICATION_OPERATION_PARAM);
        String appGroup = request.getParameter(APPLICATION_GROUP_PARAM);
        if ("save".equals(appGroup)) {
        	appGroup = "edit";
        }
	    int universityId = DataConverter.parseInt(request.getParameter("universityId"), 0);
	    boolean recognized = DataConverter.parseBoolean(request.getParameter("recognized"));
	    boolean communicated = DataConverter.parseBoolean(request.getParameter("communicated"));
	    Integer trainingLocation = DataConverter.parseInteger(request.getParameter("trainingLocation"), null);
	    boolean hasJoinedDegrees = DataConverter.parseBoolean(request.getParameter("hasJoinedDegrees"));
	    String notes = request.getParameter("notes");
	    
	    String[] trainingFormStrings = request.getParameterValues("trainingForm");
	    ArrayList<Object> trainingForm = new ArrayList<Object>();
	    if (trainingFormStrings != null) {
            for (String s : trainingFormStrings) {
                trainingForm.add(new Integer(s.trim()));
            }
        }
	    if (DataConverter.parseBoolean(request.getParameter("trainingFormOther"))) {
            String trainingFormOtherText = request.getParameter("trainingFormOtherText");
            trainingForm.add(trainingFormOtherText);
        }
	    
	    

        List<Integer> selectedInstitutions = RequestParametersUtils.convertRequestParameterToIntegerList(request.getParameter("institution"));
        Integer selectedInstitution = DataConverter.parseInteger(request.getParameter("institutions"), null);
        if (selectedInstitution != null) {
            if (selectedInstitutions == null) {
                selectedInstitutions = new ArrayList<Integer>();
            }
            if (!selectedInstitutions.contains(selectedInstitution)) {
                selectedInstitutions.add(selectedInstitution);
            }
        }

        int userId = getLoggedUser(request, response).getUserId();
	    
	    UniversityValidityDataProvider unvValDP = getNacidDataProvider().getUniversityValidityDataProvider();
	    id = unvValDP.saveUniversityValidity(id, universityId, userId, 
	            examinationDate, communicated, recognized, 
	            notes, trainingLocation, hasJoinedDegrees, 
	            trainingForm, selectedInstitutions);
	    
	    UniversityValidity unvVal = unvValDP.getUniversityValidity(id);
        if (unvVal == null) {
            throw new UnknownRecordException("Unknown university edit ID:" + id);
        }

        
        request.setAttribute("universityValiditySystemMessage",
                new SystemMessageWebModel("Данните бяха въведени в базата",
                        SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        University university = getUniversity(unvVal.getUniversityId());
        UniversityValidityWebModel wm = new UniversityValidityWebModel(
                getNacidDataProvider(), unvVal, 
                university, appId, appOper, appGroup);
        request.setAttribute(WebKeys.UNIVERSITY_VALIDITY_WEB_MODEL, wm);
        generateInstitutionsCombo(getNacidDataProvider(), request, university);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "newInstitutionCountry", null, true);

        request.setAttribute(WebKeys.NEXT_SCREEN, "university_validity_edit");
        
        new UniExamAttachmentHandler(servletContext).handleList(request, response);
    }

    @Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        throw new HandlerException("Handler: handleDelete: Method not yet written.");
    }

    public static ComboBoxWebModel generateInstitutionsCombo(NacidDataProvider nacidDataProvider, Integer countryId) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(null, true);
        CompetentInstitutionDataProvider competentInstitutionDataProvider = nacidDataProvider.getCompetentInstitutionDataProvider();
        List<CompetentInstitution> list = competentInstitutionDataProvider.getCompetentInstitutions(countryId, true);
        if (list != null) {
            for (CompetentInstitution c : list) {
                combobox.addItem("" + c.getId(), c.getName());
            }
        }
        return combobox;

    }
    private static void generateInstitutionsCombo(NacidDataProvider nacidDataProvider, HttpServletRequest request, University university) {
        ComboBoxWebModel combobox =generateInstitutionsCombo(nacidDataProvider, university.getCountryId());
        request.setAttribute(WebKeys.INSTITUTIONS_COMBO, combobox);
    }
}
