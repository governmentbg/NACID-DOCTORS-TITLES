package com.nacid.bl.table.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValue;
import com.nacid.bl.table.ColumnHeader;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableRow;
import com.nacid.bl.table.TableState;

public class TableImpl implements Table{
  
  private List<TableRowImpl> rows;
  /**
   * key - columnName
   * value - columnHeader
   * ideqta e 4e veroqtno novite filtri shte idvat ot potrebitelq 4rez ime na colonata i za da moje po-lesno da se tyrsi header-a na kolonata
   */
  private Map<String, ColumnHeader> columnHeaders = new LinkedHashMap<String, ColumnHeader>();
  /**
   * pomni ID-tata na redovete, koito sa markirani kato checked
   */
  private String uniqueColumnName;
  private int rowsFilteredCount;
  private boolean rowsFilteredCountCalculated = false;
  /**
   * Konstruktora na TableImpl ne se vijda ot vyn za da ne moje da se syzdava takyv obekt s new()
   * takyv obekt moje da se syzdava samo s TableFactory.createTable();
   */
  protected TableImpl() {
  }
  static TableImpl newInstance() {
    return new TableImpl();
  }
  
  public void setUniqueColmn(String columnName) {
    if (rows != null) {
      throw new IllegalArgumentException("You cannot set unique column if data is already imported into the table! ");
    }
    this.uniqueColumnName = columnName;
  }
  public String getUniqueColumnName() {
	  if (uniqueColumnName != null && columnHeaders.get(uniqueColumnName) != null) {
		  return uniqueColumnName;  
	  }
	  return (columnHeaders.values().iterator().next()).getColumnName();
	  
  }

  public void addColumnHeader(String columnName, int cellValuesType) throws IllegalArgumentException{
    if (rows != null) {
      throw new IllegalArgumentException("You cannot add column header if data is already set to the table! ");
    }
    //Ako tova e pyrvata kolonq koqto se sett-va i nqma settnata uniqueColumnName, togava uniqueColumnName = pyrvata kolona
    if (columnHeaders.size() == 0 && uniqueColumnName == null) {
        uniqueColumnName = columnName;
    }
    columnHeaders.put(columnName, new ColumnHeaderImpl(columnHeaders.size(), columnName, cellValuesType));
  }


