package com.nacid.bl.table.impl;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.TableCell;
import com.nacid.bl.table.TableRow;
class TableRowImpl implements /*Cloneable,*/ TableRow/*, Comparable<TableRowImpl>*/{
    private TableImpl table;
    private List<TableCellImpl> tableCells;
    private int rowId;
    private boolean editable = true;
    private boolean deletable = true;
    private boolean viewable = true;
    private String clazz;
    public TableRowImpl(TableImpl table, int rowId) {
        this.table = table;
        this.rowId = rowId;
    }

    /**
     * tozi method mai ve4e nqma da se izpolzva
     * tyi kato kletkite shte se podavat edna po edna, zashtoto ve4e shte im se podava i tipa na dannite!
     * @param values
     */
    /*public void setCellValues(Object... values) {
    if (values == null || values.length == 0) {
      return;
    }
    if (tableCells == null) {
      tableCells = new ArrayList<TableCellImpl>();
    }
    int currentCellIdx = tableCells.size();
    for (Object obj:values) {
      //System.out.println(obj);
      TableCellImpl cell = new TableCellImpl(this, obj);
      tableCells.add(cell);
    }
  }*/
    public void addCellValue(Object object, int cellValueType) throws CellCreationException {
        if (tableCells == null) {
            tableCells = new ArrayList<TableCellImpl>();
        }
        tableCells.add(new TableCellImpl(this, object, cellValueType));
    }
    public TableCellImpl getCell(int columnId) {
        if (tableCells == null || tableCells.size() < columnId ) {
            return null;
        }
        return tableCells.get(columnId);
    }
    public List<TableCell> getCells() {
        List<TableCell> result = new ArrayList<TableCell>();
        for (TableCellImpl c:tableCells) {
            result.add(c);
        }
        return result;
    }
    public int getRowId() {
        return rowId;
    }
    public TableCellImpl getUniqueCellValue() {
        return getCell(table.getUniqueColumnId());
    }
    public int getUniqueColumnId() {
        return table.getUniqueColumnId();
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    public void setViewable(boolean viewable) {this.viewable = viewable;}
    public boolean isEditable() {
        return editable;
    }

    @Override
    public boolean isDeletable() {

        return deletable;
    }
    @Override
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isViewable() {
        return viewable;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
    
}
