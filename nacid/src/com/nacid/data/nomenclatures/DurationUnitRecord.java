package com.nacid.data.nomenclatures;

import java.sql.Date;

public class DurationUnitRecord extends FlatNomenclatureRecord {
  public DurationUnitRecord() {
  }
  public DurationUnitRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
