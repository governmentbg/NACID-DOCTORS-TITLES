package com.nacid.bl.applications;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.nacid.bl.nomenclatures.DocumentType;

public interface AttachmentDataProvider {
    public static final int CERTIFICATE_STATUS_PUBLISHED = 0;
    public static final int CERTIFICATE_STATUS_OBEZSILENO = 1;
    public static final int CERTIFICATE_STATUS_UNISHTOJENO = 2;

    /**
	 * Zapisva attachment. Ako docType-a e {@link DocumentType#DOC_TYPE_CERTIFICATE} togava generira certificate number
	 * i go zapisva v cert_number_to_attached_doc. Proverkata dali moje da se generira udostoverenie za tozi application ostava v Handler-a!!!
	 */
	public int saveAttacment(
	        int id,
	        int parentId,
	        String docDescr,
	        Integer docTypeId,
	        Integer copyTypeId,
            String docflowId,
            Date docflowDate,
            
	        String contentType,
	        String fileName,
	        InputStream contentStream,
	        int fileSize,
	        
	        String scannedContentType,
	        String scannedFileName,
	        InputStream scannedContentStream,
	        int scannedFileSize,
            int userCreated);
	/**
	 * zapisva attachment. Celta e da se polzva samo za slu4aite kogato DocumentType-a e {@link DocumentType#DOC_TYPE_CERTIFICATE} i trqbva da se podade 
	 * ry4no certificateNumber, a ne da se generira takyv!
	 * Ako certificateNumber == null, method-a se dyrji po abosliutno sy6tiq na4in kato @link {@link AttachmentDataProvider#saveAttacment(int, int, String, Integer, Integer, String, Date, String, String, InputStream, int, String, String, InputStream, int, int)}
	 */
	public int saveAttacment(
	        int id,
	        int parentId,
	        String docDescr,
	        Integer docTypeId,
	        Integer copyTypeId,
            String docflowId,
            Date docflowDate,
            
	        String contentType,
	        String fileName,
	        InputStream contentStream,
	        int fileSize,
	        
	        String scannedContentType,
	        String scannedFileName,
	        InputStream scannedContentStream,
	        int scannedFileSize,
	        String certificateNumber,
			UUID certficateUuid,
            int userCreated);
	
	
	
	/**
	 * @param parentId Specialno za ApplicationAttachments parentId znachi applicationId
	 * @param docTypeId
	 * @return
	 */
	public List<Attachment> getAttachmentsForParentByType(int parentId, int docTypeId);
	public List<Attachment> getAttachmentsForParentByTypes(int parentId, List<Integer> docTypeIds);
	public List<Attachment> getAttachmentsForParent(int parentId);

	public Attachment getAttachment(int id, boolean loadContent, boolean loadScannedContent);
	/**
	 * ako attachment-a e udostoverenie, to se iztriva i zapisa mu v cert_number_to_attached_doc
	 * @param id
	 */
	public void deleteAttachment(int id);

	/**
	 * vry6ta ID-tata na DiplExamAttachments po zadaden applicationId
	 * @param applicationId
	 * @return
	 */
	public List<Integer> getAttachmentIdsByApplication(int applicationId);

    public Attachment loadattachmentForPublicRegister(int applId, int[] docTypeId);

    /**
     * @param applicationStatusId
	 * @param documentTypeId - tip dokument
     * @return - dali zaqvlenie sys status applicationStatusId moje da generira dokument ot tip documentType
     */
    public boolean hasAccessToGenerateDocument(int applicationStatusId, int documentTypeId);

	public InputStream getBlobContent(int id);
}
