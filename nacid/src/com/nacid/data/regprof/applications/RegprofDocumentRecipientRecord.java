package com.nacid.data.regprof.applications;

import com.nacid.data.annotations.Table;
import com.nacid.data.applications.DocumentRecipientRecord;

/**
 * User: ggeorgiev
 * Date: 8.10.2019 г.
 * Time: 18:25
 */
@Table(name = "regprof.regprof_document_recipient", sequence = "regprof.regprof_document_recipient_id_seq")
public class RegprofDocumentRecipientRecord extends DocumentRecipientRecord {
}
