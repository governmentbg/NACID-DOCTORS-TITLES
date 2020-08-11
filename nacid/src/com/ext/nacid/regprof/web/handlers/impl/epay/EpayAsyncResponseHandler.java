package com.ext.nacid.regprof.web.handlers.impl.epay;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.ext.nacid.regprof.web.handlers.impl.applications.EpayPaymentHandler;
import com.ext.nacid.web.handlers.NacidExtNoAuthorizationCheckBaseRequestHandler;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.payments.EpayPaymentDetail;
import com.nacid.bl.payments.ExtApplicationPaymentInformation;
import com.nacid.bl.payments.PaymentsDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.epay.EpayResponse;
import com.nacid.epay.EpayResponseException;
import com.nacid.epay.EpayUtils;



/**
 * chaka za asinhronniq response ot EPAY. Sled kato go poluchi, se opitva da sybudi nishkata, koqto e zaspala da chaka otgovora, sled koeto vry6ta otgovor na EPAY che nqma nujda da prashta poveche info za dadeniq invoiceNumber!
 * @author ggeorgiev
 *
 */
public class EpayAsyncResponseHandler extends NacidExtNoAuthorizationCheckBaseRequestHandler {
    
    public EpayAsyncResponseHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String checksum = request.getParameter("checksum");
        String encoded = request.getParameter("encoded");
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        String secretKey = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EPAY_SECRET_KEY);
        logger.debug("Checksum:" + checksum);
        logger.debug("Encoded:" + encoded);
        boolean isOk;
        EpayResponse resp;
        try {
            resp = EpayUtils.getResponse(encoded, checksum, secretKey);
            isOk = true;
        } catch (EpayResponseException e) {
            isOk = false;
            resp = e.getEpayResponse();
        }
        int status = 0;
        if (!StringUtils.isEmpty(resp.getInvoice())) {
            PaymentsDataProvider paymentsDataProvider = getNacidDataProvider().getPaymentsDataProvider();
            if (isOk) {//statusa se promenq samo ako response-a e korekten!
                if (resp.isPaidStatus()) {
                    status = EpayPaymentDetail.PAYMENT_DETAILS_STATUS_PAID;
                } else if (resp.isDeniedStatus()) {
                    status = EpayPaymentDetail.PAYMENT_DETAILS_STATUS_CANCELED;
                } else if (resp.isExpiredStatus()) {
                    //DO nothing...
                } else {
                    throw new RuntimeException("Unknown payment status..." + resp.getStatus());
                }
                
                if (status != 0) {
                    paymentsDataProvider.updateEpayPaymentDetailsStatus(resp.getInvoice(), status, resp.getPaymentTime());    
                }
            }
            paymentsDataProvider.saveEpayCommuncationMessage("IN", encoded, resp.getInvoice(), checksum);
            
            try {
                String r = EpayUtils.generateEpayResponse(resp.getInvoice(), isOk);
                paymentsDataProvider.saveEpayCommuncationMessage("OUT", r, resp.getInvoice(), null);
                response.getWriter().write(r);
                response.getWriter().flush();
                
            } catch (IOException e1) {
                throw Utils.logException(e1);
            }
            //notifying handler that payment response has come...
            EpayPaymentHandler handler = EpayPaymentHandler.getWaitingHandlerByInvoice(resp.getInvoice());
            if (isOk && handler != null) {
                synchronized (handler) {
                    handler.notify();
                }    
            }
            
            if (status != 0) {
                ExtApplicationPaymentInformation info = paymentsDataProvider.getExtApplicationPaymentInformationPaymentByRefNumber(resp.getInvoice());
                MailDataProvider mdp = getNacidDataProvider().getMailDataProvider();
                String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_MAIL_SENDER);
                String subject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EPAY_USER_MESSAGE_SUBJECT);
                String body = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EPAY_USER_MESSAGE_BODY);
                body = MessageFormat.format(body, status == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_PAID ? "извършихте" : "отказахте", DataConverter.formatDateTime(info.getDateSubmitted(), false));
                mdp.sendMessage(sender, sender, info.getFullName(), info.getEmail(), subject, body);
            }
            
        }
    }
}
