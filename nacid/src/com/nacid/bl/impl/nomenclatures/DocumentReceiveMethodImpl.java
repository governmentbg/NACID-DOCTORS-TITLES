package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.DocumentReceiveMethod;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.DocumentReceiveMethodRecord;

public class DocumentReceiveMethodImpl extends FlatNomenclatureImpl implements DocumentReceiveMethod {
    private boolean hasDocumentRecipient;
    private boolean eservicesRequirePaymentReceipt;

    public DocumentReceiveMethodImpl(DocumentReceiveMethodRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.hasDocumentRecipient = record.getHasDocumentRecipient() == 1;
        this.eservicesRequirePaymentReceipt = record.getEservicesRequirePaymentReceipt() == 1;
    }

    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_DOCUMENT_RECEIVE_METHOD;
    }

    public boolean hasDocumentRecipient() {
        return hasDocumentRecipient;
    }

    @Override
    public boolean isEservicesRequirePaymentReceipt() {
        return eservicesRequirePaymentReceipt;
    }
}
