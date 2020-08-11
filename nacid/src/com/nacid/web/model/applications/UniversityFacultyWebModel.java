package com.nacid.web.model.applications;

import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.data.DataConverter;

public class UniversityFacultyWebModel {
    private String id;
    private String universityId;
    private String name;
    private String originalName;
    private String dateFrom;
    private String dateTo;


    public UniversityFacultyWebModel(UniversityFaculty uf) {
        this.id = uf.getId() + "";
        this.universityId = uf.getUniversityId() + "";
        this.name = uf.getName() + (uf.isActive() ? "" : " (inactive) ");
        this.originalName = uf.getOriginalName();
        this.dateFrom = DataConverter.formatDate(uf.getDateFrom());
        this.dateTo = DataConverter.formatDate(uf.getDateTo());
    }

    public String getId() {
        return id;
    }

    public String getUniversityId() {
        return universityId;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }
    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

}
