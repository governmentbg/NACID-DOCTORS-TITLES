package com.nacid.bl.external.applications;

import com.nacid.bl.applications.Attachment;

import java.io.InputStream;
import java.util.List;

public interface ExtApplicationAttachmentDataProvider {

    int saveApplicationAttacment(int id, int applicationId, String docDescr, int docTypeId, String contentType, String fileName,
                                 InputStream contentStream, int copyTypeId, int fileSize, int userCreated);

	public List<Attachment> getAttachmentsForApplication(int applicationId);
	
	public List<Attachment> getAttachmentsForApplicationByType(int applicationId, int docTypeId);

	public Attachment getApplicationAttacment(int id, boolean loadContent);

	public void deleteAttachment(int id);



    public void copyRecordsToInternalDB(int extApplId, int intApplId);
}
