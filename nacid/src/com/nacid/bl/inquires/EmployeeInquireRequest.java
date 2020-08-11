package com.nacid.bl.inquires;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 14.4.2020 г.
 * Time: 0:21
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInquireRequest {
    protected List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNumSeries;
    protected List<Integer> userId;
    protected List<Integer> responsibleUserIds;
    protected String applicationNum;
    protected Date dateFrom;
    protected Date dateTo;
    /**
     * aktualen status
     */
    protected List<ApplicationStatusFromCommonInquire> applicationStatuses;
}
