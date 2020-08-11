package com.nacid.db.events;

import javax.sql.DataSource;

import com.nacid.db.utils.DatabaseService;

public class EventTypeDB extends DatabaseService{

    public EventTypeDB(DataSource ds) {
        super(ds);
    }

}
