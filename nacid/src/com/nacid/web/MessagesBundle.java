package com.nacid.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessagesBundle extends HashMap {
    private volatile static MessagesBundle messageBundle;
    private ResourceBundle resourceBundle;

    public static MessagesBundle getMessagesBundle() {
        if (messageBundle == null) {
            synchronized (MessagesBundle.class) {
                if (messageBundle == null) {
                    messageBundle = new MessagesBundle();
                }
            }
        }
        return messageBundle;
    }
    private MessagesBundle() {
        resourceBundle = ResourceBundle.getBundle("com.nacid.web.Messages");
    }

    public String get(Object key) {
        if (key instanceof String) {
            return getValue((String) key);
        }
        return null;
    }

    public String getValue(String key) {
        String result = "";
        try {
            result = resourceBundle.getString(key);    
        } catch (MissingResourceException e) {
            result = "No parameter defined for key:" + key + " in com.nacid.web.Messages.properties file";
        }
        
        if (result == null) {
            return null;
        }
        try {
            return new String(result.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        MessagesBundle helper = new MessagesBundle();
        System.out.println(helper.get("applicationdata"));
        System.out.println(System.getProperty("file.encoding"));
    }
}
