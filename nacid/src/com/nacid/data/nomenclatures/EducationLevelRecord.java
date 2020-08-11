package com.nacid.data.nomenclatures;

import java.sql.Date;

public class EducationLevelRecord extends FlatNomenclatureRecord{
  public EducationLevelRecord(){
  }
  public EducationLevelRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
