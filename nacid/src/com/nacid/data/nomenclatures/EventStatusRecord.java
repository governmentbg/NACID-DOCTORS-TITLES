package com.nacid.data.nomenclatures;

import java.sql.Date;

public class EventStatusRecord extends FlatNomenclatureRecord {

    public EventStatusRecord(){
    }
    public EventStatusRecord(int id, String name, Date dateFrom, Date dateTo) {
      super(id, name, dateFrom, dateTo);
    }
}
