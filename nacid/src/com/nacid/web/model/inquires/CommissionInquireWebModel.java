package com.nacid.web.model.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.inquires.ApplicationStatusAndLegalReasons;
import com.nacid.bl.inquires.ApplicationStatusFromCommissionInquire;
import com.nacid.bl.inquires.ApplicationTypeAndEntryNumSeries;
import com.nacid.bl.inquires.CommissionInquireRequest;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.inquires.CommissionInquireHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class CommissionInquireWebModel extends CommissionInquireRequest {

	public CommissionInquireWebModel(HttpServletRequest request, Integer jointDegreeFlag) {
		applicationTypeEntryNumSeries = InquiresUtils.generateApplicationTypeEntryNumSeries(request);
        startSessionId = DataConverter.parseInteger(request.getParameter("start_session_id"), null);
		endSessionId = DataConverter.parseInteger(request.getParameter("end_session_id"), null);
		dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
		dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
		Date d = DataConverter.parseYear(request.getParameter("diplomaDateFrom"));
		if (d != null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(d);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			d = cal.getTime();
		}
		diplomaDateFrom = d;
		
		d = DataConverter.parseYear(request.getParameter("diplomaDateTo"));
		if (d != null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(d);
			cal.set(Calendar.DATE, 31);
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			d = cal.getTime();
		}
		diplomaDateTo = d;
		
		
		int statusesCount = DataConverter.parseInt(request.getParameter("statuses_count"), 0);
		for (int i = 0; i < statusesCount; i++) {
			Integer appStatusId = DataConverter.parseInteger(request.getParameter("status_id" + i), null);
			if (appStatusId != null) {
				List<Integer> legalReasonIds = InquiresUtils.generateElementList(request, "legalReason" + i + "Ids", "legalReason" + i);
				ApplicationStatusAndLegalReasons appStatus = new ApplicationStatusAndLegalReasons(appStatusId, legalReasonIds);
				Integer joinTypeId = DataConverter.parseInteger(request.getParameter("join_type" + i), null);
				ApplicationStatusAndLegalReasons joinStatus = null;
				if (joinTypeId != null) {
					Integer joinStatusId = DataConverter.parseInteger(request.getParameter("join_type_status_id" + i), null);
					if (joinStatusId != null) {
						List<Integer> joinLegalReasonIds = InquiresUtils.generateElementList(request, "legalReasonjoin_type_" + i + "Ids", "legalReasonjoin_type_" + i);
						joinStatus = new ApplicationStatusAndLegalReasons(joinStatusId, joinLegalReasonIds);
					}	
				}
				applicationStatuses.add(new ApplicationStatusFromCommissionInquire(appStatus, joinTypeId, joinStatus));
			}
		}
		if (applicationStatuses.size() == 0) {
			List<Integer> statusIds = CommissionInquireHandler.getAvailableStatusIds();
			for (Integer i:statusIds) {
				ApplicationStatusAndLegalReasons s = new ApplicationStatusAndLegalReasons(i, null);
				applicationStatuses.add(new ApplicationStatusFromCommissionInquire(s, null, null));
			}
		}

		universityCountryIds = new ArrayList<>();
		universityIds = new ArrayList<>();
		NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(request.getSession());
		if (!InquiresUtils.generateUniversityCountryAndUniversityIds(request, universityIds, universityCountryIds, nacidDataProvider) && universityIds.size() == 0) {
			universityIds = null;
		}
		if (universityCountryIds.size() == 0) {
			universityCountryIds = null;
		}

		applicantCountryIds = InquiresUtils.generateElementList(request, "applicantCountryIds", "applicantCountry");
		
		

		/**Diploma speciality/qualification/edu level **/
		diplomaSpecialityIds = InquiresUtils.generateSpecialityIds(request, nacidDataProvider, "diplomaSpeciality");
		
		diplomaQualificationIds = InquiresUtils.generateElementList(request, "diplomaQualificationIds", "diplomaQualificationId");
		diplomaQualificationIds = InquiresUtils.addMaskedElements(request, "diplomaQualificationNamesIds", "diplomaQualification", diplomaQualificationIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION);
		
		diplomaEducationLevelIds = InquiresUtils.generateElementList(request, "diplomaEducationLevelIds", "diplomaEducationLevel");
		/**End of diploma speciality/qualification/edu level **/

		/**Recognized speciality/qualification/edu level **/
		recognizedSpecialityIds = InquiresUtils.generateSpecialityIds(request, nacidDataProvider, "recognizedSpeciality");
		
		recognizedQualificationIds = InquiresUtils.generateElementList(request, "recognizedQualificationIds", "recognizedQualificationId");
		recognizedQualificationIds = InquiresUtils.addMaskedElements(request, "recognizedQualificationNamesIds", "recognizedQualification", recognizedQualificationIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION);
		
		recognizedEducationLevelIds = InquiresUtils.generateElementList(request, "recognizedEducationLevelIds", "recognizedEducationLevel");
		
		/** End of recognized speciality/qualification/edu level **/

		appDateFrom = DataConverter.parseDate(request.getParameter("appDateFrom"));
		appDateTo = DataConverter.parseDate(request.getParameter("appDateTo"));
		this.jointDegreeFlag = jointDegreeFlag;
	}

	public void setApplicationTypeEntryNumSeries(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNumSeries) {
		this.applicationTypeEntryNumSeries = applicationTypeEntryNumSeries;
	}
}

