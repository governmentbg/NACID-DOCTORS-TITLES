package com.nacid.web.handlers.impl.users;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.menu.MenuItem;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UserSysLogOperationExtended;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;
import com.nacid.web.model.users.UserSysLogOperationWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nacid.web.handlers.UserOperationsUtils.operationToNameMap;

/**
 * User: Georgi
 * Date: 20.8.2020 г.
 * Time: 8:25
 */
public class SyslogHandler extends NacidBaseRequestHandler {
    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_USER_ID = "userId";
    private final static String COLUMN_NAME_USERNAME = "Потребител";
    private final static String COLUMN_NAME_IP = "IP";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_OPERATION_NAME = "Действие";
    private final static String COLUMN_NAME_GROUP_NAME = "Страница";

    private static final String FILTER_NAME_USER_ID = "userIdFilter";
    private static final String FILTER_NAME_OPERATION_NAME = "operationNameFilter";
    private static final String FILTER_NAME_GROUP_NAME = "groupNameFilter";
    private static final String FILTER_NAME_DATE_FROM = "dateFromFilter";
    private static final String FILTER_NAME_DATE_TO = "dateToFilter";
    private static final String FILTER_NAME_QUERY_STRING = "queryStringFilter";
    private static final String FILTER_NAME_SESSION_ID = "sessionIdFilter";

