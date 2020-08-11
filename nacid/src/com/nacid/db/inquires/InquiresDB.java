package com.nacid.db.inquires;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.inquires.ApplicationStatusFromCommonInquire;
import com.nacid.bl.inquires.ApplicationTypeAndEntryNumSeries;
import com.nacid.bl.inquires.CompanyApplicantRequest;
import com.nacid.bl.nomenclatures.SessionStatus;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.common.IntegerValue;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.data.inquire.BGAcademicRecognitionForReportRecord;
import com.nacid.data.inquire.ExpertInquireResultRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.nacid.bl.inquires.ApplicationTypeAndEntryNumSeries.*;
import static com.nacid.bl.nomenclatures.ApplicationType.RUDI_APPLICATION_TYPE;


public class InquiresDB extends DatabaseService {

    //private static String UPDATE_END_DATE_SQL = "update comission_member set date_to=? where id=?";
	private static String COMMISSION_INQUIRE_SQL = " select distinct application.id as value \n" +
			" from application \n" +
			" left join training_course on (application.training_course_id = training_course.id) \n" +
			" left join diploma_issuer on (training_course.id = diploma_issuer.diploma_id) \n" +
			" left join university on (diploma_issuer.university_id = university.id) \n" +
			" left join nomenclatures.country as uni_country on (uni_country.id = university.country_id) \n" +
			
			" left join training_course_specialities as specialities on (specialities.training_course_id = training_course.id) \n" +
			" left join nomenclatures.speciality as training_speciality on (training_speciality.id = specialities.speciality_id) \n" +
			//" left join nomenclatures.speciality as training_speciality on (training_speciality.id = training_course.speciality_id) \n"+
			
			" left join nomenclatures.qualification as training_qualification on (training_qualification.id = training_course.qualification_id) \n"+
			" left join nomenclatures.edu_level as training_edu_level on (training_edu_level.id = training_course.edu_level_id) \n"+
//			" left join app_status_history as h1 on (h1.application_id = application.id) \n" + 
//			" left join app_status_history as h2 on (h1.application_id = application.id and h1.application_id = h2.application_id and h2.id < h1.id) \n"+
			" left join comission_agenda on (comission_agenda.application_id = application.id) \n"+
			" left join comission_calendar on (comission_calendar.id = comission_agenda.session_id) \n" +
			" left join person as applicant on (applicant.id = training_course.owner_id) \n" +
			" left join recognized_specs on (recognized_specs.training_course_id = application.training_course_id) \n";
	
	
	private static String COMMON_INQUIRE_REPORT_SQL = " select distinct application.id as value \n" +
			" from application \n" +
			" left join eservices.rudi_application on (application.id = rudi_application.application_id) \n" +
			" left join eservices.rudi_signed_docs on (rudi_application.id = rudi_signed_docs.ext_app_id) \n" +
			" left join training_course on (application.training_course_id = training_course.id) \n" + 
			" left join diploma_issuer on (training_course.id = diploma_issuer.diploma_id) \n" +
			" left join diploma_type dt on (dt.id = training_course.diploma_type_id) \n" +
			" left join university on (diploma_issuer.university_id = university.id) \n" +
			" left join training_location on (training_location.training_course_id = training_course.id) \n" +
			" left join training_institution institution on (training_location.training_institution_id = institution.id) \n" +

			" left join nomenclatures.country as uni_country on (uni_country.id = university.country_id) \n" +
    			
            " left join training_course_specialities as specialities on (specialities.training_course_id = training_course.id) \n" +
            " left join nomenclatures.speciality as training_speciality on (training_speciality.id = specialities.speciality_id) \n" +
			//" left join nomenclatures.speciality as training_speciality on (training_speciality.id = training_course.speciality_id) \n"+
			
			" left join nomenclatures.qualification as training_qualification on (training_qualification.id = training_course.qualification_id) \n"+
			" left join nomenclatures.edu_level as training_edu_level on (training_edu_level.id = training_course.edu_level_id) \n"+
			//" left join app_status_history on (app_status_history.application_id = application.id) \n"+
			" left join comission_agenda on (comission_agenda.application_id = application.id) \n"+
			" left join comission_calendar on (comission_calendar.id = comission_agenda.session_id) \n" +
			" left join person as applicant on (applicant.id = training_course.owner_id) \n" +
			" left join recognized_specs on (recognized_specs.training_course_id = application.training_course_id) \n"+
			"join app_status_history current_history on current_history.id = (select id from app_status_history h1 where h1.application_id = application.id order by date_assigned desc, id desc limit 1)\n" +
			"left join app_status_history fsh on fsh.id = application.final_status_history_id\n" +
			"join app_status_history ash on ash.application_id = application.id\n" +
			"join backoffice.app_status_docflow_history current_docflow_history on current_docflow_history.id = (select id from backoffice.app_status_docflow_history h1 where h1.application_id = application.id order by date_assigned desc, id limit 1)\n" +
			"join backoffice.app_status_docflow_history adsh on adsh.application_id = application.id";
	private static String APPLICANT_IQNUIRE_REPORT_SQL = " SELECT DISTINCT application.id as value \n" +
	" from application \n" +
			" join training_course as tce on (application.training_course_id = tce.id) \n" +
			" left join person as repr on (repr.id = application.representative_id) \n" +
			" left join person as applicant on (applicant.id = application.applicant_id) \n" +
			" left join backoffice.company as applicant_company on (applicant_company.id = application.applicant_company_id) \n" +
			" left join person as owr on (owr.id = tce.owner_id) \n";

	private static String INQUIRE_TYPE_INQUIRY_SQL = "select distinct application.id as value \n" +
			" from application \n" +
			" left join training_course on (application.training_course_id = training_course.id) \n" +
			" left join diploma_issuer on (training_course.id = diploma_issuer.diploma_id) \n" + 
			" left join university on (diploma_issuer.university_id = university.id) \n" +
			" left join nomenclatures.country as uni_country on (uni_country.id = university.country_id) \n" +
	//		" left join attached_docs on (parent_id = application.id) \n" +
			" left join event on (event.application_id = application.id) \n";
	private static final String BG_ACADEMIC_RECOGNITIONS_INQUIRE_SQL = "select ari.*, uny.bg_name recognized_university_name, rss.name as recognition_status_name\n" +
																		"from bg_academic_recognition_info  ari\n" +
																		"join university uny on uny.id = ari.recognized_university_id\n" +
																		"join nomenclatures.bg_academic_recognition_status rss on rss.id = recognition_status_id\n";


