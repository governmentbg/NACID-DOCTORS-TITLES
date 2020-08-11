package com.nacid.rest.controller;

import com.nacid.bl.NacidDataProvider;
import com.nacid.db.utils.DatabaseUtils;

public class MainController {
    public NacidDataProvider getNacidDataProvider() {
        return NacidDataProvider.getNacidDataProvider(DatabaseUtils.getDataSource());
    }

}
