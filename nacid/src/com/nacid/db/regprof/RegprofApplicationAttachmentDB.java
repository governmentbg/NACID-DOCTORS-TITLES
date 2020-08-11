package com.nacid.db.regprof;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.BlobRecord;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.regprof.applications.RegprofAppStatusDocTypeRecord;
import com.nacid.data.regprof.applications.RegprofApplicationAttachmentRecord;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
import com.nacid.db.applications.AttachmentDB;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
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
//import com.nacid.db.utils.StandAloneDataSource;

public class RegprofApplicationAttachmentDB extends DatabaseService {
    private static final String SQL_SELECT_APPL_BY_APPLICATION  = "select id as value from regprof.regprof_attached_docs where parent_id=?";

    private static final String SQL_SELECT_ATT_BY_APPLICATION_AND_DOC_TYPE = "select id from regprof.regprof_attached_docs where parent_id = ? ";

    private static final String SQL_SELECT_FOR_PUBLIC_REGISTER = 
        "select b.content_type as scanned_content_type, b.content as scanned_content \n" +
                "from regprof.regprof_attached_docs ad\n" +
                "join blobs.blobs b on ad.scanned_document_id = b.id\n" +
                "where parent_id = ? and b.content_type is not null \n" +
                "and doc_type_id in ";
    
    private static final String SQL_SELECT_FOR_PUBLIC_REGISTER2 = 
        "select b.content_type as scanned_content_type, b.content as scanned_content \n" +
                "from regprof.regprof_attached_docs ad\n" +
                "join blobs.blobs b on b.id = ad.document_id\n" +
                "where parent_id= ? and content_type is not null \n" +
                "and doc_type_id in  ";
    
    private static final String SQL_SELECT_PROF_EXPERIENCE_EXAM_DOCS = "SELECT AD.id as value FROM regprof.profession_experience_exam_attached_docs AD " +
                                                                       "JOIN regprof.profession_experience_documents D ON AD.parent_id = D.id " +
                                                                       "JOIN regprof.profession_experience E ON D.prof_experience_id = E.id " +
                                                                       "JOIN regprof.training_course T ON E.training_course_id = T.id " +
                                                                       "JOIN regprof.regprof_application A ON A.training_course_id = T.id " +
                                                                       "WHERE A.id = ?";
    
    //RayaWritten---------------------------------------------
    private static final String SQL_SELECT_DOCEXAM_BY_APPLICATION  = "select regprof.doc_exam_attached_docs.id as value from regprof.doc_exam_attached_docs " +
    " left join regprof.document_examination on (regprof.doc_exam_attached_docs.parent_id = regprof.document_examination.id) " +
    " left join regprof.training_course on (regprof.training_course.id = regprof.document_examination.training_course_id) " +
    " left join regprof.regprof_application on (regprof.regprof_application.training_course_id = regprof.training_course.id) " + 
    " where regprof.regprof_application.id = ? ";
    //------------------------------------------------------------------------------------------------

    private String SQL_DELETE_BY_ID;
    private String SQL_SELECT_BY_PARENT;
    private String SQL_SELECT_BY_ID;
    private String SQL_UPDATE_RECORD;
    private String SQL_INSERT_RECORD;
    private String SQL_SELECT_ATTACHMENTS_BY_APPL_ID;
    private String SQL_SELECT_ATTACHMENTS_BY_APPL_ID_AND_DOC_TYPE_IDS;
    private String SQL_SELECT_ATTACHMENT_RECORD_DOC_IDS;
    private String SQL_UPDATE_DOCUMENT_TYPE;
       
    public static enum REGPROF_ATTACHMENT_TYPE {
        REGPROF_APPLICATION("regprof.regprof_attached_docs"),
        DOCUMENT_EXAMINATION("regprof.doc_exam_attached_docs"),
        REGPROF_PROFESSION_EXPERIENCE_DOCUMENTS("regprof.profession_experience_exam_attached_docs");
        private String tableName;
        private REGPROF_ATTACHMENT_TYPE(String tableName) {
            this.tableName = tableName;
        }
        private String getTableName() {
            return tableName;
        }
    }
    
