package com.ext.nacid.web.model.applications;

import com.nacid.bl.external.applications.ExtApplicationCommentExtended;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 17:23
 */
public class ExtApplicationCommentExtendedWebModel {
    private String comment;
    private String recipient;
    private String subject;
    private String dateCreated;
    private String userCreated;
    private boolean sendEmail;
    private Boolean processed;
    private Boolean incoming;
    public ExtApplicationCommentExtendedWebModel(ExtApplicationCommentExtended ac, UsersDataProvider usersDataProvider) {
        this.comment = ac.getComment();
        this.recipient = ac.getEmailRecipient();
        this.dateCreated = DataConverter.formatDate(ac.getDateCreated());
        this.sendEmail = ac.isSendEmail();
        this.processed = ac.isEmailProcessed();
        this.incoming = ac.isEmailIncoming();
        this.subject = ac.getEmailSubject();
        this.userCreated = usersDataProvider.getUser(ac.getUserCreated()).getFullName();
    }

    public String getComment() {
        return comment;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public Boolean getIncoming() {
        return incoming;
    }

    public String getUserCreated() {
        return userCreated;
    }
}
