package com.nacid.web.taglib.common;

import com.nacid.web.WebKeys;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.taglib.SpringStyleTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class ComboBoxTag extends SpringStyleTag {
    private String attributeName;
    private String style;
    private String onclick;
    private String onchange;
    private String id;
    private String disabled;
    private ComboBoxWebModel webmodel;
    private String selectedValue;
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    public void setStyle(String style) {
        this.style = style;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }
    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
    public void doTag() throws JspException, IOException {
        if (attributeName == null || "".equals(attributeName)) {
            attributeName = WebKeys.COMBO;
        }
        webmodel = (ComboBoxWebModel) getJspContext().getAttribute(attributeName, PageContext.REQUEST_SCOPE);
        String comboattributes = "";
        if (webmodel != null) {
            if (onclick != null && !"".equals(onclick)) {
                comboattributes += " onclick=\"" + onclick + "\" ";
            }
            if (onchange != null && !"".equals(onchange)) {
                comboattributes += " onchange=\"" + onchange + "\" ";
            }
            if (style != null && !"".equals(style)) {
                comboattributes += " class=\"" + style + "\" ";
            }
            if (id != null && !"".equals(id)) {
                comboattributes += " id=\"" + id + "\" ";
            }
            if (disabled != null && !"".equals(disabled)) {
                comboattributes += " disabled=\"" + disabled + "\" ";
            }
            //getJspContext().setAttribute("comboonclick", (onclick == null || "".equals(onclick)) ? "" : "onclick=\"" + onclick + "\" ");
            //getJspContext().setAttribute("comboonchange", (onchange == null || "".equals(onchange)) ? "" : "onchange=\"" + onchange + "\" ");
            //getJspContext().setAttribute("combostyle", (style == null || "".equals(style)) ? "" : "class=\"" + style + "\" ");
            //getJspContext().setAttribute("comboid", (id == null || "".equals(id)) ? "" : "id=\"" + style + "\"");
            getJspContext().setAttribute("comboattributes", comboattributes);
            getJspBody().invoke(null);
        }
    }
    public ComboBoxWebModel getWebModel() {
        return webmodel;
    }

    public String getSelectedValue() {
        return selectedValue;
    }
}
