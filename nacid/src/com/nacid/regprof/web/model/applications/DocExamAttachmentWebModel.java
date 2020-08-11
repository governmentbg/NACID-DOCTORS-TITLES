package com.nacid.regprof.web.model.applications;
//RayaWritten-----------------------------------------------------------------------------
public class DocExamAttachmentWebModel extends RegprofAttachmentBaseWebModel{
    private int docExamId;
    private String applicationId = "";
    public DocExamAttachmentWebModel(int docExamId, String applicationId) {
        this.docExamId = docExamId;
        this.applicationId = applicationId;
    }
    
    public DocExamAttachmentWebModel(int id, int docExamId,
            String docDescr, String fileName, String applicationId, String scannedFileName,
            String docflowNum, String docflowUrl) {
        super(id, docDescr, fileName, scannedFileName, docflowNum, docflowUrl);
        this.docExamId = docExamId;
        this.applicationId = applicationId;
    }

    public int getDocExamId() {
        return docExamId;
    }
    public String getApplicationId() {
        return applicationId;
    }
}
//------------------------------------------------------------------------------
