package com.nacid.bl.nomenclatures;

/**
 * Created by georgi.georgiev on 30.04.2015.
 */
public interface OriginalEducationLevel extends FlatNomenclature {
    public int getEducationLevelId();
    public int getCountryId();
    String getNameTranslated();
}
