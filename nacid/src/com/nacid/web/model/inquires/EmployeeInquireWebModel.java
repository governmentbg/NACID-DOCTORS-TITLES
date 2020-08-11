package com.nacid.web.model.inquires;

import com.nacid.bl.inquires.EmployeeInquireRequest;
import com.nacid.data.DataConverter;

import javax.servlet.http.HttpServletRequest;

import static com.nacid.web.model.inquires.CommonInquireWebModel.generateApplicationStatusesForCommonInquire;

public class EmployeeInquireWebModel extends EmployeeInquireRequest {
    public EmployeeInquireWebModel(HttpServletRequest request) {
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

