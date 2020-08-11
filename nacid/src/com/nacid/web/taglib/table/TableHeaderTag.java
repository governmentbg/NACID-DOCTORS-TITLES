package com.nacid.web.taglib.table;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.model.table.TableColumnHeaderWebModel;
import com.nacid.web.model.table.TableWebModel;

public class TableHeaderTag extends SimpleTagSupport {

    public void doTag() throws JspException, IOException {
        TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
        if (parent == null) {
            return;
        }
        // TableWebModel webmodel = (TableWebModel)getJspContext().getAttribute(WebKeys.TABLE_WEB_MODEL, PageContext.REQUEST_SCOPE);
        TableWebModel webmodel = parent.getWebModel();
        
        if (webmodel != null) {
            for (TableColumnHeaderWebModel columnHeader:webmodel.getColumnHeaders()) {
                getJspContext().setAttribute("columnName", columnHeader.getColumnName());
                getJspContext().setAttribute("columnId", columnHeader.getColumnNameId());
                getJspContext().setAttribute("col_style", webmodel.isColumnHidden(columnHeader.getColumnName()) ? "style=\"display:none\"" : "");
                
                String orderCol = "";
                if (columnHeader.getColumnName().equals(webmodel.getOrderColumnName())) {
                    orderCol = "<span class=\"arial\">" + (webmodel.isAscendingOrder() ? "&#9650;" : "&#9660;") + "</span>";
                }
                getJspContext().setAttribute("orderColumn", orderCol);
                getJspBody().invoke(null);
            }
        }
    }
}
