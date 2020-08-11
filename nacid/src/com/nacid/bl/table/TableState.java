package com.nacid.bl.table;

import java.util.List;
import java.util.Set;

import com.nacid.bl.table.impl.FilterCriteria;
/**
 * obekt, koito se zadava pri vikane na Table.getRows() 
 * v tozi obekt ima opisani parametri za filtrirane i sortirane
 * @author ggeorgiev
 *
 */
public interface TableState {
  /**
   * zadava criterij za sortirane 
   * @param columnName - ime na kolonata
   * @param ascending - ako true - ascending, ako false descending, ako e null - obry6ta predishnoto vyvedeno sortirane
   */
  public void setOrderCriteria(String columnName, Boolean ascending);
  
  /**
   * @param columnName - ime na kolonata za koqto shte se otnasq filter-a
   * @param condition - uslovie na filtera - edin ot definiranite v {@link FilterCriteria}
   * @param cellType - tipa na kletkata - edin ot definiranite {@link CellValueDef}
   * @param value - stojnost na kriteriq
   * 
   * 
   * Ako value == null i condition = EQUALS, togava shte vyrne vsi4ki zapisi, koito imat null za stojnost na tova pole!!!
   * @throws CellCreationException - ako ne moje da convertira value v podobavasht tip za filtrirane
   * <br />
   * <br />
   * za da se filtrira po dadena kolona ne e dostaty4no samo da se dobavi filter-a - trqbva da se settne i setFiltered(true),
   * tyi kato toq filter stoi v sesiqta i trqbva da pomni predishnite systoqniq i eventualno v nqkoi slu4ai v TableState-a shte ima zadadeni filtri
   * koito nqma da se polzvat, no shte trqbvat za sledvashtata zaqvka primerno! 
   * */
  public void addFilter(String columnName, int cellType, int condition, Object value) throws CellCreationException;
  /**
   * @param columnName - ime na kolonata za koqto shte se otnasq filter-a
   * @param condition - uslovie na filtera - edin ot definiranite v {@link FilterCriteria}
   * @param cellType - tipa na kletkata - edin ot definiranite {@link CellValueDef}
   * @param value - stojnost na kriteriq
   * @param isNot - dali filter-a e protivopolojen na tova v condition (primerno ako condition = {@link FilterCriteria}.EQUALS, i isNot = true, kriteria stava NOT EQUALS
   * 
   * Ako value == null i condition = EQUALS, togava shte vyrne vsi4ki zapisi, koito imat null za stojnost na tova pole!!!
   * @throws CellCreationException - ako ne moje da convertira value v podobavasht tip za filtrirane
   * <br />
   * <br />
   * za da se filtrira po dadena kolona ne e dostaty4no samo da se dobavi filter-a - trqbva da se settne i setFiltered(true),
   * tyi kato toq filter stoi v sesiqta i trqbva da pomni predishnite systoqniq i eventualno v nqkoi slu4ai v TableState-a shte ima zadadeni filtri
   * koito nqma da se polzvat, no shte trqbvat za sledvashtata zaqvka primerno! 
   * */
  public void addFilter(String columnName, int cellType, int condition, Object value, boolean isNot) throws CellCreationException;
  
  /**
   * @see TableState#addFilter(String, int, int, Object)
   * Razlikata e che ako ima poveche ot edin obekt v List-a, togava se pravi "ILI" vyv kriteria mejdu vsi4ki obekti
   */
  public void addFilter(String columnName, int cellType, int condition, List<Object> value) throws CellCreationException;
  /**
   * @see TableState#addFilter(String, int, int, Object, boolean)
   * Razlikata e che ako ima poveche ot edin obekt v List-a, togava se pravi "ILI" vyv kriteria mejdu vsi4ki obekti
   */
  public void addFilter(String columnName, int cellType, int condition, List<Object> value, boolean isNot) throws CellCreationException;
  
  /**
   * vyvejda ID-tata na markiranite redove (tezi koito shte trqbva da se vyrnat ako e izbrano setOnlyCheckedRows)
   * @param checkedRows
   */
  public void setCheckedRowsIds(Set<Integer> checkedRows);
  
  /**
   * vry6ta ID-tata na marikranite redove
   * @return
   */
  public Set<Integer> getCheckedRowsIds();
  /**
   * @return imeto order kolonata
   */
  public String getOrderColumn();
  /**
   * @return dali sortiraneto e ascending
   */
  public boolean isOrderAscending();
  /**
   * iz4istva vsi4ki filtri
   */
  public void clearAllFilters();
  /**
   * @return dali rezultatite trqbva da sa podredeni po zadadenite orderColumn - ascending
   */
  public boolean isOrdered();
  /**
   * @param ordered - zadava dali rezultatite shte sa podredeni po zadadenite orderColumn - ascending
   */
  public void setOrdered(boolean ordered);
  /**
   * @return - dali shte se vry6tat rezultati, koito podadat v redovete markirani sys setCheckedRowsIds() 
   */
  public boolean isOnlyCheckedRows();
  /**
   * 
   * @param onlyCheckedRows - zadava dali shte se vry6tat rezultati, koito podadat v redovete markirani sys setCheckedRowsIds()
   */
  public void setOnlyCheckedRows(boolean onlyCheckedRows);
  /**
   * @return dali rezultatite sa filtrirani sys zadadenite filterCriterias
   */
  public boolean isFiltered();
  /**
   *  
   * @param filtered - zadava dali rezultatite shte trqbva da se filtrirat sys zadadenite filterCriterias (dobaveni s addFilter(....)) 
   */
  public void setFiltered(boolean filtered);
  /**
   * vry6ta zadadebute filter criterii
   * @return
   */
  public List<FilterCriteria> getFilterCriterias();
  
  public int getStartRow();
  public void setStartRow(int startRow);
  public int getRowsCount();
  public void setRowsCount(int rowsCount);
  
  /**
   * @param uncheckedRowsIds - zadava redovete, koito ne trqbva da se vry6tat (obratnoto na checkedRowsIds)
   */
  public void setUncheckedRowsIds(Set<Integer> uncheckedRowsIds);
  /**
   * 
   * @return rowid-tata na redovete koito ne trqbva da se vry6tat
   */
  public Set<Integer> getUncheckedRowsIds();
}
