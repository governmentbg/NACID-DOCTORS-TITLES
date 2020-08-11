package com.nacid.web.model.applications;



public class DiplExamAttachmentWebModel extends AttachmentBaseWebModel{
	private int diplExamId;
	private String applicationId = "";
	public DiplExamAttachmentWebModel(int diplomaTypeId, String applicationId) {
		this.diplExamId = diplomaTypeId;
		this.applicationId = applicationId;
	}
	
	public DiplExamAttachmentWebModel(int id, int diplExamId, 
			String docDescr, String fileName, String applicationId, String scannedFileName,
			String docflowNum, int docType, String docflowUrl) {
		super(id, docDescr, fileName, scannedFileName, docflowNum, docType, docflowUrl);
		this.diplExamId = diplExamId;
		this.applicationId = applicationId;
	}

	public int getDiplExamId() {
		return diplExamId;
	}
    public String getApplicationId() {
        return applicationId;
    }
	
	
}
