package com.nacid.bl.impl.applications;

import com.nacid.bl.ApplicationDetailsForExpertPosition;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationExpert;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.DataConverter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgi.georgiev on 17.08.2015.
 */
public class ApplicationDetailsForExpertPositionImpl extends ApplicationDetailsForReportImpl implements ApplicationDetailsForExpertPosition {
    private ApplicationExpert applicationExpert;
    private String expertNames;

    public ApplicationDetailsForExpertPositionImpl(NacidDataProvider nacidDataProvider, Application application, ApplicationExpert applicationExpert) {
        super(nacidDataProvider, application);
        this.applicationExpert = applicationExpert;
        expertNames = applicationExpert.getExpert().getFullName();

    }
    public String getCourseContent() {
        return applicationExpert.getCourseContent();
    }
    public String getExpertPosition() {
        return applicationExpert.getExpertPositionId() == null ? null : applicationExpert.getExpertPosition().getName();
    }

    public String getExpertEducationLevel() {
        return applicationExpert.getEducationLevel() == null ? "" : applicationExpert.getEducationLevel().getName();
    }
    public String getExpertLegalReason() {
        return applicationExpert.getLegalReasonId() == null ? "" : applicationExpert.getLegalReason().getName();
    }

    public String getExpertSpeciality() {
        List<Speciality> specialityList = applicationExpert.getSpecialities();
        if (specialityList != null) {
            List<String> result = new ArrayList<String>();
            for (Speciality speciality : specialityList) {
                result.add(speciality.getName());
            }
            return StringUtils.join(result, "<br />");
        }
        return "";
    }
    public String getExpertProfessionalQualification() {
        return applicationExpert.getQualification() == null ? "" : applicationExpert.getQualification().getName();
    }

    public String getPreviousBoardDecisions() {
        return applicationExpert.getPreviousBoardDecisions();
    }
    public String getSimilarBulgarianPrograms() {
        return applicationExpert.getSimilarBulgarianPrograms();
    }

    public String getExpertNames() {
        return expertNames;
    }

    @Override
    public String getExpertNotes() {
        return DataConverter.parseString(applicationExpert.getNotes(), "");
    }
}
