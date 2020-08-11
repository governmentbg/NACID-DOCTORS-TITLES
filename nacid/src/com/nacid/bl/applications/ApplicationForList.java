package com.nacid.bl.applications;

import java.util.Date;
import java.util.List;

public interface ApplicationForList {
    public int getId();
    public String getAppNum();
    public Date getAppDate();
    public String getAptName();
    public String getUniName();
    public String getUniCountryName();
    public String getSpecialityName();
    public String getApnStatusName();
    public Integer getApnStatusId();
    public String getDocflowStatusName();
    public Integer getDocflowStatusId();
    public Boolean isExternalApplicationEsigned();
    public List<Integer> getEntryNumSeries();
    public List<Integer> getCommissionSessions();
    public int getExpertsProcessedStatus();
    public int getExpertsCount();
    public String getEduLevelName();
    public String getRecognizedProfGroupName();
    public String getRecognizedQualificationName();
}
