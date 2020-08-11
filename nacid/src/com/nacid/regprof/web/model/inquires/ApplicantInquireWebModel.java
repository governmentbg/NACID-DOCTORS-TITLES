package com.nacid.regprof.web.model.inquires;

import com.nacid.data.DataConverter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class ApplicantInquireWebModel {

    private int applicationType;
    private String applicantFname;
    private String applicantSname;
    private String applicantLname;
    private String applicantPersonalId;

    private String diplFName;
    private String diplSName;
    private String diplLName;
    private String reprFName;
    private String reprSName;
    private String reprLName;
    private String reprPersonalId;
    private Integer representativeCompany;
    private String applicationNum;

    private Date dateFrom;
    private Date dateTo;

    public ApplicantInquireWebModel(HttpServletRequest request) {
        applicationType = DataConverter.parseInt(request.getParameter("applicationType"), 1);

        applicantFname = DataConverter.parseString(request.getParameter("applicant_fname"), null);
        applicantSname = DataConverter.parseString(request.getParameter("applicant_sname"), null);
        applicantLname = DataConverter.parseString(request.getParameter("applicant_lname"), null);
        applicantPersonalId = DataConverter.parseString(request.getParameter("applicant_personalId"), null);

        diplFName = DataConverter.parseString(request.getParameter("dipl_fname"), null);
        diplSName = DataConverter.parseString(request.getParameter("dipl_sname"), null);
        diplLName = DataConverter.parseString(request.getParameter("dipl_lname"), null);
        reprFName = DataConverter.parseString(request.getParameter("repr_fname"), null);
        reprSName = DataConverter.parseString(request.getParameter("repr_sname"), null);
        reprLName = DataConverter.parseString(request.getParameter("repr_lname"), null);
        reprPersonalId = DataConverter.parseString(request.getParameter("repr_personalId"), null);
        representativeCompany = DataConverter.parseInteger(request.getParameter("repr_company"), null);
        applicationNum = DataConverter.parseString(request.getParameter("application_number"), null);
        dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
    }

    public String getApplicationNum() {
        return applicationNum;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public String getDiplFName() {
        return diplFName;
    }

    public String getDiplSName() {
        return diplSName;
    }

    public String getDiplLName() {
        return diplLName;
    }

    public String getReprFName() {
        return reprFName;
    }

    public String getReprSName() {
        return reprSName;
    }

    public String getReprLName() {
        return reprLName;
    }

    public String getReprPersonalId() {
        return reprPersonalId;
    }

    public Integer getRepresentativeCompany() {
        return representativeCompany;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public String getApplicantFname() {
        return applicantFname;
    }

    public String getApplicantSname() {
        return applicantSname;
    }

    public String getApplicantLname() {
        return applicantLname;
    }

    public String getApplicantPersonalId() {
        return applicantPersonalId;
    }


}
