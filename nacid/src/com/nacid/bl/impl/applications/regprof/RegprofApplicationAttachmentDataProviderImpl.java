package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.applications.CertificateNumberGenerationException;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.regprof.applications.RegprofApplicationAttachmentRecord;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
import com.nacid.db.regprof.RegprofApplicationAttachmentDB;
import com.nacid.db.regprof.RegprofApplicationAttachmentDB.REGPROF_ATTACHMENT_TYPE;
import com.nacid.db.utils.StandAloneDataSource;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class RegprofApplicationAttachmentDataProviderImpl implements RegprofApplicationAttachmentDataProvider {
    
    private RegprofApplicationAttachmentDB db;
    private NacidDataProviderImpl nacidDataProvider;
    
    public RegprofApplicationAttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new RegprofApplicationAttachmentDB(nacidDataProvider.getDataSource(), REGPROF_ATTACHMENT_TYPE.REGPROF_APPLICATION);
        this.nacidDataProvider = nacidDataProvider;
    }
    //RayaWritten--------------------------------------------
    public RegprofApplicationAttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider, REGPROF_ATTACHMENT_TYPE t) {
        this.db = new RegprofApplicationAttachmentDB(nacidDataProvider.getDataSource(), t);
        this.nacidDataProvider = nacidDataProvider;
    }
    //--------------------------------------------------------

    @Override
    public void deleteAttachment(int id) {
        try {
            RegprofApplicationAttachment att = getAttachment(id, false, false);
            db.deleteCertificateNumberToAttachedDocRecords(id);
            db.deleteRecord(id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public RegprofApplicationAttachment getAttachment(int id, boolean loadContent, boolean loadScannedContent) {
        try {
            RegprofApplicationAttachmentRecord rec = db.loadRecord(id, loadContent, loadScannedContent);
            if(rec == null) {
                return null;
            }
            return new RegprofApplicationAttachmentImpl(rec, nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<RegprofApplicationAttachment> getAttachmentsForParent(int parentId) {
        try {
            List<RegprofApplicationAttachment> ret = new ArrayList<RegprofApplicationAttachment>();
            
            List<RegprofApplicationAttachmentRecord> recs = db.loadRecordsForParent(parentId, 0);
            for (RegprofApplicationAttachmentRecord rec : recs) {
                ret.add(new RegprofApplicationAttachmentImpl(rec, nacidDataProvider));
            }
            return ret; 
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<RegprofApplicationAttachment> getAttachmentsForParentByType(int parentId, int docTypeId) {
        try {
            List<RegprofApplicationAttachment> ret = new ArrayList<RegprofApplicationAttachment>();
            
            List<RegprofApplicationAttachmentRecord> recs = db.loadRecordsForParent(parentId, docTypeId);
            for (RegprofApplicationAttachmentRecord rec : recs) {
                ret.add(new RegprofApplicationAttachmentImpl(rec, nacidDataProvider));
            }
            return ret; 
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public List<RegprofApplicationAttachment> getAttachmentsForParentByTypes(int parentId, List<Integer> docTypeIds) {
        List<RegprofApplicationAttachment> ret = new ArrayList<RegprofApplicationAttachment>();
        if (docTypeIds == null || docTypeIds.isEmpty()) {
            return ret; // return an empty list
        }
        try {
            List<RegprofApplicationAttachmentRecord> recs = db.loadRecordsForParent(parentId, docTypeIds);
            for (RegprofApplicationAttachmentRecord rec : recs) {
                ret.add(new RegprofApplicationAttachmentImpl(rec, nacidDataProvider));
            }
            return ret;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public int saveAttachment(int id, int parentId, String docDescr, Integer docTypeId, Integer copyTypeId, String docflowId, Date docflowDate,
            String contentType, String fileName, InputStream contentStream, int fileSize, String scannedContentType, String scannedFileName,
            InputStream scannedContentStream, int scannedFileSize, int userCreated) {
        return saveAttachment(id, parentId, docDescr, docTypeId, copyTypeId, docflowId, docflowDate, contentType, fileName, contentStream, fileSize, 
                scannedContentType, scannedFileName, scannedContentStream, scannedFileSize, null, null, userCreated);
    }
    
    @Override
    public int saveAttachment(int id, int parentId, String docDescr, Integer docTypeId, Integer copyTypeId, String docflowId, Date docflowDate,
                              String contentType, String fileName, InputStream contentStream, int fileSize, String scannedContentType, String scannedFileName,
                              InputStream scannedContentStream, int scannedFileSize, String certificateNumber, UUID uuid, int userCreated) {
        
        try {
            RegprofApplicationAttachmentRecord rec = new RegprofApplicationAttachmentRecord();
            rec.setId(id);
            rec.setParentId(parentId);
            rec.setDocDescr(docDescr);
            rec.setDocTypeId(docTypeId);
            rec.setCopyTypeId(copyTypeId);
            rec.setDocflowId(docflowId);
            rec.setDocflowDate(Utils.getSqlDate(docflowDate));
            rec.setContentType(contentType);
            rec.setFileName(fileName);
            rec.setContentStream(contentStream);
            rec.setScannedContentType(scannedContentType);
            rec.setScannedFileName(scannedFileName);
            rec.setScannedContentStream(scannedContentStream);
            boolean isCertificate = (DocumentType.REGPROF_CERTIFICATES.contains(docTypeId));
            rec = db.saveRecord(rec, fileSize, scannedFileSize, userCreated);
            //2020.07.10 -  v stariq kod Rostislav praveshe zapis v RegprofCertificateNumberToAttachedDoc chak kogato se natisne butona "zavedi v delovodnata sistema".
            // Sega nqma kak da se pravi chak tam zapisa, zashtoto i qr-code-a trqbva da se zapisva, a toi nqma otkyde da se vzeme, ako ne se zapishe oshte pri samoto generirane! Certrificate-a pri povtorno generirane poluchavshe sy6tiq nomer,
            // taka che pri nego nqmashe problem za zapis pri zavejdane v delovodnata
            if (id == 0 && isCertificate) {
                if ((certificateNumber == null || uuid == null)) {
                    throw new RuntimeException("Certificate number and uuid should not be null");
                } else {
                    RegprofApplicationDataProviderImpl applicationsDataProviderImpl = nacidDataProvider.getRegprofApplicationDataProvider();
                    applicationsDataProviderImpl.saveCertificateNumber(parentId, rec.getId(), certificateNumber, uuid);
                }
            } else if (id != 0 && docTypeId == DocumentType.DOC_TYPE_OBEZSILENO) {
                //Ako attachment-a stava obezsileno udostoverenie, to trqbva da se update-ne i cert_number_to_attached_doc
                db.invalidateCertificateNumber(id);
            }

            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<Integer> getAttachmentIdsByApplication(int applicationId) {
        try {
            List<Integer> res = db.getAttachmentIdsByApplication(applicationId); 
            return res.size() == 0 ? null : res;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public RegprofApplicationAttachment loadattachmentForPublicRegister(int applId, int[] docTypeId) {
        try {
            RegprofApplicationAttachmentRecord rec = db.loadRecordForPublicRegister(applId, docTypeId);
            if(rec == null) return null;
            return new RegprofApplicationAttachmentImpl(rec, nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public boolean hasAccessToGenerateDocument(int applicationStatusId, int documentTypeId) {
        try {
            return db.getApplicationStatusToDocumentTypeRecords(applicationStatusId, documentTypeId).size() != 0;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public static void main(String[] args) {
        RegprofApplicationAttachmentDB db = new RegprofApplicationAttachmentDB(new StandAloneDataSource(), REGPROF_ATTACHMENT_TYPE.REGPROF_APPLICATION);
        try {
            System.out.println(db.getApplicationStatusToDocumentTypeRecords(3, 81).size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
