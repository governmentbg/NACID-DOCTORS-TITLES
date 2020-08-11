package com.nacid.bl.impl.nomenclatures.regprof;

import java.util.List;


import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleDirective;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleItem;
import com.nacid.data.DataConverter;
import com.nacid.data.nomenclatures.regprof.RegprofArticleDirectiveRecord;


//RayaWritten-----------------------------------------
public class RegprofArticleDirectiveImpl extends FlatNomenclatureImpl implements RegprofArticleDirective {

    private String formattedDateFrom;
    private String formattedDateTo;
    private List<RegprofArticleItem> articleItems;
    
    public RegprofArticleDirectiveImpl(RegprofArticleDirectiveRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    public RegprofArticleDirectiveImpl(RegprofArticleDirectiveRecord record, List<RegprofArticleItem> articleItems){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.articleItems = articleItems;
    }
   
    public List<RegprofArticleItem> getArticleItems() {
        return articleItems;
    }
    public void setArticleItems(List<RegprofArticleItem> articleItems) {
        this.articleItems = articleItems;
    }
    
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE;
    }  
    
    public String getFormattedDateFrom(){
        formattedDateFrom = DataConverter.formatDate(dateFrom);
        return formattedDateFrom;
    }
    public String getFormattedDateTo(){
        formattedDateTo = DataConverter.formatDate(dateTo);
        return formattedDateTo;
        
    }
    

}
//-------------------------------------------------------