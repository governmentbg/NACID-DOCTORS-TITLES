package com.nacid.data.users;

import java.sql.Timestamp;

public class ApplicationChangesHistoryRecord {
    private Integer id;
    private Integer applicationId;
    private Integer userId;
    private Timestamp date;
    private String operationName;
    private String groupName;
    
    public ApplicationChangesHistoryRecord() {
    }
    public ApplicationChangesHistoryRecord(Integer id, Integer applicationId, Integer userId, Timestamp date, String operationName, String groupName) {
        this.id = id;
        this.applicationId = applicationId;
        this.userId = userId;
        this.date = date;
        this.operationName = operationName;
        this.groupName = groupName;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ApplicationChangesHistoryRecord [applicationId=").append(applicationId).append(", date=").append(date).append(", groupName=")
                .append(groupName).append(", id=").append(id).append(", operationName=").append(operationName).append(", userId=").append(userId)
                .append("]");
        return builder.toString();
    }
    
    
    
}
