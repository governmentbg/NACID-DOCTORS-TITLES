package com.nacid.web.handlers.impl.esoed;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

import com.nacid.bl.esoed.EsoedDataProvider;
import com.nacid.bl.esoed.EsoedException;
import com.nacid.bl.esoed.EsoedMessage;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.esoed.EsoedDocumentImpl;
import com.nacid.bl.impl.esoed.EsoedDocumentReceiptImpl;
import com.nacid.bl.impl.esoed.EsoedErrorImpl;
import com.nacid.esoed.webservice.EsoedSendMessageUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.PrintRequestHandler;

public class EsoedServiceReceiverHandler extends NacidBaseRequestHandler {

	public EsoedServiceReceiverHandler(ServletContext servletContext) {
		super(servletContext);
	}
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			//Prints the "request" to the console....
			new PrintRequestHandler(getServletContext()).processRequest(request, response);
			
			
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLStreamReader streamReader = xmlInputFactory.createXMLStreamReader(request.getInputStream());
			StAXOMBuilder builder = new StAXOMBuilder(streamReader);
	        OMElement element = builder.getDocumentElement();
			
	        String docString = element.getFirstChildWithName(new QName(EsoedSendMessageUtils.NS_SOAP,"message")).getText();
	        ByteArrayInputStream is = new ByteArrayInputStream( docString.getBytes("UTF-8") );
	        //create the parser
	        XMLStreamReader parser = xmlInputFactory.createXMLStreamReader(is);
	        builder = new StAXOMBuilder(parser);
	        OMElement document =  builder.getDocumentElement();

	        
	        EsoedMessage msg = getNacidDataProvider().getEsoedDataProvider().createEsoedMessage(document);
	        System.out.println("RECEIVED:" + msg);
	        getNacidDataProvider().getEsoedDataProvider().saveEsoedMessage(EsoedDataProvider.ESOED_MESSAGE_IN, msg);
	        System.out.println("After save..." + msg);
	        


	        //Return OMElement
	        OMFactory fac      = OMAbstractFactory.getOMFactory();
	        OMNamespace omNsSOAP = fac.createOMNamespace(EsoedSendMessageUtils.NS_SOAP,"");
	        OMNamespace omNs     = fac.createOMNamespace(EsoedSendMessageUtils.NS_ESOED, "");
	        OMElement result = fac.createOMElement("SubmitResponse", omNsSOAP);
	        OMElement method   = fac.createOMElement("SubmitResult", omNsSOAP);
	        result.addChild(method);

	        if( msg instanceof EsoedDocumentReceiptImpl ) {
	        	//
	        	// mark document as received
	           // msg.updateFromReceipt();
	        } else if ( msg instanceof EsoedErrorImpl ){
	        } else {
	        	/**
	        	 *TODO: 
	        	 * 1. checks if the document is not received for second(third and so on) time. if True - does not process the message
	        	 * 2. if document is received for the first time - saves it
	        	 * 3. constructs and sends DocumentReceipt
	        	 * 4. Processes the received message - how?  
	        	 */
	        	EsoedMessage resp = new EsoedDocumentReceiptImpl( msg );
	            OMElement xml = resp.asXML();
	            method.setText(xml.toString());
	            getNacidDataProvider().getEsoedDataProvider().saveEsoedMessage(EsoedDataProvider.ESOED_MESSAGE_OUT, resp);
	            
	            
	        }
	        if ( msg instanceof EsoedDocumentImpl ){
	            //((EsoedDocumentImpl) msg).process();//TODO
	        }
		} catch (XMLStreamException e) {
			throw Utils.logException(e);
		} catch (IOException e) {
			throw Utils.logException(e);
		} catch (EsoedException e) {
			throw Utils.logException(e);
		}
		
		
		
	}

}
