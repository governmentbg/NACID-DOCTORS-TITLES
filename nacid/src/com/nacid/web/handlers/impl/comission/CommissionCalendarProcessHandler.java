package com.nacid.web.handlers.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.model.comission.CommissionCalendarHeaderWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class CommissionCalendarProcessHandler extends NacidBaseRequestHandler {

    ServletContext servletContext;
    public CommissionCalendarProcessHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), 0);
        
        String operation = getOperationName(request);
        if(UserOperationsUtils.getOperationId(operation) 
                == UserOperationsUtils.OPERATION_LEVEL_SAVE) {
            operation = UserOperationsUtils.getOperationName(UserOperationsUtils.OPERATION_LEVEL_EDIT);
        }
        request.setAttribute("urlEnd", operation + "?calendar_id="+calendarId);
        
        request.setAttribute(WebKeys.COMMISSION_CALENDAR_HEADER_WEB_MODEL, new CommissionCalendarHeaderWebModel(calendarId, 
                operation, CommissionCalendarHeaderWebModel.ACTIVE_FORM_CALENDAR_PROCESS_EDIT));
        request.setAttribute(WebKeys.NEXT_SCREEN, "commission_calendar_process_edit");
        
        List<ApplicationStatus> nomenclatures = getNacidDataProvider().getNomenclaturesDataProvider().getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);
        List<FlatNomenclature> res = new ArrayList<FlatNomenclature>();
        for (FlatNomenclature n:nomenclatures) {
            res.add(n);
        }
        ComboBoxUtils.generateNomenclaturesComboBox(null, res,
                true, request, "applicationStatusChangeCombo", 
                false);
        new ApplicationMotivesHandler(servletContext).handleList(request, response);
        new CalendarSessionProtocolHandler(servletContext).handleView(request, response);
        
        CommissionCalendarHandler.generateCommissionCalendarHeaderMessage(request, getNacidDataProvider().getCommissionCalendarDataProvider().getCommissionCalendar(calendarId));
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
    	throw new IllegalStateException("Масовата промяна на статуси не работи!!!!!");
    	/*
    	
        int calendarId = DataConverter.parseInt(request.getParameter("calendarId"), -1);
        if(calendarId <= 0) {
            throw Utils.logException(new Exception("Unknown calendar id " + calendarId));
        }
        int newStatus  = DataConverter.parseInt(request.getParameter("newAppStatus"), -1);
        if(calendarId <= 0) {
            throw Utils.logException(new Exception("Unknown new app status id " + calendarId));
        }
        String selectedApplications = request.getParameter("selectedApplications");
        ArrayList<Integer> selIds = new ArrayList<Integer>();
        if(selectedApplications != null) {
            String[] ids = selectedApplications.split(";");
            for(String s : ids) {
                Integer id = DataConverter.parseInteger(s, null);
                if(id != null) {
                    selIds.add(id);
                }
            }
        }
        
        ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();
        
        for(Integer appId : selIds) {
            appDP.updateApplicationStatus(appId, newStatus, null, null, null);
        }

        SystemMessageWebModel sysMsg = selIds.size() > 0 ? 
                new SystemMessageWebModel("Статусите бяха променени", SystemMessageWebModel.MESSAGE_TYPE_CORRECT)
                :
                new SystemMessageWebModel("Не са избрани заявления", SystemMessageWebModel.MESSAGE_TYPE_ERROR);
        addSystemMessageToSession(request, "statusChangeMsg", sysMsg);
        try {
            response.sendRedirect( servletContext.getAttribute("pathPrefix") + 
                    "/control/comission_calendar_process/edit/?calendar_id=" 
                    + calendarId);
        } catch (IOException e) {
            throw Utils.logException(e);
        }*/
    }
    
}