    //RayaWritten------------------------------------------------
    public RegprofApplicationAttachmentDB(DataSource dataSource,
            REGPROF_ATTACHMENT_TYPE t) {
        super(dataSource);
        
        switch(t) {
            case REGPROF_APPLICATION:
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID = SQL_SELECT_APPL_BY_APPLICATION;
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID_AND_DOC_TYPE_IDS = SQL_SELECT_ATTACHMENTS_BY_APPL_ID + " AND regprof_attached_docs.doc_type_id in ({DOC_TYPES})";
                break;
            case DOCUMENT_EXAMINATION:
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID = SQL_SELECT_DOCEXAM_BY_APPLICATION;
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID_AND_DOC_TYPE_IDS = SQL_SELECT_ATTACHMENTS_BY_APPL_ID + " AND doc_exam_attached_docs.doc_type_id in ({DOC_TYPES})";
                break;
            case REGPROF_PROFESSION_EXPERIENCE_DOCUMENTS:
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID = SQL_SELECT_PROF_EXPERIENCE_EXAM_DOCS;
                SQL_SELECT_ATTACHMENTS_BY_APPL_ID_AND_DOC_TYPE_IDS = SQL_SELECT_ATTACHMENTS_BY_APPL_ID + " AND AD.doc_type_id in ({DOC_TYPES})";
                break;
        }
        
        SQL_DELETE_BY_ID = "delete from "+t.getTableName()+" where id=?";
        SQL_SELECT_BY_PARENT = "select ad.id,parent_id,doc_descr,doc_type_id, copy_type_id,docflow_id,docflow_date,\n" +
                "doc.content_type, doc.filename as file_name,\n" +
                "sdoc.content_type as scanned_content_type, sdoc.filename as scanned_file_name\n" +
                "from " + t.getTableName() + " ad\n" +
                "join blobs.blobs doc on doc.id = ad.document_id\n" +
                "left join blobs.blobs sdoc on sdoc.id = ad.scanned_document_id\n" +
                "where ad.parent_id = ?";


        SQL_SELECT_BY_ID = "select ad.id,parent_id,doc_descr,doc_type_id, copy_type_id,docflow_id,docflow_date,\n" +
                "doc.content_type, doc.filename as file_name, {DOCUMENT_CONTENT} as doc_content, doc.file_location as document_file_location, \n" +
                "sdoc.content_type as scanned_content_type, sdoc.filename as scanned_file_name, doc.id as document_id, sdoc.id as scanned_document_id, {SCANNED_DOCUMENT_CONTENT} as scanned_content, sdoc.file_location as scanned_document_file_location\n" +
                "from " + t.getTableName() + " ad\n" +
                "join blobs.blobs doc on doc.id = ad.document_id\n" +
                "left join blobs.blobs sdoc on sdoc.id = ad.scanned_document_id\n" +
                "where ad.id = ?";


        SQL_INSERT_RECORD = "INSERT INTO " + t.getTableName() + " (parent_id, doc_descr, doc_type_id, copy_type_id, docflow_id, docflow_date, document_id, scanned_document_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        SQL_UPDATE_RECORD = "UPDATE " + t.getTableName() + " SET parent_id = ?, doc_descr = ?, doc_type_id = ?, copy_type_id = ?, docflow_id = ?, docflow_date = ?, document_id = ?, scanned_document_id = ? WHERE id = ?";

        SQL_SELECT_ATTACHMENT_RECORD_DOC_IDS = "select document_id, scanned_document_id from " + t.getTableName() + " WHERE id = ?";

        SQL_UPDATE_DOCUMENT_TYPE = "UPDATE " + t.getTableName() + " SET doc_type_id = ? WHERE id = ?";
    }
    //--------------------------------------------------------------
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
                        ret.add(rs.getInt("value"));
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

    public List<Integer> getAttachmentIdsByApplicationAndDocumentTypes(int applicationId, List<Integer> docTypeIds) throws SQLException {
        String sql = SQL_SELECT_ATTACHMENTS_BY_APPL_ID_AND_DOC_TYPE_IDS;
        sql = sql.replace("{DOC_TYPES}", SQLUtils.columnsToParameterList(StringUtils.join(docTypeIds, ", ")));
        List<Object> objects = new ArrayList<Object>();
        objects.add(applicationId);
        objects.addAll(docTypeIds);
        List<IntegerValue> vals = super.selectRecords(sql, IntegerValue.class, objects.toArray());
        List<Integer> result = new ArrayList<Integer>();
        for (IntegerValue val : vals) {
            result.add(val.getValue());
        }
        return result;
    }

