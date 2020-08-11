package com.nacid.web.taglib.validation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.impl.Utils;
import com.nacid.web.model.applications.UniversityWebModel;
import com.nacid.web.taglib.SpringStyleTag;

public class DateInputTag extends SpringStyleTag {
    private String name;
    private String style;
    private String value;
    private String id;
    private String emptyString;
    private String pattern;
    private static final String DEFAULT_PATTERN = "dd.MM.yyyy";
    //private String path;

    public void setName(String name) {
        this.name = name;
    }
    public void setStyle(String style) {
        this.style = style;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }
    public void setPattern(String pattern) {
        this.pattern = StringUtils.isEmpty(pattern) ? DEFAULT_PATTERN : pattern;
    }
    public void setEmptyString(String emptyString) {
        this.emptyString = StringUtils.isEmpty(emptyString) ? "дд.мм.гггг" : emptyString;
    }
    public void doTag() throws JspException, IOException {
        //FormTag formTag = (FormTag) findAncestorWithClass(this, FormTag.class);
        //Object modelAttribute = formTag == null ? null : formTag.getFormModelAttribute();
        Date val = null;
        if (/*modelAttribute != null && !StringUtils.isEmpty(path) &&*/ StringUtils.isEmpty(value)) {
            try {
                //Field field = modelAttribute.getClass().getDeclaredField(path);
                //val = (Date) MethodsUtils.getGetterMethod(modelAttribute, path).invoke(modelAttribute);
            	if (hasModelAttribute()) {
            	    val = (Date) getModelAttributePathValue();
            	    if (val != null) {
            	        SimpleDateFormat simpleDateFormat = null;
                        simpleDateFormat = new SimpleDateFormat(pattern);
                        value = simpleDateFormat.format(val);    
            	    }
            	    
            	}
                if (StringUtils.isEmpty(value)) {
                    value = emptyString;
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
        String attributes = "";

        if (name != null && !"".equals(name)) {
            attributes += " name=\"" + name + "\" ";
        }
        if (value != null && !"".equals(value)) {
            attributes += " value=\"" + value + "\" ";
        }
        if (style != null && !"".equals(style)) {
            attributes += " class=\"" + style + "\" ";
        }
        if (id != null && !"".equals(id)) {
            attributes += " id=\"" + id + "\" ";
        }

        getJspContext().setAttribute("attributes", attributes);
        getJspContext().setAttribute("eString", emptyString);
        getJspBody().invoke(null);
    }
    /*public void setPath(String path) {
        this.path = path;
    }*/
    public static void main(String[] args) throws Exception {
        Field f = UniversityWebModel.class.getDeclaredField("dFrom");
        System.out.println(f.getName());
    }
}
