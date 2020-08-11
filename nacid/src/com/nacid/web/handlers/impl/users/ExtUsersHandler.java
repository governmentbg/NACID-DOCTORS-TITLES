package com.nacid.web.handlers.impl.users;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.users.ExtUsersGroupMembership;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UserGroupMembership;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.CheckBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.users.ExtUserWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

//import com.nacid.bl.external.users.ExtUser;
//import com.nacid.bl.external.users.ExtUsersDataProvider;

public class ExtUsersHandler extends NacidBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_FULL_NAME = "пълно име";
    private static final String COLUMN_NAME_USER_NAME = "потребителско име";
    private static final String COLUMN_NAME_STATE = "активен";
    private static final String COLUMN_NAME_TYPE = "тип";

    private static final String FILTER_NAME_ONLY_ACTIVE = "onlyActive";

    public ExtUsersHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (true) {
            throw new RuntimeException("Not supported...");
        }
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        int userId = DataConverter.parseInteger(request.getParameter("id"), 0);
        UsersDataProvider usersDataProvider = getNacidDataProvider().getUsersDataProvider();
        User user = userId == 0 ? null : usersDataProvider.getUserForEdit(userId);
        if (userId != 0 && user == null) {
            throw new UnknownRecordException("Unknown user ID:" + userId);
        }
        
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                    SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        resetTableData(request);
        user = usersDataProvider.getUserForEdit(userId);
        request.setAttribute(WebKeys.EXT_USER_WEB_MODEL, createWebmodel(getNacidDataProvider(), user));
        
        request.setAttribute(WebKeys.NEXT_SCREEN, "ext_user_edit");
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "ext_user_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Integer userId = DataConverter.parseInteger(request.getParameter("id"), null);
        if (userId == null) {
            throw new UnknownRecordException("Unknown User ID:" + userId);
        }

        request.setAttribute(WebKeys.NEXT_SCREEN, "ext_user_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
        User user = usersDataProvider.getUserForEdit(userId);
        if (user == null) {
            throw new UnknownRecordException("Unknown User for User ID:" + userId);
        }
        request.setAttribute(WebKeys.EXT_USER_WEB_MODEL, createWebmodel(nacidDataProvider, user));
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        generateTable(request);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Operation Delete is not implemented yet");
    }

    private void generateTable(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_EXT_USERS);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_USER_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_FULL_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);

            session.setAttribute(WebKeys.TABLE_EXT_USERS, table);
            resetTableData(request);

        }

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_EXT_USERS + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            //TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_NAME_ONLY_ACTIVE, COLUMN_NAME_STATE, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_EXT_USERS + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Външни потребители");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_EXT_USERS + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(new CheckBoxFilterWebModel(FILTER_NAME_ONLY_ACTIVE, "Само активните", DataConverter.parseBoolean(request
                    .getParameter(FILTER_NAME_ONLY_ACTIVE))));
            session.setAttribute(WebKeys.TABLE_EXT_USERS + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EXT_USERS);

        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
        List<? extends User> users = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_EXT_ID);

        if (users != null) {
            for (User u : users) {
                try {
                    
                    table.addRow(u.getUserId(), u.getUserName(), u.getFullName(),
                            getUserType(u));
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    
    private ExtUserWebModel createWebmodel(NacidDataProvider nacidDataProvider, User u) {
        String email = "";
        if(u.hasAccess(ExtUsersGroupMembership.APPLICANTS_GROUP, UserGroupMembership.FULL_ACCESS_OPERATION_ID, NacidDataProvider.APP_NACID_EXT_ID)) {
            ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
            ExtPerson p = epdp.getExtPersonByUserId(u.getUserId());
            if(p != null && p.getEmail() != null)
                email = p.getEmail();
        }
        else if(u.hasAccess(ExtUsersGroupMembership.EXPERT_GROUP, UserGroupMembership.FULL_ACCESS_OPERATION_ID, NacidDataProvider.APP_NACID_EXT_ID)) {
            ComissionMember cm = getNacidDataProvider().getComissionMemberDataProvider()
                    .getComissionMemberByUserId(u.getUserId());
            if(cm != null && cm.getEmail() != null)
                email = cm.getEmail();
        }
        
        return new ExtUserWebModel(u, getUserType(u), email);
    }
    
    private String getUserType(User u) {
        String type = "";
        if(u.hasAccess(ExtUsersGroupMembership.APPLICANTS_GROUP, UserGroupMembership.FULL_ACCESS_OPERATION_ID, NacidDataProvider.APP_NACID_EXT_ID)) {
            type = "външен потребител";
        }
        else if(u.hasAccess(ExtUsersGroupMembership.EXPERT_GROUP, UserGroupMembership.FULL_ACCESS_OPERATION_ID, NacidDataProvider.APP_NACID_EXT_ID)) {
            type = "експерт от комисията";
        }
        else {
            type = "системен потребител";
        }
        return type;
    }
}
