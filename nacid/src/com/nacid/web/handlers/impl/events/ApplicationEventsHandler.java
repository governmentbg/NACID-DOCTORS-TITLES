package com.nacid.web.handlers.impl.events;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.events.Event;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.events.EventType;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
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
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.BaseAttachmentHandler;
import com.nacid.web.handlers.impl.applications.UniversityValidityHandler;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class ApplicationEventsHandler extends NacidBaseRequestHandler {

    
    private static final String TABLE_PREFIX = "events";
    private static final String NEXT_SCREEN = "application_event_edit";
    
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_TYPE = "Тип";
    private static final String COLUMN_NAME_STATUS = "Статус";
    private static final String COLUMN_NAME_REMINDER_DATE = "Дата на напомняне";
    private static final String COLUMN_NAME_DUE_DATE = "Крайна дата";
    
    public ApplicationEventsHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        String applId = request.getParameter("applID");
        request.setAttribute("applID", applId);
        generateEventTypeCombo(null, request);
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
    }
    
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        
        String applId = request.getParameter("applID");
        request.setAttribute("applID", applId);
        
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        request.setAttribute("id", id);
        Event e = getNacidDataProvider().getEventDataProvider().getEvent(id);
        if(e == null) {
            throw new UnknownRecordException("unknown event id " + id);
        }
        
        generateEventStatusCombo(e.getEventStatus(), request);
        generateEventTypeCombo(e.getEventTypeId(), request);
        
        BaseAttachmentHandler.addEventDatesToRequest(request, e, getNacidDataProvider());
        
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int applId = DataConverter.parseInt(request.getParameter("applID"), 0);
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        Integer eventType = DataConverter.parseInteger(request.getParameter("eventType"), null);
        Integer eventStatus = DataConverter.parseInteger(request.getParameter("eventStatus"), null);
        
        EventDataProvider evDP = getNacidDataProvider().getEventDataProvider();
        Event oldEvent = evDP.getEvent(id);
        if(id != 0 && oldEvent == null) {
            throw new UnknownRecordException("unknown event id " + id);
        }
        
        if(oldEvent == null) {
            id = evDP.recalculateEvent(0, eventType, applId, null, null, null);
        }
        else if(!oldEvent.getEventTypeId().equals(eventType)) {
            id = evDP.recalculateEvent(0, eventType, applId, oldEvent.getDocId(), oldEvent.getDocCategoryId(), oldEvent.getDocumentTypeId());
        }
        else {
            evDP.setStatus(id, eventStatus, oldEvent.getDocCategoryId(), oldEvent.getDocumentTypeId());
        }
        
        addSystemMessageToSession(request, WebKeys.SYSTEM_MESSAGE, 
                new SystemMessageWebModel("Данните са въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        
        HttpSession session = request.getSession();
        session.removeAttribute(WebKeys.TABLE_APPL_EVENTS);
        
        request.setAttribute(WebKeys.APPLICATION_ID, applId);
        try {
            response.sendRedirect(request.getContextPath() 
                    + "/control/application_event/edit?id=" + id + "&applID=" + applId);
        }
        catch(IOException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        getNacidDataProvider().getEventDataProvider().deleteEvent(id);
        try {
            response.sendRedirect((String)request.getSession().getAttribute("backUrlEvents"));
        }
        catch(IOException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        
        ApplicationWebModel appWM = (ApplicationWebModel)request.getAttribute(WebKeys.APPLICATION_WEB_MODEL);
        int applicationId = Integer.parseInt(appWM.getId());
        
        String operation = getOperationName(request);
        if(operation.equals("save")) {
            operation = "edit";
        }
        String group = getGroupName(request);
        String query = "activeForm=4&id=" + applicationId;
        request.getSession().setAttribute("backUrlEvents", 
                request.getContextPath() 
                + "/control" 
                + "/" + group 
                + "/" + operation
                + "?" + query
                + "#eventsmain_table");
        
        
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPL_EVENTS);
        
        //boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request, TABLE_PREFIX);
        boolean reloadTable = true;
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_REMINDER_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DUE_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            
            session.setAttribute(WebKeys.TABLE_APPL_EVENTS, table);
            resetTableData(request, applicationId);

        }

        // TableState settings
        TableState tableState = (TableState) session
                .getAttribute(WebKeys.TABLE_APPL_EVENTS + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request, TABLE_PREFIX);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, TABLE_PREFIX);
            session.setAttribute(WebKeys.TABLE_APPL_EVENTS + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на напомнянията");
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, 
                UniversityValidityHandler.APPLICATION_ID_PARAM, applicationId + "");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName("application_event");
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        request.setAttribute("eventsTableWebModel", webmodel);
        //request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_attachment_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
                .getAttribute(WebKeys.TABLE_APPL_EVENTS + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            
            session.setAttribute(WebKeys.TABLE_APPL_EVENTS
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    
    private void resetTableData(HttpServletRequest request, int applId) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPL_EVENTS);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        EventDataProvider evDP = getNacidDataProvider().getEventDataProvider();
        
        List<Event> events = evDP.getEventsForApplication(applId);
        
        if (events != null) {
            for (Event ev : events) {
                
                
                try {
                    
                    FlatNomenclature eventStatus = getNacidDataProvider().getNomenclaturesDataProvider()
                        .getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS, ev.getEventStatus());
                    String eventType = getNacidDataProvider().getEventTypeDataProvider()
                        .getEventType(ev.getEventTypeId()).getEventName();
                    
                    table.addRow(ev.getId(), eventType, 
                            eventStatus == null ? "" : eventStatus.getName(),
                                    ev.getReminderDate(), ev.getDueDate());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (CellCreationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void generateEventStatusCombo(Integer activeId, HttpServletRequest request) {
        List<? extends FlatNomenclature> flatNomeclatures = null;

        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
            .getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS,
                    null, null);

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "");
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                combobox.addItem(s.getId() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
            request.setAttribute("eventStatus", combobox);
        }
    }
    
    private void generateEventTypeCombo(Integer activeId, HttpServletRequest request) {
        
        List<EventType> list = getNacidDataProvider().getEventTypeDataProvider().getEventTypes();

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (list != null) {
            for (EventType s : list) {
                combobox.addItem(s.getId() + "", s.getEventName());
            }
            request.setAttribute("eventType", combobox);
        }
    }
}
