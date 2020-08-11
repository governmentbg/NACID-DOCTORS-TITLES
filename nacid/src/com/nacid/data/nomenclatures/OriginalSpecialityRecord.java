package com.nacid.data.nomenclatures;

import java.sql.Date;

public class OriginalSpecialityRecord extends FlatNomenclatureRecord {
  public OriginalSpecialityRecord() {
  }
  public OriginalSpecialityRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
