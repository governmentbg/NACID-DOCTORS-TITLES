package com.nacid.data.nomenclatures;

import java.sql.Date;

public class CopyTypeRecord extends FlatNomenclatureRecord{
  public CopyTypeRecord(){
  }
  public CopyTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
    super(id, name, dateFrom, dateTo);
  }
}
