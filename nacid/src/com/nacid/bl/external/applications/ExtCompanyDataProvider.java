package com.nacid.bl.external.applications;

import com.nacid.bl.external.ExtCompany;

import java.util.Date;
import java.util.List;

public interface ExtCompanyDataProvider {

    public List<ExtCompany> getCompanies(boolean onlyActive);
    public int saveCompany(int id, String name, int countryId, String city,
                           String pcode, String addressDetails, String phone,
                           Date dateFrom, Date dateTo, String eik, Integer settlementId);
    public ExtCompany getCompany(int id);
    public void disableCompany(int id);
    public List<ExtCompany> getCompaniesByEik(String eik);
}
