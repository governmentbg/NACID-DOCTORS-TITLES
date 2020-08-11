package com.nacid.bl.impl.ras.objects;

/**
 * User: ggeorgiev
 * Date: 22.10.2019 г.
 * Time: 18:54
 */
public class RegisterApplicationRequest {
    private StructuredData structuredData;
    private String electronicServiceUri;
    private Applicant applicant;

    public StructuredData getStructuredData() {
        return structuredData;
    }

    public void setStructuredData(StructuredData structuredData) {
        this.structuredData = structuredData;
    }

    public String getElectronicServiceUri() {
        return electronicServiceUri;
    }

    public void setElectronicServiceUri(String electronicServiceUri) {
        this.electronicServiceUri = electronicServiceUri;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}





