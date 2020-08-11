package com.nacid.data.regprof.external.applications;

import com.nacid.data.external.applications.ExtESignedInformationRecord;

public class ExtRegprofESignedInformationRecord extends ExtESignedInformationRecord {

    public ExtRegprofESignedInformationRecord() {
        super();
    }

    public ExtRegprofESignedInformationRecord(int id, int userId,
            int applicationId, String signedXmlContent) {
        super(id, userId, applicationId, signedXmlContent);
    }

}
