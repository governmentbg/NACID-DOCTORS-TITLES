package com.nacid.epay;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.CivilIdType;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EpayUtils {
    public static enum CURRENCY {
        BGN("BGN"), USD("USD"), EUR("EUR");
        private String currency;
        private CURRENCY(String curr) {
            this.currency = curr;
        }
        public String getCurrencyName() {
            return currency;
        }
    }
    public static String generateEpayContentEncoded(String cin, String email, String invoiceNumber, BigDecimal amount, CURRENCY currency, Date expireTime, String description, String personNames,
            FlatNomenclature civilIdType, String civilId, String merchantName, String iban, String bic, String paymentType) {
        /*List<String> result = new ArrayList<String>();
        if (!StringUtils.isEmpty(cin)) {
            result.add("MIN=" + cin);
        }
        if (!StringUtils.isEmpty(email)) {
            result.add("EMAIL="+email);
        }
        if (result.size() == 0) {
            throw new RuntimeException("Ether Client Id Number or Email must be eneterd");
        }
        if (StringUtils.isEmpty(invoiceNumber)) {
            throw new RuntimeException("Invoice number is mandatory");
        }
        result.add("INVOICE=" + invoiceNumber);
        if (amount == null || amount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new RuntimeException("Amount must be greater than 0.01");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        result.add("AMOUNT=" + amount.toPlainString());
        if (currency != null) {
            result.add("CURRENCY=" + currency.getCurrencyName());
        }
        if (expireTime == null || expireTime.getTime() < new Date().getTime()) {
            throw new RuntimeException("Expire time is mandatory and must be in the future");
        }
        result.add("EXP_TIME=" + DataConverter.formatDateTime(expireTime, false));
        if (!StringUtils.isEmpty(description)) {
            result.add("DESCR=" + description + ":" + personNames);
        }
        result.add("MERCHANT=" + merchantName);
        result.add("IBAN=" + iban);
        result.add("BIC=" + bic);
        result.add("PSTATEMENT=" + paymentType);
        result.add("STATEMENT=" + description);
        result.add("OBLIG_PERSON=" + (personNames.length() > 26 ? personNames.substring(0, 26) : personNames));//TODO:
        if (civilIdType.getId() == CivilIdType.CIVIL_ID_TYPE_EGN) {
            result.add("EGN=" + civilId);
        } else if (civilIdType.getId() == CivilIdType.CIVIL_ID_TYPE_LNCH) {
            result.add("LNC=" + civilId);
        } else {
            throw new RuntimeException("Unsupported civil id type. The only supported are EGN/LNCh");
        }
        result.add("DOC_NO=" + paymentType);
        result.add("ENCODING=utf-8");
        String toBeEncoded = StringUtils.join(result, "\n");
        Base64 b = new Base64();
        b.setLineSeparator("");
        b.setLineLength(76);
        return b.encode(toBeEncoded.getBytes());*/




        List<String> result = new ArrayList<String>();
        if (!StringUtils.isEmpty(cin)) {
            result.add("MIN=" + cin);
        }
        if (!StringUtils.isEmpty(email)) {
            result.add("EMAIL="+email);
        }
        if (result.size() == 0) {
            throw new RuntimeException("Ether Client Id Number or Email must be eneterd");
        }
        if (StringUtils.isEmpty(invoiceNumber)) {
            throw new RuntimeException("Invoice number is mandatory");
        }
        result.add("INVOICE=" + invoiceNumber);
        if (amount == null || amount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new RuntimeException("Amount must be greater than 0.01");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        result.add("AMOUNT=" + amount.toPlainString());
        if (currency != null) {
            result.add("CURRENCY=" + currency.getCurrencyName());
        }
        if (expireTime == null || expireTime.getTime() < new Date().getTime()) {
            throw new RuntimeException("Expire time is mandatory and must be in the future");
        }
        result.add("EXP_TIME=" + DataConverter.formatDateTime(expireTime, false));
        if (!StringUtils.isEmpty(description)) {
            String desc =  description + ":" + personNames;
            desc = desc.replace("\"", "");//replace-vam kavichkite, zashtoto ima nqkakyv problem s tqh
            result.add("DESCR=" + (desc.length() > 100 ? desc.substring(0, 100) : desc));
        }
        result.add("MERCHANT=" + merchantName);
        result.add("IBAN=" + iban);
        result.add("BIC=" + bic);

        result.add("STATEMENT=" + description);
        personNames = personNames.replace("\"", "");//replace-vam kavichkite, zashtoto ima nqkakyv problem s tqh
        result.add("OBLIG_PERSON=" + (personNames.length() > 26 ? personNames.substring(0, 26) : personNames));//TODO:

        if (civilIdType.getId() == CivilIdType.CIVIL_ID_TYPE_EGN) {
            result.add("EGN=" + civilId);
        } else if (civilIdType.getId() == CivilIdType.CIVIL_ID_TYPE_LNCH || civilIdType.getId() == CivilIdType.CIVIL_ID_TYPE_PERSONAL_DOCUMENT) {
            result.add("LNC=" + civilId);
        } else {
            throw new RuntimeException("Unsupported civil id type. The only supported are EGN/LNCh");
        }

        if (!StringUtils.isEmpty(paymentType)) {
            result.add("DOC_NO=" + paymentType);
        }

        String toBeEncoded = StringUtils.join(result, "\n");
        Base64 b = new Base64();
        b.setLineSeparator("");
        b.setLineLength(76);
        try {
            return b.encode(toBeEncoded.getBytes("windows-1251"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String calculateCheckSum(String encodedString, String key) {
        try {
            return Utils.signText(encodedString, key, "HmacSha1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 
     * @param encoded
     * @param checksum
     * @param key
     * @return EpayResponse obekt, ako moje da decode-ne "encoded" Stringa. Ako checksumata poluchena ot decded+key ne syotvetstva na checksum-a ot epay, togava hvyrlq exception!!!
     * @throws EpayResponseException
     */
    public static EpayResponse getResponse(String encoded, String checksum, String key) throws EpayResponseException {
        
        Base64 b = new Base64();
        b.setLineLength(76);
        b.setLineSeparator("");
        String result = new String(b.decode(encoded));
        String[] s = result.split("=|:");
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < s.length - 1; i+=2) {
            map.put(s[i], s[i+1]);
        }
        String invoice = map.get("INVOICE");
        if (invoice != null) {
            invoice = invoice.trim();
        }
        String status = map.get("STATUS");
        if (status != null) {
            status = status.trim();    
        }
        
        Date payTime;
        try {
            payTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(map.get("PAY_TIME"));
        } catch (Exception e) {
            payTime = null;
        }
        String stan = map.get("STAN");
        String bcode = map.get("BCODE");
        EpayResponse resp = new EpayResponse(invoice, status, payTime, stan, bcode);
        String checksumCalculated = calculateCheckSum(encoded, key);
        if (!checksumCalculated.equals(checksum)) {
            throw new EpayResponseException("Returned from epay checksum and calculated checksum are not equal Calculated:" + checksumCalculated + " Returned:" + checksum , resp);
        }
        return resp;
    }
    public static String generateEpayResponse(String invoiceNumber, boolean isOk) {
        List<String> result = new ArrayList<String>();
        result.add("INVOICE=" + invoiceNumber);
        result.add("STATUS="+ (isOk ? "OK" : "ERR"));
        String str = StringUtils.join(result, ":");
        return str;
    }

    public static void main(String[] args) throws ParseException {
        /*Calendar cal = new GregorianCalendar(2020, Calendar.AUGUST, 1);
        String encodedString  = generateEpayContentEncoded("SET_THIS_CORRECT", null, "0", BigDecimal.valueOf(22.80), null, cal.getTime(), "Test");
        System.out.println(calculateCheckSum(encodedString, "SET_THIS_CORRECT"));*/
        String str = "INVOICE=6543317262227388765:STATUS=PAID:PAY_TIME=20130228162203:STAN=028867:BCODE=028867";
        String[] s = str.split("=|:");
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < s.length - 1; i+=2) {
            map.put(s[i], s[i+1]);
        }
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").parse(map.get("PAY_TIME")));
        //System.out.println(Arrays.asList());
        
    }
}
