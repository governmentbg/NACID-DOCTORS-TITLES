package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;
//RayaWritten--------------------------------------------------------------
public class RegprofArticleItemRecord extends FlatNomenclatureRecord {
    private Integer articleDirectiveId;
    private String qualificationLevelLabel;
    public RegprofArticleItemRecord(){
        
    }
    public RegprofArticleItemRecord(int id, String name, Integer articleDirectiveId, String qualificationLevelLabel, Date dateFrom, Date dateTo){
        super(id, name, dateFrom, dateTo);
        this.articleDirectiveId = articleDirectiveId;
        this.qualificationLevelLabel = qualificationLevelLabel;
    }
    public Integer getArticleDirectiveId() {
        return articleDirectiveId;
    }
    public void setArticleDirectiveId(Integer articleDirectiveId) {
        this.articleDirectiveId = articleDirectiveId;
    }

    public String getQualificationLevelLabel() {
        return qualificationLevelLabel;
    }

    public void setQualificationLevelLabel(String qualificationLevelLabel) {
        this.qualificationLevelLabel = qualificationLevelLabel;
    }
}
//--------------------------------------------------------------------------
