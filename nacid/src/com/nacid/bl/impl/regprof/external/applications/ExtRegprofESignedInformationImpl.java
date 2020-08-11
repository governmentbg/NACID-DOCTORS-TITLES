package com.nacid.bl.impl.regprof.external.applications;

import com.nacid.bl.impl.external.applications.ExtESignedInformationImpl;
import com.nacid.bl.regprof.external.ExtRegprofESignedInformation;
import com.nacid.data.regprof.external.applications.ExtRegprofESignedInformationRecord;

public class ExtRegprofESignedInformationImpl extends ExtESignedInformationImpl implements ExtRegprofESignedInformation {

    public ExtRegprofESignedInformationImpl(ExtRegprofESignedInformationRecord record) {
        super(record);
    }

}
