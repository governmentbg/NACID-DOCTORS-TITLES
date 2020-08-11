package com.nacid.web.taglib.validation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.impl.Utils;
import com.nacid.data.MethodsUtils;
import com.nacid.web.taglib.SpringStyleTag;

public class TextInputTag extends SpringStyleTag {

    private String value;
    //private String path;

    public void setValue(String value) {
        this.value = value;
    }

    public void doTag() throws JspException, IOException {
        /*FormTag formTag = (FormTag) findAncestorWithClass(this, FormTag.class);
        Object modelAttribute = formTag == null ? null : formTag.getFormModelAttribute();*/
        Object val = null;
        if (/*modelAttribute != null && !StringUtils.isEmpty(path) &&*/ StringUtils.isEmpty(value)) {
            try {
                val = getModelAttributePathValue();
                //val = MethodsUtils.getGetterMethod(modelAttribute, path).invoke(modelAttribute);
                if (val != null) {
                    value = val.toString();
                }
            } catch (SecurityException e) {
                throw Utils.logException(e);
            } catch (NoSuchMethodException e) {
                throw Utils.logException(e);
            } catch (IllegalArgumentException e) {
                throw Utils.logException(e);
            } catch (IllegalAccessException e) {
                throw Utils.logException(e);
            } catch (InvocationTargetException e) {
                throw Utils.logException(e);
            } 
        }
        getJspContext().setAttribute("value", value);
        getJspBody().invoke(null);
    }
    /*public void setPath(String path) {
        this.path = path;
    }*/
}
