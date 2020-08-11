package com.nacid.bl.impl.inquires;

import com.nacid.bl.inquires.ExpertInquireResult;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * User: Georgi
 * Date: 24.3.2020 г.
 * Time: 15:45
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ExpertInquireApplicationImpl implements ExpertInquireResult.ExpertInquireApplication {
    private int id;
    private String appNum;
    private Date appDate;
}
