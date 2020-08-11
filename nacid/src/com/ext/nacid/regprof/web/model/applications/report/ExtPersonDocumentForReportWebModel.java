package com.ext.nacid.regprof.web.model.applications.report;

import com.nacid.bl.external.ExtPersonDocument;
import com.nacid.data.external.applications.ExtPersonDocumentRecord;
import com.nacid.regprof.web.model.applications.report.base.PersonDocumentForReportBaseWebModel;

//RayaWritten----------------------------------------------
public class ExtPersonDocumentForReportWebModel extends PersonDocumentForReportBaseWebModel{

    public ExtPersonDocumentForReportWebModel(ExtPersonDocument document) {
        super(document);
    }
    public ExtPersonDocumentForReportWebModel(ExtPersonDocumentRecord personDoc) {
        super(personDoc);
    }

}
//-----------------------------------------------------------
