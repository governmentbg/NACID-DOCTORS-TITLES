/**
 * 
 */
package com.nacid.bl.mail;

import java.util.List;

import javax.mail.Session;

/**
 * @author bocho
 *
 */
public interface MailDataProvider {
    
    /**Extracts messages from mail server to be saved in DB
     * 
     */
    public abstract void getMailFromServer();
    /**
     * @param MailBean msg - the email message to be sent. Use MailBeanImpl.
     * @return if message is sent returns true, onerror - false
     */
    public abstract Integer sendMessage(MailBean msg);
    /**
     * @param fromName - ime na izprashta4a
     * @param fromEmail - email na izprashta4a
     * @param toName - ime na polu4atelq
     * @param toEmail - email na polu4atelq
     * @param subject - tema na syob6tenieto
     * @param message - syob6tenie
     * @return
     */
    public abstract Integer sendMessage(String fromName, String fromEmail, String toName, String toEmail, String subject, String message);

    /** Method for obtaining session.
     * @return mail session
     */
    public abstract Session getSession();
    
    public abstract Integer saveMail(MailBean msg);
    
    public List<Mail> getAllMails();
    public Mail getMailById(int id);
    public void deleteMailById(int id);
    public List<Mail> getMailsByRecepient(String recepient);
    public List<Mail> getMailsBySender(String sender);

}
