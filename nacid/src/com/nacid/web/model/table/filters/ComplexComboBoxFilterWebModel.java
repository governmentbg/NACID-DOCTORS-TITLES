package com.nacid.web.model.table.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.nacid.web.model.common.ComboBoxWebModel;

public class ComplexComboBoxFilterWebModel extends ComboBoxFilterWebModel {
    private Map<Object, String> selectedItems;
    public ComplexComboBoxFilterWebModel(ComboBoxWebModel comboBox, String comboName, String label, Map<Object, String> selectedItems) {
        super(comboBox, comboName, label);
        this.selectedItems = selectedItems;
    }

    public Map<Object, String> getSelectedItems() {
        return selectedItems;
    }
    public String getSelectedItemIds() {
        List<String> result = new ArrayList<String>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (Object i:selectedItems.keySet()) {
                if (i != null) {
                    result.add(i + "");    
                }
                
            }
        }
        return StringUtils.join(result, ";");
    }
    public String getType() {
        return "complexCombo";
    }    
}
