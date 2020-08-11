package com.nacid.bl.table;

import java.util.Date;

public interface TableCell {
  
  /**
   * 
   * @return stojnostta na kolonata v long
   */
  //public long getIntegerValue();
  /**
   * 
   * @return stojnostta na kolonata v double
   */
  //public double getFloatingValue();
  //public Date getDateValue();
  //public String getStringValue();
  /**
   * 
   * @return tipa na kolonata - edin ot definiranite v interface-a TableCell
   */
  //public int getCellValueType();
  /**
   * @return referenciq kym row obekta kym koito prinadleji konkretnata kletka 
   */
  public TableRow getRow();
  /**
   * 
   * @return nomera na reda kym koito prinadleji kletkata 
   */
 // public int getRowNumber();
  /**
   * 
   * @return nomera na kolonata kym koqto prinadleji kletkata
   */
  //public int getColNumber();
  
  public CellValue getCellValue();
}