	private static final String EMPLOYEE_INQUIRE_REPORT_SQL = "select distinct application.id as value from application\n" +
																"join app_status_history current_history on current_history.id = (select id from app_status_history h1 where h1.application_id = application.id order by date_assigned desc, id desc limit 1)\n" +
																"left join app_status_history fsh on fsh.id = application.final_status_history_id\n" +
																"join app_status_history ash on ash.application_id = application.id\n" +
																"join backoffice.app_status_docflow_history current_docflow_history on current_docflow_history.id = (select id from backoffice.app_status_docflow_history h1 where h1.application_id = application.id order by date_assigned desc, id limit 1)\n" +
																"join backoffice.app_status_docflow_history adsh on adsh.application_id = application.id";
	private static final String COMMISSION_MEMBER_REPORT_SQL =
			"select distinct application.id as application_id, application.app_num, application.app_date, e.id as expert_id, e.fname||coalesce(' '||e.sname, '')||coalesce(' '||e.lname,'') expert_names " +
					"from application\n" +
					"join application_expert ae on ae.application_id = application.id\n" +
					"join comission_member e on e.id = ae.expert_id\n" +
					"join app_status_history current_history on current_history.id = (select id from app_status_history h1 where h1.application_id = application.id order by date_assigned desc, id desc limit 1)\n" +
					"left join app_status_history fsh on fsh.id = application.final_status_history_id\n" +
					"join app_status_history ash on ash.application_id = application.id\n" +
					"join backoffice.app_status_docflow_history current_docflow_history on current_docflow_history.id = (select id from backoffice.app_status_docflow_history h1 where h1.application_id = application.id order by date_assigned desc, id limit 1)\n" +
					"join backoffice.app_status_docflow_history adsh on adsh.application_id = application.id";

