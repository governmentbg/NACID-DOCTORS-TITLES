package com.nacid.web.handlers.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.ApplicationForList;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.comission.CommissionCalendarHeaderWebModel;
import com.nacid.web.model.comission.CommissionCalendarWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.CellFormatter;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;






public class CommissionCalendarHandler extends NacidBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "Заседание";
    private final static String COLUMN_NAME_SESSION_NUMBER = "Номер на заседанието";
    private final static String COLUMN_NAME_DATE = "Дата и час";
    private final static String COLUMN_NAME_STATUS = "Статус на заседанието";
    
    private static final String FILTER_NAME_STATUS = "statusFilter";
    
    public CommissionCalendarHandler(ServletContext servletContext) {

        super(servletContext);
    }
    @Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS , nDP, true, request, WebKeys.SESSION_STATUS_COMBO, NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);
		request.setAttribute(WebKeys.NEXT_SCREEN, "commission_calendar_edit");
	}


	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int calendarId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (calendarId <= 0) {
			throw new UnknownRecordException("Unknown Commission Calendar ID:" + calendarId);
		}
		generateCommissionCalendar(calendarId, request, getOperationName(request));
		
	}
	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
			new HomePageHandler(request.getSession().getServletContext())
					.processRequest(request, response);
			return;
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
		ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		ComissionMemberDataProvider comissionMemberDataProvider = nacidDataProvider.getComissionMemberDataProvider();
		int id = DataConverter.parseInt(request.getParameter("id"), 0);
		CommissionCalendar oldCalendar = id == 0 ? null : commissionCalendarDataProvider.getCommissionCalendar(id);
		if  (id != 0 && oldCalendar == null) {
			throw new UnknownRecordException("Unknown commission calendar for edit id=" + id);
		}
		
		String notes = request.getParameter("notes");
		int sessionStatus = DataConverter.parseInt(request.getParameter("session_status"), 0);
		Date dateTime = DataConverter.parseDateTime(request.getParameter("dateTime"), false) ;
		List<CommissionCalendar> calendars = commissionCalendarDataProvider.getCommissionCalendarsByDate(dateTime);
		if (calendars != null) {
			/**
			 * Ako ima ve4e vyveden zapis sys sy6tata data/4as
			 *  - ako se dobavq nov zapis => problem
			 *  - ako se redaktira star zapis i oldCalendar.getId() != zapisa v bazata s tozi data/4as/ => problem 
			 */ 
			if ((oldCalendar == null || (oldCalendar.getId() != calendars.get(0).getId()))) {
				ComboBoxUtils.generateNomenclaturesComboBox(sessionStatus, NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS , nacidDataProvider.getNomenclaturesDataProvider(), true, request, WebKeys.SESSION_STATUS_COMBO, NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);
				request.setAttribute(WebKeys.COMMISSION_CALENDAR_WEB_MODEL, new CommissionCalendarWebModel(id, notes, dateTime));
				request.setAttribute(WebKeys.COMMISSION_CALENDAR_HEADER_WEB_MODEL, new CommissionCalendarHeaderWebModel(id, UserOperationsUtils.getOperationName(UserOperationsUtils.OPERATION_LEVEL_EDIT), CommissionCalendarHeaderWebModel.ACTIVE_FORM_CALENDAR_EDIT));
				request.setAttribute(WebKeys.NEXT_SCREEN, "commission_calendar_edit");
				request.setAttribute(WebKeys.SYSTEM_MESSAGE,new SystemMessageWebModel(MessagesBundle.getMessagesBundle().getValue("exists_calendar_same_datetime"), SystemMessageWebModel.MESSAGE_TYPE_ERROR));
				return;
			}
				
		}
		
		
		//int sessionNumber = DataConverter.parseInt(request.getParameter("session_number"), 0);
		//Posledno(ili predposledno, zatova ne iztrivam koda, otnasqsht se za sessionNumber) - za sessionNumber shte se zapisva id-to
		int newId = commissionCalendarDataProvider.saveCommissionCalendar(id, dateTime, notes, sessionStatus/*, sessionNumber*/);
		if (id == 0) {
			/**Adding Applications with status APPLICATION_AUTHENTIC_STATUS_CODE to the given calendar*/ 
			List<ApplicationForList> applications = applicationsDataProvider.getApplicationsForList(ApplicationType.RUDI_APPLICATION_TYPE, ApplicationStatus.APPLICATION_AUTHENTIC_STATUS_CODE, false);
			List<Integer> applicationIds = null;
			if (applications != null) {
				applicationIds = new ArrayList<Integer>();
				for (ApplicationForList a:applications) {
					applicationIds.add(a.getId());
				}
			}
			commissionCalendarDataProvider.setApplicationsToCalendar(newId, applicationIds);
			/**End of adding applications */
			
			/** Adding all active commission members to the given calendar*/
			List<ComissionMember> commissionMembers = comissionMemberDataProvider.getComissionMembers(true, null);
			if (commissionMembers != null) {
				for (ComissionMember cm:commissionMembers) {
					commissionCalendarDataProvider.addCommissionMemberToCalendar(newId, cm.getId(), false, false, null);
				}
			}
			/** end of adding commission members*/
		}
		
		
		
		
		
		
		
		request.setAttribute(WebKeys.SYSTEM_MESSAGE,new SystemMessageWebModel(MessagesBundle.getMessagesBundle().getValue("data_saved"), SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
		generateCommissionCalendar(newId, request, UserOperationsUtils.getOperationName(UserOperationsUtils.OPERATION_LEVEL_EDIT));
		
		request.getSession().removeAttribute(WebKeys.TABLE_COMMISSION_CALENDAR);
		/**
		 * 
[14:46:57] Irina Vassileva: можеш ли да го сложиш правеното от иво в коментар
[14:47:10] Irina Vassileva: да го има все пак, акмо им хрумне друго

....
[14:53:59] Irina Vassileva: т.е. стана:
[14:54:26] Irina Vassileva: уведомяват се всички от горната таблица с емейли, след натискане на уведоми
[14:54:40] Irina Vassileva: уведоми може да се натиска повече от един път
[14:54:59] Irina Vassileva: т.е. - ако се промени - да им се прати пак емайл
[14:55:08] Irina Vassileva: така ли ?
[14:55:49] Георги Георгиев: да
[14:55:57] Irina Vassileva: ок
[14:56:03] Георги Георгиев: уведоми праща мейли на всички хора с мейли
[14:56:10] Георги Георгиев: може да се натиска когато и както си искаш
		 */
		if (id != 0) {
			//CommissionCalendarUtils.notifyMembersForSessionChange(newId, nacidDataProvider);	
		}
		
	}
	private void generateCommissionCalendar(int id, HttpServletRequest request, String action) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
		NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
		CommissionCalendar commissionCalendar = commissionCalendarDataProvider.getCommissionCalendar(id);
		if (commissionCalendar == null) {
			throw new UnknownRecordException("Unknown Commission Calendar ID:" + id);
		}
		ComboBoxUtils.generateNomenclaturesComboBox(commissionCalendar.getSessionStatusId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS , nDP, true, request, WebKeys.SESSION_STATUS_COMBO, NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);
		request.setAttribute(WebKeys.COMMISSION_CALENDAR_WEB_MODEL, new CommissionCalendarWebModel(commissionCalendar));
		request.setAttribute(WebKeys.COMMISSION_CALENDAR_HEADER_WEB_MODEL, new CommissionCalendarHeaderWebModel(id, action, CommissionCalendarHeaderWebModel.ACTIVE_FORM_CALENDAR_EDIT));
		request.setAttribute(WebKeys.NEXT_SCREEN, "commission_calendar_edit");
		
		generateCommissionCalendarHeaderMessage(request, commissionCalendar);
	}
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_COMMISSION_CALENDAR);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_SESSION_NUMBER, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            session.setAttribute(WebKeys.TABLE_COMMISSION_CALENDAR, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session
                .getAttribute(WebKeys.TABLE_COMMISSION_CALENDAR + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_STATUS, request, table, tableState);           
            session.setAttribute(WebKeys.TABLE_COMMISSION_CALENDAR + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Календар на комисията");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.setColumnFormatter(COLUMN_NAME_DATE, CellFormatter.DATE_TIME_FORMATTER);
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
                .getAttribute(WebKeys.TABLE_COMMISSION_CALENDAR + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(generateStatusFilterComboBox(request.getParameter(FILTER_NAME_STATUS), getNacidDataProvider().getNomenclaturesDataProvider(), request));
            
            session.setAttribute(WebKeys.TABLE_COMMISSION_CALENDAR
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }
    
    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_COMMISSION_CALENDAR);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        
        CommissionCalendarDataProvider commCalDP = nacidDataProvider.getCommissionCalendarDataProvider();
        List<CommissionCalendar> calendars = commCalDP.getCommissionCalendarsByStatus(0);
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
                
        if (calendars != null) {
            for (CommissionCalendar cc : calendars) {
                try {
                    String sessionStatus = nomDP.getFlatNomenclature(
                            NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS, 
                                cc.getSessionStatusId()).getName();
                    table.addRow(cc.getId(), cc.getSessionNumber(), cc.getDateAndTime(), sessionStatus);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    
    private static ComboBoxFilterWebModel generateStatusFilterComboBox(String activeCountryName,
            NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryName, true);

        List<FlatNomenclature> statuses = nomenclaturesDataProvider.getFlatNomenclatures(
                NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        if (statuses != null) {
            for (FlatNomenclature s : statuses) {
                combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_STATUS, COLUMN_NAME_STATUS);
        res.setElementClass("brd");
        return res;
    }
    
    static void generateCommissionCalendarHeaderMessage(HttpServletRequest request, CommissionCalendar calendar) {
    	String message = "";
    	if (calendar != null) {
    		message += " Заседание № " + calendar.getSessionNumber() + "/" + DataConverter.formatDateTime(calendar.getDateAndTime(), false);
    	}
    	request.setAttribute("commission_calendar_header", message);
    }
}
