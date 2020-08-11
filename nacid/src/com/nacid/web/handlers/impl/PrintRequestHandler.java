package com.nacid.web.handlers.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.web.exceptions.HandlerException;
import com.nacid.web.handlers.NacidBaseRequestHandler;

public class PrintRequestHandler extends NacidBaseRequestHandler {

	public PrintRequestHandler(ServletContext servletContext) {
		super(servletContext);
	}
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		//request.setCharacterEncoding("utf-8");
		try {
			
			Enumeration<String> headerNames = request.getHeaderNames();
			System.out.println("Headers");
			while (headerNames.hasMoreElements()) {
				String hName = (String) headerNames.nextElement();
				System.out.print("Name:" + hName);
				Enumeration params = request.getHeaders(hName);
				while (params.hasMoreElements()) {
					Object element = params.nextElement();
					System.out.print("\t" + element);
				}
				System.out.println();
			}
			System.out.println("End of headers\n\n\n");
			
			InputStream is = request.getInputStream();
			InputStreamReader reader = new InputStreamReader(is, "utf-8");
			//System.out.println("Encoding:" + reader.getEncoding());
			//logger.debug("Encoding:" + reader.getEncoding());
			BufferedReader bReader = new BufferedReader(reader);
			String line;
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println(request.getContentType());
			StringBuilder buff = new StringBuilder();
			while ((line = bReader.readLine()) != null ) {
				buff.append(line + "\n");
			
			}
			System.out.println(buff);
		} catch (IOException e) {
			throw new HandlerException(e.getMessage());
		}
		
		
		
	}

}
