package com.nacid.data.nomenclatures.regprof;

import java.math.BigDecimal;
import java.sql.Date;

import com.nacid.data.nomenclatures.FlatNomenclatureRecord;
//RayaWritten----------------------------------------------------
public class ServiceTypeRecord extends FlatNomenclatureRecord{
    private Integer executionDays;
    private BigDecimal servicePrice;
    public ServiceTypeRecord(){
        
    }
    public ServiceTypeRecord(int id, String name, Integer executionDays, Date dateFrom, Date dateTo, BigDecimal servicePrice) {
        super(id, name, dateFrom, dateTo);
        this.executionDays = executionDays;
        this.servicePrice = servicePrice;
    }
    public Integer getExecutionDays() {
        return executionDays;
    }
    public void setExecutionDays(Integer executionDays) {
        this.executionDays = executionDays;
    }
    public BigDecimal getServicePrice() {
        return servicePrice;
    }
    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }
    
    
    
    
    
}
//-------------------------------------------------------------------
