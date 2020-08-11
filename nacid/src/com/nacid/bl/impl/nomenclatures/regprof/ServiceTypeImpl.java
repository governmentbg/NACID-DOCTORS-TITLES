package com.nacid.bl.impl.nomenclatures.regprof;

import java.math.BigDecimal;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
import com.nacid.data.DataConverter;
import com.nacid.data.nomenclatures.regprof.RegprofArticleItemRecord;
import com.nacid.data.nomenclatures.regprof.ServiceTypeRecord;
//RayaWritten----------------------------------------------------------------
public class ServiceTypeImpl extends FlatNomenclatureImpl implements
        ServiceType {
    
    private Integer executionDays;
    private BigDecimal servicePrice;
    public ServiceTypeImpl(ServiceTypeRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.executionDays = record.getExecutionDays();
        this.servicePrice = record.getServicePrice();
        
    }
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_SERVICE_TYPE;
    }
    public Integer getExecutionDays() {
        return executionDays;
    }
    
    public String getFormattedDateFrom(){
        return DataConverter.formatDate(dateFrom);
    }
    
    public String getFormattedDateTo(){
        return DataConverter.formatDate(dateTo);
    }
    public BigDecimal getServicePrice() {
        return servicePrice;
    }
    
    
}
//-------------------------------------------------------------------------