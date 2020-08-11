package com.nacid.web.model.nomenclatures.regprof;

import java.util.Date;

import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.data.DataConverter;

//RayaWritten---------------------------------------------
public class SecondaryProfessionalQualificationWebModel {
    private String id = "";
    private String name = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private String code = "";
    
    public SecondaryProfessionalQualificationWebModel(int id, String name,  String dateFrom, String dateTo, String code) {
        this.id = id + "";
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.code = code;
      }
      public SecondaryProfessionalQualificationWebModel(SecondaryProfessionalQualification profQualification) {
        this.id = profQualification.getId() + "";
        this.name = profQualification.getName();
        this.dateFrom = profQualification.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(profQualification.getDateFrom());
        this.dateTo = profQualification.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(profQualification.getDateTo());
        this.code = profQualification.getCode() == null ? "" : profQualification.getCode();
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
