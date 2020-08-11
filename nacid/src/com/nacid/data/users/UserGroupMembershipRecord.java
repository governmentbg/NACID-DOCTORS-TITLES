package com.nacid.data.users;


public class UserGroupMembershipRecord {
	private int recordId;
	private int userId;
	private int groupId;
	private String operations;
	private int webApplication;
	/**
	 * ako na daden potrebitel mu e zadadena -1 za operations, tova ozna4ava 4e toi ima prava za vsi4ki operacii v konkretnata grupa!
	 */
	public static final int FULL_ACCESS_OPERATION_ID = -1;
	/**
	 * ako za daden potrebitel ima dobavena grupa -1, tova ozna4ava 4e toj ima pylni parava za vsi4ki grupi
	 */
	public static final int FULL_ACCESS_GROUP_ID = -1;
	public UserGroupMembershipRecord() {
	}
	public UserGroupMembershipRecord(int recordId, int userId, int groupId, String operations, int webApplication) {
		this.recordId = recordId;
		this.userId = userId;
		this.groupId = groupId;
		this.operations = operations;
		this.webApplication = webApplication;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public void setOperations(String operations) {
		this.operations = operations;
	}
	public void setWebApplication(int webApplication) {
	    this.webApplication = webApplication;
	}
	public int getRecordId() {
		return recordId;
	}
	public int getUserId() {
		return userId;
	}
	public int getGroupId() {
		return groupId;
	}
	public String getOperations() {
		return operations;
	}
	public int getWebApplication() {
	    return webApplication;
	}


}
