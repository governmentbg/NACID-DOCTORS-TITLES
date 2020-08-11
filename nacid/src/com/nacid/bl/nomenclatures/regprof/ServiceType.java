package com.nacid.bl.nomenclatures.regprof;

import java.math.BigDecimal;

import com.nacid.bl.nomenclatures.FlatNomenclature;
//RayaWritten----------------------------------------------
public interface ServiceType extends FlatNomenclature {
    public static final int SERVICE_TYPE_QUICK = 1;
    public static final int SERVICE_TYPE_STANDARD = 2;
    public Integer getExecutionDays();
    public BigDecimal getServicePrice(); 
}
//-------------------------------------------------