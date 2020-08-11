package com.nacid.bl.nomenclatures.regprof;

import java.util.List;

import com.nacid.bl.impl.nomenclatures.regprof.RegprofArticleItemImpl;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;
//RayaWritten-----------------------------------------------------
public interface RegprofArticleDirective extends FlatNomenclature{
    public List<RegprofArticleItem> getArticleItems();
    public void setArticleItems(List<RegprofArticleItem> articleItems);
    public String getFormattedDateFrom();
    public String getFormattedDateTo();
}
//---------------------------------------------------------------
