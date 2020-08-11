package com.nacid.regprof.web.model.applications.report.base;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;

//RayaWritten------------------------------------------------------------
public class RegprofProfessionExperienceForReportBaseWebModel {
    protected List<RegprofProfExperienceDocumentForReportBaseWebModel> documents;
    protected String days;
    protected String months;
    protected String years;
    protected String nomenclatureProfessionExperience;
    protected String dateCreated;
    protected String experiencePeriod;
    
    public RegprofProfessionExperienceForReportBaseWebModel(RegprofProfessionExperienceRecord record, NacidDataProvider nacidDataProvider){
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        documents = new ArrayList<RegprofProfExperienceDocumentForReportBaseWebModel>();
        if(record != null){
            for(RegprofProfessionExperienceDocumentRecord doc : record.getProfessionExperienceDocuments()){
                if(doc != null){
                    documents.add(new RegprofProfExperienceDocumentForReportBaseWebModel(doc, nacidDataProvider));
                }
            }

            days = record.getDays() + "";
            months = record.getMonths() + "";
            years = record.getYears() + "";
            FlatNomenclature pe = null;
            if(record.getNomenclatureProfessionExperienceId()!= null){
                pe = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, record.getNomenclatureProfessionExperienceId());
            }
            nomenclatureProfessionExperience = pe != null ? pe.getName() : null;
            dateCreated = DataConverter.formatDate(record.getDateCreated());
            experiencePeriod = record.getDays()+(record.getDays() != 1 ? " дни, ": " ден, ")+record.getMonths()+(record.getMonths() != 1 ? " месеца, " : " месец, ")+
                    record.getYears()+(record.getYears() != 1 ? " години ": " година ");
            //TODO: notRestricted
        }
    }

    public List<RegprofProfExperienceDocumentForReportBaseWebModel> getDocuments() {
        return documents;
    }

    public String getDays() {
        return days;
    }

    public String getMonths() {
        return months;
    }

    public String getYears() {
        return years;
    }

    public String getNomenclatureProfessionExperience() {
        return nomenclatureProfessionExperience;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getExperiencePeriod() {
        return experiencePeriod;
    }

    public void setExperiencePeriod(String experiencePeriod) {
        this.experiencePeriod = experiencePeriod;
    }
    
}
//--------------------------------------------------------------------------