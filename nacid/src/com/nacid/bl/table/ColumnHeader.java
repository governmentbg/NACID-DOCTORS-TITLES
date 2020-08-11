package com.nacid.bl.table;

public interface ColumnHeader {
  public String getColumnName();
  /**
   * 
   * @return vytreshen nomer na kolonata (zapo4va ot 0)
   */
  public int getColumnId();
  public int getColumnCellsType();
  
  public void setCellsType(int cellsType);
}
