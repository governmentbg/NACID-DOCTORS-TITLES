package com.nacid.db.utils;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class StandaloneConnectionWrapper implements Connection {
  
  private Connection connection;
  private boolean autocommit = true;
  
  public StandaloneConnectionWrapper(Connection connection) {
    super();
    this.connection = connection;
  }

  public void clearWarnings() throws SQLException {
    connection.clearWarnings();
  }

  public void close() throws SQLException {
//    if (!autocommit) {
//      connection.setAutoCommit(true);
//    }
  }

  public void commit() throws SQLException {
    connection.commit();
  }

  public Statement createStatement() throws SQLException {
    return connection.createStatement();
  }

  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    return connection.createStatement(resultSetType, resultSetConcurrency);
  }

  public boolean getAutoCommit() throws SQLException {
    return connection.getAutoCommit();
  }

  public String getCatalog() throws SQLException {
    return connection.getCatalog();
  }

  public int getHoldability() throws SQLException {
    return connection.getHoldability();
  }

  public DatabaseMetaData getMetaData() throws SQLException {
    return connection.getMetaData();
  }

  public int getTransactionIsolation() throws SQLException {
    return connection.getTransactionIsolation();
  }

  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return connection.getTypeMap();
  }

  public SQLWarning getWarnings() throws SQLException {
    return connection.getWarnings();
  }

  public boolean isClosed() throws SQLException {
    return connection.isClosed();
  }

  public boolean isReadOnly() throws SQLException {
    return connection.isReadOnly();
  }

  public String nativeSQL(String sql) throws SQLException {
    return connection.nativeSQL(sql);
  }

  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
  }

  public CallableStatement prepareCall(String sql) throws SQLException {
    return connection.prepareCall(sql);
  }

  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
  }

  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return connection.prepareStatement(sql, autoGeneratedKeys);
  }

  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    return connection.prepareStatement(sql, columnIndexes);
  }

  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    return connection.prepareStatement(sql, columnNames);
  }

  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return connection.prepareStatement(sql);
  }

  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    connection.releaseSavepoint(savepoint);
  }

  public void rollback() throws SQLException {
    connection.rollback();
  }

  public void rollback(Savepoint savepoint) throws SQLException {
    connection.rollback(savepoint);
  }

  public void setAutoCommit(boolean autoCommit) throws SQLException {
    this.autocommit = autoCommit; 
    connection.setAutoCommit(autoCommit);
  }

  public void setCatalog(String catalog) throws SQLException {
    connection.setCatalog(catalog);
  }

  public void setHoldability(int holdability) throws SQLException {
    connection.setHoldability(holdability);
  }

  public void setReadOnly(boolean readOnly) throws SQLException {
    connection.setReadOnly(readOnly);
  }

  public Savepoint setSavepoint() throws SQLException {
    return connection.setSavepoint();
  }

  public Savepoint setSavepoint(String name) throws SQLException {
    return connection.setSavepoint(name);
  }

  public void setTransactionIsolation(int level) throws SQLException {
    connection.setTransactionIsolation(level);
  }

  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    connection.setTypeMap(map);
  }

@Override
public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
	return null;
}

@Override
public Blob createBlob() throws SQLException {
	return null;
}

@Override
public Clob createClob() throws SQLException {
	return null;
}

@Override
public NClob createNClob() throws SQLException {
	return null;
}

@Override
public SQLXML createSQLXML() throws SQLException {
	return null;
}

@Override
public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
	return null;
}

    @Override
    public void setSchema(String schema) throws SQLException {

    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    @Override
public Properties getClientInfo() throws SQLException {
	return null;
}

@Override
public String getClientInfo(String arg0) throws SQLException {
	return null;
}

@Override
public boolean isValid(int arg0) throws SQLException {
	return false;
}

@Override
public void setClientInfo(Properties arg0) throws SQLClientInfoException {
	
}

@Override
public void setClientInfo(String arg0, String arg1)
		throws SQLClientInfoException {
	
}

@Override
public boolean isWrapperFor(Class<?> arg0) throws SQLException {
	return false;
}

@Override
public <T> T unwrap(Class<T> arg0) throws SQLException {
	return null;
}
}
