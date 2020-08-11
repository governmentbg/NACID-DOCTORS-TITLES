package com.nacid.bl.impl.users.regprof;

import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.bl.users.regprof.ResponsibleUser;
import com.nacid.data.users.ResponsibleUserRecord;

//RayaWritten-------------------------------------------
public class ResponsibleUserImpl implements ResponsibleUser{
    Integer id;
    Integer regprofApplicationId;
    Integer userId;
    String fullName;
    
    public ResponsibleUserImpl(ResponsibleUserRecord record, UsersDataProvider usersDP){
        this.id = record.getId();
        this.regprofApplicationId = record.getRegprofApplicationId();
        this.userId = record.getUserId();
        this.fullName = usersDP.getUser(userId).getFullName();
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getRegprofApplicationId() {
        return regprofApplicationId;
    }
    public void setRegprofApplicationId(Integer regprofApplicationId) {
        this.regprofApplicationId = regprofApplicationId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }   
}
//------------------------------------------------------
