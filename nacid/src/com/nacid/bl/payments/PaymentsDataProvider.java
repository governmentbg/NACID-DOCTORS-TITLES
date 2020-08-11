package com.nacid.bl.payments;

import java.math.BigDecimal;
import java.util.Date;


public interface PaymentsDataProvider {
    //public Liability getLiability(int id);
    //public EpayPaymentDetails getPayment(int id);
    //public List<EpayPaymentDetails> getEpayPaymentsByLiability(int liabilityId);
    //public Liability saveLiability(Liability liab);
    //public EpayPaymentDetails saveEpayPayment(EpayPaymentDetails payment);
    public Liability saveLiability(int id, BigDecimal amount, int status, Date dateGenerated, Date datePayment, String currency, int paymentType);
    public Liability getLiabilityByExternalApplicationId(int extApplicationId);
    /**
     * vry6ta poslednata informaciq za plashtane po dadeno zadyljenie...
     * @param liabilityId
     * @param addNewIfNotExist - dobavq nov zapis ako ne sy6testvuva takyv i togava go vry6ta.
     * @return
     */
    public EpayPaymentDetail getLastPayment(int liabilityId, boolean addNewIfNotExist);
    
    /**
     * 
     * @param applicationId
     * @return posledniq invoiceNumber za dadenoto zaqvlenie!
     */
    //public String getCurrentInvoiceNumberByExternalApplicationId(int applicationId);
    public void updateEpayPaymentDetailsStatus(String refNumber, Integer status, Date datePayment);
    
    public void saveEpayCommuncationMessage(String inOut, String content, String correlationId, String checksum);
    /**
     * 
     * @param refNumber
     * @return osnovna informaciq za plashtaneto s tozi nomer - mail-a na choveka, izvyrshil plashtaneto, suma na plashtaneto, data na plashtane, status na plashtane!
     */
    public ExtApplicationPaymentInformation getExtApplicationPaymentInformationPaymentByRefNumber(String refNumber);
    /**
     * 
     * @param refNumber
     * @param idn pri obry6tenie kym url-to za plashtane na zadyljenie kym biudjeta, epay ni vry6tat nqkakyv unikalen nomer, kojto se kazva IDN, kojto posle se polzva ot klienta za plashtane na zadyljenieto si...
     */
    public void updateEpayPaymentIdn(String refNumber, String idn);
}
