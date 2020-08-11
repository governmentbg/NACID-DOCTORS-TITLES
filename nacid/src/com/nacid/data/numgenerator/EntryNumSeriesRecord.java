package com.nacid.data.numgenerator;

import com.nacid.data.annotations.Table;

/**
 * Created by georgi.georgiev on 27.08.2015.
 */
@Table(name="numgenerator.entry_num_series")
public class EntryNumSeriesRecord {
    private int id;
    private String seriesName;
    private int lastAllocated;
    private String code;
    private String archiveCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public int getLastAllocated() {
        return lastAllocated;
    }

    public void setLastAllocated(int lastAllocated) {
        this.lastAllocated = lastAllocated;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }
}
