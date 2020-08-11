package com.nacid.web.model.mail;

import com.nacid.bl.mail.Mail;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.impl.mail.MailAdminHandler;

public class MailWebModel {
    private int id;
    private String date;
    private String status;
    private String type;
    private String recipient;
    private String subject;
    private String body;
    
    public MailWebModel(Mail m) {
       this.id = m.getId();
       this.body = m.getBody();
       this.subject = m.getSubject();
       this.recipient = m.getRecepient();
       this.date = DataConverter.formatDateTime(m.getDate(), true);
       this.status = MailAdminHandler.getMailStatus(m);
       this.type = MailAdminHandler.getMailType(m);
    }
    
    public int getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public String getStatus() {
        return status;
    }
    public String getType() {
        return type;
    }
    public String getRecipient() {
        return recipient;
    }
    public String getSubject() {
        return subject;
    }
    public String getBody() {
        return body;
    }
    
    
}
