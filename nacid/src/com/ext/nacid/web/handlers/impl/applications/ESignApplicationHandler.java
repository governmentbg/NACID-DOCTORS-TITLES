package com.ext.nacid.web.handlers.impl.applications;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;

public class ESignApplicationHandler extends NacidExtBaseRequestHandler {

	public ESignApplicationHandler(ServletContext servletContext) {
		super(servletContext);
	}
	private static final String ENCODING = "UTF-8";
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Inside handle default on ESignApplicationHandler....");
		System.out.println("Request url:" + request.getRequestURL());
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new RuntimeException("no multipart/form-data stream....");
		}
		logRequestParams(request);
		System.out.println("End of request params....");
		try {
			// Creates a factory for file items (memory or disk saved) with max size for data
			// stored in memory and default directory for saving files in the system's temp dir
			DiskFileItemFactory fitemFactory = new DiskFileItemFactory();
			//fitemFactory.setSizeThreshold(MAX_MEM_SIZE);

			ServletFileUpload uplHandler = new ServletFileUpload(fitemFactory);
			//uplHandler.setFileSizeMax(MAX_REQ_SIZE);
			uplHandler.setHeaderEncoding(ENCODING);
			List fileItems = uplHandler.parseRequest(request);
			Iterator iter = fileItems.iterator();
			InputStream is = null;
			int fileSize = 0;
			String contentType = null;
			int applicationId = 0;
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				System.out.println("field name:" + item.getFieldName());
				if (item.isFormField()) {
                    if(item.getFieldName().equals("applicationId")) {
                    	applicationId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    }
                } 
                else {
                    fileSize = (int)item.getSize();
                    if(fileSize > 0) {
                        is = item.getInputStream();
                        contentType = item.getContentType();
                    }
                }
			}
			System.out.println("applicationId:" + applicationId);
			System.out.println("inputStream:" );
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				new ByteArrayInputStream("<a>".getBytes("UTF-8"));
			}
		} catch (Exception e) {
			throw Utils.logException(e);
		}

	}


}
