package com.nacid.esoed.webservice;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.esoed.EsoedDataProvider;
import com.nacid.bl.esoed.EsoedException;
import com.nacid.bl.esoed.EsoedMessage;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.esoed.EsoedDocumentImpl;
import com.nacid.bl.utils.UtilsDataProvider;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.MessageContext;

import javax.xml.stream.XMLStreamException;

public class EsoedSendMessageUtils {
	public static String NS_SOAP = "http://esoed.egov.bg/2008/05/SoapEntryPoint/v1";
	public static String NS_SIGN = "http://esoed.egov.bg/2008/05/Utility/v1";
	public static final String NS_ESOED = "http://esoed.egov.bg/2008/05/Envelope/v1";
	
	/**
	 * @param nacidDataProvider
	 * @param envelopeVersion
	 * @param originatorExtraData - celta na tazi informaciq e da identificira syob6tenieto (t.e. ako ima nqkolko izprashtaniq/polu4avaniq/ svyrzani s edin dokument, to tozi string da e edin i sy6t!)
	 * @param senderUri - URI na izprashta4a
	 * @param documentUri - URI na dokumenta 
	 * @param documentTypeUri 
	 * @param recipientUri - URI na polu4atelq
	 * @param bodyContent - sydyrjanie na syob6tenieto, koeto trqbva da se transportira
	 */
	public static void sendMessage(NacidDataProvider nacidDataProvider, String envelopeVersion, String originatorExtraData, String senderUri, String documentUri, String documentTypeUri, String recipientUri, String bodyContent) {
		try {
			EsoedMessage msg = new EsoedDocumentImpl();
			msg.setEnvelopeVersion(envelopeVersion);
			msg.setOriginatorExtraData(originatorExtraData);
			msg.setSenderURI(senderUri);
			msg.setDocumentURI(documentUri);
			msg.setDocumentTypeURI(documentTypeUri);
			msg.setRecipientURI(recipientUri);
			msg.setBody(AXIOMUtil.stringToOM(bodyContent));
			send(nacidDataProvider, msg);
		} catch (XMLStreamException e) {
			throw Utils.logException(e);
		} catch (AxisFault e) {
			throw Utils.logException(e);
		}
	}
	
	
	private static void invokeNonBlocking(String content, String targetUrl) throws AxisFault {
		EndpointReference targetEPR = new EndpointReference(targetUrl);
		Options options = new Options();
		options.setTo(targetEPR);
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		//options.setUseSeparateListener(true);
		options.setAction("Submit");
		options.setSoapVersionURI(Constants.URI_SOAP12_ENV);

		//AxisCallback to handle the response
		AxisCallback callback = new AxisCallback() {

			//Like finally
			public void onComplete() {
				System.out.println("onComplete");
			}

			public void onError(Exception e) {
				throw Utils.logException(e);
			}


			public void onFault(MessageContext msgContext) {
				System.out.println("onFaultMessage");
			}

			public void onMessage(MessageContext msgContext) {
				System.out.println("Received message SOAPaction = " + msgContext.getSoapAction());

			}

		};
		//Non-Blocking Invocation
		ServiceClient sender = new ServiceClient();
		sender.setOptions(options);
		sender.sendReceiveNonBlocking(createLoad(content), callback);
		//Wait till the callback receives the response.

	}


	private static OMElement createLoad(String text) {

		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(NS_SOAP,"ns");

		OMElement method = fac.createOMElement("Submit", omNs);
		OMElement value = fac.createOMElement("message", omNs);
		value.setText(text);
		method.addChild(value);

		return method;
	}


