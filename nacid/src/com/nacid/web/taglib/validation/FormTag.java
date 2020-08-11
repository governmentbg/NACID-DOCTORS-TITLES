package com.nacid.web.taglib.validation;

import com.nacid.web.WebKeys;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormTag extends SimpleTagSupport implements DynamicAttributes {


    protected String action;
    protected String method;
    protected String name;
    protected String onsubmit;
    protected String enctype;
    protected String additionalvalidation;
    protected String backurl;
    protected boolean skipsubmitbuttons;
    protected String ajax;
    private String printurl;
    private boolean addprintbutton;
    protected ArrayList<String> validationFuncs = new ArrayList<String>();
    private boolean addGenerateButton;
    private String additionalButtons;
    private boolean additionalButtonsVisible;
    protected String modelAttribute;
    private String submitBtnText;
    private String submitBtnStyle;
    private boolean skipSubmit;
    private Map<String,Object> tagAttrs = new HashMap<String,Object>();

    @Override
    public void doTag() throws JspException, IOException {
        
        StringWriter sw = new StringWriter();
        getJspBody().invoke(sw);

        JspWriter out = getJspContext().getOut();
        StringBuffer sb = new StringBuffer();
        sb.append("<form ");
        if(name != null) {
            sb.append("name=\"");
            sb.append(name);
            sb.append("\"");
            sb.append(" id=\"");
            sb.append(name);
            sb.append("\" ");
        }
        sb.append("method=\"");
        sb.append(method);
        sb.append("\" ");

        sb.append("action=\"");
        sb.append(action);
        sb.append("\" ");

        if(enctype != null) {
            sb.append("enctype=\"");
            sb.append(enctype);
            sb.append("\" ");

        }


        // add dynamic attributes
        for ( String attrName : tagAttrs.keySet() ) {
            sb.append(" " + attrName + "=\"");
            sb.append(tagAttrs.get(attrName));
            sb.append("\" ");
        }

        if(validationFuncs.size() > 0 || onsubmit != null || additionalvalidation != null || ajax != null) {
            sb.append("onsubmit=\"");
            if(onsubmit != null) {
                sb.append(onsubmit + ";");
            }
            if (ajax != null && !"".equals(ajax)) {
                sb.append("if (validateForm" + name + "()) " + ajax + ";return false;");
            } else {
                sb.append("return validateForm" + name + "();");    
            }
            
            sb.append("\" >\n ");
            sb.append(getOnsubmitFunction());
        } else {
            sb.append(" >\n");    
        }
 
        if(addGenerateButton) {
            sb.append("<input type=\"hidden\" name=\"generate\" id=\"generate\" value=\"0\" />");
        }
        /**
         * generating submit and back buttons
         */
        StringBuilder submitbuttons = new StringBuilder("");
        if (!skipsubmitbuttons) {
            submitbuttons.append("\n<br />");
            submitbuttons.append("<p class=\"cc\">\n");
            if (!StringUtils.isEmpty(backurl)) {
            	submitbuttons.append("<input class=\"back\" type=\"button\" onclick=\"document.location.href='" + backurl + "';\" value=\"Назад\" title=\"Назад\" />\n");	
            }
            
            String submitTxt = submitBtnText == null ? "Запис на данните" : submitBtnText;
            String submitStyle = submitBtnStyle == null ? "" : "style=\""+submitBtnStyle + "\"";
            
            Boolean operationView = (Boolean) getJspContext().getAttribute(WebKeys.OPERATION_VIEW, PageContext.REQUEST_SCOPE);
            String submitbutton = operationView == null || operationView == false ? "<input class=\"save\" type=\"submit\" value=\""+submitTxt+"\" "+submitStyle+"/>\n" : "";
            
            if(!skipSubmit) {
                submitbuttons.append(submitbutton);
            }
            
            if (addprintbutton && printurl != null) {
            	submitbuttons.append("<input class=\"print\" type=\"button\" onclick=\"window.open('" + printurl + "','Печат','width=800,height=600');\" value=\"Печат\"/>\n");
            }
            
            if(addGenerateButton) {
                submitbuttons.append("<input class=\"generate\" type=\"submit\" onclick=\"$('generate').value = 1; \" value=\"Генерирай\"/>\n");
            }
            if (additionalButtonsVisible && additionalButtons != null) {
            	submitbuttons.append(additionalButtons);
            }
            submitbuttons.append("</p>");
            sb.append(submitbuttons);
        }
        
        
        out.write(sb.toString());
        if (!StringUtils.isEmpty(modelAttribute)) {
            sw.append("<input name=\"modelAttribute\" type=\"hidden\" value=\"" + modelAttribute + "\" />\n");
        }
        if (!skipsubmitbuttons) {
            sw.append("<input name=\"sub\" value=\"1\" type=\"hidden\"/>\n");
            sw.append("  <div class=\"clr20\"><!--  --></div>\n");
            sw.append(submitbuttons);
        }
        
        out.write(sw.toString());
        out.write("</form>");
    }

    public void setSkipSubmit(boolean skipSubmit) {
        this.skipSubmit = skipSubmit;
    }

    protected String getOnsubmitFunction() {
        StringBuffer sb = new StringBuffer();
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("function validateForm" + name + "(){\n");
        sb.append("clearAllErrors(" + getFormName() +"); ");
        sb.append("var ret=true; ");
        for(String s : validationFuncs) {
            sb.append("ret=");
            sb.append(s);
            sb.append(" && ret;\n");
        }
        if (additionalvalidation != null && !"".equals(additionalvalidation)) {
            sb.append("ret=");
            sb.append(additionalvalidation);
            sb.append(" && ret;");
        }
        sb.append("alertError(!ret);");
        sb.append("return ret;");
        sb.append("\n}\n");
        sb.append("</script>\n");
        return sb.toString();
    }
    
    

    public void setSubmitBtnText(String submitBtnText) {
        this.submitBtnText = submitBtnText;
    }

    public void setSubmitBtnStyle(String submitBtnStyle) {
        this.submitBtnStyle = submitBtnStyle;
    }

    public void addInputValidation(String s) {
        validationFuncs.add(s);
    }

    public void setAction(String action) {
        this.action = action;
    }


    public void setMethod(String method) {
        this.method = method;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setOnsubmit(String onsubmit) {
        this.onsubmit = onsubmit;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    public void setAdditionalvalidation(String additionalvalidation) {
        this.additionalvalidation = additionalvalidation;
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }
    public String getFormName() {
        return "document." + name;
    }

    public void setSkipsubmitbuttons(boolean skipsubmitbuttons) {
        this.skipsubmitbuttons = skipsubmitbuttons;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }

	public void setPrinturl(String printurl) {
		this.printurl = "".equals(printurl) ? null : printurl;
	}

	public void setAddprintbutton(boolean addprintbutton) {
		this.addprintbutton = addprintbutton;
	}
	
	public void setAddGenerateButton(boolean addGenerateButton) {
	    this.addGenerateButton = addGenerateButton;
	}

	public void setAdditionalButtons(String additionalButtons) {
		this.additionalButtons = StringUtils.isEmpty(additionalButtons) ? null : additionalButtons;
	}
	public void setAdditionalButtonsVisible(boolean additionalButtonsVisible) {
		this.additionalButtonsVisible = additionalButtonsVisible;
	}

	/*public String getModelAttribute() {
		return modelAttribute;
	}*/

	public void setModelAttribute(String modelAttribute) {
		this.modelAttribute = modelAttribute;
	}
	public String getModelAttribute() {
	    return modelAttribute;
	}
	public Object getFormModelAttribute() {
		return modelAttribute == null ? null : getJspContext().getAttribute(modelAttribute, PageContext.REQUEST_SCOPE);
	}

    // store all other (dynamic) attributes
    public void setDynamicAttribute(String uri, String name, Object value) {
        tagAttrs.put(name, value);
    }
}
