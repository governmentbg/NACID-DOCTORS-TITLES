package com.nacid.data.regprof.external.applications;

import com.nacid.data.annotations.Table;
import com.nacid.data.external.applications.ExtApplicationCommentRecord;

import java.sql.Timestamp;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 16:17
 */
@Table(name = "eservices.regprof_application_comments", sequence = "eservices.regprof_application_comments_id_seq")
public class ExtRegprofApplicationCommentRecord extends ExtApplicationCommentRecord {
    public ExtRegprofApplicationCommentRecord(int id, int applicationId, String comment, int sendEmail, Integer emailId, int systemMessage, Timestamp dateCreated, int userCreated) {
        super(id, applicationId, comment, sendEmail, emailId, systemMessage, dateCreated, userCreated);
    }

    public ExtRegprofApplicationCommentRecord() {
    }
}
