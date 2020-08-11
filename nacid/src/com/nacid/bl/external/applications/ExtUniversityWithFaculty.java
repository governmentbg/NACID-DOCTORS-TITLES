package com.nacid.bl.external.applications;

import com.nacid.bl.impl.applications.UniversityWithFaculty;

public class ExtUniversityWithFaculty extends UniversityWithFaculty {

    public ExtUniversityWithFaculty(ExtUniversity university, ExtUniversityFaculty faculty) {
        super(university, faculty);
    }

    @Override
    public ExtUniversity getUniversity() {
        return (ExtUniversity) university;
    }

    @Override
    public ExtUniversityFaculty getFaculty() {
        return (ExtUniversityFaculty) faculty;
    }
}
