package com.nacid.web.model.users;

//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.users.User;

public class ExtUserWebModel {
    private String id;
    private String fullName;
    private String userName;
    private boolean status;
    private String type;
    private String email;

   
    public ExtUserWebModel(User user, String type, String email) {
        id = user.getUserId() + "";
        fullName = user.getFullName();
        userName = user.getUserName();
        status = user.getStatus() == User.USER_STATUS_ACTIVE ? true : false;
        this.type = type;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }


}