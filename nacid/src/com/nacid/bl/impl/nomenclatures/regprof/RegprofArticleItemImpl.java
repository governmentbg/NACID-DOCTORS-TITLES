package com.nacid.bl.impl.nomenclatures.regprof;

import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleItem;
import com.nacid.data.DataConverter;
import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.regprof.RegprofArticleItemRecord;

//RayaWritten---------------------------------
public class RegprofArticleItemImpl extends FlatNomenclatureImpl implements RegprofArticleItem {
    
    private Integer articleDirectiveId;
    private String qualificationLevelLabel;

    
    public RegprofArticleItemImpl(RegprofArticleItemRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.articleDirectiveId = record.getArticleDirectiveId();
        this.qualificationLevelLabel = record.getQualificationLevelLabel();
        
    }
       
    public Integer getArticleDirectiveId() {
        return articleDirectiveId;
    }
    
    public boolean getIsActive(){
        boolean dateToToday = false;
        if(dateTo != null){
            dateToToday = dateTo.getTime() != new Date().getTime();
        }
        return isActive() && !dateToToday;
    }
    
    public String getDateFromFormatted(){
        return DataConverter.formatDate(dateFrom);
    }
    
    public String getDateToFormatted(){
        return DataConverter.formatDate(dateTo);
    }
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_REGPROF_ARTICLE_ITEM;
    }

    @Override
    public String getQualificationLevelLabel() {
        return qualificationLevelLabel;
    }

}
//------------------------------------------------
