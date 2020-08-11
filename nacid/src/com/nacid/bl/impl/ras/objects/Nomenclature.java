package com.nacid.bl.impl.ras.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: ggeorgiev
 * Date: 23.10.2019 г.
 * Time: 17:53
 */
public class Nomenclature {
    private int id;
    private String code;
    private String nameAlt;
    private Integer parentId;
    @JsonProperty("_ExternalId")
    private Integer externalId;
    private String name;
    @JsonProperty("isActive")
    private boolean active;
    private Integer viewOrder;
    private Integer version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameAlt() {
        return nameAlt;
    }

    public void setNameAlt(String nameAlt) {
        this.nameAlt = nameAlt;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getExternalId() {
        return externalId;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getViewOrder() {
        return viewOrder;
    }

    public void setViewOrder(Integer viewOrder) {
        this.viewOrder = viewOrder;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Nomenclature{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nameAlt='" + nameAlt + '\'' +
                ", parentId=" + parentId +
                ", externalId=" + externalId +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", viewOrder=" + viewOrder +
                ", version=" + version +
                '}';
    }
}
