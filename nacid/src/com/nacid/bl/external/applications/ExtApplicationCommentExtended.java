package com.nacid.bl.external.applications;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 14:30
 */
public interface ExtApplicationCommentExtended extends ExtApplicationComment {
    Boolean isEmailProcessed();

    Boolean isEmailIncoming();

    String getEmailRecipient();

    String getEmailSubject();
}
