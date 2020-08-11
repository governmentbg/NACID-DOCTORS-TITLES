package com.nacid.data.nomenclatures;

import java.sql.Date;

public class EducationAreaRecord extends FlatNomenclatureRecord {
  public EducationAreaRecord(){
  }
  public EducationAreaRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
  
}
