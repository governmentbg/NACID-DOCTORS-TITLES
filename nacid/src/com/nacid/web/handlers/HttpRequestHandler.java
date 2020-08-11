package com.nacid.web.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.impl.MenuShowHandler;

/**
 * Base interface to request handlers.
 */

public interface HttpRequestHandler /*extends java.io.Serializable */{
    
    public static final String SYSTEM_MESSAGES_MAP = "systemMessagesMap";
  
  public void processRequest(HttpServletRequest request, HttpServletResponse response);
	
	public void handleNew(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * @throws UnknownRecordException - pri opit za redaktirane na danni, koito ne sy6testvuvat
	 */
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) throws UnknownRecordException;

	/**
	 * @throws UnknownRecordException - pri opit za zapisvane na danni, koito ne sy6testvuvat
	 */
	public void handleSave(HttpServletRequest request, HttpServletResponse response) throws UnknownRecordException;
	
	/**
	 * @throws UnknownRecordException - pri opit za iztrivane na danni, koito ne sy6testvuvat
	 */
	public void handleDelete(HttpServletRequest request, HttpServletResponse response) throws UnknownRecordException;
	
	public int getGroupId();
	/**
	 * ako se nalaga custom initializaciq na handler-a
	 * @param request
	 * @param response
	 */
	public void initHandler(HttpServletRequest request, HttpServletResponse response);
}
