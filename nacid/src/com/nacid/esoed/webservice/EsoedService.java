package com.nacid.esoed.webservice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.log4j.Logger;

import com.nacid.bl.esoed.EsoedMessage;
import com.nacid.bl.impl.esoed.EsoedDocumentImpl;
import com.nacid.bl.impl.esoed.EsoedDocumentReceiptImpl;
import com.nacid.bl.impl.esoed.EsoedErrorImpl;



public class EsoedService {
    private static final Logger log = Logger.getLogger(EsoedService.class);
    private static final String NS_SOAP  = "http://esoed.egov.bg/2008/05/SoapEntryPoint/v1";
    private static final String NS_ESOED = "http://esoed.egov.bg/2008/05/Envelope/v1";

    public OMElement Submit(OMElement content){
        OMElement    result   = null;
        OMElement    document = null;
        InputStream  is = null;
        String                esoedRequestMessageString  = null;

        try {
            String docString = content.getFirstChildWithName(new QName(NS_SOAP,"message")).getText();
            is = new ByteArrayInputStream( docString.getBytes("UTF-8") );
            //create the parser
            XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(is);
            StAXOMBuilder   builder = new StAXOMBuilder(parser);
            document =  builder.getDocumentElement();

            
            EsoedMessage msg = null;//EsoedFactory.createEsoedMessage( document );//TODO
//            msg.dump();
            //msg.save("IN");//TODO
            log.info("Received  "+msg.toString() );


            //Return OMElement
            OMFactory fac      = OMAbstractFactory.getOMFactory();
            OMNamespace omNsSOAP = fac.createOMNamespace(NS_SOAP,"");
            OMNamespace omNs     = fac.createOMNamespace(NS_ESOED, "");
            result   = fac.createOMElement("SubmitResponse", omNsSOAP);
            OMElement method   = fac.createOMElement("SubmitResult", omNsSOAP);
            result.addChild(method);

            if( msg instanceof EsoedDocumentReceiptImpl ) {
                // mark document as received
               // msg.updateFromReceipt();
            } else if ( msg instanceof EsoedErrorImpl ){
            } else {
                EsoedMessage response = new EsoedDocumentReceiptImpl( msg );
                OMElement xml = response.asXML();
                method.setText(xml.toString());
                //response.save("OUT");//TODO
                log.info("Response "+response.toString() );
            }
            if ( msg instanceof EsoedDocumentImpl ){
                //((EsoedDocumentImpl) msg).process();//TODO
            }
        } catch (Exception e) {
            //TODO - generate SubmitFault with elements:
            //message and code
            log.error("Unexpected exception occured ", e);
        }
        return result;
    }
}

