package com.nacid.db.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SQLUtils {
  
  public static String columnsToSetList(String columnList) {
    return columnsToSetList(columnList, 0);
  }
  
  public static String columnsToSetList(String columnList, int skipFirstCols) {
    StringTokenizer tokenizer = new StringTokenizer(columnList, ",");
    StringBuffer result = new StringBuffer();
    int i = 0;
    while (tokenizer.hasMoreTokens()) {
      String colName = tokenizer.nextToken().trim();
      if (++i > skipFirstCols) {
        result.append(colName).append("=? ");
        if (tokenizer.hasMoreTokens()) {
          result.append(", ");
        }
      }
    }
    return result.toString();
  }
  //RayaWritten--------------------------------------------------------
  public static boolean variableNameEqualsColumnName(String variable, String column){
      for(int i=0, j=i ; i<variable.length(); i++, j++){       
          if(i == variable.length()-1 && j != column.length()-1){
              return false;
          } else if(i != variable.length()-1 && j == column.length()-1){
              return false;
          }
          if(Character.isLowerCase(variable.charAt(i))&&variable.charAt(i)!=column.charAt(j)){
              return false;
          } else if(Character.isUpperCase(variable.charAt(i))&&((column.charAt(j)!='_')||
                  Character.toLowerCase(variable.charAt(i))!=column.charAt(++j))){
              return false;
          }
      }
      return true;
  }
  //------------------------------------------------------------------
  public static String parametersCountToParameterList(int parametersCount) {
      List<String> result = new ArrayList<>();
      for (int i = 0; i < parametersCount; i++) {
        result.add("?");
      }
      return StringUtils.join(result, ", ");
  }
  public static String columnsToParameterList(String columnList) {
    return columnsToParameterList(columnList, 0);
  }
  
  public static String columnsToParameterList(String columnList, int skipFirstCols) {
    StringTokenizer tokenizer = new StringTokenizer(columnList, ",");
    StringBuffer result = new StringBuffer();
    while (tokenizer.hasMoreTokens()) {
      tokenizer.nextToken();
      if (skipFirstCols-- <= 0) {
        result.append("?");
        if (tokenizer.hasMoreTokens()) {
          result.append(", ");
        }
      }
    }
    return result.toString();
  }
  
  
  public static String insertTableNameToColumnList(String columnList, String tableName) {
    return insertTableNameToColumnList(columnList, tableName, 0);
  }
  
  public static String insertTableNameToColumnList(String columnList, String tableName, int skipFirstCols) {
    StringTokenizer tokenizer = new StringTokenizer(columnList, ",");
    StringBuffer result = new StringBuffer();
    int i = 0;
    while (tokenizer.hasMoreTokens()) {
      String colName = tokenizer.nextToken().trim();
      if (++i > skipFirstCols) {
        result.append(tableName).append(".").append(colName);
        if (tokenizer.hasMoreTokens()) {
          result.append(", ");
        }
      }
    }
    return result.toString();
  }

  public static String columnSubList(String columnList, int skipFirstCols) {
    StringTokenizer tokenizer = new StringTokenizer(columnList, ",");
    StringBuffer result = new StringBuffer();
    int i = 0;
    while (tokenizer.hasMoreTokens()) {
      String colName = tokenizer.nextToken().trim();
      if (++i > skipFirstCols) {
        result.append(colName);
        if (tokenizer.hasMoreTokens()) {
          result.append(", ");
        }
      }
    }
    return result.toString();
  }
  public static void main(String[] args) {
	  //String USERS_TABLE_COLUMNS = "id, uinsert, dinsert, uupdate, dupdate, cfullname, chostname, clogname, clogpass, itype, istatus, email, tel, credit_amount";
	  //System.out.println(columnSubList(USERS_TABLE_COLUMNS, 1));
      String col = "diploma_fname";
      String var = "diplomaFname";
      System.out.println("Result: "+variableNameEqualsColumnName(var, col));
  }
}
