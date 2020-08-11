package com.nacid.db.utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.nacid.bl.impl.Utils;

import java.io.PrintWriter;
import java.sql.*;

/**
 * Database utility class. Used for getting connection to database and freeing database resources.
 * User: Administrator
 */
public class DatabaseUtils {

  public static int openConnectionsCounter = 0;

  static {
    try {
      Class.forName("org.postgresql.Driver");
      //DriverManager.registerDriver(new org.postgresql.Driver());
    } catch (Exception e) {
      //logger.error("Failed to register jdbc driver: ", e);
    }
  }


  /**
   * Gets datasource.
   */
  public static DataSource getDataSource() {
    try {
      Context initialContext = new InitialContext();
      /*DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/NacidDataSource");*/
      Context envContext = (Context) initialContext.lookup("java:comp/env");
      DataSource dataSource = (DataSource) envContext.lookup("jdbc/NacidDataSource");
      //dataSource.setLogWriter(new PrintWriter(System.out));
      return dataSource;
    } catch (Exception e) {
      throw Utils.logException(e);
    }
  }

  /**
   * Gets datasource.
   */
  public static DataSource getDataSource(String jndiName) {
    try {
      Context initialContext = new InitialContext();
      DataSource dataSource = (DataSource) initialContext.lookup(jndiName);
      return dataSource;
    } catch (Exception e) {
      //logger.error("Could not lookup dataSource:", e);
    }
    return null;
  }

  /**
   * Gets connection to the database.
   *
   * @return Connection if everything is in order;
   *         null otherwise (for example if the databse is not available).
   */
  public static Connection getConnection() {
    try {
      DataSource dataSource = getDataSource();
      Connection connection = dataSource.getConnection();
      return connection;
    } catch (Exception e) {
      //logger.error("Could not get connetion form pool", e);
    }
    return null;
  }
}