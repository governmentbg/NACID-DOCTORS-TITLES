package com.nacid.regprof.web.model.applications.report.base;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.regprof.RegprofProfessionExperienceDatesRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;

//RayaWritten---------------------------------------------------------
public class RegprofProfExperienceDocumentForReportBaseWebModel {
    protected List<RegprofProfExpeienceDatesForReportBaseWebModel> dates;
    protected boolean datesCount;
    protected String documentDate;
    protected String documentIssuer;
    protected String documentNumber;
    protected boolean forExperienceCalculaion;
    protected String profExperienceDocType;
    protected String profExperienceId;
    protected String id;


    public RegprofProfExperienceDocumentForReportBaseWebModel(RegprofProfessionExperienceDocumentRecord document, NacidDataProvider nacidDataProvider){
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        dates = new ArrayList<RegprofProfExpeienceDatesForReportBaseWebModel>();
        for(RegprofProfessionExperienceDatesRecord ped : document.getDates()){
            if(ped != null){
                dates.add(new RegprofProfExpeienceDatesForReportBaseWebModel(ped));
            }
        }
        datesCount = document.getForExperienceCalculation() == 1 ? true : false;
        FlatNomenclature exprDocType = null;
        if(document.getProfExperienceDocTypeId() != null){
            exprDocType = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE, document.getProfExperienceDocTypeId());
        }
        profExperienceDocType = exprDocType != null ? exprDocType.getName() : "";
        documentIssuer = document.getDocumentIssuer();
        documentNumber = document.getDocumentNumber();
        //documentDate = DataConverter.formatDate(document.getDocumentDate());
        documentDate = document.getDocumentDate();
        forExperienceCalculaion = document.getForExperienceCalculation() == 1 ? true : false;
        id = document.getId()+""; 
        profExperienceId = document.getProfExperienceId()+"";
    }


    public List<RegprofProfExpeienceDatesForReportBaseWebModel> getDates() {
        return dates;
    }


    public boolean getDatesCount() {
        return datesCount;
    }


    public String getDocumentDate() {
        return documentDate;
    }


    public String getDocumentIssuer() {
        return documentIssuer;
    }


    public String getDocumentNumber() {
        return documentNumber;
    }


    public boolean isForExperienceCalculaion() {
        return forExperienceCalculaion;
    }


    public String getProfExperienceDocType() {
        return profExperienceDocType;
    }


    public String getId() {
        return id;
    }


    public String getProfExperienceId() {
        return profExperienceId;
    }
    

}
//-----------------------------------------------------------------------
