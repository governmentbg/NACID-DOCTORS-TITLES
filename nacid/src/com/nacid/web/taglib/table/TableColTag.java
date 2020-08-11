package com.nacid.web.taglib.table;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.model.table.TableRowWebModel;

public class TableColTag extends SimpleTagSupport {

  public void doTag() throws JspException, IOException {
    TableRowTag parent = (TableRowTag) getParent();
    if (parent == null) {
      return;
    }
    TableRowWebModel currentRow = parent.getCurrentRow();
    if (currentRow != null) {
      for (int i = 0; i < currentRow.getColumnsCount(); i++) {
        getJspContext().setAttribute("colValue", currentRow.getCellValue(i));
        getJspContext().setAttribute("colTitle", currentRow.getCellTitle(i));
        getJspContext().setAttribute("col_style", currentRow.isHidden(i) ? "style=\"display:none\"" : "");
        getJspBody().invoke(null);
      }
    }
  }
}
