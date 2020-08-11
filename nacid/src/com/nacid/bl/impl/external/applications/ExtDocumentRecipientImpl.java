package com.nacid.bl.impl.external.applications;

import com.nacid.bl.external.ExtDocumentRecipient;
import com.nacid.bl.impl.applications.DocumentRecipientImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.external.applications.ExtDocumentRecipientRecord;

/**
 * User: ggeorgiev
 * Date: 4.10.2019 г.
 * Time: 14:57
 */
public class ExtDocumentRecipientImpl extends DocumentRecipientImpl implements ExtDocumentRecipient {
    public ExtDocumentRecipientImpl(ExtDocumentRecipientRecord record, NomenclaturesDataProvider nomenclaturesDataProvider) {
        super(record, nomenclaturesDataProvider);
    }
}
