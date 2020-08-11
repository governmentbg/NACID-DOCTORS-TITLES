package com.nacid.bl.nomenclatures;

/**
 * User: ggeorgiev
 * Date: 4.10.2019 г.
 * Time: 15:04
 */
public interface DocumentReceiveMethod extends FlatNomenclature {
    boolean hasDocumentRecipient();
    boolean isEservicesRequirePaymentReceipt();

}
