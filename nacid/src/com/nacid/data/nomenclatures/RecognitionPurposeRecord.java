package com.nacid.data.nomenclatures;

import java.sql.Date;

public class RecognitionPurposeRecord extends FlatNomenclatureRecord {
  public RecognitionPurposeRecord() {
  }
  public RecognitionPurposeRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
