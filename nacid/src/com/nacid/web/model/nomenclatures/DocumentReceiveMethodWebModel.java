package com.nacid.web.model.nomenclatures;

import com.nacid.bl.nomenclatures.DocumentReceiveMethod;
import com.nacid.bl.nomenclatures.FlatNomenclature;

/**
 * User: ggeorgiev
 * Date: 4.10.2019 г.
 * Time: 15:33
 */
public class DocumentReceiveMethodWebModel extends FlatNomenclatureWebModel {
    private boolean hasDocumentRecipient;
    private boolean eservicesRequirePaymentReceipt;
    public DocumentReceiveMethodWebModel(Integer id, String name, String dateFrom, String dateTo, String groupName, String nomenclatureName, int nomenclatureType, boolean hasDocumentRecipient, boolean eservicesRequirePaymentReceipt) {
        super(id, name, dateFrom, dateTo, groupName, nomenclatureName, nomenclatureType);
        this.hasDocumentRecipient = hasDocumentRecipient;
        this.eservicesRequirePaymentReceipt = eservicesRequirePaymentReceipt;
    }

    public DocumentReceiveMethodWebModel(DocumentReceiveMethod flatNomenclature, String groupName, String nomenclatureName) {
        super(flatNomenclature, groupName, nomenclatureName);
        this.hasDocumentRecipient = flatNomenclature.hasDocumentRecipient();
        this.eservicesRequirePaymentReceipt = flatNomenclature.isEservicesRequirePaymentReceipt();
    }

    public DocumentReceiveMethodWebModel(String groupName, String nomenclatureName, int nomenclatureType) {
        super(groupName, nomenclatureName, nomenclatureType);
    }

    public boolean isHasDocumentRecipient() {
        return hasDocumentRecipient;
    }

    public boolean isEservicesRequirePaymentReceipt() {
        return eservicesRequirePaymentReceipt;
    }
}
