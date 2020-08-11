package com.nacid.data.nomenclatures;

import java.sql.Date;

public class QualificationRecord extends FlatNomenclatureRecord {
  public QualificationRecord() {
  }
  public QualificationRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