	public InquiresDB(DataSource ds) {
        super(ds);
    }
    public List<Integer> getApplicationRecordIdsForCommissionInquire(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNums, Integer jointDegree, Integer startSessionId, Integer endSessionId, java.sql.Date fromDate, java.sql.Date toDate, java.sql.Date diplomaDateFrom, java.sql.Date diplomaDateTo,/*List<ApplicationStatusFromCommissionInquire> applicationStatuses,*/ List<Integer> applicantCountryIds, List<Integer> universityCountryIds,
																	 List<Integer> universityIds, List<Integer> diplomaSpecialityIds, List<Integer> diplomaQualificationIds, List<Integer> diplomaEducationLevelIds,
																	 List<Integer> recognizedSpecialityIds, List<Integer> recognizedQualificationIds, List<Integer> recognizedEducationLevelIds, Date appDateFrom, Date appDateTo) throws SQLException {
    	List<String> criterias = new ArrayList<String>();
    	List<Object> objects = new ArrayList<Object>();
    	
    	//Vry6tat se samo zavurshilite zasedaniq (sys status = SessionStatus.SESSION_STATUS_PROVEDENO)
    	criterias.add(" comission_calendar.session_status_id = ? "); 
    	objects.add(SessionStatus.SESSION_STATUS_PROVEDENO);
    	addJointDegreeFilter(jointDegree, criterias, objects);
		addApplicationTypeStatement(applicationTypeEntryNums, criterias, objects);
		addEntryNumSeriesStatement(applicationTypeEntryNums, criterias, objects);
    	
    	if (startSessionId != null || endSessionId != null || fromDate != null || toDate != null) {
    		String sql = " application.id in (select application_id from app_status_history where session_id in (select id from comission_calendar where 1 = 1 ";
    		if (startSessionId != null) {
    			sql += " AND session_num >= ? ";
    			objects.add(startSessionId);
    		}
    		if (endSessionId != null) {
    			sql += " AND session_num <= ? ";
    			objects.add(endSessionId);
    		}
    		
    		if (fromDate != null) {
    			sql += " AND session_time >= ? ";
    			objects.add(new Timestamp(fromDate.getTime()));
    		}
    		if (toDate != null) {
    			sql += " AND session_time <= ? ";
    			objects.add(new Timestamp(toDate.getTime()));
    		}
    		
    		sql += " ) )";
    		criterias.add(sql);
    	}
    	
    	
    	
    	/*if (fromDate != null || toDate != null) {
    		String dateCriteria = "";
    		if (fromDate != null) {
    			dateCriteria = " comission_calendar.session_time >= ? " + (toDate == null ? "" : " AND ");
    			objects.add(new Timestamp(fromDate.getTime()));
    		}
    		if (toDate != null) {
    			dateCriteria += " comission_calendar.session_time <= ? ";
    			objects.add(new Timestamp(toDate.getTime()));
    		}
    		criterias.add(dateCriteria);
    	}*/
    	if (diplomaDateFrom != null || diplomaDateTo != null) {
    		String dateCriteria = "";
    		if (diplomaDateFrom != null) {
    			dateCriteria = " training_course.diploma_date >= ? " + (diplomaDateTo == null ? "" : " AND ");
    			objects.add(diplomaDateFrom);
    		}
    		if (diplomaDateTo != null) {
    			dateCriteria += " training_course.diploma_date <= ? ";
    			objects.add(diplomaDateTo);
    		}
    		criterias.add(dateCriteria);
    	}
    	
    	/*if (applicationStatuses != null && applicationStatuses.size() > 0) {
    		List<String> appStatusesSql = new ArrayList<String>();
    		for (ApplicationStatusFromCommissionInquire status:applicationStatuses) {
    			String currentCriteria = "( ( h1.status_id = ? "; 
    			ApplicationStatusAndLegalReasons s = status.getStatus();
    			objects.add(s.getStatusId());
    			if (s.getLegalReasons() != null && s.getLegalReasons().size() > 0) {
    				currentCriteria += " AND  h1.stat_legal_reason_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(s.getLegalReasons(), ",")) + ") " ;
    				objects.addAll(s.getLegalReasons());
    			}
    			currentCriteria += " ) ";
    			ApplicationStatusAndLegalReasons joinStatus = status.getJoinStatus();
    			if (status.getJoinType() != null && joinStatus != null ) {
    				currentCriteria += " AND " + (status.getJoinType() == ApplicationStatusFromCommissionInquire.JOIN_TYPE_NOT ? "NOT" : "") + " (  h2.status_id = ? "; 
        			objects.add(joinStatus.getStatusId());
        			if (joinStatus.getLegalReasons() != null && joinStatus.getLegalReasons().size() > 0) {
        				currentCriteria += " AND h2.stat_legal_reason_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(joinStatus.getLegalReasons(), ",")) + ") " ;
        				objects.addAll(joinStatus.getLegalReasons());
        			}
        			currentCriteria += " ) ";
    			}
    			currentCriteria += " ) ";
    			appStatusesSql.add(currentCriteria);
    		}
    		
    		criterias.add(" (" + StringUtils.join(appStatusesSql, " OR ") + ") ");
    	}*/
    	
    	if (applicantCountryIds != null && applicantCountryIds.size() > 0) {
    		criterias.add(" applicant.citizenship_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(applicantCountryIds, ",")) + ") " );
    		objects.addAll(applicantCountryIds);
    	}
    	
    	
    	//Ako ima ili universitet ili dyrjava, togava rezultata e obedinenie mejdu universitetite i dyrjavite
    	if ((universityIds != null && universityIds.size() > 0) || universityCountryIds != null && universityCountryIds.size() > 0) {
    		List<String> lst = new ArrayList<String>();
    		if (universityIds != null && universityIds.size() > 0) {
        		lst.add(" university.id in (" + SQLUtils.columnsToParameterList(StringUtils.join(universityIds, ",")) + ") ");
    			//criterias.add( );
        		objects.addAll(universityIds);
        	} else {
        		
        	}
        	
        	if (universityCountryIds != null && universityCountryIds.size() > 0) {
        		lst.add(" university.country_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(universityCountryIds, ",")) + ") " );
        		objects.addAll(universityCountryIds);
        	}
        	criterias.add("( " + StringUtils.join(lst, " OR ") + ") ");
        	
    	}
    	
    	
    	
    	
    	/*if (diplomaSpecialityIds != null && diplomaSpecialityIds.size() > 0) {
    		criterias.add(" training_course.speciality_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(diplomaSpecialityIds, ",")) + ") " );
    		objects.addAll(diplomaSpecialityIds);
    	}*/
    	
    	/** TEST */
    	if (diplomaSpecialityIds != null && diplomaSpecialityIds.size() > 0) {
            criterias.add(" training_speciality.id in (" + SQLUtils.columnsToParameterList(StringUtils.join(diplomaSpecialityIds, ",")) + ") " );
            objects.addAll(diplomaSpecialityIds);
        }
    	
    	if (diplomaQualificationIds != null && diplomaQualificationIds.size() > 0) {
    		criterias.add(" training_course.qualification_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(diplomaQualificationIds, ",")) + ") " );
    		objects.addAll(diplomaQualificationIds);
    	}
    	
    	if (diplomaEducationLevelIds != null && diplomaEducationLevelIds.size() > 0) {
    		criterias.add(" training_course.edu_level_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(diplomaEducationLevelIds, ",")) + ") " );
    		objects.addAll(diplomaEducationLevelIds);
    	}
    	
    	
    	
    	if (recognizedSpecialityIds != null && recognizedSpecialityIds.size() > 0) {
    		criterias.add(" recognized_specs.spec_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(recognizedSpecialityIds, ",")) + ") " );
    		objects.addAll(recognizedSpecialityIds);
    	}
    	
    	if (recognizedQualificationIds != null && recognizedQualificationIds.size() > 0) {
    		criterias.add(" training_course.recognized_qualification_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(recognizedQualificationIds, ",")) + ") " );
    		objects.addAll(recognizedQualificationIds);
    	}
    	
    	if (recognizedEducationLevelIds != null && recognizedEducationLevelIds.size() > 0) {
    		criterias.add(" training_course.recognized_edu_level_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(recognizedEducationLevelIds, ",")) + ") " );
    		objects.addAll(recognizedEducationLevelIds);
    	}
    	
    	if (appDateFrom != null) {
    		criterias.add(" application.app_date >= ? ");
    		objects.add(appDateFrom);
    	}
    	
    	if (appDateTo != null) {
    		criterias.add(" application.app_date <= ? ");
    		objects.add(appDateTo);
    	}
    
    	String sql = COMMISSION_INQUIRE_SQL+ " WHERE (" + StringUtils.join(criterias, ") AND\n\t ( ") + ") ";

        List<IntegerValue> res = super.selectRecords(sql, IntegerValue.class, objects.toArray());
        return integerValueToInteger(res);

    }
    private List<Integer> integerValueToInteger(List<IntegerValue> lst) {
        List<Integer> result = new ArrayList<Integer>();
        for (IntegerValue i : lst) {
            result.add(i.getValue());
        }
        return result;
    }
    
