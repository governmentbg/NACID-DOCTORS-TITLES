package com.nacid.data.esoed;

import java.sql.Timestamp;

public class EsoedMessageRecord {
	private int id;
	private String inOut;
	private String transport;
	private String envelopeVersion;
	private String messageType;
	private String senderUri;
	private String documentUri;
	private String documentTypeUri;
	private String recipientUri;
	private String content;
	private String esoedSessionId;
	private String eserviceUri;
	private String esoedCorrelationId;
	private String originatorExtraData;
	private String sessionId;
	private String correlationId;
	private Timestamp receiveTime;
	private String sendStatus = "PEN";
	private Integer errors = 0;
	private Timestamp sendDate;
	private Timestamp receiptDate;
	private String processStatus = "PEN";
	private Timestamp processDate;
	public EsoedMessageRecord() {}
	
	public EsoedMessageRecord(int id, String inOut, String transport, String envelopeVersion, String messageType, String senderUri,
			String documentUri, String documentTypeUri, String recipientUri, String content, String esoedSessionId, String eserviceUri,
			String esoedCorrelationId, String originatorExtraData, String sessionId, String correlationId, Timestamp receiveTime) {
		this.id = id;
		this.inOut = inOut;
		this.transport = transport;
		this.envelopeVersion = envelopeVersion;
		this.messageType = messageType;
		this.senderUri = senderUri;
		this.documentUri = documentUri;
		this.documentTypeUri = documentTypeUri;
		this.recipientUri = recipientUri;
		this.content = content;
		this.esoedSessionId = esoedSessionId;
		this.eserviceUri = eserviceUri;
		this.esoedCorrelationId = esoedCorrelationId;
		this.originatorExtraData = originatorExtraData;
		this.sessionId = sessionId;
		this.correlationId = correlationId;
		this.receiveTime = receiveTime;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInOut() {
		return inOut;
	}
	public void setInOut(String inOut) {
		this.inOut = inOut;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getEnvelopeVersion() {
		return envelopeVersion;
	}
	public void setEnvelopeVersion(String envelopeVersion) {
		this.envelopeVersion = envelopeVersion;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getSenderUri() {
		return senderUri;
	}
	public void setSenderUri(String senderUri) {
		this.senderUri = senderUri;
	}
	public String getDocumentUri() {
		return documentUri;
	}
	public void setDocumentUri(String documentUri) {
		this.documentUri = documentUri;
	}
	public String getDocumentTypeUri() {
		return documentTypeUri;
	}
	public void setDocumentTypeUri(String documentTypeUri) {
		this.documentTypeUri = documentTypeUri;
	}
	public String getRecipientUri() {
		return recipientUri;
	}
	public void setRecipientUri(String recipientUri) {
		this.recipientUri = recipientUri;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEsoedSessionId() {
		return esoedSessionId;
	}
	public void setEsoedSessionId(String esoedSessionId) {
		this.esoedSessionId = esoedSessionId;
	}
	public String getEserviceUri() {
		return eserviceUri;
	}
	public void setEserviceUri(String eserviceUri) {
		this.eserviceUri = eserviceUri;
	}
	public String getEsoedCorrelationId() {
		return esoedCorrelationId;
	}
	public void setEsoedCorrelationId(String esoedCorrelationId) {
		this.esoedCorrelationId = esoedCorrelationId;
	}
	public String getOriginatorExtraData() {
		return originatorExtraData;
	}
	public void setOriginatorExtraData(String originatorExtraData) {
		this.originatorExtraData = originatorExtraData;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public Integer getErrors() {
		return errors;
	}
	public void setErrors(Integer errrors) {
		this.errors = errrors;
	}

	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	public Timestamp getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Timestamp receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public Timestamp getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Timestamp processDate) {
		this.processDate = processDate;
	}



}
