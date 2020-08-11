package com.nacid.bl.applications.regprof;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.nomenclatures.DocumentType;

public interface RegprofApplicationAttachmentDataProvider {
    
    /**
     * Zapisva attachment. Ako docType-a e {@link DocumentType#DOC_TYPE_CERTIFICATE} togava generira certificate number
     * i go zapisva v cert_number_to_attached_doc. Proverkata dali moje da se generira udostoverenie za tozi application ostava v Handler-a!!!
     */
    public int saveAttachment(
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
            int scannedFileSize, int userCreated);
    /**
     * zapisva attachment. Celta e da se polzva samo za slu4aite kogato DocumentType-a e {@link DocumentType#DOC_TYPE_CERTIFICATE} i trqbva da se podade 
     * ry4no certificateNumber, a ne da se generira takyv!
     * Ako certificateNumber == null, method-a se dyrji po abosliutno sy6tiq na4in kato @link {@link AttachmentDataProvider#saveAttacment(int, int, String, Integer, Integer, String, Date, String, String, InputStream, int, String, String, InputStream, int)} 
     */
    public int saveAttachment(
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
            UUID uuid,
            int userCreated);
    
    /**
     * @param parentId. Specialno za ApplicationAttachments parentId znachi applicationId
     * @param docTypeId
     * @return
     */
    public List<RegprofApplicationAttachment> getAttachmentsForParentByType(int parentId, int docTypeId);
    public List<RegprofApplicationAttachment> getAttachmentsForParent(int parentId);

    public RegprofApplicationAttachment getAttachment(int id, boolean loadContent, boolean loadScannedContent);
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

    public RegprofApplicationAttachment loadattachmentForPublicRegister(int applId, int[] docTypeId);

    /**
     * @param documentType - tip dokument
     * @param applicationStatusId
     * @return - dali zaqvlenie sys status applicationStatusId moje da generira dokument ot tip documentType
     */
    public boolean hasAccessToGenerateDocument(int applicationStatusId, int documentTypeId);
    
    /**
     * 
     * @param parentId - id na zaqvlenie
     * @param docTypeIds - spisyk ot id-ta na tip dokument
     * @return - vryshta attachmentite kym dadeno zaqvlenie, koito sa ot tip, izbroen v docTypeIds
     */
    public List<RegprofApplicationAttachment> getAttachmentsForParentByTypes(int parentId, List<Integer> docTypeIds);
}
