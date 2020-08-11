package com.nacid.web.taglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ExpressionFactory;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.nacid.bl.applications.regprof.RegprofTrainingCourse;
import com.nacid.bl.applications.regprof.RegprofTrainingCourseDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.data.MethodsUtils;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.web.taglib.validation.FormTag;

public abstract class SpringStyleTag extends SimpleTagSupport {
	private String path;
	private static Logger logger = Logger.getLogger(SpringStyleTag.class);
	private static Pattern PATTERN = Pattern.compile("^(.*?)\\[(\\d+)\\]");
	public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public boolean hasModelAttribute() {
        FormTag formTag = (FormTag) findAncestorWithClass(this, FormTag.class);
        return formTag == null ? false : (formTag.getFormModelAttribute() == null ? false : true);
    }
    public Object getModelAttributePathValue() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        
        FormTag formTag = (FormTag) findAncestorWithClass(this, FormTag.class);
        Object modelAttribute = formTag == null ? null : formTag.getFormModelAttribute();
        
        ExpressionFactory factory = new org.apache.el.ExpressionFactoryImpl();
        
        if (modelAttribute != null && !StringUtils.isEmpty(path)) {
            return factory.createValueExpression(getJspContext().getELContext(), "${requestScope['" + formTag.getModelAttribute() + "']." + getPath() + "}", Object.class).getValue(getJspContext().getELContext());
        }
        return null;
        
        
	}
    
    
    public static void main(String[] args) throws Exception {
        String path = "experienceRecord.professionExperienceDocuments[0].documentNumber";
        RegprofTrainingCourseDataProvider dp = new NacidDataProviderImpl(new StandAloneDataSource()).getRegprofTrainingCourseDataProvider();
        RegprofTrainingCourse tc = dp.getRegprofTrainingCourse(127);
        Object modelAttribute = tc;
        if (modelAttribute != null && !StringUtils.isEmpty(path)) {
            logger.debug("Path:" + path);
            String[] methods = path.split("\\.");
            Object subObject = modelAttribute;
            //Object result = null;
            for (int i = 0; i < methods.length - 1 ; i++) {
                String s = methods[i];
                if (subObject == null) {
                    break;
                }
                //ima index (izraza e vyv vida xxx[6])
                Matcher matcher = PATTERN.matcher(s);
                Integer idx = null;
                if (matcher.find()) {
                    s = matcher.group(1);
                    idx = Integer.valueOf(matcher.group(2));
                }
                
                Method m = MethodsUtils.getGetterMethod(subObject, s);
                if (m == null) {
                    throw new RuntimeException("Unknown method name " + MethodsUtils.generateGetterMethodName(s) + " for class" + subObject.getClass() + "   (path = " + path + ") model attribute class = " + modelAttribute.getClass());
                }
                subObject = m.invoke(subObject);
                
                if (idx != null) {
                    m = subObject.getClass().getMethod("get", int.class);
                    subObject = m.invoke(subObject, idx);    
                }
            }
            if (subObject != null) {
                System.out.println(subObject.getClass().getDeclaredField(methods[methods.length - 1]));    
            }
               
        }
        
    }
}
