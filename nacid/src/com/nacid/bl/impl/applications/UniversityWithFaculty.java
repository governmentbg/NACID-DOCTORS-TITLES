package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityFaculty;

public class UniversityWithFaculty {
    protected University university;
    protected UniversityFaculty faculty;

    public UniversityWithFaculty(University university, UniversityFaculty faculty) {
        this.university = university;
        this.faculty = faculty;
    }

    public University getUniversity() {
        return university;
    }

    public UniversityFaculty getFaculty() {
        return faculty;
    }
}