    public void updateDocumentType(int attachmentId, int documentType) throws SQLException {
        super.execute(SQL_UPDATE_DOCUMENT_TYPE, documentType, attachmentId);
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

            super.execute(SQL_DELETE_BY_ID, id);
            AttachmentDB.deleteBlobContent(this, documentId, true);
            if (scannedDocumentId != null) {
                AttachmentDB.deleteBlobContent(this, scannedDocumentId, true);
            }

        } finally {
            release(connection);
        }
    }
    public void deleteCertificateNumberToAttachedDocRecords(int attachmentId) throws SQLException{
        super.deleteRecords(RegprofCertificateNumberToAttachedDocRecord.class, " attached_doc_id = ? ", attachmentId);
    }
    public void invalidateCertificateNumber(int attachmentId) throws SQLException {
        String sql = "update regprof.cert_number_to_attached_doc set invalidated = 1 where attached_doc_id = ?";
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            try {
                ps.setInt(1, attachmentId);
                ps.executeUpdate();
            } finally {
                ps.close();
            }
        } finally {
            con.close();
        }
    }
    
    public RegprofApplicationAttachmentRecord loadRecord(int id, boolean loadContent, boolean loadScannedContent) throws SQLException {
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
                        RegprofApplicationAttachmentRecord r = new RegprofApplicationAttachmentRecord();
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
                        String fileLocation = rs.getString("document_file_location");
                        String scannedDocumentFileLocation = rs.getString("scanned_document_file_location");
                        if (loadContent) {
                            InputStream is = rs.getBinaryStream("doc_content");
                            if (is == null && !StringUtils.isEmpty(fileLocation)) {//the content is inside the filesystem!
                                is = getBlobContent(fileLocation);
                            }
                            r.setContentStream(is);
                        }

                        if (loadScannedContent) {
                            InputStream is = rs.getBinaryStream("scanned_content");
                            if (is == null && !StringUtils.isEmpty(scannedDocumentFileLocation)) {
                                is = getBlobContent(scannedDocumentFileLocation);
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
    
    public List<RegprofApplicationAttachmentRecord> loadRecordsForParent(int parentId, int docTypeId) throws SQLException {
        Connection connection = getConnection();
        try {
            String sqlAddition = "";
            if(docTypeId > 0) {
                sqlAddition = " and doc_type_id = ? order by id ASC";
            }
            PreparedStatement p = connection.prepareStatement(SQL_SELECT_BY_PARENT + sqlAddition);
            try {
                
                p.setInt(1, parentId);
                
                if(docTypeId > 0) {
                    p.setInt(2, docTypeId);
                }
                
                ResultSet rs = p.executeQuery();
                return attachmentsResultSetToAttachmentsList(rs);
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }



    public List<RegprofApplicationAttachmentRecord> loadRecordsForParent(int parentId, List<Integer> docTypeId) throws SQLException {
        Connection connection = getConnection();
        try {
            String sqlAddition = "";
            if(docTypeId != null && docTypeId.size() > 0) {
                sqlAddition = " and doc_type_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(docTypeId, ", ")) + ") order by id ASC";
            }
            PreparedStatement p = connection.prepareStatement(SQL_SELECT_BY_PARENT + sqlAddition);
            try {
                int arg = 1;
                p.setInt(arg++, parentId);
                if(docTypeId != null && docTypeId.size() > 0) {
                    for (Integer i : docTypeId) {
                        p.setInt(arg++, i);
                    }
                }
                ResultSet rs = p.executeQuery();
                return attachmentsResultSetToAttachmentsList(rs);
            } finally {
                p.close();
            }
        } finally {
            release(connection);
        }
    }

    private List<RegprofApplicationAttachmentRecord> attachmentsResultSetToAttachmentsList(ResultSet rs) throws SQLException {
        try {
            ArrayList<RegprofApplicationAttachmentRecord> ret = new ArrayList<RegprofApplicationAttachmentRecord>();
            while(rs.next()) {
                RegprofApplicationAttachmentRecord r = new RegprofApplicationAttachmentRecord();
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

                ret.add(r);
            }
            return ret;
        } finally {
            rs.close();
        }
    }
    
    public RegprofApplicationAttachmentRecord saveRecord(RegprofApplicationAttachmentRecord rec, int fileSize, int scannedFileSize, int userCreated) throws SQLException {
        RegprofApplicationAttachmentRecord ret = rec;
        if(rec.getId() == 0) {
            ret = insertRecord(rec, fileSize, scannedFileSize, userCreated);
        }
        else {
            updateRecord(rec, fileSize, scannedFileSize, userCreated);
        }
        return ret;
    }
    
    private RegprofApplicationAttachmentRecord insertRecord(RegprofApplicationAttachmentRecord rec, int fileSize, int scannedFileSize, int userCreated) throws SQLException {
        boolean hasSecondFile = scannedFileSize > 0;
        Connection connection = getConnection();
        try {

            Timestamp dateCreated = new Timestamp(new java.util.Date().getTime());
            BlobRecord document = new BlobRecord(getBlobSequenceNextValue(), rec.getFileName(), rec.getContentType(), fileSize, null, dateCreated, userCreated, null);
            AttachmentDB.writeContentToFileSystemAndSetFileLocation(document, rec.getContentStream());
            document = super.insertRecord(document, false);

            BlobRecord scannedDocument = null;
            if (hasSecondFile) {
                scannedDocument = new BlobRecord(getBlobSequenceNextValue(), rec.getFileName(), rec.getContentType(), scannedFileSize, null, dateCreated, userCreated, null);
                AttachmentDB.writeContentToFileSystemAndSetFileLocation(scannedDocument, rec.getScannedContentStream());
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
                p.setObject(cnt++, scannedDocument == null ? null : scannedDocument.getId());

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
    
    private void updateRecord(RegprofApplicationAttachmentRecord rec, int fileSize, int scannedFileSize, int userCreated) throws SQLException {
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

                AttachmentDB.deleteBlobContent(this, documentId, false);
                AttachmentDB.writeContentToFileSystemAndSetFileLocation(old, rec.getContentStream());

                super.updateRecord(old);
            }
            if (hasSecondFile) {
                if (scannedDocumentId == null) { //do sega e nqmalo vtori file
                    BlobRecord newRec = new BlobRecord(getBlobSequenceNextValue(), rec.getScannedFileName(), rec.getScannedContentType(), scannedFileSize, null, new Timestamp(new java.util.Date().getTime()), userCreated, null);
                    AttachmentDB.writeContentToFileSystemAndSetFileLocation(newRec, rec.getScannedContentStream());
                    newRec = super.insertRecord(newRec, false);
                    scannedDocumentId = newRec.getId();
                } else {
                    BlobRecord old = super.selectRecord(new BlobRecord(), scannedDocumentId);
                    old.setFilename(rec.getScannedFileName());
                    old.setContentType(rec.getScannedContentType());
//                    old.setContent(new BinaryStream(rec.getScannedContentStream(), scannedFileSize));
                    old.setContent(null);
                    old.setFilesize(scannedFileSize);
                    AttachmentDB.deleteBlobContent(this, scannedDocumentId, false);
                    AttachmentDB.writeContentToFileSystemAndSetFileLocation(old, rec.getScannedContentStream());
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
    
    public RegprofApplicationAttachmentRecord loadRecordForPublicRegister(int applId, int[] docTypeId) throws SQLException {
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
                    RegprofApplicationAttachmentRecord rec = new RegprofApplicationAttachmentRecord();
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
    public List<RegprofAppStatusDocTypeRecord> getApplicationStatusToDocumentTypeRecords(int applicationStatusId, int documentTypeId) throws SQLException {
        return super.selectRecords(RegprofAppStatusDocTypeRecord.class, "( app_status_id is null or app_status_id = ? ) AND doc_type_id = ?", applicationStatusId, documentTypeId);
    }
    private int getBlobSequenceNextValue() throws SQLException {
        return (int) getSequenceNextValue("blobs.blobs_id_seq");
    }
    public static void main(String[] args) throws SQLException {
        StandAloneDataSource ds = new StandAloneDataSource();
        RegprofApplicationAttachmentDB db = new RegprofApplicationAttachmentDB(ds, REGPROF_ATTACHMENT_TYPE.REGPROF_APPLICATION);
        List<RegprofApplicationAttachmentRecord> res = db.loadRecordsForParent(259, DocumentType.REGPROF_CERTIFICATES);
        System.out.println(res);
    }

}
