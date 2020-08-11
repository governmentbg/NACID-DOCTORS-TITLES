package com.nacid.bl.nomenclatures.regprof;

import java.util.Date;

import com.nacid.bl.nomenclatures.FlatNomenclature;

public interface ProfessionExperienceDocumentType extends FlatNomenclature {
    
    public static final int DOC_TYPE_TRUDOVA_KNIJKA = 1;
    public static final int DOC_TYPE_UP_3 = 2;
    public static final int DOC_TYPE_OSIGURITELNA_KNIJKA = 3;
    public static final int DOC_TYPE_SLUJEBNA_KNIJKA = 4;
    
    public int getId();
    public String getName();
    public Date getDateFrom();
    public Date getDateTo();
    public boolean isForExperienceCalculation();
}
