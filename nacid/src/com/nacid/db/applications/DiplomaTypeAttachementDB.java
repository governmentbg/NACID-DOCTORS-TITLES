package com.nacid.db.applications;

import com.nacid.bl.applications.Attachment;
import com.nacid.bl.impl.Utils;
import com.nacid.data.BlobRecord;
import com.nacid.data.applications.DiplomaTypeAttachmentRecord;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.common.StringValue;
import com.nacid.db.utils.DatabaseService;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.nacid.db.applications.AttachmentDB.getBlobContent;
import static com.nacid.db.applications.AttachmentDB.getRelativeContentLocation;

public class DiplomaTypeAttachementDB extends DatabaseService {


    private static final String SQL_DELETE_BY_ID = "delete from diploma_type_attached_docs where id=?";
	private static final String SQL_SELECT_BY_DIPL_TYPE = "select ad.id, diploma_type_id, doc_descr, doc_type_id, filename as file_name, content_type \n" +
                                                            "from diploma_type_attached_docs ad\n" +
                                                            "left join blobs.blobs b on b.id = ad.document_id\n" +
                                                            " where diploma_type_id = ?";
	private static final String SQL_SELECT_BY_ID = "select ad.id, diploma_type_id, doc_descr, doc_type_id, filename as file_name, content_type, b.id as document_id, b.file_location as document_file_location, {CONTENT} as doc_content \n" +
                                                                "from diploma_type_attached_docs ad\n" +
                                                                "left join blobs.blobs b on b.id = ad.document_id where ad.id=?";
    private static final String SQL_SELECT_DOCUMENT_ID_BY_ATTACHMENT_ID = "select document_id as value from diploma_type_attached_docs where id = ?";
	private static final String SQL_INSERT = "insert into diploma_type_attached_docs (diploma_type_id, doc_descr, doc_type_id, document_id) values (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "update diploma_type_attached_docs set diploma_type_id = ?, doc_descr = ?, doc_type_id = ?, document_id = ? where id = ?";
	

	public DiplomaTypeAttachementDB(DataSource ds) {
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
	
	public void deleteRecord(int id) throws SQLException {
        Integer documentId = selectDocumentIdByAttachmentId(id);
        super.execute(SQL_DELETE_BY_ID, id);
		AttachmentDB.deleteBlobContent(this, documentId, true);
	}

	public DiplomaTypeAttachmentRecord loadRecord(int id, boolean loadContent) throws SQLException {
		Connection connection = getConnection();
		try {
            String sql = SQL_SELECT_BY_ID;
            sql = sql.replace("{CONTENT}", loadContent ? "content" : "null");
			PreparedStatement p = connection.prepareStatement(sql);
			try {
				
				p.setInt(1, id);
				
				ResultSet rs = p.executeQuery();
				try {
					if(rs.next()) {
						InputStream is = rs.getBinaryStream("doc_content");
						String fileName = rs.getString("file_name");
						if (loadContent) {//the content should be read from the filesystem!
							if (is == null) {
								String fileLocation = rs.getString("document_file_location");
								if (!StringUtils.isEmpty(fileLocation)) {
									is = getBlobContent(fileLocation);
								}
							}

						}

						DiplomaTypeAttachmentRecord result = new DiplomaTypeAttachmentRecord(
							rs.getInt("id"),
							rs.getInt("diploma_type_id"),
							rs.getString("doc_descr"),
							rs.getInt("doc_type_id"),
							is,
							fileName,
							rs.getString("content_type"));
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
	
	public List<DiplomaTypeAttachmentRecord> loadRecordsForDiplomaType(int diplomaTypeId) throws SQLException {
		Connection connection = getConnection();
		try {
			PreparedStatement p = connection.prepareStatement(SQL_SELECT_BY_DIPL_TYPE);
			try {
				
				p.setInt(1, diplomaTypeId);
				
				ResultSet rs = p.executeQuery();
				try {
					ArrayList<DiplomaTypeAttachmentRecord> ret = new ArrayList<DiplomaTypeAttachmentRecord>();
					while(rs.next()) {
						ret.add(new DiplomaTypeAttachmentRecord(
								rs.getInt("id"),
								rs.getInt("diploma_type_id"),
								rs.getString("doc_descr"),
								rs.getInt("doc_type_id"),
								null,
								rs.getString("file_name"),
								rs.getString("content_type")));
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
	
	public DiplomaTypeAttachmentRecord saveRecord(DiplomaTypeAttachmentRecord rec, int fileSize, int userCreated) throws SQLException {
		DiplomaTypeAttachmentRecord ret = rec;
		if(rec.getId() == 0) {
			ret = insertRecord(rec, fileSize, userCreated);
		}
		else {
			updateRecord(rec, fileSize);
		}
		return ret;
	}
	
	private DiplomaTypeAttachmentRecord insertRecord(DiplomaTypeAttachmentRecord rec, int fileSize, int userCreated) throws SQLException {
		Connection connection = getConnection();
		try {

			BlobRecord blob = new BlobRecord(getBlobSequenceNextValue(), rec.getFileName(), rec.getContentType(), fileSize, null, new Timestamp(new java.util.Date().getTime()), userCreated, null);
			AttachmentDB.writeContentToFileSystemAndSetFileLocation(blob, rec.getContentStream());
            blob = super.insertRecord(blob, false);

            PreparedStatement p = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			try {
				int cnt = 1;
				p.setInt(cnt++, rec.getDiplomaTypeId());
				p.setString(cnt++, rec.getDocDescr());
				p.setInt(cnt++, rec.getDocTypeId());
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
	private int selectDocumentIdByAttachmentId(int id) throws SQLException {
        return super.selectRecords(SQL_SELECT_DOCUMENT_ID_BY_ATTACHMENT_ID, IntegerValue.class, id).get(0).getValue();
    }
	private void updateRecord(DiplomaTypeAttachmentRecord rec, int fileSize) throws SQLException {
		Connection connection = getConnection();
		boolean hasFile = fileSize > 0;
		try {
            Integer documentId = selectDocumentIdByAttachmentId(rec.getId());
            if (hasFile) {
                BlobRecord blob = super.selectRecord(new BlobRecord(), documentId);
                blob.setFilesize(fileSize);
                blob.setContent(null);
                blob.setFilename(rec.getFileName());
                AttachmentDB.deleteBlobContent(this, blob.getId(), false);
				AttachmentDB.writeContentToFileSystemAndSetFileLocation(blob, rec.getContentStream());
				super.updateRecord(blob);
            }

            PreparedStatement p = connection.prepareStatement(SQL_UPDATE);
			try {
				int cnt = 1;
				p.setInt(cnt++, rec.getDiplomaTypeId());
				p.setString(cnt++, rec.getDocDescr());
				p.setInt(cnt++, rec.getDocTypeId());
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
	private int getBlobSequenceNextValue() throws SQLException {
		return (int) getSequenceNextValue("blobs.blobs_id_seq");
	}
}
