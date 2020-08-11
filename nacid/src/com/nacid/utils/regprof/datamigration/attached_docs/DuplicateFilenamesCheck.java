package com.nacid.utils.regprof.datamigration.attached_docs;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDetailsForList;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.db.utils.StandAloneDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class DuplicateFilenamesCheck {
    
    private DataSource nacidDataSource;

    public DuplicateFilenamesCheck(DataSource nacidDataSource) {
        this.nacidDataSource = nacidDataSource;
    }

    public void check() {
        NacidDataProvider nacidDataProvider = new NacidDataProviderImpl(nacidDataSource);
        RegprofApplicationDataProvider dp = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofApplicationAttachmentDataProvider attDP = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
        List<? extends RegprofApplicationDetailsForList> list = dp.getRegprofApplicationDetailsForList(false);
        List<Integer> applicationIds = new ArrayList<Integer>();
        for (RegprofApplicationDetailsForList app : list) {
            applicationIds.add(app.getId());
        }
        for (Integer appId : applicationIds) {
            List<RegprofApplicationAttachment> attachments = attDP.getAttachmentsForParent(appId);
            if (attachments != null && !attachments.isEmpty()) {
                List<String> fileNames = new ArrayList<String>();
                for (RegprofApplicationAttachment att : attachments) {
                    if (fileNames.contains(att.getFileName())) {
                        System.out.println("Duplicate filename " + att.getFileName() + " for application id #" + appId);
                    } else {
                        fileNames.add(att.getFileName());
                    }
                }
            }
        }
        System.out.println("Method has finished");
    }
    
    
    public static void main(String[] args) {
        DuplicateFilenamesCheck helper = new DuplicateFilenamesCheck(new StandAloneDataSource(/*"jdbc:postgresql://localhost:9000/NACID/", "postgres", "postgres"*/));
        helper.check();
    }

}
