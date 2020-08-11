/**
 * 
 */
package com.nacid.bl.impl.mail;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.mail.Mail;
import com.nacid.bl.mail.MailBean;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.nomenclatures.CopyType;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.mail.MailRecord;
import com.nacid.db.mail.MailDB;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.Security;
import java.sql.SQLException;
import java.util.*;

/**
 * @author bocho
 * 
 */
public class MailDataProviderImpl implements MailDataProvider {

    /**
     * 
     */
    private final String HOST;
    /**
     * host for pop3
     */
    private final String POP_HOST = "";
    /**
     * host for IMAP
     */
    private final String IMAP_HOST = "";
    /**
     * Debug mode shows mess
     */
    private final boolean MAIL_DEBUG = true;
    /**
     * User for SMTP authentication not used for now
     */
    private final String SMTP_AUTH_USER;
    /**
     * Password for SMTP user authentication not used for now
     */
    private final String SMTP_AUTH_PWD;

    private final String SMTP_ADDITIONAL_PROPERTIES;
    /**
     * SSL Socket factory in use not used for now
     */
    private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    /**
     * SMTP port not used for now
     */
    private final String SMTP_PORT;
    /**
     * 
     */
    private Properties props = new Properties();
    /**
     * 
     */
    private NacidDataProvider nacidDataProvider;
    /**
     * 
     */
    private MailDB db;

