package com.nacid.bl;

import com.nacid.bl.applications.ApplicationDetailsForReport;

/**
 * Created by georgi.georgiev on 17.08.2015.
 */
public interface ApplicationDetailsForExpertPosition extends ApplicationDetailsForReport {
    public String getCourseContent();
    public String getExpertPosition();
    public String getExpertEducationLevel() ;
    public String getExpertSpeciality();
    public String getExpertProfessionalQualification();
    public String getPreviousBoardDecisions();
    public String getSimilarBulgarianPrograms();
    public String getExpertLegalReason();
    public String getExpertNames();
    public String getExpertNotes();
}
