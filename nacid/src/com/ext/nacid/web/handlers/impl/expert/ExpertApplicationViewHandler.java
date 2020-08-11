package com.ext.nacid.web.handlers.impl.expert;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.impl.applications.ExpertReportHandler;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
/**
 * shte se polzva za razglejdane na report na zaqvlenie na expert, otvoreno ot spravki
 * @author ggeorgiev
 *
 */
public class ExpertApplicationViewHandler extends NacidExtBaseRequestHandler {

	public ExpertApplicationViewHandler(ServletContext servletContext) {
		super(servletContext);
	}
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
		Integer id = DataConverter.parseInteger(request.getParameter("id"), null);
		if (id == null ) {
			throw new RuntimeException("no application id is set... id=" + request.getParameter("id"));
		}
		ExpertReportHandler.prepareExpertReport(request, response, getNacidDataProvider(), id);
		request.setAttribute(WebKeys.NEXT_SCREEN, "expert_application_view");
	}
}
