package com.ext.nacid.web.handlers.impl.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.mail.Mail;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.mail.MailWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public abstract class MailHandlerBase extends NacidExtBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_STATUS = "Статус";
    private final static String COLUMN_NAME_INOUT = "Тип";
    private final static String COLUMN_NAME_DATE = "Дата";
    //private final static String COLUMN_NAME_TO = "Получател";
    private final static String COLUMN_NAME_SUBJECT = "Относно";
    
    protected MailHandlerBase(ServletContext servletContext) {
        super(servletContext);
    }

    
    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        if(id == 0) {
            throw new UnknownRecordException("No id");
        }
        
        MailDataProvider mDP = getNacidDataProvider().getMailDataProvider();
        
        Mail mail = mDP.getMailById(id);
        
        if(mail == null) {
            throw new UnknownRecordException("No mail with id = " + id);
        }
        
        request.setAttribute(WebKeys.MAIL_WEB_MODEL, new MailWebModel(mail));
        request.setAttribute("groupName", getGroupName(request));
        
        request.setAttribute(WebKeys.NEXT_SCREEN, "mail_edit");
    }
    
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_MAIL_ADMIN);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            
            
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_INOUT, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            //table.addColumnHeader(COLUMN_NAME_TO, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_SUBJECT, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            session.setAttribute(WebKeys.TABLE_MAIL_ADMIN, table);
            resetTableData(request, response);

        }

        // TableState settings
        TableState tableState = (TableState) session
                .getAttribute(WebKeys.TABLE_MAIL_ADMIN + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_MAIL_ADMIN + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на електронните писма");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
                .getAttribute(WebKeys.TABLE_MAIL_ADMIN + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            session.setAttribute(WebKeys.TABLE_MAIL_ADMIN
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }
    
    private void resetTableData(HttpServletRequest request, HttpServletResponse response) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_MAIL_ADMIN);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        MailDataProvider mDP = nacidDataProvider.getMailDataProvider();
        ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
        
        User eu = getLoggedUser(request, response);
        ExtPerson person = epdp.getExtPersonByUserId(eu.getUserId());
        ComissionMemberDataProvider cmdp = nacidDataProvider.getComissionMemberDataProvider();
        ComissionMember member = cmdp.getComissionMemberByUserId(eu.getUserId());
        String mailAddress = null;
        if(member != null) {
            mailAddress = member.getEmail();
        }
        if(person != null) {
            mailAddress = person.getEmail();
        }
        
        if(StringUtils.isEmpty(mailAddress)) {
            return;
        }
        
        List<Mail> mails = mDP.getMailsByRecepient(mailAddress);
        List<Mail> mails1 = mDP.getMailsBySender(mailAddress);
        
        if(mails == null) mails = new ArrayList<Mail>();
        if(mails1 == null) mails1 = new ArrayList<Mail>();
        
        mails.addAll(mails1);
        
        Collections.sort(mails, new Comparator<Mail>() {

            @Override
            public int compare(Mail o1, Mail o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        
        for (Mail m : mails) {

            try {
                table.addRow(m.getId(), getMailStatus(m), getMailType(m), m.getDate(),
                // m.getRecepient(),
                        m.getSubject());
            } catch (IllegalArgumentException e) {
                throw Utils.logException(e);
            } catch (CellCreationException e) {
                throw Utils.logException(e);
            }
        }

    }
    
    public static String getMailStatus(Mail m) {
        return m.isProcessed() ? "изпратено" : "обработва се";
    }
    
    public static String getMailType(Mail m) {
        return !m.isIncome() ? "входящо" : "изходящо";
    }
}
