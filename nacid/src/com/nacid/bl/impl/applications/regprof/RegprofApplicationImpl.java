package com.nacid.bl.impl.applications.regprof;

import java.util.Date;
import java.util.List;

import com.nacid.data.regprof.applications.RegprofDocumentRecipientRecord;
import org.springframework.web.context.request.RequestAttributes;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.users.User;
import com.nacid.bl.users.regprof.ResponsibleUser;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.applications.PersonRecord;

//RayaWritten------------------------------------
public class RegprofApplicationImpl implements RegprofApplication, RequestParameterInterface {
    
    private PersonRecord applicant;
    private PersonRecord representative;
    private RegprofApplicationDetailsImpl applicationDetails;
    private PersonDocumentRecord applicantDocuments;
    private RegprofTrainingCourseDetailsImpl trCourseDocumentPersonDetails;
    private List<ResponsibleUser> responsibleUsers;
    private RegprofDocumentRecipientRecord documentRecipientRecord;
    
    public PersonRecord getApplicant() {
        return applicant;
    }
    public void setApplicant(PersonRecord applicant) {
        this.applicant = applicant;
    }
    public PersonRecord getRepresentative() {
        return representative;
    }
    public void setRepresentative(PersonRecord representative) {
        this.representative = representative;
    }
    public RegprofApplicationDetailsImpl getApplicationDetails() {
        return applicationDetails;
    }
    public void setApplicationDetails(
            RegprofApplicationDetailsImpl applicationDetails) {
        this.applicationDetails = applicationDetails;
    }
    public PersonDocumentRecord getApplicantDocuments() {
        return applicantDocuments;
    }
    public void setApplicantDocuments(PersonDocumentRecord applicantDocuments) {
        this.applicantDocuments = applicantDocuments;
    }
    public String getDocFlowNumber() {
        return getApplicationNumber() + "/" + DataConverter.formatDate(getApplicationDate());
    }
    public String getApplicationNumber() {
        return applicationDetails.getAppNum();
    }
    public Date getApplicationDate() {
        return applicationDetails.getAppDate();
    }
    public int getApplicationStatusId() {
        return applicationDetails.getStatus();
    }
    public String getEmail() {
        return applicationDetails.getApplicantEmail();
    }
    public Integer getId() {
        return applicationDetails.getId();
    }
    public void setId(Integer id) {
        applicationDetails.setId(id);
    }
    public RegprofTrainingCourseDetailsImpl getTrCourseDocumentPersonDetails() {
        return trCourseDocumentPersonDetails;
    }
    public void setTrCourseDocumentPersonDetails(
            RegprofTrainingCourseDetailsImpl trCourseDocumentPersonDetails) {
        this.trCourseDocumentPersonDetails = trCourseDocumentPersonDetails;
    }
    public List<ResponsibleUser> getResponsibleUsers() {
        return responsibleUsers;
    }
    public void setResponsibleUsers(List<ResponsibleUser> responsibleUsers) {
        this.responsibleUsers = responsibleUsers;
    } 
    
    public void setNamesToUpperCase(){
        PersonRecord p;
        if(applicant != null){
            p = applicant;
            if(p.getfName()!= null){
                p.setfName(p.getfName().toUpperCase());
            }
            if(p.getlName()!= null){
                p.setlName(p.getLName().toUpperCase());
            }
            if(p.getsName()!= null){
                p.setsName(p.getSName().toUpperCase());
            }
        }
        if(representative != null){
            p = representative;
            if(p.getfName()!= null){
                p.setfName(p.getfName().toUpperCase());
            }
            if(p.getlName()!= null){
                p.setlName(p.getLName().toUpperCase());
            }
            if(p.getsName()!= null){
                p.setsName(p.getSName().toUpperCase());
            }
        }
        if( trCourseDocumentPersonDetails.getDocumentFname()!= null){
            trCourseDocumentPersonDetails.setDocumentFname(trCourseDocumentPersonDetails.getDocumentFname().toUpperCase());
        }
        if( trCourseDocumentPersonDetails.getDocumentSname()!= null){
            trCourseDocumentPersonDetails.setDocumentSname(trCourseDocumentPersonDetails.getDocumentSname().toUpperCase());
        }
        if( trCourseDocumentPersonDetails.getDocumentLname()!= null){
            trCourseDocumentPersonDetails.setDocumentLname(trCourseDocumentPersonDetails.getDocumentLname().toUpperCase());
        }
        p = null;
    }

    @Override
    public RegprofDocumentRecipientRecord getDocumentRecipient() {
        return documentRecipientRecord;
    }

    @Override
    public void setDocumentRecipient(RegprofDocumentRecipientRecord record) {
        this.documentRecipientRecord = record;
    }
}
//-----------------------------------------------------------------------