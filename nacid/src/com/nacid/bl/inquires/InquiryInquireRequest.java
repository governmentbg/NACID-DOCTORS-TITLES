package com.nacid.bl.inquires;

import lombok.Getter;

import java.util.List;

/**
 * User: Georgi
 * Date: 14.4.2020 г.
 * Time: 0:10
 */
@Getter
public class InquiryInquireRequest {
    /**
     *  jointDegreesFlag - 0 - bez syvmestni stepeni, 1 - samo syvmestni stepeni; null - nqma znachenie
     * 	documentTypeIds - id-ta na documentite - null ili prazen list ne u4astva vyv filtriraneto
     * 	universityCountryIds - id-ta na dyrjavei
     * 	universityIds
     * 	eventStatusIds - statusi na sybitie
     */
    protected List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNumSeries;
    protected List<Integer> documentTypeIds;
    protected List<Integer> eventStatusIds;
    protected List<Integer> universityCountryIds;
    protected List<Integer> universityIds;
    protected Integer jointDegreeFlag;
}
