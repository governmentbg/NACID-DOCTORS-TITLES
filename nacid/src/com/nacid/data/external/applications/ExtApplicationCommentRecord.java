package com.nacid.data.external.applications;

import com.nacid.bl.external.applications.ExtApplicationComment;
import com.nacid.data.annotations.Table;

import java.sql.Timestamp;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 14:28
 */
@Table(name = "eservices.rudi_application_comments", sequence = "eservices.rudi_application_comments_id_seq")
public class ExtApplicationCommentRecord implements ExtApplicationComment {
    private int id;
    private int applicationId;
    private String comment;
    private int sendEmail;
    private int systemMessage;
    private Integer emailId;
    private Timestamp dateCreated;
    private int userCreated;

    public ExtApplicationCommentRecord(int id, int applicationId, String comment, int sendEmail, Integer emailId, int systemMessage, Timestamp dateCreated, int userCreated) {
        this.id = id;
        this.applicationId = applicationId;
        this.comment = comment;
        this.sendEmail = sendEmail;
        this.emailId = emailId;
        this.systemMessage = systemMessage;
        this.dateCreated = dateCreated;
        this.userCreated = userCreated;
    }

    public ExtApplicationCommentRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(int sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(int userCreated) {
        this.userCreated = userCreated;
    }

    @Override
    public boolean isSendEmail() {
        return sendEmail == 1;
    }

    public int getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(int systemMessage) {
        this.systemMessage = systemMessage;
    }
    public boolean isSystemMessage() {
        return systemMessage == 1;
    }
}
