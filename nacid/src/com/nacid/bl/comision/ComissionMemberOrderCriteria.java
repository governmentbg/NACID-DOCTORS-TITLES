package com.nacid.bl.comision;

import com.nacid.bl.OrderCriteria;

public class ComissionMemberOrderCriteria extends OrderCriteria {
    public static String ORDER_COLUMN_FULL_NAME = "fullName";
    public static String ORDER_COLUMN_ID = "id";
    public ComissionMemberOrderCriteria(String orderColumn, boolean ascending) {
        super(orderColumn, ascending);
    }
}
