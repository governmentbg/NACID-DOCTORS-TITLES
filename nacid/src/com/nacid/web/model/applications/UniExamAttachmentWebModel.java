package com.nacid.web.model.applications;



public class UniExamAttachmentWebModel extends AttachmentBaseWebModel{

    private String applicationId;
	
	private int universityValidityId;
	private String groupName = "";
	public UniExamAttachmentWebModel(int diplomaTypeId, String applicationId, String groupName) {
		this.universityValidityId = diplomaTypeId;
		this.applicationId = applicationId;
		this.groupName = groupName;
		
	}
	
	public UniExamAttachmentWebModel(int id, int universityValidityId, 
			String docDescr, String fileName, String applicationId, 
			String groupName, String scannedFileName,
			String docflowNum, int docType, String docflowUrl) {
		super(id, docDescr, fileName, scannedFileName, docflowNum, docType, docflowUrl);
		this.universityValidityId = universityValidityId;
		this.applicationId = applicationId;
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public int getDiplomaTypeId() {
		return universityValidityId;
	}

	public String getApplicationId() {
	    return applicationId;
	}
}
