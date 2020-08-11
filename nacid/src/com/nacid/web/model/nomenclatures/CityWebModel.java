package com.nacid.web.model.nomenclatures;

import com.nacid.bl.nomenclatures.City;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
public class CityWebModel {
    private String id;
    private String code;
    private String name;

    public CityWebModel(City c) {
        this.id  = c.getId() + "";
        this.code = c.getCode();
        this.name = c.getName();
    }
    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
