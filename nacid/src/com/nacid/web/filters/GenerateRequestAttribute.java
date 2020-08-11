package com.nacid.web.filters;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenerateRequestAttribute implements Filter  {
    @Autowired
    private FormattingConversionServiceFactoryBean formattingConversionServiceFactory;
    
    Logger logger = Logger.getLogger(GenerateRequestAttribute.class);
    //protected final Log logger = LogFactory.getLog(super.getClass());
   


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        DelegatingFilterProxy p;
        String modelAttribute = request.getParameter("modelAttribute");
        //Boolean validateForm = DataConverter.parseBoolean(request.getParameter("validateForm"));
        if (!StringUtils.isEmpty(modelAttribute)) {
            HttpServletRequest req = (HttpServletRequest)request;
            WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
            Object record = ctx.getBean(modelAttribute);
            if (record == null) {
                throw new RuntimeException("no object with name " + modelAttribute + " is defined inside spring's context...");
            }
            System.out.println("REcord = " + record);
            
            
            // apply binder to custom target object
            ServletRequestDataBinder binder = new ServletRequestDataBinder(record);
            binder.registerCustomEditor(java.sql.Date.class, new SqlDatePropertyEditor());
            //service, koito bind-va v zavisimost ot annotations!
            binder.setConversionService(formattingConversionServiceFactory.getObject());
            
            // register custom editors, if desired
            //binder.registerCustomEditor(...);
            // trigger actual binding of request parameters
            binder.bind(request);
            List<String> errors = null;            
            List<ObjectError> err = binder.getBindingResult().getAllErrors();
            if (err != null && err.size() > 0) {
                errors = new ArrayList<String>();
                for (ObjectError e:err) {
                    errors.add("Code:" + e.getCode() + "\n<br />  defaultMessage:" + e.getDefaultMessage() + "\n<br />  objectName:" + e.getObjectName());
                }
                //TODO:Tuk ima seriozen problem s hvyrlqneto na Exception - okazva se, che poletata ot tip data se podavat kato "дд.мм.гггг" ako nqma popylnena nikakva data i gyrmqt RuntimeExceptions!!! Mislih edin variant da izkliuchvam tochno tozi tip greshki, no neshto ne mi haresa... Po skoro moje bi trqbwa da se izmisli kak da ne se podavat!!!
                //throw new RuntimeException("Error trying to bind form data..." + StringUtils.join(errorMessage, "; "));
            }


            
            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            Validator validator = validatorFactory.usingContext().getValidator();

            
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(binder.getBindingResult().getTarget());
            if ( constraintViolations != null ) {
                if (errors == null) {
                    errors = new ArrayList<String>();    
                }
                for (ConstraintViolation<Object> cv:constraintViolations) {
                    //System.out.println(cv.getInvalidValue() + "  " + cv.getConstraintDescriptor());
                    errors.add(cv.getConstraintDescriptor().toString());
                }    
            }
            
            
            request.setAttribute(modelAttribute, binder.getBindingResult().getTarget());
            request.setAttribute("errors", errors != null && errors.size() == 0 ? null : errors);
            
            //List<ObjectError> errors = binder.getBindingResult().getAllErrors();
            System.out.println("Manually generated object...." + binder.getBindingResult().getTarget());

        }
        chain.doFilter(request, response);
        
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
        //okaza se che veche nqma nujda ot tova neshto - ako filter-a se definira v web.xml kato org.springframework.web.filter.DelegatingFilterProxy, i posle se definira bean v webapp-config.xml, to neshtata sami se awtowire-vat!!!, tyj kato Spring ima otgovornostta da syzdade filter-a!
        /*ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.configureBean(this, "formattingConversionServiceFactory");*/
    }
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }



}
