package com.nacid.bl.applications;

import java.io.InputStream;
import java.util.List;

public interface DiplomaTypeAttachmentDataProvider {

	public int saveDiplomaTypeAttacment(int id, int diplomaTypeId, String docDescr, int docTypeId, InputStream contentStream, String fileName, String contentType, int fileSize, int userCreated);

	public List<Attachment> getAttachmentsForDiplomaType(int diplomaTypeId);

	public Attachment getDiplomaTypeAttacment(int id, boolean loadContent);

	public void deleteRecord(int id);

}
