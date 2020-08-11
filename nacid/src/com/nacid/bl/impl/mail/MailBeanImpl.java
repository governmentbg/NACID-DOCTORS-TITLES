/**
 * This is a bean class representing e-mail message to be send or received.
 */
package com.nacid.bl.impl.mail;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.mail.MailBean;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author bocho
 *
 */
public class MailBeanImpl extends MailBean {

    /**
     * Blind carbon copy to tertiary recipients who receive the message without anyone else (including the To, Cc, and Bcc recipients) seeing who the tertiary recipients are.
     */
    private Map<String, String> bccMap = new HashMap<String, String>();
    /**
     * Carbon copy to secondary recipients—other interested parties
     */
    private Map<String, String> ccMap = new HashMap<String, String>();
    /**
     * The e-mail address, and optionally the name of the author.
     */
    private Map<String, String> fromMap = new HashMap<String, String>();
    /**
     * The text of the message.
     */
    private String msgBody = null;
    /**
     * Address that should be used to reply to the message.
     */
    private Map<String, String> replyToMap = new HashMap<String, String>();
    /**
     * Address of the actual sender acting on behalf of the author listed in the From: field (secretary, list manager, etc.).
     */
    private String senderStr = null;
    /**
     * A brief summary of the topic of the message. Certain abbreviations are commonly used in the subject, including "RE:" and "FW:".
     */
    private String subjectStr = null;
    /**
     * The e-mail address(es), and optionally name(s) of the message's recipient(s). Indicates primary recipients (multiple allowed)
     */
    private Map<String, String> toMap = new HashMap<String, String>();
    
    /**
     * Indicates whether the message is incoming or outgoing:
     *  - 1 for incoming message
     *  - 0 for outgoing message
     */ 
    private int inOut = -1;
    
    /**
     * Indicates whether message has been processed(sent) successfully.
     * It has meaning only for outgoing messages. 
     */
    private boolean processed = false;
    
