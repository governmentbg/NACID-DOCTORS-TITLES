package com.nacid.web.model.nomenclatures.regprof;

import java.util.Date;

import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionGroup;
import com.nacid.data.DataConverter;

//RayaWritten---------------------------------------------
public class SecondaryProfessionGroupWebModel {
    private String id = "";
    private String name = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private String code = "";
    
    public SecondaryProfessionGroupWebModel(int id, String name,  String dateFrom, String dateTo, String code) {
        this.id = id + "";
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.code = code;
      }
      public SecondaryProfessionGroupWebModel(SecondaryProfessionGroup group) {
        this.id = group.getId() + "";
        this.name = group.getName();
        this.dateFrom = group.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(group.getDateFrom());
        this.dateTo = group.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(group.getDateTo());
        this.code = group.getCode() == null ? "" : group.getCode();
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
    public String getCode() {
        return code;
    }
      
}
//----------------------------------------------------------
