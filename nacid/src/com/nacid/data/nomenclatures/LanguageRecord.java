package com.nacid.data.nomenclatures;

import com.nacid.data.annotations.Column;
import com.nacid.data.annotations.Table;

import java.sql.Date;

@Table(name = "nomenclatures.language")
public class LanguageRecord extends FlatNomenclatureRecord {
    @Column(name = "iso639_code")
    private String iso639Code;

    public LanguageRecord() {
    }

    public LanguageRecord(int id, String name, Date dateFrom, Date dateTo, String iso639Code) {
        super(id, name, dateFrom, dateTo);
        this.iso639Code = iso639Code;
    }

    public String getIso639Code() {
        return iso639Code;
    }

    public void setIso639Code(String iso639Code) {
        this.iso639Code = iso639Code;
    }
}