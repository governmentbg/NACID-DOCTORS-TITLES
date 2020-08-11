package com.nacid.data.external.applications;

import com.nacid.bl.external.applications.ExtApplicationCommentExtended;
import com.nacid.data.annotations.Table;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 14:28
 */
public class ExtApplicationCommentExtendedRecord extends ExtApplicationCommentRecord implements ExtApplicationCommentExtended {

    private Integer intEmailProcessed;
    private Integer intEmailIncoming;
    private String emailRecipient;
    private String emailSubject;

    public Integer getIntEmailProcessed() {
        return intEmailProcessed;
    }

    public void setIntEmailProcessed(Integer intEmailProcessed) {
        this.intEmailProcessed = intEmailProcessed;
    }

    public Integer getIntEmailIncoming() {
        return intEmailIncoming;
    }

    public void setIntEmailIncoming(Integer intEmailIncoming) {
        this.intEmailIncoming = intEmailIncoming;
    }

    @Override
    public String getEmailRecipient() {
        return emailRecipient;
    }

    public void setEmailRecipient(String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    @Override
    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    @Override
    public Boolean isEmailProcessed() {
        return intEmailProcessed == null ? null : intEmailProcessed == 1;
    }

    @Override
    public Boolean isEmailIncoming() {
        return intEmailIncoming == null ? null : intEmailIncoming == 1;
    }
}
