package com.nacid.bl.impl.applications;

import java.util.Objects;

public class UniversityIdWithFacultyId {
    private Integer universityId;
    private Integer facultyId;

    public UniversityIdWithFacultyId(Integer universityId, Integer facultyId) {
        this.universityId = universityId;
        this.facultyId = facultyId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniversityIdWithFacultyId that = (UniversityIdWithFacultyId) o;
        return Objects.equals(universityId, that.universityId) &&
                Objects.equals(facultyId, that.facultyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(universityId, facultyId);
    }
}
