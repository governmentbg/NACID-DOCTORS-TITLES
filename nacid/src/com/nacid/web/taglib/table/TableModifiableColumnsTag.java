package com.nacid.web.taglib.table;

import com.nacid.web.model.table.TableWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * User: Georgi
 * Date: 3.4.2020 г.
 * Time: 15:58
 */
public class TableModifiableColumnsTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
        if (parent == null) {
            return;
        }
        TableWebModel table = parent.getWebModel();
        if (table.isModifyColumns()) {
            int rowId = 1;
            for (String columnName : table.getModifiableColumnNamesList()) {
                getJspContext().setAttribute("columnName", columnName);
                getJspContext().setAttribute("selected", table.isModifiableColumnVisible(columnName));
                getJspContext().setAttribute("rowId", rowId++);
                getJspBody().invoke(null);
            }
        }
    }
}
