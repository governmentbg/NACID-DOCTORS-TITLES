package com.nacid.data.nomenclatures;

import java.sql.Date;

public class GraduationDocumentTypeRecord extends FlatNomenclatureRecord {
  public GraduationDocumentTypeRecord() {
  }
  public GraduationDocumentTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
