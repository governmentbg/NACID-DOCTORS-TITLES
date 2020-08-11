package com.nacid.data.regprof.external.applications;

import com.nacid.data.annotations.Table;

@Table(name="eservices.regprof_app_libilities")
public class ExtRegprofApplicationLiabilityRecord {
    private int id;
    private int extRegprofApplicationId;
    private int liabilityId;
    
    
    
    public ExtRegprofApplicationLiabilityRecord() {
    }
    public ExtRegprofApplicationLiabilityRecord(int id, int extRegprofApplicationId,
            int liabilityId) {
        this.id = id;
        this.extRegprofApplicationId = extRegprofApplicationId;
        this.liabilityId = liabilityId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getExtRegprofApplicationId() {
        return extRegprofApplicationId;
    }
    public void setExtRegprofApplicationId(int extRegprofApplicationId) {
        this.extRegprofApplicationId = extRegprofApplicationId;
    }
    public int getLiabilityId() {
        return liabilityId;
    }
    public void setLiabilityId(int liabilityId) {
        this.liabilityId = liabilityId;
    }
    
}
