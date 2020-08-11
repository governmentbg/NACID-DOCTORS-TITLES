package com.nacid.bl.inquires;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * User: Georgi
 * Date: 13.4.2020 г.
 * Time: 23:09
 */
@AllArgsConstructor
@Getter
public class ApplicationTypeAndEntryNumSeries {
    private Integer applicationType;
    private List<Integer> entryNumSeries;
    private int joinType;
    public static final int JOIN_TYPE_AND = 1;//vsichki izbroeni (no zaqvlenieto moje da ima i poveche)
    public static final int JOIN_TYPE_OR = 2;//nqkoi ot izbroenite
    public static final int JOIN_TYPE_EQUALS = 3;//tochno vsichki izbroeni

}