  public TableRow addRow(Object... cellValues)  throws IllegalArgumentException, CellCreationException {
    TableRowImpl row = new TableRowImpl(this, rows == null ? 0 : rows.size());
    if (cellValues.length != columnHeaders.size()) {
      throw new IllegalArgumentException("Columns count for header and for given row don't match! headers:" + columnHeaders.size() + " values:" + cellValues.length);
    }
    for (String s:columnHeaders.keySet()) {
      ColumnHeader currentColumnHeader = columnHeaders.get(s);
      row.addCellValue(cellValues[currentColumnHeader.getColumnId()], currentColumnHeader.getColumnCellsType());
    }
    addRow(row);
    return row;
  }
  private void addRow(TableRowImpl row) {
    if (row == null) {
      return;
    }
    if (rows == null) {
      rows = new ArrayList<TableRowImpl>();
    }
    rows.add(row);
  }
  public synchronized List<TableRow> getRows(final TableState tableState) {
    if (rows == null) {
      rowsFilteredCount = 0;
      rowsFilteredCountCalculated = true;
      return new ArrayList<TableRow>();
      
    }
    List<TableRowImpl> result = getRowsOnlyFiltered(tableState);
    rowsFilteredCount = result.size();
    rowsFilteredCountCalculated = true;
    if (tableState != null) {
      if (tableState.isOrdered() && tableState.getOrderColumn() != null) {
        Collections.sort(result, createRowComparator(tableState));
      }  
    } else {
      result = rows;
    }
    
    List<TableRow> res = new ArrayList<TableRow>();
    for (TableRowImpl r:result) {
      res.add(r);
    }
    if (tableState == null) {
    	return res;
    }

    int sr = 0;
    int rc;
    if (tableState.getStartRow() != 0 && tableState.getStartRow() < res.size()) {
      sr = tableState.getStartRow();
    }
    //TODO: Kakyv e bil smisyla na posledniq && sr <= rowsCount, zashtoto ne srabotva v sledniq slu4aj: 
    //obshto redove 751, iskat se 200 reda kato se zapo4ne ot 400. - vry6tat se poslednite 351 reda....
    if (tableState.getRowsCount() != 0 && ((tableState.getRowsCount() + sr) <= res.size()) /*&& sr <= rowsCount*/) {
      rc = tableState.getRowsCount();
    } else {
      rc = res.size() - sr;
    }
    return res.subList(sr, sr + rc);
    
    

  }
  /**
   * vry6ta redovete samo filtrirani bez da im se prilaga orderCriteria i startRow-rowsCount!
   * @param tableState
   * @return
   */
  private List<TableRowImpl> getRowsOnlyFiltered(final TableState tableState) {
	  List<TableRowImpl> result = new ArrayList<TableRowImpl>();
	  if (tableState != null) {
		  for (TableRowImpl row:rows) {
			  //Ako uncheckedRowIds(redovete, koito ne trqbva da se vry6tat) ne sydyrja current rowId
			  if (tableState.getUncheckedRowsIds() == null || (tableState.getUncheckedRowsIds() != null && !tableState.getUncheckedRowsIds().contains(row.getRowId()))) {
				  //Ako ne trqbva da se vry6tat samo checknatite redove ili checkedRowsIds sydyrjat id-to na teku6tiq rowId
				  if (!tableState.isOnlyCheckedRows() || (tableState.isOnlyCheckedRows() && tableState.getCheckedRowsIds() != null && tableState.getCheckedRowsIds().contains(row.getRowId()))) {
					  //Ako rezultata ne trqbva da vry6ta samo filtriranite redove ili reda match-va vsi4kite filtri
					  if (!tableState.isFiltered() || (tableState.isFiltered() && match(row, tableState)) ) {
						  result.add(row);  
					  }
				  }				  
			  }

		  }
	  } else {
		  result = rows;
	  }
	  return result;

  }
  private Comparator<TableRowImpl> createRowComparator(final TableState filter) {
    //System.out.println("inside create comparator!");
    return new Comparator<TableRowImpl>(){
      public int compare(TableRowImpl o1, TableRowImpl o2) {
        int orderColId = getColumnIdByName(filter.getOrderColumn());
        if (orderColId == -1) {
          return 0;
        }
        int res;
        if (o1 == null || o2 == null || o2.getCell(orderColId) == null || o1.getCell(orderColId) == null) {
          res = -1;
        } else {
          res = o1.getCell(orderColId).compareTo(o2.getCell(orderColId));
        }
        return filter.isOrderAscending() ? res : - res;
      }
    };
  }
  public int getRowsCount() {
    return rows == null ? 0 : rows.size();
  }
  public List<ColumnHeader> getCoumnHeaders() {
    List<ColumnHeader> result = new ArrayList<ColumnHeader>();
    for (String s:columnHeaders.keySet()) {
      result.add(columnHeaders.get(s));
    }
    return result;
  }
  private boolean match(TableRowImpl row, TableState filter) {
    for (FilterCriteria f:filter.getFilterCriterias()) {
      int columnId = getColumnIdByName(f.getColumnName());
      if (columnId != -1 && !FilterMatcher.match(f, row.getCell(columnId).getCellValue())) {
        return false;
      }
    }
    return true;
  }
  /**
   * @param columnName 
   * @return - vry6ta columnId-to na kolonata s ime columnName
   */
  private int getColumnIdByName(String columnName) {
    ColumnHeader header = columnHeaders.get(columnName);
    if (header == null) {
      return -1;
    }
    return header.getColumnId();
  }

  public int getUniqueColumnId() {
    //return uniqueColumnName == null ? 0 : columnHeaders.get(uniqueColumnName) == null ? 0 : columnHeaders.get(uniqueColumnName).getColumnId();
	  return getCoulmnHeader(getUniqueColumnName()).getColumnId();
  }
  
  public ColumnHeader getCoulmnHeader(String columnName) {
    return columnHeaders.get(columnName);
  }
  
  public void emtyTableData() {
    this.rows = new ArrayList<TableRowImpl>();
    //checkedRowsIds = null;
  }
  
  /**
   * ako se izvika getRowsFiltered i rowsFilteredCountCalcuted == false - se hvyrlq exception!
   * tozi parametyr se settva na true sled kato se izvika getRows(...);
   * ako ima, se dava stojnostta koqto e v nego!
   */
  public synchronized int getRowsFilteredCount() {
    if (rowsFilteredCountCalculated) {
      rowsFilteredCountCalculated = false;
      return rowsFilteredCount;  
    } else {
      throw new IllegalArgumentException();
    }
    
  }
  public Set<Integer> getRowIdsByUniqueColumnValues(Collection<Object> uniqueColumnValues) throws CellCreationException {
	  ColumnHeader header = getCoulmnHeader(getUniqueColumnName());
	  Map<Object, Integer> uniqueColumnValuesToRowIds = new HashMap<Object, Integer>();
	  for (TableRow r:rows) {
		  uniqueColumnValuesToRowIds.put(r.getCell(getUniqueColumnId()).getCellValue().getValue(), r.getRowId());
	  }
	  Set<Integer> result = new LinkedHashSet<Integer>();
	  for (Object o:uniqueColumnValues) {
		  CellValue cellValue = CellValue.createCellValue(o, header.getColumnCellsType());
		  Integer r = uniqueColumnValuesToRowIds.get(cellValue.getValue());
		  if (r != null) {
			  result.add(r);
		  }
	  }
	  return result;
  }
}
