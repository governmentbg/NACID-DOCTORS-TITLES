package com.nacid.bl.impl.applications;

import com.nacid.bl.OrderCriteria;

public class ApplicationOrderCriteria extends OrderCriteria{
    public static final String ORDER_COLUMN_ID = "id";
    public static final String ORDER_COLUMN_DATE = "app_date";
    public static final String ORDER_COLUMN_NONE = null;
    public ApplicationOrderCriteria(String orderColumn, boolean ascending) {
        super(orderColumn, ascending);
    }
    
    public String getOrderSqlString() {
        if (getOrderColumn() != null) {
            return " ORDER BY " + getOrderColumn() + (isAscending() ? " ASC " : " DESC ");
        }
        return "";
    }
}
