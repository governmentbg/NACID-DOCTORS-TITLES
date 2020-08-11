package com.nacid.data.nomenclatures;

import java.sql.Date;

public class SessionStatusRecord extends FlatNomenclatureRecord {
  public SessionStatusRecord() {
  }
  public SessionStatusRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
