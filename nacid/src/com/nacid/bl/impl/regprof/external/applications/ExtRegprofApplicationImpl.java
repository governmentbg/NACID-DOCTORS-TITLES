package com.nacid.bl.impl.regprof.external.applications;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.data.external.applications.ExtPersonDocumentRecord;
import com.nacid.data.external.applications.ExtPersonRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseRecord;
import com.nacid.data.regprof.external.applications.ExtRegprofDocumentRecipientRecord;

import java.util.Objects;

public class ExtRegprofApplicationImpl implements RequestParameterInterface {
    /**
     * status pri koito potrebitelq oshte moje da si editva dannite
     */
    public static final int STATUS_EDITABLE = 0;
    /**
     * status pri koito dannite sa prateni za priznavane v internal bazata na nacid i user-a nqma pravo da editva
     */
    public static final int STATUS_NOT_EDITABLE = 1;
    /**
     * status pri koito kopirani v istinskata baza
     */
    public static final int STATUS_TRANSFERED = 3;

    /**
     * status, pri koito zaqvlenieto e prikliucheno, t.e. nqma da se prehvyrli nikoga v backoffice-a! Potrebitelq sy6to ne trqbva da gi vijda tezi zaqvleniq!!!!
     */
    public static final int STATUS_FINISHED = 4;
    
    private ExtPersonRecord applicant;
    private ExtPersonDocumentRecord applicantDocuments;
    private ExtRegprofApplicationDetailsImpl applicationDetails;
    private ExtRegprofTrainingCourseRecord trainingCourseDetails;
    private ExtRegprofDocumentRecipientRecord documentRecipient;
    private int differentApplicantRepresentative;
    public ExtPersonRecord getApplicant() {
        return applicant;
    }
    public void setApplicant(ExtPersonRecord applicant) {
        this.applicant = applicant;
    }
    public ExtPersonDocumentRecord getApplicantDocuments() {
        return applicantDocuments;
    }
    public void setApplicantDocuments(ExtPersonDocumentRecord applicantDocuments) {
        this.applicantDocuments = applicantDocuments;
    }
    public ExtRegprofApplicationDetailsImpl getApplicationDetails() {
        return applicationDetails;
    }
    public void setApplicationDetails(
            ExtRegprofApplicationDetailsImpl applicationDetails) {
        this.applicationDetails = applicationDetails;
        differentApplicantRepresentative = applicationDetails == null ? 0 : Objects.equals(applicationDetails.getApplicantId(), applicationDetails.getRepresentativeId()) ? 0 : 1;
    }
    public ExtRegprofTrainingCourseRecord getTrainingCourseDetails() {
        return trainingCourseDetails;
    }
    public void setTrainingCourseDetails(
            ExtRegprofTrainingCourseRecord trainingCourseDetails) {
        this.trainingCourseDetails = trainingCourseDetails;
    }
    public boolean isNew() {
        return applicationDetails == null || applicationDetails.getId() == null;
    }

    public ExtRegprofDocumentRecipientRecord getDocumentRecipient() {
        return documentRecipient;
    }

    public void setDocumentRecipient(ExtRegprofDocumentRecipientRecord documentRecipient) {
        this.documentRecipient = documentRecipient;
    }

    public int getDifferentApplicantRepresentative() {
        return differentApplicantRepresentative;
    }

    public void setDifferentApplicantRepresentative(int differentApplicantRepresentative) {
        this.differentApplicantRepresentative = differentApplicantRepresentative;
    }
}
