package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.DocCategoryRecord;

public class DocCategoryImpl extends FlatNomenclatureImpl implements DocCategory {

  public DocCategoryImpl(DocCategoryRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY;
  }
}
