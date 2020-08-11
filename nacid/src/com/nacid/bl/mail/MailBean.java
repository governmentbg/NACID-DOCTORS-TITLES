package com.nacid.bl.mail;

import java.util.Date;
import java.util.Map;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;


public abstract class MailBean extends MimeMessage {
    
    private MailBean(){
        super((Session) null);
    }

    protected MailBean(Session session) {
        super(session);
    }

    /**Adds To recipient to the toLst list.
     * @param toAddress - address(name <address>) to send the message.
     * @return true if all is successful or false if toAddress is empty or null.
     */
    public abstract boolean addToAddress(String toAddress, String toName);

    /**Adds Cc recipient to the ccLst list.
     * @param ccAddress - address(name <address>) to send the message as carbon copy.
     * @return true if all is successful or false if ccAddress is empty or null.
     */
    public abstract boolean addCcAddress(String ccAddress, String ccName);

    /**Adds Bcc recipient to the bccLst list.
     * @param bccAddress - address(name <address>) to send the message as blind carbon copy.
     * @return true if all is successful or false if bccAddress is empty or null.
     */
    public abstract boolean addBccAddress(String bccAddress, String bccName);

    public abstract Date getDateSent();

    public abstract void setDateSend(Date d);

    /**
     * @return the bccMap
     */
    public abstract Map<String, String> getBccMap();

    /**
     * @param bccMap the bccMap to set
     */
    public abstract void setBccMap(Map<String, String> bccMap);

    /**
     * @return the ccMap
     */
    public abstract Map<String, String> getCcMap();

    /**
     * @param ccMap the ccMap to set
     */
    public abstract void setCcMap(Map<String, String> ccMap);

    /**
     * @return the fromMap
     */
    public abstract Map<String, String> getFromMap();

    /**
     * @param fromMap
     */
    public abstract void setFromMap(Map<String, String> fromMap);

    /**
     * @return the msgBody
     */
    public abstract String getMsgBody();

    /**
     * @param msgBody the msgBody to set
     */
    public abstract void setMsgBody(String msgBody);

    /**
     * @return the replyToMap
     */
    public abstract Map<String, String> getReplyToMap();

    /**
     * @param replyToMap the replyToMap to set
     */
    public abstract void setReplyToMap(Map<String, String> replyToMap);

    /**
     * @return the senderStr
     */
    public abstract String getSenderStr();

    /**
     * @param senderStr the senderStr to set
     */
    public abstract void setSenderStr(String senderStr);

    /**
     * @return the subjectStr
     */
    public abstract String getSubjectStr();

    /**
     * @param subjectStr the subjectStr to set
     */
    public abstract void setSubjectStr(String subjectStr);

    /**
     * @return the toMap
     */
    public abstract Map<String, String> getToMap();

    /**
     * @param toMap the toMap to set
     */
    public abstract void setToMap(Map<String, String> toMap);
    /**
     * @return
     */
    public abstract int getInOut();
    /**
     * @param inOut
     */
    public abstract void setInOut(int inOut);
    /**
     * @return
     */
    public abstract boolean isProcessed();
    /**
     * @param processed
     */
    public abstract void setProcessed(boolean processed) ;
    public abstract String getMessage_id();

}