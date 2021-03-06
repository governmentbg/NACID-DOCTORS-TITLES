package com.nacid.web.model.inquires;

import com.nacid.bl.inquires.ApplicantInquireRequest;
import com.nacid.data.DataConverter;

import javax.servlet.http.HttpServletRequest;

public class ApplicantInquireWebModel extends ApplicantInquireRequest {

    public ApplicantInquireWebModel(HttpServletRequest request, Integer jointDegreeFlag) {
        applicationTypeEntryNumSeries = InquiresUtils.generateApplicationTypeEntryNumSeries(request);
        ownerFname = DataConverter.parseString(request.getParameter("owner_fname"), null);
        ownerSname = DataConverter.parseString(request.getParameter("owner_sname"), null);
        ownerLname = DataConverter.parseString(request.getParameter("owner_lname"), null);
        ownerPersonalId = DataConverter.parseString(request.getParameter("owner_personalId"), null);

        applicantFname = DataConverter.parseString(request.getParameter("applicant_fname"), null);
        applicantSname = DataConverter.parseString(request.getParameter("applicant_sname"), null);
        applicantLname = DataConverter.parseString(request.getParameter("applicant_lname"), null);
        applicantPersonalId = DataConverter.parseString(request.getParameter("applicant_personalId"), null);

        applicantCompanyName = DataConverter.parseString(request.getParameter("applicant_company_name"), null);
        applicantCompanyEik = DataConverter.parseString(request.getParameter("applicant_company_eik"), null);


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
        this.jointDegreeFlag = jointDegreeFlag;
    }

}

