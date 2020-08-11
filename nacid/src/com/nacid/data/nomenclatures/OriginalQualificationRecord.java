package com.nacid.data.nomenclatures;

import java.sql.Date;

public class OriginalQualificationRecord extends FlatNomenclatureRecord {
  public OriginalQualificationRecord() {
  }
  public OriginalQualificationRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
