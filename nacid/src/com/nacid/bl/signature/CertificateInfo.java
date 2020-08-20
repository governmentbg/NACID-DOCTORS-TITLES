package com.nacid.bl.signature;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by denislav.veizov on 24.11.2017 г..
 */
public class CertificateInfo {

    private String name;

    private String email;

    private String civilId;

    private String issuer;

    private String validFrom;

    private String validTo;

    private String unifiedIdCode;

    private String country;

    private static String FORMATTER_PATTERN = "dd-MM-yyyy HH:mm:ss";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Date getValidFrom() {
        try {
            return StringUtils.isEmpty(validFrom) ? null : new SimpleDateFormat(FORMATTER_PATTERN).parse(validFrom);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        try {
            return StringUtils.isEmpty(validTo) ? null : new SimpleDateFormat(FORMATTER_PATTERN).parse(validTo);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getUnifiedIdCode() {
        return unifiedIdCode;
    }

    public void setUnifiedIdCode(String unifiedIdCode) {
        this.unifiedIdCode = unifiedIdCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
