package com.nacid.bl.inquires;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 14.4.2020 г.
 * Time: 0:27
 */
@Getter
@NoArgsConstructor
public class ExpertInquireRequest extends EmployeeInquireRequest {
    public ExpertInquireRequest(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNumSeries, List<Integer> userId, List<Integer> responsibleUserIds, String applicationNum, Date dateFrom, Date dateTo, List<ApplicationStatusFromCommonInquire> applicationStatuses) {
        super(applicationTypeEntryNumSeries, userId, responsibleUserIds, applicationNum, dateFrom, dateTo, applicationStatuses);
    }
}
