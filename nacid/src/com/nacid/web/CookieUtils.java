package com.nacid.web;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie myCookie = getCookie(request, cookieName);
    if  (myCookie != null) {
    	return myCookie.getValue();
    }
    return null;
	}
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
	  Cookie[] cookies = request.getCookies();
    Cookie myCookie=null;
    if (cookies != null) {
      for (Cookie c : cookies) {
        if (cookieName.equals(c.getName())) {
          myCookie = c;
          break;
        }
      }
    }
    return myCookie;
	}
	/**
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param persistent - true - persisten Cookie, false - session Cookie
	 * @param expirationDate - pri persistent=false ne igrae
	 */
	public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue, String path, boolean persistent, Date expirationDate) {
		if (cookieName == null || cookieValue == null || (persistent && expirationDate == null)) {
			return;
		}
		Cookie cookie = new Cookie(cookieName, cookieValue);
		int expiration = persistent ? (int) ((expirationDate.getTime() - new Date().getTime())/1000) : -1;
		cookie.setMaxAge(expiration);
		cookie.setPath(path);
		response.addCookie(cookie);
	}
	
	public static void removeCookie( HttpServletResponse response, String path, String cookieName) {
	  //Cookie cookie = getCookie(request, cookieName);
	  Cookie cookie = new Cookie(cookieName, "");
	  cookie.setMaxAge(0);
	  cookie.setPath(path);
	  response.addCookie(cookie);
	}
}
