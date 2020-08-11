package com.nacid.bl.impl.external.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtCompany;
import com.nacid.bl.impl.applications.CompanyImpl;
import com.nacid.data.external.applications.ExtCompanyRecord;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
public class ExtCompanyImpl extends CompanyImpl implements ExtCompany {
    public ExtCompanyImpl(NacidDataProvider nacidDataProvider, ExtCompanyRecord rec) {
        super(nacidDataProvider, rec);
    }
}
