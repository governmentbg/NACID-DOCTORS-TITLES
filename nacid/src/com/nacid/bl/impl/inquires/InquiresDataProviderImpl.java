package com.nacid.bl.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.ApplicationDetailsForReportImpl;
import com.nacid.bl.impl.applications.ApplicationsDataProviderImpl;
import com.nacid.bl.inquires.*;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.data.applications.ApplicationRecord;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.data.inquire.BGAcademicRecognitionForReportRecord;
import com.nacid.data.inquire.ExpertInquireResultRecord;
import com.nacid.db.applications.ApplicationsDB;
import com.nacid.db.inquires.InquiresDB;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InquiresDataProviderImpl implements InquiresDataProvider {
	 /**
     * shte okazva za koi status kolko nazad v history-to trqbva da se vry6ta za da se nameri konkretniq status, koito ni interesuva! 
     */
	private static final Map<Integer, Integer> appStatusIdToBackwardStatusesCount = new HashMap<Integer, Integer>();
	//TODO:FIX THIS. Veche nqma statusi arhivirano i prikliucheno!!! te sa dokumentooborotni statusi. Neshto generalno trqbva da se promeni v tazi spravka!!!
	/*static {
		appStatusIdToBackwardStatusesCount.put(ApplicationStatus.APPLICATION_FINISHED_STATUS_CODE, 1);
		appStatusIdToBackwardStatusesCount.put(ApplicationStatus.APPLICATION_ARCHIVED_STATUS_CODE, 2);	
	}*/
	
	
    private NacidDataProviderImpl nacidDataProvider;
    private InquiresDB db;
    public InquiresDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new InquiresDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }


    @Override
    public List<ApplicationForInquireRecord> getApplicationsForCommissionInquire(CommissionInquireRequest request) {
        try {
            return db.getApplicationsForInquireByIds(getApplicationIdsForCommissionInquire(request));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForCommissionInquire(CommissionInquireRequest request) {

        List<Integer> applications = getApplicationIdsForCommissionInquire(request);
        return generateApplicationDetalsForReport(applications);
    }
    
    private List<Integer> getApplicationIdsForCommissionInquire(CommissionInquireRequest request) {
    	try {
    		if ((request.getUniversityCountryIds() == null || request.getUniversityCountryIds().size() == 0) && (request.getUniversityIds() != null && request.getUniversityIds().size() == 0)) {
				return null;
			}
    		if (request.getDiplomaSpecialityIds() != null && request.getDiplomaSpecialityIds().size() == 0) {
				return null;
			}
    		if (request.getRecognizedSpecialityIds() != null && request.getRecognizedSpecialityIds().size() == 0) {
				return null;
			}
    		//			diplomaDateFrom, diplomaDateTo
    		//Pyrvo filtrira zaqvleniqta po vsi4ki drugi kriterii bez statusa!
    		List<Integer> records = db.getApplicationRecordIdsForCommissionInquire(request.getApplicationTypeEntryNumSeries(), request.getJointDegreeFlag(), request.getStartSessionId(), request.getEndSessionId(),
					Utils.getSqlDate(request.getDateFrom()), Utils.getSqlDate(request.getDateTo()), Utils.getSqlDate(request.getDiplomaDateFrom()), Utils.getSqlDate(request.getDiplomaDateTo()),
					request.getApplicantCountryIds(), request.getUniversityCountryIds(), request.getUniversityIds(), request.getDiplomaSpecialityIds(), request.getDiplomaQualificationIds(),
					request.getDiplomaEducationLevelIds(), request.getRecognizedSpecialityIds(), request.getRecognizedQualificationIds(), request.getRecognizedEducationLevelIds(), Utils.getSqlDate(request.getAppDateFrom()), Utils.getSqlDate(request.getAppDateTo()));
    		if (records.size() == 0) {
    			return null;
    			
    		}
    		//Sled tova filtrira zaqvleniqta samo po status
    		Set<Integer> applicationIdsFilteredByStatus = getApplicationIdsFilteredByStatuses(request.getDateFrom(), request.getDateTo(), request.getStartSessionId(), request.getEndSessionId(), request.getApplicationStatuses());
    		if (applicationIdsFilteredByStatus == null) {
    			return null;
    		}



            List<Integer> res = new ArrayList<Integer>();
    		//Nakraq pravi se4enie na dvete filtriraniq
    		for (Integer r:records) {
    			if (applicationIdsFilteredByStatus.contains(r)) {
                    res.add(r);
    			}
    		}

    		return res.size() == 0 ? null : res;
    		 
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    public List<ApplicationForInquireRecord> getApplicationsForCommonInquire(CommonInquireRequest request) {
        try {
            return db.getApplicationsForInquireByIds(getApplicationIdsForCommonInquire(request ));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForCommonInquire(CommonInquireRequest request) {
        List<Integer> applications = getApplicationIdsForCommonInquire(request);
        return generateApplicationDetalsForReport(applications);

    }

    private List<Integer> getApplicationIdsForCommonInquire(CommonInquireRequest request) {
		try {
			if ((request.getUniversityCountryIds() == null || request.getUniversityCountryIds().size() == 0) && (request.getUniversityIds() != null && request.getUniversityIds().size() == 0)) {
				return null;
			}
			if ((request.getTrainingInstitutionCountryIds() == null || request.getTrainingInstitutionCountryIds().size() == 0) && (request.getTrainingInstitutionIds() != null && request.getTrainingInstitutionIds().size() == 0)) {
				return null;
			}
			if (request.getDiplomaSpecialityIds() != null && request.getDiplomaSpecialityIds().size() == 0) {
				return null;
			}
			if (request.getRecognizedSpecialityIds() != null && request.getRecognizedSpecialityIds().size() == 0) {
				return null;
			}
			//Pyrvo vzema zapisite bez da filtrira po commission status
			List<Integer> recordIds = db.getApplicationRecordsForCommonInquire(request.getApplicationTypeEntryNumSeries(), request.isOnlyCommissionApplications(), request.getStartSessionId(), request.getEndSessionId(), Utils.getSqlDate(request.getCommissionDateFrom()), Utils.getSqlDate(request.getCommissionDateTo()), Utils.getSqlDate(request.getDiplomaDateFrom()), Utils.getSqlDate(request.getDiplomaDateTo()), request.getJointDegreeFlag(), request.isESubmited(), request.getESigned(), request.getApplicantCountryIds(),
					request.getUniversityCountryIds(), request.getUniversityIds(), request.getDiplomaSpecialityIds(), request.getDiplomaOriginalSpecialityIds(),
					request.getDiplomaQualificationIds(), request.getDiplomaOriginalQualificationIds(),
					request.getDiplomaEducationLevelIds(), request.getDiplomaOriginalEducationLevelIds(),
					request.getRecognizedSpecialityIds(), request.getRecognizedQualificationIds(), request.getRecognizedEducationLevelIds(), Utils.getSqlDate(request.getAppDateFrom()), Utils.getSqlDate(request.getAppDateTo()), request.getAttachmentDocumentTypeIds(), request.getResponsibleUserIds(), request.getUserCreatedIds(), request.getTrainingInstitutionCountryIds(), request.getTrainingInstitutionIds(), request.getApplicationStatuses(),
					request.isOnlyUniversitiesWithPublicRegisters(), request.getDocumentReceiveMethods(), request.getCompanyApplicantRequest());
			if (recordIds.size() == 0) {
				return null;
			}

			
			//Sled tova filtrira zaqvleniqta po commissionStatuses
    		Set<Integer> applicationIdsFilteredByCommissionStatus = null;
    		if (request.getCommissionApplicationStatuses() != null) {
    			applicationIdsFilteredByCommissionStatus = getApplicationIdsFilteredByStatuses(request.getCommissionDateFrom(), request.getCommissionDateTo(), request.getStartSessionId(), request.getEndSessionId(), request.getCommissionApplicationStatuses());
    			//ako nqma nito edno zaqvlenie, filtrirano s tozi kriterii...
    			if (applicationIdsFilteredByCommissionStatus == null) {
        			return null;
        		}
    		}
			
			
			//sled tova pravi se4enie mejdu trite filtera - tezi ot records, tezi filtrirani samo po actual status i tezi, filtrirani samo po status ot comisiq
			List<Integer> resultIds = new ArrayList<Integer>();
			for (Integer r:recordIds) {
				if ( 

						(applicationIdsFilteredByCommissionStatus == null || applicationIdsFilteredByCommissionStatus.contains(r))
				) {
                    resultIds.add(r);
				}
			}
            return resultIds;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}


    public List<ApplicationForInquireRecord> getApplicationsForEmployeeInquire(EmployeeInquireRequest request) {
        try {
            return db.getApplicationsForInquireByIds(getApplicationIdsForEmployeeInquire(request));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForEmployeeInquire(EmployeeInquireRequest request) {
        List<Integer> applications = getApplicationIdsForEmployeeInquire(request);
        return generateApplicationDetalsForReport(applications);
    }
    public List<Integer> getApplicationIdsForEmployeeInquire(EmployeeInquireRequest request) {
        try {
            List<Integer> recordIds = db.getApplicationRecordIdsForEmployeeInquire(request.getApplicationTypeEntryNumSeries(), request.getResponsibleUserIds(), request.getUserId(), request.getApplicationNum(), Utils.getSqlDate(request.getDateFrom()), Utils.getSqlDate(request.getDateTo()), request.getApplicationStatuses());
            return recordIds;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


	public List<ExpertInquireResult> getExpertInquireResult(ExpertInquireRequest request) {
		try {
			List<ExpertInquireResultRecord> records = db.getExpertInquiresResult(request.getApplicationTypeEntryNumSeries(), request.getResponsibleUserIds(), request.getUserId(), request.getApplicationNum(), Utils.getSqlDate(request.getDateFrom()), Utils.getSqlDate(request.getDateTo()), request.getApplicationStatuses());
			List<ExpertInquireResult> res = records
					.stream()
					.collect(Collectors.groupingBy(r -> new ExpertInquireResultImpl(r.getExpertId(), r.getExpertNames(), null), Collectors.mapping(r -> (ExpertInquireResult.ExpertInquireApplication) new ExpertInquireApplicationImpl(r.getApplicationId(), r.getAppNum(), r.getAppDate()), Collectors.toList())))
					.entrySet()
					.stream().map(r -> new ExpertInquireResultImpl(r.getKey().getExpertId(), r.getKey().getExpertNames(), r.getValue()))
					.map(r -> (ExpertInquireResult)r)
					.collect(Collectors.toList());
			return res;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}







    public List<ApplicationForInquireRecord> getApplicationsForApplicantInquire(ApplicantInquireRequest request) {
        try {
            return db.getApplicationsForInquireByIds(getApplicationIdsForApplicantInquire(request));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForApplicantInquire(ApplicantInquireRequest request) {
        return generateApplicationDetalsForReport(getApplicationIdsForApplicantInquire(request));
    }
	public List<Integer> getApplicationIdsForApplicantInquire(ApplicantInquireRequest request) {
		try {
			List<Integer> recordIds = db.getApplicationRecordIdsForApplicantInquire(request.getApplicationTypeEntryNumSeries(), request.getApplicantFname(), request.getApplicantSname(), request.getApplicantLname(), request.getApplicantPersonalId(), request.getApplicantCompanyName(), request.getApplicantCompanyEik(), request.getOwnerFname(), request.getOwnerSname(), request.getOwnerLname(), request.getOwnerPersonalId(), request.getDiplFName(),
					request.getDiplSName(), request.getDiplLName(), request.getReprFName(), request.getReprSName(), request.getReprLName(), request.getReprPersonalId(), request.getRepresentativeCompany(),
					request.getApplicationNum(), Utils.getSqlDate(request.getDateFrom()), Utils.getSqlDate(request.getDateTo()));
            return recordIds;

		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

    public List<ApplicationForInquireRecord> getApplicationsForInquiryInquire(InquiryInquireRequest request) {
        try {
            return db.getApplicationsForInquireByIds(getApplicationIdsForInquiryInquire(request));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
	
	public List<Integer> getApplicationIdsForInquiryInquire(InquiryInquireRequest request) {
		try {
			List<Integer> recordIds = db.getApplicationRecordIdsForInquiryInquire(request.getApplicationTypeEntryNumSeries(), request.getJointDegreeFlag(), request.getDocumentTypeIds(), request.getUniversityCountryIds(), request.getUniversityIds(), request.getEventStatusIds());
			return recordIds;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	public List<ApplicationDetailsForReport> getApplicationDetailsForReportForInquiryInquire(InquiryInquireRequest request) {
		return generateApplicationDetalsForReport(getApplicationIdsForInquiryInquire(request));
	}
	
	private List<ApplicationDetailsForReport> generateApplicationDetalsForReport(List<Integer> applicationIds) {
    	try {
            if (applicationIds == null) {
                return null;
            }
            List<ApplicationDetailsForReport> result = new ArrayList<ApplicationDetailsForReport>();

            List<ApplicationRecord> records = db.selectRecords(ApplicationRecord.class, "id in (" + StringUtils.join(applicationIds, ", ") + " )");

            List<Application> apps = ApplicationsDataProviderImpl.generateApplications(nacidDataProvider.getApplicationsDataProvider().getApplicationsDB(), records, nacidDataProvider);
            for (Application a:apps) {
                result.add(new ApplicationDetailsForReportImpl(nacidDataProvider, a));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }

	/**
     * vry6ta zaqvleniqta filtrirani sys statuses
     * @param statuses
     * @return
     * @throws SQLException
     */
    public Set<Integer> getApplicationIdsFilteredByStatuses(Date dateFrom, Date dateTo, Integer startSessionId, Integer endSessionId,List<ApplicationStatusFromCommissionInquire> statuses) throws SQLException {
    	if (statuses == null) {
    		return null;
    	}
    	ApplicationsDataProviderImpl applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		ApplicationsDB applicationsDb = applicationsDataProvider.getApplicationsDB();
    	Set<Integer> result = new HashSet<Integer>();
    	
    	for (ApplicationStatusFromCommissionInquire s: statuses) {
    		ApplicationStatusAndLegalReasons appStatus = s.getStatus();
    		/**
    		 * 1.pyrvo se vzemat zaqvleniqta sys status = tozi v getStatus();. 
    		 * 2.ako ima vyveden joinType i joinStatus != null, se vzemat zaqvleniqta koito imat/nqmat(v zavisimost ot joinType-a) status-a v joinStatus.
    		 * 3.pravi se se4enie mejdu 2ta vyrnati lista.
    		 * 4.se4enieto se obedinqva s result-a
    		 */
    		
    		List<Integer> statIds = applicationsDb.getAppStatusHistoryRecords(Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), startSessionId, endSessionId, appStatus.getStatusId(), appStatus.getLegalReasons(), true);
    		if (statIds.size() == 0) {
    			continue;
    		}
    		List<Integer> joinIds = null;
    		if (s.getJoinType() != null && s.getJoinType() != ApplicationStatusFromCommissionInquire.JOIN_TYPE_NONE && s.getJoinStatus() != null) {
    			ApplicationStatusAndLegalReasons joinStatus = s.getJoinStatus();	
    			joinIds = applicationsDb.getAppStatusHistoryRecords(Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), startSessionId, endSessionId,joinStatus.getStatusId(), joinStatus.getLegalReasons(), s.getJoinType() == ApplicationStatusFromCommissionInquire.JOIN_TYPE_AND ? true : false);
    			if (joinIds.size() == 0) {
    				continue;
    			}
    		}
    		if (joinIds != null) {
    			statIds.retainAll(joinIds);
    		}
    		result.addAll(statIds);
    	}
    	return result.size() == 0 ? null : result;
    }
	public static void main(String[] args) {
		BasicConfigurator.configure();
		InquiresDataProvider inquiresDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource(/*"jdbc:postgresql://fly.nacid.bg:5432/NACID_development", "postgres", "postgres"*/)).getInquiresDataProvider();
		
		
		
		
		
		
		ApplicationStatusAndLegalReasons currentStatus = new ApplicationStatusAndLegalReasons(1, null);
    	ApplicationStatusAndLegalReasons finalStatus = new ApplicationStatusAndLegalReasons(10, null);
    	ApplicationStatusFromCommissionInquire s1 = new ApplicationStatusFromCommissionInquire(currentStatus, null, null);
    	ApplicationStatusFromCommissionInquire s2 = new ApplicationStatusFromCommissionInquire(finalStatus, null, null);
    	List<ApplicationStatusFromCommissionInquire> statuses = Arrays.asList(new ApplicationStatusFromCommissionInquire(finalStatus, null, null));
    
    	Calendar cal = GregorianCalendar.getInstance();
    	Utils.clearTimeFields(cal);
    	cal.set(Calendar.DATE, 1);
    	cal.set(Calendar.MONTH, 5);
    	cal.set(Calendar.YEAR, 2011);
    	Date dateFrom = cal.getTime();
    	cal.set(Calendar.MONTH, 5);
    	cal.set(Calendar.DATE, 28);
    	Date dateTo = cal.getTime();
//    	List<ApplicationForInquireRecord> apps = inquiresDataProvider.getApplicationsForCommonInquire(Arrays.asList(ApplicationType.RUDI_APPLICATION_TYPE), false, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, dateFrom, dateTo, null, null, null, Arrays.asList(359), Arrays.asList(1,2,3));
    	
    	
    	//List<Application> apps = inquiresDataProvider.getApplicationsForCommissionInquire(false, null, null, null, null, null, Arrays.asList(s1, s2), null, null, null, null, null, null, null, null, null);
    	
//		System.out.println(apps == null ? "null" : apps.size());
		
    	
    	
		System.out.println("end...");
	}

	public List<BGAcademicRecognitionForReportRecord> getBgAcademicRecognitionForReportRecords(List<String> ownerNames, List<String> citizenship, List<String> university,
																							   List<String> universityCountry, List<String> diplomaSpeciality, List<String> educationLevel, String protocolNumber,
																							   String denialProtocolNumber, List<String> recognizedSpeciliaty,
																							   List<Integer> recognizedUniversityIds,
																							   String outputNumber, String inputNumber, List<Integer> recognitionStatusIds) {
		try {
			return db.getBgAcademicRecognitionForReportRecords(ownerNames, citizenship, university, universityCountry, diplomaSpeciality, educationLevel, protocolNumber, denialProtocolNumber, recognizedSpeciliaty, recognizedUniversityIds, outputNumber, inputNumber, recognitionStatusIds);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}


	
   
    
}
