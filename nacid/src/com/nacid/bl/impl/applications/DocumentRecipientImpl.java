package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.DocumentRecipient;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.DocumentRecipientRecord;

/**
 * User: ggeorgiev
 * Date: 4.10.2019 г.
 * Time: 14:28
 */
public class DocumentRecipientImpl extends DocumentRecipientBaseImpl implements DocumentRecipient {
    public DocumentRecipientImpl(DocumentRecipientRecord record, NomenclaturesDataProvider nomenclaturesDataProvider) {
        super(record, nomenclaturesDataProvider);
    }
}
