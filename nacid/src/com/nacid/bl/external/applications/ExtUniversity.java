package com.nacid.bl.external.applications;

import com.nacid.bl.applications.University;

public interface ExtUniversity extends University {
	public boolean isStandartUniversity();
	/**
	 * @return vry6ta stojnost samo kogato universiteta ne e standarten
	 */
	public String getUniversityTxt();
}
