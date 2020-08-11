package com.nacid.data.applications;

public class DiplomaIssuerRecord {
  private int id;
  private int diplomaId;
  private int universityId;
  private Integer facultyId;
  private int orderNumber;

  
  public DiplomaIssuerRecord() {
  }
  public DiplomaIssuerRecord(int id, int diplomaId, int universityId, Integer facultyId, int orderNumber) {
    this.id = id;
    this.diplomaId = diplomaId;
    this.universityId = universityId;
    this.orderNumber = orderNumber;
    this.facultyId = facultyId;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getDiplomaId() {
    return diplomaId;
  }
  public int getUniversityId() {
    return universityId;
  }
  public int getOrderNumber() {
    return orderNumber;
  }
  public void setDiplomaId(int diplomaId) {
    this.diplomaId = diplomaId;
  }
  public void setUniversityId(int universityId) {
    this.universityId = universityId;
  }
  public void setOrderNumber(int orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Integer getFacultyId() {
    return facultyId;
  }

  public void setFacultyId(Integer facultyId) {
    this.facultyId = facultyId;
  }
}
