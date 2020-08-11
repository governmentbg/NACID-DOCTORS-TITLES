package com.nacid.bl.mail;

import java.util.Date;

public interface Mail {

    public int getId();
    public String getSubject();
    public String getBody();
    public Date getDate();
    public boolean isIncome();
    public boolean isProcessed();
    public String getRecepient();
}
