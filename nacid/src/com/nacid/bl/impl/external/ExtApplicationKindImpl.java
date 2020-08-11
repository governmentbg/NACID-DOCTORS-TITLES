package com.nacid.bl.impl.external;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtApplicationKind;
import com.nacid.data.external.applications.ExtApplicationKindRecord;

import java.math.BigDecimal;

/**
 * Created by georgi.georgiev on 23.09.2015.
 */
public class ExtApplicationKindImpl implements ExtApplicationKind {
    private ExtApplicationKindRecord record;
    private NacidDataProvider nacidDataProvider;
    public ExtApplicationKindImpl(NacidDataProvider nacidDataProvider, ExtApplicationKindRecord record) {
        this.record = record;
        this.nacidDataProvider = nacidDataProvider;
    }
    public Integer getId() {
        return record.getId();
    }
    public int getApplicationId() {
        return record.getApplicationId();
    }
    public int getEntryNumSeriesId() {
        return record.getEntryNumSeriesId();
    }
    public String getEntryNumSeriesName() {
        return nacidDataProvider.getNumgeneratorDataProvider().getEntryNumSeriesNameById(record.getEntryNumSeriesId());
    }
    public BigDecimal getPrice() {
        return record.getPrice();
    }
    public String getEntryNum() {
        return record.getEntryNum();
    }
}
