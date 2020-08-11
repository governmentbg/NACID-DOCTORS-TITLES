package com.nacid.data.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by georgi.georgiev on 17.06.2016.
 */
public class ResultRow {
    private Map<String, Object> objects = new HashMap<String, Object>();
    public void addObject(String name, Object value) {
        objects.put(name, value);
    }
    public Object get(String name) {
        return objects.get(name);
    }

}
