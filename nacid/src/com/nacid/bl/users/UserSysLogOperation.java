package com.nacid.bl.users;

import java.util.Date;

public interface UserSysLogOperation {
    public String getGroupName();
    public String getOperationName();
    public String getQueryString();
    public Date getDateCreated();
}
