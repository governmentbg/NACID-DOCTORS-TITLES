package com.nacid.web.model.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.inquires.ApplicationStatusAndLegalReasons;
import com.nacid.bl.inquires.ApplicationStatusFromCommissionInquire;
import com.nacid.bl.inquires.ApplicationStatusFromCommonInquire;
import com.nacid.bl.inquires.CommonInquireRequest;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.inquires.CommissionInquireHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class CommonInquireWebModel extends CommonInquireRequest {

	public CommonInquireWebModel(HttpServletRequest request, Integer jointDegreeFlag) {
		applicationTypeEntryNumSeries = InquiresUtils.generateApplicationTypeEntryNumSeries(request);

        eSubmited = DataConverter.parseBoolean(request.getParameter("eSubmited"));
		Integer eSigned = DataConverter.parseInteger(request.getParameter("eSigned"), null);
		if (eSubmited && eSigned != null) {
			this.eSigned = DataConverter.parseBoolean(eSigned.toString());
		}
		startSessionId = DataConverter.parseInteger(request.getParameter("start_session_id"), null);
		endSessionId = DataConverter.parseInteger(request.getParameter("end_session_id"), null);
		commissionDateFrom = DataConverter.parseDate(request.getParameter("commissionDateFrom"));
		commissionDateTo = DataConverter.parseDate(request.getParameter("commissionDateTo"));
		onlyCommissionApplications = DataConverter.parseBoolean(request.getParameter("commission_checkbox"));



		applicationStatuses = generateApplicationStatusesForCommonInquire(request);
		
		
		int commissionStatusesCount = DataConverter.parseInt(request.getParameter("commission_status_ids_count"), 0);
		for (int i = 0; i < commissionStatusesCount; i++) {
			Integer appStatusId = DataConverter.parseInteger(request.getParameter("commission_status_id" + i), null);
			if (appStatusId != null) {
				List<Integer> legalReasonIds = InquiresUtils.generateElementList(request, "legalReasoncommission_" + i + "Ids", "legalReasoncommission_" + i);
				ApplicationStatusAndLegalReasons appStatus = new ApplicationStatusAndLegalReasons(appStatusId, legalReasonIds);
				Integer joinTypeId = DataConverter.parseInteger(request.getParameter("commission_join_type" + i), null);
				ApplicationStatusAndLegalReasons joinStatus = null;
				if (joinTypeId != null) {
					Integer joinStatusId = DataConverter.parseInteger(request.getParameter("commission_join_type_status_id" + i), null);
					if (joinStatusId != null) {
						List<Integer> joinLegalReasonIds = InquiresUtils.generateElementList(request, "legalReasoncommission_join_type_" + i + "Ids", "legalReasoncommission_join_type_" + i);
						joinStatus = new ApplicationStatusAndLegalReasons(joinStatusId, joinLegalReasonIds);
					}	
				}
				commissionApplicationStatuses.add(new ApplicationStatusFromCommissionInquire(appStatus, joinTypeId, joinStatus));
			}
		}
		//dobavq vsi4ki commission statusi za filtrirane, no samo ako e checknata otmetkata onlyCommissionApplications!!!
		if (onlyCommissionApplications && commissionApplicationStatuses.size() == 0) {
			List<Integer> statusIds = CommissionInquireHandler.getAvailableStatusIds();
			for (Integer i:statusIds) {
				ApplicationStatusAndLegalReasons s = new ApplicationStatusAndLegalReasons(i, null);
				commissionApplicationStatuses.add(new ApplicationStatusFromCommissionInquire(s, null, null));
			}
		}
		if (commissionApplicationStatuses.size() == 0) {
			commissionApplicationStatuses = null;
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

		TreeSet<Integer> _trainingInstitutionCountryIds = new TreeSet<Integer>();
		TreeSet<Integer> _trainingInstitutionIds = new TreeSet<Integer>();
		if (!InquiresUtils.generateInstitutionCountryAndInstitutionIds(request, _trainingInstitutionIds, _trainingInstitutionCountryIds, nacidDataProvider) && _trainingInstitutionIds.size() == 0) {
			trainingInstitutionIds = null;
		} else {
			trainingInstitutionIds = new ArrayList<>(_trainingInstitutionIds);
		}
		trainingInstitutionCountryIds = _trainingInstitutionCountryIds.size() == 0 ? null : new ArrayList<>(_trainingInstitutionCountryIds);

		applicantCountryIds = InquiresUtils.generateElementList(request, "applicantCountryIds", "applicantCountry");
		
		

		/**Diploma speciality/originalSpeciality/qualification/edu level **/
		diplomaSpecialityIds = InquiresUtils.generateSpecialityIds(request, nacidDataProvider, "diplomaSpeciality");

		diplomaOriginalSpecialityIds = InquiresUtils.generateElementList(request, "diplomaOriginalSpecialityIds", "diplomaOriginalSpecialityId");
		//Dobavqne na originalnite specialnosti, izbrani s maska
		diplomaOriginalSpecialityIds = InquiresUtils.addMaskedElements(request, "diplomaOriginalSpecialityNamesIds", "diplomaOriginalSpeciality", diplomaOriginalSpecialityIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY);

		diplomaOriginalQualificationIds = InquiresUtils.generateElementList(request, "diplomaOriginalQualificationIds", "diplomaOriginalQualificationId");
		//Dobavqne na originalnite kvalifikacii, izbrani s maska
		diplomaOriginalQualificationIds = InquiresUtils.addMaskedElements(request, "diplomaOriginalQualificationNamesIds", "diplomaOriginalQualification", diplomaOriginalQualificationIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION);

		diplomaQualificationIds = InquiresUtils.generateElementList(request, "diplomaQualificationIds", "diplomaQualificationId");
		//Dobavqne na kvalifikaciite, izbrani s maska
		diplomaQualificationIds = InquiresUtils.addMaskedElements(request, "diplomaQualificationNamesIds", "diplomaQualification", diplomaQualificationIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION);
		
		diplomaEducationLevelIds = InquiresUtils.generateElementList(request, "diplomaEducationLevelIds", "diplomaEducationLevel");

		diplomaOriginalEducationLevelIds = InquiresUtils.generateElementList(request, "diplomaOriginalEduLevelIds", "diplomaOriginalEduLevelId");
		//Dobavqne na originalnite OKS-ta, izbrani s maska
		diplomaOriginalEducationLevelIds = InquiresUtils.addMaskedElements(request, "diplomaOriginalEduLevelNamesIds", "diplomaOriginalEduLevel", diplomaOriginalEducationLevelIds, NomenclaturesDataProvider.NOMENCLATURE_ORIGINAL_EDUCATION_LEVEL);

		/**End of diploma speciality/qualification/edu level **/

		/**Recognized speciality/qualification/edu level **/
		recognizedSpecialityIds = InquiresUtils.generateSpecialityIds(request, nacidDataProvider, "recognizedSpeciality");
		
		recognizedQualificationIds = InquiresUtils.generateElementList(request, "recognizedQualificationIds", "recognizedQualificationId");
		//Dobavqne na kvalifikaciite, izbrani s maska
		recognizedQualificationIds = InquiresUtils.addMaskedElements(request, "recognizedQualificationNamesIds", "recognizedQualification", recognizedQualificationIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION);

		recognizedEducationLevelIds = InquiresUtils.generateElementList(request, "recognizedEducationLevelIds", "recognizedEducationLevel");
		
		/** End of recognized speciality/qualification/edu level **/
		
		this.appDateFrom = DataConverter.parseDate(request.getParameter("appDateFrom"));
		this.appDateTo = DataConverter.parseDate(request.getParameter("appDateTo"));
		
		this.diplomaDateFrom = DataConverter.parseYear(request.getParameter("diplomaDateFrom"));
		this.diplomaDateTo = DataConverter.parseYear(request.getParameter("diplomaDateTo"));
		if (diplomaDateTo != null) {
		    Calendar cal = new GregorianCalendar();
		    cal.setTime(diplomaDateTo);
		    cal.add(Calendar.YEAR, 1);
		    cal.add(Calendar.SECOND, -1);
		    diplomaDateTo = cal.getTime();
		}
        attachmentDocumentTypeIds = InquiresUtils.generateElementList(request, "attachmentDocumentTypeIds", "attachmentDocumentType");


        userCreatedIds = InquiresUtils.generateElementList(request, "userCreatedIds", "userCreated");
        responsibleUserIds = InquiresUtils.generateElementList(request, "responsibleUserIds", "responsibleUser");
        this.jointDegreeFlag = jointDegreeFlag;
        this.onlyUniversitiesWithPublicRegisters = DataConverter.parseBoolean(request.getParameter("universities_only_with_diploma_registers"));
        this.documentReceiveMethods = InquiresUtils.generateElementList(request, "documentReceiveMethodIds", "documentReceiveMethod");
        this.companyApplicantRequest = InquiresUtils.generateCompanyApplicantRequest(request, nacidDataProvider);
	}

	public static List<ApplicationStatusFromCommonInquire> generateApplicationStatusesForCommonInquire(HttpServletRequest request) {
		List<ApplicationStatusFromCommonInquire> actualApplicationStatuses = new ArrayList<>();
		int statusesCount = DataConverter.parseInt(request.getParameter("status_ids_count"), 0);
		for (int i = 0; i < statusesCount; i++) {
			Integer currentStatusId = DataConverter.parseInteger(request.getParameter("status_id" + i), null);
			Integer finalStatusId = DataConverter.parseInteger(request.getParameter("final_status_id" + i), null);
			Integer docflowStatusId = DataConverter.parseInteger(request.getParameter("docflow_status_id" + i), null);


            if (currentStatusId != null || finalStatusId != null || docflowStatusId != null) {
				ApplicationStatusAndLegalReasons applicationStatus = null;
				ApplicationStatusAndLegalReasons finalStatus = null;
				Date finalStatusDateFrom = null;
				Date finalStatusDateTo = null;
				Date statusDateFrom = null;
				Date statusDateTo = null;
                Date docflowStatusDateFrom = null;
                Date docflowStatusDateTo = null;

				if (currentStatusId != null) {
					List<Integer> legalReasonIds = InquiresUtils.generateElementList(request, "legalReason" + i + "Ids", "legalReason" + i);
					applicationStatus = new ApplicationStatusAndLegalReasons(currentStatusId, legalReasonIds);
					statusDateFrom = DataConverter.parseDate(request.getParameter("statusDateFrom" + i));
					statusDateTo = DataConverter.parseDate(request.getParameter("statusDateTo" + i));
				}



				if (finalStatusId != null) {
					List<Integer> finalLegalReasonIds = InquiresUtils.generateElementList(request, "legalReasonfinal_" + i + "Ids", "legalReasonfinal_" + i);
					finalStatusDateFrom = DataConverter.parseDate(request.getParameter("finalStatusDateFrom" + i));
					finalStatusDateTo = DataConverter.parseDate(request.getParameter("finalStatusDateTo" + i));
					finalStatus = new ApplicationStatusAndLegalReasons(finalStatusId, finalLegalReasonIds);
				}
				if (docflowStatusId != null) {
                    docflowStatusDateFrom = DataConverter.parseDate(request.getParameter("docflowStatusDateFrom" + i));
                    docflowStatusDateTo = DataConverter.parseDate(request.getParameter("docflowStatusDateTo" + i));

                }
				boolean onlyActualStatus = DataConverter.parseBoolean(request.getParameter("only_actual_status" + i));



				actualApplicationStatuses.add(new ApplicationStatusFromCommonInquire(onlyActualStatus, applicationStatus, statusDateFrom, statusDateTo, finalStatus, finalStatusDateFrom, finalStatusDateTo, docflowStatusId, docflowStatusDateFrom, docflowStatusDateTo));
			}
		}
		return actualApplicationStatuses;
	}
}

