package com.nacid.web.taglib.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.handlers.impl.applications.FileUploadListener;

public class LoadingTag extends SimpleTagSupport {
	
	@Override
	public void doTag() throws JspException, IOException {
		
		PageContext context = (PageContext)getJspContext();
		HttpServletRequest request = (HttpServletRequest)context.getRequest();
		HttpSession session = request.getSession();
		
		FileUploadListener listener = (FileUploadListener)session.getAttribute(WebKeys.FILE_UPLOAD_LISTENER);
		if(listener != null) {		
			long read = listener.getBytesRead();
			long total = listener.getContentLength();
			getJspContext().setAttribute("readed", read);
			getJspContext().setAttribute("total", total);
			getJspContext().setAttribute("percents", 
			        total != 0 ? (read * 100 / total) + "%" : "");
			getJspBody().invoke(null);
			
		}
	}
}
