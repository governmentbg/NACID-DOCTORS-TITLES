package com.nacid.bl.external;

import com.nacid.bl.applications.base.PersonBase;

public interface ExtPerson extends PersonBase {
	public String getHashCode();
	public String getEmail();
	public Integer getUserId();
	public ExtPersonDocument getActiveExtPersonDocument();
}
