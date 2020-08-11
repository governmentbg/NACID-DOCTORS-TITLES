package com.nacid.data.nomenclatures;

import java.sql.Date;

public class TrainingFormRecord extends FlatNomenclatureRecord {
  public TrainingFormRecord() {
  }
  public TrainingFormRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
