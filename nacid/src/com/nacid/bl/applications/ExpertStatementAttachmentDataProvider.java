package com.nacid.bl.applications;

import java.io.InputStream;
import java.util.List;

public interface ExpertStatementAttachmentDataProvider {

	public List<ExpertStatementAttachment> getExpertStatementsForApplication(int applicationId);
	
	public List<ExpertStatementAttachment> getExpertStatementsForApplicationAndExpert(int applicationId, int expertId);

	public ExpertStatementAttachment getExpertStatementAttachment(int id, boolean loadContent);

    public int saveExpertStatementAttacment(int id, int expertId, String docDescr, int docTypeId, InputStream is, String fileName, String contentType,
            int fileSize, int applicationId);

    public void deleteRecord(int attId);

}
