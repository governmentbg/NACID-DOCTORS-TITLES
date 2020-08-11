package com.nacid.esign;

import org.apache.log4j.Logger;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.utils.Constants;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.security.x509.X500Name;

import javax.security.auth.x500.X500Principal;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignatureUtils {

    public static String OID_EMAIL = "1.2.840.113549.1.9.1";
	public static final int DIRECTORY_NAME = 4;
	public static final String REG_EXP_ALT_BULSTAT = ".*(OID\\.2\\.5\\.4\\.10\\.100\\.1\\.1=)(\\d{9}(\\d{4})?)\\D.*";
	public static final String REG_EXP_ALT_EGN = ".*(OID\\.2\\.5\\.4\\.3\\.100\\.1\\.1=)(\\d{10})\\D.*";
	public static final String REG_EXP_EGN = ".*(EGN:|EGNT:|PID:|UID=EGN)(\\d{10})\\D.*";
	public static final String REG_EXP_BULSTAT = ".*(?:BULSTAT:|B:|2\\.5\\.4\\.10\\.100\\.1\\.1=|OU=EIK)(\\d{9}(\\d{4})?)\\D.*";
    
    private static Logger log = Logger.getLogger(SignatureUtils.class);
    private static List<X509Certificate> caList = new ArrayList<X509Certificate>();

	public static void verifyCertificate(X509Certificate cert, Collection<CRL> crls, Calendar calendar) throws CertificateException {
        if (calendar == null)
            calendar = new GregorianCalendar();
        if (cert.hasUnsupportedCriticalExtension())
            throw new CertificateException("Has unsupported critical extension");
        cert.checkValidity(calendar.getTime());
        if (crls != null) {
            for (CRL crl : crls) {
                if (crl.isRevoked(cert))
                	throw new CertificateException("Certificate revoked");
            }
        }
    }

    public static void verifyCertificates(Certificate certs[], Collection<CRL> crls, Calendar calendar) throws CertificateException {
        if (calendar == null)
            calendar = new GregorianCalendar();
        for (int k = 0; k < certs.length; ++k) {
            X509Certificate cert = (X509Certificate)certs[k];
            verifyCertificate(cert, crls, calendar);
            for (X509Certificate trustedCert: caList) {
                try {
                    cert.verify(trustedCert.getPublicKey());
                    return;
                } catch (Exception e) {
                    continue;
                }
            }
        }
        throw new CertificateException("Certificate cannot be verified against the KeyStore");
    }

    public static X509Certificate verifyXMLSignature(Document doc, String baseURI) throws Exception {
		X509Certificate cert = null;
		boolean valid = false;
		
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new DSContext("ds", Constants.SignatureSpecNS));        
        XPathExpression xexpr = xpath.compile("//ds:Signature[1]");
        Element sigElement = (Element)xexpr.evaluate(doc, XPathConstants.NODE);
		
		if (sigElement == null) 
			throw new XMLSignatureException("Signature selement is not found!");

		XMLSignature signature = new XMLSignature(sigElement, baseURI);
		KeyInfo ki = signature.getKeyInfo();
		if (ki == null)
			throw new XMLSignatureException("Did not find a key info");
			
		cert = signature.getKeyInfo().getX509Certificate();
		if (cert != null) {
			valid = signature.checkSignatureValue(cert);
			if (!valid) 
				throw new XMLSignatureException("Invalid signature!");
		} else {
			PublicKey pk = signature.getKeyInfo().getPublicKey();
			if (pk != null) {
				valid = signature.checkSignatureValue(pk);
				if (!valid)
					throw new XMLSignatureException("Invalid signature!");
			} else {
				throw new XMLSignatureException("Did not find a public key, so the signature can't be checked");
			}
		}
		return cert;
    }
    
	public static X509Certificate verifyXMLSignature(InputStream is, String baseURI) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			return verifyXMLSignature(db.parse(is), baseURI);
		} finally {
			try {is.close();} catch(IOException ignore) {}
		}

	}
	
	public static X509Certificate verifyXMLSignature(File file) throws Exception {
		return verifyXMLSignature(new FileInputStream(file), file.toURL().toString());
	}
	
	public static X509Certificate loadCert(String filePath) throws FileNotFoundException, CertificateException {
		InputStream is = null;
		X509Certificate cert = null;

		try {
			is = new FileInputStream(new File(filePath));
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate) cf.generateCertificate(is);
		} finally {
			if (is != null) try {is.close();} catch (Exception ignore) {}
		}

		return cert;
	}
    
    
	public static KeyStore loadKeystore(String path, String provider, String pass) throws Exception {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			KeyStore keystore = (provider == null) ? KeyStore.getInstance("JKS") : KeyStore.getInstance("JKS", provider);
			keystore.load(is, pass != null ? pass.toCharArray() : null);
			return keystore;
		} finally {
			if (is != null) try {is.close();} catch (Exception ignore) {}
		}
	}

	public static X509CRL loadCRL(URL url) throws IOException, CertificateException, CRLException {
		X509CRL crl = null;
		InputStream is = null;

		try {
			is = url.openStream();
			crl = loadCRL(is);
		} finally {
			if (is != null) try {is.close();} catch (Exception ignore) {}
		}
		return crl;
	}

	public static X509CRL loadCRLFile(String filePath) throws FileNotFoundException, CertificateException, CRLException {
		return loadCRL(new FileInputStream(filePath));
	}
	
	public static X509CRL loadCRL(InputStream is) throws  CRLException, CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509CRL)cf.generateCRL(is);
	}
	
	public static URL getCrlDistributionPoint(X509Certificate certificate) throws CertificateParsingException {
		try {
			DERObject obj = getExtensionValue(certificate, X509Extensions.CRLDistributionPoints.getId());
			if (obj == null)
				return null;
			ASN1Sequence distributionPoints = (ASN1Sequence) obj;
			for (int i = 0; i < distributionPoints.size(); i++) {
				ASN1Sequence distrPoint = (ASN1Sequence) distributionPoints.getObjectAt(i);
				for (int j = 0; j < distrPoint.size(); j++) {
					ASN1TaggedObject tagged = (ASN1TaggedObject) distrPoint.getObjectAt(j);
					if (tagged.getTagNo() == 0) {
						String url = getStringFromGeneralNames(tagged.getObject());
						if (url != null)
							return new URL(url);
					}
				}
			}
		} catch (Exception e) {
			log.error("Cannot get CRL distribution point");
			throw new CertificateParsingException(e);
		}
		return null;
	}
	

	private static DERObject getExtensionValue(X509Certificate cert, String oid)	throws IOException {
		byte[] bytes = cert.getExtensionValue(oid);
		if (bytes == null) return null;
		
		ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
		ASN1OctetString octs = (ASN1OctetString) aIn.readObject();
		aIn = new ASN1InputStream(new ByteArrayInputStream(octs.getOctets()));
		return aIn.readObject();
	}
	
	private static String getStringFromGeneralNames(DERObject names) {
		ASN1Sequence namesSequence = ASN1Sequence.getInstance((ASN1TaggedObject)names, false);
		if (namesSequence.size() == 0) 
			return null;
		DERTaggedObject taggedObject = (DERTaggedObject)namesSequence.getObjectAt(0);
		return new String(ASN1OctetString.getInstance(taggedObject, false).getOctets());
    }	
	
	public static String getAltSubject(X509Certificate cert) throws CertificateParsingException {
		String subj = null;
		Collection<List<?>> cl = cert.getSubjectAlternativeNames();
		if (cl != null) {
			for (List<?> lst : cl) {
				Integer id = (Integer)lst.get(0);
				if (id != null && id.intValue() == DIRECTORY_NAME) {
					X500Principal princ = new X500Principal((String)lst.get(1));
					subj = princ.getName(X500Principal.RFC1779);
				}
			}
		}
		return subj;
	}

	public static String getEgn(X509Certificate cert) throws CertificateParsingException {
		String egn = null;
		Pattern p = null;
		String subject = getAltSubject(cert);
		if (subject != null) {
			p = Pattern.compile(REG_EXP_ALT_EGN);
            Matcher m = p.matcher(subject);
            if (m.matches()) {
                egn = m.group(2);
            }
		}

        if (egn == null) {
            subject = cert.getSubjectDN().getName();
            p = Pattern.compile(REG_EXP_EGN);
            if (subject != null) {
                Matcher m = p.matcher(subject);
                if (m.matches()) {
                    egn = m.group(2);
                }
            }
        }

		if (egn == null) {
			log.debug("egn cannot be extracted");
		}

		return egn;
	}

	
	public static String getBulstat(X509Certificate cert) throws CertificateParsingException {
		String bulstat = null;
		String subject = cert.getSubjectDN().getName();			
		Pattern p = Pattern.compile(REG_EXP_BULSTAT, Pattern.DOTALL);
		int group = 1;
		Matcher m = p.matcher(subject);
		if (m.matches()) {
			bulstat = m.group(group);
		} else {
			log.debug("bulstat cannot be extracted");
		}
		return bulstat;
	}

	public static String getIssuerName(X509Certificate cert) throws IOException {
        String issuerDN = cert.getIssuerX500Principal().getName();
        return new X500Name(issuerDN).getCommonName();
    }

    public static String getEmail(X509Certificate cert) throws IOException {
        String email = null;
        email = getOIDValue(cert, OID_EMAIL);
        return email;
    }

    public static String getCommonName(X509Certificate cert) throws IOException {
        String subject = cert.getSubjectX500Principal().getName();
        return new X500Name(subject).getCommonName();
    }

    public static String getOIDValue(X509Certificate cert, String oid) throws IOException {
        X500Principal subj = cert.getSubjectX500Principal();
        byte[] bytes = subj.getEncoded();
        ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
        ASN1Sequence rdnsSeq = (ASN1Sequence)aIn.readObject();
        
        for (int i = 0; i < rdnsSeq.size(); i++) {
            DERSet set = (DERSet) rdnsSeq.getObjectAt(i);
            for (int j = 0; j < set.size(); j++) {
                ASN1Sequence seq = (ASN1Sequence)set.getObjectAt(j);
                String id = seq.getObjectAt(0).toString();
                if (id.equals(oid)) return ((DERString)seq.getObjectAt(1)).getString();
            }
        }
        return null;
    }

    public static List<String> getPolicyInfoExt(X509Certificate certificate) throws Exception{
        List<String> policyInfoExt = new ArrayList<String>();
        
        byte[] policyInformationExtensionValue = certificate.getExtensionValue(X509Extensions.CertificatePolicies.getId());
        if(policyInformationExtensionValue==null){
            throw new Exception("The issuer of the certificate is not valid!");      
        }
        ASN1InputStream asn1Input = new ASN1InputStream(policyInformationExtensionValue );
        DEROctetString policyInformationOctetString = (DEROctetString)asn1Input.readObject();
        asn1Input = new ASN1InputStream(policyInformationOctetString.getOctets());
        DERSequence transfer = (DERSequence)asn1Input.readObject();
        if(transfer==null){
            throw new Exception("The issuer of the certificate is not valid!");      
        }
        Enumeration transferList = transfer.getObjects();
        while (transferList.hasMoreElements()) {
            Object element = transferList.nextElement();
            if(element==null){
                throw new Exception("The issuer of the certificate is not valid!");                   
            }
            if(element instanceof DERSequence) {
                DERSequence derSequence = (DERSequence)element;
                Enumeration derSequenceList = derSequence.getObjects();
                while (derSequenceList.hasMoreElements()) {
                    Object derSequenceListElement = derSequenceList.nextElement();
                    if(derSequenceListElement==null){
                        throw new Exception("The issuer of the certificate is not valid!");                   
                    }
                    if(derSequenceListElement instanceof DERObjectIdentifier){
                        DERObjectIdentifier identifier = (DERObjectIdentifier)derSequenceListElement; 
                        policyInfoExt.add(identifier.toString());
                    }
                }
            }
        }
        if(policyInfoExt.isEmpty()){       
            throw new Exception("The issuer of the certificate is not valid!");
        }
        return policyInfoExt;
    }
    
	public static void configure(String keystorePath, String keystorePasswd) throws Exception{
		KeyStore ks = loadKeystore(keystorePath, null, keystorePasswd);
		Enumeration<String> aliases = ks.aliases();
		while (aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			if (ks.isCertificateEntry(alias)) {
				X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
				caList.add(cert);
			}
		}
        Provider bcProv = new BouncyCastleProvider();
        bcProv.put("Alg.Alias.Signature.SHA1with1.2.840.113549.1.1.5","SHA1WithRSAEncryption");
        bcProv.put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1withRSA", "SHA256WithRSAEncryption");
        Security.addProvider(bcProv);
		org.apache.xml.security.Init.init();
	}

    
	
}

class DSContext implements NamespaceContext{
	private String prefix;
	private String namespaceURI;
	private Map<String, Set> prefixesByURI = new HashMap<String, Set>();
	
	public DSContext(String prefix, String namespaceURI) {
		this.prefix = prefix;
		this.namespaceURI = namespaceURI;
		
		Set<String> set = new HashSet<String>();
		set.add(prefix);
		prefixesByURI.put(namespaceURI, set);
	}

	public String getNamespaceURI(String prefix) {
		return (prefix != null && prefix.equals(this.prefix)) ? namespaceURI : null; 
	}

	public String getPrefix(String namespaceURI) {
		return (namespaceURI != null && namespaceURI.equals(this.namespaceURI)) ? prefix : null;
	}

	public Iterator getPrefixes(String namespaceURI) {
	    if (namespaceURI == null)
	        throw new IllegalArgumentException("namespaceURI cannot be null");
	      if (prefixesByURI.containsKey(namespaceURI)) {
	        return ((Set) prefixesByURI.get(namespaceURI)).iterator();
	      } else {
	        return Collections.EMPTY_SET.iterator();
	      }
	}
		
}
