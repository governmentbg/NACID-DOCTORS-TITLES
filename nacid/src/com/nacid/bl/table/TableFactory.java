package com.nacid.bl.table;

import com.nacid.bl.table.impl.TableFactoryImpl;
/**
 * @author ggeorgiev
 * Tova e otpravnata to4ka za syzdavane na tables
 * tablica se syzdava 4rez 
 * TableFactory factory = TableFactory.newInstance();
 * Table table = factory.createTable();
 * 
 */

public abstract class TableFactory {
  private volatile static TableFactoryImpl tableFactory;
  public static TableFactory getInstance() {
    if (tableFactory == null) {
      synchronized (TableFactory.class) {
        if (tableFactory == null) {
          tableFactory = new TableFactoryImpl();
        }
      }
    }
    return tableFactory;
  }
  public abstract Table createTable();
  public abstract TableState createTableState();
}
