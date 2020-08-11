package com.nacid.web.model.nomenclatures;

import com.nacid.bl.nomenclatures.LegalReason;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;

/**
 * User: ggeorgiev
 * Date: 6.11.2019 г.
 * Time: 18:31
 */
public class LegalReasonWebModel extends FlatNomenclatureWebModel {
    private String ordinanceArticle;
    private String regulationArticle;
    private String regulationText;

    public LegalReasonWebModel(Integer id, String name, String ordinanceArticle, String regulationArticle, String regulationText, String dateFrom, String dateTo, String groupName) {
        super(id, name, dateFrom, dateTo, groupName, null, NomenclaturesDataProvider.NOMENCLATURE_LEGAL_REASON);
        this.ordinanceArticle = ordinanceArticle;
        this.regulationArticle = regulationArticle;
        this.regulationText = regulationText;
    }

    public LegalReasonWebModel(LegalReason flatNomenclature, String groupName) {
        super(flatNomenclature, groupName, null);
        this.ordinanceArticle = flatNomenclature.getOrdinanceArticle();
        this.regulationArticle = flatNomenclature.getRegulationArticle();
        this.regulationText = flatNomenclature.getRegulationText();
    }

    public LegalReasonWebModel(String groupName) {
        super(groupName, null, NomenclaturesDataProvider.NOMENCLATURE_LEGAL_REASON);
    }

    public String getOrdinanceArticle() {
        return ordinanceArticle;
    }

    public String getRegulationArticle() {
        return regulationArticle;
    }

    public String getRegulationText() {
        return regulationText;
    }
}
