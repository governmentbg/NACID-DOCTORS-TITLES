package com.nacid.bl.table.record;

import com.nacid.bl.table.CellValueDef;

public class CellValueDefImpl implements CellValueDef{
  int cellValueType;
  public CellValueDefImpl(int cellValueType) {
    this.cellValueType = cellValueType;
  }
  @Override
  public int getCellValueType() {
    return cellValueType;
  }
  
}
