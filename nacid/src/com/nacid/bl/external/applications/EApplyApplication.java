package com.nacid.bl.external.applications;

import java.util.Date;
import java.util.List;

/**
 * Created by georgi.georgiev on 27.08.2015.
 */
public interface EApplyApplication {
    public int getId();
    public String getNames();
    public boolean isEsigned();
    public List<Integer> getApplicationKinds();
    public Date getTimeOfCreation();
    public int getApplicationStatus();
    boolean isCommunicated();

}
