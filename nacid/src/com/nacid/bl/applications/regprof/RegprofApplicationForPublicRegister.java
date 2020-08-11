package com.nacid.bl.applications.regprof;

import java.util.Date;
import java.util.List;

public interface RegprofApplicationForPublicRegister {
    public int getId();
    public String getAppNum();
    public Date getAppDate();
    public String getApplicantName();
    public String getApplicationCountryName();
    public String getValidatedCertNumber();
    public List<String> getInvalidatedCertNumbers();
    public int getFinalStatusId();
}
