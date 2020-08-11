package com.nacid.data.nomenclatures;

import java.sql.Date;

public class CivilIdTypeRecord extends FlatNomenclatureRecord {
  public CivilIdTypeRecord() {
  }
  public CivilIdTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
