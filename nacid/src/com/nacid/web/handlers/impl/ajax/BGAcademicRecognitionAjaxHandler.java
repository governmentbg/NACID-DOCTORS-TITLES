package com.nacid.web.handlers.impl.ajax;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.academicrecognition.BGAcademicRecognitionUploadHandler;

public class BGAcademicRecognitionAjaxHandler extends NacidBaseRequestHandler{

	private static final String similarName = "similar"; 
	private static final String similarIdName = "similarSelectedId";
	private static final String indexName = "index";
	private static final String relatedIdName = "relatedId";
	
	private static final String recognitionSimilarsNextScreen = "bgacademicrecognition_similar";
	private static final String recognitionTableScreen = "bgacademicrecognition_upload_list_part";
	private static final String attributeName = "BGAcademicRecognitionInfoImpl";
	
	public BGAcademicRecognitionAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String idStr = request.getParameter(relatedIdName);
		String indexStr = request.getParameter(indexName);
		if(indexStr != null && !indexStr.isEmpty()){
			Integer index = Integer.parseInt(indexStr);
			Integer id = idStr == null || idStr.isEmpty() ? null : Integer.parseInt(idStr);
			List<BGAcademicRecognitionInfoImpl> sessionRecords = (List<BGAcademicRecognitionInfoImpl>)session.getAttribute(attributeName);
			if(index >=0 && sessionRecords != null && sessionRecords.size()>= index){
				BGAcademicRecognitionInfoImpl found = sessionRecords.get(index);
				found.setRelatedRecognitionId(id);
				request.setAttribute(similarName, found.getSimilarRecognitions());
				request.setAttribute(similarIdName, found.getRelatedRecognitionId());
				request.setAttribute(indexName, index);
			}
		} else {
			throw new RuntimeException("No index parameter.");
		}
		
		request.setAttribute(WebKeys.NEXT_SCREEN, recognitionSimilarsNextScreen);
	}

	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String indexStr = request.getParameter(indexName);

		if(indexStr != null && !indexStr.isEmpty()){
			Integer index = Integer.parseInt(indexStr);
			List<BGAcademicRecognitionInfoImpl> sessionRecords = (List<BGAcademicRecognitionInfoImpl>)session.getAttribute(attributeName);

			if(index >=0 && sessionRecords != null && sessionRecords.size()>= index){
				BGAcademicRecognitionInfoImpl found = sessionRecords.get(index);
				request.setAttribute(similarName, found.getSimilarRecognitions());
				request.setAttribute(similarIdName, found.getRelatedRecognitionId());
				request.setAttribute(indexName, index);
			}
		} else {
			throw new RuntimeException("No index parameter.");
		}
		
		 request.setAttribute(WebKeys.NEXT_SCREEN, recognitionSimilarsNextScreen);

	}
	
	@Override 
	public void handleDelete(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String indexStr = request.getParameter(indexName);

		if(indexStr != null && !indexStr.isEmpty()){
			Integer index = Integer.parseInt(indexStr);
			List<BGAcademicRecognitionInfoImpl> sessionRecords = (List<BGAcademicRecognitionInfoImpl>)session.getAttribute(attributeName);

			if(index >=0 && sessionRecords != null && sessionRecords.size()>= index){
				BGAcademicRecognitionInfoImpl found = sessionRecords.get(index);
				sessionRecords.remove(found);
				new BGAcademicRecognitionUploadHandler(getServletContext()).createTable(request, response, sessionRecords);
				
			}
		} else {
			throw new RuntimeException("No index parameter.");
		}
		
		 request.setAttribute(WebKeys.NEXT_SCREEN, recognitionTableScreen);
	}

}
