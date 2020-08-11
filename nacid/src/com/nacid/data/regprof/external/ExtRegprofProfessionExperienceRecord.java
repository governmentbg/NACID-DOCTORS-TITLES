package com.nacid.data.regprof.external;

import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import org.springframework.util.AutoPopulatingList;

import java.util.List;
public class ExtRegprofProfessionExperienceRecord extends RegprofProfessionExperienceRecord {
    private String professionExperienceTxt;
    public ExtRegprofProfessionExperienceRecord() {
        professionExperienceDocuments = new AutoPopulatingList<ExtRegprofProfessionExperienceDocumentRecord>(ExtRegprofProfessionExperienceDocumentRecord.class);   
    }
    
    public String getProfessionExperienceTxt() {
        return professionExperienceTxt;
    }
    public void setProfessionExperienceTxt(String professionExperienceTxt) {
        this.professionExperienceTxt = professionExperienceTxt;
    }
    
    public List<? extends ExtRegprofProfessionExperienceDocumentRecord> getProfessionExperienceDocuments() {
        return (List<? extends ExtRegprofProfessionExperienceDocumentRecord>) professionExperienceDocuments;
    }
}
