package com.nacid.bl.impl.external.applications;

import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.bl.external.applications.ExtUniversityFaculty;
import com.nacid.data.external.applications.ExtDiplomaIssuerRecord;

import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 13.3.2019 г.
 * Time: 16:44
 */
public class ExtUniversityFacultyImpl implements ExtUniversityFaculty {
    private ExtDiplomaIssuerRecord extDiplomaIssuer;
    private UniversityFaculty faculty;

    public ExtUniversityFacultyImpl(UniversityFaculty uf, ExtDiplomaIssuerRecord r) {
        this.faculty = uf;
        this.extDiplomaIssuer = r;
    }

    @Override
    public boolean isStandardFaculty() {
        return faculty != null;
    }

    @Override
    public String getFacultyTxt() {
        return isStandardFaculty() ? null : extDiplomaIssuer.getFacultyTxt();
    }

    @Override
    public int getId() {
        return isStandardFaculty() ? faculty.getId() : 0;
    }

    @Override
    public int getUniversityId() {
        return isStandardFaculty() ? faculty.getUniversityId() : 0;
    }

    @Override
    public String getName() {
        return isStandardFaculty() ? faculty.getName() : null;
    }

    @Override
    public String getOriginalName() {
        return isStandardFaculty() ? faculty.getOriginalName() : null;
    }

    @Override
    public Date getDateFrom() {
        return isStandardFaculty() ? faculty.getDateFrom() : null;
    }

    @Override
    public Date getDateTo() {
        return isStandardFaculty() ? faculty.getDateTo() : null;
    }

    @Override
    public boolean isActive() {
        return isStandardFaculty() ? faculty.isActive() : false;
    }
}
