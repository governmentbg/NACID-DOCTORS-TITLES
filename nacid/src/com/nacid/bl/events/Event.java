package com.nacid.bl.events;

import java.util.Date;

public interface Event {

    public static final int EVENT_BRING_DOCUMENTS = 1;
    
    public Integer getDocCategoryId();

    public Integer getDocumentTypeId();
    
    public Date getDueDate();

    public Date getReminderDate();

    public Integer getEventStatus();

    public Integer getDocId();

    public Integer getApplicationId();

    public Integer getEventTypeId();
    
    

    public int getId();

}
