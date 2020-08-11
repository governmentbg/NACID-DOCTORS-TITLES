package com.nacid.db.utils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class StandAloneDataSource implements DataSource {
  private String connectionUrl = "jdbc:postgresql://192.168.0.37:5433/NACID"/*"jdbc:postgresql://localhost:5432/NACID/"*/;
  private String user = "postgres";
  private String pass = "p0stGr#s";
  private Connection connection;

  public StandAloneDataSource() {
  }

  public StandAloneDataSource(String connectionUrl, String user, String pass) {
    this.connectionUrl = connectionUrl;
    this.user = user;
    this.pass = pass;
  }

  public Connection getConnection() throws SQLException {
    if (connection == null) {
      try {
        Class.forName("org.postgresql.Driver").newInstance();
      } catch (Exception e) {
        throw new SQLException(e.toString());
      }

      Connection conn = DriverManager.getConnection(connectionUrl, user, pass);
      connection = new StandaloneConnectionWrapper(conn);
    }

    return connection;
  }

  public Connection getConnection(String username, String password) throws SQLException {
    return getConnection();
  }

  public PrintWriter getLogWriter() throws SQLException {
    return null;
  }

  public int getLoginTimeout() throws SQLException {
    return 0;
  }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

  }

  public void setLoginTimeout(int seconds) throws SQLException {

  }

  public boolean isWrapperFor(Class<?> arg0) throws SQLException {
    return false;
  }

  public <T> T unwrap(Class<T> arg0) throws SQLException {
    return null;
  }

  public void close() {
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception $_) {
      }
      connection = null;
    }
  }
}
