package com.nacid.data.nomenclatures;

import java.sql.Date;

public class CommissionPositionRecord extends FlatNomenclatureRecord {
  public CommissionPositionRecord() {
  }
  public CommissionPositionRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
