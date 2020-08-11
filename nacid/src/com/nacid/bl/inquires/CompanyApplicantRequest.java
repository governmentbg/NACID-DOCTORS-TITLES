package com.nacid.bl.inquires;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * User: Georgi
 * Date: 15.4.2020 г.
 * Time: 13:43
 */
@AllArgsConstructor
@Getter
public class CompanyApplicantRequest {
    private List<Integer> applicantTypes;
    private List<Integer> companyIds;

}
