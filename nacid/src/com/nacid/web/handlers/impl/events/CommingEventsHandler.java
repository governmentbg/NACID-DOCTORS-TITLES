package com.nacid.web.handlers.impl.events;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.events.Event;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.events.EventType;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.DocumentType;
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
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class CommingEventsHandler extends NacidBaseRequestHandler {

    
    private static final String NEXT_SCREEN = "comming_event_edit";
    
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_DOCFLOW_NUM = "Деловоден номер на заявлението";
    private static final String COLUMN_NAME_DOC_TYPE = "Тип документ";
    private static final String COLUMN_NAME_TYPE = "Тип";
    private static final String COLUMN_NAME_STATUS = "Статус";
    private static final String COLUMN_NAME_REMINDER_DATE = "Дата за напомняне";
    private static final String COLUMN_NAME_DUE_DATE = "Крайна дата";
    
    private static final String FILTER_NAME_EVENT_TYPE = "eventTypeFilter";
    public CommingEventsHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        
        
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
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        Integer eventType = DataConverter.parseInteger(request.getParameter("eventType"), null);
        Integer eventStatus = DataConverter.parseInteger(request.getParameter("eventStatus"), null);
        
        EventDataProvider evDP = getNacidDataProvider().getEventDataProvider();
        Event oldEvent = evDP.getEvent(id);
        if(oldEvent == null) {
            throw new UnknownRecordException("unknown event id " + id);
        }
        
        if(!oldEvent.getEventTypeId().equals(eventType)) {
            id = evDP.recalculateEvent(0, eventType, oldEvent.getApplicationId(), oldEvent.getDocId(), oldEvent.getDocCategoryId(), oldEvent.getDocumentTypeId());
        }
        else {
            evDP.setStatus(id, eventStatus, oldEvent.getDocCategoryId(), oldEvent.getDocumentTypeId());
        }
        
        addSystemMessageToSession(request, WebKeys.SYSTEM_MESSAGE, 
                new SystemMessageWebModel("Данните са въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        
        HttpSession session = request.getSession();
        session.removeAttribute(WebKeys.TABLE_COMMING_EVENTS);
        
        
        try {
            response.sendRedirect(request.getContextPath() 
                    + "/control/comming_event/edit?id=" + id );
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
            response.sendRedirect(request.getContextPath() 
                    + "/control/comming_event/list?getLastTableState=1");
        }
        catch(IOException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        
        
        
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_COMMING_EVENTS);
        
        //boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request, TABLE_PREFIX);
        boolean reloadTable = true;
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DOCFLOW_NUM, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DOC_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_REMINDER_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DUE_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            
            session.setAttribute(WebKeys.TABLE_COMMING_EVENTS, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session
                .getAttribute(WebKeys.TABLE_COMMING_EVENTS + WebKeys.TABLE_STATE);
        
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_COMMING_EVENTS + WebKeys.TABLE_STATE, tableState);
        }
        
        TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_EVENT_TYPE, COLUMN_NAME_TYPE, request, table, tableState);
        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на напомнянията");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName("comming_event");
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        //request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_attachment_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
                .getAttribute(WebKeys.TABLE_COMMING_EVENTS + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(generateEventTypeFilter(getNacidDataProvider(), request.getParameter(FILTER_NAME_EVENT_TYPE)));
            session.setAttribute(WebKeys.TABLE_COMMING_EVENTS
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
        
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");
    }
    
    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_COMMING_EVENTS);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        EventDataProvider evDP = getNacidDataProvider().getEventDataProvider();
        
        List<Event> events = evDP.getActiveEvents();
        
        if (events != null) {
            for (Event ev : events) {
                
                
                try {
                    
                    FlatNomenclature eventStatus = getNacidDataProvider().getNomenclaturesDataProvider()
                        .getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS, ev.getEventStatus());
                    String eventType = getNacidDataProvider().getEventTypeDataProvider()
                        .getEventType(ev.getEventTypeId()).getEventName();
                    
                    ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();
                    Application app = appDP.getApplication(ev.getApplicationId());
                    String docflowNum = "";
                    if(app != null) {
                        docflowNum = app.getDocFlowNumber();
                    }
                    
                    String docType = "";
                    try {
                        NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
                        AttachmentDataProvider attDP = null;
                        if (ev.getDocCategoryId() == DocCategory.APPLICATION_ATTACHMENTS)
                            attDP = getNacidDataProvider().getApplicationAttachmentDataProvider();
                        else if (ev.getDocCategoryId() == DocCategory.DIPLOMA_EXAMINATIONS)
                            attDP = getNacidDataProvider().getDiplExamAttachmentDataProvider();
                        else if (ev.getDocCategoryId() == DocCategory.UNIVERSITY_EXAMINATIONS)
                            attDP = getNacidDataProvider().getUniExamAttachmentDataProvider();
                        
                        Attachment att = attDP.getAttachment(ev.getDocId(), false, false);
                        
                        DocumentType dt = nomDP.getDocumentType(att.getDocTypeId());
                        docType = dt.getName();
                    }
                    catch(NullPointerException e) {
                        
                    }
                    
                    table.addRow(ev.getId(), docflowNum, docType,
                            eventType, 
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

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
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
    private static ComboBoxFilterWebModel generateEventTypeFilter(NacidDataProvider nacidDataProvider, String activeKey) {
    	ComboBoxWebModel webmodel = new ComboBoxWebModel(activeKey, true);
    	List<EventType> eventTypes = nacidDataProvider.getEventTypeDataProvider().getEventTypes();
    	if (eventTypes != null) {
    		for (EventType t: eventTypes) {
        		webmodel.addItem(t.getEventName(), t.getEventName());
        	}	
    	}
    	ComboBoxFilterWebModel filter = new ComboBoxFilterWebModel(webmodel, FILTER_NAME_EVENT_TYPE, "Тип: ");
    	filter.setElementClass("brd");
    	return filter;
    }
}
