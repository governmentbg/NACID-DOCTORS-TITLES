package com.nacid.data.users;

import com.nacid.data.annotations.Table;

//RayaWritten-----------------------------
@Table(name="regprof.responsible_users")
public class ResponsibleUserRecord {    
    Integer id;
    Integer regprofApplicationId;
    Integer userId;
    
    public ResponsibleUserRecord(){
        
    }
    
    public ResponsibleUserRecord(Integer id, Integer regprofApplicationId,
            Integer userId) {
        super();
        this.id = id;
        this.regprofApplicationId = regprofApplicationId;
        this.userId = userId;
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
}
//----------------------------------------
