package com.nacid.data.applications;

import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.bl.impl.Utils;
import com.nacid.data.annotations.Table;

import java.util.Date;

@Table(name = "university_faculty", sequence = "university_faculty_id_seq")
public class UniversityFacultyRecord implements UniversityFaculty {
    private int id;
    private int universityId;
    private String name;
    private String originalName;
    private Date dateFrom;
    private Date dateTo;

    public UniversityFacultyRecord() {
    }

    public UniversityFacultyRecord(int id, int universityId, String name, String originalName, Date dateFrom, Date dateTo) {
        this.id = id;
        this.universityId = universityId;
        this.name = name;
        this.originalName = originalName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public boolean isActive() {
        return Utils.isRecordActive(dateFrom, dateTo);
    }
}
