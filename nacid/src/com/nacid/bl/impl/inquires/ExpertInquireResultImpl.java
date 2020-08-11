package com.nacid.bl.impl.inquires;

import com.nacid.bl.inquires.ExpertInquireResult;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * User: Georgi
 * Date: 24.3.2020 г.
 * Time: 13:51
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ExpertInquireResultImpl implements ExpertInquireResult {
    private int expertId;
    private String expertNames;
    private List<ExpertInquireApplication> applications;
}
