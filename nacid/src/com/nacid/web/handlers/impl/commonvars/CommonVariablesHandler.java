package com.nacid.web.handlers.impl.commonvars;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.bl.utils.CommonVariable;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.commonvars.CommonVariableWebModel;
import com.nacid.web.model.table.TableWebModel;

public class CommonVariablesHandler extends NacidBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_DESCRIPTION = "Описание";
    private final static String COLUMN_NAME_VALUE = "Стойност";
    
    public CommonVariablesHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "common_vars_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int commonVarId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (commonVarId <= 0) {
            throw new UnknownRecordException("Unknown CommonVar ID:request.getParameter(\"id\")=" + request.getParameter("id"));
        }
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        CommonVariable variable = utilsDataProvider.getCommonVariable(commonVarId);
        if (variable == null) {
            throw new UnknownRecordException("Unknown Common Var ID:" + commonVarId);
        }
        request.setAttribute(WebKeys.COMMON_VARIABLE_WEB_MODEL, new CommonVariableWebModel(variable));
        request.setAttribute(WebKeys.NEXT_SCREEN, "common_vars_edit");
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        
        int id = DataConverter.parseInt(request.getParameter("id"), -1);
        if (id == -1) {
            throw new UnknownRecordException("Unknown common variable id:" + request.getParameter("id"));
        }
        
        String name = request.getParameter("name");
        String value = request.getParameter("value");
        String description = request.getParameter("description");
        
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        id = utilsDataProvider.saveCommonVariable(id, name, value, description);
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        

        request.setAttribute(WebKeys.COMMON_VARIABLE_WEB_MODEL, new CommonVariableWebModel(id + "", name, value, description));
        request.setAttribute(WebKeys.NEXT_SCREEN, "common_vars_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_COMMON_VARIABLES);
    }
  

	@Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        
        Table table = (Table) session.getAttribute(WebKeys.TABLE_COMMON_VARIABLES);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DESCRIPTION, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_VALUE, CellValueDef.CELL_VALUE_TYPE_STRING);
            
            session.setAttribute(WebKeys.TABLE_COMMON_VARIABLES, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък с общи променливи");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_COMMON_VARIABLES);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        List<CommonVariable> vars = nacidDataProvider.getUtilsDataProvider().getCommonVariables();
        
        if (vars != null) {
            for (CommonVariable var : vars) {
                try {
                    table.addRow(var.getId(), var.getDescription(), var.getVariableValue());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
