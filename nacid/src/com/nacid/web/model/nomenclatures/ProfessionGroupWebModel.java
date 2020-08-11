package com.nacid.web.model.nomenclatures;

import java.util.Date;

import com.nacid.bl.nomenclatures.ProfessionGroup;
import com.nacid.data.DataConverter;

public class ProfessionGroupWebModel {
  private String id = "";
  private String name = "";
  private String dateFrom = DataConverter.formatDate(new Date());
  private String dateTo = "дд.мм.гггг";
  
  public ProfessionGroupWebModel(int id, String name,  String dateFrom, String dateTo) {
    this.id = id + "";
    this.name = name;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }
  public ProfessionGroupWebModel(ProfessionGroup professionGroup) {
    this.id = professionGroup.getId() + "";
    this.name = professionGroup.getName();
    this.dateFrom = professionGroup.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(professionGroup.getDateFrom());
    this.dateTo = professionGroup.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(professionGroup.getDateTo());
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