    public List<Integer> getApplicationRecordsForCommonInquire(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNums, boolean onlyCommissionApplications, Integer startSessionId, Integer endSessionId, Date commissionDateFrom, Date commissionDateTo, Date diplomaDateFrom, Date diplomaDateTo,
															   Integer jointDegree, boolean eSubmited, Boolean eSigned, List<Integer> applicantCountryIds, List<Integer> universityCountryIds,
															   List<Integer> universityIds, List<Integer> diplomaSpecialityIds, List<Integer> diplomaOriginalSpecialityIds, List<Integer> diplomaQualificationIds, List<Integer> diplomaOriginalQualificationIds,
															   List<Integer> diplomaEducationLevelIds, List<Integer> diplomaOriginalEducationLevelIds,
															   List<Integer> recognizedSpecialityIds, List<Integer> recognizedQualificationIds, List<Integer> recognizedEducationLevelIds, Date appDateFrom, Date appDateTo,
															   List<Integer> attachmentDocumentTypeIds, List<Integer> responsibleUserIds, List<Integer> userCreatedIds, List<Integer> trainingInstitutionCountryIds, List<Integer> trainingInstitutionIds,
															   List<ApplicationStatusFromCommonInquire> status, boolean onlyWithDiplomaRegistersUniversities, List<Integer> documentReceiveMethodIds, CompanyApplicantRequest companyIds) throws SQLException {
    	//String sql = APPLICATINS_REPORT_SQL;
    	List<String> criterias = new ArrayList<String>();
    	List<Object> objects = new ArrayList<Object>();
    	//Dobavq samo poslednite zapisi ot app_status_history za dadenoto application.id
    	//criterias.add(" app_status_history.id = (select max(id) from app_status_history where application_id = application.id) ");
    	criterias.add(" 1 = 1 ");



		addApplicationTypeStatement(applicationTypeEntryNums, criterias, objects);
		addEntryNumSeriesStatement(applicationTypeEntryNums, criterias, objects);

    	if (onlyCommissionApplications) {
    		criterias.add(" comission_calendar.session_status_id = ? "); 
        	objects.add(SessionStatus.SESSION_STATUS_PROVEDENO);
        	if (startSessionId != null || endSessionId != null || commissionDateFrom != null || commissionDateTo != null) {
        		String sql = " application.id in (select application_id from app_status_history where session_id in (select id from comission_calendar where 1 = 1 ";
        		if (startSessionId != null) {
        			sql += " AND session_num >= ? ";
        			objects.add(startSessionId);
        		}
        		if (endSessionId != null) {
        			sql += " AND session_num <= ? ";
        			objects.add(endSessionId);
        		}
        		
        		if (commissionDateFrom != null) {
        			sql += " AND session_time >= ? ";
        			objects.add(new Timestamp(commissionDateFrom.getTime()));
        		}
        		if (commissionDateTo != null) {
        			sql += " AND session_time <= ? ";
        			objects.add(new Timestamp(commissionDateTo.getTime()));
        		}
        		
        		sql += " ) )";
        		criterias.add(sql);
        	}
        	
    	}
    	
    	if (diplomaDateFrom != null || diplomaDateTo != null) {
    		String dateCriteria = "";
    		if (diplomaDateFrom != null) {
    			dateCriteria = " training_course.diploma_date >= ? " + (diplomaDateTo == null ? "" : " AND ");
    			objects.add(diplomaDateFrom);
    		}
    		if (diplomaDateTo != null) {
    			dateCriteria += " training_course.diploma_date <= ? ";
    			objects.add(diplomaDateTo);
    		}
    		criterias.add(dateCriteria);
    	}
    	
    	
    	addJointDegreeFilter(jointDegree, criterias, objects);
    	if (eSubmited) {
    		criterias.add(" rudi_application.application_id is not null ");
    		if (eSigned != null) {
    			criterias.add("rudi_signed_docs.id is " + ( eSigned ? "not" : "") + " null");
    			if (eSigned) {
    				
    			}
        	}
    	}
    	

    	_addInParameter(" applicant.citizenship_id", applicantCountryIds, objects, criterias);

    	//Ako ima ili universitet ili dyrjava, togava rezultata e obedinenie mejdu universitetite i dyrjavite
    	if ((universityIds != null && universityIds.size() > 0) || universityCountryIds != null && universityCountryIds.size() > 0) {
    		List<String> lst = new ArrayList<String>();
			_addInParameter("university.id", universityIds, objects, lst);
			_addInParameter("university.country_id", universityCountryIds, objects, lst);
        	criterias.add("( " + StringUtils.join(lst, " OR ") + ") ");
    	}
    	if (onlyWithDiplomaRegistersUniversities) {
    		criterias.add("university.url_diploma_register is not null and university.url_diploma_register != ''");
		}

		//Ako ima ili institution ili dyrjava, togava rezultata e obedinenie mejdu instituciite i dyrjavite
		if (!CollectionUtils.isEmpty(trainingInstitutionIds) || !CollectionUtils.isEmpty(trainingInstitutionCountryIds)) {
			List<String> lst = new ArrayList<String>();
			_addInParameter("institution.id", trainingInstitutionIds, objects, lst);
			_addInParameter("institution.country_id", trainingInstitutionCountryIds, objects, lst);
			criterias.add("( " + StringUtils.join(lst, " OR ") + ") ");
		}

		_addInParameter("training_speciality.id", diplomaSpecialityIds, objects, criterias);
		_addInParameter("specialities.original_speciality_id", diplomaOriginalSpecialityIds, objects, criterias);
		_addInParameter("training_course.qualification_id", diplomaQualificationIds, objects, criterias);
		_addInParameter("training_course.original_qualification_id", diplomaOriginalQualificationIds, objects, criterias);
    	_addInParameter("training_course.edu_level_id", diplomaEducationLevelIds, objects, criterias);
    	_addInParameter("dt.original_edu_level_id", diplomaOriginalEducationLevelIds, objects, criterias);

    	_addInParameter("recognized_specs.spec_id", recognizedSpecialityIds, objects, criterias);
    	_addInParameter("training_course.recognized_qualification_id", recognizedQualificationIds, objects, criterias);
    	_addInParameter("training_course.recognized_edu_level_id", recognizedEducationLevelIds, objects, criterias);
    	_addInParameter("application.document_receive_method_id", documentReceiveMethodIds, objects, criterias);
		generateCompanyApplicantParameter(companyIds, objects, criterias);

    	
    	if (appDateFrom != null) {
    		criterias.add(" application.app_date >= ? ");
    		objects.add(appDateFrom);
    	}
    	
    	if (appDateTo != null) {
    		criterias.add(" application.app_date <= ? ");
    		objects.add(appDateTo);
    	}

        if (attachmentDocumentTypeIds != null && attachmentDocumentTypeIds.size() > 0) {
            criterias.add(" application.id in (select parent_id from attached_docs where doc_type_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(attachmentDocumentTypeIds, ",")) + ") )");
            objects.addAll(attachmentDocumentTypeIds);
        }

