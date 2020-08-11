package com.nacid.web.taglib.table;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.data.DataConverter;
import com.nacid.web.model.table.TableWebModel;

public class TableEmptyRowTag extends SimpleTagSupport {
    private String row;
    private int additionalCols;

    public void setRow(String row) {
        this.row = row;
    }

    public void setAdditionalCols(String additionalCols) {
        this.additionalCols = DataConverter.parseInt(additionalCols, 0);
    }

    public void doTag() throws JspException, IOException {
        TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
        if (parent == null) {
            return;
        }
        // TableWebModel webmodel = (TableWebModel)getJspContext().getAttribute(WebKeys.TABLE_WEB_MODEL, PageContext.REQUEST_SCOPE);
        TableWebModel webmodel = parent.getWebModel();
        if (webmodel != null) {
            boolean addRow = false;
            if ("begin".equals(row)) {
                addRow = webmodel.addEmptyRow(true);
            } else if ("end".equals(row)) {
                addRow = webmodel.addEmptyRow(false);
            }
            if (addRow) {
                StringBuilder result = new StringBuilder("<tr>");
                for (int i = 0; i < webmodel.getVisibleColumnsCount() + additionalCols; i++) {
                    result.append("<td>...</td>");
                }
                result.append("</tr>");
                getJspContext().setAttribute("content", result.toString());
                getJspBody().invoke(null);
            }
        }
    }
}
