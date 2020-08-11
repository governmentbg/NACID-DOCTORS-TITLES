package com.nacid.bl.nomenclatures.regprof;

import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.annotations.Table;

//RayaWritten------------------------------------
public interface RegprofArticleItem extends FlatNomenclature{
    public Integer getArticleDirectiveId();
    public boolean getIsActive();
    public String getQualificationLevelLabel();

}
//-----------------------------------------------------