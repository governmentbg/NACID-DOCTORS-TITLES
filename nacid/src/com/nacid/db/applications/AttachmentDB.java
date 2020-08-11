package com.nacid.db.applications;

import com.nacid.bl.impl.Utils;
import com.nacid.data.BlobRecord;
import com.nacid.data.applications.AppStatusDocTypeRecord;
import com.nacid.data.applications.AttachmentRecord;
import com.nacid.data.applications.CertificateNumberToAttachedDocRecord;
import com.nacid.data.common.StringValue;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Georgi Georgiev: 03.02.2015. Predi content-a na attachmentite stoeshe v osnovnata tablica  attached_docs/dipl_exam_attached_docs/uni_exam_attached_docs/ Sega ima promqna - vsichki attachments sa izneseni v nova tablica blobs.
 * Za da zapazq logikata nagore v prilojenieto promenih vsichki SQL-i da vry6tat AttachmentRecords po sy6tiq nachin po kojto sa izglejdali predi tazi promqna (vse edno attachment-ite sa vytre v attached_docs/.../ tablicite)
 */
public class AttachmentDB extends DatabaseService {
	public static String FILE_LOCATION_BASE;//TODO:Set this as commonvars parameter
    private static final String SQL_SELECT_UNIEXAM_BY_APPLICATION  = "select uni_exam_attached_docs.id from uni_exam_attached_docs " +  
            " left join university_validity on (uni_exam_attached_docs.parent_id = university_validity.id) " +
            " left join university_examination on (university_validity.id = university_examination.university_validity_id) " +
            " left join training_course on (training_course.id = university_examination.training_course_id) " + 
            " left join application on (application.training_course_id = training_course.id) " + 
            "where application.id = ? ";
    private static final String SQL_SELECT_DIPLEXAM_BY_APPLICATION  = "select dipl_exam_attached_docs.id from dipl_exam_attached_docs " +  
            " left join diploma_examination on (dipl_exam_attached_docs.parent_id = diploma_examination.id) " +
            " left join training_course on (training_course.id = diploma_examination.training_course_id) " +
            " left join application on (application.training_course_id = training_course.id) " + 
            " where application.id = ? ";
    
    private static final String SQL_SELECT_APPL_BY_APPLICATION  = "select id from attached_docs where parent_id=?";

    private static final String SQL_SELECT_FOR_PUBLIC_REGISTER =
        "select content_type scanned_content_type, content scanned_content " +
                " from attached_docs ad" +
                " join blobs.blobs b on ad.scanned_document_id = b.id " +
                " where parent_id=? and content_type is not null "
                + "and doc_type_id in ";
	
    private static final String SQL_SELECT_FOR_PUBLIC_REGISTER2 = 
        "select content_type as scanned_content_type, content as scanned_content " +
        " from attached_docs ad " +
        " join blobs.blobs b on ad.document_id = b.id where " +
        " parent_id = ? and content_type is not null " +
        " and doc_type_id in ";

    
    private String SQL_DELETE_BY_ID;
    private String SQL_SELECT_BY_PARENT;
    private String SQL_SELECT_BY_ID;
    private String SQL_INSERT_RECORD;
    private String SQL_SELECT_ATTACHMENTS_BY_APPL_ID;
	private String SQL_SELECT_ATTACHMENT_RECORD_DOC_IDS;
    private String SQL_UPDATE_RECORD;
    private String SQL_UPDATE_DOCUMENT_TYPE;

    public static enum ATTACHMENT_TYPE {
        APPLICATION("attached_docs"),
        DIPLOMA_EXAMINATION("dipl_exam_attached_docs"),
        UNIV_EXAMINATION("uni_exam_attached_docs");
        private String tableName;
        private ATTACHMENT_TYPE(String tableName) {
            this.tableName = tableName;
        }
        private String getTableName() {
            return tableName;
        }
    }
    
