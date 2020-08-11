package com.nacid.bl.nomenclatures.regprof;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.nacid.bl.nomenclatures.FlatNomenclature;


public interface EducationType extends FlatNomenclature {
    //RayaWritten---------------------------------------------
    public static final int EDU_TYPE_HIGH = 1;
    public static final int EDU_TYPE_SECONDARY = 2;
    public static final int EDU_TYPE_SDK = 3;
    public static final int EDU_TYPE_SECONDARY_PROFESSIONAL = 4;
    
    //-----------------------------------------------------------
    
    public static final Set<Integer> HIGHER_EDUCATION_TYPES = new HashSet<Integer>(Arrays.asList(EDU_TYPE_HIGH, EDU_TYPE_SDK));
    public static final Set<Integer> SECONDARY_EDUCATION_TYPES = new HashSet<Integer>(Arrays.asList(EDU_TYPE_SECONDARY_PROFESSIONAL, EDU_TYPE_SECONDARY));
    
}
