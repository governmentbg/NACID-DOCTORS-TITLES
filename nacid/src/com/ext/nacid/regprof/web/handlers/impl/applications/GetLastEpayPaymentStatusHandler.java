package com.ext.nacid.regprof.web.handlers.impl.applications;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.payments.EpayPaymentDetail;
import com.nacid.bl.payments.Liability;
import com.nacid.bl.payments.PaymentsDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.exceptions.UnknownRecordException;

public class GetLastEpayPaymentStatusHandler extends NacidExtBaseRequestHandler {

    public GetLastEpayPaymentStatusHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        Integer applicationId = DataConverter.parseInteger(request.getParameter("applicationId"), null);  
        if (applicationId == null) {
            throw new UnknownRecordException();
        }
        ExtRegprofApplicationsDataProvider dp = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationDetailsImpl details = dp.getApplicationDetails(applicationId);
        
        try {
            ExtRegprofUserAccessUtils.checkApplicantActionAccess(ExtRegprofUserAccessUtils.USER_ACTION_VIEW, getExtPerson(request, response), details);
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e.getMessage());
        }
        PaymentsDataProvider paymentsDataProvider = nacidDataProvider.getPaymentsDataProvider();
        Liability l = paymentsDataProvider.getLiabilityByExternalApplicationId(applicationId);
        EpayPaymentDetail lastPayment = paymentsDataProvider.getLastPayment(l.getId(), false);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("status", lastPayment.getStatus());
        } catch (JSONException e) {
            throw Utils.logException(e);
        }
        writeToResponse(response, jsonObj.toString());
    }
}
