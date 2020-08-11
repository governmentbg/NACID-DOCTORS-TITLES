package com.nacid.bl.nomenclatures;


public interface ExpertPosition extends FlatNomenclature {
    public Integer getRelatedAppStatusId();
    public FlatNomenclature getRelatedAppStatus();
}
