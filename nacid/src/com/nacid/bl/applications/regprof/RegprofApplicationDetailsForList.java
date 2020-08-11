package com.nacid.bl.applications.regprof;

import java.util.Date;

//RayaWritten------------------------------------------
public interface RegprofApplicationDetailsForList {    
    public Integer getId();
    public void setId(Integer id);
    public String getAppNum();
    public void setAppNum(String appNum);
    public Date getAppDate();
    public void setAppDate(Date appDate);
    public Integer getStatus();
    public void setStatus(Integer status);
    public String getStatusName();
    public void setStatusName(String statusName);
    public String getFullName();
    public void setFullName(String fullName);
    public Integer getProfInstId();
    public void setProfInstId(Integer profInstId);
    public String getProfInstName();
    public void setProfInstName(String profInstName);
    public Integer getQualificationId();
    public void setQualificationId(Integer qualificationId);
    public String getQualificationName();
    public void setQualificationName(String qualificationName);
    public Integer getExtSignedDocId();
    public void setExtSignedDocId(Integer extSignedDocId);
    public Integer getSpecialityId();
    public void setSpecialityId(Integer specialityId);
    public String getSpecialityName();
    public void setSpecialityName(String specialityName);
    public Integer getServiceTypeId();
    public void setServiceTypeId(Integer serviceTypeId);
    public Date getEndDate();
    public void setEndDate(Date endDate);
    public String getImiCorrespondence();
    public void setImiCorrespondence(String imiCorrespondence);

    public int getDocflowStatusId();
    public void setDocflowStatusId(int docflowStatusId);

    public String getDocflowStatusName();
    public void setDocflowStatusName(String docflowStatusName);
    
}
//---------------------------------------------------