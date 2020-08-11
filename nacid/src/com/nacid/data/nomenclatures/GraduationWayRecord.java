package com.nacid.data.nomenclatures;

import java.sql.Date;

public class GraduationWayRecord extends FlatNomenclatureRecord {
  public GraduationWayRecord() {
  }
  public GraduationWayRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
