package com.ext.nacid.regprof.web.handlers.impl.applications;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.regprof.web.model.applications.EPayWebModel;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
import com.nacid.bl.payments.EpayPaymentDetail;
import com.nacid.bl.payments.Liability;
import com.nacid.bl.payments.PaymentsDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.model.common.SystemMessageWebModel;
/**
 * obrabotva sinhronniq otgovor ot epay, koito se poluchava, kogato klienta natisne plashtane/otkaz ot plashtane!
 * sinhronniq otgovor zaspiva za 5 sekundi da chaka asinhronniq, ako asinhronniq ne e doshyl predi sinhronniq. 
 * ako za tezi x sekundi ne se poluchi asinhronniq otgovor, se zarejda stranicata s message = "chaka otgovor ot epay", i tazi stranica proverqva za asinhronniq otgovor prez 10 sekundi no max 30 pyti...
 *
 */
/**
 * 2013.04.26 - tova neshto otpadna, tyj kato za biudjetni institucii neshtata stoeli po razlichen nachin. Nqma preprashtane ot EPAY kym nasheto prilojenie, t.e. veche nikoi ne vika url-ta ot roda na http://localhost:8080/nacid_regprof_ext/control/epay?appId={0}&invoicenumber={1}&status=ok/cancel
 * poradi tazi prichina i vinagi v klasa EPayWebModel, field-a waitingForEpay e vinagi false! Prosto ne iskah da zatrivam tozi klas, zashtoto nqkoga ako trqbva da se implementira funkcionalnostta za nebiudjetno plashtane, moje da ni se naloji da go polzvame!
 */
public class EpayPaymentHandler extends NacidExtBaseRequestHandler {
    public EpayPaymentHandler(ServletContext servletContext) {
        super(servletContext);
    }
    private static final String WAITING_FOR_EPAY = "waitingForEpay";
    /*private boolean notified;
    public void setNotified(boolean notified) {
        this.notified = notified;
    }*/
    private static final Map<String, EpayPaymentHandler> handlers = new HashMap<String, EpayPaymentHandler>();
    public static EpayPaymentHandler getWaitingHandlerByInvoice(String invoiceNumber) {
        return handlers.get(invoiceNumber);
    }
    
    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInteger(request.getParameter("appId"), null);
        if (applicationId == null) {
            throw new RuntimeException();
        }

        String invoiceNumber = DataConverter.parseString(request.getParameter("invoicenumber"), null);
        //String status = DataConverter.parseString(request.getParameter("status"), null);
        SystemMessageWebModel wm;
        boolean waitingForEpayResponse = false;
        
        PaymentsDataProvider pdp = getNacidDataProvider().getPaymentsDataProvider();
        Liability l = pdp.getLiabilityByExternalApplicationId(applicationId);
        EpayPaymentDetail payment = pdp.getLastPayment(l.getId(), false);

        synchronized (this) {
            try {
                if (payment.getStatus() == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_GENERATED) {//nishkata zaspiva samo ako nqma otgovor ot EPAY, t.e. plashtaneto stoi sys status GENERATED!
                    handlers.put(invoiceNumber, this);
                    this.wait(5 * 1000);//sleeping for five seconds
                    handlers.remove(invoiceNumber);  
                    payment = pdp.getLastPayment(l.getId(), false);//sled kato izchaka 5 secundi ili ako asyncresponse-a go notify-ne predi tova, prochita nanovo infoto za payment-a
                }
            } catch (InterruptedException e) {
                handlers.remove(invoiceNumber);
                throw Utils.logException(e);
            }
            //v obshti linii response-a ot epay idva predi da se zaredi tozi handler, t.e. EpayAsyncResponseHandler ne namira nishka, koqto da notify-ne...
            if (payment.getStatus() == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_CANCELED || payment.getStatus() == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_PAID) {
                if (payment.getStatus() == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_PAID) {
                    wm = new SystemMessageWebModel("Вие извършихте плащането чрез EPAY", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
                } else {
                    wm = new SystemMessageWebModel("Вие отказахте плащането в EPAY", SystemMessageWebModel.MESSAGE_TYPE_ERROR);
                }
            } else {//payment status == generated i oshte nqma otgovor ot EPAY
                wm = new SystemMessageWebModel("Очакване на отговор от EPAY...", SystemMessageWebModel.MESSAGE_TYPE_ERROR);
                waitingForEpayResponse = true;
            }

            //TODO:Send email to the person if everithing is alright. Izprashtaneto na mail nqma da stava tuk a v EpayResponseHandler, maj
            
        }
            
        addSystemMessageToSession(request, "paymentStatusMessage", wm);
        try {
            response.sendRedirect(request.getContextPath() + "/control/applications/"
                    + "view"
                    + "?id=" + applicationId + "&activeForm=" + ExtRegprofApplicationsHandler.FORM_ID_EPAY_PAYMENT + (waitingForEpayResponse ? "&" + WAITING_FOR_EPAY + "=1" : ""));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    public static void fillEpayPaymentData(ExtPerson applicant, ExtRegprofApplicationDetailsImpl app, HttpServletRequest request, NacidDataProvider nacidDataProvider, Liability liab) {
        UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        String epayUrl = null;//utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EPAY_URL);
        String cin = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EPAY_CIN);
        ServiceType serviceType = nomenclaturesDataProvider.getServiceType(app.getServiceTypeId()); 
        Boolean waitingForEpay = DataConverter.parseBoolean(request.getParameter(WAITING_FOR_EPAY));
        EPayWebModel wm = new EPayWebModel(app.getId().toString(), serviceType, epayUrl, liab.getAmount(), cin, waitingForEpay ? false : liab.isPaid(), waitingForEpay);
        request.setAttribute("epaymodel", wm);
    }
    
}
