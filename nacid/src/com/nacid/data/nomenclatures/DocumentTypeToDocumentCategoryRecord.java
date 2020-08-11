package com.nacid.data.nomenclatures;

import com.nacid.data.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User: Georgi
 * Date: 31.3.2020 г.
 * Time: 14:58
 */
@Table(name = "nomenclatures.doc_type_to_doc_category")
@Data
@AllArgsConstructor
public class DocumentTypeToDocumentCategoryRecord {
    private int id;
    private int docTypeId;
    private int docCategoryId;
}
