package com.nacid.web.model.nomenclatures;

import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.data.DataConverter;

import java.util.Date;

/**
 * Created by georgi.georgiev on 14.04.2015.
 */
public class ApplicationStatusWebModel {
    private String id = "";
    private String name = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private boolean isLegal;

    public ApplicationStatusWebModel(String id, String name, String dateFrom, String dateTo, boolean isLegal) {
        this.id = id;
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.isLegal = isLegal;
    }

    public ApplicationStatusWebModel(ApplicationStatus status) {
        this.id = status.getId() + "";
        this.name = status.getName();
        this.dateFrom = status.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(status.getDateFrom());
        this.dateTo = status.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(status.getDateTo());
        this.isLegal = status.isLegal();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public boolean isLegal() {
        return isLegal;
    }
}