	private static String signMessage(String msg, String outCode) throws AxisFault{

		String signedXML= null;
		String encryptedXML= null;
		System.out.println("Invoking signMessage" );
		String target = (String) "Reference.getInstance().map(ESOED_SIGN_URL,outCode)";//TODO

		EndpointReference targetEPR = new EndpointReference(target);
		ServiceClient sc = new ServiceClient();
		Options opt = new Options();
		opt.setTo(targetEPR);
		//opt.setAction("Sign");
		//opt.setAction("Submit");
		opt.setSoapVersionURI(Constants.URI_SOAP11_ENV);
		sc.setOptions(opt);
		//OMElement res = sc.sendReceive(createLoad(content));

		//QName operation = new QName("Submit");
		//OMElement res = sc.sendReceive(operation, createLoad(content));



		//OMElement res = sc.sendReceive(createLoad(msg));
		OMFactory fac = null;
		OMNamespace omNs = null;
		OMElement method = null;
		OMElement value = null;
		OMElement res  = null;

		fac = OMAbstractFactory.getOMFactory();
		omNs= fac.createOMNamespace(NS_SIGN,"ns");
		method = fac.createOMElement("Sign", omNs);
		value = fac.createOMElement("message", omNs);
		value.setText(msg);
		method.addChild(value);
		res = sc.sendReceive(method);
		signedXML = res.getFirstElement().getText();
		//System.out.println("Signed XML = "+signedXML);


		fac = OMAbstractFactory.getOMFactory();
		omNs= fac.createOMNamespace(NS_SIGN,"ns");
		method = fac.createOMElement("Encrypt", omNs);
		value = fac.createOMElement("message", omNs);
		value.setText(msg);
		method.addChild(value);
		res = sc.sendReceive(method);
		res = sc.sendReceive(method);
		encryptedXML = res.getFirstElement().getText();
		//System.out.println("Encrypted XML = " + encryptedXML);

		return encryptedXML;
	}
	
	
	/** Sends EsoedMessage and processes response message.
	 * This method assumes that the request message is already saved in database by calling request.save("OUT") method.
	 * @param  request - message to be send
	 */
	private static void send(NacidDataProvider nacidDataProvider, EsoedMessage request) throws AxisFault {
		ServiceClient sc = new ServiceClient();
		Options opt = new Options();
		EsoedDataProvider esoedDataProvider = nacidDataProvider.getEsoedDataProvider();
		try {
			esoedDataProvider.saveEsoedMessage(EsoedDataProvider.ESOED_MESSAGE_OUT, request);
			String target = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(UtilsDataProvider.ESOED_SEND_URL);

			
			EndpointReference targetEPR = new EndpointReference(target);
			opt.setTo(targetEPR);
			opt.setSoapVersionURI(Constants.URI_SOAP12_ENV);
			sc.setOptions(opt);

			System.out.println("Sending  "+request.asXMLString() );
			OMElement res = sc.sendReceive( createLoad(request.asXMLString()) );
			
			EsoedMessage response = esoedDataProvider.createEsoedMessage( res.getFirstElement().getText() );
			request.setEsoedSessionId( response.getEsoedSessionId() );
			request.setEsoedCorrelationId( response.getEsoedCorrelationId() );

			esoedDataProvider.saveEsoedMessage(EsoedDataProvider.ESOED_MESSAGE_IN, response);
			// mark sent message status according to response
			esoedDataProvider.updateFromResponse(request, response);

		} catch (EsoedException e){
			e.printStackTrace();
			System.out.println("Can not send message");
		} finally {
			sc.cleanup();
			sc.cleanupTransport();
		}
	}
	
	/** Sends EsoedMessage and processes response message.
	 * This method assumes that the request message is not saved in database (therefore it will save it before trying send by calling request.save("OUT") method).
	 * @param msg - XML message as string
	 */
	private static void send(NacidDataProvider nacidDataProvider, String msg) throws AxisFault, EsoedException  {
		EsoedDataProvider esoedDataProvider = nacidDataProvider.getEsoedDataProvider();
		EsoedMessage request = esoedDataProvider.createEsoedMessage( msg );
		send(nacidDataProvider,  request );
	}
}