        if (responsibleUserIds != null && responsibleUserIds.size() > 0) {
            List<String> str = new ArrayList<String>();
            for (Integer i:responsibleUserIds) {
                str.add(i + "");
            }
            criterias.add(" application.responsible_user in (" + StringUtils.join(str, ", ") + ") ");
        }

        if (userCreatedIds != null && userCreatedIds.size() > 0) {
            List<String> str = new ArrayList<String>();
            for (Integer i : userCreatedIds) {
                str.add(i + "");
            }
            criterias.add(" application.created_by_user_id in (" + StringUtils.join(str, ", ") + ") ");
        }
        criterias.add(addStatusCriteria(status, objects));

    	String sql = COMMON_INQUIRE_REPORT_SQL + " WHERE (" + StringUtils.join(criterias, ") AND\n\t ( ") + ") ";
        List<IntegerValue> res = super.selectRecords(sql, IntegerValue.class, objects.toArray());
        //System.out.println(sql + jointDegree);
        return integerValueToInteger(res);
    }
    private void generateCompanyApplicantParameter(CompanyApplicantRequest rq, List<Object> objects, List<String> criterias) {
		if ( rq== null) {
			return;
		}
		List<String> c = new ArrayList<>();
		if (!CollectionUtils.isEmpty(rq.getApplicantTypes())) {
			_addInParameter("application.applicant_type", rq.getApplicantTypes(), objects, c);
		}
		if (!CollectionUtils.isEmpty(rq.getCompanyIds())) {
			_addInParameter("application.applicant_company_id", rq.getCompanyIds(), objects, c);
		}
		if (c.size() > 0) {
			criterias.add("( " +  StringUtils.join(c, " AND ") + " )");
		}
	}
    private void _addInParameter(String column, List<?> values, List<Object> objects, List<String> criterias) {
		if (values != null && values.size() > 0) {
			criterias.add(column  + " in (" + SQLUtils.columnsToParameterList(StringUtils.join(values, ",")) + ") ");
			objects.addAll(values);
		}
	}

    public String addStatusCriteria(List<ApplicationStatusFromCommonInquire> status, List<Object> objects) throws SQLException {
        if (status == null || status.size() == 0) {
            return "1 = 1";
        }

        List<String> sql = new ArrayList<String>();

        for (ApplicationStatusFromCommonInquire s : status) {
            List<String> subSql = new ArrayList<String>();
            String statusTable;
            //ako e vdignat flaga onlyActualStatus, se tyrsi za current_history.status_id, inache v ash.status_id
            if (s.isOnlyActualStatus()) {
                statusTable = "current_history";
            } else {
                statusTable = "ash";
            }
            if (s.getApplicationStatus() != null) {
                subSql.add(statusTable + ".status_id = ?");
                objects.add(s.getApplicationStatus().getStatusId());
                List<Integer> lr = s.getApplicationStatus().getLegalReasons();
                if (lr != null && lr.size() > 0) {
                    subSql.add(statusTable + ".stat_legal_reason_id in (" + StringUtils.join(lr, ", ") + " )");
                }
                if (s.getApplicationStatusDateFrom() != null) {
                    subSql.add(statusTable + ".date_assigned >= ?");
                    objects.add(s.getApplicationStatusDateFrom());
                }
                if (s.getApplicationStatusDateTo() != null) {
                    subSql.add(statusTable + ".date_assigned <= ?");
                    objects.add(s.getApplicationStatusDateTo());
                }
            }

            if (s.getFinalApplicationStatus() != null) {
                subSql.add("fsh.status_id = ?");
                objects.add(s.getFinalApplicationStatus().getStatusId());
                List<Integer> lr = s.getFinalApplicationStatus().getLegalReasons();
                if (lr != null && lr.size() > 0) {
                    subSql.add("fsh.stat_legal_reason_id in (" + StringUtils.join(lr, ", ") + " )");
                }
            }

            if (s.getFinalApplicationStatusDateFrom() != null) {
                subSql.add("fsh.date_assigned >= ?");
                objects.add(s.getFinalApplicationStatusDateFrom());
            }
            if (s.getFinalApplicationStatusDateTo() != null) {
                subSql.add("fsh.date_assigned <= ?");
                objects.add(s.getFinalApplicationStatusDateTo());
            }

            if (s.getDocflowStatusId() != null) {
                subSql.add("application.docflow_status_id = ?");
                objects.add(s.getDocflowStatusId());
                if (s.getDocflowStatusDateFrom() != null) {
                    subSql.add("current_docflow_history.date_assigned >= ?");
                    objects.add(s.getDocflowStatusDateFrom());
                }
                if (s.getDocflowStatusDateTo() != null) {
                    subSql.add("current_docflow_history.date_assigned <= ?");
                    objects.add(s.getDocflowStatusDateTo());
                }
            }

            sql.add(" ( " +StringUtils.join(subSql, ") AND (") + " ) ");
        }
        return " (" + StringUtils.join(sql, ") OR (") + ")";
    }
    public List<Integer> getApplicationRecordIdsForApplicantInquire(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNums, String applicantFname, String applicantSname, String applicantLname, String applicantPersonalId, String applicantCompanyName, String applicantCompanyEik,
                                                                    String ownerFname, String ownerSname, String ownerLname, String ownerPersonalId, String diplFName, String diplSName, String diplLName, String reprFName, String reprSName, String reprLName, String reprPersonalId, Integer reprCompany, String applicationNum, Date dateFrom, Date dateTo) throws SQLException{
    	List<String> criterias = new ArrayList<String>();
    	List<Object> objects = new ArrayList<Object>();
    	criterias.add(" 1 = 1 ");

		addApplicationTypeStatement(applicationTypeEntryNums, criterias, objects);
		addEntryNumSeriesStatement(applicationTypeEntryNums, criterias, objects);

    	if (!StringUtils.isEmpty(applicantFname)) {
    		criterias.add(" applicant.fname ilike ? ");
    		objects.add(applicantFname + "%");
    	}
    	if (!StringUtils.isEmpty(applicantSname)) {
    		criterias.add(" applicant.sname ilike ? ");
    		objects.add(applicantSname + "%");
    	}
    	if (!StringUtils.isEmpty(applicantLname)) {
    		criterias.add(" applicant.lname ilike ? ");
    		objects.add(applicantLname + "%");
    	}
    	if (!StringUtils.isEmpty(applicantPersonalId)) {
    		criterias.add(" applicant.civil_id like ? ");
    		objects.add("%" + applicantPersonalId + "%");
    	}

        if (!StringUtils.isEmpty(applicantCompanyName)) {
            criterias.add(" applicant_company.name ilike ? ");
            objects.add(applicantCompanyName + "%");
        }
        if (!StringUtils.isEmpty(applicantCompanyEik)) {
            criterias.add(" applicant_company.eik like ? ");
            objects.add("%" + applicantCompanyEik + "%");
        }


        if (!StringUtils.isEmpty(ownerFname)) {
            criterias.add(" owr.fname ilike ? ");
            objects.add(ownerFname + "%");
        }
        if (!StringUtils.isEmpty(ownerSname)) {
            criterias.add(" owr.sname ilike ? ");
            objects.add(ownerSname + "%");
        }
        if (!StringUtils.isEmpty(ownerLname)) {
            criterias.add(" owr.lname ilike ? ");
            objects.add(ownerLname + "%");
        }
        if (!StringUtils.isEmpty(ownerPersonalId)) {
            criterias.add(" owr.civil_id like ? ");
            objects.add("%" + ownerPersonalId + "%");
        }



        if (!StringUtils.isEmpty(diplFName)) {
            criterias.add(" tce.fname ilike ? ");
            objects.add(diplFName + "%");
        }
        if (!StringUtils.isEmpty(diplSName)) {
            criterias.add(" tce.sname ilike ? ");
            objects.add(diplSName + "%");
        }
        if (!StringUtils.isEmpty(diplLName)) {
            criterias.add(" tce.lname ilike ? ");
            objects.add(diplLName + "%");
        }
        
        
        
        if (!StringUtils.isEmpty(reprFName)) {
            criterias.add(" repr.fname ilike ? ");
            objects.add(reprFName + "%");
        }
        if (!StringUtils.isEmpty(reprSName)) {
            criterias.add(" repr.sname ilike ? ");
            objects.add(reprSName + "%");
        }
        if (!StringUtils.isEmpty(reprLName)) {
            criterias.add(" repr.lname ilike ? ");
            objects.add(reprLName + "%");
        }
        
        if (!StringUtils.isEmpty(reprPersonalId)) {
            criterias.add(" repr.civil_id like ? ");
            objects.add("%" + reprPersonalId + "%");
        }
        if (reprCompany != null) {
            criterias.add(" application.company_id = ? ");
            objects.add(reprCompany);
        }
        
    	if (!StringUtils.isEmpty(applicationNum)) {
    		criterias.add(" app_num = ? ");
    		objects.add(applicationNum);
    	}
    	
    	if (dateFrom != null) {
    		criterias.add(" app_date >= ? ");
    		objects.add(dateFrom);
    	}
    	if (dateTo != null) {
    		criterias.add(" app_date <= ? ");
    		objects.add(dateTo);
    	}
    	String sql = APPLICANT_IQNUIRE_REPORT_SQL + " WHERE (" + StringUtils.join(criterias, ") AND\n\t ( ") + ") ";
        List<IntegerValue> res = super.selectRecords(sql, IntegerValue.class, objects.size() == 0 ? null : objects.toArray());
        //System.out.println("SQL" + sql);
        return integerValueToInteger(res);


    	
    }
    
    
    public List<Integer> getApplicationRecordIdsForInquiryInquire(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNums, Integer jointDegree, List<Integer> documentTypeIds, List<Integer> universityCountryIds,
    		List<Integer> universityIds, List<Integer> eventStatusIds) throws SQLException{
    	List<String> criterias = new ArrayList<String>();
    	List<Object> objects = new ArrayList<Object>();
    	criterias.add(" 1 = 1 ");

		addApplicationTypeStatement(applicationTypeEntryNums, criterias, objects);
		addEntryNumSeriesStatement(applicationTypeEntryNums, criterias, objects);


		addJointDegreeFilter(jointDegree, criterias, objects);
    	if (documentTypeIds != null && documentTypeIds.size() > 0 ) {
    		criterias.add(" event.doc_type_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(documentTypeIds, ",")) + ") " );
    		objects.addAll(documentTypeIds);
    	}
    	
    	
    	//Ako ima ili universitet ili dyrjava, togava rezultata e obedinenie mejdu universitetite i dyrjavite
    	if ((universityIds != null && universityIds.size() > 0) || universityCountryIds != null && universityCountryIds.size() > 0) {
    		List<String> lst = new ArrayList<String>();
    		if (universityIds != null && universityIds.size() > 0) {
        		lst.add(" university.id in (" + SQLUtils.columnsToParameterList(StringUtils.join(universityIds, ",")) + ") ");
    			//criterias.add( );
        		objects.addAll(universityIds);
        	} else {
        		
        	}
        	
        	if (universityCountryIds != null && universityCountryIds.size() > 0) {
        		lst.add(" university.country_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(universityCountryIds, ",")) + ") " );
        		objects.addAll(universityCountryIds);
        	}
        	criterias.add("( " + StringUtils.join(lst, " OR ") + ") ");
        	
    	}
    	if (eventStatusIds != null && eventStatusIds.size() > 0 ) {
    		criterias.add(" event.event_status in (" + SQLUtils.columnsToParameterList(StringUtils.join(eventStatusIds, ",")) + ") " );
    		objects.addAll(eventStatusIds);
    	}
    	
    	String sql = INQUIRE_TYPE_INQUIRY_SQL + " WHERE (" + StringUtils.join(criterias, ") AND\n\t ( ") + ") ";
    	//System.out.println(sql);
        List<IntegerValue> res = super.selectRecords(sql, IntegerValue.class, objects.size() == 0 ? null : objects.toArray());
        return integerValueToInteger(res);
    	
    }
    private void addJointDegreeFilter(Integer jointDegree, List<String> criterias, List<Object> objects) {
		if (jointDegree != null) {
			String s = " (training_course.is_joint_degree = ? ";
			if (jointDegree == 0) {
				s += " or training_course.is_joint_degree is null ";
			}
			s += " ) ";
			criterias.add(s);
			objects.add(jointDegree);
		}
	}
	public List<ExpertInquireResultRecord> getExpertInquiresResult(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNums, List<Integer> responsibleUserIds, List<Integer> userId, String applicationNumber, Date dateFrom, Date dateTo, List<ApplicationStatusFromCommonInquire> status) throws SQLException {
		List<Object> params = new ArrayList<>();
		List<String> criterias = new ArrayList<>();

		addApplicationTypeStatement(applicationTypeEntryNums, criterias, params);
		addEntryNumSeriesStatement(applicationTypeEntryNums, criterias, params);

		if (responsibleUserIds != null && responsibleUserIds.size() > 0) {
			List<String> str = new ArrayList<String>();
			for (Integer i:responsibleUserIds) {
				str.add(i + "");
			}
			criterias.add(" responsible_user in (" + StringUtils.join(str, ", ") + ") ");
		}

		if (userId != null && userId.size() > 0) {
			criterias.add(" ash.user_id in (" + StringUtils.join(userId, ", ") + ") ");
		}

		if (!StringUtils.isEmpty(applicationNumber)) {
			criterias.add( " app_num = ? ");
			params.add(applicationNumber);
		}
		if (dateFrom != null) {
			criterias.add("app_date >= ?");
			params.add(dateFrom);
		}
		if (dateTo != null) {
			criterias.add("app_date <= ?");
			params.add(dateTo);
		}
		criterias.add(addStatusCriteria(status, params));


		String sql = COMMISSION_MEMBER_REPORT_SQL + " WHERE " + (criterias.size() == 0 ? "1 = 1" : StringUtils.join(criterias, " and ")) ;
		return selectRecords(sql, ExpertInquireResultRecord.class, criterias.size() == 0 ? null : params.toArray());

	}
    
    public List<Integer> getApplicationRecordIdsForEmployeeInquire(List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNums, List<Integer> responsibleUserIds, List<Integer> userId, String applicationNumber, Date dateFrom, Date dateTo, List<ApplicationStatusFromCommonInquire> status) throws SQLException {
        List<Object> params = new ArrayList<Object>();
        List<String> criterias = new ArrayList<String>();

		addApplicationTypeStatement(applicationTypeEntryNums, criterias, params);
		addEntryNumSeriesStatement(applicationTypeEntryNums, criterias, params);

		if (responsibleUserIds != null && responsibleUserIds.size() > 0) {
            List<String> str = new ArrayList<String>();
            for (Integer i:responsibleUserIds) {
                str.add(i + "");
            }
            criterias.add(" responsible_user in (" + StringUtils.join(str, ", ") + ") ");
        }
        
        if (userId != null && userId.size() > 0) {
            criterias.add(" ash.user_id in (" + StringUtils.join(userId, ", ") + ") ");
        }
        
        if (!StringUtils.isEmpty(applicationNumber)) {
            criterias.add( " app_num = ? ");
            params.add(applicationNumber);
        }
        if (dateFrom != null) {
            criterias.add("app_date >= ?");
            params.add(dateFrom);
        }
        if (dateTo != null) {
            criterias.add("app_date <= ?");
            params.add(dateTo);
        }
        criterias.add(addStatusCriteria(status, params));
        String sql = EMPLOYEE_INQUIRE_REPORT_SQL + " WHERE " + (criterias.size() == 0 ? "1 = 1" : StringUtils.join(criterias, " and "));
        List<IntegerValue> res = selectRecords(sql, IntegerValue.class, criterias.size() == 0 ? null : params.toArray());
        return integerValueToInteger(res);


    }
