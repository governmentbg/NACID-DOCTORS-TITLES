package com.nacid.web.model.inquires;

import com.nacid.bl.inquires.ExpertInquireRequest;
import com.nacid.data.DataConverter;

import javax.servlet.http.HttpServletRequest;

import static com.nacid.web.model.inquires.CommonInquireWebModel.generateApplicationStatusesForCommonInquire;

/**
 * User: Georgi
 * Date: 24.3.2020 г.
 * Time: 15:15
 */
public class ExpertInquireWebModel extends ExpertInquireRequest {
    public ExpertInquireWebModel(HttpServletRequest request) {
        applicationTypeEntryNumSeries = InquiresUtils.generateApplicationTypeEntryNumSeries(request);
        applicationNum = DataConverter.parseString(request.getParameter("application_number"), null);
        dateFrom = DataConverter.parseDate(request.getParameter("appDateFrom"));
        dateTo = DataConverter.parseDate(request.getParameter("appDateTo"));
        //userId = DataConverter.parseInteger(request.getParameter("user_id"), null);
        userId = InquiresUtils.generateElementList(request, "userIds", "user");
        responsibleUserIds = InquiresUtils.generateElementList(request, "responsibleUserIds", "responsibleUser");

        applicationStatuses = generateApplicationStatusesForCommonInquire(request);
    }
}
