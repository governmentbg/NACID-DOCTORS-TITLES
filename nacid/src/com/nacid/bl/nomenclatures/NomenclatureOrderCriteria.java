package com.nacid.bl.nomenclatures;

import com.nacid.bl.OrderCriteria;
import com.nacid.data.nomenclatures.*;
import com.nacid.data.nomenclatures.regprof.*;

public class NomenclatureOrderCriteria extends OrderCriteria{
    public static final String ORDER_COLUMN_ID = "id";
    public static final String ORDER_COLUMN_NAME = "name";
    public static final String ORDER_COLUMN_NONE = null;
    public NomenclatureOrderCriteria(String orderColumn, boolean ascending) {
        super(orderColumn, ascending);
    }
    
    public static NomenclatureOrderCriteria getDefaultOrderCriteria(Class<? extends FlatNomenclatureRecord> cls) {
        
        if (cls.equals(EducationAreaRecord.class)
                //RayaWritten------------------------
                ||cls.equals(SecondaryProfessionalQualificationRecord.class)
                //||cls.equals(SecondarySpecialityRecord.class)
                //---------------------------------------------------------
                || cls.equals(QualificationRecord.class) 
                || cls.equals(CommissionPositionRecord.class)
                || cls.equals(ProfessionGroupRecord.class)
                || cls.equals(SpecialityRecord.class)
                || cls.equals(ProfessionGroupRecord.class)
                || cls.equals(CountryRecord.class)
                || cls.equals(LanguageRecord.class)
                || cls.equals(LegalReasonRecord.class)
                || cls.equals(PersonalIdDocumentTypeRecord.class)
                || cls.equals(SecondarySpecialityRecord.class)) {
            return new NomenclatureOrderCriteria(ORDER_COLUMN_NAME, true);
        } else {
            return new NomenclatureOrderCriteria(ORDER_COLUMN_ID, true);
        }
    }
    public String getOrderSqlString() {
        if (getOrderColumn() != null) {
            return " ORDER BY " + getOrderColumn() + (isAscending() ? " ASC " : " DESC ");
        }
        return "";
    }
}