//applicationType se filtrira v addEntryNumSeriesStatement
	private static void addApplicationTypeStatement(List<ApplicationTypeAndEntryNumSeries> appTypeEntryNums, List<String> criterias, List<Object> params) {
//		if (!CollectionUtils.isEmpty(appTypeEntryNums)) {
//			List<String> str = new ArrayList<String>();
//			for (ApplicationTypeAndEntryNumSeries i : appTypeEntryNums) {
//				str.add("?");
//				params.add(i.getApplicationType());
//			}
//			criterias.add(" application.application_type in (" + StringUtils.join(str, ", ") + ") ");
//		}
	}
	private static void addEntryNumSeriesStatement(List<ApplicationTypeAndEntryNumSeries> appTypeEntryNums, List<String> criterias, List<Object> params) {
		if (!CollectionUtils.isEmpty(appTypeEntryNums)) {
			List<String> c = new ArrayList<>();
			for (ApplicationTypeAndEntryNumSeries e : appTypeEntryNums) {
				List<Integer> kinds = e.getEntryNumSeries() == null ? NumgeneratorDataProvider.APPLICATION_TYPE_TO_ENTRYNUM_SERIES.get(e.getApplicationType()) : e.getEntryNumSeries();
				String operation;
				switch (e.getJoinType()) {
					case JOIN_TYPE_AND:
						operation = " @> ";
						break;
					case JOIN_TYPE_OR:
						operation = " && ";
						break;
					case  JOIN_TYPE_EQUALS:
						operation = " = ";
						break;
					default:
						throw new RuntimeException("Unknown joinType: " + e.getJoinType());
				}
				c.add("application.application_type = ? AND (select array (select entry_num_series_id from application_kind  where application_id = application.id)) " + operation + " array[" + StringUtils.join(kinds, ", ") + "]");
				params.add(e.getApplicationType());
			}
			criterias.add("(" + StringUtils.join(c, " OR ") + ")");
		}
	}
    public List<ApplicationForInquireRecord> getApplicationsForInquireByIds(List<Integer> appIds) throws SQLException {
        if (appIds == null || appIds.size() == 0) {
            return new ArrayList<ApplicationForInquireRecord>();
        }
        return selectRecords(ApplicationForInquireRecord.class, "id in (" + StringUtils.join(appIds, ", ") + ")");
    }

	public List<BGAcademicRecognitionForReportRecord> getBgAcademicRecognitionForReportRecords(List<String> ownerNames, List<String> citizenship, List<String> university,
																							   List<String> universityCountry, List<String> diplomaSpeciality, List<String> educationLevel, String protocolNumber,
																							   String denialProtocolNumber, List<String> recognizedSpeciliaty,
																							   List<Integer> recognizedUniversityIds,String outputNumber, String inputNumber, List<Integer> recognitionStatusIds) throws SQLException {
		List<Object> objects = new ArrayList<>();
		List<String> criterias = new ArrayList<>();
		addStringCriterias(ownerNames, "", "", "applicant ilike ?", " OR ", criterias, objects);
		addStringCriterias(citizenship, "", "", "citizenship = ?", "OR", criterias, objects);
		addStringCriterias(university, "", "", "university ilike ?", "OR", criterias, objects);
		addStringCriterias(universityCountry, "", "", "university_country ilike ?", "OR", criterias, objects);
		addStringCriterias(educationLevel, "", "", "education_level ilike ?", "OR", criterias, objects);
		addStringCriterias(StringUtils.isEmpty(protocolNumber) ? null : Arrays.asList(protocolNumber), "%", "%", "protocol_number ilike ?", "OR", criterias, objects);
		addStringCriterias(StringUtils.isEmpty(denialProtocolNumber) ? null : Arrays.asList(denialProtocolNumber), "%", "%", "denial_protocol_number ilike ?", "OR", criterias, objects);
		addStringCriterias(diplomaSpeciality, "", "", "diploma_speciality ilike ?", "OR", criterias, objects);
		addStringCriterias(recognizedSpeciliaty, "", "", "recognized_speciality ilike ?", "OR", criterias, objects);
		addStringCriterias(recognizedUniversityIds, null, null, "recognized_university_id = ?", " OR ", criterias, objects);
		addStringCriterias(StringUtils.isEmpty(outputNumber) ? null : Arrays.asList(outputNumber), "%", "%", "output_number ilike ?", "OR", criterias, objects);
		addStringCriterias(StringUtils.isEmpty(inputNumber) ? null : Arrays.asList(inputNumber), "%", "%", "input_number ilike ?", "OR", criterias, objects);
		addStringCriterias(recognitionStatusIds, null, null, "recognition_status_id = ?", " OR ", criterias, objects);


		String sql = BG_ACADEMIC_RECOGNITIONS_INQUIRE_SQL + " WHERE " + (criterias.size() == 0 ? "1 = 1" : StringUtils.join(criterias, " and "));
		logger.debug(sql);
		return selectRecords(sql, BGAcademicRecognitionForReportRecord.class, objects.size() == 0 ? null : objects.toArray());
	}

	private static void addStringCriterias(List<?> parameters, String parameterPrefix, String parameterSuffix, String clause, String joinOperator, List<String> criterias, List<Object> objects) {
		if (!Utils.isEmpty(parameters)) {
			List<String> partialCriteria = new ArrayList<>();
			for (Object parameter : parameters) {
				partialCriteria.add(clause);
				if (parameter instanceof String) {
					objects.add((parameterPrefix == null ? "" : parameterPrefix) + parameter + (parameterSuffix == null ? "" : parameterSuffix));
				} else {
					objects.add(parameter);
				}

			}
			criterias.add(partialCriteria.stream().collect(Collectors.joining(" " + joinOperator + " ", " (", ") ")));
		}

	}

    public static void main(String[] args) throws SQLException{
        InquiresDB db = new InquiresDB(new StandAloneDataSource());
		System.out.println(db.getExpertInquiresResult(Arrays.asList(new ApplicationTypeAndEntryNumSeries(RUDI_APPLICATION_TYPE, null, 1)),null, null, null, null, null, null));
//		System.out.println(db.getBgAcademicRecognitionForReportRecords(Arrays.asList("Иванов"), Arrays.asList("българия"), Arrays.asList("университет"), Arrays.asList("Франция"), Arrays.asList("право"), Arrays.asList("бакалавър"), "Протокол", "отказ",  null, Arrays.asList(2349), "92.00-87/25.06.2012", "21-00-639", Arrays.asList(4)));
//        Map<Integer, List<Integer>> professionalInstitutions = new HashMap<Integer, List<Integer>>();
        //professionalInstitutions.put(1, Arrays.asList(1,2,3));
//        professionalInstitutions.put(null, Arrays.asList(4,5,6));
        //professionalInstitutions.put(10, null);
        /*List<ApplicationStatusFromCommissionInquire> applicationStatuses = new ArrayList<ApplicationStatusFromCommissionInquire>();
        ApplicationStatusAndLegalReasons s1 = new ApplicationStatusAndLegalReasons(10,null);
        ApplicationStatusAndLegalReasons s2 = null;//new ApplicationStatusAndLegalReasons(11,null);
        Integer joinType = null;//ApplicationStatusFromCommissionInquire.JOIN_TYPE_NOT
        ApplicationStatusFromCommissionInquire stat = new ApplicationStatusFromCommissionInquire(s1, joinType, s2);
        applicationStatuses.add(stat);*/
        /*Calendar cal = new GregorianCalendar(2011, 0, 1);
        java.sql.Date date = new Date(cal.getTimeInMillis());
        List<ApplicationRecord> records = db.getApplicationRecordsForEmployeeInquire(Arrays.asList(-1), null, null, null, null);
        //List<ApplicationRecord> records = db.getApplicationRecordIdsForCommissionInquire(null, 84, 87, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        if (records != null) {
        	for (ApplicationRecord r:records) {
        		System.out.println(r.getId());
        	}
        }*/
        //db.getRegprofApplicationsForCommonInqure(new java.sql.Date(new java.util.Date().getTime()), new java.sql.Date(new java.util.Date().getTime()), Arrays.asList(6));
        //db.getApplicationRecordIdsForCommissionInquire(null, null, null, applicationStatuses, null, null, null, null, null, null, null, null, null);
        //db.getApplicationRecordsForCommonInquire(false, null, null, null, null, null, null, null, null, null, null);
    }
}
