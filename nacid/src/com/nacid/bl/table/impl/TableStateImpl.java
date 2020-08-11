package com.nacid.bl.table.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.TableState;
import com.nacid.bl.table.TableOrderCriteria;

public class TableStateImpl implements TableState {
	private List<FilterCriteria> filterCriterias = new ArrayList<FilterCriteria>();
	private Set<Integer> checkedRowsIds = null;
	private Set<Integer> uncheckedRowsIds = null;
	private TableOrderCriteria orderCriteria;
	private boolean ordered;
	private boolean onlyCheckedRows;
	private boolean filtered;
	private int startRow;
	private int rowsCount;
	private TableStateImpl() {

	}
	public static TableStateImpl newInstance() {
		return new TableStateImpl();
	}
	public void clearAllFilters() {
		filterCriterias = new ArrayList<FilterCriteria>();
		checkedRowsIds = null;
		ordered = false;
		onlyCheckedRows  = false;
		filtered = false;
	}
	public void setOrderCriteria(String columnName, Boolean ascending) {
		boolean newAscending;
		if (orderCriteria == null) {
			orderCriteria = new TableOrderCriteria();
			newAscending = ascending == null ? true : ascending;
		} else {

			/**
			 * 1. Ako ascending == null - proverqva se dali predishnata sortirashta
			 * kolona e sy6tata - ako da obryshta flaga na isAscending(); - ako ne -
			 * slaga true; 2. ako ascending != null - slaga napravo podadenata
			 * stojnost
			 */
			if (ascending == null) {
				String orderColId = getOrderColumn();
				String newOrderColId = columnName;
				newAscending = (newOrderColId != null && newOrderColId.equals(orderColId)) ? !isOrderAscending() : true;
			} else {
				newAscending = ascending;
			}
		}
		orderCriteria.setAscending(newAscending);
		orderCriteria.setColumnName(columnName);
	}

	public String getOrderColumn() {
		if (orderCriteria != null) {
			return orderCriteria.getColumnName();
		}
		return null;
	}

	public void addFilter(String columnName, int cellType, int condition, Object value) throws CellCreationException {
		addFilter(columnName, cellType, condition, value, false);
	}
	public void addFilter(String columnName, int cellType, int condition, Object value, boolean isNot) throws CellCreationException {
		filterCriterias.add(new FilterCriteria(cellType, columnName, condition, value, isNot));
	}

	public void addFilter(String columnName, int cellType, int condition, List<Object> value) throws CellCreationException {
        addFilter(columnName, cellType, condition, value, false);
    }
    public void addFilter(String columnName, int cellType, int condition, List<Object> value, boolean isNot) throws CellCreationException {
        filterCriterias.add(new FilterCriteria(cellType, columnName, condition, value, isNot));
    }
	
	
	public void setCheckedRowsIds(Set<Integer> checkedRows) {
		if (checkedRows == null) {
			checkedRows = new TreeSet<Integer>();
		}
		this.checkedRowsIds = checkedRows;
	}

	public Set<Integer> getCheckedRowsIds() {
		return checkedRowsIds;
	}

	public boolean isOrderAscending() {
		return orderCriteria == null ? false : orderCriteria.isAscending();
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public boolean isOnlyCheckedRows() {
		return onlyCheckedRows;
	}

	public void setOnlyCheckedRows(boolean onlyCheckedRows) {
		this.onlyCheckedRows = onlyCheckedRows;
	}

	public boolean isFiltered() {
		return filtered;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}
	public List<FilterCriteria> getFilterCriterias() {
		return filterCriterias;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getRowsCount() {
		return rowsCount;
	}
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}
	public void setUncheckedRowsIds(Set<Integer> uncheckedRowsIds) {
		this.uncheckedRowsIds = uncheckedRowsIds;
	}
	public Set<Integer> getUncheckedRowsIds() {
		return uncheckedRowsIds == null ? new HashSet<Integer>() : uncheckedRowsIds;
	}


}
