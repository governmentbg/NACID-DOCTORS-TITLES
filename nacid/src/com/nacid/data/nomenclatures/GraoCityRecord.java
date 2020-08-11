package com.nacid.data.nomenclatures;

import com.nacid.bl.nomenclatures.GraoCity;
import com.nacid.data.annotations.Column;
import com.nacid.data.annotations.Table;

/**
 * Created by georgi.georgiev on 18.09.2015.
 */
@Table(name="nomenclatures.graosettlements", sequence = "nomenclatures.graosettlements_id_seq")
public class GraoCityRecord implements GraoCity {
    private int id;
    @Column(name="municipalityid")
    private int municipalityId;
    private String code;
    private String name;
    @Column(name="isdistrict")
    private byte isDistrict;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(int municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getIsDistrict() {
        return isDistrict;
    }

    public void setIsDistrict(byte isDistrict) {
        this.isDistrict = isDistrict;
    }
}
