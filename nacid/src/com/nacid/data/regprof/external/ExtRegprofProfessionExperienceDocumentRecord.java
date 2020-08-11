package com.nacid.data.regprof.external;

import com.nacid.data.annotations.Table;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;
import org.springframework.util.AutoPopulatingList;

import java.util.List;
@Table(name="eservices.regprof_profession_experience_documents")
public class ExtRegprofProfessionExperienceDocumentRecord extends RegprofProfessionExperienceDocumentRecord {
    public ExtRegprofProfessionExperienceDocumentRecord() {
        dates = new AutoPopulatingList<ExtRegprofProfessionExperienceDatesRecord>(ExtRegprofProfessionExperienceDatesRecord.class);
    }

    public List<? extends ExtRegprofProfessionExperienceDatesRecord> getDates() {
        return (List<? extends ExtRegprofProfessionExperienceDatesRecord>)dates;
    }
    
}
