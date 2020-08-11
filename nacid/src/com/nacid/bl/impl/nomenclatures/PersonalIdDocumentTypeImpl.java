package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.AgeRange;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.PersonalIdDocumentTypeRecord;

public class PersonalIdDocumentTypeImpl extends FlatNomenclatureImpl implements AgeRange {

  public PersonalIdDocumentTypeImpl(PersonalIdDocumentTypeRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE;
  }
}
