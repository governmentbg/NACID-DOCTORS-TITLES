package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.ExpertPosition;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.nomenclatures.ExpertPositionRecord;

public class ExpertPositionImpl extends FlatNomenclatureImpl implements ExpertPosition {
    private Integer relatedAppStatusId;
    private NacidDataProvider nacidDataProvider;
    public ExpertPositionImpl(NacidDataProvider nacidDataProvider, ExpertPositionRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.relatedAppStatusId = record.getRelatedAppStatusId();
        this.nacidDataProvider = nacidDataProvider;
    }

    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_EXPERT_POSITION;
    }

    public Integer getRelatedAppStatusId() {
        return relatedAppStatusId;
    }

    @Override
    public FlatNomenclature getRelatedAppStatus() {
        return getRelatedAppStatusId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, getRelatedAppStatusId());
    }
}
