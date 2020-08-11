package com.nacid.bl.regprof;

import java.util.Date;
//RayaWritten-------------------------------------
public interface ProfessionalInstitution {

    public Integer getId();
    public void setId(Integer id);
    public Integer getCountryId();
    public void setCountryId(Integer countryId);
    public String getBgName();
    public void setBgName(String bgName);
    public String getCity();
    public void setCity(String city);
    public String getAddrDetails();
    public void setAddrDetails(String addrDetails);
    public String getPhone();
    public void setPhone(String phone);
    public String getFax();
    public void setFax(String fax);
    public String getEmail();
    public void setEmail(String email);
    public String getWebSite();
    public void setWebSite(String webSite);
    public Date getDateFrom();
    public void setDateFrom(Date dateFrom);
    public Date getDateTo();
    public void setDateTo(Date dateTo);
    public String getUrlDiplomaRegister();
    public void setUrlDiplomaRegister(String urlDiplomaRegister);
    public Integer getProfessionalInstitutionTypeId();
    public void setProfessionalInstitutionTypeId(Integer professionalInstitutionTypeId);
}
//-----------------------------------------------------