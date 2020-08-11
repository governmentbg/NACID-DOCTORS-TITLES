package com.nacid.db.regprof.external;

import com.nacid.db.external.applications.ExtApplicationAttachmentDB;

import javax.sql.DataSource;

public class ExtRegprofApplicationAttachmentDB extends ExtApplicationAttachmentDB {
    public ExtRegprofApplicationAttachmentDB(DataSource ds) {
        super(ds, "eservices.regprof_attached_docs", "regprof.regprof_attached_docs");
    }
}
