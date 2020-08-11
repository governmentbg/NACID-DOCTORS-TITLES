package com.nacid.bl.nomenclatures;


public interface Country extends FlatNomenclature {
	public static final int COUNTRY_ID_BULGARIA = NomenclaturesDataProvider.COUNTRY_ID_BULGARIA;
	public static final int COUNTRY_ID_UNDEFINED = 247;
	
	public String getIso3166Code();
	public String getOfficialName();
}
