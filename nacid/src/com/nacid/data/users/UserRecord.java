package com.nacid.data.users;


public class UserRecord {
	private int userId;
	private String fullName;
	private String shortName;
	private String userName;
	private String userPass;
	private int status;
	private String email;
	private String telephone;
	public UserRecord(){
	}
	public UserRecord(int userId, String fullName, String shortName,
			String userName, String userPass, int status, String email, String telephone) {
		this.userId = userId;
		this.fullName = fullName;
		this.shortName = shortName;
		this.userName = userName;
		this.userPass = userPass;
		this.status = status;
		this.email = email;
		this.telephone = telephone;
	}

	public int getUserId() {
		return userId;
	}
	public String getFullName() {
		return fullName;
	}
	public String getShortName() {
		return shortName;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public int getStatus() {
		return status;
	}
	public String getEmail() {
		return email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setUserId(int userId) {
    this.userId = userId;
  }
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public void setUserPass(String userPass) {
    this.userPass = userPass;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }
  public String toString() {
		return "UserRecord{" + 
		"\r\tUserId:" + userId +
		"\r\tfullName:" + fullName +
		"\r\tshortName:" + shortName +
		"\r\tuserName:" + userName +
		"\r\tuserPass:" + userPass +
		"\r\tstatus:" + status +
		"\r\temail:" + email +
		"\r\ttelephone:" + telephone +
		"\r}";
	}
	
	
	
	
}
