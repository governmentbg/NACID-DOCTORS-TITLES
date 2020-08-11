package com.nacid.web.taglib.validation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.taglib.FormInputUtils;
import com.nacid.web.taglib.SpringStyleTag;

public class CheckBoxInputTag extends SpringStyleTag {

    private String value;
    private boolean checked;
    public void setValue(String value) {
        this.value = value;
    }
    public void setChecked(String checked) {
        this.checked = DataConverter.parseBoolean(checked);
    }

    public void doTag() throws JspException, IOException {
        Object val = null;
        if (/*modelAttribute != null && !StringUtils.isEmpty(path) &&*/ StringUtils.isEmpty(value)) {
            try {
        		val = getModelAttributePathValue();
        		value = "1";
        		if (val == null) {
        		    checked = false;
        		} else if (val instanceof Number || val.getClass().getName().equals("int.class")) {
        		    checked = ((Number)val).intValue() != 0;
        		} else if (val instanceof Boolean){
        		    checked = (Boolean) val;
        		} else {
                    throw new RuntimeException("Unsupported value type of checkbox input (" + value.getClass() + ") only allowed are boolean/java.lang.Number/");
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
        getJspContext().setAttribute("checked", FormInputUtils.getCheckBoxCheckedText(checked));
        getJspBody().invoke(null);
    }
    
    public static void main(String[] args) throws Exception {
        //Method method = CheckBoxInputTag.class.getMethod("getValue");
        //System.out.println(method.getReturnType().getClass().isInstance(Number.class));
        int x = 5;
        Object o = x;
        System.out.println(o.getClass());
    }
}
