package com.ext.nacid.regprof.web.handlers.impl.epay;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.ext.nacid.regprof.web.model.applications.EpayRedirectWebModel;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.payments.EpayPaymentDetail;
import com.nacid.bl.payments.Liability;
import com.nacid.bl.payments.PaymentsDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.epay.EpayUtils;
import com.nacid.web.exceptions.UnknownRecordException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by georgi.georgiev on 15.02.2016.
 */
public class EpayRedirectPaymentHandler extends NacidExtBaseRequestHandler {
    public EpayRedirectPaymentHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        Integer applicationId = DataConverter.parseInteger(request.getParameter("applicationId"), null);
        if (applicationId == null) {
            throw new UnknownRecordException();
        }
        ExtRegprofApplicationsDataProvider dp = getNacidDataProvider().getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationDetailsImpl details = dp.getApplicationDetails(applicationId);

        try {
            ExtRegprofUserAccessUtils.checkApplicantActionAccess(ExtRegprofUserAccessUtils.USER_ACTION_VIEW, getExtPerson(request, response), details);
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e.getMessage());
        }
        PaymentsDataProvider paymentsDataProvider = nacidDataProvider.getPaymentsDataProvider();

        Liability liab = paymentsDataProvider.getLiabilityByExternalApplicationId(applicationId);
        if (liab == null) {
            throw new RuntimeException("No liability defined for given application Id...");//TODO:Vinagi li shte ima generirani zadyljeniq kym dadeno zaqvlenie????
        }
        if (liab.getStatus() == Liability.LIABILITY_PAID) {
            throw new RuntimeException("Задължението е вече платено!");
        }
        EpayPaymentDetail paymentDetails = paymentsDataProvider.getLastPayment(liab.getId(), true);
        if (paymentDetails.getStatus() == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_PAID) {
            throw new RuntimeException("Zadyljenieto e markirano kato neplateno, a poslednoto plashtane po tova zadyljenie e markirano kato plateno...");//ne bi trqbvalo da se stiga do tuk, tyi kato ako plashtaneto e plateno, trqbva i statusa na zadyljenieto da e plateno!!!!
        }




        UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();

        String invoiceNumber = paymentDetails.getRefNumber();

        Map<String, String> commonVars = utilsDataProvider.getCommonVariablesAsMap();
        String paymentUrl = commonVars.get(UtilsDataProvider.EPAY_PAYMENT_URL);
        String epayOkUrl = commonVars.get(UtilsDataProvider.EPAY_URL_OK);
        epayOkUrl = MessageFormat.format(epayOkUrl, applicationId, invoiceNumber);//TODO:VERY IMPORTANT: Da mahna invoiceNumber ot url-to. Ne trqbva da se razchita na tozi invoice number, tyj kato nqkoj moje da si go editne na ryka, a v bazata trqbva da ima vryzka applicationId to invoiceNumber!!!!!

        String epayCancelUrl = commonVars.get(UtilsDataProvider.EPAY_URL_CANCEL);
        epayCancelUrl = MessageFormat.format(epayCancelUrl, applicationId, invoiceNumber);//TODO:VERY IMPORTANT: Da mahna invoiceNumber ot url-to. Ne trqbva da se razchita na tozi invoice number, tyj kato nqkoj moje da si go editne na ryka, a v bazata trqbva da ima vryzka applicationId to invoiceNumber!!!!!

        String cin = commonVars.get(UtilsDataProvider.EPAY_CIN);
        String secretKey = commonVars.get(UtilsDataProvider.EPAY_SECRET_KEY);

        BigDecimal amount = liab.getAmount();//serviceType.getServicePrice();


        ExtPerson p = getExtPerson(request, response);

        String personNames = p.getFullName();
        String description = commonVars.get(UtilsDataProvider.EPAY_PAYMENT_DESCRIPTION);

        String encoded = EpayUtils.generateEpayContentEncoded(cin, null, invoiceNumber, amount, EpayUtils.CURRENCY.BGN, paymentDetails.getExpirationDate(), description, personNames, p.getCivilIdType(), p.getCivilId(),
                commonVars.get(UtilsDataProvider.NACID_MERCHANT_NAME), commonVars.get(UtilsDataProvider.NACID_IBAN), commonVars.get(UtilsDataProvider.NACID_BIC), commonVars.get(UtilsDataProvider.EPAY_PAYMENT_TYPE_ID));
        String checksum = EpayUtils.calculateCheckSum(encoded, secretKey);

        paymentsDataProvider.saveEpayCommuncationMessage("OUT", encoded, invoiceNumber, checksum);
        EpayRedirectWebModel wm = new EpayRedirectWebModel(paymentUrl, epayOkUrl, epayCancelUrl, checksum, encoded);
        request.setAttribute("epayRedirectWebModel", wm);
        setNextScreen(request, "epay_redirect");

    }
}
