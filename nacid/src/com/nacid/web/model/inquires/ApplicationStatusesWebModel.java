package com.nacid.web.model.inquires;

import java.util.Map;
import java.util.TreeMap;

public class ApplicationStatusesWebModel {
    Map<String, Integer> applicationStatuses = new TreeMap<String, Integer>();
    public void addApplicationStatus(String statusName) {
        if (statusName != null) {
            Integer cnt = applicationStatuses.get(statusName);
            if (cnt == null) {
                applicationStatuses.put(statusName, 1);
            } else {
                applicationStatuses.put(statusName, ++cnt);
            }
        }
    }
    public Map<String, Integer> getApplicationStatuses() {
        return applicationStatuses;
    }
}
