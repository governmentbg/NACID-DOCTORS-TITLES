package com.nacid.web.handlers.impl.esoed;


import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.esoed.EsoedDataProvider;
import com.nacid.bl.esoed.EsoedMessage;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.esoed.EsoedDocumentImpl;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.esoed.webservice.EsoedSendMessageUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.apache.axiom.om.util.AXIOMUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;


public class EsoedServiceSenderHandler  extends NacidBaseRequestHandler {

	public EsoedServiceSenderHandler(ServletContext servletContext) {
		super(servletContext);
	}
	
	public EsoedServiceSenderHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
		super(nacidDataProvider, groupId, servletContext);
	}



	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		EsoedSendMessageUtils.sendMessage(getNacidDataProvider(), "1.0", "test", "0002-94", "0000-83", "000R-02", "0002-94", "<testMessage><data>DDD</data></testMessage>");
	}

	public static void main(String[] args) {
		
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		EsoedDataProvider esoedDataProvider = nacidDataProvider.getEsoedDataProvider();
		EsoedMessage msg = new EsoedDocumentImpl();
		msg.setEnvelopeVersion("1.0");
		msg.setOriginatorExtraData("test");
		msg.setSenderURI("0002-94");
		msg.setDocumentURI("0000-83");
		msg.setDocumentTypeURI("000R-02");
		msg.setRecipientURI("0002-94");
		try {
			msg.setBody(AXIOMUtil.stringToOM("<testMessage><data>xxx</data></testMessage>"));
		} catch (XMLStreamException e) {
			throw Utils.logException(e);
		}
		System.out.println(msg.asXMLString());
		
		new EsoedServiceSenderHandler(nacidDataProvider, -1, null).processRequest(null, null);
	}

}
