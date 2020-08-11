package com.nacid.web.taglib.table;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.table.TableWebModel;

public class TableTag extends SimpleTagSupport {
    private TableWebModel webmodel;
    private String attribute;
    private int currentRowId;
    public void setAttribute(String attribute) {
        this.attribute = (attribute == null || "".equals(attribute)) ? WebKeys.TABLE_WEB_MODEL : attribute;
    }
    public void doTag() throws JspException, IOException {
        webmodel = (TableWebModel) getJspContext().getAttribute(attribute, PageContext.REQUEST_SCOPE);
        if (webmodel != null) {
            getJspContext().setAttribute("allRowsCount", webmodel.getAllRowsCount());
            getJspContext().setAttribute("checkedRows", webmodel.getCheckedRowsForInput());
            getJspContext().setAttribute("uncheckedRows", webmodel.getUncheckedRowsForInput());
            getJspContext().setAttribute("rowBegin", webmodel.getSelectedRowBegin());
            getJspContext().setAttribute("rowsCount", webmodel.getSelectedRowsCount());
            getJspContext().setAttribute("availableRowsCount", webmodel.getAvailableRowsCount() + "");
            getJspContext().setAttribute("onlyChecked", webmodel.isOnlyChecked() ? "checked=\"checked\"" : "");
            getJspContext().setAttribute("groupName", webmodel.getGroupName());
            getJspContext().setAttribute("formGroupName", webmodel.getFormGroupName());
            getJspContext().setAttribute("formAdditionalParams", webmodel.getFormAdditionalRequestParams());
            getJspContext().setAttribute("tableName", webmodel.getTableName());
            getJspContext().setAttribute("orderColumn", webmodel.getOrderColumnName());
            getJspContext().setAttribute("orderAscending", webmodel.isAscendingOrder() ? "true" : "false");
            getJspContext().setAttribute("newRequestParams", webmodel.getRequestParams(TableWebModel.OPERATION_NAME_NEW));
            getJspContext().setAttribute("editRequestParams", webmodel.getRequestParams(TableWebModel.OPERATION_NAME_EDIT));
            getJspContext().setAttribute("viewRequestParams", webmodel.getRequestParams(TableWebModel.OPERATION_NAME_VIEW));
            getJspContext().setAttribute("deleteRequestParams", webmodel.getRequestParams(TableWebModel.OPERATION_NAME_DELETE));
            getJspContext().setAttribute("hasOperationsColumn", webmodel.isHasOperationsColumn());
            getJspContext().setAttribute("isModifyColumns", webmodel.isModifyColumns());
            if (webmodel.isViewOpenInNewWindow()) {
                getJspContext().setAttribute("viewLinkTarget", "target=\"_blank\"");
            }
            getJspBody().invoke(null);
        }
    }
    public TableWebModel getWebModel() {
        return webmodel;
    }
    public void setCurrentRowId(int rowId) {
    	this.currentRowId = rowId;
    }
	public int getCurrentRowId() {
		return currentRowId;
	}
    
}
