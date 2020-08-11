package com.nacid.bl.applications.base;

import java.math.BigDecimal;

/**
 * Created by georgi.georgiev on 30.09.2015.
 */
public interface ApplicationKindBase {
    public Integer getId();
    public int getApplicationId();
    public int getEntryNumSeriesId();
    public String getEntryNumSeriesName();
    public BigDecimal getPrice();
    public String getEntryNum();
}
