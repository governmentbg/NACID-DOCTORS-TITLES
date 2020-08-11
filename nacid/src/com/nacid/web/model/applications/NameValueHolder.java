package com.nacid.web.model.applications;

public class NameValueHolder {

    private String name;
    private String value;
    private String selected;
    private String index;
    
    public NameValueHolder() {
        
    }
    
    
    
    public NameValueHolder(String name, String value, String selected, String index) {
        super();
        this.name = name;
        this.value = value;
        this.selected = selected;
        this.index = index;
    }



    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public String getSelected() {
        return selected;
    }
    public void setSelected(String selected) {
        this.selected = selected;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    
}