    private static Map<String, String> GROUP_NAMES = new HashMap<>();
    static {
        GROUP_NAMES.put("syslog", "Системен журнал");
        GROUP_NAMES.put("application_attachment", "Прикачени файлове към заявление");
        GROUP_NAMES.put("dipl_exam_attachment", "Прикачени файлове към проучване на диплома");
        GROUP_NAMES.put("applications", "Заявления");
        GROUP_NAMES.put("expert_statement_attachment", "Становища на експерти");
        GROUP_NAMES.put("applications_status", "Статус на заявление");
        GROUP_NAMES.put("e_applying", "Електронно подадени заявления");
        GROUP_NAMES.put("applications_expert", "Експерти към заявление");
        GROUP_NAMES.put("original_education_level", "Оригинална степен на образование");
        GROUP_NAMES.put("comission_calendar_process", "Календар от комисия");
        GROUP_NAMES.put("commission_calendar", "Календар от комисия");
        GROUP_NAMES.put("application_motives", "Мотиви");
        GROUP_NAMES.put("university_validity", "Проверка на висше училище");
        GROUP_NAMES.put("diploma_type", "Тип диплома");
        GROUP_NAMES.put("person", "Лица");
        GROUP_NAMES.put("users", "Потребители");
        GROUP_NAMES.put("specialitysuggest", "Избор на специалност");
        GROUP_NAMES.put("universitysuggest", "Избор на университет");
        GROUP_NAMES.put("training_course_diploma_type_universities_ajax", "Тип диплома - Избор на университет");
        GROUP_NAMES.put("civilidsuggestion", "Избор на лице");
        GROUP_NAMES.put("flatnomenclaturesuggest", "Избор на номенклатура");
        GROUP_NAMES.put("originalspecialitysuggest", "Избор на оригинална специалност");
        GROUP_NAMES.put("home", "Начало");
        GROUP_NAMES.put("legal_reason_ajax", "Избор на правни основания");
        GROUP_NAMES.put("diploma_type_ajax", "Избор на тип диплома");
        GROUP_NAMES.put("comming_event", "Предстоящи събития");
        GROUP_NAMES.put("commonvariables", "Общи променливи");
        GROUP_NAMES.put("application_expert", "Експерт към заявление");
    }
    public SyslogHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public SyslogHandler(ServletContext servletContext) {
        super(servletContext);
    }


    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_SYSLOG);

        boolean reloadTable = true;//RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_USER_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_USERNAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_IP, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_GROUP_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_OPERATION_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);

            session.setAttribute(WebKeys.TABLE_SYSLOG, table);
            resetTableData(request);

        }

        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Системен журнал");
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideUnhideColumn(COLUMN_NAME_ID, true);
        webmodel.hideUnhideColumn(COLUMN_NAME_USER_ID, true);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_SYSLOG + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();

            ComboBoxFilterWebModel userIdFilter = new ComboBoxFilterWebModel(getUsersCombobox(request), FILTER_NAME_USER_ID, COLUMN_NAME_USERNAME);
            userIdFilter.setLabelOnTop(true);
            userIdFilter.setElementClass("brd w150");
            filtersWebModel.addFiler(userIdFilter);

            ComboBoxFilterWebModel groupsFilter = new ComboBoxFilterWebModel(getGroupNamesCombobox(request), FILTER_NAME_GROUP_NAME, COLUMN_NAME_GROUP_NAME);
            groupsFilter.setLabelOnTop(true);
            groupsFilter.setElementClass("brd w200");
            filtersWebModel.addFiler(groupsFilter);

            ComboBoxFilterWebModel operationsFilter = new ComboBoxFilterWebModel(getOperationNamesCombobox(request), FILTER_NAME_OPERATION_NAME, COLUMN_NAME_OPERATION_NAME);
            operationsFilter.setLabelOnTop(true);
            operationsFilter.setElementClass("brd w120");
            filtersWebModel.addFiler(operationsFilter);


            TextFieldFilterWebModel tfFWB = new TextFieldFilterWebModel(FILTER_NAME_DATE_FROM, "От дата", request.getParameter(FILTER_NAME_DATE_FROM));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd w80");
            filtersWebModel.addFiler(tfFWB);


            tfFWB = new TextFieldFilterWebModel(FILTER_NAME_DATE_TO, "До дата", request.getParameter(FILTER_NAME_DATE_TO));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd w80");
            filtersWebModel.addFiler(tfFWB);

            tfFWB = new TextFieldFilterWebModel(FILTER_NAME_QUERY_STRING, "Параметри", request.getParameter(FILTER_NAME_QUERY_STRING));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd w200");
            filtersWebModel.addFiler(tfFWB);

            tfFWB = new TextFieldFilterWebModel(FILTER_NAME_SESSION_ID, "ID на сесията", request.getParameter(FILTER_NAME_SESSION_ID));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd w200");
            filtersWebModel.addFiler(tfFWB);

            session.setAttribute(WebKeys.TABLE_SYSLOG + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_SYSLOG);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        UsersDataProvider udp = nacidDataProvider.getUsersDataProvider();

        Integer userId = DataConverter.parseInteger(request.getParameter(FILTER_NAME_USER_ID), null);
        Date dateFrom = DataConverter.parseDate(request.getParameter(FILTER_NAME_DATE_FROM));
        Date dateTo = DataConverter.parseDate(request.getParameter(FILTER_NAME_DATE_TO));
        int webappId = (Integer) request.getSession().getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID);
        String operationName = DataConverter.parseString(request.getParameter(FILTER_NAME_OPERATION_NAME), null);
        String groupName = DataConverter.parseString(request.getParameter(FILTER_NAME_GROUP_NAME), null);
        String queryString = DataConverter.parseString(request.getParameter(FILTER_NAME_QUERY_STRING), null);
        String sessionId = DataConverter.parseString(request.getParameter(FILTER_NAME_SESSION_ID), null);
        int sysLogCount = udp.countUserSysLogOperations(userId, dateFrom, dateTo, webappId, groupName, operationName, queryString, sessionId);
        try {
            if (sysLogCount > 10000) {
                table.addRow(0, 0, "Върнатите записи са над 10000 (" + sysLogCount + "). Моля използвайте филтрите!", null, null, null, null);
            } else {
                List<UserSysLogOperationExtended> syslogs = udp.getUserSysLogOperations(userId, dateFrom, dateTo, webappId, groupName, operationName, queryString, sessionId);
                if (syslogs != null) {
                    for (UserSysLogOperationExtended u : syslogs) {
                        String gn = getGroupName(getNacidDataProvider(), u.getGroupName());
                        String on = getOperationName(u.getOperationName());
                        table.addRow(u.getId(), u.getUserId(), u.getUsername(), u.getRemoteAddress(), u.getDateCreated(), gn, on);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw Utils.logException(e);
        } catch (CellCreationException e) {
            throw Utils.logException(e);
        }

    }

    private ComboBoxWebModel getUsersCombobox(HttpServletRequest request) {
        UsersDataProvider udp = getNacidDataProvider().getUsersDataProvider();

        List<? extends User> users = udp.getUsers(0, -1, 0, (Integer) request.getSession().getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID));
        ComboBoxWebModel wm = new ComboBoxWebModel(request.getParameter(FILTER_NAME_USER_ID), true);
        users.stream().forEach(u -> wm.addItem(u.getUserId() + "", u.getUserName()));
        return wm;
//        ComboBoxFilterWebModel wm = new ComboBoxFilterWebModel();
    }
    private ComboBoxWebModel getOperationNamesCombobox(HttpServletRequest request) {
        ComboBoxWebModel wm = new ComboBoxWebModel(request.getParameter(FILTER_NAME_OPERATION_NAME), false);
        wm.addItem("", "-");//empty empty (the default empty has "-" for key)
        UserOperationsUtils.partOfUrl2OperationLevel.entrySet().stream().forEach(e -> wm.addItem(e.getKey(), operationToNameMap.get(e.getValue())));
        return wm;
    }

    private ComboBoxWebModel getGroupNamesCombobox(HttpServletRequest request) {
        ComboBoxWebModel wm = new ComboBoxWebModel(request.getParameter(FILTER_NAME_GROUP_NAME), false);
        wm.addItem("", "-");//empty empty (the default empty has "-" for key)
        GROUP_NAMES.entrySet().stream().forEach(e -> wm.addItem(e.getKey(), e.getValue()));
        return wm;
    }
    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        Integer id = DataConverter.parseInteger(request.getParameter("id"), null);
        if (id != null) {

            UserSysLogOperationExtended rec = getNacidDataProvider().getUsersDataProvider().getUserSysLogOperationExtended(id);
            request.setAttribute("syslog", rec == null ? null : new UserSysLogOperationWebModel(getNacidDataProvider(), rec));
            setNextScreen(request, "syslog_view");
        } else {
            try {
                response.sendRedirect(request.getContextPath() + "/control/syslog/list");
            } catch (IOException e) {
                throw Utils.logException(e);
            }
        }
    }

    public static String getGroupName(NacidDataProvider nacidDataProvider, String groupName) {
        List<MenuItem> menuItems = nacidDataProvider.getMenuDataProvider(NacidDataProvider.APP_NACID_ID).getMenuItemsByPartOfUrl("/control/" + groupName + "/");
        if (menuItems != null && menuItems.size() == 1) {
            return menuItems.get(0).getName();
        }
        return GROUP_NAMES.get(groupName) == null ? groupName : GROUP_NAMES.get(groupName);
    }
    public static String getOperationName(String operationName) {
        return UserOperationsUtils.getOperationId(operationName) == 0 ? operationName : UserOperationsUtils.operationToNameMap.get(UserOperationsUtils.getOperationId(operationName));
    }
}
