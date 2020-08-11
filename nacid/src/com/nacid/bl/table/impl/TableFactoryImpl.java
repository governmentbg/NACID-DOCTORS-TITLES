package com.nacid.bl.table.impl;

import com.nacid.bl.table.TableState;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;

public class TableFactoryImpl extends TableFactory{

  @Override
  public Table createTable() {
    return TableImpl.newInstance();
  }
  public TableState createTableState() {
    return TableStateImpl.newInstance();
  }
  
}
