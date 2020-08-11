package com.nacid.db.payments;

import com.nacid.data.payments.EpayPaymentDetailRecord;
import com.nacid.data.payments.ExtApplicationPaymentInformationRecord;
import com.nacid.data.payments.LiabilityRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class PaymentsDB extends DatabaseService {
    private static final String SQL = " select pen.email,liab.amount, pdl.status as payment_status, apn.date_submitted, pdl.date_payment as payment_date_payment, pen.fname||coalesce(' '||pen.sname,'')||coalesce(' '||pen.lname,'') as full_name " +
                                        " from eservices.regprof_application apn " +
                                        " join eservices.regprof_app_libilities lmp on lmp.ext_regprof_application_id = apn.id " +
                                        " join liabilities liab on liab.id = lmp.liability_id " +
                                        " join epay_payment_details pdl on pdl.liability_id = liab.id " +
                                        " join eservices.person pen on pen.id = apn.applicant_id " +
                                        " where pdl.ref_number = ?";
    public PaymentsDB(DataSource ds) {
        super(ds);
    }
    public LiabilityRecord getLiabitlityRecordByExternalApplicationId(int extApplicationId) throws SQLException {
        return selectRecord(LiabilityRecord.class, " id = (SELECT liability_id from eservices.regprof_app_libilities WHERE ext_regprof_application_id = ?)", extApplicationId);
    }
    public EpayPaymentDetailRecord insertEpayPaymentDetailsRecord(EpayPaymentDetailRecord rec) throws SQLException {
        rec.setId((int)getSequenceNextValue("epay_payment_details_id_seq"));
        rec.setRefNumber("NORQ" + String.format("%05d", rec.getId()));
        insertRecord(rec, false);
        return rec;
    }
    public void updateEpayPaymentDetailsStatus(String refNumber, Integer status, Timestamp datePayment) throws SQLException {
        execute("UPDATE epay_payment_details set status = ?, date_payment = ? where ref_number = ?", status, datePayment, refNumber);
    }
    public void updateEpayPaymentIdn(String refNumber, String idn) throws SQLException {
        execute("UPDATE epay_payment_details set idn = ? where ref_number = ?", idn, refNumber);
    }
    public void updateLiabilityStatusByEpayPaymentRefNumber(String refNumber, Integer status, Timestamp datePayment) throws SQLException {
        execute("UPDATE liabilities set status = ?, date_payment = ? where id = (select liability_id from epay_payment_details where ref_number = ?)", status, datePayment, refNumber);
    }
    public EpayPaymentDetailRecord selectLastEpayPaymentDetailRecord(int liabilityId) throws SQLException {
        return selectRecord(EpayPaymentDetailRecord.class, "liability_id = ? order by id desc limit 1", liabilityId);
    }
    public ExtApplicationPaymentInformationRecord getExtApplicationPaymentInformationPaymentByRefNumber(String refNumber) throws SQLException {
        List<ExtApplicationPaymentInformationRecord> recs = selectRecords(SQL, ExtApplicationPaymentInformationRecord.class, refNumber);
        return recs.size() == 0 ? null : recs.get(0);
                
    }
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        System.out.println(String.format("%09d", 123));
        PaymentsDB db = new PaymentsDB(new StandAloneDataSource());
        db.selectLastEpayPaymentDetailRecord(1);
    }
}
