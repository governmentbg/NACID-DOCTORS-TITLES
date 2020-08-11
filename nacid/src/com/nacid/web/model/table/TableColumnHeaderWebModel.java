package com.nacid.web.model.table;

import com.nacid.bl.table.ColumnHeader;

public class TableColumnHeaderWebModel {
  private String columnNameId;
  private String columnName;
  TableColumnHeaderWebModel(ColumnHeader header) {
    this.columnNameId = header.getColumnName();
    this.columnName = header.getColumnName();
  }
  public String getColumnNameId() {
    return columnNameId;
  }
  public String getColumnName() {
    return columnName;
  }
}
