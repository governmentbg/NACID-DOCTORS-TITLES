package com.nacid.bl.applications;

import java.util.Date;
import java.util.List;

public interface ApplicationForPublicRegister {
    public int getId();
    public String getAppNum();
    public Date getAppDate();
    public String getApplicantName();
    public String getUniversityName();
    public String getUniversityCountry();
    public String getRecognizedSpecialityName();
    public String getValidatedCertNumber();
    public List<String> getInvalidatedCertNumbers();
    public int getFinalStatusId();
}
