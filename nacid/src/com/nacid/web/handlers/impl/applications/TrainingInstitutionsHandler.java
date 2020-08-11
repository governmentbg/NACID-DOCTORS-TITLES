package com.nacid.web.handlers.impl.applications;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingInstitution;
import com.nacid.bl.applications.TrainingInstitutionDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.TrainingInstitutionWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class TrainingInstitutionsHandler extends NacidBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_COUNTRY = "Държава";
    private static final String COLUMN_NAME_CITY = "Град";
    private static final String COLUMN_NAME_PHONE = "Телефон";
    private static final String COLUMN_NAME_DATEFROM = "От дата";
    private static final String COLUMN_NAME_DATETO = "До дата";

    private static final String EDIT_SCREEN = "training_institution_edit";
    
    public TrainingInstitutionsHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY,
                nDP, true, request, "country", null, false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY,
                nDP, true, request, "univCountry", null, true);
        
        request.setAttribute("universityCombo", new ComboBoxWebModel("-", true));
        
        request.setAttribute(WebKeys.TRAINING_INSTITUTION_WEB_MODEL, 
                new TrainingInstitutionWebModel(null, getNacidDataProvider()));
        
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
    }
    
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        if(id <= 0) {
            throw new UnknownRecordException("no training institution with id " + id);
        }
        loadTrainingInstitution(request, response, nacidDataProvider, id);
        
    }
    private static void loadTrainingInstitution(HttpServletRequest request, HttpServletResponse response, NacidDataProvider nacidDataProvider, int institutionId) {
    	
        TrainingInstitutionDataProvider tiDP = nacidDataProvider.getTrainingInstitutionDataProvider();
        
        TrainingInstitution ti = tiDP.selectTrainingInstitution(institutionId);
        if(ti == null) {
            throw new UnknownRecordException("no training institution with id " + institutionId);
        }
        
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        ComboBoxUtils.generateNomenclaturesComboBox(ti.getCountryId(), NomenclaturesDataProvider.NOMENCLATURE_COUNTRY,
                nDP, true, request, "country", null, false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY,
                nDP, true, request, "univCountry", null, true);
        
        request.setAttribute("universityCombo", new ComboBoxWebModel("-", true));
        
        request.setAttribute(WebKeys.TRAINING_INSTITUTION_WEB_MODEL, 
                new TrainingInstitutionWebModel(ti, nacidDataProvider));
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
    }
    
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
        	throw new UnknownRecordException("form not submitted...");
        }
    	NacidDataProvider nacidDataProvider = getNacidDataProvider();
    	TrainingInstitutionDataProvider trainingInstitutionDataProvider = nacidDataProvider.getTrainingInstitutionDataProvider();
    	int id = DataConverter.parseInt(request.getParameter("id"), 0);
    	
    	String name = request.getParameter("name");
    	int countryId = DataConverter.parseInt(request.getParameter("country"), 0);
    	String city = request.getParameter("city");
    	String pcode = request.getParameter("pcode");
    	String addrDetails = request.getParameter("addrDetails");
    	String phone = request.getParameter("phone");
    	Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
    	Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
    	String stringUnivIds = DataConverter.parseString(request.getParameter("universityIds"), null);
    	String[] ids = stringUnivIds == null ? null : stringUnivIds.split(";");
    	int[] result = null;
    	if (ids != null && ids.length > 0) {
    		result = new int[ids.length];
    		for (int i = 0; i < ids.length; i++ ) {
    			result[i] = DataConverter.parseInt(ids[i], 0);
    		}
    	}
    	
    	id = trainingInstitutionDataProvider.saveTrainingInstitution(id, name, countryId, city, pcode, addrDetails, phone, dateFrom, dateTo, result);
    	loadTrainingInstitution(request, response, nacidDataProvider, id);
    	request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха записани в базата.", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
    	request.getSession().removeAttribute(WebKeys.TABLE_TRAINING_INSTITUTION);
    }
    
    @Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int institutionId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (institutionId <= 0) {
            throw new UnknownRecordException("Unknown competent institution Id:" + institutionId);
        }
        TrainingInstitutionDataProvider tiDP = getNacidDataProvider().getTrainingInstitutionDataProvider();
        tiDP.deactivateTrainingInstitution(institutionId);

        request.getSession().removeAttribute(WebKeys.TABLE_TRAINING_INSTITUTION);
        handleList(request, response);
    }
    
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_TRAINING_INSTITUTION);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_CITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PHONE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATEFROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATETO, CellValueDef.CELL_VALUE_TYPE_DATE);
            
            session.setAttribute(WebKeys.TABLE_TRAINING_INSTITUTION, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_TRAINING_INSTITUTION + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            
            session.setAttribute(WebKeys.TABLE_TRAINING_INSTITUTION + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на институции, провеждащи обучението");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_TRAINING_INSTITUTION + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            
            session.setAttribute(WebKeys.TABLE_TRAINING_INSTITUTION + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    
    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_TRAINING_INSTITUTION);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        TrainingInstitutionDataProvider tiDP = nacidDataProvider.getTrainingInstitutionDataProvider();
        List<TrainingInstitution> tis = tiDP.selectTrainingInstitutions();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();

        if (tis != null) {
            for (TrainingInstitution i : tis) {
                try {
                    Country c = nomDP.getCountry(i.getCountryId());
                    table.addRow(i.getId(), i.getName(), c.getName(), i.getCity(), i.getPhone(), 
                            i.getDateFrom(), i.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
