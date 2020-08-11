package com.nacid.data.nomenclatures;

import java.sql.Date;

public class BolognaCycleRecord extends FlatNomenclatureRecord {
  public BolognaCycleRecord() {
  }
  public BolognaCycleRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