    /**
     * @param nacidDataProvider
     */
    public MailDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new MailDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
        UtilsDataProvider utilsDataProvider = nacidDataProvider
                .getUtilsDataProvider();
        HOST = utilsDataProvider
                .getCommonVariableValue(UtilsDataProvider.SMTP_HOST);
        SMTP_AUTH_USER = utilsDataProvider
                .getCommonVariableValue(UtilsDataProvider.SMTP_AUTH_USER);
        SMTP_AUTH_PWD = utilsDataProvider
                .getCommonVariableValue(UtilsDataProvider.SMTP_AUTH_PASS);
        SMTP_PORT = utilsDataProvider
                .getCommonVariableValue(UtilsDataProvider.SMTP_PORT);
        SMTP_ADDITIONAL_PROPERTIES = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.MAIL_ADDITIONAL_PROPERTIES);

        //props.put("mail.pop3.user", SMTP_AUTH_USER);
        //props.put("mail.pop3.host", "172.16.0.10");
        
        this.props.put("mail.smtp.host", HOST);
        this.props.put("mail.debug", MAIL_DEBUG);

        if (!StringUtils.isEmpty(SMTP_ADDITIONAL_PROPERTIES)) {
            for (String s:SMTP_ADDITIONAL_PROPERTIES.split(";")) {
                props.put(s.split("=")[0], s.split("=")[1]);
            }
        }

        // IMAP provider
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        // POP3 provider
        //props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        // NNTP provider (if any)
        // props.setProperty("mail.nntp.socketFactory.class", SSL_FACTORY);
        // IMAP provider
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        // POP3 provider
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        // NNTP provider (if any)
        // props.setProperty("mail.nntp.socketFactory.fallback", "false");
        // IMAP provider
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");
        // POP3 provider
        props.setProperty("mail.pop3.port", "110");// 995
        props.setProperty("mail.pop3.socketFactory.port", "110");// 995
        // NNTP provider (if any)
        // props.setProperty( "mail.pop3.port", "563");
        // props.setProperty( "mail.pop3.socketFactory.port", "563");
    }

    // /**Method for creating instances.
    // * Setting properties, before returning an instance.
    // * @return new instance of MailSenderImpl
    // */
    // public static MailSender getInstance(){
    // MailSenderImpl mailSend = new MailSenderImpl();
    //        
    // return mailSend;
    // }
    /*
     * (non-Javadoc)
     * 
     * @see com.nacid.bl.mail.MailSender#sendMessage(com.nacid.bl.mail.MailBean)
     */
    public Integer sendMessage(MailBean msg) {
        try {
            Transport.send(msg);
            msg.setProcessed(true);
            return this.saveMail(msg);
        } catch (MessagingException e) {
            Utils.logException(e);
            // System.err.println(e);
            return null;
        }
    }

    /**
     * author: GGerogiev
     * @return
     */
    public Integer sendMessage(String fromName, String fromEmail,
                               String toName, String toEmail, String subject, String message) {
        Map<String, String> fromM = new HashMap<String, String>();
        fromM.put(fromEmail, fromName);
        Map<String, String> toM = new HashMap<String, String>();
        toM.put(toEmail, toName);
        MailBean mailBean = new MailBeanImpl(fromM, fromEmail, toM, null, null,
                subject, fromM, message, new Date(), getSession());
        return sendMessage(mailBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nacid.bl.mail.MailSender#getSession()
     */
    public Session getSession() {
        Session session;
        if (!org.apache.commons.lang.StringUtils.isEmpty(SMTP_AUTH_USER)) {
            props.put("mail.smtp.auth", "true");
            session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
                        }
                    }
            );
        } else {
            session = Session.getInstance(props);
        }
        return session;
    }

    @Override
    public void getMailFromServer() {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        try {
            Session ses = this.getSession();
            Store store = ses.getStore("pop3");
            store.connect("172.16.0.10", "rudi", "rudi");
            // Get "INBOX"
            Folder fldr = store.getFolder("INBOX");
            //If messages will be removed or edited set READ_WRITE:
            fldr.open(Folder.READ_WRITE);
            int count = fldr.getMessageCount();
            System.out.println(count + " new Messages");
            for (int i = 1; i <= count; i++) {
                // Get a message by its sequence number
                MimeMessage m = (MimeMessage) fldr.getMessage(i);
                InternetAddress[] from = (InternetAddress[]) m.getFrom();
                Address sender = m.getSender();
                InternetAddress[] to = (InternetAddress[]) m.getRecipients(Message.RecipientType.TO);
                InternetAddress[] cc = (InternetAddress[]) m.getRecipients(Message.RecipientType.CC);
                InternetAddress[] bcc = (InternetAddress[]) m.getRecipients(Message.RecipientType.BCC);
                Date d = m.getReceivedDate();
                if(d == null) d = m.getSentDate();
                if(d == null) d = new Date();
                
                ByteArrayOutputStream sos = new ByteArrayOutputStream();
                InputStream is = m.getInputStream();
                int read = 0;
                byte[] buf = new byte[1024];

                while ((read = is.read(buf)) > 0) {
                    sos.write(buf, 0, read);
                }
                sos.close();
                is.close();
                
                MailBean mb = new MailBeanImpl(
                        convertAddressToMap(from),
                        ""+ (sender == null ? m.getFrom()[0] : sender),
                        convertAddressToMap(to),
                        convertAddressToMap(cc),
                        convertAddressToMap(bcc),
                        m.getSubject(),
                        convertAddressToMap((InternetAddress[]) m.getReplyTo()),
                        sos.toString("UTF-8"), d, 1, false, ses);
                this.saveMail(mb);
                //Removes already read messages!
                m.setFlag(Flags.Flag.DELETED, true);
                
                generateAttachedDoc(mb, nacidDataProvider, 1);
            }
            //NB! If this is true messages with flag DELETED=true will be DELETED!!!
            fldr.close(true);
            store.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (NoSuchProviderException nspe) {
            nspe.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    public static Map<String, String> convertAddressToMap(InternetAddress[] ia) {
        Map<String, String> addressMap = new HashMap<String, String>();
        if(ia != null) {
            for (InternetAddress a : ia) {
                addressMap.put(a.getAddress(), a.getPersonal());
            }
        }
        return addressMap;
    }

    @Override
    public Integer saveMail(MailBean msg) {
        String sent_to = "";
        String reply_to = "";
        Set<String> strSet = msg.getToMap().keySet();
        for (String mail : strSet) {
            sent_to = sent_to
                    + (msg.getToMap().get(mail) != null ? msg.getToMap().get(
                            mail) : "") + " <" + mail + ">" + ", ";
        }
        strSet = msg.getReplyToMap().keySet();
        for (String mail : strSet) {
            reply_to = reply_to
                    + (msg.getReplyToMap().get(mail) != null ? msg
                            .getReplyToMap().get(mail) : "") + " <" + mail
                    + ">" + ", ";
        }
        MailRecord mr;
        mr = new MailRecord(-1, msg.getInOut(), (msg.isProcessed() ? 1 : 0),
                new Date(), msg.getSenderStr(), sent_to, reply_to, msg
                        .getSubjectStr(), msg.getMsgBody(), msg.getMessage_id());
        return db.saveMail(mr);
    }

    @Override
    public List<Mail> getAllMails() {
        List<MailRecord> recs = null;

        try {
            recs = db.selectRecords(MailRecord.class, null);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        List<Mail> ret = new ArrayList<Mail>();
        if (recs != null) {
            for (MailRecord r : recs) {
                ret.add(new MailImpl(r));
            }
        }
        return ret;
    }

    @Override
    public List<Mail> getMailsBySender(String sender) {
        List<MailRecord> recs = null;

        recs = db.getMailBySender(sender);

        List<Mail> ret = new ArrayList<Mail>();
        if (recs != null) {
            for (MailRecord r : recs) {
                ret.add(new MailImpl(r));
            }
        }
        return ret;
    }

    @Override
    public List<Mail> getMailsByRecepient(String recepient) {
        List<MailRecord> recs = null;

        recs = db.getMailByRecepient(recepient);

        List<Mail> ret = new ArrayList<Mail>();
        if (recs != null) {
            for (MailRecord r : recs) {
                ret.add(new MailImpl(r));
            }
        }
        return ret;
    }

    @Override
    public Mail getMailById(int id) {
        MailRecord rec = new MailRecord();

        try {
            rec = db.selectRecord(rec, id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return new MailImpl(rec);
    }

    @Override
    public void deleteMailById(int id) {
        try {
            db.deleteRecord(MailRecord.class, id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public static void generateAttachedDoc(MailBean m, NacidDataProvider nDP, int userCreated) {
        String sender = m.getSenderStr();
        String subject = m.getSubjectStr();
        String body = m.getMsgBody();
        
        
        Map<String, String> fromM = m.getFromMap(); 
        Set<String> keys = fromM.keySet();
        String senderMail = keys.iterator().next();
        //String senderMail = m.getFromMap().get(key);
        
        
        StringBuilder sb = new StringBuilder();
        sb.append("From:" + sender + "\r\n");
        sb.append("Subject:" + subject + "\r\n");
        sb.append("Body:" + body );
        
        byte[] data = null;
        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw Utils.logException(e);
        }
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        AttachmentDataProvider attDP = nDP.getApplicationAttachmentDataProvider();
        Application app = nDP.getApplicationsDataProvider().getApplicationByMail(senderMail);
        if(app == null) {
            System.out.println("No application for mail:" + senderMail);
            return;
        }
        attDP.saveAttacment(0, app.getId(), body, 
                DocumentType.DOC_TYPE_MESSAGE_BY_EMAIL_IN, 
                CopyType.ELECTRONIC_FORM, 
                null, null, "text/plain", 
                "email_in_" + DataConverter.formatDateTime(m.getDateSent(), false), 
                is, data.length, 
                null, null, null, 0, userCreated);
    }

    public static void main(String[] args) {
        NacidDataProvider nacidDataProvider = NacidDataProvider
                .getNacidDataProvider(new StandAloneDataSource());
        MailDataProvider mailDataProvider = nacidDataProvider
                .getMailDataProvider();
        Map<String, String> fromM = new HashMap<String, String>();
        fromM.put("nacid_test@gravis.bg", "Test");
        Map<String, String> toM = new HashMap<String, String>();
        toM.put("gogobg@yahoo.com", "gogobg@yahoo.com");
        /*
         * MailBeanImpl(Map<String,String>,String,Map<String,String>,
         * Map<String,String>,Map<String,String>,String,
         * Map<String,String>,String,Date,Session)
         */
        /*
         * MailBeanImpl(Map<String, String> fromM, String sender, Map<String,
         * String> toM, Map<String, String> ccM, Map<String, String> bccM,
         * String subject, Map<String, String> replyTo, String msgBody, Date
         * date, int in_out, boolean processed, Session ses)
         */
        MailBean msg = new MailBeanImpl(fromM, "nacid_test@gravis.bg", toM,
                ((Map<String, String>) null), ((Map<String, String>) null),
                "What's uuuuuuuuup!", fromM, "What's uuuuuuup!", new Date(), 0,
                false, mailDataProvider.getSession());
        mailDataProvider.sendMessage(msg);
        System.out.println("end...");
    }

}
