package com.nacid.bl.table;

import java.util.List;


public interface TableRow{
  /**
   * @return unikalnoto ID na reda
   */
  public int getRowId();
  public TableCell getCell(int colId);
  public List<TableCell> getCells();
  /**
   * @return stojnostta na unikalnata kolona
   */
  public TableCell getUniqueCellValue();
  /**
   * @return - idTo na unikalnata kolona
   */
  public int getUniqueColumnId();
  
  /**
   * pravi reda editable/not editable/
   * @param editable
   */
  public void setEditable(boolean editable);
 /**
   * @return dali reda shte moje da se redaktira
   */
  public boolean isEditable();

    public boolean isViewable();
    public void setViewable(boolean viewable);
public boolean isDeletable();
public void setDeletable(boolean deletable);
  public void setClazz(String cls);
  public String getClazz();
}
