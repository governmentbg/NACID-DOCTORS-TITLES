package com.nacid.web.model.applications;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.bl.impl.applications.UniversityWithFaculty;

public class UniversityWithFacultyWebModel {
    private UniversityWebModel university;
    private UniversityFacultyWebModel faculty;

    public UniversityWithFacultyWebModel(UniversityWithFaculty universityWithFaculty) {
        this(universityWithFaculty.getUniversity(), universityWithFaculty.getFaculty());
    }
    public UniversityWithFacultyWebModel(University university, UniversityFaculty faculty) {
        this.university = new UniversityWebModel(university);
        this.faculty = faculty == null ? null : new UniversityFacultyWebModel(faculty);
    }

    public UniversityWebModel getUniversity() {
        return university;
    }

    public UniversityFacultyWebModel getFaculty() {
        return faculty;
    }
}