    /**
     * @param fromM
     * @param sender
     * @param toM
     * @param ccM
     * @param bccM
     * @param subject
     * @param replyTo
     * @param msgBody
     * @param date
     * @param ses
     */
    public MailBeanImpl(Map<String, String> fromM, String sender, Map<String, String> toM,
            Map<String, String> ccM, Map<String, String> bccM, String subject,
            Map<String, String> replyTo, String msgBody, Date date, Session ses) {
        this(fromM, sender, toM,
               ccM, bccM, subject,
               replyTo, msgBody,  date, 0, false, ses);
    }
    /**
     * @param fromM
     * @param sender
     * @param toM
     * @param ccM
     * @param subject
     * @param msgBody
     * @param ses
     */
    public MailBeanImpl(Map<String, String> fromM, String sender, Map<String, String> toM,
            Map<String, String> ccM, String subject, String msgBody, Session ses) {
        this(fromM, sender, toM,
             ((Map<String, String>)null), ((Map<String, String>)null), subject,
            fromM, msgBody, new Date(), 0, false, ses);
    }
    /** Creates message for e-mailing.
     * @param from - The e-mail address, and optionally the name of the author. Map expects "person@mail.com", "Person Name" - key-value.
     * @param sender - Address of the actual sender acting on behalf of the author listed in the From: field (secretary, list manager, etc.).
     * @param toM - The e-mail address(es), and optionally name(s) of the message's recipient(s). Indicates primary recipients (multiple allowed). Map expects "person@mail.com", "Person Name" - key-value.
     * @param ccM - Carbon copy to secondary recipients—other interested parties.Map expects "person@mail.com", "Person Name" - key-value.
     * @param bccM - Blind carbon copy to tertiary recipients who receive the message without anyone else (including the To, Cc, and Bcc recipients) seeing who the tertiary recipients are.Map expects "person@mail.com", "Person Name" - key-value.
     * @param subject - A brief summary of the topic of the message. Certain abbreviations are commonly used in the subject, including "RE:" and "FW:".
     * @param replyTo - Address that should be used to reply to the message.
     * @param msgBody - The text of the message.
     * @param date - The date of the message.
     * @param in_out - 1 for incoming and 0 for outgoing
     * @param processed - true if message is successfully processed
     * @param ses - Session for the message.
     */
    public MailBeanImpl(Map<String, String> fromM, String sender, Map<String, String> toM,
        Map<String, String> ccM, Map<String, String> bccM, String subject,
        Map<String, String> replyTo, String msgBody, Date date, int in_out, boolean processed, Session ses) {
        super(ses);
        try{
            //-- Maps:
            //From address(es):
            this.setFrom(fromM);
            //To address(es):
            this.setTo(toM);
            //Cc Addresses: 
            this.setCc(ccM);
            //Bcc Addresses: 
            this.setBcc(bccM);
            //Reply to address(es):
            this.setReplyTo(replyTo);
            //Strings:
            this.senderStr = sender;
            this.setSender(new InternetAddress(sender));
            this.subjectStr = subject;
            this.setSubject(this.subjectStr);
            this.msgBody = msgBody;
            this.setText(msgBody);
            this.setSentDate(date);
        }catch(MessagingException me){
            Utils.logException(me);
        }
        catch(UnsupportedEncodingException uee){
            Utils.logException(uee);
        }
        this.inOut=in_out;
        this.processed = processed;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#addToAddress(java.lang.String, java.lang.String)
     */
    public boolean addToAddress(String toAddress, String toName) {
        boolean isOK = false;
        if (toAddress != null && !("".equals(toAddress.trim()))) {
            if (toName == null) {
                toName = "";
            }
            this.toMap.put(toAddress, toName);
            try {
                this.addRecipient(RecipientType.TO, new InternetAddress(toAddress, toName));
            } catch (MessagingException mex) {
//                Logger.getLogger(MailBeanImpl.class.getName()).log(Level.SEVERE, null, mex);
                Utils.logException(mex);
                isOK = false;
            } catch (UnsupportedEncodingException uex) {
//                Logger.getLogger(MailBeanImpl.class.getName()).log(Level.SEVERE, null, uex);
                Utils.logException(uex);
//                System.err.println();
            isOK = false;
            }
            isOK = true;
        }
        return isOK;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#addCcAddress(java.lang.String, java.lang.String)
     */
    public boolean addCcAddress(String ccAddress, String ccName) {
        boolean isOK = false;
        if (ccAddress != null && !("".equals(ccAddress.trim()))) {
            if (ccName == null) {
                ccName = "";
            }
            this.ccMap.put(ccAddress, ccName);
            try {
                this.addRecipient(RecipientType.CC, new InternetAddress(ccAddress, ccName));
            } catch (MessagingException mex) {
                Utils.logException(mex);
                isOK = false;
            } catch (UnsupportedEncodingException uex) {
                Utils.logException(uex);
                isOK = false;
            }
            isOK = true;
        }
        return isOK;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#addBccAddress(java.lang.String, java.lang.String)
     */
    public boolean addBccAddress(String bccAddress, String bccName) {
        boolean isOK = false;
        if (bccAddress != null && !("".equals(bccAddress.trim()))) {
            if (bccName == null) {
                bccName = "";
            }
            this.bccMap.put(bccAddress, bccName);
            try {
                this.addRecipient(RecipientType.BCC, new InternetAddress(bccAddress, bccName));
            } catch (MessagingException mex) {
                Utils.logException(mex);
                isOK = false;
            } catch (UnsupportedEncodingException uex) {
                Utils.logException(uex);
                isOK = false;
            }
            isOK = true;
        } 
        return isOK;
        
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getDateSent()
     */
    public Date getDateSent(){
        try {
            return this.getSentDate();
        } catch (MessagingException me) {
            Utils.logException(me);
            return null;
        }
    }
    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setDateSend(java.util.Date)
     */
    public void setDateSend(Date d){
        try {
            this.setSentDate(d);
        } catch (MessagingException me) {
            Utils.logException(me);
        }
    }
    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getBccMap()
     */
    public Map<String, String> getBccMap() {
        return bccMap;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setBccMap(java.util.Map)
     */
    public void setBccMap(Map<String, String> bccMap) {
        try {
            this.setBcc(bccMap);
        } catch (UnsupportedEncodingException uee) {
            Utils.logException(uee);
        } catch (MessagingException me) {
            Utils.logException(me);
        }
    }
    /**
     * @param bccM
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private void setBcc(Map<String, String> bccM)throws UnsupportedEncodingException, MessagingException {
        if(bccM!=null && !(bccM.isEmpty()) ){
            this.bccMap = bccM;
            Set <String> bccAddr = this.bccMap.keySet();
            for (String addrStr : bccAddr ){
                String nameStr = this.bccMap.get(addrStr);
                this.addRecipient(RecipientType.BCC, new InternetAddress(addrStr, nameStr));
            }
        }
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getCcMap()
     */
    public Map<String, String> getCcMap() {
        return ccMap;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setCcMap(java.util.Map)
     */
    public void setCcMap(Map<String, String> ccMap) {
        try {
            this.setCc(ccMap);
        } catch (UnsupportedEncodingException uee) {
            Utils.logException(uee);
        } catch (MessagingException me) {
            Utils.logException(me);
        }
    }
    private void setCc(Map<String, String> ccM) throws UnsupportedEncodingException, MessagingException {
        if(ccM!=null && !(ccM.isEmpty()) ){
            this.ccMap = ccM;
            Set <String> ccAddr = this.ccMap.keySet();
            for (String addrStr : ccAddr ){
                String nameStr = this.ccMap.get(addrStr);
                this.addRecipient(RecipientType.CC, new InternetAddress(addrStr, nameStr));
            }
        }
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getFromMap()
     */
    public Map<String, String> getFromMap() {
        return fromMap;
    }

    /**
     * @param fromMap the fromMap to set
     * @throws UnsupportedEncodingException 
     * @throws MessagingException 
     */
    private void setFrom(Map<String, String> fromM) throws UnsupportedEncodingException, MessagingException {
        if(fromM!=null && !(fromM.isEmpty()) ){
            this.fromMap = fromM;
            Set <String> fromAddr = this.fromMap.keySet();
            Address[] addrArr= new Address[fromAddr.size()];
            int i = 0;
            for (String addrStr : fromAddr ){
                String nameStr = this.fromMap.get(addrStr);
                addrArr[i] = new InternetAddress(addrStr, nameStr);
                i++;
            }
            this.addFrom(addrArr);
        }
    }
    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setFromMap(java.util.Map)
     */
    public void setFromMap(Map<String, String> fromMap){
        try {
            this.setFrom(fromMap);
        } catch (UnsupportedEncodingException uee) {
            Utils.logException(uee);
        } catch (MessagingException me) {
            Utils.logException(me);
        }
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getMsgBody()
     */
    public String getMsgBody() {
        return msgBody;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setMsgBody(java.lang.String)
     */
    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getReplyToMap()
     */
    public Map<String, String> getReplyToMap() {
        return replyToMap;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setReplyToMap(java.util.Map)
     */
    public void setReplyToMap(Map<String, String> replyToMap) {
        try {
            this.setReplyTo(replyToMap);
        } catch (UnsupportedEncodingException uee) {
            Utils.logException(uee);
        } catch (MessagingException me) {
            Utils.logException(me);
        }
    }
    private void setReplyTo(Map<String, String> replyTo) throws UnsupportedEncodingException, MessagingException {
        if(replyTo!=null && !(replyTo.isEmpty()) ){
            this.replyToMap = replyTo;
            Set <String> replyToAddr = this.replyToMap.keySet();
            Address[] addrArr= new Address[replyToAddr.size()];
            int i = 0;
            for (String addrStr : replyToAddr ){
                String nameStr = this.replyToMap.get(addrStr);
                addrArr[i] = new InternetAddress(addrStr, nameStr);
                i++;
            }
            this.setReplyTo(addrArr);
        }
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getSenderStr()
     */
    public String getSenderStr() {
        return senderStr;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setSenderStr(java.lang.String)
     */
    public void setSenderStr(String senderStr) {
        this.senderStr = senderStr;
        try {
            this.setSender(new InternetAddress(this.senderStr));
        } catch (AddressException e) {
            Utils.logException(e);
        } catch (MessagingException e) {
            Utils.logException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getSubjectStr()
     */
    public String getSubjectStr() {
        return subjectStr;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setSubjectStr(java.lang.String)
     */
    public void setSubjectStr(String subjectStr) {
        this.subjectStr = subjectStr;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#getToMap()
     */
    public Map<String, String> getToMap() {
        return toMap;
    }

    /* (non-Javadoc)
     * @see com.nacid.bl.impl.mail.Mail#setToMap(java.util.Map)
     */
    public void setToMap(Map<String, String> toMap) {
        try {
            this.setTo(toMap);
        } catch (UnsupportedEncodingException uee) {
            Utils.logException(uee);
        } catch (MessagingException me) {
            Utils.logException(me);
        }
    }
    /**
     * @param toM
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private void setTo(Map<String, String> toM) throws UnsupportedEncodingException, MessagingException {
        if(toM!=null && !(toM.isEmpty()) ){
            this.toMap = toM;
            Set <String> toAddr = this.toMap.keySet();
            for (String addrStr : toAddr ){
                String nameStr = this.toMap.get(addrStr);
                this.addRecipient(RecipientType.TO, new InternetAddress(addrStr, nameStr));
            }
        }
    }

    /**
     * @return the inOut
     */
    public int getInOut() {
        return inOut;
    }

    /**
     * @param inOut the inOut to set
     */
    public void setInOut(int inOut) {
        this.inOut = inOut;
    }

    /**
     * @return the processed
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * @param processed the processed to set
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
    public String getMessage_id(){
        try {
            return this.getMessageID();
        } catch (MessagingException e) {
            Utils.logException(e);
            return null;
        }
    }
}
