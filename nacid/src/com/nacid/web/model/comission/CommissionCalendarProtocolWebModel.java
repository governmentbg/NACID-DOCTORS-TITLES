package com.nacid.web.model.comission;

public class CommissionCalendarProtocolWebModel {

    private int calendarId;
    private String operation;
    private String fileName;
    public CommissionCalendarProtocolWebModel(int calendarId, String operation, String fileName) {
        this.calendarId = calendarId;
        this.operation = operation;
        this.fileName = fileName;
    }
    public int getCalendarId() {
        return calendarId;
    }
    public String getOperation() {
        return operation;
    }
    public String getFileName() {
        return fileName;
    }
    
}
