package com.nacid.data.nomenclatures;

import java.sql.Date;

public class DocCategoryRecord extends FlatNomenclatureRecord {
  public DocCategoryRecord(){
  }
  public DocCategoryRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
  
}