	public AttachmentDB(DataSource ds, ATTACHMENT_TYPE t) {
		super(ds);
		
		switch(t) {
            case APPLICATION:
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID = SQL_SELECT_APPL_BY_APPLICATION;
                break;
            case DIPLOMA_EXAMINATION:
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID = SQL_SELECT_DIPLEXAM_BY_APPLICATION;
                break;
            case UNIV_EXAMINATION:
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID = SQL_SELECT_UNIEXAM_BY_APPLICATION;
                break;
		}


        SQL_DELETE_BY_ID = "delete from "+t.getTableName()+" where id=?";
        SQL_SELECT_BY_PARENT = "select ad.id,parent_id,doc_descr,doc_type_id, copy_type_id,docflow_id,docflow_date,doc.content_type, doc.filename as file_name,sdoc.content_type as scanned_content_type,sdoc.filename as scanned_file_name\n" +
                "from " + t.getTableName() + " ad\n" +
                "join blobs.blobs doc on ad.document_id = doc.id\n" +
                "left join blobs.blobs sdoc on ad.scanned_document_id = sdoc.id\n" +
                "where parent_id=? ";
        SQL_SELECT_BY_ID= "select ad.id,parent_id,doc_descr,doc_type_id, copy_type_id,docflow_id,docflow_date,doc.content_type, doc.filename as file_name,sdoc.content_type as scanned_content_type,sdoc.filename as scanned_file_name, \n" +
				"doc.id as document_id, sdoc.id as scanned_document_id, \n" +
				"{DOCUMENT_CONTENT} as doc_content, {SCANNED_DOCUMENT_CONTENT} as scanned_content,\n" +
				"doc.file_location as document_file_location, sdoc.file_location as scanned_document_file_location\n" +
                "from " + t.getTableName() + " ad\n" +
                "join blobs.blobs doc on ad.document_id = doc.id\n" +
                "left join blobs.blobs sdoc on ad.scanned_document_id = sdoc.id\n" +
                "where ad.id=?";
        SQL_INSERT_RECORD = "INSERT INTO " + t.getTableName() + " (parent_id, doc_descr, doc_type_id, copy_type_id, docflow_id, docflow_date, document_id, scanned_document_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";


		SQL_SELECT_ATTACHMENT_RECORD_DOC_IDS = "select document_id, scanned_document_id from " + t.getTableName() + " WHERE id = ?";

        SQL_UPDATE_RECORD = "UPDATE " + t.getTableName() + " SET parent_id = ?, doc_descr = ?, doc_type_id = ?, copy_type_id = ?, docflow_id = ?, docflow_date = ?, document_id = ?, scanned_document_id = ? WHERE id = ?";
        SQL_UPDATE_DOCUMENT_TYPE = "UPDATE " + t.getTableName() + " SET doc_type_id = ? WHERE id = ?";

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
	
	public List<Integer> getAttachmentIdsByApplication(int applicationId) throws SQLException {
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(SQL_SELECT_ATTACHMENTS_BY_APPL_ID);
            try {
                p.setInt(1, applicationId);
                ResultSet rs = p.executeQuery();
                try {
                    ArrayList<Integer> ret = new ArrayList<Integer>();
                    while(rs.next()) {
                        ret.add(rs.getInt("id"));
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
	
	public void deleteRecord(int id) throws SQLException {
		Connection connection = getConnection();
		try {
            Integer documentId = null;
            Integer scannedDocumentId = null;
            PreparedStatement p = connection.prepareStatement(SQL_SELECT_ATTACHMENT_RECORD_DOC_IDS);
            try {
                p.setInt(1, id);

                ResultSet rs = p.executeQuery();
                try {
                    rs.next();
                    documentId = rs.getObject("document_id") == null ? null : rs.getInt("document_id");
                    scannedDocumentId = rs.getObject("scanned_document_id") == null ? null : rs.getInt("scanned_document_id");
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
            //pyrvo se iztriva attachment-a a posle blob-ovete!
            super.execute(SQL_DELETE_BY_ID, id);
            deleteBlobContent(this, documentId, true);
            if (scannedDocumentId != null) {
				deleteBlobContent(this, scannedDocumentId, true);
            }

		} finally {
			release(connection);
		}
	}
	public void deleteCertificateNumberToAttachedDocRecords(int attachmentId) throws SQLException{
		super.deleteRecords(CertificateNumberToAttachedDocRecord.class, " attached_doc_id = ? ", attachmentId);
	}
	public void invalidateCertificateNumber(int attachmentId, int invalidatedType) throws SQLException {
		String sql = "update cert_number_to_attached_doc set invalidated = ? where attached_doc_id = ?";
    	Connection con = getConnection();
    	try {
    		PreparedStatement ps = con.prepareStatement(sql);
    		try {
    			int arg = 1;
                ps.setInt(arg++, invalidatedType);
    			ps.setInt(arg++, attachmentId);
    			ps.executeUpdate();
    		} finally {
    			ps.close();
    		}
    	} finally {
    		con.close();
    	}
	}
	
	public AttachmentRecord loadRecord(int id, boolean loadContent, boolean loadScannedContent) throws SQLException {
		Connection connection = getConnection();
		try {
			String sql = SQL_SELECT_BY_ID;
			sql = sql.replace("{DOCUMENT_CONTENT}", loadContent ? "doc.content" : "null");
			sql = sql.replace("{SCANNED_DOCUMENT_CONTENT}", loadScannedContent ? "sdoc.content" : "null");
            PreparedStatement p = connection.prepareStatement(sql);
			try {
				p.setInt(1, id);

				ResultSet rs = p.executeQuery();
				try {
					if(rs.next()) {
						AttachmentRecord r = new AttachmentRecord();
						r.setId(rs.getInt("id"));
						r.setParentId(rs.getInt("parent_id")); 
						r.setDocDescr(rs.getString("doc_descr")); 
						r.setDocTypeId((Integer)rs.getObject("doc_type_id"));
						r.setContentType(rs.getString("content_type"));
						r.setFileName(rs.getString("file_name"));
						r.setCopyTypeId((Integer)rs.getObject("copy_type_id"));
						r.setDocflowId(rs.getString("docflow_id"));
						r.setDocflowDate(rs.getDate("docflow_date"));
						r.setScannedContentType(rs.getString("scanned_content_type"));
						r.setScannedFileName(rs.getString("scanned_file_name"));
						String docFileLocation = rs.getString("document_file_location");
						String scannedDocFileLocation = rs.getString("scanned_document_file_location");

						if (loadContent) {
							InputStream is = rs.getBinaryStream("doc_content");
							if (is == null && !StringUtils.isEmpty(docFileLocation)) {//searches for the inputstream in the filesystem if does not exist in the database and docFileLocation is not empty!
								is = getBlobContent(docFileLocation);
							}
							r.setContentStream(is);

						}
						if (loadScannedContent) {
							InputStream is = rs.getBinaryStream("scanned_content");
							if (is == null && !StringUtils.isEmpty(scannedDocFileLocation)) {
								is = getBlobContent(scannedDocFileLocation);
							}
							r.setScannedContentStream(is);
						}

                        return r;
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
	
	public List<AttachmentRecord> loadRecordsForParent(int parentId, List<Integer> docTypeIds) throws SQLException {
		Connection connection = getConnection();
		try {

			String sqlAddition = "";
		    if(!Utils.isEmpty(docTypeIds)) {
		    	sqlAddition = " and doc_type_id in (" + SQLUtils.parametersCountToParameterList(docTypeIds.size()) + ")" ;
		    }
			PreparedStatement p = connection.prepareStatement(SQL_SELECT_BY_PARENT + sqlAddition);
			try {

				int arg = 1;
				p.setInt(arg++, parentId);
				if (!Utils.isEmpty(docTypeIds)) {
					for (Integer docTypeId : docTypeIds) {
						p.setInt(arg++, docTypeId);
					}
				}
				ResultSet rs = p.executeQuery();
				try {
					ArrayList<AttachmentRecord> ret = new ArrayList<AttachmentRecord>();
					while(rs.next()) {
					    AttachmentRecord r = new AttachmentRecord();
                        r.setId(rs.getInt("id"));
                        r.setParentId(rs.getInt("parent_id")); 
                        r.setDocDescr(rs.getString("doc_descr")); 
                        r.setDocTypeId((Integer)rs.getObject("doc_type_id"));
                        r.setContentType(rs.getString("content_type"));
                        r.setFileName(rs.getString("file_name"));
                        r.setCopyTypeId((Integer)rs.getObject("copy_type_id"));
                        r.setDocflowId(rs.getString("docflow_id"));
                        r.setDocflowDate(rs.getDate("docflow_date"));
                        r.setScannedContentType(rs.getString("scanned_content_type"));
                        r.setScannedFileName(rs.getString("scanned_file_name"));
//                        r.setContentStream(rs.getBinaryStream("doc_content"));
//                        r.setScannedContentStream(rs.getBinaryStream("scanned_content"));

                        ret.add(r);
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
	
	public AttachmentRecord saveRecord(AttachmentRecord rec, int fileSize, int scannedFileSize, int userCreated) throws SQLException {
		AttachmentRecord ret = rec;
		if(rec.getId() == 0) {
			ret = insertRecord(rec, fileSize, scannedFileSize, userCreated);
		}
		else {
			updateRecord(rec, fileSize, scannedFileSize, userCreated);
		}
		return ret;
	}

    public void updateDocumentType(int attachmentId, int documentType) throws SQLException {
        super.execute(SQL_UPDATE_DOCUMENT_TYPE, documentType, attachmentId);
    }
	
	private AttachmentRecord insertRecord(AttachmentRecord rec, int fileSize, int scannedFileSize, int userCreated) throws SQLException {
		boolean hasSecondFile = scannedFileSize > 0;
	    Connection connection = getConnection();
		try {
            Timestamp dateCreated = new Timestamp(new java.util.Date().getTime());

            BlobRecord document = new BlobRecord(getBlobSequenceNextValue(), rec.getFileName(), rec.getContentType(), fileSize, null, dateCreated, userCreated, null);
			writeContentToFileSystemAndSetFileLocation(document, rec.getContentStream());
            document = super.insertRecord(document, false);


            BlobRecord scannedDocument = null;
            if (hasSecondFile) {
            	scannedDocument = new BlobRecord(getBlobSequenceNextValue(), rec.getFileName(), rec.getContentType(), scannedFileSize, null, dateCreated, userCreated, null);
				writeContentToFileSystemAndSetFileLocation(scannedDocument, rec.getScannedContentStream());
            	scannedDocument = super.insertRecord(scannedDocument, false);
            }


            PreparedStatement p = connection.prepareStatement(SQL_INSERT_RECORD, Statement.RETURN_GENERATED_KEYS);
			try {
				
				int cnt = 1;
				
				p.setInt(cnt++, rec.getParentId());
				p.setString(cnt++, rec.getDocDescr());
				p.setObject(cnt++, rec.getDocTypeId());
				p.setObject(cnt++, rec.getCopyTypeId());
                p.setString(cnt++, rec.getDocflowId());
                p.setDate(cnt++, rec.getDocflowDate());
                p.setInt(cnt++, document.getId());
                if (hasSecondFile) {
                    p.setInt(cnt++, scannedDocument.getId());
                } else {
                    p.setNull(cnt++, java.sql.Types.INTEGER);
                }
				
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
	/*public void updateBlobLocation(int id, Path location) throws SQLException {
		execute("UPDATE blobs.blobs set file_location = ? where id = ?", getRelativeContentLocation(location).toString(), id);
	}*/

	private void updateRecord(AttachmentRecord rec, int fileSize, int scannedFileSize, int userCreated) throws SQLException {
		Connection connection = getConnection();
		boolean hasFile = fileSize > 0;
		boolean hasSecondFile = scannedFileSize > 0;
        Integer documentId = null;
        Integer scannedDocumentId = null;

		try {
            PreparedStatement p = connection.prepareStatement(SQL_SELECT_ATTACHMENT_RECORD_DOC_IDS);
            try {
                p.setInt(1, rec.getId());

                ResultSet rs = p.executeQuery();
                try {
                    rs.next();
                    documentId = rs.getObject("document_id") == null ? null : rs.getInt("document_id");
                    scannedDocumentId = rs.getObject("scanned_document_id") == null ? null : rs.getInt("scanned_document_id");
                } finally {
                    rs.close();
                }
            } finally {
                p.close();
            }
            if (hasFile) {
                BlobRecord old = super.selectRecord(new BlobRecord(), documentId);
                old.setFilename(rec.getFileName());
                old.setContentType(rec.getContentType());
//                old.setContent(new BinaryStream(rec.getContentStream(), fileSize));
				old.setContent(null);
                old.setFilesize(fileSize);
                deleteBlobContent(this, documentId, false);
				writeContentToFileSystemAndSetFileLocation(old, rec.getContentStream());
				super.updateRecord(old);
            }
            if (hasSecondFile) {
                if (scannedDocumentId == null) { //do sega e nqmalo vtori file
                    BlobRecord newRec = new BlobRecord(getBlobSequenceNextValue(), rec.getScannedFileName(), rec.getScannedContentType(), scannedFileSize, null, new Timestamp(new java.util.Date().getTime()), userCreated, null);
					writeContentToFileSystemAndSetFileLocation(newRec, rec.getScannedContentStream());
                    newRec = super.insertRecord(newRec, false);
					scannedDocumentId = newRec.getId();
                } else {
                    BlobRecord old = super.selectRecord(new BlobRecord(), scannedDocumentId);
					old.setFilename(rec.getScannedFileName());
                    old.setContentType(rec.getScannedContentType());
                    old.setContent(null);
                    old.setFilesize(scannedFileSize);
                    deleteBlobContent(this, scannedDocumentId, false);
					writeContentToFileSystemAndSetFileLocation(old, rec.getScannedContentStream());
                    super.updateRecord(old);
                }
            }

            p = connection.prepareStatement(SQL_UPDATE_RECORD);
			try {
				
				int cnt = 1;
				
				p.setInt(cnt++, rec.getParentId());
				p.setString(cnt++, rec.getDocDescr());
				p.setObject(cnt++, rec.getDocTypeId());
                p.setObject(cnt++, rec.getCopyTypeId());
                p.setString(cnt++, rec.getDocflowId());
                p.setDate(cnt++, rec.getDocflowDate());
                p.setObject(cnt++, documentId);
                p.setObject(cnt++, scannedDocumentId);

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
                if (rec.getScannedContentStream() != null) {
                    rec.getScannedContentStream().close();
                }
			} catch (IOException e) {
			    throw Utils.logException(e);
			}
			release(connection);
		}
	}
	
	public AttachmentRecord loadRecordForPublicRegister(int applId, int[] docTypeId) throws SQLException {
	    Connection connection = getConnection();
        try {
            String inStr = "";
            String coma = "";
            for(int i : docTypeId) {
                inStr += coma + i;
                coma = ",";
            }
            String sql = SQL_SELECT_FOR_PUBLIC_REGISTER + "(" + inStr + ")";
            PreparedStatement p = connection.prepareStatement(sql);
            System.out.println(sql + "  " + applId);
            try {
                
                int cnt = 1;
                p.setInt(cnt++, applId);
                ResultSet rs = p.executeQuery();
                try {
                    AttachmentRecord rec = new AttachmentRecord();
                    if(!rs.next()) {
                    	sql = SQL_SELECT_FOR_PUBLIC_REGISTER2 + "(" + inStr + ")";
                    	System.out.println(sql + "  " + applId);
                    	p = connection.prepareStatement(sql);
                    	p.setInt(1, applId);
                    	rs = p.executeQuery();
                    	if (!rs.next()) return null;
                    }
                    rec.setScannedContentType(rs.getString("scanned_content_type"));
                    rec.setScannedContentStream(rs.getBinaryStream("scanned_content"));
                    return rec;
                }
                finally {
                    rs.close();
                }
                
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
	}
	private int getBlobSequenceNextValue() throws SQLException {
		return (int) getSequenceNextValue("blobs.blobs_id_seq");
	}
	public List<AppStatusDocTypeRecord> getApplicationStatusToDocumentTypeRecords(int applicationStatusId, int documentTypeId) throws SQLException {
		return super.selectRecords(AppStatusDocTypeRecord.class, "( app_status_id is null or app_status_id = ? ) AND doc_type_id = ?", applicationStatusId, documentTypeId);
	}
	public InputStream getBlobContent(int id ) throws SQLException {
		BlobRecord rec = super.selectRecord(new BlobRecord(), id);
		return rec.getContent() == null || rec.getContent().getInputStream() == null ? (!StringUtils.isEmpty(rec.getFileLocation()) ? getBlobContent(rec.getFileLocation()) : null) : rec.getContent().getInputStream();
	}

	public static void writeContentToFileSystemAndSetFileLocation(BlobRecord rec, InputStream inputStream) {
		try {
			Path fileLocation = generateFileLocation(rec.getId(), rec.getFilename());
			Files.createDirectories(fileLocation.getParent());
			Files.copy(inputStream, fileLocation);
			rec.setFileLocation(getRelativeContentLocation(fileLocation).toString());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static void deleteBlobContent(DatabaseService dbservice, int blobId, boolean deleteBlob) throws SQLException {
		String fileLocation = dbservice.selectRecords("select file_location as value from blobs.blobs where id = ?", StringValue.class, blobId).get(0).getValue();
		if (deleteBlob) {//bloba se iztriva ot bazata predi da se iztrie contenta, zashtoto ne e golqm problem, ako go nqma blobRecord-a, no go ima contenta. no obratnoto si e problemaciq!
			dbservice.execute("delete from blobs.blobs where id = ?", blobId);
		}
		deleteOldFiles(fileLocation);

	}
	private static void deleteOldFiles(String fileLocation) {
		try {
			Path folder = Paths.get(FILE_LOCATION_BASE).resolve(fileLocation).getParent();
			logger.debug("Deleting folder:" + folder );
            FileDeleteStrategy.FORCE.delete(folder.toFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static InputStream getBlobContent(String fileLocation)  {
		try {
			return Files.newInputStream(Paths.get(FILE_LOCATION_BASE).resolve(fileLocation));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Path getRelativeContentLocation(Path file) {
		return Paths.get(FILE_LOCATION_BASE).relativize(file);
	}

	private static Path generateFileLocation(int id, String fileName) {
		String extension = FilenameUtils.getExtension(fileName);
		if (!StringUtils.isEmpty(extension)) {
			extension = "." + extension;
		}
		return generateFolderLocation(id).resolve(id  + extension);
	}
	private static Path generateFolderLocation(int id) {
		return Paths.get(FILE_LOCATION_BASE).resolve((id / 10000) + "") .resolve(id + "");
	}
	public static void main(String[] args) throws SQLException {
		AttachmentDB db = new AttachmentDB(new StandAloneDataSource(), ATTACHMENT_TYPE.APPLICATION);
		System.out.println(db.getApplicationStatusToDocumentTypeRecords(5, 5).size());
	}
}
