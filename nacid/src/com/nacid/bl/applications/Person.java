package com.nacid.bl.applications;

import com.nacid.bl.applications.base.PersonBase;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;

public interface Person extends PersonBase {
	public Country getBirthCountry();
	public Country getCitizenship();
	public PersonDocument getPersonDocument();
	
}
