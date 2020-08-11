package com.nacid.bl.inquires;

import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 13.4.2020 г.
 * Time: 23:52
 */
@Getter
public class ApplicantInquireRequest {
    /**
     *  applicantFname/ownerFname - firstName starts with.... pyrvoto ime zapo4va s....
     * 	applicantSname/ownerSname - secondName starts with...
     * 	applicantLname/ownerLname - lastName starts with...
     * 	applicantPersonalId/ownerPersonalId - chast ot personalId
     *  applicantCompanyName -> company's name starts with
     *  applicantCompanyEik -> company's eik contains
     * 	applicationNum - celiq applicationNum
     * 	dateFrom - nachanla data na syzdavane na zaqvlenie
     * 	dateTo - krajna data na syzdavane na zaqvlenie
     */
    protected List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNumSeries;
    protected String ownerFname;
    protected String ownerSname;
    protected String ownerLname;
    protected String ownerPersonalId;

    protected String applicantFname;
    protected String applicantSname;
    protected String applicantLname;
    protected String applicantPersonalId;

    protected String applicantCompanyName;
    protected String applicantCompanyEik;


    protected String diplFName;
    protected String diplSName;
    protected String diplLName;
    protected String reprFName;
    protected String reprSName;
    protected String reprLName;
    protected String reprPersonalId;
    protected Integer representativeCompany;
    protected String applicationNum;

    protected Date dateFrom;
    protected Date dateTo;
    protected Integer jointDegreeFlag;
}
