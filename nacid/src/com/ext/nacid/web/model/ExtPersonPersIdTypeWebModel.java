package com.ext.nacid.web.model;

public class ExtPersonPersIdTypeWebModel {

    private int id;
    private int index;
    private String name;
    private String checked;
    public ExtPersonPersIdTypeWebModel(int id, int index, String name, boolean checked) {
        this.id = id;
        this.index = index;
        this.name = name;
        this.checked = checked ? "checked=\"checked\"" : "";
    }
    public int getId() {
        return id;
    }
    public int getIndex() {
        return index;
    }
    public String getName() {
        return name;
    }
    public String getChecked() {
        return checked;
    }
    
    
}
