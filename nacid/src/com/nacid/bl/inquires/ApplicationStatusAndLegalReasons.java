package com.nacid.bl.inquires;

import java.util.List;

public class ApplicationStatusAndLegalReasons {
	private int statusId;
	private List<Integer> legalReasons;
	public ApplicationStatusAndLegalReasons(int statusId, List<Integer> legalReasons) {
		this.statusId = statusId;
		this.legalReasons = legalReasons;
	}
	public int getStatusId() {
		return statusId;
	}
	public List<Integer> getLegalReasons() {
		return legalReasons;
	}
}
