package com.nacid.bl.impl.payments;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.payments.EpayPaymentDetail;
import com.nacid.bl.payments.ExtApplicationPaymentInformation;
import com.nacid.bl.payments.Liability;
import com.nacid.bl.payments.PaymentsDataProvider;
import com.nacid.data.payments.EpayCommunicationMessageRecord;
import com.nacid.data.payments.EpayPaymentDetailRecord;
import com.nacid.data.payments.LiabilityRecord;
import com.nacid.db.payments.PaymentsDB;

public class PaymentsDataProviderImpl implements PaymentsDataProvider {
    private PaymentsDB db;
    private NacidDataProviderImpl nacidDataProvider;
    public PaymentsDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        db = new PaymentsDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    @Override
    public EpayPaymentDetail getLastPayment(int liabilityId, boolean addNewIfNotExist) {
        try {
            EpayPaymentDetailRecord rec = db.selectLastEpayPaymentDetailRecord(liabilityId);
            if (!addNewIfNotExist) {//ako ne trqbva da se dobavq nov zapis, vry6ta tova, koeto e namerilo...
                return rec;
            }
            
            //ako nqmame posleden zapis, ili zapisa e markinra kato otkazan, ili ako ne e otkazan, no mu e iztekyl expirationTime-a, togava se generira nov zapis!
            if (rec == null) {
                return generateNewEpayPaymentDetailRecord(liabilityId);
            } else if (rec != null && rec.getStatus() == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_CANCELED) {
                return generateNewEpayPaymentDetailRecord(liabilityId);
            } else if (rec != null && rec.getStatus() == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_GENERATED && rec.getExpirationDate().getTime() < new Date().getTime()) {
                return generateNewEpayPaymentDetailRecord(liabilityId);
            }
            return rec;
        } catch (SQLException e) {
            throw Utils.logException(e);
        } 
        
    }
    private EpayPaymentDetail generateNewEpayPaymentDetailRecord(int liabilityId) throws SQLException {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MINUTE, 60);//TODO:ExpirationTime????
        EpayPaymentDetailRecord rec = new EpayPaymentDetailRecord(0, liabilityId, EpayPaymentDetail.PAYMENT_DETAILS_STATUS_GENERATED, new Timestamp(new java.util.Date().getTime()), new Timestamp(cal.getTimeInMillis()), null, null);
        rec = db.insertEpayPaymentDetailsRecord(rec);
        return rec;
    }

    @Override
    public Liability getLiabilityByExternalApplicationId(int extApplicationId) {
        try {
            return db.getLiabitlityRecordByExternalApplicationId(extApplicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public void saveEpayCommuncationMessage(String inOut, String content, String correlationId, String checksum) {
        try {
            EpayCommunicationMessageRecord msg = new EpayCommunicationMessageRecord(0, new java.sql.Timestamp(new Date().getTime()), inOut, content, correlationId, checksum);
            db.insertRecord(msg);
        } catch (SQLException e) {
            Utils.logException(e); //ne throwvam exception, zashtoto logvaneto ne e sy6testveno za funkcionirane na prilojenieto!
        }   
    }
    /*@Override
    public String getCurrentInvoiceNumberByExternalApplicationId(int extApplicationId) {

        return null;
    }*/
    @Override
    public void updateEpayPaymentDetailsStatus(String refNumber, Integer status, Date datePayment) {
        try {
            db.updateEpayPaymentDetailsStatus(refNumber, status, datePayment == null ? null : new java.sql.Timestamp(datePayment.getTime()));
            if (status == EpayPaymentDetail.PAYMENT_DETAILS_STATUS_PAID) {//ako statusa e plateno, update-va i statusa na zadyljenieto!
                db.updateLiabilityStatusByEpayPaymentRefNumber(refNumber, Liability.LIABILITY_PAID, datePayment == null ? null : new java.sql.Timestamp(datePayment.getTime()));
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    public void updateEpayPaymentIdn(String refNumber, String idn) {
        try {
            db.updateEpayPaymentIdn(refNumber, idn);
        } catch (SQLException e) {
            
        }
    }
    public Liability saveLiability(int id, BigDecimal amount, int status, Date dateGenerated, Date datePayment, String currency, int paymentType) {
        try {
            LiabilityRecord rec = new LiabilityRecord(0, amount, Liability.LIABILITY_NOT_PAID, Utils.getTimestamp(dateGenerated), Utils.getTimestamp(datePayment), currency, paymentType);
            rec = db.insertRecord(rec);
            return rec;    
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    @Override
    public ExtApplicationPaymentInformation getExtApplicationPaymentInformationPaymentByRefNumber(String refNumber) {
        try {
            return db.getExtApplicationPaymentInformationPaymentByRefNumber(refNumber);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
}
