package com.nacid.bl.external.applications;

import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 14:30
 */
public interface ExtApplicationComment {
    public int getId() ;
    public int getApplicationId();
    public String getComment();
    public boolean isSendEmail();
    public Integer getEmailId();
    public Date getDateCreated();
    public int getUserCreated();
    public boolean isSystemMessage();
}
