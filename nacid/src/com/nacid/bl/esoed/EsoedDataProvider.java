package com.nacid.bl.esoed;

import org.apache.axiom.om.OMElement;

public interface EsoedDataProvider {
	public static final String ESOED_MESSAGE_OUT = "OUT";
	public static final String ESOED_MESSAGE_IN = "IN";
	/*public int insertMessage(String inOut, String transport, String envelopeVersion, String messageType, String senderUri,
			String documentUri, String documentTypeUri, String recipientUri, String content, String esoedSessionId, String eserviceUri,
			String esoedCorrelationId, String originatorExtraData, String sessionId, String correlationId, String receiveTime);*/
	public EsoedMessage createEsoedMessage(OMElement document) throws EsoedException;
	
	public EsoedMessage createEsoedMessage(String documentString) throws EsoedException;
	
	public void saveEsoedMessage(String inOut, EsoedMessage msg) throws EsoedException;
	
	public void updateFromResponse(EsoedMessage msg, EsoedMessage response) throws EsoedException;
	
	public void updateFromReceipt(EsoedMessage msg) throws EsoedException;
}
