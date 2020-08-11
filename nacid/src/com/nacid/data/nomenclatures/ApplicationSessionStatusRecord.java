package com.nacid.data.nomenclatures;

import java.sql.Date;

public class ApplicationSessionStatusRecord extends FlatNomenclatureRecord {
  public ApplicationSessionStatusRecord() {
  }
  public ApplicationSessionStatusRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
