package com.ext.nacid.web.model.applications;

import com.nacid.bl.external.ExtApplicationKind;
import com.nacid.data.DataConverter;

/**
 * Created by georgi.georgiev on 23.09.2015.
 */
public class ExtApplicationKindWebModel {
    private String entryNumSeriesName;
    private String price;
    private String entryNum;

    public ExtApplicationKindWebModel(ExtApplicationKind applicationKind) {
        this.entryNumSeriesName = applicationKind.getEntryNumSeriesName();
        this.entryNum = applicationKind.getEntryNum();
        this.price = DataConverter.formatDouble(applicationKind.getPrice() == null ? null : applicationKind.getPrice().doubleValue(), 2);
    }

    public String getEntryNumSeriesName() {
        return entryNumSeriesName;
    }

    public String getPrice() {
        return price;
    }

    public String getEntryNum() {
        return entryNum;
    }
}
