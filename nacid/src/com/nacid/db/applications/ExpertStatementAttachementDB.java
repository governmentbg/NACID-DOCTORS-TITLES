package com.nacid.db.applications;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.ExpertStatementAttachmentRecord;
import com.nacid.db.utils.DatabaseService;

public class ExpertStatementAttachementDB extends DatabaseService {
	
	public final static String SELECT_STATEMENT = 
	    "select id,doc_descr,doc_type_id,content_type,file_name,application_id,expert_id from expert_statement where id=?";
    
	public final static String SELECT_STATEMENT_AND_CONTENT = 
        "select id,doc_descr,doc_type_id,doc_content,content_type,file_name,application_id,expert_id from expert_statement where id=?";
    
	
	public final static String SELECT_STATEMENTS_BY_APPLICATION = 
        "select id,doc_descr,doc_type_id,content_type,file_name,application_id,expert_id from expert_statement where application_id=?";
    
	public final static String SELECT_STATEMENTS_BY_APPLICATION_AND_EXPERT = 
	    "select id,doc_descr,doc_type_id,content_type,file_name,application_id,expert_id from expert_statement where application_id=? and expert_id=?";

    private static final String DELETE_STATEMENT = "delete from expert_statement where id=?";

    private static final String SQL_UPDATE = 
        "update expert_statement set expert_id=?, doc_descr=?, doc_type_id=?, doc_content=?, file_name=?, content_type=?, application_id=? where id=?";

    private static final String SQL_UPDATE_NO_CONTENT = 
        "update expert_statement set expert_id=?, doc_descr=?, doc_type_id=?, application_id=? where id=?";

    
    private static final String SQL_INSERT = 
        "insert into expert_statement (expert_id,doc_descr,doc_type_id,doc_content,file_name,content_type,application_id) values (?,?,?,?,?,?,?)";
	
	public ExpertStatementAttachementDB(DataSource ds) {
		super(ds);
	}

	@Override
	public <T> void deleteRecord(Class<T> cls, Object uniqueColumnValue) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported.");
	}
	
	@Override
	public <T> T insertRecord(T o) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported.");
	};
	
	@Override
	public void updateRecord(Object o) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported.");
	}
	
	@Override
	public <T> T selectRecord(T object, Object uniqueColumnValue) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported.");
	};
	
	@Override
	public <T> List<T> selectRecords(Class<T> cls, String condition, Object... parameters) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported.");
	}
	
    public ExpertStatementAttachmentRecord loadExpertStatement(int id, boolean loadContent) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(loadContent ? SELECT_STATEMENT_AND_CONTENT : SELECT_STATEMENT);
            try {
                p.setInt(1, id);
                ResultSet rs = p.executeQuery();
                try {
                    if (rs.next()) {

                        ExpertStatementAttachmentRecord result = new ExpertStatementAttachmentRecord(
                                rs.getInt("id"), 
                                rs.getInt("application_id"), 
                                rs.getInt("expert_id"), 
                                rs.getString("doc_descr"), 
                                rs.getInt("doc_type_id"),
                                loadContent ? rs.getBinaryStream("doc_content") : null,
                                rs.getString("file_name"), 
                                rs.getString("content_type"));
                        return result;

                    } else {
                        return null;
                    }
                } finally {
                    rs.close();
                }

            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public List<ExpertStatementAttachmentRecord> loadExpertStatementByApplication(int applicationId) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(SELECT_STATEMENTS_BY_APPLICATION);
            try {
                p.setInt(1, applicationId);
                ResultSet rs = p.executeQuery();
                try {
                    List<ExpertStatementAttachmentRecord> result = new ArrayList<ExpertStatementAttachmentRecord>();
                    while (rs.next()) {

                        ExpertStatementAttachmentRecord item = new ExpertStatementAttachmentRecord(
                                rs.getInt("id"), 
                                rs.getInt("application_id"), 
                                rs.getInt("expert_id"), 
                                rs.getString("doc_descr"), 
                                rs.getInt("doc_type_id"),
                                null,
                                rs.getString("file_name"), 
                                rs.getString("content_type"));
                        
                        result.add(item);
                    }
                    return result;
                } finally {
                    rs.close();
                }

            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public List<ExpertStatementAttachmentRecord> loadExpertStatementByApplicationAndExpert(int applicationId, int expertId) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(SELECT_STATEMENTS_BY_APPLICATION_AND_EXPERT);
            try {
                p.setInt(1, applicationId);
                p.setInt(2, expertId);
                ResultSet rs = p.executeQuery();
                try {
                    List<ExpertStatementAttachmentRecord> result = new ArrayList<ExpertStatementAttachmentRecord>();
                    while (rs.next()) {

                        ExpertStatementAttachmentRecord item = new ExpertStatementAttachmentRecord(
                                rs.getInt("id"), 
                                rs.getInt("application_id"), 
                                rs.getInt("expert_id"), 
                                rs.getString("doc_descr"), 
                                rs.getInt("doc_type_id"),
                                null,
                                rs.getString("file_name"), 
                                rs.getString("content_type"));
                        
                        result.add(item);
                    }
                    return result;
                } finally {
                    rs.close();
                }

            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    public int saveExpertStatementAttachment(int id, int expertId, String docDescr, int docTypeId, InputStream is, String fileName,
            String contentType, int fileSize, int applicationId) throws SQLException {
        
        boolean isUpdate = id > 0;
        boolean hasContent = !isUpdate || fileSize > 0;
        
        Connection connection = getConnection();
        try {
            PreparedStatement p = null;
            if(isUpdate) {
                if(hasContent) {
                    p = connection.prepareStatement(SQL_UPDATE);
                }
                else {
                    p = connection.prepareStatement(SQL_UPDATE_NO_CONTENT);
                }
            }
            else {
                p = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            }
            try {
                
                int cnt = 1;
                
                p.setInt(cnt++, expertId);
                p.setString(cnt++, docDescr);
                p.setInt(cnt++, docTypeId);
                if (hasContent) {
                    p.setBinaryStream(cnt++, is, fileSize);
                    p.setString(cnt++, fileName);
                    p.setString(cnt++, contentType);
                }
                p.setInt(cnt++, applicationId);
                if(isUpdate) {
                    p.setInt(cnt++, id);
                }
                
                p.executeUpdate();
                
                if (!isUpdate) {
                    ResultSet rs = p.getGeneratedKeys();
                    try {
                        if (rs.next()) {
                            id = rs.getInt("id");
                        }
                    } finally {
                        rs.close();
                    }
                }
            } finally {
                p.close();
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw Utils.logException(e);
                }
            }
            release(connection);
        }
        
        return id;
    }

    public void deleteExpertStatementAttachment(int attId) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(DELETE_STATEMENT);
            try {
                p.setInt(1, attId);
                p.executeUpdate();
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }
    
	
}
