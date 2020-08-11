package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.applications.AttachmentRecord;
import com.nacid.data.applications.CertificateNumberToAttachedDocRecord;
import com.nacid.db.applications.AttachmentDB;
import com.nacid.db.applications.AttachmentDB.ATTACHMENT_TYPE;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AttachmentDataProviderImpl implements AttachmentDataProvider {

	private AttachmentDB db;
	private NacidDataProviderImpl nacidDataProvider;
    private ATTACHMENT_TYPE attachmentType;
	public AttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider, ATTACHMENT_TYPE t) {
		this.db = new AttachmentDB(nacidDataProvider.getDataSource(), t);
		this.nacidDataProvider = nacidDataProvider;
        this.attachmentType = t;
	}
	
	@Override
	public void deleteAttachment(int id) {
		try {
		    Attachment att = getAttachment(id, false, false);
		    db.deleteCertificateNumberToAttachedDocRecords(id);
			db.deleteRecord(id);
			nacidDataProvider.getEventDataProvider()
			    .deleteEventsForDocument(id, att.getDocType().getId());
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public Attachment getAttachment(int id, boolean loadContent, boolean loadScannedContent) {
		try {
			AttachmentRecord rec = db.loadRecord(id, loadContent, loadScannedContent);
			if(rec == null) {
				return null;
			}
			return new AttachmentImpl(rec, nacidDataProvider);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	public InputStream getBlobContent(int id) {
		try {
			return db.getBlobContent(id);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public List<Attachment> getAttachmentsForParent(int parentId) {
		try {
			ArrayList<Attachment> ret = new ArrayList<Attachment>();
			
			List<AttachmentRecord> recs = db.loadRecordsForParent(parentId, null);
			for(AttachmentRecord rec :recs) {
				ret.add(new AttachmentImpl(rec, nacidDataProvider));
			}
			return ret; 
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
    public List<Attachment> getAttachmentsForParentByType(int parentId, int docTypeId) {
		return getAttachmentsForParentByTypes(parentId, docTypeId == 0 ? null : Arrays.asList(docTypeId));
    }



	@Override
	public List<Attachment> getAttachmentsForParentByTypes(int parentId, List<Integer> docTypeIds) {
		try {

			return	db.loadRecordsForParent(parentId, docTypeIds == null ? null : docTypeIds.stream().filter(dt -> dt > 0).collect(Collectors.toList()))
					.stream()
					.map(a -> new AttachmentImpl(a, nacidDataProvider))
					.collect(Collectors.toList());
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	public int saveAttacment(int id, int parentId, String docDescr, Integer docTypeId, Integer copyTypeId, String docflowId, Date docflowDate,
							 String contentType, String fileName, InputStream contentStream, int fileSize, String scannedContentType, String scannedFileName,
							 InputStream scannedContentStream, int scannedFileSize, int userCreated) {
		return saveAttacment(id, parentId, docDescr, docTypeId, copyTypeId, docflowId, docflowDate, contentType, fileName, contentStream, fileSize, scannedContentType, scannedFileName, scannedContentStream, scannedFileSize, null, null, userCreated);
	}
	
	@Override
	public int saveAttacment(int id, int parentId, String docDescr, Integer docTypeId, Integer copyTypeId, String docflowId, Date docflowDate,
							 String contentType, String fileName, InputStream contentStream, int fileSize, String scannedContentType, String scannedFileName,
							 InputStream scannedContentStream, int scannedFileSize, String certificateNumber, UUID certificateUuid, int userCreated) {
	    
	    try {
	    	boolean isCertificate = DocumentType.RUDI_AND_RUDI_DOCTORATE_CERTIFICATE_TYPES.contains(docTypeId);
			if (id == 0 && isCertificate && (certificateNumber == null || certificateUuid == null)) {
				throw new RuntimeException("Certificate number should not be empty!!!");
			}
	    	AttachmentRecord rec = new AttachmentRecord();
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

            if (isCertificate) {
                List<CertificateNumberToAttachedDocRecord> certNumbers = nacidDataProvider.getApplicationsDataProvider().getAllCertificateNumbers(rec.getParentId());
                if (certificateNumber != null) {
                    for (CertificateNumberToAttachedDocRecord certNumber : certNumbers) {
                        if (certNumber.getInvalidated() == 0 && !Objects.equals(id, certNumber.getAttachedDocId())) {
                            throw new RuntimeException("Едно заявление не може да има повече от едно активно удостоверение!");
                        }
                    }
                }
            }
            rec = db.saveRecord(rec, fileSize, scannedFileSize, userCreated);
            
            if (id == 0 && isCertificate) {
            	ApplicationsDataProviderImpl applicationsDataProviderImpl = nacidDataProvider.getApplicationsDataProvider();
				applicationsDataProviderImpl.saveCertificateNumber(parentId, rec.getId(), certificateNumber, certificateUuid);
            } else if (docTypeId == DocumentType.DOC_TYPE_OBEZSILENO || docTypeId == DocumentType.DOC_TYPE_DOCTORATE_OBEZSILENO) { //Ako attachment-a stava obezsileno udostoverenie, to trqbva da se update-ne i cert_number_to_attached_doc
            	db.invalidateCertificateNumber(rec.getId(), CERTIFICATE_STATUS_OBEZSILENO);
            } else if (docTypeId == DocumentType.DOC_TYPE_UNISHTOJENO || docTypeId == DocumentType.DOC_TYPE_DOCTORATE_UNISHTOJENO) { //Ako attachment-a stava unishtojeno udostoverenie, to trqbva da se update-ne i cert_number_to_attached_doc
                db.invalidateCertificateNumber(rec.getId(), CERTIFICATE_STATUS_UNISHTOJENO);
            }
            
            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	}
	
	@Override
	public List<Integer> getAttachmentIdsByApplication(int applicationId)  {
		try {
			List<Integer> res = db.getAttachmentIdsByApplication(applicationId); 
			return res.size() == 0 ? null : res;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public Attachment loadattachmentForPublicRegister(int applId, int[] docTypeId) {
	    try {
	        AttachmentRecord rec = db.loadRecordForPublicRegister(applId, docTypeId);
	        if(rec == null) return null;
            return new AttachmentImpl(
                    rec, nacidDataProvider);
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

}
