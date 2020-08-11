package com.nacid.web.model.applications;

import java.util.List;



public class ApplicationAttachmentWebModel extends AttachmentBaseWebModel{
	private int applicationId;
	private List<Integer> appStatusesToAllowCertNumberAssign;
	public ApplicationAttachmentWebModel(int applicationId, List<Integer> appStatusesToAllowCertNumberAssign/*, boolean allowCertNumberAssign*/) {
		super (0, "", "", null, null, null, null);
		this.applicationId = applicationId;
		this.appStatusesToAllowCertNumberAssign = appStatusesToAllowCertNumberAssign;
	}
	public ApplicationAttachmentWebModel(int id, int applicationId, 
			String docDescr, String fileName, String scannedFileName,
			String docflowNum, int docType, String docflowUrl) {
		super(id, docDescr, fileName, scannedFileName, docflowNum, docType, docflowUrl);
		this.applicationId = applicationId;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public List<Integer> getAppStatusesToAllowCertNumberAssign() {
		return appStatusesToAllowCertNumberAssign;
	}
	
}
