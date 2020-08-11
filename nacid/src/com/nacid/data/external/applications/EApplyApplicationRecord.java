package com.nacid.data.external.applications;

import com.nacid.bl.external.applications.EApplyApplication;

import java.util.Date;
import java.util.List;

/**
 * Created by georgi.georgiev on 27.08.2015.
 */
public class EApplyApplicationRecord implements EApplyApplication {
    private int id;
    private String names;
    private int esigned;
    private List<Integer> applicationKinds;
    private Date timeOfCreation;
    private int applicationStatus;
    private int communicated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public int getEsigned() {
        return esigned;
    }

    public void setEsigned(int esigned) {
        this.esigned = esigned;
    }

    public List<Integer> getApplicationKinds() {
        return applicationKinds;
    }

    public void setApplicationKinds(List<Integer> applicationKinds) {
        this.applicationKinds = applicationKinds;
    }

    public Date getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(Date timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public int getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(int applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Override
    public boolean isEsigned() {
        return false;
    }

    public int getCommunicated() {
        return communicated;
    }

    public void setCommunicated(int communicated) {
        this.communicated = communicated;
    }

    @Override
    public boolean isCommunicated() {
        return communicated == 1;
    }
}
