package com.nacid.web.handlers.impl.academicrecognition;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.academicrecognition.AcademicRecognitionDataProvider;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionFileLog;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionInfo;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionLogDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionExtendedImpl;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionFileLogImpl;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl;
import com.nacid.bl.impl.nomenclatures.BgAcademicRecognitionStatusImpl;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.FileUploadListener;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class BGAcademicRecognitionUploadHandler extends NacidBaseRequestHandler {
    private static Log logger = LogFactory.getLog(BGAcademicRecognitionUploadHandler.class);
	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_APPLICANT = "Заявител";
	private static final String COLUMN_NAME_CITIZENSHIP = "Гражданство";
	
	private static final String COLUMN_NAME_UNIVERSITY = "Университет";
	private static final String COLUMN_NAME_UNIVERSITY_COUNTRY = "Държава на ВУ";
	private static final String COLUMN_NAME_EDUCATION_LEVEL = "Придобита ОКС";
	private static final String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
	private static final String COLUMN_NAME_DIPLOMA_NUMBER = "Номер на диплома";
	private static final String COLUMN_NAME_DIPLOMA_DATE = "Дата на диплома";
	private static final String COLUMN_NAME_PROTOCOL_NUMBER = "Решение на АС на ВУ";
	private static final String COLUMN_NAME_DENIAL_PROTOCOL_NUMBER = "Решение на АС на ВУ (отказ/ професионален бакалавър / бакалавър / магистър)";
	private static final String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
	private static final String COLUMN_NAME_SIMILAR = "Сходни";
	
	
	//private static final String FILTER_NAME_COUNTRY = "countryFilter";
	private static final String nextScreen = "bgacademicrecognition_upload";
	private static final String attributeName = "BGAcademicRecognitionInfoImpl";
	private static final String attributeFileName = "bgAcademicRecognitionFileUplaoded";
	public BGAcademicRecognitionUploadHandler(ServletContext servletContext) {
		super(servletContext);
	}
	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
	    request.setAttribute(WebKeys.NEXT_SCREEN, nextScreen);
	    BGAcademicRecognitionHandler.generateBGUniversitiesCombobox(null, getNacidDataProvider().getUniversityDataProvider(), request);
	}
	@SuppressWarnings("rawtypes")
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        // create file upload factory and upload servlet
        DiskFileItemFactory factory = new DiskFileItemFactory();

        FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(getServletContext());
        factory.setFileCleaningTracker(pTracker);
        ServletFileUpload upload = new ServletFileUpload(factory);

        // set file upload progress listener
        FileUploadListener listener = new FileUploadListener();

        session.setAttribute(WebKeys.FILE_UPLOAD_LISTENER, listener);

        // upload servlet allows to set upload listener
        upload.setProgressListener(listener);

        List uploadedItems = null;
        
        try {
            // iterate over all uploaded files
            uploadedItems = upload.parseRequest(request);

            int recognizedUniversityId = 0;
            int startRows = 0;
            int startColumns = 0;
            String inNumber = null;
            String outNumber = null;
            //String contentType = null;
            InputStream is = null;
            String fileName = "";
            int fileSize = 0;
            
            Iterator iter = uploadedItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {

                    if (item.getFieldName().equals("recognizedUniversityId"))
                        recognizedUniversityId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("startRows"))
                    	startRows = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("startColumns"))
                    	startColumns = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("inputNumber"))
                    	inNumber = item.getString("UTF-8");
                    else if (item.getFieldName().equals("outputNumber"))
                    	outNumber = item.getString("UTF-8");
                    
                } else {
                    if (item.getFieldName().equals("doc_content")) {
                        fileSize = (int) item.getSize();
                        if (fileSize > 0) {
                            is = item.getInputStream();
                            fileName = item.getName();
                            //fileName = ApplicationAttachmentHandler.prepareFileName(item.getName());
                            //contentType = item.getContentType();
                        }
                    }
                }
            }
            if (is == null) {
                SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Трябва да изберете файл за въвеждане", SystemMessageWebModel.MESSAGE_TYPE_ERROR);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, systemMessageWebmodel);
                handleNew(request, response);
                return;
            }
            List<BGAcademicRecognitionInfoImpl> records = BGAcademicRecognitionReadXLSData.readExcel(is, fileName, startRows, 
            		startColumns, inNumber, outNumber);  
            NacidDataProvider provider = getNacidDataProvider();
            NomenclaturesDataProvider nomProvider = provider.getNomenclaturesDataProvider();
            FlatNomenclature flRecognized = nomProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, "Призната");
            AcademicRecognitionDataProvider dp = provider.getAcademicRecognitionDataProvider();
    		
            for (BGAcademicRecognitionInfoImpl r:records) {
                r.setRecognizedUniversityId(recognizedUniversityId);
                System.out.println("RECORD:  " + r);
                r.setRecognitionStatusId(flRecognized.getId());
                List<BGAcademicRecognitionExtendedImpl> acRecognitions = dp.getSimilarAcademicRecognitions(r.getApplicant(), r.getUniversity(),
                		r.getUniversityCountry(), r.getEducationLevel(), r.getDiplomaSpeciality(), null);
                if(acRecognitions != null && acRecognitions.size()>0){
                	r.setSimilarRecognitions(acRecognitions);
                }
            }
            session.setAttribute(attributeName, records);
            session.setAttribute(attributeFileName, fileName);
            createTable(request, response, records);
            request.setAttribute(WebKeys.NEXT_SCREEN, "bgacademicrecognition_upload_list");
            
        } catch (UnsupportedEncodingException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        } catch (FileUploadException e) {
            throw Utils.logException(e);
        } finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }
        //SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        //request.setAttribute(WebKeys.SYSTEM_MESSAGE, systemMessageWebmodel);
        //handleNew(request, response);        
        

    }
	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
	    HttpSession session = request.getSession();
	    Boolean save = DataConverter.parseBoolean(request.getParameter("save_bg_academic_recognition_records"));
	    if (save) {
	    	User user = getLoggedUser(request, response);
	    	NacidDataProvider provider = getNacidDataProvider();
	        NomenclaturesDataProvider nomProvider = provider.getNomenclaturesDataProvider();
	    	FlatNomenclature flCanceled = nomProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, "Отменена");
	        List<BGAcademicRecognitionInfoImpl> records = (List<BGAcademicRecognitionInfoImpl>) session.getAttribute(attributeName);
	        AcademicRecognitionDataProvider dp = provider.getAcademicRecognitionDataProvider();
	        BGAcademicRecognitionLogDataProvider logDP = provider.getBGAcademicRecognitionLogDataProvider();
	        for (BGAcademicRecognitionInfoImpl r:records) {
	            dp.saveBGAcademicRecognitionRecord(r);
	            if(r.getHasSimilar() && r.getRelatedRecognitionId() != null){
	            	for(BGAcademicRecognitionInfoImpl extended: r.getSimilarRecognitions()){
	            		if(r.getRelatedRecognitionId().equals(extended.getId())){
	            			BGAcademicRecognitionInfoImpl record = new BGAcademicRecognitionInfoImpl(); 
	            			record.setApplicant(extended.getApplicant());
	            			record.setCitizenship(extended.getCitizenship());
	            			record.setCreatedDate(extended.getCreatedDate());
	            			record.setDenialProtocolNumber(extended.getDenialProtocolNumber());
	            			record.setDiplomaDate(extended.getDiplomaDate());
	            			record.setDiplomaNumber(extended.getDiplomaNumber());
	            			record.setDiplomaSpeciality(extended.getDiplomaSpeciality());
	            			record.setEducationLevel(extended.getEducationLevel());
	            			record.setId(extended.getId());
	            			record.setInputNumber(extended.getInputNumber());
	            			record.setNotes(extended.getNotes());
	            			record.setOutputNumber(extended.getOutputNumber());
	            			record.setProtocolNumber(extended.getProtocolNumber());
	            			record.setRecognizedSpeciality(extended.getRecognizedSpeciality());
	            			record.setRecognizedUniversityId(extended.getRecognizedUniversityId());
	            			record.setRelatedRecognitionId(extended.getRelatedRecognitionId());
	            			record.setUniversity(extended.getUniversity());
	            			record.setUniversityCountry(extended.getUniversityCountry());
	            			record.setRecognitionStatusId(flCanceled.getId());
	            			dp.saveBGAcademicRecognitionRecord(record);
	            			break;
	            		}
	            	}
	            }
	        }
	        BGAcademicRecognitionFileLog log = new BGAcademicRecognitionFileLogImpl();
	        log.setFileName((String) session.getAttribute(attributeFileName));
	        log.setRecordCount(records.size());
	        log.setUploadDate(new Date());
	        log.setUploadUserId(user.getUserId());
	        logDP.saveBGAcademicRecognitionFileLog(log);
	        
	        SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
	        request.setAttribute(WebKeys.SYSTEM_MESSAGE, systemMessageWebmodel);
	        session.removeAttribute(attributeName);
	        session.removeAttribute(attributeFileName);
	        try {
                response.sendRedirect("/nacid/control/bgacademicrecognition/list?reload=1");
            } catch (IOException e) {
                throw Utils.logException(e);
            }
	    }
	            
 	}
	@Override
	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
	    String operationName = getOperationName(request);
	    if ("back".equals(operationName)) {
	        request.getSession().removeAttribute(attributeName);
            try {
                response.sendRedirect("/nacid/control/bgacademicrecognition/list?reload=1");
            } catch (IOException e) {
                throw Utils.logException(e);
            }
	    } else {
	        handleNew(request, response);
	    }
	}
	/*public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		//NacidDataProvider nacidDataProvider = getNacidDataProvider();
		request.setAttribute(attributeName, new BGAcademicRecognitionInfoImpl());
		request.setAttribute(WebKeys.NEXT_SCREEN, nextScreen);
		generateBGUniversitiesCombobox(null, getNacidDataProvider().getUniversityDataProvider(), request);
	}

	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		
		int id = DataConverter.parseInt(request.getParameter("id"), -1);
		if (id <= 0) {
			throw new UnknownRecordException("Unknown university ID:" + id);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, nextScreen);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();

		AcademicRecognitionDataProvider dp = nacidDataProvider.getAcademicRecognitionDataProvider();
		BGAcademicRecognitionInfoImpl record = dp.getAcademicRecognition(id);
		request.setAttribute(attributeName, record);
		
		request.setAttribute("recognizedUniversities", record);
		generateBGUniversitiesCombobox(record.getRecognizedUniversityId(), getNacidDataProvider().getUniversityDataProvider(), request);
	}*/

	public void createTable(HttpServletRequest request, HttpServletResponse response,  List<BGAcademicRecognitionInfoImpl> records) {
		
        TableFactory tableFactory = TableFactory.getInstance();
        Table table = tableFactory.createTable();

        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APPLICANT, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_CITIZENSHIP, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_EDUCATION_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DIPLOMA_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DIPLOMA_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DIPLOMA_DATE, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_PROTOCOL_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DENIAL_PROTOCOL_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_RECOGNIZED_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_SIMILAR, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);

        
        resetTableData(table, records);

		
		// TableState settings
		TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на признати дипломи, които ще бъдат въведени в базата");
		// webmodel.setColumnFormatter("userDate",
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		webmodel.setHasOperationsColumn(true);
		webmodel.hideOperation(TableWebModel.OPERATION_NAME_VIEW);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "bgacademicrecognition_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = new FiltersWebModel();
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}

	/*public void handleSave(HttpServletRequest request, HttpServletResponse response) {
	    BGAcademicRecognitionInfoImpl record = (BGAcademicRecognitionInfoImpl) request.getAttribute(attributeName);
	    if (record == null) {
	        throw new UnknownRecordException("Unknown bg academic recognition...");
	    }
	    AcademicRecognitionDataProvider dp = getNacidDataProvider().getAcademicRecognitionDataProvider();
	    dp.saveBGAcademicRecognitionRecord(record);
	    request.setAttribute(attributeName, record);
	    request.getSession().removeAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION);
	    resetTableData(request);
	    request.setAttribute(WebKeys.NEXT_SCREEN, nextScreen);
	    
	    SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
	    request.setAttribute(WebKeys.SYSTEM_MESSAGE, systemMessageWebmodel);
	    generateBGUniversitiesCombobox(record.getRecognizedUniversityId(), getNacidDataProvider().getUniversityDataProvider(), request);
	}

	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int universityId = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (universityId <= 0) {
	      throw new UnknownRecordException("Unknown university Id:" + universityId);
	    }
	    
	    UniversityDataProvider unDP = getNacidDataProvider().getUniversityDataProvider();
	    unDP.disableUniversity(universityId);
	    
	    request.getSession().removeAttribute(WebKeys.TABLE_BG_ACADEMIC_RECOGNITION);
	    handleList(request, response);
	}*/
	
	private void resetTableData(Table table, List<BGAcademicRecognitionInfoImpl> records) {
		table.emtyTableData();
		if (records != null) {
			for (BGAcademicRecognitionInfo acRec : records) {
				try {
					
					table.addRow(acRec.getId(), acRec.getApplicant(), acRec.getCitizenship(), acRec.getUniversity(), acRec.getUniversityCountry(), 
					        acRec.getEducationLevel(),
							acRec.getDiplomaSpeciality(), acRec.getDiplomaNumber(), acRec.getDiplomaDate(),
							acRec.getProtocolNumber(), acRec.getDenialProtocolNumber(), 
							acRec.getRecognizedSpeciality(), acRec.getHasSimilar());
				} catch (IllegalArgumentException e) {
				    throw Utils.logException(e);
				} catch (CellCreationException e) {
				    throw Utils.logException(e);
				}
			}
		}
	}
	/*private static void generateBGUniversitiesCombobox(Integer selectedUniversityId, UniversityDataProvider udp, HttpServletRequest request) {
	    List<University> universities = udp.getUniversities(Country.COUNTRY_ID_BULGARIA, false);
	    ComboBoxUtils.generateComboBox(selectedUniversityId, universities, request, "recognizedUniversities", true, "getId", "getBgName");
	}*/
	
}
