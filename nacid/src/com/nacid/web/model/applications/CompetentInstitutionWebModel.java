package com.nacid.web.model.applications;

import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.data.DataConverter;

public class CompetentInstitutionWebModel {
  private String id;
  private String name;
  private String originalName;
  private String phone;
  private String fax;
  private String email;
  private String addressDetails;
  private String url;
  private String dateFrom;
  private String dateTo;
  public CompetentInstitutionWebModel(CompetentInstitution institution) {
    this.id = institution.getId() + "";
    this.name = institution.getName();
    this.originalName = institution.getOriginalName();
    this.phone = institution.getPhone();
    this.fax = institution.getFax();
    this.email = institution.getEmail();
    this.addressDetails = institution.getAddressDetails();
    this.url = institution.getUrl();
    this.dateFrom = DataConverter.formatDate(institution.getDateFrom(), "дд.мм.гггг");
    this.dateTo = DataConverter.formatDate(institution.getDateTo(), "дд.мм.гггг");
  }
  public String getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public String getPhone() {
    return phone;
  }
  public String getFax() {
    return fax;
  }
  public String getEmail() {
    return email;
  }
  public String getAddressDetails() {
    return addressDetails;
  }
  public String getUrl() {
    return url;
  }
  public String getDateFrom() {
    return dateFrom;
  }
  public String getDateTo() {
    return dateTo;
  }
  public String getOriginalName() {
    return originalName;
  }
  
}
