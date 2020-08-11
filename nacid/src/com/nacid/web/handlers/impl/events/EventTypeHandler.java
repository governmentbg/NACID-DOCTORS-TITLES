package com.nacid.web.handlers.impl.events;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.events.EventType;
import com.nacid.bl.events.EventTypeDataProvider;
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
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.events.EventTypeWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class EventTypeHandler extends NacidBaseRequestHandler {

    
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Име";
    private static final String COLUMN_NAME_REMINDER_DAYS = "Срок за напомняне";
    private static final String COLUMN_NAME_DUE_DAYS= "Краен срок";
    private static final String COLUMN_NAME_TEXT = "Текст на напомнянето";
    
    private static final String EDIT_SCREEN = "event_type_edit";
    
    public EventTypeHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        
        request.setAttribute(WebKeys.EVENT_TYPE_WEB_MODEL, new EventTypeWebModel(null));
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
    }
    
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        
        if(id == 0) {
            throw new UnknownRecordException("Unknown eventtype ID:" + id);
        }
        
        EventTypeDataProvider etDP = getNacidDataProvider().getEventTypeDataProvider();
        
        EventType et = etDP.getEventType(id);
        if(et == null) {
            throw new UnknownRecordException("Unknown eventtype ID:" + id);
        }
        
        request.setAttribute(WebKeys.EVENT_TYPE_WEB_MODEL, new EventTypeWebModel(et));
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        String eventName = request.getParameter("eventName");
        String reminderText = request.getParameter("reminderText");
        Integer reminderDays = DataConverter.parseInteger(request.getParameter("reminderDays"), null);
        Integer dueDays = DataConverter.parseInteger(request.getParameter("dueDays"), null);
        
        EventTypeDataProvider etDP = getNacidDataProvider().getEventTypeDataProvider();
        
        id = etDP.saveEventType(id, eventName, reminderDays, dueDays, reminderText);
        
        request.getSession().removeAttribute(WebKeys.TABLE_EVENT_TYPE);
        
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните са записани в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        
        request.setAttribute(WebKeys.EVENT_TYPE_WEB_MODEL, 
                new EventTypeWebModel(id, eventName, reminderDays, dueDays, reminderText));
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
    }
    
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_EVENT_TYPE);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            
            
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_REMINDER_DAYS, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DUE_DAYS, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_TEXT, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            
            session.setAttribute(WebKeys.TABLE_EVENT_TYPE, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session
                .getAttribute(WebKeys.TABLE_EVENT_TYPE + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_EVENT_TYPE + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на типовете напомняния");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
                .getAttribute(WebKeys.TABLE_EVENT_TYPE + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            session.setAttribute(WebKeys.TABLE_EVENT_TYPE
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    
    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EVENT_TYPE);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        
        EventTypeDataProvider etDP = getNacidDataProvider().getEventTypeDataProvider();
        
        List<EventType> eTypes = etDP.getEventTypes();
        
        if (eTypes != null) {
            for (EventType et : eTypes) {
                
                
                try {
                    
                    table.addRow(et.getId(), et.getEventName(), et.getReminderDays(),
                            et.getDueDays(), et.getReminderText());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
