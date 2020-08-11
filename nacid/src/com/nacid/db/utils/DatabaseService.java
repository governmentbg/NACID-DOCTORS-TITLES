package com.nacid.db.utils;

import com.nacid.bl.impl.Utils;
import com.nacid.data.MethodsUtils;
import com.nacid.data.common.BinaryStream;
import com.nacid.data.common.LongValue;
import com.nacid.data.common.ResultRow;
import com.nacid.data.config.DataConfigFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DatabaseService {
  
  protected DataSource ds;
  protected static DataConfigFactory dataConfigFactory = DataConfigFactory.getInstance();
  protected static Logger logger = Logger.getLogger(DatabaseService.class);
  //private static Map<String, PreparedStatement> preparedStatementsMap = new HashMap<String, PreparedStatement>();
  
  protected DatabaseService(DataSource ds) {
    this.ds = ds;
  }
  
  protected Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
  
  protected void release(Connection connection, PreparedStatement ps, ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (Exception e) {
          throw Utils.logException(e);
      }
    }
    if (ps != null) {
      try {
        ps.close();
      } catch (Exception e) {
          throw Utils.logException(e);
      }
    }
    release(connection);
  }
  
  protected void release(Connection connection) {
    try {
      connection.close();
    } catch (Exception e) {
        throw Utils.logException(e);
    }
  }
  
  /**
   * generira SQL za update-vane na zapis
   * zapisa trqbva da e definiran v com/nacid/data/config/xml/config.xml
   * @param o
   * @return
   */
  private static String generateUpdateSql(Object o) {
    List<String> columnNames = dataConfigFactory.getColumnNames(o.getClass().getName());
    String r = StringUtils.join(columnNames, ", ");
    return "UPDATE " + dataConfigFactory.getTableName(o.getClass().getName()) + " SET " + SQLUtils.columnsToSetList(r, 1) + " WHERE " + columnNames.get(0) + " = ?"; 
  }
  //RayaWritten-----------------------------------------------------------------
  /**
   * Generate update string so to be used to update a record
   * @param o - the object that is used
   * @param columns - the columns in the database that are to be updated
   */
  private static String generateUpdateSql(Object o, String...columns){
      List<String> columnNames = dataConfigFactory.getColumnNames(o.getClass().getName());
      List<String> myColumnNames = new ArrayList<String>();
      myColumnNames.add(columnNames.get(0));
      for(int i=0; i<columns.length; i++){
          for(int j=1; j<columnNames.size(); j++){
              if(columnNames.get(j).equals(columns[i])){
                  myColumnNames.add(columnNames.get(j));
              }
          }
      }
      String r = StringUtils.join(myColumnNames, ", ");
      return "UPDATE "+dataConfigFactory.getTableName(o.getClass().getName())+" SET "+SQLUtils.columnsToSetList(r, 1)+" WHERE "+columnNames.get(0)+" = ? ";
  }
  //----------------------------------------------------------------------------
  /**
   * update-va Object, koito e definiran predvaritelno v com/nacid/data/config/xml/config.xml
   * @param o
   * @throws SQLException
   */
  public void updateRecord(Object o) throws SQLException {
    if (o == null) {
      return;
    }
    Connection connection = getConnection();
    try {
      PreparedStatement p = connection.prepareStatement(generateUpdateSql(o));
      prepareUpdateStatement(o, p);
      logger.debug(p);
      try {
        p.executeUpdate();
      } finally {
        p.close();
      }
    } finally {
      release(connection);
    }
  }
  //RayaWritten--------------------------------------------------------------
  /**
   * Update just the columns specified by using the object's values in the corresponding fields
   * @param o - the object used for the update
   * @param columns - the columns to be updated
   * @throws SQLException
   */
  public void updateRecord(Object o, String...columns) throws SQLException{
      if (o == null) {
          return;
      }
      Connection connection = getConnection();
      try {
          PreparedStatement p = connection.prepareStatement(generateUpdateSql(o, columns));
          prepareUpdateStatement(o, p, columns);
          try {
              p.executeUpdate();
          } finally {
              p.close();
          }
      } finally {
            release(connection);
      }
  }
  //------------------------------------------------------------------------
  /**
   * prepere-va PreparedStatement-a s poletata za update!
   * @param o - trqbva da e definiran v com/nacid/data/config/xml/config.xml
   * @param p
   */
  private static void prepareUpdateStatement(Object o, PreparedStatement p) {
    int j = 1;
    List<String> variableNames = dataConfigFactory.getVariableNames(o.getClass().getName());
    for (int i = 1; i <= variableNames.size(); i++) {
      j = i == variableNames.size() ? 0 : i;
      String currentVariableName = variableNames.get(j);
      try {
        Method m = MethodsUtils.getGetterMethod(o, currentVariableName);
        //addDataToStatement(i, p, m.getReturnType(), m.invoke(o));
        //p.setObject(i, m.invoke(o));
        addDataToStatement(i, p, m.getReturnType(), m.invoke(o));
      } catch (Exception e) {
        throw new RuntimeException("Problem invoking getter method for variable " + currentVariableName, e);
      }
    }
  }
  //RayaWritten-------------------------------------------------------------
  /**
   * Prepare the statement by setting the values
   * @param o - the used object
   * @param p - the prepared statement
   * @param columns - the columns in the database to be updated
   */
  private static void prepareUpdateStatement(Object o, PreparedStatement p, String...columns) {
      int j = 0;
      boolean flag = false;
      List<String> variableNames = dataConfigFactory.getVariableNames(o.getClass().getName());
      List<String> myVariableNames = new ArrayList<String>();
      myVariableNames.add(variableNames.get(0));
      for(int k=0; k<columns.length; k++){
          flag = false;
          for(int m=1; m<variableNames.size(); m++){
              if(SQLUtils.variableNameEqualsColumnName(variableNames.get(m), columns[k])){
                  myVariableNames.add(variableNames.get(m));
                  flag=true;
              }
          }
          if(!flag){
              DatabaseService.logger.debug("There is a column you entered that doesn't match any of the table columns   ColumnName:" + columns[k] + "  objectClass:" + o.getClass());
          }
      }
      for (int i = 1; i <= myVariableNames.size(); i++) {
          j = i == myVariableNames.size() ? 0 : i;
          String currentVariableName = myVariableNames.get(j);
          try {
              Method m = MethodsUtils.getGetterMethod(o, currentVariableName);
              addDataToStatement(i, p, m.getReturnType(), m.invoke(o));
          } catch (Exception e) {
              throw new RuntimeException("Problem invoking getter method for variable " + currentVariableName, e);
          }
      }
  }
  //-------------------------------------------------------------------------
  
  /**
   * insert-va Object, koito e definiran predvaritelno v com/nacid/data/config/xml/config.xml
   * Stojnostta na unikalnata kolona se generira ot sequence
   * @param o
   * @throws SQLException
   */
  public <T> T insertRecord(T o)  throws SQLException {
	  return insertRecord(o, true);
  }
  /**
   * insert-va Object, koito e definiran predvaritelno v com/nacid/data/config/xml/config.xml
   * @param <T>
   * @param o
   * @param autoGenerateUniqueColumnFromSequence - kazva dali unikalnata kolona shte se generira ot sequence ili shte se vyvejda
   * @return
   * @throws SQLException
   */
  public <T> T insertRecord(T o, boolean autoGenerateUniqueColumnFromSequence)  throws SQLException {
    if (o == null) {
      return null;
    }
    Connection connection = getConnection();
    try {
      String sql = generateInsertSql(o, autoGenerateUniqueColumnFromSequence);
      logger.debug(sql);
      PreparedStatement p = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      prepareInsertStatement(o, p, autoGenerateUniqueColumnFromSequence);
      try {
        if (p.executeUpdate() > 0) {
          ResultSet rs = p.getGeneratedKeys();
          try {
        	  if (rs.next()) {
                  Object returnValue = rs.getObject(dataConfigFactory.getUniqueColumnName(o.getClass().getName()));
                  String uniqueVariableName = dataConfigFactory.getUniqueVariableName(o.getClass().getName());
                    try {
                      MethodsUtils.getSetterMethod(o, uniqueVariableName).invoke(o, returnValue);
                    } catch (IllegalArgumentException e) {
                      throw new RuntimeException("No setter method configured for variable ...." + uniqueVariableName + " on class" + o, e);
                    } catch (SecurityException e) {
                      throw new RuntimeException("No setter method configured for variable ...." + uniqueVariableName + " on class" + o, e);
                    } catch (IllegalAccessException e) {
                      throw new RuntimeException("Illegal access for variable ...." + uniqueVariableName + " on class " + o, e);
                    } catch (InvocationTargetException e) {
                      throw new RuntimeException("No setter method configured for variable ...." + uniqueVariableName + " on class" + o, e);
                    } catch (NoSuchMethodException e) {
                      throw new RuntimeException("No setter method configured for variable ...." + uniqueVariableName + " on class" + o, e);
                    }
                    return o;
                   
                }
          } finally {
        	  rs.close();
          }
          
        }
        return null;
      } catch (IllegalArgumentException e) {
        throw (new SQLException("Error while trying to execute insert query.\n SQL:" + p, e));
      } catch (SQLException e) {
        throw (new SQLException("Error while trying to execute insert query.\n SQL:" + p, e));
      } finally {
        p.close();
      }
    } finally {
      release(connection);
    }
  }
  
  
  /**
   * generira SQL za insert-vane na zapis
   * zapisa trqbva da e definiran v com/nacid/data/config/xml/config.xml
   * @param o
   * @return
   */
  private static String generateInsertSql(Object o, boolean generateUniqueColumnFromSequence) {
    String r = StringUtils.join(dataConfigFactory.getColumnNames(o.getClass().getName()), ", ");
    return "INSERT INTO " + dataConfigFactory.getTableName(o.getClass().getName()) + " (" + SQLUtils.columnSubList(r, generateUniqueColumnFromSequence ? 1 : 0) + ") VALUES ( " + SQLUtils.columnsToParameterList(r, generateUniqueColumnFromSequence ? 1 : 0) + " )";
  }
  
  
  private static void prepareInsertStatement(Object o, PreparedStatement p, boolean generateUniqueColumnFromSequence) {
    int j = 1;
    List<String> variableNames = dataConfigFactory.getVariableNames(o.getClass().getName());
    for (int i = generateUniqueColumnFromSequence ? 1 : 0; i < variableNames.size(); i++) {
      String currentVariableName = variableNames.get(i);
      try {
        Method m = MethodsUtils.getGetterMethod(o, currentVariableName);
        //addDataToStatement(j++, p, m.getReturnType(), m.invoke(o));
        //p.setObject(j++, m.invoke(o));
        addDataToStatement(j++, p, m.getReturnType(), m.invoke(o));
      } catch (Exception e) {
        throw new RuntimeException("Problem invoking getter method for variable " + currentVariableName + " on class" + o.getClass(), e);
      }
    }
  }
  public <T> T selectRecord(Class<T> cls, String condition, Object... parameters) throws SQLException{
      List<T> res = selectRecords(cls, condition, parameters);
      if (res == null || res.size() == 0) {
          return null;
      }
      return res.get(0);
      
  }
  /**
   * @param object - obekta v koito shte se popylvat dannite!!!!!
   * @param uniqueColumnValue
   * @return
   * @throws SQLException
   * Primer :
   * SpecialityRecord record = new SpecialityRecord();
   * record = (SpecialityRecord) selectRecord(record, 100000);
   * shte vyrne SpecialityRecord-a, v koito id=to e 100000; 
   */
  public <T> T selectRecord(T object, Object uniqueColumnValue) throws SQLException{
    String className = object.getClass().getName();
    String sql = " SELECT " + StringUtils.join(dataConfigFactory.getColumnNames(className), " , ") + " FROM " + dataConfigFactory.getTableName(className) + " WHERE " + dataConfigFactory.getUniqueColumnName(className) + " = ?";
    Connection connection = getConnection();
    //System.out.println("SQL:" + sql);
	//System.out.println("Params:" + uniqueColumnValue);
    try {
      /*PreparedStatement p = preparedStatementsMap.get(className);
      if (p == null) {
    	  p = connection.prepareStatement(sql);
      }*/
    	//System.out.println(sql + " " + uniqueColumnValue);
    	PreparedStatement p = connection.prepareStatement(sql);
      
      
      try {
        int arg = 1;
        //addDataToStatement(arg++, p, uniqueColumnValue.getClass(), uniqueColumnValue);
        //p.setObject(arg++, uniqueColumnValue);
        addDataToStatement(arg++, p, uniqueColumnValue == null ? null : uniqueColumnValue.getClass(), uniqueColumnValue);
        ResultSet rs = p.executeQuery();
        try {
          if (rs.next()) {
            List<String> columnNames = dataConfigFactory.getColumnNames(className);
            List<String> variableNames = dataConfigFactory.getVariableNames(className);
            for (int i = 0; i < dataConfigFactory.getColumnNames(className).size(); i++) {
              try {
                //MethodsUtils.getSetterMethod(object, variableNames.get(i)).invoke(object, rs.getObject(columnNames.get(i)));
                Class<?> returnType = MethodsUtils.getGetterMethod(object, variableNames.get(i)).getReturnType();
                MethodsUtils.getSetterMethod(object, variableNames.get(i)).invoke(object, convertResultSetObject(rs, returnType, columnNames.get(i)));
              } catch (SQLException e) {
                throw e;
              } catch (Exception e) {
                throw new RuntimeException("Problem invoking getter/setter/ method for variableName " + variableNames.get(i) + " on class" + object.getClass(), e);
              }
            }
            return object;
          }
          return null;
        } finally {
          rs.close();
        }
      } catch (IllegalArgumentException e) {
        throw (new SQLException("Error while trying to execute selectRecord query.\n RecordType:" + object.getClass().getName() + "\n SQL:" + p, e));
      } catch (SQLException e) {
        throw (new SQLException("Error while trying to execute selectRecord query.\n RecordType:" + object.getClass().getName() + "\n SQL:" + p, e));
      } finally {
        p.close();
      }
    } finally {
      release(connection);
    }
  }
  /**
   * @param cls - obekta ot koito shte se iztriva
   * @param uniqueColumnValue - stojnostta na unikalnata kolona, koqto shte se iztriva
   * @throws SQLException
   */
  public <T> void deleteRecord(Class<T> cls, Object uniqueColumnValue) throws SQLException{
    String className = cls.getName();
    String sql = " DELETE FROM " + dataConfigFactory.getTableName(className) + " WHERE " + dataConfigFactory.getUniqueColumnName(className) + " = ?";
    logger.debug("SQL:" + sql);
    logger.debug("Param:" + uniqueColumnValue);
    Connection connection = getConnection();
    try {
      PreparedStatement p = connection.prepareStatement(sql);
      try {
        int arg = 1;
        //addDataToStatement(arg++, p, uniqueColumnValue.getClass(), uniqueColumnValue);
        //p.setObject(arg++, uniqueColumnValue);
        addDataToStatement(arg++, p, uniqueColumnValue == null ? null : uniqueColumnValue.getClass(), uniqueColumnValue);
        p.executeUpdate();
      } finally {
        p.close();
      }
    } finally {
      release(connection);
    }
  }
  /**
   * iztriva zapisi sys zadadeno uslovie primerno training_course_id = ?, parameters = 5 
   * @param <T>
   * @param cls
   * @param condition
   * @param parameters
   * @throws SQLException
   */
  public <T> void deleteRecords(Class<T> cls, String condition, Object... parameters) throws SQLException{
    String className = cls.getName();
    String sql = " DELETE FROM " + dataConfigFactory.getTableName(className) + " WHERE " + condition;
    logger.debug("SQL:" + sql);
    logger.debug("Params:" + (parameters == null || parameters.length == 0 ? "" : Arrays.asList(parameters)));
    Connection connection = getConnection();
    try {
      PreparedStatement p = connection.prepareStatement(sql);
      try {
        if (condition != null && parameters.length > 0) {
          for (int i = 0; i < parameters.length; i++) {
            p.setObject(i+1, parameters[i]);    
          }
        }
        p.executeUpdate();
      } finally {
        p.close();
      }
    } finally {
      release(connection);
    }
  }
  
  private static void addDataToStatement(int id, PreparedStatement p, Class<?> type, Object object) throws SQLException {
      if (type != null && object != null && type.equals(java.util.Date.class)) {
          p.setTimestamp(id, new java.sql.Timestamp(((java.util.Date)object).getTime()));    
      } else if (object != null && object.getClass().equals(BinaryStream.class) && !((BinaryStream)object).isEmpty()) {
          BinaryStream bs = (BinaryStream) object;
          p.setBinaryStream(id, bs.getInputStream(), bs.getStreamSize());
      } else {
          p.setObject(id, object);
      }
      /*if (type.equals(java.sql.Date.class)) {
      p.setDate(id, (java.sql.Date) object); 
    } else if (type.equals(java.util.Date.class)) {
      
    } else if (type.equals(int.class) || type.equals(Integer.class)) {
      p.setInt(id, (Integer) object);
    } else if (type.equals(long.class) || type.equals(Long.class)) {
      p.setLong(id, (Long) object); 
    } else {
      p.setString(id, (String) object.toString()); 
    }*/
  }
  /**
   * 
   * @param <T>
   * @param cls
   * @param condition - ako ne trqbva da se podavat paremetri za condition se slaga null
   * @param parameters
   * @return - ako nqma zapisi za vry6tane, vry6ta prazen List
   * @throws SQLException
   */
  public <T> List<T> selectRecords(Class<T> cls, String condition, Object... parameters) throws SQLException{
      String className = cls.getName();
      String sql = " SELECT " + StringUtils.join(dataConfigFactory.getColumnNames(className), " , ") + " FROM " + dataConfigFactory.getTableName(className);
      if (condition != null) {
          sql += " WHERE " + condition;
      }
      return selectRecords(sql, cls, parameters);
  }
  
  public <T> List<T> selectRecords(String sql, Class<T> cls, Object... parameters) throws SQLException{
	  String className = cls.getName();
	  /*System.out.println(sql);
	  if (parameters != null && parameters.length > 0) {
		  System.out.println(Arrays.asList(parameters));  
	  }*/
	  
	  Connection connection = getConnection();
	  try {
		  PreparedStatement p = connection.prepareStatement(sql);
		  logger.debug("SQL:" + sql);
		  logger.debug("Params:" + (parameters == null || parameters.length == 0 ? "" : Arrays.asList(parameters)));
		  try {
			  //addDataToStatement(arg++, p, uniqueColumnValue.getClass(), uniqueColumnValue);
			  if (parameters != null && parameters.length > 0) {
				  for (int i = 0; i < parameters.length; i++) {
					  //p.setObject(i+1, parameters[i]);    
					  addDataToStatement(i+1, p, parameters[i] == null ? null : parameters[i].getClass(), parameters[i]);
				  }
			  }
			  ResultSet rs = p.executeQuery();
			  //System.out.println(rs);
			  try {
				  List<String> columnNames = dataConfigFactory.getColumnNames(className);
				  List<String> variableNames = dataConfigFactory.getVariableNames(className);
				  List<T> result = new ArrayList<T>();
				  
				  while (rs.next()) {
                      if (cls.equals(ResultRow.class)) {
                          ResultSetMetaData metadata = rs.getMetaData();
                          ResultRow row = new ResultRow();
                          for (int i = 1; i <= metadata.getColumnCount(); i++) {
                              String name = metadata.getColumnLabel(i);
                              Object object = rs.getObject(name);
                              row.addObject(name, object);
                          }
                          result.add((T) row);
                      } else {
                          int i = 0;
                          T obj = null;
                          try {
                              try {
                                  obj = cls.newInstance();
                              } catch (InstantiationException e) {
                                  throw new RuntimeException("Problem invoking no-arg new instance method for class " + cls, e);
                              } catch (IllegalAccessException e) {
                                  throw new RuntimeException("Problem invoking no-arg new instance method for class " + cls, e);
                              }
                              for (i = 0; i < dataConfigFactory.getColumnNames(className).size(); i++) {
                                  Class<?> returnType = MethodsUtils.getGetterMethod(obj, variableNames.get(i)).getReturnType();
                                  //System.out.println(MethodsUtils.getSetterMethod(obj, variableNames.get(i)));
                                  //System.out.println(">" + convertResultSetObject(rs, returnType, columnNames.get(i)) + "<");
                                  MethodsUtils.getSetterMethod(obj, variableNames.get(i)).invoke(obj, convertResultSetObject(rs, returnType, columnNames.get(i)));
                              }
                              //System.out.println(obj);
                              result.add(obj);
                          } catch (SQLException e) {
                              throw e;
                          } catch (SecurityException e) {
                              throw new RuntimeException("Problem invoking getter/setter method for variable " + variableNames.get(i) + " on class " + cls, e);
                          } catch (NoSuchMethodException e) {
                              throw new RuntimeException("Problem invoking getter/setter method for variable " + variableNames.get(i) + " on class " + cls, e);
                          } catch (IllegalArgumentException e) {
                              throw new RuntimeException("Illegal Arguments for setter method for variable " + variableNames.get(i) + " of class " + cls, e);
                          } catch (IllegalAccessException e) {
                              throw new RuntimeException("Problem invoking getter/setter method for variable " + variableNames.get(i) + " on class " + cls, e);
                          } catch (InvocationTargetException e) {
                              throw new RuntimeException("Problem invoking getter/setter method for variable " + variableNames.get(i) + " on class " + cls, e);
                          }
                      }
                  }
				  return result;
			  } finally {
				  rs.close();
			  }
		  } catch (IllegalArgumentException e) {
			  throw (new SQLException("IllegalArgumentException while trying to execute selectRecords query.\n RecordType:" + cls.getName() + "\nSQL:" + p, e));
		  } catch (SQLException e) {
			  throw (new SQLException("SQLException while trying to execute selectRecords query.\n RecordType:" + cls.getName() + "\nSQL:" + p, e));
		  } finally {
			  p.close();
		  }
	  } finally {
		  release(connection);
	  }
  }
  
  public int execute(String sql, Object... parameters) throws SQLException {
      Connection connection = getConnection();
      try {
          PreparedStatement p = connection.prepareStatement(sql);
          try {
              logger.debug("SQL:" + sql);
              if (parameters != null && parameters.length > 0) {
                  for (int i = 0; i < parameters.length; i++) {
                      if (parameters[i] != null && parameters[i] instanceof java.sql.Timestamp) {
                          p.setTimestamp(i+1, (java.sql.Timestamp)parameters[i]);
                      } else if (parameters[i] != null && parameters[i] instanceof java.util.Date) {
                          p.setObject(i+1, new java.sql.Date(((java.util.Date)parameters[i]).getTime()) );
                      } else  {
                          p.setObject(i+1, parameters[i]);    
                      }
                          
                  }
              }
              logger.debug("Parameters:"+ Arrays.asList(parameters));
              boolean isQuery = p.execute();
              if (isQuery) {
                  return 1;
              } else {
                  return p.getUpdateCount();
              }    
          } finally {
              p.close();
          }
          
      } finally {
          release(connection);
      }
      
      

  }
  public long getSequenceNextValue(String sequenceName) throws SQLException {
      List<LongValue> lv = selectRecords("select nextval(?) as value", LongValue.class, sequenceName);
      return lv.get(0).getValue();
      
  }
  
  /**
   * ideqta e da convertira primiteve dataTypes pravilno!
   * zashtoto getInt(null) vry6ta 0, dokato getObject(null) vry6ta null
   * @param rs
   * @param type
   * @param columnName
   * @return
   * @throws SQLException
   */
  private Object convertResultSetObject(ResultSet rs, Class<?> type, String columnName) throws SQLException {
    Object result;
    if (type.equals(int.class) || (type.equals(Integer.class))) {
      result = rs.getInt(columnName);
    } else if (type.equals(long.class) || type.equals(Long.class)) {
      result = rs.getLong(columnName);
    } else if (type.equals(short.class) || type.equals(Short.class)) {
      result = rs.getShort(columnName);
    } else if (type.equals(byte.class) || type.equals(Byte.class)) {
      result = rs.getByte(columnName);
    } else if (type.equals(double.class) || type.equals(Double.class)) {
      result = rs.getDouble(columnName);
    } else if (type.equals(float.class) || type.equals(Float.class)) {
      result = rs.getFloat(columnName);
    } else if (type.equals(boolean.class)) {
      result = rs.getBoolean(columnName);
    } else if (type.equals(BinaryStream.class)){
        BinaryStream content = new BinaryStream(rs.getBinaryStream(columnName), 0);
        return content;
    } else if (type.equals(List.class)){
        List<Object> lst = new ArrayList<Object>();
        for (Object o:(Object[])rs.getArray(columnName).getArray()) {
            lst.add(o);
        }
        return lst.size() == 0 ? null : lst;
    } else {
        return rs.getObject(columnName);
    }

    if (Number.class.isAssignableFrom(type) && rs.wasNull()) {
        return null;
    } else {
        return result;
    }




    //return null;
  }

}
