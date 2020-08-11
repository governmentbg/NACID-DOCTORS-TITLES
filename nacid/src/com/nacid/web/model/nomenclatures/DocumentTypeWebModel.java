package com.nacid.web.model.nomenclatures;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.DataConverter;

public class DocumentTypeWebModel {

	private String id = "";
	private String name = "";
	private String dateFrom = DataConverter.formatDate(new Date());
	private String dateTo = "дд.мм.гггг";
	private boolean isIncoming;
	private boolean hasDocflowId;
	private List<Integer> docCategoryId;
	private Map<Integer, Integer> docCategoryIdMap;
	private String documentTemplate;
	
	public DocumentTypeWebModel(String id, String name, boolean isIncoming, boolean hasDocflowId, List<Integer> docCategoryId, String dateFrom, String dateTo, String documentTemplate) {
		this.id = id;
		this.name = name;
		this.isIncoming = isIncoming;
		this.hasDocflowId = hasDocflowId;
		this.docCategoryId = docCategoryId;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		docCategoryIdMap = docCategoryId == null ? new HashMap<>() : docCategoryId.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
		this.documentTemplate = documentTemplate;
	}
	public DocumentTypeWebModel(DocumentType documentType) {
		this.id = documentType.getId() + "";
		this.name = documentType.getName();
		this.isIncoming = documentType.isIncoming();
		this.hasDocflowId = documentType.isHasDocflowId();
		this.docCategoryId = documentType.getDocCategoryIds();
		this.dateFrom = documentType.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(documentType.getDateFrom());
		this.dateTo = documentType.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(documentType.getDateTo());
		documentTemplate = documentType.getDocumentTemplate();
		docCategoryIdMap = docCategoryId == null ? new HashMap<>() : docCategoryId.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public boolean isIncoming() {
		return isIncoming;
	}
	public boolean isHasDocflowId() {
		return hasDocflowId;
	}
	public List<Integer> getDocCategoryId() {
		return docCategoryId;
	}
	public Map<Integer, Integer> getDocCategoryIdMap() {
		return docCategoryIdMap;
	}

	public String getDocumentTemplate() {
		return documentTemplate;
	}
}
