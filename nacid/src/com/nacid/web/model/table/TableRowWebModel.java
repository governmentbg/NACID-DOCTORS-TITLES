package com.nacid.web.model.table;

import com.nacid.bl.table.CellValue;

import java.util.ArrayList;
import java.util.List;

public class TableRowWebModel {
  private List<TableCellWebModel> cells = new ArrayList<TableCellWebModel>();
  private int uniqueColumnId;
  private String rowId;
  private TableWebModel tableWebModel;
  private boolean editable;
  private boolean deletable;
    private boolean viewable;
  private String cls;
  TableRowWebModel(int rowId, int uniqueColumnId, TableWebModel tableWebModel, boolean editable, boolean deletable, boolean viewable, String clazz) {
    this.rowId = rowId + "";
    this.uniqueColumnId = uniqueColumnId;
    this.tableWebModel = tableWebModel;
    this.editable = editable;
    this.cls = clazz;
    this.deletable = deletable;
      this.viewable = viewable;
  }
  public void addCell(CellFormatter formatter, CellValue cellValue) {
    //Podrazbirashti se formatters
    if (formatter == null) {
      formatter = CellFormatter.createCellFormatterByCellValueType(cellValue.getCellValueType());
    }
    cells.add(new TableCellWebModel(formatter, cellValue));
  }
  public void setClazz(String cls) {
      this.cls = cls;
  }
  public String getClazz() {
      return cls;
  }
  public String getUniqueCellValue() {
    return cells.get(uniqueColumnId).getValue();
  }
  public String getRowId() {
    return rowId;
  }
  protected List<TableCellWebModel> getCells() {
    return cells;
  }
  public int getColumnsCount() {
    return cells.size();
  }
  public String getCellValue(int columnId) {
    return cells.get(columnId).getValue();
  }
  public String getCellTitle(int columnId) {
    return cells.get(columnId).getTitle();
  }
  public boolean isHidden(int columnId) {
	  return tableWebModel.isColumnIdHidden(columnId);
  }
public boolean isEditable() {
	return editable;
}
public boolean isDeletable() {
    return deletable;
}

    public boolean isViewable() {
        return viewable;
    }
}
