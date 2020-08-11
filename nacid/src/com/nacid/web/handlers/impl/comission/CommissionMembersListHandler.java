package com.nacid.web.handlers.impl.comission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.comision.CommissionParticipation;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.comission.CommissionCalendarHeaderWebModel;
import com.nacid.web.model.comission.CommissionParticipationBaseDataWebModel;
import com.nacid.web.model.comission.CommissionParticipationWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class CommissionMembersListHandler extends NacidBaseRequestHandler {

	private static final String COLUMN_NAME_ID ="Id";
	private static final String COLUMN_NAME_NAME ="Име";  	
	private static final String COLUMN_NAME_FNAME ="Фамилия";
	//private static final String COLUMN_NAME_PHONE ="Телефон/и";
	//private static final String COLUMN_NAME_GSM ="GSM";
	private static final String COLUMN_NAME_PROF_GROUP ="Професионално направление";
	//private static final String COLUMN_NAME_EMAIL ="Електронна поща";
	private static final String COLUMN_NAME_COMISSION_POS ="Позиция в комисията";

	/**
	 * shte pomni unikalnite stojnosti na redovete v tablicata, koito ne trqbva da se pokazvat v dolnata tablica 
	 * (koqto sydyrja spisyk s vsi4ki chlenove na komisiqta ot koito moje da se izbira) 
	 */
	private Set<Object> uncheckedIds = new HashSet<Object>();
	public CommissionMembersListHandler(ServletContext servletContext) {
		super(servletContext);
	}
	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), -1);
		if (calendarId <= 0) {
			throw new UnknownRecordException("Unknown Commission Calendar ID:" + calendarId);
		}
		generateCommissionMembersListWebModels( calendarId,  request,  response, getOperationName(request));
		
		CommissionCalendarHandler.generateCommissionCalendarHeaderMessage(request, getNacidDataProvider().getCommissionCalendarDataProvider().getCommissionCalendar(calendarId));
	}
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
			new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
			return;
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
		int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), 0);
		String strCommissionMemberIds = request.getParameter("commission_member_ids");
		commissionCalendarDataProvider.deleteCommissionMembersList(calendarId);
		if (!"".equals(strCommissionMemberIds)) {
	    	for(String s : strCommissionMemberIds.split(";")) {
		        Integer id =DataConverter.parseInteger(s, null);
		        if (id != null) {
		        	boolean notified = DataConverter.parseBoolean(request.getParameter("member_notified_" + id));
		        	boolean participated = notified && DataConverter.parseBoolean(request.getParameter("member_participated_" + id));
		        	commissionCalendarDataProvider.addCommissionMemberToCalendar(calendarId, id, notified, participated, null);
		        	
		        }
	    		
		    }	
	    }
		generateCommissionMembersListWebModels( calendarId,  request,  response,  UserOperationsUtils.getOperationName(UserOperationsUtils.OPERATION_LEVEL_EDIT));
		request.setAttribute("commissionMembersSaved", new SystemMessageWebModel("Данните бяха записани в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
	}
	private void generateCommissionMembersListWebModels(int calendarId, HttpServletRequest request, HttpServletResponse response, String action){
		//Start of generating table data with selected applications for given calendar id
		CommissionCalendarDataProvider commissionCalendarDataProvider = getNacidDataProvider().getCommissionCalendarDataProvider();
		List<CommissionParticipation> comissionMembers = commissionCalendarDataProvider.getCommissionParticipations(calendarId);
		if (comissionMembers != null) {
			List<CommissionParticipationWebModel> cpwm = new ArrayList<CommissionParticipationWebModel>();
			for (CommissionParticipation cp:comissionMembers) {
				cpwm.add(new CommissionParticipationWebModel(cp));
				//pri zarejdane na gornata tablica, slaga ID-tata na commissionMembers v promenlivata uncheckedIds
				//celta e pri zarejdane na dolnata tablica zapisite s tezi ID-ta da ne figurirat tam
                uncheckedIds.add(cp.getCommissionMemberId());
			}
			request.setAttribute(WebKeys.COMMISSION_PARTICIPATION_WEB_MODEL, cpwm);
		}
		//End of generating table data with selected applications for given calendar id
		
		request.setAttribute(WebKeys.COMMISSION_PARTICIPATION_BASE_WEB_MODEL, new CommissionParticipationBaseDataWebModel(calendarId,comissionMembers));
		
		request.setAttribute(WebKeys.COMMISSION_CALENDAR_HEADER_WEB_MODEL, new CommissionCalendarHeaderWebModel(calendarId, action, CommissionCalendarHeaderWebModel.ACTIVE_FORM_CALENDAR_COMMISSION_MEMBERS_EDIT));
		
		handleList(request, response);
		/**
		 * Tyi kato handleList() slaga za next_screen commission_members_list_table, za da moje kato se izvika s AJAX commission_applications/list
		 * da moje ajax-a da vyrne tablicata bez headera i footera. Poradi tazi pri4ina se nalaga handleList() da zaredi cqlata stranica s header/footer i druga informaciq 
		 */
		request.setAttribute(WebKeys.NEXT_SCREEN, "commission_members_list");
	}
	
	//Tozi method se polzva za 2 neshta - pyrvo za generirane na dolnata tablica v stranicata http://localhost:8080/nacid/control/commission_members_list/edit/...
	//Sled tova za ajax-a, koito pregenerira samo dolnata tablica
	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_COMMISSION_MEMBERS_LIST);

		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		if (reloadTable || table == null) {
			table = createEmptyTable();
			session.setAttribute(WebKeys.TABLE_COMMISSION_MEMBERS_LIST, table);
			ComissionMemberDataProvider comissionMemberDataProvider = getNacidDataProvider().getComissionMemberDataProvider();
			resetTableData(table, comissionMemberDataProvider.getComissionMembers(true, null));

		}

		// TableState settings
		TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
		//Dobavq ID-tata na redovete ot gornata tablica, koito ne trqbva da se vijdat v dolnata
		CommissionApplicationsHandler.addUncheckedRowsIdsToTableState(request, table, tableState, uncheckedIds);
		 
		

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel(null);

		webmodel.setGroupName("app_comission");
		webmodel.insertTableData(table, tableState);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
		webmodel.setViewOpenInNewWindow(true);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = new FiltersWebModel();
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

	}
	private static Table createEmptyTable() {
		TableFactory tableFactory = TableFactory.getInstance();
		Table table = tableFactory.createTable();
		table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
		table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_FNAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		
		//table.addColumnHeader(COLUMN_NAME_GSM, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_PROF_GROUP, CellValueDef.CELL_VALUE_TYPE_STRING);
		//table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_COMISSION_POS, CellValueDef.CELL_VALUE_TYPE_STRING);
		//table.addColumnHeader(COLUMN_NAME_PHONE, CellValueDef.CELL_VALUE_TYPE_STRING);
		return table;
	}
	private void resetTableData(Table table, List<ComissionMember> commissionMembers) {
		if (table == null) {
			return;
		}
		table.emtyTableData();
		if (commissionMembers != null) {
			for (ComissionMember cm : commissionMembers) {
                try {
                    String pgName = "";
                    if (cm.getProfGroup() != null && cm.getProfGroup().getName() != null) {
                        pgName = cm.getProfGroup().getName();
                    }
                    table.addRow(cm.getId(), cm.getFname(), cm.getLname()/*, cm.getGsm()*/, pgName, /*cm.getEmail(),*/ cm.getComissionPos()
                            .getName()/*, cm.getPhone()*/);
                   
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
		}
	}
}
