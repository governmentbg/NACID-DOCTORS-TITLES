package com.nacid.web.taglib.common;

import com.nacid.bl.impl.Utils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.taglib.FormInputUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ComboBoxOptionsTag extends SimpleTagSupport {
  public void doTag() throws JspException, IOException {
    ComboBoxTag parent = (ComboBoxTag) getParent();
    try {
        Object pathValue = parent.getModelAttributePathValue();
        ComboBoxWebModel webmodel = parent.getWebModel();
        String selectedValue = parent.getSelectedValue();
        if (webmodel != null) {
          for (int i = 0; i < webmodel.getItemsCount(); i++) {
            getJspContext().setAttribute("key", webmodel.getItemKey(i));
            getJspContext().setAttribute("value", webmodel.getItemValue(i));
            getJspContext().setAttribute("additionalAttributes", webmodel.getAdditionalAttributes(i));
            getJspContext().setAttribute("selected", FormInputUtils.getComboBoxSelectedText(
                                                                    (pathValue != null && pathValue.toString().equals(webmodel.getItemKey(i)))
                                                                    || (!StringUtils.isEmpty(selectedValue) && selectedValue.toString().equals(webmodel.getItemKey(i)))
                                                                    || webmodel.isItemSelected(i)));
            getJspBody().invoke(null);
          }
        }
    } catch (Exception e) {
        throw Utils.logException(e);
    }
    
  }
}
