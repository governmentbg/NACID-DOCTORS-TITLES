/**
 * 
 */
package com.nacid.data.mail;

import java.util.Date;

/**
 * @author bocho
 *
 */
public class MailRecord {

    private int id = -1;
    private int in_out = 0;
    private int processed = 0;
    private Date date = null;
    private String sent_from = null;
    private String sent_to = null;
    private String reply_to = null;
    private String subject = null;
    private String body = null;
    private String message_id = null;
    
    /**
     * @param id
     * @param inOut
     * @param processed
     * @param date
     * @param sentFrom
     * @param sentTo
     * @param replyTo
     * @param subject
     * @param body
     * @param messageId
     */
    public MailRecord(int id, int inOut, int processed, Date date,
            String sentFrom, String sentTo, String replyTo, String subject,
            String body, String messageId) {
        super();
        this.id = id;
        this.in_out = inOut;
        this.processed = processed;
        this.date = date;
        this.sent_from = sentFrom;
        this.sent_to = sentTo;
        this.reply_to = replyTo;
        this.subject = subject;
        this.body = body;
        this.message_id = messageId;
    }
    
    /**
     * 
     */
    public MailRecord() {
        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the in_out
     */
    public int getIn_out() {
        return in_out;
    }

    /**
     * @param inOut the in_out to set
     */
    public void setIn_out(int inOut) {
        in_out = inOut;
    }

    /**
     * @return the processed
     */
    public int getProcessed() {
        return processed;
    }

    /**
     * @param processed the processed to set
     */
    public void setProcessed(int processed) {
        this.processed = processed;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the sent_from
     */
    public String getSent_from() {
        return sent_from;
    }

    /**
     * @param sentFrom the sent_from to set
     */
    public void setSent_from(String sentFrom) {
        sent_from = sentFrom;
    }

    /**
     * @return the sent_to
     */
    public String getSent_to() {
        return sent_to;
    }

    /**
     * @param sentTo the sent_to to set
     */
    public void setSent_to(String sentTo) {
        sent_to = sentTo;
    }

    /**
     * @return the reply_to
     */
    public String getReply_to() {
        return reply_to;
    }

    /**
     * @param replyTo the reply_to to set
     */
    public void setReply_to(String replyTo) {
        reply_to = replyTo;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the message_id
     */
    public String getMessage_id() {
        return message_id;
    }

    /**
     * @param messageId the message_id to set
     */
    public void setMessage_id(String messageId) {
        message_id = messageId;
    }

    /**
     * return String representation of MailRecord.
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
