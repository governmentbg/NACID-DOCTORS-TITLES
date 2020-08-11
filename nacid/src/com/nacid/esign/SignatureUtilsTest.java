package com.nacid.esign;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;

public class SignatureUtilsTest {

    private static Logger log = Logger.getLogger(SignatureUtils.class);
	
    public static void main(String[] args) {
    	try {
    		
    		SignatureUtils.configure("C:/Users/ggeorgiev/Desktop/verify-dsa/verify-dsa/trusted-ca.jks", "123456");
    		X509Certificate cert = SignatureUtils.verifyXMLSignature(new File("C:/Users/ggeorgiev/Desktop/verify-dsa/verify-dsa/signed_application.xml"));
    		ByteArrayOutputStream os = new ByteArrayOutputStream();
    		os.write(cert.getEncoded());
    		System.out.println(os.toString());
    		String issuer = SignatureUtils.getIssuerName(cert);
    		System.out.println("issuer: " + issuer);
    		System.out.println("name: " + SignatureUtils.getCommonName(cert));
    		System.out.println("email: " + SignatureUtils.getEmail(cert));
    		System.out.println("egn: " + SignatureUtils.getEgn(cert));
    		System.out.println("bulstat: " + SignatureUtils.getBulstat(cert));
    		
    		System.out.println("policyInfoExt:");
    		for (String policy: SignatureUtils.getPolicyInfoExt(cert)) {
    			System.out.println(policy);
    		}
    		SignatureUtils.verifyCertificates(new X509Certificate[] {cert}, null, null);
		} catch (Exception e) {
			log.error("Cannot process signed xml!", e);
		}
    }
}
