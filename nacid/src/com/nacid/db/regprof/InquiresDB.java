package com.nacid.db.regprof;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegProfApplicationForInquireImpl;
import com.nacid.bl.regprof.RegprofApplicationStatusFromCommonInquire;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InquiresDB extends DatabaseService {

    public InquiresDB(DataSource ds) {
        super(ds);
    }
  //TODO:Tezi neshta svyrzani s regprof's inquires trqbva da otidat v sobstven DB!
    private static final String COMMON_INQUIRE_SQL = "select distinct apn.id, apn.app_num as app_number, apn.app_date,  apn.app_num||'/'||to_char(apn.app_date, 'dd.MM.yyyy') docflow_number, \n" +
                                                  "CASE WHEN apn.names_dont_match = 1 THEN tce.document_fname||coalesce(' '||tce.document_sname,'')||coalesce(' '||document_lname,'') ELSE apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') END as applicant_diploma_names,\n" +
                                                  "apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') as applicant_names, \n" +
                                                  "apt.civil_id as civil_id, cip.name as citizenship_name,\n" +
                                                  "(case when ext_apn.id is not null then 1 else 0 end) as electronically_applied,\n" +
                                                  "(case when sdc.id is not null then 1 else 0 end) as esigned,\n" +
                                                  "ste.name as service_type, acy.name as application_country,\n" +
                                                  "ras.name as status, dss.name as docflow_status, cpq.name as certificate_qualification,\n" +
                                                  "pin.bg_name as professional_institution_name, pinn.former_name as professional_institution_former_name,\n" +
                                                  "sdkpin.bg_name as sdk_professional_institution_name, sdkpinn.former_name as sdk_professional_institution_former_name,\n" +
                                                  "tce.document_date, tce.sdk_document_date, ete.id as education_type_id, ete.name as education_type,\n" +
                                                  "tce.has_education, tce.has_experience,\n" +
                                                  "hpq.name as high_professional_qualification, sdkpq.name as sdk_professional_qualification, secpq.name as secondary_professional_qualification,\n" +
                                                  "(select array(select s.name from regprof.training_course_specialities tcs join nomenclatures.higher_speciality s on s.id = tcs.higher_speciality_id where tcs.training_course_id = tce.id and tcs.higher_speciality_id is not null order by tcs.id)) higher_specialities,\n" +
                                                  "(select array(select s.name from regprof.training_course_specialities tcs join nomenclatures.higher_speciality s on s.id = tcs.sdk_speciality_id where tcs.training_course_id = tce.id and tcs.sdk_speciality_id is not null order by tcs.id)) sdk_specialities,\n" +
                                                  "(select array(select s.name from regprof.training_course_specialities tcs join nomenclatures.secondary_speciality s on s.id = tcs.secondary_speciality_id where tcs.training_course_id = tce.id and tcs.secondary_speciality_id is not null order by tcs.id)) secondary_specialities,\n" +
                                                  "pe.name profession_experience_name, pee.years as profession_experience_years, pee.months as profession_experience_months, pee.days as profession_experience_days,\n" +
                                                  "rqd.name as recognized_qualification_degree, rel.name as recognized_education_level, rpn.name as recognized_profession,\n" +
                                                  "rai.name as recognized_article_item, rad.name as recognized_article_directive, apn.imi_correspondence,\n" +
                                                  "(select array(select 'Номер: '||document_number||', Издател:'||document_issuer||', Тип:'||pedt.name||', Дата: '||document_date \n" +
                                                  "from regprof.profession_experience_documents  ped2\n" +
                                                  "join nomenclatures.profession_experience_document_type pedt on pedt.id = ped2.prof_experience_doc_type_id\n" +
                                                  "where prof_experience_id = pee.id\n" +
                                                  "order by ped2.id)) as profession_experience_documents\n" +
                                                  "from regprof.regprof_application  apn  \n" +
                                                  "left join backoffice.company coy on apn.rep_from_company = coy.id \n" +
                                                  "left join eservices.regprof_application ext_apn on ext_apn.regprof_application_id = apn.id \n" +
                                                  "left join eservices.regprof_signed_docs sdc on sdc.ext_app_id = ext_apn.id \n" +
                                                  "join person apt on apt.id = apn.applicant_id \n" +
                                                  "left join person as repr on (repr.id = apn.representative_id)\n" +
                                                  "left join nomenclatures.country cip on apt.citizenship_id = cip.id\n" +
                                                  "join regprof.training_course tce on tce.id = apn.training_course_id \n" +
                                                  "left join regprof.profession_experience pee on pee.training_course_id = tce.id  \n" +
                                                  "left join nomenclatures.profession_experience pe on pee.n_profession_experience_id = pe.id\n" +
                                                  "left join regprof.profession_experience_documents ped on ped.prof_experience_id = pee.id\n" +
                                                  "left join regprof.prof_qualification_examination pqe on pqe.training_course_id = tce.id and pqe.id = (select max(id) from regprof.prof_qualification_examination pqe2 where pqe2.training_course_id = tce.id) \n" + //TODO:Tuk ima nqkakyv problem, zashtoto nqkoi training_courses imat poveche ot edin prof_qualification_examination
                                                  "left join regprof.profession_experience_examination peen on peen.regprof_profession_experience_id = pee.id and peen.id = (select max(id) from regprof.profession_experience_examination peen2 where peen2.regprof_profession_experience_id = pee.id) \n" +//TODO:Tuk ima nqkakyv problem, zashtoto nqkoi profession_experiences imat poveche ot edin prof_experience_examination
                                                  "left join nomenclatures.secondary_qualification_degree rqd on rqd.id = pqe.recognized_qualification_degree_id\n" +
                                                  "left join nomenclatures.profession rpn on rpn.id = pqe.recognized_profession_id\n" +
                                                  "left join nomenclatures.edu_level rel on pqe.recognized_qualification_level_id = rel.id\n" +
                                                  "left join nomenclatures.article_item rai on pqe.article_item_id = rai.id\n" +
                                                  "left join nomenclatures.article_directive rad on rai.article_directive_id = rad.id\n" +
                                                  "left join nomenclatures.certificate_prof_qualification cpq on tce.certificate_prof_qualification_id = cpq.id \n" +
                                                  "join nomenclatures.app_status ras on apn.status = ras.id \n" +
                                                  "join nomenclatures.app_status_docflow dss on apn.docflow_status_id = dss.id \n" +
                                                  "left join nomenclatures.service_type ste on ste.id = apn.service_type_id\n" +
                                                  "left join nomenclatures.country acy on acy.id = apn.application_country_id\n" +
                                                  "left join regprof.professional_institution pin on pin.id = tce.prof_institution_id\n" +
                                                  "left join regprof.professional_institution_names pinn on pinn.id = tce.prof_institution_org_name_id\n" +
                                                  "left join nomenclatures.education_type ete on ete.id = tce.education_type_id\n" +
                                                  "left join regprof.professional_institution sdkpin on sdkpin.id = tce.sdk_prof_institution_id\n" +
                                                  "left join regprof.professional_institution_names sdkpinn on sdkpinn.id = tce.sdk_prof_institution_org_name_id\n" +
                                                  "left join nomenclatures.higher_prof_qualification hpq on hpq.id = tce.high_prof_qualification_id\n" +
                                                  "left join nomenclatures.higher_prof_qualification sdkpq on sdkpq.id = tce.sdk_prof_qualification_id\n" +
                                                  "left join nomenclatures.secondary_prof_qualification secpq on secpq.id = tce.sec_prof_qualification_id\n" +
                                                  "left join regprof.app_status_history fss on fss.id = apn.final_status_history_id\n" +
                                                    "JOIN regprof.app_status_history shy on shy.application_id = apn.id\n" +
//                                                    "JOIN regprof.app_status_docflow_history dshy on dshy.application_id = apn.id\n" +
                                                    "join regprof.app_status_history current_history on current_history.id = (select id from regprof.app_status_history h1 where h1.application_id = apn.id order by date_assigned desc, id desc limit 1)\n" +
                                                    "join regprof.app_status_docflow_history current_docflow_history on current_docflow_history.id = (select id from regprof.app_status_docflow_history h1 where h1.application_id = apn.id order by date_assigned desc, id limit 1)\n";
    /*private static String APPLICANT_IQNUIRE_REPORT_SQL = " SELECT distinct apn.id, apn.app_num as app_number, apn.app_date,  " +
            "CASE WHEN apn.names_dont_match = 1 THEN tce.document_fname||coalesce(' '||tce.document_sname,'')||coalesce(' '||document_lname,'') ELSE apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') END as applicant_diploma_names,\n" +
            "apt.fname||coalesce(' '||apt.sname,'')||coalesce(' '||apt.lname,'') as applicant_names, \n" +
            " apt.civil_id as civil_id, (select name from nomenclatures.regprof_app_status where id = apn.status) as status, (select name from nomenclatures.certificate_prof_qualification where id = tce.certificate_prof_qualification_id) as certificate_qualification \n" +
                                                " from regprof.regprof_application apn \n" + 
                                                " join regprof.training_course as tce on (tce.id = apn.training_course_id) \n" +
                                                " left join person as repr on (repr.id = apn.representative_id) \n" +
                                                " left join person as apt on (apt.id = apn.applicant_id) \n";*/
    //professionalInstitutions - key - profInsitutionId (moje i da e null), value - list ot formerInstitutionNames- sy6to moje da e null!
    public List<RegProfApplicationForInquireImpl> getRegprofApplicationsForCommonInqure(boolean eSubmitted, Boolean eSigned, java.sql.Date applicationDateFrom, java.sql.Date applicationDateTo, List<Integer> representativeCompanyIds,
            Map<Integer, List<Integer>> professionalInstitutions, List<Integer> candidateCountryIds, Date diplomaDateFrom, Date diplomaDateTo, Integer educationTypeId, List<Integer> secProfQualificationIds, List<Integer> highProfQualificationIds, 
            List<Integer> secSpecialityIds, List<Integer> higherSpecialityIds,List<Integer> sdkSpecialityIds, List<Integer> recognizedHigherEduLevelIds, List<Integer> recognizedSecondaryQualificationDegrees,
            List<Integer> recognizedProfessions, List<Integer> experienceProfessionIds, Map<Integer, List<Integer>> directiveArticleIds, List<RegprofApplicationStatusFromCommonInquire> actualStatuses,
            Integer serviceType, Date serviceTypeDateTo, List<String> imiCorrespondences, List<Integer> attachmentDocumentTypeIds, Boolean apostilleApplication,
            List<Integer> educationDocumentTypes, List<Integer> experienceDocumentTypes) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        List<String> criterias = new ArrayList<String>();
        criterias.add(" 1 = 1 ");
        if (applicationDateFrom != null) {
            criterias.add("apn.app_date >= ?");
            objects.add(applicationDateFrom);
        }
        if (applicationDateTo != null) {
            criterias.add("apn.app_date <= ?");
            objects.add(applicationDateTo);
        }
        if (representativeCompanyIds != null && representativeCompanyIds.size() > 0) {
            criterias.add(" coy.id in (" + SQLUtils.columnsToParameterList(StringUtils.join(representativeCompanyIds, ",")) + ") ");
            objects.addAll(representativeCompanyIds);
        }
        if (candidateCountryIds != null && candidateCountryIds.size() > 0) {
            criterias.add(" apn.application_country_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(candidateCountryIds, ",")) + ") ");
            objects.addAll(candidateCountryIds);
        }
        
        if (professionalInstitutions != null) {
            List<String> institutionsSubelementsSql = new ArrayList<String>();
            for (Integer insitutionId:professionalInstitutions.keySet()) {
                List<String> subSql = new ArrayList<String>();
                if (insitutionId != null) {
                    subSql.add(" (tce.sdk_prof_institution_id = ? or (tce.sdk_prof_institution_id is null and tce.prof_institution_id = ?) ) "); //ako ima SDK, se proverqva instituciqta na sdk-to. Ako nqma SDK, se proverqva drugata instituciq
                    objects.add(insitutionId);
                    objects.add(insitutionId);
                }
                List<Integer> formerNameIds = professionalInstitutions.get(insitutionId);
                
                if (formerNameIds != null && formerNameIds.size() > 0) {
                    subSql.add(" coalesce(tce.sdk_prof_institution_org_name_id, tce.prof_institution_org_name_id) in (" + SQLUtils.columnsToParameterList(StringUtils.join(formerNameIds, ",")) + ") ");
                    objects.addAll(formerNameIds);
                }
                if (subSql.size() > 0) {
                    institutionsSubelementsSql.add("(" + StringUtils.join(subSql, " AND ") + ") ");
                }
            }
            
            if (institutionsSubelementsSql.size() > 0) {
                criterias.add("( " + StringUtils.join(institutionsSubelementsSql, " OR ") + " ) ");
            }
        }
        
        
        
        if (diplomaDateFrom != null) {
            criterias.add("tce.document_date >= ?");
            objects.add(diplomaDateFrom);
        }
        if (diplomaDateTo != null) {
            criterias.add("tce.document_date <= ?");
            objects.add(diplomaDateTo);
        }
        
        if (eSubmitted) {
            criterias.add(" ext_apn.id is not null ");
            if (eSigned != null) {
                criterias.add(" sdc.id is " + ( eSigned ? "not" : "") + " null ");
            }
        }
        
        if (educationTypeId != null) {
            criterias.add("tce.education_type_id = ?");
            objects.add(educationTypeId); 
        }
        if (secProfQualificationIds != null) {
            criterias.add(" tce.sec_prof_qualification_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(secProfQualificationIds, ",")) + ") ");
            objects.addAll(secProfQualificationIds);
        }
        if (highProfQualificationIds != null) {
            criterias.add(" tce.high_prof_qualification_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(highProfQualificationIds, ",")) + ") ");
            objects.addAll(highProfQualificationIds);
        }
        if (secSpecialityIds != null) {
            criterias.add(" tce.id in ( select training_course_id from regprof.training_course_specialities WHERE secondary_speciality_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(secSpecialityIds, ",")) + ")) ");
            objects.addAll(secSpecialityIds);
        }
        if (higherSpecialityIds != null) {
            criterias.add(" tce.id in ( select training_course_id from regprof.training_course_specialities WHERE higher_speciality_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(higherSpecialityIds, ",")) + ")) ");
            objects.addAll(higherSpecialityIds);
        }
        if (sdkSpecialityIds != null) {
            criterias.add(" tce.id in ( select training_course_id from regprof.training_course_specialities WHERE sdk_speciality_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(sdkSpecialityIds, ",")) + ")) ");
            objects.addAll(sdkSpecialityIds);
        }
        if (recognizedHigherEduLevelIds != null) {
            criterias.add(" pqe.recognized_qualification_level_id in (" + StringUtils.join(recognizedHigherEduLevelIds, ", ") + ") ");
        }
        if (recognizedSecondaryQualificationDegrees != null) {
            criterias.add(" pqe.recognized_qualification_degree_id in (" + StringUtils.join(recognizedSecondaryQualificationDegrees, ", ") + ") ");
        }
        if (recognizedProfessions != null) {
            criterias.add(" pqe.recognized_profession_id in (" + StringUtils.join(recognizedProfessions, ", ") + ") ");
        }
        if (experienceProfessionIds != null) {
            criterias.add(" pee.n_profession_experience_id in (" + StringUtils.join(experienceProfessionIds, ", ") + ") ");
        }


        if (directiveArticleIds != null) {
            List<String> substr = new ArrayList<String>();
            for (Integer articleId:directiveArticleIds.keySet()) {
                List<Integer> itemIds = directiveArticleIds.get(articleId);
                if (itemIds != null) {
                    substr.add("( pqe.article_item_id in (" + StringUtils.join(itemIds, ", ") + ") )");        
                    substr.add("( peen.article_item_id in (" + StringUtils.join(itemIds, ", ") + ") )");
                } else {
                    substr.add(" (pqe.article_item_id in (select id from nomenclatures.article_item where article_directive_id = ?)) ");
                    substr.add(" (peen.article_item_id in (select id from nomenclatures.article_item where article_directive_id = ?)) ");
                    objects.add(articleId);
                    objects.add(articleId);
                }
            }
            criterias.add("(" + StringUtils.join(substr, " OR ") + ") ");
            
        }
        if (actualStatuses != null && actualStatuses.size() > 0) {
            List<String> statutesSql = new ArrayList<String>();

            for (RegprofApplicationStatusFromCommonInquire status:actualStatuses) {
                List<String> statusesPart = new ArrayList<String>();
                if (status.getApplicationStatusId() != null) {
                    String statusTable;
                    if (status.isOnlyActualStatus()) {
                        statusTable = "current_history";
                    } else {
                        statusTable = "shy";
                    }
                    statusesPart.add(statusTable + ".status_id = ?");
                    objects.add(status.getApplicationStatusId());
                    if (status.getApplicationStatusDateFrom() != null) {
                        statusesPart.add(statusTable + ".date_assigned >= ?");
                        objects.add(status.getApplicationStatusDateFrom());
                    }
                    if (status.getApplicationStatusDateTo() != null) {
                        statusesPart.add(statusTable + ".date_assigned <= ?");
                        objects.add(status.getApplicationStatusDateTo());
                    }

                }
                if (status.getFinalApplicationStatusId() != null) {
                    statusesPart.add("fss.status_id = ?");
                    objects.add(status.getFinalApplicationStatusId());
                    if (status.getFinalApplicationStatusDateFrom() != null) {
                        statusesPart.add("fss.date_assigned >= ?");
                        objects.add(status.getFinalApplicationStatusDateFrom());
                    }
                    if (status.getFinalApplicationStatusDateTo() != null) {
                        statusesPart.add("fss.date_assigned <= ?");
                        objects.add(status.getFinalApplicationStatusDateTo());
                    }
                }
                if (status.getDocflowStatusId() != null) {
                    statusesPart.add("current_docflow_history.app_status_docflow_id = ?");
                    objects.add(status.getDocflowStatusId());
                    if (status.getDocflowStatusDateFrom() != null) {
                        statusesPart.add("current_docflow_history.date_assigned >= ?");
                        objects.add(status.getDocflowStatusDateFrom());
                    }
                    if (status.getDocflowStatusDateTo() != null) {
                        statusesPart.add("current_docflow_history.date_assigned <= ?");
                        objects.add(status.getDocflowStatusDateTo());
                    }
                }
                if (statusesPart.size() > 0) {
                    statutesSql.add(StringUtils.join(statusesPart, " AND "));
                }
            }

            criterias.add("( (" + StringUtils.join(statutesSql, ") OR (") + ") )");
        }
        if (serviceType != null) {
            objects.add(serviceType);
            criterias.add("apn.service_type_id = ?");
        }
        if (serviceTypeDateTo != null) {
            objects.add(serviceTypeDateTo);
            criterias.add("apn.end_date <= ?");
        }
        if (!Utils.isEmpty(imiCorrespondences)) {
            List<String> lst = new ArrayList<String>();
            for (String s:imiCorrespondences) {
                lst.add(" (apn.imi_correspondence ilike ? ) ");
                
                objects.add((s.startsWith("*") ? "%" + s.substring(1) : s) + "%");//ako sluchaino string-a zapochva sys * 
            }
            criterias.add(" ( " + StringUtils.join(lst, " OR ") + " ) ");
            
        }

        if (apostilleApplication != null && apostilleApplication) {
            criterias.add(" apn.apostille_application = ?");
            objects.add(1);
        }

        if (attachmentDocumentTypeIds != null && attachmentDocumentTypeIds.size() > 0) {
            criterias.add(" apn.id in (select parent_id from regprof.regprof_attached_docs where doc_type_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(attachmentDocumentTypeIds, ",")) + ") )");
            objects.addAll(attachmentDocumentTypeIds);
        }

        if (educationDocumentTypes != null) {
            String paramsLst = SQLUtils.parametersCountToParameterList(educationDocumentTypes.size());
            criterias.add(" tce.document_type in (" + paramsLst + ") OR tce.sdk_document_type IN (" + paramsLst + ")");
            objects.addAll(educationDocumentTypes);
            objects.addAll(educationDocumentTypes);
        }

        if (experienceDocumentTypes != null) {
            criterias.add(" ped.prof_experience_doc_type_id IN (" + SQLUtils.parametersCountToParameterList(experienceDocumentTypes.size()) + ") ");
            objects.addAll(experienceDocumentTypes);
        }

        return selectRecords(COMMON_INQUIRE_SQL + " WHERE (" + StringUtils.join(criterias, ") \nAND (") + ") ", RegProfApplicationForInquireImpl.class, objects.toArray());
    }



    
    
    
    public List<RegProfApplicationForInquireImpl> getRegprofApplicationRecordsForApplicantInquire(String fname, String sname, String lname, String personalId, String diplFName, String diplSName, String diplLName, String reprFName, String reprSName, String reprLName, String reprPersonalId, Integer reprCompany, String applicationNum, Date dateFrom, Date dateTo) throws SQLException{
        List<String> criterias = new ArrayList<String>();
        List<Object> objects = new ArrayList<Object>();
        criterias.add(" 1 = 1 ");
        
        if (!StringUtils.isEmpty(fname)) {
            criterias.add(" apt.fname ilike ? ");
            objects.add(fname + "%");
        }
        if (!StringUtils.isEmpty(sname)) {
            criterias.add(" apt.sname ilike ? ");
            objects.add(sname + "%");
        }
        if (!StringUtils.isEmpty(lname)) {
            criterias.add(" apt.lname ilike ? ");
            objects.add(lname + "%");
        }
        if (!StringUtils.isEmpty(personalId)) {
            criterias.add(" apt.civil_id like ? ");
            objects.add("%" + personalId + "%");
        }
        
        
        if (!StringUtils.isEmpty(diplFName)) {
            criterias.add(" tce.document_fname ilike ? ");
            objects.add(diplFName + "%");
        }
        if (!StringUtils.isEmpty(diplSName)) {
            criterias.add(" tce.document_sname ilike ? ");
            objects.add(diplSName + "%");
        }
        if (!StringUtils.isEmpty(diplLName)) {
            criterias.add(" tce.document_lname ilike ? ");
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
            criterias.add(" apn.rep_from_company = ? ");
            objects.add(reprCompany);
        }
        
        if (!StringUtils.isEmpty(applicationNum)) {
            criterias.add(" apn.app_num = ? ");
            objects.add(applicationNum);
        }
        
        if (dateFrom != null) {
            criterias.add(" apn.app_date >= ? ");
            objects.add(dateFrom);
        }
        if (dateTo != null) {
            criterias.add(" apn.app_date <= ? ");
            objects.add(dateTo);
        }
        //String sql = APPLICANT_IQNUIRE_REPORT_SQL + " WHERE (" + StringUtils.join(criterias, ") AND\n\t ( ") + ") ";
        String sql = COMMON_INQUIRE_SQL + " WHERE (" + StringUtils.join(criterias, ") AND\n\t ( ") + ") ";
        //System.out.println("SQL" + sql);
        return super.selectRecords(sql, RegProfApplicationForInquireImpl.class, objects.size() == 0 ? null : objects.toArray());
        
    }
}
