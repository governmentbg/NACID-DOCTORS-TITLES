
package com.nacid.bl.applications.regprof;

import java.util.Date;

//RayaWritten--------------------------------
public interface RegprofAppStatusHistory {
    
    public Integer getId();
    public void setId(Integer id);
    public Integer getApplicationId();
    public void setApplicationId(Integer applicationId);
    public Integer getStatusId();
    public void setStatusId(Integer statusId);
    public Date getDateAssigned();
    public void setDateAssigned(Date dateAssigned);
    public Integer getUserAssigned();
    public void setUserAssigned(Integer userAssigned);
}
//------------------------------------------