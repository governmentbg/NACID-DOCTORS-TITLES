package com.nacid.utils;

public class ApplicationId {
	private int applicationId;

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ApplicationId [applicationId=").append(applicationId).append("]");
		return builder.toString();
	}
	
	
}
