package com.nacid.bl.external.applications;

import com.nacid.bl.applications.UniversityFaculty;

/**
 * User: ggeorgiev
 * Date: 13.3.2019 г.
 * Time: 16:37
 */
public interface ExtUniversityFaculty extends UniversityFaculty {

    public boolean isStandardFaculty();

    public String getFacultyTxt();
}
