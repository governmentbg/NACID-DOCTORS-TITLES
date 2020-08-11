package com.nacid.data.applications;

import java.sql.Date;

public class CompetentInstitutionRecord {
  private int id;
  private int countryId;
  private String name;
  private String originalName;
  private String phone;
  private String fax;
  private String email;
  private String addressDetails;
  private String url;
  private String notes;
  private Date dateFrom;
  private Date dateTo;
  public CompetentInstitutionRecord() {
  }
  public CompetentInstitutionRecord(int id, int countryId, String name, String originalName, String phone, String fax, String email,
      String addressDetails, String url, String notes, Date dateFrom, Date dateTo) {
    this.id = id;
    this.countryId = countryId;
    this.name = name;
    this.originalName = originalName;
    this.phone = phone;
    this.fax = fax;
    this.email = email;
    this.addressDetails = addressDetails;
    this.url = url;
    this.notes = notes;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getCountryId() {
    return countryId;
  }
  public void setCountryId(int countryId) {
    this.countryId = countryId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getOriginalName() {
    return originalName;
  }
  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getFax() {
    return fax;
  }
  public void setFax(String fax) {
    this.fax = fax;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getAddressDetails() {
    return addressDetails;
  }
  public void setAddressDetails(String addressDetails) {
    this.addressDetails = addressDetails;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public Date getDateFrom() {
    return dateFrom;
  }
  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }
  public Date getDateTo() {
    return dateTo;
  }
  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }
}
