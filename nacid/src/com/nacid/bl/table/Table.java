package com.nacid.bl.table;

import java.util.Collection;
import java.util.List;
import java.util.Set;



public interface Table {
  
  /**
   * informaciqta za kolonite se dobavq predi vyvejdaneto na kakvito i da e danni s addRow()!!!
   * pri opit za vyvejdane na ime na kolona sled kato ve4e sa vyvedeni nqkakvi danni v tablicata 
   * se hvyrlq {@link IllegalArgumentException}
   * @param columnName
   * @param cellValuesType - edin ot definiranite v {@link CellValueDef} interface-a
   * @throws IllegalArgumentException
   */
  public void addColumnHeader(String columnName, int cellValuesType) throws IllegalArgumentException;
  /**
   * zadava unikalna kolona - ako ne se zadade, za unikalna se priema pyrvata kolona
   * @param columnName
   * @throws IllegalArgumentException - ako se opita da se zadade unikalna kolona sled kato ve4e ima vyvedeni danni
   */
  public void setUniqueColmn(String columnName) throws IllegalArgumentException;
  /**
   * dobavq nov red kym tablicata
   * @param cellValues
   * @throws IllegalArgumentException - ako broq na kolonite ne otgovarq na broq na vyvedenite ColumnHeaders
   * @throws CellCreationException - ako tipa na dannite v dadena kolona ne syvpada s definiraniq v header-a na kolonata (s addColumnHeader) 
   */
  
  public TableRow addRow(Object... cellValues) throws IllegalArgumentException, CellCreationException ;
  
  
  public ColumnHeader getCoulmnHeader(String columnName); 
  
  public List<ColumnHeader> getCoumnHeaders();
  
  /**
   * izprazva dannite na tablicata
   */
  public void emtyTableData();
  
  /***
   * 
   * @param state - TableState obekt v koito sa opisani razli4ni kriterii za filtrirane i sortirane 
   * ( primerno stojnostite na pyrvata kolona da sa > 5, stojnostite na vtorata kolona da sa "text" i t.n.;
   * da se vyrnat samo opredelenite redove s dadeni ID-ta, rezultatite da se sortirat po zadadena kolona.... 
   * )
   * @return
   */
  public List<TableRow> getRows(TableState state);
  
  /**
   * vry6ta broq na vsi4ki redove v tablicata!
   * @return
   */
  public int getRowsCount();
  
  /**
   * vry6ta broq na filtriranite redove, bez da e prilojeno filtriraneto startRow-rowsCount!
   * tozi method moje da se vika edinstveno sled izvikvaneto na table.getRows(), kato moje da se izvikva samo vednyj sled vsqko izvikvane na getRows !!!
   * pri opit za povtorno izvikvane ili pri opit za izvikvane bez predi tova da e izvikan getRows(), hvyrlq {@link IllegalArgumentException}
   * @param tableState
   * @return
   */
  public int getRowsFilteredCount() throws IllegalArgumentException;
  
  //Vry6ta imeto na unikalnata kolona (ako ne e zadadena unikalna kolona sys setUniqueColumn(), togava za unikalna se priema pyrvata kolona i tozi method vry6ta neq
  public String getUniqueColumnName();
  
  
  //na tozi method mu se podavat Collection ot stojnosti na unikalnata kolona i toi vry6ta vytre6nite ID-ta na tezi redove 
  public Set<Integer> getRowIdsByUniqueColumnValues(Collection<Object> uniqueColumnValues) throws CellCreationException;
}
