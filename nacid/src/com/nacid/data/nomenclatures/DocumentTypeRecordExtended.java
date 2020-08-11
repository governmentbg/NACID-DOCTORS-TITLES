package com.nacid.data.nomenclatures;

import java.util.List;

public class DocumentTypeRecordExtended extends DocumentTypeRecord {
    private List<Integer> documentCategoryIds;

    public List<Integer> getDocumentCategoryIds() {
        return documentCategoryIds;
    }

    public void setDocumentCategoryIds(List<Integer> documentCategoryIds) {
        this.documentCategoryIds = documentCategoryIds;
    }
}
