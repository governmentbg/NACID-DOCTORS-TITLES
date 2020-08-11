package com.nacid.web.model.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.inquires.InquiryInquireRequest;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.inquires.InquiryInquireHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class InquiryInquireWebModel extends InquiryInquireRequest {

    public InquiryInquireWebModel(HttpServletRequest request, NacidDataProvider nacidDataProvider, Integer jointDegreeFlag) {
        applicationTypeEntryNumSeries = InquiresUtils.generateApplicationTypeEntryNumSeries(request);
        documentTypeIds = InquiresUtils.generateElementList(request, "documentTypeIds", "documentType");
        //Ako documentType == null, tova ozna4ava 4e document type trqbva da vkliu4va vsi4ki document types v kategoriite Проучвания висши училища и Проучвания диплома
        if (documentTypeIds == null) {
            List<DocumentType> docTypes = InquiryInquireHandler.getDocumentTypesForInquiryInquireCombobox(nacidDataProvider);
            documentTypeIds = new ArrayList<Integer>();
            for (DocumentType dt : docTypes) {
                documentTypeIds.add(dt.getId());
            }
        }
        eventStatusIds = InquiresUtils.generateElementList(request, "eventStatusIds", "eventStatus");

        universityCountryIds = new ArrayList<>();
        universityIds = new ArrayList<>();

        if (!InquiresUtils.generateUniversityCountryAndUniversityIds(request, universityIds, universityCountryIds, NacidBaseRequestHandler.getNacidDataProvider(request.getSession())) && universityIds.size() == 0) {
            universityIds = null;
        }
        if (universityCountryIds.size() == 0) {
            universityCountryIds = null;
        }
        this.jointDegreeFlag = jointDegreeFlag;
    }
}
