package com.nacid.bl.impl.ras.objects;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 23.10.2019 г.
 * Time: 17:57
 */
public class GetNomenclaturesResult {
    private int totalCount;
    private List<Nomenclature> result;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Nomenclature> getResult() {
        return result;
    }

    public void setResult(List<Nomenclature> result) {
        this.result = result;
    }
}
