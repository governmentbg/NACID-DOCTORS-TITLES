package com.nacid.web.handlers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import com.nacid.web.model.table.TableWebModel;
import org.apache.commons.lang.StringUtils;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.bl.table.impl.FilterCriteria;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.model.table.filters.CheckBoxFilterWebModel;
import org.springframework.util.CollectionUtils;

public class TableStateAndFiltersUtils {
	public static final TableState createBaseTableState(HttpServletRequest request, Table table) {
		return createBaseTableState(request, table, null);
	}
	/**
	 * syzdava tableState s osnovni parametri - orderColumn, orderAscending, onlyChecked, onlyCheckedRows..... 
	 * @param request
	 * @return
	 */
	public static final TableState createBaseTableState(HttpServletRequest request, Table table, String tablePrefix) {
		TableState tableState = TableFactory.getInstance().createTableState();
		String orderCol = RequestParametersUtils.getParameterOrderColumn(request, tablePrefix);
		if (orderCol != null) {
			tableState.setOrderCriteria(orderCol, RequestParametersUtils.getParameterOrderAscending(request, tablePrefix));
		} else {
			tableState.setOrderCriteria(table.getUniqueColumnName(), false);
		}
		tableState.setCheckedRowsIds(RequestParametersUtils.getParameterCheckedRows(request, tablePrefix));
		tableState.setOnlyCheckedRows(RequestParametersUtils.getParameterOnlyChecked(request, tablePrefix));
		tableState.setOrdered(true);
		tableState.setFiltered(true);
		tableState.setStartRow(RequestParametersUtils.getParameterRowBegin(request, tablePrefix));
		tableState.setRowsCount(RequestParametersUtils.getParameterRowsCount(request, tablePrefix));
		return tableState;
	}
	public final static void addToDateFilterToTableState(HttpServletRequest request, Table table, TableState state) {
		addToDateFilterToTableState(request, table, state, null);
	}
	public final static void addToDateFilterToTableState(HttpServletRequest request, Table table, TableState state, String tablePrefix) {
		boolean addToDateFilter = RequestParametersUtils.getParameterToDateFilter(request, tablePrefix); 
		if (table == null || !addToDateFilter) {
			return;
		}
		try {
			state.addFilter("До дата", table.getCoulmnHeader("До дата").getColumnCellsType(), FilterCriteria.CONDITION_EQUALS, (Object)null);
		} catch (CellCreationException e) {
			throw Utils.logException(e);
		}
	}
	public static CheckBoxFilterWebModel getToDateFilterWebModel(HttpServletRequest request) {
		return getToDateFilterWebModel(request, null);
	}
	public static CheckBoxFilterWebModel getToDateFilterWebModel(HttpServletRequest request, String tablePrefix) {
		return new CheckBoxFilterWebModel(RequestParametersUtils.PARAM_NAME_TO_DATE_FILTER, 
				"Само активните", 
				RequestParametersUtils.getParameterToDateFilter(request, tablePrefix));
	}

