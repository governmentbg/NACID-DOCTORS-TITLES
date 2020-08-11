package com.nacid.data.nomenclatures;

import com.nacid.data.annotations.Column;
import com.nacid.data.annotations.Table;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
@Table(name="nomenclatures.eksettlements", sequence = "nomenclatures.eksettlements_id_seq")
public class CityRecord {
    private int id;
    private String code;
    @Column(name="typename")
    private String typeName;
    private String name;
    @Column(name="districtcode")
    private String districtCode;
    @Column(name="manucipalitycode")
    private String municipalityCode;
    @Column(name="mayoraltycode")
    private String mayoraltyCode;
    @Column(name="typecode")
    private String typeCode;
    private String category;
    private String altitude;

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getMayoraltyCode() {
        return mayoraltyCode;
    }

    public void setMayoraltyCode(String mayoraltyCode) {
        this.mayoraltyCode = mayoraltyCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }
}
