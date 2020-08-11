package com.nacid.bl;

import com.nacid.bl.comision.ComissionMemberOrderCriteria;
import com.nacid.bl.impl.applications.ApplicationOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;


public class OrderCriteria {
    private String orderColumn;
    private boolean ascending;
    public static final int ORDER_CRITERIA_NOMENCLATURE = 1;
    public static final int ORDER_CRITERIA_COMISSION_MEMBER = 2;
    public static final int ORDER_CRITERIA_APPLICATION = 3;
    
    protected OrderCriteria(String orderColumn, boolean ascending) {
        this.orderColumn = orderColumn;
        this.ascending = ascending;
    }
    public String getOrderColumn() {
        return orderColumn;
    }
    public boolean isAscending() {
        return ascending;
    }
    /**
     * 
     * @param type edin ot definiranite tipove v {@link OrderCriteria}
     * @param orderColumn - koq kolona shte se sortira
     * @param ascending 
     * @return
     */
    public static OrderCriteria createOrderCriteria(int type, String orderColumn, boolean ascending) {
        switch (type) {
        case ORDER_CRITERIA_NOMENCLATURE:
            return new NomenclatureOrderCriteria(orderColumn, ascending);
        case ORDER_CRITERIA_COMISSION_MEMBER:
            return new ComissionMemberOrderCriteria(orderColumn, ascending);
        case ORDER_CRITERIA_APPLICATION:
            return new ApplicationOrderCriteria(orderColumn, ascending);
        default:
            break;
        }
        return new OrderCriteria(orderColumn, ascending);
    } 
}
