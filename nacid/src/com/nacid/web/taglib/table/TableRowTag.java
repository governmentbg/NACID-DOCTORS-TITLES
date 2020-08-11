package com.nacid.web.taglib.table;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.nacid.web.model.table.TableRowWebModel;
import com.nacid.web.model.table.TableWebModel;

public class TableRowTag extends SimpleTagSupport {
    private TableRowWebModel currentRow;

    public void doTag() throws JspException, IOException {
        TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
        if (parent == null) {
            return;
        }
        
        //TableWebModel webmodel = (TableWebModel) getJspContext().getAttribute(WebKeys.TABLE_WEB_MODEL, PageContext.REQUEST_SCOPE);
        TableWebModel webmodel = parent.getWebModel();
        if (webmodel != null) {
            for (int currentRowNumber = 0; currentRowNumber < webmodel.getRowsCount(); currentRowNumber++) {
                currentRow = webmodel.getRow(currentRowNumber);
                parent.setCurrentRowId(currentRowNumber);
                getJspContext().setAttribute("currentRowNumber", currentRowNumber + 1 + webmodel.getSelectedRowBegin());
                getJspContext().setAttribute("rowId", currentRow.getRowId());
                getJspContext().setAttribute("rowChecked", webmodel.isRowChecked(currentRow.getRowId()) ? "checked=\"checked\"" : "");
                getJspContext().setAttribute("uniqueRowValue", currentRow.getUniqueCellValue());
                getJspContext().setAttribute("row_class", StringUtils.isEmpty(currentRow.getClazz()) ? "white_bg" : currentRow.getClazz());
                getJspBody().invoke(null);
            }
        }
    }

    public TableRowWebModel getCurrentRow() {
        return currentRow;
    }
}
