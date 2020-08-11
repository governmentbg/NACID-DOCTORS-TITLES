package com.nacid.data.nomenclatures;

import java.sql.Date;

public class TrainingLocationRecord extends FlatNomenclatureRecord {
  public TrainingLocationRecord() {
  }
  public TrainingLocationRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
