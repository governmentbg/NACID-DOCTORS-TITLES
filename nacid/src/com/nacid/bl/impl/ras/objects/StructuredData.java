package com.nacid.bl.impl.ras.objects;

/**
 * User: ggeorgiev
 * Date: 22.10.2019 г.
 * Time: 18:49
 */

public class StructuredData {
    private Person person;
    private AcademicDegree academicDegree;
    private RasFile certificateFile;
    private RasFile dissertationFile;
    private RasFile summaryFile;
//    private String lotId = null;


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public AcademicDegree getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(AcademicDegree academicDegree) {
        this.academicDegree = academicDegree;
    }

    public RasFile getCertificateFile() {
        return certificateFile;
    }

    public void setCertificateFile(RasFile certificateFile) {
        this.certificateFile = certificateFile;
    }

    public RasFile getDissertationFile() {
        return dissertationFile;
    }

    public void setDissertationFile(RasFile dissertationFile) {
        this.dissertationFile = dissertationFile;
    }

    public RasFile getSummaryFile() {
        return summaryFile;
    }

    public void setSummaryFile(RasFile summaryFile) {
        this.summaryFile = summaryFile;
    }
}