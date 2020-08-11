package com.nacid.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.nacid.data.DataConverter;

public class PropertiesBundle {
	private static ResourceBundle resourceBundle;
	PropertiesBundle(InputStream is) throws IOException {
		resourceBundle = new PropertyResourceBundle(is);
	}
	
	/*public static boolean isDebug() {
		String value = resourceBundle.getString("debug");
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		return DataConverter.parseBoolean(value);
	}*/
	
	/**
	 * @return dali nastroikata za izprashtane na mails e vkliu4ena ili ne!
	 */
	public static boolean isEmailsEnabled() {
		try {
		    String value = resourceBundle.getString("emails_enabled");
	        if (StringUtils.isEmpty(value)) {
	            return false;
	        }
	        return DataConverter.parseBoolean(value);    
		} catch (RuntimeException e) {
		    return false;
        }
	    
	}
	
	/**
	 * @return 
	 * true - docflow-a trqbva da se generira ot webservice-a
	 * false - random generated docflows
	 */
	public static boolean isDocflowEnabled() {
		try {
		    String value = resourceBundle.getString("docflow_enabled");
	        if (StringUtils.isEmpty(value)) {
	            return false;
	        }
	        return DataConverter.parseBoolean(value);    
		} catch (RuntimeException e) {
		    return false;
        }
	    
	}
	
	public static void main(String[] args) {
		
	}
}
