package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

public class EducationTypeRecord extends FlatNomenclatureRecord {
  public EducationTypeRecord(){
  }
  public EducationTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
  
}