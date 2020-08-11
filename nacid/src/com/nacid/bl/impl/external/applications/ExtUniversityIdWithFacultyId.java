package com.nacid.bl.impl.external.applications;

import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;

/**
 * User: ggeorgiev
 * Date: 13.3.2019 г.
 * Time: 17:20
 */
public class ExtUniversityIdWithFacultyId extends UniversityIdWithFacultyId {
    private String universityTxt;
    private String facultyTxt;
    public ExtUniversityIdWithFacultyId(Integer universityId, String universityTxt, Integer facultyId, String facultyTxt) {
        super(universityId, facultyId);
        this.universityTxt = universityTxt;
        this.facultyTxt = facultyTxt;
    }

    public String getUniversityTxt() {
        return universityTxt;
    }

    public String getFacultyTxt() {
        return facultyTxt;
    }
}
