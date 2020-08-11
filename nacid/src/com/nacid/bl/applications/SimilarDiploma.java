package com.nacid.bl.applications;

import java.util.Date;
import java.util.List;

/**
 * Created by georgi.georgiev on 14.10.2015.
 */
public interface SimilarDiploma {
    public int getId();
    public Date getDiplomaDate();
    public Integer getEduLevelId();
    public String getEduLevelName();
    public String getAppNum();
    public Date getAppDate();
    public int getCivilIdType();
    public String getCivilId();
    public String getFname();
    public String getSname();
    public String getLname();
    public List<String> getSpecialityNames();
    public List<String> getUniversityNames();
    public List<Integer> getUniversityCountryIds();
    public List<String> getUniversityCountryNames();
}
