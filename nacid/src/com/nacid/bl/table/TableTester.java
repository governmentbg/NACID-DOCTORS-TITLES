package com.nacid.bl.table;

import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.table.impl.FilterCriteria;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.db.utils.StandAloneDataSource;

public class TableTester {

  /**
   * @param args
   * @throws CellCreationException 
   * @throws IllegalArgumentException 
   */
  public static void main(String[] args) throws IllegalArgumentException, CellCreationException {
    Table table = TableFactory.getInstance().createTable();
    NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
    UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
    table.addColumnHeader("fullname", CellValueDef.CELL_VALUE_TYPE_STRING);
    table.addColumnHeader("username", CellValueDef.CELL_VALUE_TYPE_STRING);
    table.addColumnHeader("email", CellValueDef.CELL_VALUE_TYPE_STRING);
    table.addColumnHeader("status", CellValueDef.CELL_VALUE_TYPE_INTEGER);
    List<? extends User> users = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_ID);
    for (User u:users) {
      table.addRow(u.getFullName(), u.getUserName(), u.getEmail(), u.getStatus());
    }
   TableState tablestate = TableFactory.getInstance().createTableState();
   
   tablestate.addFilter("fullname", CellValueDef.CELL_VALUE_TYPE_STRING, FilterCriteria.CONDITION_CONTAINS , "");
   //tablestate.addFilter("status", CellValueDef.CELL_VALUE_TYPE_INTEGER, FilterCriteria.CONDITION_EQUALS , 1);
   tablestate.setFiltered(true);
   List<TableRow> rows = table.getRows(tablestate);
   for (TableRow r:rows) {
     for (TableCell c:r.getCells()) {
       System.out.print(c.getCellValue().getValue() + "\t");
     }
     System.out.println();
   }
  }

}
