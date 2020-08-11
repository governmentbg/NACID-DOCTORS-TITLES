package com.nacid.bl;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_AUTH_USER = "George.V.Georgiev@gmail.com ";
	private static final String SMTP_AUTH_PWD = "";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String SMTP_PORT = "465";
	public static boolean sendEmail(String from, String to, String subject, String content) {
		return sendEmail(from, new String[] {to}, null, null, subject, content);
	}
	public static boolean sendEmail(String from, String[] to, String[] cc, String[] bcc, String subject, String content) {
		Properties props = new Properties();
		boolean debug = true;
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");
		
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
			}
		});
		session.setDebug(debug);
		Message msg = new MimeMessage(session);

		try {
			Address addressFrom = toAddress(from);
			msg.setFrom(addressFrom);
			msg.setRecipients(Message.RecipientType.TO, toAddresses(to));
			if (cc != null && cc.length > 0) {
				msg.setRecipients(Message.RecipientType.CC, toAddresses(cc));
			}
			if (bcc != null && bcc.length > 0) {
				msg.setRecipients(Message.RecipientType.BCC, toAddresses(cc));
			}
			msg.setSubject(subject);
			msg.setSentDate(new java.util.Date());
			msg.setContent(content, "text/plain");
			Transport.send(msg);
			//TODO:Log sent mails????
			return true;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	public static Address toAddress(String address) throws AddressException {
		return new InternetAddress(address);
	}
	public static Address[] toAddresses(String[] addresses) throws AddressException {
		if (addresses == null || addresses.length == 0) {
			return null;
		}
		InternetAddress[] result = new InternetAddress[addresses.length];
		for (int i = 0; i < addresses.length; i++) {
			result[i] = (InternetAddress)toAddress(addresses[i]);
		}
		return result;
	}

	
	public static void main(String[] args) {
		sendEmail(SMTP_AUTH_USER, "gogobg@yahoo.com", "test subject", "test content");
		System.out.println("end...");
	}

}
