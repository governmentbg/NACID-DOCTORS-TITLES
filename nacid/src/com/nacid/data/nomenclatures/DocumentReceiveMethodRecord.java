package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 14.08.2019
 */
public class DocumentReceiveMethodRecord extends FlatNomenclatureRecord {
    private int hasDocumentRecipient;
    private int eservicesRequirePaymentReceipt;
    public DocumentReceiveMethodRecord() {
    }

    public DocumentReceiveMethodRecord(int id, String name, int hasDocumentRecipient, int eservicesRequirePaymentReceipt, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
        this.hasDocumentRecipient = hasDocumentRecipient;
        this.eservicesRequirePaymentReceipt = eservicesRequirePaymentReceipt;
    }

    public int getHasDocumentRecipient() {
        return hasDocumentRecipient;
    }

    public void setHasDocumentRecipient(int hasDocumentRecipient) {
        this.hasDocumentRecipient = hasDocumentRecipient;
    }

    public int getEservicesRequirePaymentReceipt() {
        return eservicesRequirePaymentReceipt;
    }

    public void setEservicesRequirePaymentReceipt(int eservicesRequirePaymentReceipt) {
        this.eservicesRequirePaymentReceipt = eservicesRequirePaymentReceipt;
    }
}
