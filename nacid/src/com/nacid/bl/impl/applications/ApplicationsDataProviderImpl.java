package com.nacid.bl.impl.applications;

import com.nacid.bl.ApplicationDetailsForExpertPosition;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.docflow.DocFlowException;
import com.nacid.bl.docflow.DocFlowNumber;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
import com.nacid.bl.impl.AppStatusHistoryImpl;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.comission.CommissionCalendarDataProviderImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.*;
import com.nacid.data.external.applications.ExtApplicationRecord;
import com.nacid.data.users.ApplicationChangesHistoryRecord;
import com.nacid.data.users.UserRecord;
import com.nacid.db.applications.ApplicationsDB;
import com.nacid.db.applications.AttachmentDB;
import com.nacid.db.comission.CommissionCalendarDB;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.report.TemplateGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nacid.bl.nomenclatures.DocumentType.*;

public class ApplicationsDataProviderImpl implements ApplicationsDataProvider {

    private NacidDataProviderImpl nacidDataProvider;
    private ApplicationsDB db;

    
    public ApplicationsDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new ApplicationsDB(nacidDataProvider.getDataSource(), nacidDataProvider.getNomenclaturesDataProvider().getNomenclaturesDB());
        this.nacidDataProvider = nacidDataProvider;

    }

    public int getApplicationType(int applicationId) {
        try {
            return db.getApplicationType(applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public Application getApplication(int applicationId) {
        try {
            ApplicationRecord record = new ApplicationRecord();
            record = db.selectRecord(record, applicationId);
            if (record == null) {
                return null;
            }
            return new ApplicationImpl(nacidDataProvider, record);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    public ApplicationDetailsForReport getApplicationDetailsForReport(int applicationId) {
    	try {
            ApplicationRecord record = new ApplicationRecord();
            record = db.selectRecord(record, applicationId);
            if (record == null) {
                return null;
            }
            return new ApplicationDetailsForReportImpl(nacidDataProvider, new ApplicationImpl(nacidDataProvider, record));
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }


    public ApplicationDetailsForExpertPosition getApplicationDetailsForExpertPosition(int applicationId, int expertId) {
        Application application = getApplication(applicationId);
        ApplicationExpert applicationExpert = getApplicationExpert(applicationId, expertId);
        return new ApplicationDetailsForExpertPositionImpl(nacidDataProvider, application, applicationExpert);
    }

    public List<Application> getAssignedApplications() {
        try {
            List<ApplicationExpertRecord> applicationExpertRecords = db.getApplicationExperts(0);
            if (applicationExpertRecords.size() == 0) {
            	return null;
            }

            List<Integer> appIds =
                    applicationExpertRecords.stream().
                            map(ApplicationExpertRecord::getApplicationId).
                            distinct().
                            collect(Collectors.toList());


            /*Set<Integer> appIds = new HashSet<Integer>();
            for (ApplicationExpertRecord r:applicationExpertRecords) {
            	appIds.add(r.getApplicationId());
            }*/
            return getApplications(appIds, null);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    public List<Application> getApplications(int applicationType, boolean onlyInProcedure) {
        try {
        	return generateApplications(db.getApplicationRecords(applicationType, onlyInProcedure));
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }

    public List<ApplicationForList> getApplicationsForList(int applicationType, Integer applicationStatus, boolean onlyInProcedure) {
        try {
            List<ApplicationRecordForList> result = db.getApplicationRecordsForList(applicationType, applicationStatus, null, onlyInProcedure);
            return result.size() == 0 ? null : result.stream().map(Function.identity()).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<ApplicationForList> getApplicationsByCommission(int calendarId) {
        try {
            List<ApplicationRecordForList> result = db.getApplicationRecordsForList(ApplicationType.RUDI_APPLICATION_TYPE, null, calendarId, false);
            return result.size() == 0 ? null : result.stream().map(Function.identity()).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    /*public List<? extends ApplicationForList> getApplicationsForList(List<Integer> statuses, boolean onlySelected) {
            try {
                return db.getApplicationRecordsForList(statuses, onlySelected);
            } catch (SQLException e) {
                throw Utils.logException(e);
            }
        }*/
    @Override
    public List<? extends ApplicationForPublicRegister> getApplicationsByFinalStatus(int finalStatus) {
        try {
            return db.getApplicationRecordForPublicRegisterByFinalStatus(finalStatus);
            /*List<Application> result;
            if (finalStatus == ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE) {
            	result =  generateApplications(db.getApplicationRecordsFromAppStatusHistoryContainingStatus(finalStatus));
            } else {
            	result = generateApplications(db.getApplicationsByFinalStatus(finalStatus));	
            }
            if (result != null) {
                
                //filling application's certificate numbers....
                List<Integer> appIds = new ArrayList<Integer>();
                for (Application a:result) {
                    appIds.add(a.getId());
                }
                List<CertificateNumberToAttachedDocRecord> certificateNumbers = db.getCertificateNumberToAttachedDocRecords(appIds);
                Map<Integer, List<CertificateNumberToAttachedDocRecord>> map = new HashMap<Integer, List<CertificateNumberToAttachedDocRecord>>();
                for (CertificateNumberToAttachedDocRecord cn:certificateNumbers) {
                    List<CertificateNumberToAttachedDocRecord> cnByApp = map.get(cn.getApplicationId());
                    if (cnByApp == null) {
                        cnByApp = new ArrayList<CertificateNumberToAttachedDocRecord>();
                        map.put(cn.getApplicationId(), cnByApp);
                    }
                    cnByApp.add(cn);
                }
                
                //end of filling application's certificate numbers...
                //filling recognized speclialities ....
                List<RecognizedSpecsRecord> recognizedSpecs = db.getRecognizedSpecialities(appIds);
                Map<Integer, List<Integer>> specsMapByTrainingCourse = new HashMap<Integer, List<Integer>>();
                for (RecognizedSpecsRecord r:recognizedSpecs) {
                    List<Integer> lst = specsMapByTrainingCourse.get(r.getTrainingCourseId());
                    if (lst == null) {
                        lst = new ArrayList<Integer>();
                        specsMapByTrainingCourse.put(r.getTrainingCourseId(), lst);
                    }
                    lst.add(r.getSpecId());
                }
                for (Application a:result) {
                    ((ApplicationImpl)a).setCertificateNumbers(map.get(a.getId()));
                    ((TrainingCourseImpl)a.getTrainingCourse()).fillRecognizedSpecialities(specsMapByTrainingCourse.get(a.getTrainingCourseId()));
                }
                
                
            }
            return result;*/
        }
        catch(SQLException e) {
            throw Utils.logException(e);
        }
    }
    public boolean hasMoreApplicationsByApplicant(int applicantId, int excludingApplicationId) {
        try {
            Integer cnt = db.getApplicationsByApplicantCount(applicantId, excludingApplicationId);
            return cnt >= 1;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }

    public List<Application> getApplicationsByApplicant(int applicantId) {
        try {
            return generateApplications(db.getApplicationRecordsByApplicantId(applicantId));
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<Application> getApplicationsForExpert(int expertId) {
        try {
            List<Application> apps = generateApplications(db.getApplicationsForExpert(expertId));
            if (apps == null) {
            	return null;
            }
            List<Application> result =
                    apps.stream().
                            filter(a -> a.allowExpertAssignment()).
                            collect(Collectors.toList());
            /*for (Application a:apps) {
            	if (a.allowExpertAssignment()) {
            		result.add(a);
            	}
            }*/
            return result.size() == 0 ? null : result;
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    public List<Application> getApplications(List<Integer> applicationIds, OrderCriteria orderCriteria) {
        try {
        	ApplicationOrderCriteria aoc = orderCriteria instanceof ApplicationOrderCriteria ? (ApplicationOrderCriteria)orderCriteria : null;
        	return generateApplications(db.getApplicationRecords(applicationIds, aoc));

        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    private List<Application> generateApplications(List<ApplicationRecord> records) {
    	return generateApplications(db, records, nacidDataProvider);
    }
    public static List<Application> generateApplications(ApplicationsDB db, List<ApplicationRecord> records, NacidDataProviderImpl nacidDataProvider) {
    	if (records.size() == 0) {
    		return null;
    	}
    	try {
        	List<Application> result = new ArrayList<Application>();
        	//getting training courses with one query
        	List<Integer> trainingCourseIds = new ArrayList<Integer>();
        	List<Integer> applicantIds = new ArrayList<Integer>();
        	List<Integer> applicationIds = new ArrayList<Integer>();
            for (ApplicationRecord r: records) {
            	trainingCourseIds.add(r.getTrainingCourseId());
            	if (r.getApplicantId() != null) {
                    applicantIds.add(r.getApplicantId());
                }
            	applicationIds.add(r.getId());
            }

            List<TrainingCourseRecord> tcRecords = db.getTrainingCourseRecords(trainingCourseIds);
        	Map<Integer, TrainingCourseRecord> tcMap = tcRecords.stream().collect(Collectors.toMap(TrainingCourseRecord::getId, Function.identity()));

        	//getting applicants and putting them into map with key=id, and value = PersonRecord
        	List<PersonRecord> applicantRecords = db.getPersonRecords(applicantIds);
        	Map<Integer, PersonRecord> applicantsMap = applicantRecords.stream().collect(Collectors.toMap(PersonRecord::getId, Function.identity()));

        	//getting ext applications and puts them into map with key = internalApplicationId, value = ExtApplicationRecord
        	List<ExtApplicationRecord> extApplications = db.getExtApplicationRecords(applicationIds);
            Map<Integer, ExtApplicationRecord> extApplicationsMap = extApplications.stream().collect(Collectors.toMap(ExtApplicationRecord::getApplicationId, Function.identity()));


        	for (ApplicationRecord r: records) {
                ApplicationImpl app = new ApplicationImpl(nacidDataProvider, r);
                app.setTrainingCourse(tcMap.get(r.getTrainingCourseId()));
                if (r.getApplicantId() != null) {
                    app.setApplicant(applicantsMap.get(r.getApplicantId()));
                }
                app.setExtApplication(extApplicationsMap.get(r.getId()));
        		result.add(app);
            }
            return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    	
    }
    
    public Person getPerson(int personId) {
        try {
            PersonRecord record = db.selectRecord(new PersonRecord(), personId); 
        	return record == null ? null : new PersonImpl(record, nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }

    @Override
    public PersonDocument getPersonDocument(int personId) {
        try {
            return db.getPersonDocument(personId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    List<ApplicationRecognitionPurpose> getApplicationRecognitionPurposes(int applicationId) {
        try {
            List<ApplicationRecognitionPurposeRecord> records = db.getApplicationRecognitionPurposeRecords(applicationId);
            if (records.size() == 0) {
                return null;
            }
            List<ApplicationRecognitionPurpose> result = new ArrayList<ApplicationRecognitionPurpose>();
            for (ApplicationRecognitionPurposeRecord r: records) {
                result.add(new ApplicationRecognitionPurposeImpl(r, nacidDataProvider));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);

        }
    }
    public int saveApplication(
            int id,
            String applicationNumber,
            Date applicationDate,
            Integer applicantId,
            Integer applicantCompanyId,
            Integer representativeId,
            int trainingCourseId,
            String email,
            int officialEmailComm,
            int homeCountryId,
            String homeCity,
            String homePostCode,
            String homeAddressDetails,
            boolean homeIsBg,
            String bgCity,
            String bgPostCode,
            String bgAddressDetails,
            Integer bgAddressOwner,
            int createdByUserId,
            Date timeOfCreation,
            int applicationStatusId,
            Integer companyId,
            int differentDiplomaNames, 
            String homePhone, 
            String bgPhone,
            Integer reprCountryId, 
            String reprCity, 
            String reprPcode, 
            String reprAddressDetail,
            String reprPhone, 
            String summary,
            String archiveNumber,
            Boolean personalDataUsage,
            Boolean dataAuthentic, 
            Boolean representativeAuthorized,
            String notes,
            String submittedDocs,
            String applicantInfo,
            int userUpdated,
            Integer responsibleUser,
            int applicationDocflowStatusId, String representativeType, Short typePayment,
            Integer deliveryType, Boolean declaration, String courierNameAddress, String outgoingNumber, String internalNumber, Boolean isExpress, int applicantType,
            int applicationType,
            Integer documentReceiveMethodId,
            Integer applicantPersonalIdDocumentType) {

        ApplicationRecord record = new ApplicationRecord(id, applicationNumber, Utils.getSqlDate(applicationDate), applicantId, applicantCompanyId, representativeId,
                trainingCourseId, email, officialEmailComm, homeCountryId, homeCity, homePostCode, homeAddressDetails, homeIsBg ? 1 : 0, bgCity, 
                bgPostCode, bgAddressDetails, bgAddressOwner, createdByUserId, timeOfCreation == null ? null : new Timestamp(timeOfCreation.getTime()), applicationStatusId, 
                        companyId, differentDiplomaNames, homePhone, bgPhone,
                        reprCountryId, reprCity, reprPcode, reprAddressDetail, reprPhone, summary, archiveNumber, DataConverter.parseBooleanToInteger(personalDataUsage), DataConverter.parseBooleanToInteger(dataAuthentic), DataConverter.parseBooleanToInteger(representativeAuthorized), notes,
                        		submittedDocs, applicantInfo, responsibleUser, applicationDocflowStatusId,  representativeType,  typePayment,
                 deliveryType,  declaration,  courierNameAddress,  outgoingNumber,  internalNumber,  isExpress, applicantType, applicationType, documentReceiveMethodId, applicantPersonalIdDocumentType);
        
        try {
            int oldApplicationStatusId;
            int oldDocflowStatusId;
        	if (id == 0) {
                    if (applicationNumber == null && applicationDate == null) {
                        prepareApplicationNumberAndDate(record);
                    }
                    record = db.insertRecord(record);

                    if (applicationType == ApplicationType.RUDI_APPLICATION_TYPE) {
                        addApplicationKind(record.getId(), NumgeneratorDataProvider.NACID_SERIES_ID, BigDecimal.ZERO, null, null);
                    } else if (applicationType == ApplicationType.DOCTORATE_APPLICATION_TYPE) {
                        addApplicationKind(record.getId(), NumgeneratorDataProvider.DOCTORATE_SERIES_ID, BigDecimal.ZERO, null, null);


                        //pri doctorate se vyvejda tochno po edin diplomaType za vseki application!
                        DiplomaTypeDataProvider diplomaTypeDataProvider = nacidDataProvider.getDiplomaTypeDataProvider();
                        try {
                            int dtId = diplomaTypeDataProvider.saveDiplomaType(0, null, null, null, null, null, null, null, "DT-" + record.getApplicationNumber(), null, null, false, null, null, null, null, null, null, DiplomaType.TYPE_DOCTORATE);
                            db.updateTrainingCourseDiplomaType(trainingCourseId, dtId);

                        } catch (DiplomaTypeEditException e) {
                            throw Utils.logException(e);
                        }


                    }
                
                    //settva Integer.MIN_VALUE za oldApplicationStatusId, za da se garantira 4e pri insert na novo zaqvlenie shte se zapishe stojnost i v app_status_history
                    oldApplicationStatusId = Integer.MIN_VALUE;
                    oldDocflowStatusId = Integer.MIN_VALUE;
            } else {
                ApplicationRecord oldRecord = new ApplicationRecord();

                oldRecord = db.selectRecord(oldRecord, record.getId());
                record.setApplicationNumber(oldRecord.getApplicationNumber());
                record.setApplicationDate(oldRecord.getApplicationDate());
                record.setFinalStatusHistoryId(oldRecord.getFinalStatusHistoryId());
                db.updateRecord(record);

                oldApplicationStatusId = oldRecord.getApplicationStatusId();
                oldDocflowStatusId = oldRecord.getDocflowStatusId();
            }

            addStatusHistoryRecordIfStatusChanged(record.getId(), oldApplicationStatusId, applicationStatusId, null, null, userUpdated);

            addDoclfowStatusHistoryRecordIfStatusChanged(record.getId(), oldDocflowStatusId, applicationDocflowStatusId, userUpdated);
            return record.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void deleteApplicationKinds(int applicationId) {
        try {
            db.deleteApplicationKindRecords(applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void addApplicationKind(int applicationId, int entryNumSeriesId, BigDecimal price, String entryNum, Integer finalStatusHistoryId) {
        try {
            db.addApplicationKindRecord(applicationId, entryNumSeriesId, price, entryNum, finalStatusHistoryId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


    public ApplicationStatus getApplicationStatusByAppStatusHistoryId(int appStatusHistoryId) {
        try {
            AppStatusHistoryRecord rec = db.getAppStatusHistoryRecord(appStatusHistoryId);
            return nacidDataProvider.getNomenclaturesDataProvider().getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, rec.getStatusId());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    private void prepareApplicationNumberAndDate(ApplicationRecord record) {
        if (record.getApplicationType() == ApplicationType.RUDI_APPLICATION_TYPE) {
            NumgeneratorDataProvider numgeneratorDataProvider = nacidDataProvider.getNumgeneratorDataProvider();
            record.setApplicationNumber(numgeneratorDataProvider.getNextNumber(NumgeneratorDataProvider.NACID_SERIES_ID, NumgeneratorDataProvider.SUBMISSION_TYPE_DESK));
            record.setApplicationDate(Utils.getSqlDate(numgeneratorDataProvider.getWorkingDate()));
        } else if (record.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE) {
            NumgeneratorDataProvider numgeneratorDataProvider = nacidDataProvider.getNumgeneratorDataProvider();
            record.setApplicationNumber(numgeneratorDataProvider.getNextNumber(NumgeneratorDataProvider.DOCTORATE_SERIES_ID, NumgeneratorDataProvider.SUBMISSION_TYPE_DESK));
            record.setApplicationDate(Utils.getSqlDate(numgeneratorDataProvider.getWorkingDate()));
        } else {
            throw new RuntimeException("Cannot generate application number for applicationType = " + record.getApplicationType());
        }

        
    }
    
    
    public int savePerson(int id, String firstName, String surName, String lastName, 
            String civilId, int civilIdType, Integer birthCountry, String birthCity,
            Date birthDate, Integer citizenshipId) {
        
        if(firstName != null) firstName = firstName.toUpperCase();
        if(surName != null) surName = surName.toUpperCase();
        if(lastName != null) lastName = lastName.toUpperCase();
        
        PersonRecord record = new PersonRecord(id, firstName, surName, lastName, civilId, 
                civilIdType, birthCountry, birthCity, Utils.getSqlDate(birthDate), citizenshipId);
        try {
            if (id == 0) {
                record = db.insertRecord(record);
            } else {
                db.updateRecord(record);
            }
            return record.getId();
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public int savePersonDocument(Integer id, int personId, Date dateOfIssue, String issuedBy, String number) {
        try {
            PersonDocumentRecord rec = new PersonDocumentRecord(null, number, dateOfIssue, issuedBy, 1, personId);
            rec = db.savePersonDocumentRecord(rec);
            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    public void updateApplicationStatus(int applicationId, int applicationStatusId, int docflowStatusId, String arhiveNumber, Integer legalReasonId, String submittedDocs, int userUpdated) {
        try {
            ApplicationRecord record = db.selectRecord(new ApplicationRecord(), applicationId);
            if (record == null) {
                return;
            }
            int oldApplicationStatus = record.getApplicationStatusId();
            int oldApplicationDocflowStatus = record.getDocflowStatusId();
            record.setApplicationStatusId(applicationStatusId);
            if(arhiveNumber != null) {
                record.setArchiveNumber(arhiveNumber);
            }
            if (submittedDocs != null) {
            	record.setSubmittedDocs(submittedDocs);
            }
            record.setDocflowStatusId(docflowStatusId);
            db.updateRecord(record);
            

            addStatusHistoryRecordIfStatusChanged(applicationId, oldApplicationStatus, applicationStatusId, legalReasonId, null, userUpdated);
            addDoclfowStatusHistoryRecordIfStatusChanged(record.getId(), oldApplicationDocflowStatus, docflowStatusId, userUpdated);

        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public void updateMotives(int applicationId, int applicationStatusId, String applicantInfo, String motives, Integer legalReasonId, int sessionId, int userUpdated) {
    	try {
    		ApplicationRecord record = db.selectRecord(new ApplicationRecord(), applicationId);
    		int oldApplicationStatusId = record.getApplicationStatusId();
    		record.setApplicantInfo(applicantInfo);
    		record.setSummary(motives);
    		record.setApplicationStatusId(applicationStatusId);
    		db.updateRecord(record);
    		

            addStatusHistoryRecordIfStatusChanged(applicationId, oldApplicationStatusId, applicationStatusId, legalReasonId, sessionId, userUpdated);

    		
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
    }


    /**
     * dobavq zapis v appStatusHistory ako status-a na zaqvlenieto e promenen!
     * 
     * @param applicationId
     * @param oldApplicationStatusId - star status
     * @param newApplicationStatusId - nov status
     * @param newLegalReasonId - nov legalReasonId
     * @return
     * @throws SQLException
     */
    private AppStatusHistoryRecord addStatusHistoryRecordIfStatusChanged(int applicationId, int oldApplicationStatusId, int newApplicationStatusId, Integer newLegalReasonId, Integer sessionId, int userAssigned) throws SQLException {
    	 List<AppStatusHistoryRecord> appStatuses = db.getAppStatusHistoryRecords(applicationId);
         AppStatusHistoryRecord lastAppStatus = appStatuses.size() == 0 ? null : appStatuses.get(0);


        List<ApplicationKind> applicationKinds = getApplicationKindsPerApplication(applicationId);

         //Zapis v historyto se zapisva samo ako e promenen status-a ili e promenen legalReason-a
         if(
        		oldApplicationStatusId != newApplicationStatusId || 
         		lastAppStatus == null || 
         		(
         				( lastAppStatus.getStatLegalReasonId() == null && newLegalReasonId != null ) ||
         				( lastAppStatus.getStatLegalReasonId() != null && newLegalReasonId == null ) ||
         				( lastAppStatus.getStatLegalReasonId() != null && newLegalReasonId != null && lastAppStatus.getStatLegalReasonId().intValue() != newLegalReasonId) )
         		) {

             AppStatusHistoryRecord histRec = new AppStatusHistoryRecord(0, applicationId, newApplicationStatusId, new java.sql.Date(System.currentTimeMillis()), newLegalReasonId, sessionId, userAssigned);
             histRec = db.insertRecord(histRec);

             NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
             // update-va final_status_history_id v application obekta pri neobhodimost (t.e. statusa e smenen + noviq status e praven)
             ApplicationStatus status = nomenclaturesDataProvider.getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, histRec.getStatusId());
             if (status.isLegal()) {
                 db.updateApplicationFinalStatusHistoryRecordId(applicationId, histRec.getId());
             }



             //settva finalStatusId za vseki edin applicationKind (ako takushtiq status e final(legal) za dadeniq tip applicationKind)
             if (applicationKinds != null) {
                 for (ApplicationKind applicationKind : applicationKinds) {
                     ApplicationStatus statPerApplicationKind = nomenclaturesDataProvider.getApplicationStatus(applicationKind.getEntryNumSeriesId(), histRec.getStatusId());
                     if (statPerApplicationKind != null && statPerApplicationKind.isLegal()) {
                         db.updateApplicationKindFinalStatusHistoryRecordId(applicationKind.getId(), histRec.getId());
                     }
                 }
             }
             return histRec;
         }
        return null;

    }


    private void addDoclfowStatusHistoryRecordIfStatusChanged(int applicationId, int oldDocflowStatusId, int newDocflowStatusId, int userAssigned) throws SQLException {
        List<AppDocflowStatusHistoryRecordExtended> appStatuses = db.getAppDocflowStatusHistoryRecords(applicationId);
        AppDocflowStatusHistoryRecord lastAppStatus = appStatuses.size() == 0 ? null : appStatuses.get(0);
        //Zapis v historyto se zapisva samo ako e promenen status-a
        if(oldDocflowStatusId != newDocflowStatusId ||lastAppStatus == null) {
            AppDocflowStatusHistoryRecord histRec = new AppDocflowStatusHistoryRecord(0, applicationId, newDocflowStatusId, new java.sql.Date(System.currentTimeMillis()), userAssigned);
            db.insertRecord(histRec);
        }
    }

    public List<Person> getPersons(int civilIdType, String civilId, boolean isPartOfCivilId) {
        try {
            List<PersonRecord> records = db.getPersonRecords(civilIdType, civilId, isPartOfCivilId);
            return records.size() == 0 ? null : records.stream().map(r -> new PersonImpl(r, nacidDataProvider)).collect(Collectors.toList());

        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<Person> getPersons() {
        try {
            List<PersonRecord> records = db.selectRecords(PersonRecord.class, null);
            return records.size() == 0 ? null : records.stream().map(r -> new PersonImpl(r, nacidDataProvider)).collect(Collectors.toList());
            /*if (records.size() == 0) {
                return null;
            }
            List<Person> result = new ArrayList<Person>();
            for (PersonRecord p : records) {
                result.add(new PersonImpl(p, nacidDataProvider));
            }
            return result;*/
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public Person getPersonByCivilId(String civilId) {
        try {
            PersonRecord record = db.selectRecord(PersonRecord.class, " civil_id = ? ", civilId);
            if (record != null) {
                return new PersonImpl(record, nacidDataProvider);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return null;
    }

    @Override
    public boolean deletePerson(int personId) {
        try {
            db.deleteRecord(PersonRecord.class, personId);
            return true;
        } catch (SQLException e) {
            if(e.getMessage().indexOf("violates foreign key constraint") != -1) {
                e.printStackTrace();
                return false;
            } 
            throw Utils.logException(e);
        }
    }

    public void deleteApplicationRecognitionPurpose(int applicationId) {
        try {
            db.deleteApplicationRecognitionPurposeRecord(applicationId);
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public void addApplicationRecognitionPurposeRecord(int applicationId, Integer recognitionPurposeId, String notes) {
        try {
            ApplicationRecognitionPurposeRecord record = new ApplicationRecognitionPurposeRecord(0, applicationId, recognitionPurposeId, notes);
            db.insertRecord(record);
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    /**
     *Tozi method se polzva v ApplicationImp 
     */
    public List<ApplicationExpert> getApplicationExperts(int applicationId) {
        try {
            List<ApplicationExpertRecord> records = db.getApplicationExperts(applicationId);
            if (records.size() == 0) {
                return null;
            }
            List<ApplicationExpert> result = new ArrayList<ApplicationExpert>();
            for (ApplicationExpertRecord record:records) {
                result.add(new ApplicationExpertImpl(nacidDataProvider, record));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<ApplicationExpertSpecialityRecord> getApplicationExpertSpecialities(int applicationId, int expertId) {
        try {
            return db.getApplicationExpertSpecialities(applicationId, expertId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }


    }
    
    @Override
    public boolean hasExpertAccessToApplication(int applicationId, int expertId) {
        try {
            return db.hasExpertAccessToApplication(applicationId, expertId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public void deleteApplicationExperts(int applicationId) {
        try {
            db.deleteApplicationExperts(applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public void addApplicationExpert(int applicationId, int expertId, String notes, int finished, String courseContent, Integer expertPosition, Integer legalReasonId, List<Integer> specialityIds, Integer qualificationId, Integer eduLevelId, String previousBoardDecisions, String similarBulgarianPrograms) {
        try {
            ApplicationExpertRecord record = new ApplicationExpertRecord(0, applicationId, expertId, notes, finished, courseContent, expertPosition, legalReasonId, qualificationId, eduLevelId, previousBoardDecisions, similarBulgarianPrograms);
            db.insertRecord(record);
            if (specialityIds != null) {
                for (Integer specialityId : specialityIds) {
                    ApplicationExpertSpecialityRecord r = new ApplicationExpertSpecialityRecord(0, applicationId, expertId, specialityId);
                    db.insertRecord(r);
                }
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<AppStatusHistory> getAppStatusHistory(int applId) {
        try {
            List<AppStatusHistoryRecord> recs = db.getAppStatusHistoryRecords(applId);
            List<AppStatusHistory> ret = new ArrayList<AppStatusHistory>();
            for(AppStatusHistoryRecord rec : recs) {
                ret.add(new AppStatusHistoryImpl(rec, nacidDataProvider));
            }
            return ret;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<AppDocflowStatusHistory> getAppDocflowStatusHistory(int applicationId) {
        try {
            List<AppDocflowStatusHistoryRecordExtended> recs = db.getAppDocflowStatusHistoryRecords(applicationId);
            if (recs.size() == 0) {
                return null;
            }
            List<AppDocflowStatusHistory> result = new ArrayList<AppDocflowStatusHistory>();
            for (AppDocflowStatusHistoryRecordExtended rec : recs) {
                result.add(new AppDocflowStatusHistoryImpl(rec));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }



    public Integer getLastLegalReasonId(int applicationId) {
    	List<AppStatusHistory> appStatuses = getAppStatusHistory(applicationId);
    	return appStatuses == null || appStatuses.size() == 0 ? null : appStatuses.get(0).getStatLegalReasonId();
    }
    
    
    
    


	public boolean isElectronicallyApplied(int applicationId) {
		ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
		return extApplicationsDataProvider.getApplicationByInternalApplicationId(applicationId) != null;
	}


    @Override
    public void setExpertProcessStatus(int applicationId, int expertId, int pocessStat) {
        try {
            ApplicationExpertRecord rec = db.getApplicationExpert(applicationId, expertId);
            rec.setProcessStat(pocessStat);
            db.updateRecord(rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


    @Override
    public ApplicationExpert getApplicationExpert(int applicationId, int expertId) {
        try {
            ApplicationExpertRecord rec = db.getApplicationExpert(applicationId, expertId);
            if(rec == null) return null;
            return new ApplicationExpertImpl(nacidDataProvider, rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    


    @Override
    public Integer getApplicationIdForExtReport(String docflowNumber, Date docflowDate, String personalId) {
        try {
            return db.getApplicationIdForExtReport(docflowNumber,
                    Utils.getSqlDate(docflowDate), personalId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
	 
    @Override 
    public Application getApplicationByMail(String email) {
        try {
            ApplicationRecord rec = db.getApplicationByEMail(email);
            if(rec == null) return null;
            return new ApplicationImpl(nacidDataProvider, rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    /**
     * proverqva dali moje da se generira certificateNumber - toq method realno izvikva {@link #generateCertificateNumber(int)}
     * i ako pri optit za generiraneto na certificate-a se hvyrli nqkakyv exception toi se re-throw-va
     */
    public void checkCertificateNumberGeneration(int applicationId) throws CertificateNumberGenerationException {
    	generateCertificateNumber(applicationId);
    }
    public String generateCertificateNumber(int applicationId) throws CertificateNumberGenerationException {
        Application app = getApplication(applicationId);
        if (app == null) {
            throw new RuntimeException("No application with applicationId = " + applicationId);
        }
        if (app.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE) {
            return generateDoctorateCertificateNumber(app);
        } else {
            return generateRudiCertificateNumber(app);
        }
    }
    private String generateDoctorateCertificateNumber(Application app) throws CertificateNumberGenerationException {
        try {
            String certificateNumber;
            //Ako zaqvlenieto ima ve4e izdaden certificateNumber, t.e. ima zapis v tablicata cert_number_to_attached_docs, togava stava vypros za pre-izdavane na nov certificateNumber, koito se polu4ava ot startiq, kato mu se dobavi xx-yy-1/dneshna data;xx-yy-2/dneshna data i t.n.
            List<CertificateNumberToAttachedDocRecord> certificateNumberToAttachedDocRecords = db.getCertificateNumberToAttachedDocRecords(app.getId(), null);
            checkApplicationStatusForCertificate(certificateNumberToAttachedDocRecords, app);
            if (certificateNumberToAttachedDocRecords != null && certificateNumberToAttachedDocRecords.size() > 0) {
                String oldCertificateNumber = certificateNumberToAttachedDocRecords.get(0).getCertificateNumber();
                String oldNumber = oldCertificateNumber.split("/")[0];
                String[] parts = oldNumber.split("-");
                Integer currentCertificateNumber = null;
                if (parts.length == 3) {
                    currentCertificateNumber = 1;
                } else if (parts.length == 4) {
                    currentCertificateNumber = DataConverter.parseInteger(parts[3], null) + 1;
                }
                if (currentCertificateNumber == null) {
                    throw new RuntimeException("Unexpected old certificateNumber format. Expected xx-yy-nn/dd.mm.yyyy. OldCertificateNumber=" + oldCertificateNumber);
                }
                certificateNumber = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + currentCertificateNumber + "/" + DataConverter.formatDate(new Date());
            } else {
                certificateNumber = app.getApplicationNumber() + "/" + DataConverter.formatDate(new Date());
            }
            return certificateNumber;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    private String generateRudiCertificateNumber(Application app) throws CertificateNumberGenerationException {
    	try {


        	
        	String certificateNumber = "";
        	//Ako zaqvlenieto ima ve4e izdaden certificateNumber, t.e. ima zapis v tablicata cert_number_to_attached_docs, togava stava vypros za pre-izdavane na nov certificateNumber, koito se polu4ava ot startiq, kato mu se dobavi xx-yy-1/dneshna data;xx-yy-2/dneshna data i t.n.
        	List<CertificateNumberToAttachedDocRecord> certificateNumberToAttachedDocRecords = db.getCertificateNumberToAttachedDocRecords(app.getId(), null);
            checkApplicationStatusForCertificate(certificateNumberToAttachedDocRecords, app);
        	if (certificateNumberToAttachedDocRecords != null && certificateNumberToAttachedDocRecords.size() > 0) {
        		String oldCertificateNumber = certificateNumberToAttachedDocRecords.get(0).getCertificateNumber();
        		String oldNumber = oldCertificateNumber.split("/")[0];
        		String[] parts = oldNumber.split("-");
        		Integer currentCertificateNumber;
        		if (parts.length != 3) {
        			currentCertificateNumber = 1;
        		} else {
        			currentCertificateNumber = DataConverter.parseInteger(parts[2], null);
        			if (currentCertificateNumber == null) {
        				throw new RuntimeException("Unexpected old certificateNumber format. Expected xx-yy-nn/dd.mm.yyyy. OldCertificateNumber=" + oldCertificateNumber);
        			}
        			currentCertificateNumber++;
        			
        		}
        		certificateNumber = parts[0] + "-" + parts[1] + "-" + currentCertificateNumber + "/" + DataConverter.formatDate(new Date());
        	} else {
                DocFlowNumber docFlowNumber = new DocFlowNumber(app.getDocFlowNumber(), false);
            	certificateNumber += docFlowNumber.getPrepNo() + "-";
            	CommissionCalendarDataProviderImpl commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
            	CommissionCalendar commissionCalendar = commissionCalendarDataProvider.getLastCommissionExaminedApplication(app.getId());
            	if (commissionCalendar == null) {
            		throw new CertificateNumberGenerationException("Не може да се генерира номер на удостоверение, тъй като заявлението не е разгледано от комисия.");
            	}
            	certificateNumber += commissionCalendar.getSessionNumber();
            	certificateNumber += "/" + DataConverter.formatDate(commissionCalendar.getDateAndTime());
        	}
        	
        	return certificateNumber;
    	} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }

    /**
     * proverqva dali zaqvlenieto e s podhodqsht status za izdavane na certificate!
     * @param app
     * @throws CertificateNumberGenerationException
     */
    private void checkApplicationStatusForCertificate(List<CertificateNumberToAttachedDocRecord> certificateNumberToAttachedDocRecords, Application app) throws CertificateNumberGenerationException {
        if (CollectionUtils.isEmpty(certificateNumberToAttachedDocRecords) && app.getApplicationStatusId() != ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE) {
            FlatNomenclature priznatoStatus = nacidDataProvider.getNomenclaturesDataProvider().getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE);
            throw new CertificateNumberGenerationException("Не може да се генерира номер на удостоверение, тъй като статуса на заявлението не е " + priznatoStatus.getName());
        }
    }

    
    public void saveCertificateNumber(int applicationId, int attachmentId, String certificateNumber, UUID certificateUuid) {
    	try {
    	    if (certificateNumber == null) {
    	        throw new RuntimeException("Certificate number should not be null!");
            }
            db.invalidateOldCertificateNumbers(applicationId, AttachmentDataProvider.CERTIFICATE_STATUS_UNISHTOJENO);

    	    db.insertRecord(new CertificateNumberToAttachedDocRecord(0, certificateNumber, attachmentId, applicationId, 0, certificateUuid));
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    /**
     * vry6ta zapisa ot bazata 
     * @param applicationId
     * @return
     */
    public String getCertificateNumber(int applicationId) {
    	try {
    		List<CertificateNumberToAttachedDocRecord> records = db.getCertificateNumberToAttachedDocRecords(applicationId, 0);
    		if (records.size() == 0) {
    			return null;
    		}
    		return records.get(0).getCertificateNumber();
    	} catch (SQLException e) {
    		throw Utils.logException(e);
		}
    }

    public List<CertificateNumberToAttachedDocRecord> getAllCertificateNumbers(int applicationId) {
        try {
            return db.getCertificateNumberToAttachedDocRecords(applicationId, null);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public Integer getApplicationIdForExtReport(String certNumber, String personalId) {
    	try {
    		return db.getApplicationIdForExtReport(certNumber, personalId);
    	} catch (SQLException e) {
    		throw Utils.logException(e);
		}
    }
    public List<String> getEmployeesWorkedOnApplication(int applicationId) {
        try {
            List<ApplicationChangesHistoryRecord> records = db.selectRecords(ApplicationChangesHistoryRecord.class, "application_id = ?", applicationId);
            List<Integer> userIds = new ArrayList<Integer>();
          
            for (ApplicationChangesHistoryRecord r:records) {
                userIds.add(r.getUserId());
            }
            List<UserRecord> usrs = db.selectRecords(UserRecord.class, "id in ( " + StringUtils.join(userIds, ", ") + ") ");
            return usrs.size() == 0 ? null : usrs.stream().map(UserRecord::getFullName).collect(Collectors.toList());

            /*List<String> result = new ArrayList<String>();
            for (UserRecord r:usrs) {
                result.add(r.getFullName());
            }
            return result.size() == 0 ? null : result;*/
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public ApplicationsDB getApplicationsDB() {
    	return db;
    }
    public int getApplicationIdByDiplomaExaminationId(int diplomaExaminationId) {
        try {
            return db.getApplicationIdByDiplomaExaminationId(diplomaExaminationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public void duplicateApplicationCertificate(int applicationId, int userChanged) {
        duplicateApplicationCertificate(applicationId, DUPLICATE_TYPE.DUPLICATE_TYPE_CERTIFICATE_DUPLICATE, userChanged);
    }
    public void duplucateApplicationCertificateBecauseOfFactualError(int applicationId, int userChanged) {
        duplicateApplicationCertificate(applicationId, DUPLICATE_TYPE.DUPLICATE_TYPE_CERTIFICATE_FACTUAL_ERROR, userChanged);
    }
    private static enum DUPLICATE_TYPE {
        DUPLICATE_TYPE_CERTIFICATE_DUPLICATE,
        DUPLICATE_TYPE_CERTIFICATE_FACTUAL_ERROR

    }


    private void duplicateApplicationCertificate(int applicationId, DUPLICATE_TYPE duplicateType, int userChanged) {
        try {

            ApplicationRecord applicationRecord = db.selectRecord(ApplicationRecord.class, "id = ?", applicationId);
            boolean isDoctorateApplication = applicationRecord.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE;
            int newCertificateDocumentType;
            int unishtojenoDocType;
            switch (duplicateType) {
                case DUPLICATE_TYPE_CERTIFICATE_DUPLICATE:
                    break;
                case DUPLICATE_TYPE_CERTIFICATE_FACTUAL_ERROR:
                    break;
            }
            if (!isDoctorateApplication) {
                newCertificateDocumentType = duplicateType == DUPLICATE_TYPE.DUPLICATE_TYPE_CERTIFICATE_DUPLICATE ? DOC_TYPE_CERTIFICATE_DUPLICATE : (duplicateType == DUPLICATE_TYPE.DUPLICATE_TYPE_CERTIFICATE_FACTUAL_ERROR ? DOC_TYPE_CERTIFICATE_FACTUAL_ERROR : null);
                unishtojenoDocType = DOC_TYPE_UNISHTOJENO;
            } else {
                newCertificateDocumentType = duplicateType == DUPLICATE_TYPE.DUPLICATE_TYPE_CERTIFICATE_DUPLICATE ? DOC_TYPE_DOCTORATE_CERTIFICATE_DUPLICATE : (duplicateType == DUPLICATE_TYPE.DUPLICATE_TYPE_CERTIFICATE_FACTUAL_ERROR ? DOC_TYPE_DOCTORATE_CERTIFICATE_FACTUAL_ERROR : null);
                unishtojenoDocType = DOC_TYPE_DOCTORATE_UNISHTOJENO;
            }


            AttachmentDB appAttachmentDb = new AttachmentDB(nacidDataProvider.getDataSource(), AttachmentDB.ATTACHMENT_TYPE.APPLICATION);

            CommissionCalendarDB commissionCalendarDB = nacidDataProvider.getCommissionCalendarDataProvider().getDb();
            int commissionAgendaId = commissionCalendarDB.getLastCommissionIdExaminedApplication(applicationId);

            //promenq tipa na starite sertifikati na unishtojeno
            List<CertificateNumberToAttachedDocRecord> oldCertificates = db.getCertificateNumberToAttachedDocRecords(applicationId, 0);

            for (CertificateNumberToAttachedDocRecord oldCertificate : oldCertificates) {
                if (oldCertificate.getAttachedDocId() != null) {
                    appAttachmentDb.updateDocumentType(oldCertificate.getAttachedDocId(), unishtojenoDocType);
                }
            }
            //kraj na promqnata na tip

            //Mnogo stranno - za rudi applications (nedoktorski stepeni), certificate number-a izglejda vyv vida na applicationNumber-commissionAgenda/currentDate, dokato getCertificateNumber() generira applicationNumber-commissionAgenda-index/currentDate
            //Za da ne schupq neshto ostavam stariq kod za nedoktorksi stepeni, a za doktorksi vikam getCertificateNumber(). Bi bilo po-logichno i za rudi apps da se vika tozi kod, no zashto ima razlika v generiraniq nomer (nqma go -index), ne znam...
            //12.03.2020 - razkarvam go tozi kod, zashtoto spored men ne e OK! Normalno e da si ima -index v nomera. Pone gledam poslednoto staro generirano udostoverenie za OFG i to ima -> applicationId=10257
            /*String certificateNumber;
            if (!isDoctorateApplication) {
                DocFlowNumber docFlowNumber = new DocFlowNumber(applicationRecord.getApplicationNumber() + "/" + DataConverter.formatDate(applicationRecord.getApplicationDate()), false);

                certificateNumber = docFlowNumber.getPrepNo() + "-";
                certificateNumber += commissionAgendaId;
                certificateNumber += "/" + DataConverter.formatDate(new Date());
            } else {

            }*/

            String certificateNumber = generateCertificateNumber(applicationId);
            UUID uuid = UUID.randomUUID();
            InputStream is = TemplateGenerator.generateCertificate(nacidDataProvider, newCertificateDocumentType, new TemplateGenerator.GenerateCertificateRequest(getApplicationDetailsForReport(applicationId), certificateNumber, uuid));

            DocumentType documentType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(newCertificateDocumentType);
            String fileName = applicationRecord.getApplicationNumber() + "_" + documentType.getDocumentTemplate() + "_" + DataConverter.formatDate(new Date()) + ".doc";
            AttachmentRecord certificate = new AttachmentRecord(0, applicationId, null, newCertificateDocumentType, "application/msword", fileName, is, 0, null, null, null, null, null);

            certificate = appAttachmentDb.saveRecord(certificate, is.available(), 0, userChanged);

            saveCertificateNumber(applicationId, certificate.getId(),  certificateNumber, uuid);
        } catch (SQLException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        }

    }
    public List<ApplicationKind> getApplicationKindsPerApplication(int applicationId) {
        try {
            List<ApplicationKindRecord> recs = db.getApplicationKindsPerApplication(applicationId);
            if (recs.size() == 0) {
                return null;
            }
            return recs.stream().map(rec -> new ApplicationKindImpl(nacidDataProvider, rec)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


    public List<SimilarDiploma> getSimilarDiplomas(String civilId, String firstName, List<Integer> universityCountryIds, List<String> universityCountryNames, Integer eduLevelId, Integer diplomaYear, Integer skipApplicationId) {
        try {
            List<SimilarDiplomaRecord> res = db.getSimilarDiplomas(civilId, firstName, universityCountryIds, universityCountryNames, eduLevelId, diplomaYear, skipApplicationId);
            return res.size() == 0 ? null : res.stream().map(Function.identity()).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public DocumentRecipient getDocumentRecipient(int applicationId) {
        try {
            DocumentRecipientRecord res = db.getDocumentRecipient(applicationId);
            return res == null ? null : new DocumentRecipientImpl(res, nacidDataProvider.getNomenclaturesDataProvider());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void saveDocumentRecipient(int applicationId, String name, int countryId, String city, String district, String postCode, String address, String mobilePhone) {
        try {
            db.saveDocumentRecipient(applicationId, name, countryId, city, district, postCode, address, mobilePhone);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void deleteDocumentRecipient(int applicationId) {
        try {
            db.deleteDocumentRecipient(applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public static void main(String[] args) throws SQLException, DocFlowException {
    	NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
    	ApplicationsDataProviderImpl applicationsDataProvider = (ApplicationsDataProviderImpl) nacidDataProvider.getApplicationsDataProvider();
    	//Application a = applicationsDataProvider.getApplication(100);
    	//ApplicationDetailsForReport report = applicationsDataProvider.getApplicationDetailsForReport(3963);
    	//System.out.println(CommissionApplicationsHandler.getFinishedByExpertsText(a));
    	//System.out.println("0882 84 55 47/ 0886 715 184/0031 622 011 210".length());
    }
}
