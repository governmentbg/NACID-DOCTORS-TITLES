package com.nacid.web.model.table;

import com.nacid.bl.table.ColumnHeader;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableRow;
import com.nacid.bl.table.TableState;
import com.nacid.web.handlers.UserOperationsUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TableWebModel {
    public static final int OPERATION_NAME_NEW = UserOperationsUtils.OPERATION_LEVEL_NEW;
    public static final int OPERATION_NAME_EDIT = UserOperationsUtils.OPERATION_LEVEL_EDIT;
    public static final int OPERATION_NAME_VIEW = UserOperationsUtils.OPERATION_LEVEL_VIEW;
    public static final int OPERATION_NAME_DELETE = UserOperationsUtils.OPERATION_LEVEL_DELETE;
    public static final int OPERATION_NAME_ALL = -1;
    
    /**
     * shte pazi podadenite dopylnitelni parametri kym new/edit/view/delete linkovete
     * key - param_type 
     * value - params
     */
    private Map<Integer, String> requestParameters = new HashMap<Integer, String>();
    /**
     * shte pomni koq ot operaciite new/edit/view/delete ne trqbva da se vizualizira
     */
    private Set<Integer> hiddenOperationNames = new HashSet<Integer>();
    private Map<Integer, TableColumnHeaderWebModel> columnHeaders = new LinkedHashMap<Integer, TableColumnHeaderWebModel>();
    private List<TableRowWebModel> rows = new ArrayList<TableRowWebModel>();
    private Map<String, CellFormatter> cellFormatters = new HashMap<String, CellFormatter>();
    /**
     * shte pomni dopylnitelnite parametri, koito mogat da se podavat na form - actiona
     */
    private String additionalFormParams = "";
    private Set<String> hiddenColumnNames = new HashSet<String>();
    private Set<String> hiddenModifiableColumnNames = new HashSet<>();
    private boolean modifyColumns;
    private String orderColumnName;
    private boolean ascendingOrder;
    private boolean onlyChecked;
    private Set<String> checkedRows;
    private String unchekedRowsForInput;
    private int selectedRowBegin;
    private int selectedRowsCount;
    private String allRowsCount;
    private String groupName;
    private String formGroupName;
    private String tableName;
    private int availableRowsCount;
    private boolean viewOpenInNewWindow;
    private boolean hasOperationsColumn = true;

    /**
     * 
     * @param tableName - ako tableName == null - togava <h3></h3> elementa v koito se slaga zaglavieto ne se pokazva!
     */
    public TableWebModel(String tableName) {
        this.tableName = tableName;
    }
    /**
     * @param table - tablica
     * tyi kato columnFormaterite (ako ima nqkakvi) trqbva da se slojat predi da po4nat da se popylvat kakvito i da e danni v
     * tablicata, tezi neshta ne mogat da stoqt v konstruktora!!!
     */
    public void insertTableData(Table table, TableState tableState/*, int startRow, int rowsCount*/) {
        orderColumnName = tableState == null ? null : tableState.getOrderColumn();
        ascendingOrder = tableState == null ? false : tableState.isOrderAscending();
        this.onlyChecked = tableState == null ? false : tableState.isOnlyCheckedRows();
        this.allRowsCount = table.getRowsCount() + "";
        int i = 0;
        for (ColumnHeader h:table.getCoumnHeaders()) {
            columnHeaders.put(i++, new TableColumnHeaderWebModel(h));
        }
        checkedRows = tableState == null ? new HashSet<String>() : getCheckedRowsIds(tableState.getCheckedRowsIds());
        this.unchekedRowsForInput = tableState == null ? "" : StringUtils.join(tableState.getUncheckedRowsIds(), ";");
        selectedRowBegin = tableState == null ? 0 : tableState.getStartRow();
        selectedRowsCount = tableState == null ? table.getRowsCount() : tableState.getRowsCount();

        List<TableRow> tableRows = table.getRows(tableState);
        availableRowsCount = table.getRowsFilteredCount();
        
        /**
         * po podrazbirane skriva kolonata s unique idtata
         */
        hideUnhideColumn(table.getUniqueColumnName(), true);
        
        
        
        
        if (tableRows != null) {
            for (TableRow r:tableRows) {
                TableRowWebModel row = new TableRowWebModel(r.getRowId(), r.getUniqueColumnId(), this, r.isEditable(), r.isDeletable(), r.isViewable(), r.getClazz());
                if (r.getCells() != null && r.getCells().size() > 0) {
                    for (i = 0; i < r.getCells().size(); i++) {
                        String cellFormatterId = columnHeaders.get(i).getColumnNameId();
                        row.addCell(cellFormatters.get(cellFormatterId),  r.getCells().get(i).getCellValue()); 
                    }
                }
                rows.add(row);
            }
        }
    }
    public List<TableColumnHeaderWebModel> getColumnHeaders() {
        List<TableColumnHeaderWebModel> result = new ArrayList<TableColumnHeaderWebModel>();
        for (TableColumnHeaderWebModel m:columnHeaders.values()) {
        	result.add(m);
        }
    	return result; 
    }
    public TableColumnHeaderWebModel getColumnHeader(String headerName) {
    	for (TableColumnHeaderWebModel m:columnHeaders.values()) {
    		if (m.getColumnName().equals(headerName)) {
    			return m;
    		}
    	}
    	return null;
    }
    /**
     * 
     * @param formatterId - edin ot definiranite v CellFormatter
     * Ako za dadena kolona ne se zadade cellFormatter po podrazbirane e plain text
     */
    public void setColumnFormatter(String columnName, int formatterId) {
        if (rows.size() > 0) {
            throw new IllegalArgumentException("You cannot set column formatter if any data is set into table!!!!");
        }
        cellFormatters.put(columnName, CellFormatter.createCellFormatterByFormatterId(formatterId));
    }
    public TableRowWebModel getRow(int rowId) {
        if (rowId >= rows.size()) {
            return null;
        }
        return rows.get(rowId);
    }

    /**
     * vru6ta fakti4eskiq broj na redovete - toj moje da se razli4ava ot selectedRowsCount
     * tyi kato selectedRowsCount vkliu4va i redovete koito ne sa markirani
     * @return
     */
    public int getRowsCount() {
        return rows.size();
    }
    /*public int getColumnsCount() {
        return columnHeaders.size() == 0 ? 0 : columnHeaders.size();
    }*/
    public String getCellValue(int rowId, int columnId) {
        if (rows.size() <= rowId) {
            return "";
        } else if (rows.get(0).getCells().size() <= columnId) {
            return "";
        } else {
            return rows.get(rowId).getCells().get(columnId).getValue();
        }
    }
    public String getOrderColumnName() {
        return orderColumnName;
    }
    public boolean isAscendingOrder() {
        return ascendingOrder;
    }

    /**
     * @return dali daden red e markiran
     */
    public boolean isRowChecked(String uniqueRowId) {
        return checkedRows.contains(uniqueRowId);
    }

    public String getCheckedRowsForInput() {
        return StringUtils.join(checkedRows,";");
    }
    
    public String getUncheckedRowsForInput() {
    	return unchekedRowsForInput;
    }
    /**
     * @return - vyvedenata ot potrebitelq stojnost na rowBegin
     */
    public int getSelectedRowBegin() {
        return selectedRowBegin ;
    }
    /**
     * vyvedenata ot potrebitelq stojnost na rowsCount - razli4ava se ot getRowsCount() - tyi kato tova e stojnostta koqto vyvejda potrebitelq
     * primerno - iska da mu se vizualizirat 20 zapisa kato se zapo4ne ot red 120, no v tablicata ima 122 zapisa
     * togava getSelectedRowsCount() shte vyrne 20, a getRowsCount() shte vyrne 2!!!
     * @return
     */
    public int getSelectedRowsCount() {
        return selectedRowsCount;
    }
    public boolean isOnlyChecked() {
        return onlyChecked;
    }
    private Set<String> getCheckedRowsIds(Set<Integer> checkedRows) {
        if (checkedRows == null) {
            return new TreeSet<String>();
        }
        Set<String> result = new TreeSet<String>();
        for (Integer i:checkedRows) {
            result.add(i + "");
        }
        return result;
    }

    /**
     * vry6ta url-to 4rez koeto se editva zadaden red ot tablicata
     * @return
     */
    public String getGroupName() {
        return groupName;
    }
    /**
     * vry6ta group name-a, koito se izpisva vyv action-a na formata!
     */
    public String getFormGroupName() {
        return StringUtils.isEmpty(formGroupName) ? groupName : formGroupName;
    }
    
    public String getFormAdditionalRequestParams() {
    	return additionalFormParams;
    	
    }
    /**
     * dobavq dopylnitelen parametyr kym formata - ideqta e kato se generira linka
     * form action = "/control/alabala/list", sled list-a da mogat da se podavat dopylnitelni parametri, t.e. list?param1=value1&param2=value2
     */
    public void addFormAdditionalRequestParam(String param, String value) {
    	if (StringUtils.isEmpty(additionalFormParams)) {
    		additionalFormParams = "?";
    	} else {
    		additionalFormParams += "&amp;";
    	}
    	additionalFormParams += param + "=" + value;
    }
    /**
     * zadava groupName-a (neobhodim e za da se generira url-to za editvane...
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public void setFormGroupName(String formGroupName) {
    	this.formGroupName = formGroupName;
    }
    /**
     * vry6ta broq na vsi4ki redove, koito se sydyrjat v tablicata (na nivo business logic)
     * @return
     */
    public String getAllRowsCount() {
        return allRowsCount;
    }

    public String getTableName() {
        return tableName;
    }
    /**
     * vry6ta broq redove predi da e prilojeno filtriraneto startRows - rowCount
     * (ideqta e potrebitelq da znae mejdu kolko redove moje da stranicira!)
     */
    public int getAvailableRowsCount() {
        return availableRowsCount;
    }
    /**
     * dali da slaga prazen red(takyv s .... v nachaloto/kraq na tablicata)
     * @param atStart - true - v nachaloto - false - v kraq
     * @return
     */
    public boolean addEmptyRow(boolean atStart) {
        if (atStart) {
            return selectedRowBegin != 0;
        } else {
            return (selectedRowBegin + selectedRowsCount < availableRowsCount);
        }
    }

    public String getRequestParams(int parameterType) {
        return requestParameters.get(parameterType) == null ? "" : requestParameters.get(parameterType);
    }

    public boolean isViewOpenInNewWindow() {
        return viewOpenInNewWindow;
    }

    /**
     * dobavq request parametri kym linkovete new/edit/view/delete (primerno "id" = "6")
     * @param operationName - edin ot opisanite v {@link TableWebModel} OPERATION_NAME_*
     * @param parameterName - ime na parametyra
     * @param parameterValue - stojnost na parametyra
     */
    public void addRequestParam(int operationName, String parameterName, String parameterValue) {
        if (operationName == OPERATION_NAME_ALL) {
            addRequestParam(OPERATION_NAME_DELETE, parameterName, parameterValue);
            addRequestParam(OPERATION_NAME_NEW, parameterName, parameterValue);
            addRequestParam(OPERATION_NAME_VIEW, parameterName, parameterValue);
            addRequestParam(OPERATION_NAME_EDIT, parameterName, parameterValue);
            return;
        }
        String current = requestParameters.get(operationName);
        if (current == null) {
            current = "";
        }
        //Tyi kato  za momenta "new" nqma parametri po default, pri vyvejdane na pyrviq parametyr trqbva da se sloji ?
        if (operationName == OPERATION_NAME_NEW && current.equals("")) {
            current = "?";    
        } else {
            current += "&amp;";
        }
        current += parameterName + "=" + parameterValue;
        requestParameters.put(operationName, current);
    }
    /**
     * skriva edna ot operaciite v tablicata new/edit/delete
     * @param opreationName - edna ot definiranite v {@link TableWebModel} OPERATION_NAME*
     */
    public void hideOperation(int opreationName) {
    	hiddenOperationNames.add(opreationName);
    }

    public boolean isHasOperationsColumn() {
        return hasOperationsColumn;
    }
    public void setHasOperationsColumn(boolean noOperationsColumn) {
        this.hasOperationsColumn = noOperationsColumn;
    }
    public void setViewOpenInNewWindow(boolean value) {
        viewOpenInNewWindow = value;
    }
    /**
     * @param operationId - edna ot definiranite v {@link TableWebModel} OPERATION_NAME* konstanti, koito syotvetstvat na ekvivalentnite konstanti v {@link UserOperationsUtils} OPERATION_LEVEL_*
     * @return
     */
    public boolean isOperationIdHidden(int operationId) {
    	return hiddenOperationNames.contains(OPERATION_NAME_ALL) || hiddenOperationNames.contains(operationId);
    }
    
    public boolean isColumnHidden(String columnName) {
    	return hiddenColumnNames.contains(columnName) || hiddenModifiableColumnNames.contains(columnName);
    }
    public boolean isColumnIdHidden(int id) {
    	return hiddenColumnNames.contains(columnHeaders.get(id).getColumnName()) || hiddenModifiableColumnNames.contains(columnHeaders.get(id).getColumnName());
    }
    /**
     * skritva ili pokazva colona. Ako tazi kolona e skrita, tq ne moje da se polzva modifiableColumn!
     * @param columnName - ime na kolonata
     * @param hidden - true - skriva kolonata; false - pokazva kolonata
     */
    public void hideUnhideColumn(String columnName, boolean hidden) {
    	if (hidden) {
    		hiddenColumnNames.add(columnName);	
    	} else {
    		hiddenColumnNames.remove(columnName);
    	}
    }
    public void hideUnhideModifiableColumn(String columnName, boolean hidden) {
        if (hidden) {
            hiddenModifiableColumnNames.add(columnName);
        } else {
            hiddenModifiableColumnNames.remove(columnName);
        }
    }

    /**
     * dali shte mogat da se modificirat vidimite koloni prez prilojenieto
     * @param modifyColumns
     * @return
     */
    public void setModifyColumns(boolean modifyColumns) {
        this.modifyColumns = modifyColumns;
    }

    public boolean isModifyColumns() {
        return modifyColumns;
    }

    /**
     * @return spisyk s kolonite, koito mogat da se redaktirat
     */
    public List<String> getModifiableColumnNamesList() {
        return modifyColumns ? getColumnHeaders().stream().filter(r -> !hiddenColumnNames.contains(r.getColumnName())).map(r -> r.getColumnName()).collect(Collectors.toList()) : new ArrayList<>();
    }

    /**
     * @param columnName
     * @return dali dadena kolona, koqto moje da se redaktira e vidima ili ne!
     */
    public boolean isModifiableColumnVisible(String columnName) {
        return !hiddenModifiableColumnNames.contains(columnName);
    }

    /**
     * @return broq na vidimite koloni (broq na vsi4ki koloni, namalen s broq na skritite koloni)
     */
    public int getVisibleColumnsCount() {
    	return getColumnHeaders().size() - hiddenColumnNames.size() - hiddenModifiableColumnNames.size();
    }
    
    
}
