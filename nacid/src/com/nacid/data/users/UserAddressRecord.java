package com.nacid.data.users;

public class UserAddressRecord {
	/**
	 * principno tezi promenlivi e dobre da se prehvyrlqt v dadeniq Impl class v bl layer-a
	 */
	public static final int ADDRESS_TYPE_INDIVIDUAL = 0;
	public static final int ADDRESS_TYPE_GROUP = 1;
	
	public static final int ADDRESS_RECORD_INACTIVE = 0;
	public static final int ADDRESS_RECORD_ACTIVE = 1;
	private int recordId;
	private int userId;
	/**
	 * Opredelq dali adresa koito e zadaden se otnasq za grupa ili e individualen
	 * primerno 192.168.0.1 moje da se otnasq za 192.168.0.1(individualen) ili 192.168.0.1*(grupov)
	 */
	private String ipAddress;
	private int addressType;
	private int active;
	public UserAddressRecord() {
	}
	public UserAddressRecord(int recordId, int userId, String ipAddress, int addressType, int active) {
		this.recordId = recordId;
		this.userId = userId;
		this.ipAddress = ipAddress;
		this.addressType = addressType;
		this.active = active ;
	}
	public int getRecordId() {
		return recordId;
	}
	public int getUserId() {
		return userId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public int getAddressType() {
		return addressType;
	}
	public int getActive() {
		return active;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public void setAddressType(int addressType) {
		this.addressType = addressType;
	}
	public void setActive(int active) {
		this.active = active;
	}
	
	
	
	
	
}
