package com.nacid.bl.applications;

import java.util.Date;

public interface UniversityFaculty {
    public int getId();
    public int getUniversityId();
    public String getName();
    public String getOriginalName();
    public Date getDateFrom();
    public Date getDateTo();
    public boolean isActive();
}
