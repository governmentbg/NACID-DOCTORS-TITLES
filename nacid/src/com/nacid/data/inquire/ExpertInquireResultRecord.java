package com.nacid.data.inquire;

import lombok.Data;

import java.util.Date;

/**
 * User: Georgi
 * Date: 18.3.2020 г.
 * Time: 16:31
 */
@Data
public class ExpertInquireResultRecord {
    private int expertId;
    private String expertNames;
    private int applicationId;
    private String appNum;
    private Date appDate;
}
