package com.nacid.bl.inquires;

import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 18.3.2020 г.
 * Time: 16:27
 */
public interface ExpertInquireResult {
    public interface ExpertInquireApplication {
        int getId();
        String getAppNum();
        Date getAppDate();
    }
    public int getExpertId();
    public String getExpertNames();
    public List<ExpertInquireApplication> getApplications();
}
