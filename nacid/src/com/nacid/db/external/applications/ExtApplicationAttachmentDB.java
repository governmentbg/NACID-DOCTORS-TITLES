package com.nacid.db.external.applications;

import com.nacid.bl.impl.Utils;
import com.nacid.data.BlobRecord;
import com.nacid.data.common.BinaryStream;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.external.applications.ExtApplicationAttachmentRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ExtApplicationAttachmentDB extends DatabaseService {
	protected String attachmentsTable = "eservices.rudi_attached_docs";
	protected String internalAttachmentsTable = "attached_docs";

	private static final String SQL_DELETE_BY_ID  = "delete from {0} where id=?";
    private static final String SQL_SELECT_BY_APPLICATION = "SELECT ad.id, application_id, doc_descr, doc_type_id, content_type, filename as file_name, copy_type_id \n" +
                                                            "FROM {0} ad\n" +
                                                            "join blobs.blobs b on b.id = ad.document_id\n" +
                                                            "where application_id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT ad.id, application_id, doc_descr, doc_type_id, content_type, {CONTENT} as doc_content, filename as file_name, copy_type_id \n" +
                                                    "FROM {0} ad\n" +
                                                    "join blobs.blobs b on b.id = ad.document_id\n" +
                                                    "where ad.id=?";

    private static final String SQL_SELECT_DOCUMENT_ID_BY_ATTACHMENT_ID = "select document_id as value from {0} where id = ?";
    private static final String SQL_UPDATE_BY_ID = "UPDATE {0} set application_id=?, doc_descr=?, doc_type_id=?, copy_type_id=?, document_id = ? WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO {0} (application_id, doc_descr, doc_type_id, copy_type_id, document_id) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_COPY_BLOB_TO_INT_DB = "insert into blobs.blobs (id, filename, content_type, filesize, content, created_date, rudi_user_created) \n" +
                                                            "(SELECT ?, filename, content_type, filesize, content, now(), rudi_user_created FROM blobs.blobs where id = (select document_id from {0} where id = ?))";
    private static final String SQL_COPY_ATTACHMENTS_TO_INT_DB = "INSERT INTO {1} (parent_id, doc_descr, doc_type_id, copy_type_id, document_id) (select ?, doc_descr, doc_type_id, copy_type_id, ? from {0} where id = ?)";




	public ExtApplicationAttachmentDB(DataSource ds) {
		super(ds);
	}

	public ExtApplicationAttachmentDB(DataSource ds, String attachmentsTable, String internalAttachmentsTable) {
        super(ds);
        this.attachmentsTable = attachmentsTable;
        this.internalAttachmentsTable = internalAttachmentsTable;
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
	}

	@Override
	public <T> List<T> selectRecords(Class<T> cls, String condition, Object... parameters) throws SQLException {
		throw new UnsupportedOperationException("This method is not supported.");
	}

	public void copyRecordsToInternalDB(int extApplicationId, int intApplId) throws SQLException {
        List<ExtApplicationAttachmentRecord> extAttachedDocs = loadRecordsForApplication(extApplicationId, 0);
        for (ExtApplicationAttachmentRecord extAttachedDoc : extAttachedDocs) {
            long blobsSeq = getSequenceNextValue("blobs.blobs_id_seq");
            super.execute(MessageFormat.format(SQL_COPY_BLOB_TO_INT_DB, attachmentsTable), blobsSeq, extAttachedDoc.getId());
            super.execute(MessageFormat.format(SQL_COPY_ATTACHMENTS_TO_INT_DB, attachmentsTable, internalAttachmentsTable), intApplId, blobsSeq, extAttachedDoc.getId());
        }
	}



	public void deleteRecord(int id) throws SQLException {
		Connection connection = getConnection();
		try {
            int documentId = getDocumentIdByAttachmentId(id);
            super.execute(MessageFormat.format(SQL_DELETE_BY_ID, attachmentsTable), id);
            super.deleteRecord(BlobRecord.class, documentId);
		} finally {
			release(connection);
		}
	}

	public ExtApplicationAttachmentRecord loadRecord(int id, boolean loadContent) throws SQLException {
		Connection connection = getConnection();
		try {
			String sql =  SQL_SELECT_BY_ID.replace("{CONTENT}", loadContent ? "content" : "null");
            sql = MessageFormat.format(sql, attachmentsTable);
            PreparedStatement p = connection.prepareStatement(sql);
			try {

				p.setInt(1, id);

				ResultSet rs = p.executeQuery();
				try {
					if(rs.next()) {
						ExtApplicationAttachmentRecord result = new ExtApplicationAttachmentRecord(
								rs.getInt("id"),
								rs.getInt("application_id"),
								rs.getString("doc_descr"),
								rs.getInt("doc_type_id"),
								rs.getString("content_type"),
								rs.getString("file_name"),
                                rs.getBinaryStream("doc_content"),
								rs.getInt("copy_type_id"));
						return result;
					}
					else {
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

	public List<ExtApplicationAttachmentRecord> loadRecordsForApplication(int applicationId, int docTypeId) throws SQLException {
		Connection connection = getConnection();
		try {
		    String sqlAddition = "";
		    if(docTypeId > 0) {
		        sqlAddition = "and doc_type_id=?";
		    }
			PreparedStatement p = connection.prepareStatement(MessageFormat.format(SQL_SELECT_BY_APPLICATION, attachmentsTable) + sqlAddition);
			try {

				p.setInt(1, applicationId);

				if(docTypeId > 0) {
				    p.setInt(2, docTypeId);
				}

				ResultSet rs = p.executeQuery();
				try {
					ArrayList<ExtApplicationAttachmentRecord> ret = new ArrayList<ExtApplicationAttachmentRecord>();
					while(rs.next()) {
						ret.add(new ExtApplicationAttachmentRecord(
								rs.getInt("id"),
								rs.getInt("application_id"),
								rs.getString("doc_descr"),
								rs.getInt("doc_type_id"),
								rs.getString("content_type"),
								rs.getString("file_name"),
								null,
								rs.getInt("copy_type_id")));
					}
					return ret;
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

	public ExtApplicationAttachmentRecord saveRecord(ExtApplicationAttachmentRecord rec, int fileSize, int userCreated) throws SQLException {
	    ExtApplicationAttachmentRecord ret = rec;
		if(rec.getId() == 0) {
			ret = insertRecord(rec, fileSize, userCreated);
		}
		else {
			updateRecord(rec, fileSize);
		}
		return ret;
	}

	private ExtApplicationAttachmentRecord insertRecord(ExtApplicationAttachmentRecord rec, int fileSize, int userCreated) throws SQLException {
		Connection connection = getConnection();
		try {
			PreparedStatement p = connection.prepareStatement(MessageFormat.format(SQL_INSERT, attachmentsTable), Statement.RETURN_GENERATED_KEYS);
			try {
				BlobRecord blob = new BlobRecord(0, rec.getFileName(), rec.getContentType(), fileSize, new BinaryStream(rec.getContentStream(), fileSize), new Timestamp(new java.util.Date().getTime()), userCreated, null);
                blob = super.insertRecord(blob);


				int cnt = 1;

				p.setInt(cnt++, rec.getApplicationId());
				p.setString(cnt++, rec.getDocDescr());
				p.setInt(cnt++, rec.getDocTypeId());
				p.setInt(cnt++, rec.getCopyTypeId());
                p.setInt(cnt++, blob.getId());

				p.executeUpdate();

				ResultSet rs = p.getGeneratedKeys();
				try {
					if(rs.next()) {
						rec.setId(rs.getInt("id"));
					}
					return rec;
				} finally {
					rs.close();
				}
			} finally {
				p.close();
			}
		} finally {
			try {
				rec.getContentStream().close();
			} catch (IOException e) {
			    throw Utils.logException(e);
			}
			release(connection);
		}
	}
	private int getDocumentIdByAttachmentId(int attachmentId) throws SQLException {
        return super.selectRecords(MessageFormat.format(SQL_SELECT_DOCUMENT_ID_BY_ATTACHMENT_ID, attachmentsTable), IntegerValue.class, attachmentId).get(0).getValue();

    }
	private void updateRecord(ExtApplicationAttachmentRecord rec, int fileSize) throws SQLException {
		Connection connection = getConnection();
		boolean hasFile = fileSize > 0;
		try {
            Integer documentId = getDocumentIdByAttachmentId(rec.getId());
            if (hasFile) {
                BlobRecord blob = super.selectRecord(new BlobRecord(), documentId);
                blob.setFilesize(fileSize);
                blob.setContent(new BinaryStream(rec.getContentStream(), fileSize));
                blob.setFilename(rec.getFileName());
                super.updateRecord(blob);
            }

            PreparedStatement p = connection.prepareStatement(MessageFormat.format(SQL_UPDATE_BY_ID, attachmentsTable));
			try {

				int cnt = 1;
				p.setInt(cnt++, rec.getApplicationId());
				p.setString(cnt++, rec.getDocDescr());
				p.setInt(cnt++, rec.getDocTypeId());
				p.setInt(cnt++, rec.getCopyTypeId());
				p.setInt(cnt++, documentId);

				p.setInt(cnt++, rec.getId());

				p.executeUpdate();

			} finally {
				p.close();
			}
		} finally {
			try {
				if(rec.getContentStream() != null) {
					rec.getContentStream().close();
				}
			} catch (IOException e) {
			    throw Utils.logException(e);
			}
			release(connection);
		}
	}

    public static void main(String[] args) throws SQLException {
        DataSource ds = new StandAloneDataSource();
        ExtApplicationAttachmentDB db = new ExtApplicationAttachmentDB(ds);
        db.copyRecordsToInternalDB(17, 3982);
    }
}