	/**
	 * shte dobavq prost combo filter kym tablicata<br />
	 * primerno (trqbva da se dobavi filter na tablicata n_speciality po professionGroupName - togava o4akvam filter-a v requesta da se kazva professionGroupNameFilter, a kolonata professionGroupName
	 * @param filterName - imeto na poleto 
	 * @param columnName - imeto na kolonata kym koqto trqbva da se dobavi filter-a
	 * @param request
	 * @param table
	 * V momenta tozi method razbira samo ot INTEGER types, kato vseki drug vid se vyzpriema kato String
	 */
	public static void addComboFilterToTableState(String filterName, String columnName, HttpServletRequest request, Table table, TableState state) {
		String filterValue = request.getParameter(filterName);
		if (filterValue == null || "-".equals(filterValue)) {
			return;
		}
		try {
			if (table.getCoulmnHeader(columnName).getColumnCellsType() == CellValueDef.CELL_VALUE_TYPE_INTEGER) {
				state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_EQUALS, DataConverter.parseInteger(filterValue, null));
			} else if(table.getCoulmnHeader(columnName).getColumnCellsType() == CellValueDef.CELL_VALUE_TYPE_BOOLEAN) {
				state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_EQUALS, DataConverter.parseBoolean(filterValue));
			} else {
				state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_EQUALS, filterValue);	
			}
			  
		} catch (CellCreationException e) {
			throw Utils.logException(e);
		}

	}


	public static void addContainsFilterToTableState(String filterName, String columnName, HttpServletRequest request, Table table, TableState state) {
		String filterValue = request.getParameter(filterName);
		if (filterValue == null || "".equals(filterValue)) {
			return;
		}
		try {
			state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_CONTAINS, filterValue);
		} catch (CellCreationException e) {
			throw Utils.logException(e);
		}

	}
	/**
	 * dpbavq "ili" filter kym tableState. Zasega takova neshto ima v spisyk na razpredelenite zaqvleniq - imena na expert! 
	 * @param filterName
	 * @param columnName
	 * @param request
	 * @param table
	 * @param state
	 */
	public static void addComplexEqualsFilterToTableState(String filterName, String columnName, HttpServletRequest request, Table table, TableState state) {
        try {
            if (table.getCoulmnHeader(columnName).getColumnCellsType() == CellValueDef.CELL_VALUE_TYPE_INTEGER) {
                Set<Integer> selectedIds = RequestParametersUtils.convertRequestParameterToIntegerTreeSet(request.getParameter(filterName + "Ids"));
                Integer selectedId = DataConverter.parseInteger(request.getParameter(filterName), null);
                if (selectedId != null) {
                    if (selectedIds == null) {
                        selectedIds = new TreeSet<Integer>();
                    }
                    selectedIds.add(selectedId);
                }
                if (selectedIds == null) {
                    return;
                }
                state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_EQUALS, new ArrayList<Object>(selectedIds));
            } else {
                Set<String> selectedIds = RequestParametersUtils.convertRequestParamToList(request.getParameter(filterName + "Ids"));
                String selectedId = request.getParameter(filterName);
                if (!StringUtils.isEmpty(selectedId) && !"-".equals(selectedId)) {
                    if (selectedIds == null) {
                        selectedIds = new TreeSet<String>();
                    }
                    selectedIds.add(selectedId);
                }
                if (selectedIds == null) {
                    return;
                }
                state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_EQUALS, new ArrayList<Object>(selectedIds));    
            }
            
        } catch (CellCreationException e) {
            throw Utils.logException(e);
        }
    }
	
	
	/**
	 * @param filterName
	 * @param columnName
	 * @param request
	 * @param table
	 * @param state
	 * tozi method razbira ot Integer i Boolean filters, kato vseki drug tip se cast-va do string
	 */
	public static void addEqualsFilterToTableState(String filterName, String columnName, HttpServletRequest request, Table table, TableState state) {
		String filterValue = request.getParameter(filterName);
		if (filterValue == null || "".equals(filterValue)) {
			return;
		}
		try {
			if(table.getCoulmnHeader(columnName).getColumnCellsType() == CellValueDef.CELL_VALUE_TYPE_BOOLEAN) {
				state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), 
						FilterCriteria.CONDITION_EQUALS, DataConverter.parseBoolean(filterValue));
			} else if (table.getCoulmnHeader(columnName).getColumnCellsType() == CellValueDef.CELL_VALUE_TYPE_INTEGER) {
				state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), 
						FilterCriteria.CONDITION_EQUALS, DataConverter.parseInteger(filterValue, null));
			}
			else {
				state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_EQUALS, filterValue);
			}
		} catch (CellCreationException e) {
			throw Utils.logException(e);
		}

	}

	public static void addStartsWithFilterToTableState(String filterName, String columnName, HttpServletRequest request, Table table, TableState state) {
		String filterValue = request.getParameter(filterName);
		if (filterValue == null || "".equals(filterValue)) {
			return;
		}
		try {
			state.addFilter(columnName, table.getCoulmnHeader(columnName).getColumnCellsType(), FilterCriteria.CONDITION_STARTS_WITH, filterValue);
		} catch (CellCreationException e) {
			throw Utils.logException(e);
		}

	}

	/**
	 * ne znam dali mqstoto na tozi method beshe tuk, zashtoto predi tova tozi klas izobshto ne se zanimavashe s TableWebModel!!!
	 * @param wm
	 * @param request
	 * @param prefix
	 */
	public static void setHiddenModifiableColumns(TableWebModel wm, HttpServletRequest request, String prefix) {
		List<String> columns = RequestParametersUtils.getParameterHiddenModifiableColumns(request, null);
		if (!CollectionUtils.isEmpty(columns)) {
			columns.forEach(r -> wm.hideUnhideModifiableColumn(r, true));
		}
	}





}
