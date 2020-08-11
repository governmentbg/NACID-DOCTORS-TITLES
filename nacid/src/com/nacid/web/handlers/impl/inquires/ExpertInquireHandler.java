package com.nacid.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.inquires.ExpertInquireResult;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.ApplicationsHandler;
import com.nacid.web.model.inquires.ExpertInquireWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.HiddenFilterWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static com.nacid.web.handlers.impl.inquires.InquireBaseHandler.generateApplicationTypeComboBox;

/**
 * User: Georgi
 * Date: 24.3.2020 г.
 * Time: 15:00
 */
public class ExpertInquireHandler extends NacidBaseRequestHandler {
    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_EXPERT_NAME = "Име на експерт";
    private final static String COLUMN_NAME_APPLICATIONS_COUNT = "Брой заявления";
    private final static String COLUMN_NAME_APPLICATIONS = "Списък със заявления";
    private final static String COLUMN_NAME_APPLICATION_ENTRY_NUMS = "Вид услуга";

    private static final String REQUEST_PARAMETER_SHOW_APPS = "show_apps";
    public ExpertInquireHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public ExpertInquireHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        ApplicationsHandler.generateCompaniesCombo(request, getNacidDataProvider(), null);
        NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
        List<ApplicationStatus> allStatuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allStatuses, true, request, "allStatusesCombo", true);

        List<ApplicationStatus> allLegalStatuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allLegalStatuses, true, request, "allLegalStatusesCombo", true);

        List<ApplicationDocflowStatus> allDocflowStatuses = nomenclaturesDataProvider.getApplicationDocflowStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        ComboBoxUtils.generateNomenclaturesComboBox(null, allDocflowStatuses, true, request, "allDocflowStatusesCombo", true);

        UsersDataProvider usersDataProvider = getNacidDataProvider().getUsersDataProvider();
        List<? extends User> users = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_ID);
        ComboBoxUtils.generateUsersComboBox(null, users, request, "usersCombo", true, true);
        ComboBoxUtils.generateUsersComboBox(null, users, request, "responsibleUsersCombo", true, true);

        generateApplicationTypeComboBox(request);

        request.setAttribute("status_ids_count", 1);
        request.setAttribute(WebKeys.NEXT_SCREEN, "expert_inquire");
    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(getTableName());

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            table = constructEmptyTable(request);
            session.setAttribute(getTableName(), table);
            resetTableData(request, getExpertInquireResult(request));
        }

        // TableState settings
        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);


        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel(null);

        webmodel.setGroupName("app_comission");
        webmodel.setFormGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        webmodel.hideUnhideColumn(COLUMN_NAME_APPLICATIONS, !DataConverter.parseBoolean(request.getParameter(REQUEST_PARAMETER_SHOW_APPS)));
        webmodel.setViewOpenInNewWindow(true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);

        FiltersWebModel filtersWebModel = new FiltersWebModel();
        filtersWebModel.addFiler(new HiddenFilterWebModel(REQUEST_PARAMETER_SHOW_APPS, request.getParameter(REQUEST_PARAMETER_SHOW_APPS)));
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

        request.setAttribute(WebKeys.NEXT_SCREEN, "inquire_list");
    }


    protected List<ExpertInquireResult> getExpertInquireResult(HttpServletRequest request) {
        ExpertInquireWebModel inquireWebModel = new ExpertInquireWebModel(request);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        List<ExpertInquireResult> res = nacidDataProvider.getInquiresDataProvider().getExpertInquireResult(inquireWebModel);
        return res;
    }

    protected void resetTableData(HttpServletRequest request, List<ExpertInquireResult> res) {
        Table table = (Table) request.getSession().getAttribute(getTableName());
        if (table == null) {
            return;
        }
        table.emtyTableData();

//        NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(request.getSession());
        if (res != null) {
            for (ExpertInquireResult rec : res) {
                try {
                    table.addRow(rec.getExpertId(),
                            rec.getExpertNames(),
                            rec.getApplications() == null ? 0 : rec.getApplications().size(),
                            rec.getApplications() == null ? "" : rec.getApplications().stream().map(a -> String.format("<a target='_blank' href='/nacid/control/applications/view?id=%d'>%s</a>", a.getId(), a.getAppNum() + "/" + DataConverter.formatDate(a.getAppDate()))).collect(Collectors.joining("<br />\n")));
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    protected Table constructEmptyTable(HttpServletRequest request) {
        TableFactory tableFactory = TableFactory.getInstance();
        Table table = tableFactory.createTable();
        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_EXPERT_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APPLICATIONS_COUNT, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APPLICATIONS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APPLICATION_ENTRY_NUMS, CellValueDef.CELL_VALUE_TYPE_STRING);

        return table;

    }

    protected String getTableName() {
        return "expertInquireTable";
    }
}
