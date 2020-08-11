package com.nacid.data.nomenclatures.regprof;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

import java.sql.Date;
//RayaWritten-------------------------------------------------------------
@Table(name="nomenclatures.article_directive")
public class RegprofArticleDirectiveRecord extends FlatNomenclatureRecord{

    public RegprofArticleDirectiveRecord() {
        super();
    }

    public RegprofArticleDirectiveRecord(int id, String name, Date dateFrom,
            Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
    
}
//---------------------------------------------------------------------------
