package com.nacid.bl.regprof.external;

import java.util.Date;

public interface ExtRegprofApplicationForList {
    public int getId();
    public String getApplicantName();
    public Date getDate();
    public String getDocflowNumber();
    public int getStatusId();
    public Integer getInternalStatusId();
    public String getFinalStatusName();
    //public String getStatusName();
    public boolean isEsigned();
    boolean isCommunicated();
}
