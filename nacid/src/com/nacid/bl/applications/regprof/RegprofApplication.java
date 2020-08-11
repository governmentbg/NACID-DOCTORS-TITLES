package com.nacid.bl.applications.regprof;

import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.bl.users.regprof.ResponsibleUser;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.applications.PersonRecord;
import com.nacid.data.regprof.applications.RegprofDocumentRecipientRecord;

import java.util.Date;
import java.util.List;

//RayaWritten-------------------------
public interface RegprofApplication {

    public PersonRecord getApplicant();
    public void setApplicant(PersonRecord applicant);
    public PersonRecord getRepresentative();
    public void setRepresentative(PersonRecord representative);
    public RegprofApplicationDetailsImpl getApplicationDetails();
    public PersonDocumentRecord getApplicantDocuments();
    public void setApplicantDocuments(PersonDocumentRecord applicantDocuments);
    public void setApplicationDetails(RegprofApplicationDetailsImpl applicationDetails);
    public String getDocFlowNumber();
    public String getApplicationNumber();
    public Date getApplicationDate();
    public int getApplicationStatusId();
    public String getEmail();
    public Integer getId();
    public void setId(Integer id);
    public RegprofTrainingCourseDetailsImpl getTrCourseDocumentPersonDetails();
    public void setTrCourseDocumentPersonDetails(RegprofTrainingCourseDetailsImpl trCourseDocumentPersonDetails);
    public List<ResponsibleUser> getResponsibleUsers();
    public void setResponsibleUsers(List<ResponsibleUser> responsibleUsers); 
    public void setNamesToUpperCase();

    public RegprofDocumentRecipientRecord getDocumentRecipient();
    public void setDocumentRecipient(RegprofDocumentRecipientRecord record);
}
//------------------------------------