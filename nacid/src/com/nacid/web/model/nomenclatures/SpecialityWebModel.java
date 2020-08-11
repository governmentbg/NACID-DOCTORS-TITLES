package com.nacid.web.model.nomenclatures;

import java.util.Date;

import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.DataConverter;

public class SpecialityWebModel {
  private String id = "";
  private String name = "";
  private String dateFrom = DataConverter.formatDate(new Date());
  private String dateTo = "дд.мм.гггг";
  
  public SpecialityWebModel(int id, String name,  String dateFrom, String dateTo) {
    this.id = id + "";
    this.name = name;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }
  public SpecialityWebModel(Speciality speciality) {
    this.id = speciality.getId() + "";
    this.name = speciality.getName();
    this.dateFrom = speciality.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(speciality.getDateFrom());
    this.dateTo = speciality.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(speciality.getDateTo());
  }
  public String getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public String getDateFrom() {
    return dateFrom;
  }
  public String getDateTo() {
    return dateTo;
  }
  
  
  
}
