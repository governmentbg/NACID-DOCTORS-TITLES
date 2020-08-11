package com.nacid.bl.impl.regprof.external.applications;

import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.external.applications.ExtApplicationAttachmentDataProviderImpl;
import com.nacid.bl.regprof.external.ExtRegprofApplicationAttachmentDataProvider;
import com.nacid.db.regprof.external.ExtRegprofApplicationAttachmentDB;

public class ExtRegprofApplicationAttachmentDataProviderImpl extends ExtApplicationAttachmentDataProviderImpl implements ExtRegprofApplicationAttachmentDataProvider {

	public ExtRegprofApplicationAttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		super(nacidDataProvider, new ExtRegprofApplicationAttachmentDB(nacidDataProvider.getDataSource()));
	}
}
