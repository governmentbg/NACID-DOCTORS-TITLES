package com.nacid.bl.applications;

import java.util.Date;
import java.util.List;

public interface CompanyDataProvider {

    public List<Company> getCompanies(boolean onlyActive);
    public int saveCompany(int id, String name, int countryId, String city, 
            String pcode, String addressDetails, String phone,
            Date dateFrom, Date dateTo, String eik, Integer settlementId);
    public Company getCompany(int id);
    public void disableCompany(int id);
    public List<Company> getCompaniesByPartOfEik(String partOfEik);
    public List<Company> getCompaniesByEik(String eik);
    public List<Company> getCompaniesByPartOfName(String partOfName, boolean startsWith);
}
