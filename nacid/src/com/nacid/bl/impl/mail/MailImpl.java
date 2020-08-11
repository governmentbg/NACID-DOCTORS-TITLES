package com.nacid.bl.impl.mail;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.nacid.bl.mail.Mail;
import com.nacid.data.mail.MailRecord;

public class MailImpl implements Mail {

    private int id;
    private String subject;
    private String body;
    private Date date;
    private boolean income;
    private boolean processed;
    private String recepient;
    
    public MailImpl(MailRecord rec) {
        id = rec.getId();
        subject = StringEscapeUtils.escapeXml(rec.getSubject());
        body = StringEscapeUtils.escapeXml(rec.getBody());
        date = rec.getDate();
        income = rec.getIn_out() != 0;
        processed = rec.getProcessed() != 0;
        recepient = StringEscapeUtils.escapeXml(rec.getSent_to());
    }
    
    
    @Override
    public int getId() {
       return id;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public boolean isIncome() {
        return income;
    }

    @Override
    public boolean isProcessed() {
        return processed;
    }


    @Override
    public String getRecepient() {
        return recepient;
    }

}
