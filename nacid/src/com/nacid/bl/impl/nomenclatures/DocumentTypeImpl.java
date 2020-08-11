package com.nacid.bl.impl.nomenclatures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.nomenclatures.DocumentTypeRecordExtended;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
@Log4j
public class DocumentTypeImpl extends FlatNomenclatureImpl implements DocumentType {
    private boolean isIncoming;
    private boolean hasDocflowId;
    private List<Integer> docCategoryIds;
    private String documentTemplate;
    private NacidDataProvider nacidDataProvider;

    public DocumentTypeImpl(NacidDataProvider nacidDataProvider, DocumentTypeRecordExtended record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.isIncoming = record.getIsIncoming() == 1 ? true : false;
        this.hasDocflowId = record.getHasDocflowId() == 1 ? true : false;
        this.docCategoryIds = record.getDocumentCategoryIds();
        this.documentTemplate = record.getDocumentTemplate();
        this.nacidDataProvider = nacidDataProvider;
    }

    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_DOCUMENT_TYPE;
    }

    public boolean isIncoming() {
        return isIncoming;
    }

    public boolean isHasDocflowId() {
        return hasDocflowId;
    }

    public List<Integer> getDocCategoryIds() {
        return docCategoryIds;
    }

    @Override
    public String getLongName() {
        return getName() + "/" + (isIncoming() ? "вх." : "изх.");
    }

    public String getDocumentTemplate() {
        return documentTemplate;
    }


    public String getDocumentUrl() {
        String url = getDocumentTemplate();
        if (url == null) {
            return null;
        }
        if (!StringUtils.isEmpty(url) && Utils.isUrlValid(url)) {
            String attUrl = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(UtilsDataProvider.NACID_ATTACHMENTS_URL);
            if (url.startsWith(attUrl)) {
                try {
                    ObjectMapper om = new ObjectMapper();
                    NacidAttachment nacidAttachment = om.readValue(new URL(url), NacidAttachment.class);
                    return CollectionUtils.isEmpty(nacidAttachment.getDocuments()) ? null : nacidAttachment.getDocuments().get(0).getFileUrl();
                } catch (Exception e) {
                    log.error("Error reading content from " + url, e);
                    return null;
                }
            } else {
                return url;
            }
        } else {
            return null;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    protected static class NacidAttachment {
        private int id;
        @JsonProperty("group_name")
        private String groupName;
        private List<NacidAttachmentDocument> documents;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        protected static class NacidAttachmentDocument {
            private int id;
            @JsonProperty("file_url")
            private String fileUrl;

        }
    }
}
