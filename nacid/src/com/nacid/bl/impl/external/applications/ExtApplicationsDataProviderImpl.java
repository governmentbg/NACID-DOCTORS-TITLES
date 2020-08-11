package com.nacid.bl.impl.external.applications;

import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.TrainingCourseDataProvider;
import com.nacid.bl.external.ExtAddress;
import com.nacid.bl.external.ExtApplicationKind;
import com.nacid.bl.external.ExtDocumentRecipient;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.applications.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.TrainingCourseSpecialityImpl;
import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.bl.impl.external.ExtAddressImpl;
import com.nacid.bl.impl.external.ExtApplicationKindImpl;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.external.applications.*;
import com.nacid.data.external.applications.xml.ExtApplicationsXml;
import com.nacid.data.external.applications.xml.ObjectFactory;
import com.nacid.db.external.applications.ExtApplicationsDB;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.esign.SignatureUtils;
import com.nacid.web.exceptions.UnknownRecordException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ExtApplicationsDataProviderImpl implements ExtApplicationsDataProvider {

    private NacidDataProviderImpl nacidDataProvider;
    private ExtApplicationsDB db;
    //private boolean esignConfigured = true;
    
    public ExtApplicationsDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new ExtApplicationsDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }


    public ExtApplication getApplication(int applicationId) {
        try {
            ExtApplicationRecord record = new ExtApplicationRecord();
            record = db.selectRecord(record, applicationId);
            if (record == null) {
                return null;
            }
            return new ExtApplicationImpl(nacidDataProvider, record);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    public synchronized int transferApplicationToIntDB(int extApplicationId, int loggedUserId, Integer applicantId, Integer applicantCompanyId, Integer representativeId, int ownerId, List<Integer> universityIds, List<Integer> specialityIds, String diplomaSeries, String diplomaNumber, String diplomaRegistrationNumber, Date diplomaDate) {
        ApplicationsDataProvider appDP = nacidDataProvider.getApplicationsDataProvider();
        TrainingCourseDataProvider tcDP = nacidDataProvider.getTrainingCourseDataProvider();
        ExtTrainingCourseDataProvider extTcDP = nacidDataProvider.getExtTrainingCourseDataProvider();
        ExtApplicationAttachmentDataProvider extAttDP = nacidDataProvider.getExtApplicationAttachmentDataProvider();
        ExtApplicationsDataProvider extApplDP = nacidDataProvider.getExtApplicationsDataProvider();
        
        ExtApplication extAppl = getApplication(extApplicationId);
        
        if(extAppl == null) {
            throw new UnknownRecordException("no ext application id=" + extApplicationId);
        }
        if (extAppl.getInternalApplicationId() != null) {
            throw new RuntimeException("Already transferred...");
        }
        
        ExtTrainingCourse extTC = extAppl.getTrainingCourse();
        ExtPerson extPerson = extAppl.getApplicant();



        int tcId = tcDP.saveTrainingCourse(0, diplomaSeries, diplomaNumber, diplomaRegistrationNumber, diplomaDate,
                null, extTC.getFName(), extTC.getSName(), 
                extTC.getLName(), 
                false,//isjointdegree 
                /*extTrainingCourseTrainingLocation.getTrainingLocationCountryId(),
                extTrainingCourseTrainingLocation.getTrainingLocationCity(),*/
                extTC.getTrainingStart(), 
                extTC.getTrainingEnd(), extTC.getTrainingDuration(), 
                extTC.getDurationUnitId(), extTC.getCredits(), 
                extTC.getEducationLevelId(),
                extTC.getQualificationId(), false, 
                extTC.getSchoolCountryId(), extTC.getSchoolCity(), 
                extTC.getSchoolName(), extTC.getSchoolGraduationDate(), 
                extTC.getSchoolNotes(), extTC.getPrevDiplomaUniversityId(), 
                extTC.getPrevDiplomaEduLevelId(), 
                extTC.getPrevDiplomaGraduationDate(), extTC.getPrevDiplomaNotes(), extTC.getPrevDiplomaSpecialityId(),
                null, null, extTC.getGraduationDocumentTypeId(), extTC.getCreditHours(), extTC.getEctsCredits(),
                ownerId, extTC.getThesisTopic(), extTC.getThesisTopicEn(), extTC.getProfGroupId(), null, extTC.getThesisDefenceDate(), extTC.getThesisBibliography(), extTC.getThesisVolume(), extTC.getThesisLanguageId(), extTC.getThesisAnnotation(), extTC.getThesisAnnotationEn(),
                extTC.getOriginalQualificationId());


        List<TrainingCourseSpecialityImpl> trainingCourseSpecialities = new ArrayList<>();
        if (specialityIds != null) {
            for (int i:specialityIds) {
                trainingCourseSpecialities.add(new TrainingCourseSpecialityImpl(tcId, i, null));
            }
            tcDP.saveTrainingCourseSpecialities(tcId, trainingCourseSpecialities);
        }

        //Saving training locations
        for (ExtTrainingCourseTrainingLocation trainingLocation: extTC.getTrainingCourseTrainingLocations()) {
        	tcDP.saveTrainingCourseTrainingLocation(0, tcId, trainingLocation.getTrainingLocationCountryId(), trainingLocation.getTrainingLocationCity(), null);
        }
        //End of zaving training locations
        
        
        if (universityIds.size() > 0) {
            List<UniversityIdWithFacultyId> unisWithFaculty = universityIds.stream().map(r -> new UniversityIdWithFacultyId(r, null)).collect(Collectors.toList());
            tcDP.updateTrainingCourseUniversities(tcId, unisWithFaculty.get(0), unisWithFaculty.size() == 1 ? null : unisWithFaculty.subList(1, unisWithFaculty.size()));
        }
        
        List<ExtGraduationWay> extGradWays = extTcDP.getExtGraduationWays(extTC.getId());
        if(extGradWays != null) {
            for(ExtGraduationWay gw : extGradWays) {
                tcDP.addTrainingCourseGraduationWayRecord(
                        tcId, gw.getGraduationWayId(), gw.getNotes());
            }
        }
        
        List<ExtTrainingForm> extTrForms = extTcDP.getExtTrainingForms(extTC.getId());
        if(extTrForms != null) {
            for(ExtTrainingForm tf : extTrForms) {
                tcDP.setTrainingCourseTrainingForm(
                        tcId, tf.getTrainingFormId(), tf.getNotes());
            }
        }

        int homeCountry = extAppl.getHomeCountryId();
        String homeCity = extAppl.getHomeCity();
        String homePostCode = extAppl.getHomePostCode();
        String homeAddressDetails = extAppl.getHomeAddressDetails();
        boolean homeIsBg = extAppl.homeIsBg();
        String email = extPerson == null ? null : extPerson.getEmail();
        if (extAppl.getContactDetailsId() != null) {
            ExtAddress contactDetails = extAppl.getContactDetails();
            homeCountry = contactDetails.getCountryId();
            homeCity = contactDetails.getCityId() == null ? contactDetails.getForeignCity() : contactDetails.getCity().getName();
            homePostCode = contactDetails.getPostalCode();
            homeAddressDetails = contactDetails.getAddress();
            homeIsBg = contactDetails.getCountryId() == Country.COUNTRY_ID_BULGARIA;
            email = contactDetails.getEmail();
        }

        int applId = appDP.saveApplication(0,
                extAppl.getApplicationNumber(),
                extAppl.getApplicationDate(),
                applicantId,
                applicantCompanyId,
                representativeId, tcId,
                email, 1,
                homeCountry, homeCity,
                homePostCode,
                homeAddressDetails, homeIsBg,
                extAppl.getBgCity(), 
                extAppl.getBgPostCode(), extAppl.getBgAddressDetails(), 
                Application.BG_ADDRESS_OWNER_APPLICANT, 
                loggedUserId, 
                extAppl.getTimeOfCreation(), 
                ApplicationStatus.APPLICATION_IZCHAKVANE_STATUS_CODE, 
                null, 
                extAppl.differentApplicantAndDiplomaNames() ? 1 : 0, 
                extAppl.getHomePhone(), 
                extAppl.getBgPhone(), null, null, 
                null, null, 
                null, null, null, extAppl.isPersonalDataUsage(), extAppl.getDataAuthentic(), null, null, null, null, User.ADMIN_USER_ID, null, ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE,
                extAppl.getRepresentativeType(), extAppl.getTypePayment(), extAppl.getDeliveryType(), extAppl.getDeclaration(), extAppl.getCourierNameAddress(), extAppl.getOutgoingNumber(), extAppl.getInternalNumber(),
                extAppl.getIsExpress(), extAppl.getApplicantType(),
                extAppl.getApplicationType(),
                extAppl.getDocumentReceiveMethodId(),
                extAppl.getApplicantPersonalIdDocumentTypeId());

        List<ExtApplicationKind> kinds = extAppl.getApplicationKinds();
        for (ExtApplicationKind kind : kinds) {
            appDP.addApplicationKind(applId, kind.getEntryNumSeriesId(), kind.getPrice(), kind.getEntryNum(), null);
        }

        List<ExtPurposeOfRecognition> extPurpOfRecs = extTcDP.getExtPurposesOfRecognition(extApplicationId);
        if(extPurpOfRecs != null) {
            for(ExtPurposeOfRecognition pop : extPurpOfRecs) {
                appDP.addApplicationRecognitionPurposeRecord(
                        applId, pop.getPurposeOfRecognitionId(), pop.getNotes());
            }
        }

        ExtDocumentRecipient extDocumentRecipient = getDocumentRecipient(extApplicationId);
        if (extDocumentRecipient != null) {
            appDP.saveDocumentRecipient(applId, extDocumentRecipient.getName(), extDocumentRecipient.getCountryId(), extDocumentRecipient.getCity(), extDocumentRecipient.getDistrict(), extDocumentRecipient.getPostCode(), extDocumentRecipient.getAddress(), extDocumentRecipient.getMobilePhone());
        }

        extAttDP.copyRecordsToInternalDB(extApplicationId, applId);


        extApplDP.saveApplication(extAppl.getId(),
                extAppl.getApplicantId(),
                extAppl.getApplicantCompanyId(),
                extAppl.differentApplicantAndDiplomaNames(),
                extAppl.getTrainingCourseId(), 
                extAppl.getHomeCountryId(), extAppl.getHomeCity(), 
                extAppl.getHomePostCode(), extAppl.getHomeAddressDetails(), 
                extAppl.getHomePhone(), extAppl.homeIsBg(), 
                extAppl.getBgCity(), extAppl.getBgPostCode(), extAppl.getBgAddressDetails(), 
                extAppl.getBgPhone(), extAppl.getTimeOfCreation(), 
                extAppl.getSummary(), 
                ExtApplication.STATUS_TRANSFERED, applId, extAppl.isPersonalDataUsage(), extAppl.getDataAuthentic(),
                extAppl.getApplicantType(),
                extAppl.getDeputy(),
                extAppl.getRepresentativeId(),
                extAppl.getRepresentativeType(),
                extAppl.getContactDetailsId(),
                extAppl.getTypePayment(),
                extAppl.getDeliveryType(),
                extAppl.getDeclaration(),
                extAppl.getCourierNameAddress(),
                extAppl.getOutgoingNumber(),
                extAppl.getInternalNumber(),
                extAppl.getIsExpress(),
                extAppl.getDocFlowNumber(),
                extAppl.getApplicationType(),
                extAppl.getDocumentReceiveMethodId(),
                extAppl.getApplicantPersonalIdDocumentTypeId()
        );


        
        return applId;
    }

    @Override
    public void markApplicationFinished(int extApplicationId) {
        try {
            db.markApplicationFinished(extApplicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<ExtApplication> getApplicationsByRepresentative(int applicantId, Integer applicationType) {
        try {
            List<Integer> applicationTypes = applicationType == null ? null : NumgeneratorDataProvider.APPLICATION_TYPE_TO_ENTRYNUM_SERIES.get(applicationType);
            if (applicationType != null && applicationTypes == null) {
                throw new RuntimeException("Unknown applicationType:" + applicationType);
            }
            return generateApplications(db.getApplicationRecordsByRepresentativeId(applicantId, applicationTypes));
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    
    
    private List<ExtApplication> generateApplications(List<ExtApplicationRecord> records) {
    	if (records.size() == 0) {
    		return null;
    	}
        return records.stream().map(rec -> new ExtApplicationImpl(nacidDataProvider, rec)).collect(Collectors.toList());
    }
   
    public int saveApplication(int id, Integer applicantId, Integer applicantCompanyId, boolean differentDiplomaNames,
			int trainingCourseId, int homeCountryId, String homeCity, String homePostCode, String homeAddressDetails, String homePhone, boolean homeIsBg,
			String bgCity, String bgPostCode, String bgAddressDetails, String bgPhone, Date timeOfCreation, String summary, int applicationStatus, Integer internalApplicationId,
			Boolean personalDataUsage, Boolean dataAuthentic, int applicantType, Boolean deputy, Integer representativeId, String representativeType, Integer contactDetailsId, Short typePayment, Integer deliveryType, Boolean declaration, String courierNameAddress, String outgoingNumber, String internalNumber, Boolean isExpress,
            String entryNum, int applicationType, Integer documentReceiveMethodId, Integer applicantPersonalIdDocumentType) {
    	ExtApplicationRecord record = new ExtApplicationRecord(id, applicantId, applicantCompanyId, DataConverter.parseBooleanToInteger(differentDiplomaNames), trainingCourseId, homeCountryId, homeCity, homePostCode, homeAddressDetails, homePhone, homeIsBg ? 1 : 0, bgCity, bgPostCode, bgAddressDetails, bgPhone, timeOfCreation == null ? null : new Timestamp(timeOfCreation.getTime()), summary, applicationStatus, internalApplicationId, DataConverter.parseBooleanToInteger(personalDataUsage), DataConverter.parseBooleanToInteger(dataAuthentic), applicantType,
                 deputy, representativeId, representativeType, contactDetailsId, typePayment, deliveryType, declaration, courierNameAddress, outgoingNumber, internalNumber, isExpress, entryNum, documentReceiveMethodId, applicantPersonalIdDocumentType);
        try {
            if (id == 0) {
                record = db.insertRecord(record);
                List<Integer> entryNumSeries = NumgeneratorDataProvider.APPLICATION_TYPE_TO_ENTRYNUM_SERIES.get(applicationType);
                if (entryNumSeries == null || entryNumSeries.size() != 1) {
                    throw new RuntimeException("Cannot determine applicationKind from applicationType: " + applicantType + " EntryNumSeries: " + entryNumSeries);//tyj kato za da se dobavi kolona applicationType v eservices.rudi_application, trqbva da se razbutva i prilojenieto na Venci, ne pravq takava kolona, a po applicationType-a opredelqm entryNumSeriesId. V sluchaq za RUDI/Doktorati ima konversiq 1 kym 1 mejdu applicationType i entryNumSeries, tam nqma problemi. A i v ext* prilojenieto se raboti samo s teq 2 vida applications, ne bi trqbvalo da ima problemi...
                }
                ExtApplicationKindRecord applicationKindRecord = new ExtApplicationKindRecord(0, record.getId(), entryNumSeries.get(0));
                db.insertRecord(applicationKindRecord);
            } else {
                db.updateRecord(record);
            }
            if (applicationStatus == ExtApplication.STATUS_NOT_EDITABLE) {
            	db.updateApplicationXml(record.getId(), getApplicationXml(record.getId()));
            }
            return record.getId();
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public List<ExtApplicationKind> getApplicationKindsPerApplication(int applicationId) {
        try {
            List<ExtApplicationKindRecord> recs = db.getApplicationKindsPerApplication(applicationId);
            if (recs.size() == 0) {
                return null;
            }
            return recs.stream().map(rec -> new ExtApplicationKindImpl(nacidDataProvider, rec)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public String getApplicationXml(int applicationId) {
    	try {
    		ExtApplicationsXml result = generateExternalApplicationXml(applicationId);
        	StringWriter stringWriter = new StringWriter();
    		JAXBContext jc = JAXBContext.newInstance( "com.nacid.data.external.applications.xml" );
    		Marshaller m = jc.createMarshaller();
    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    		m.marshal( result, stringWriter );
    		return stringWriter.toString();	
    	} catch (Exception e) {
			throw Utils.logException(e);
		}
    	
    }
    public void saveSignedApplicationXml(int userId, int applicationId,  String signedXmlContent) throws SignedXmlException {
    	if (!isExternalApplicationSignedXmlCorrect(applicationId, signedXmlContent)) {
    		throw new SignedXmlException("Има разлика между подписания xml и данните в базата!");
    	}
    	//TODO:Ne znam kakvo da pravq ako ima problemi s elektronnoto podpisvane - zasega hvyrlqm exception
    	ExtESignedInformationRecord record = new ExtESignedInformationRecord(0,userId, applicationId, signedXmlContent);
    	try {
    		UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
    		try {
				SignatureUtils.configure(utilsDataProvider.getCommonVariableValue("pathToTrustedIssuersFile"), utilsDataProvider.getCommonVariableValue("trustedIssuersFilePass"));
			} catch (Exception e) {
				throw Utils.logException(new Exception("Системата не може да прочете файла с издателите на сертификати ", e));
			}
			
			try {
				X509Certificate cert = SignatureUtils.verifyXMLSignature(new ByteArrayInputStream(signedXmlContent.getBytes()), null);
				record.setIssuer(SignatureUtils.getIssuerName(cert));
				record.setName(SignatureUtils.getCommonName(cert));
				record.setEmail(SignatureUtils.getEmail(cert));
				record.setCivilId(SignatureUtils.getEgn(cert));
				record.setUnifiedIdCode(SignatureUtils.getBulstat(cert));
				Date validityFrom = cert.getNotBefore();
				record.setValidityFrom(validityFrom == null ? null : new Timestamp(validityFrom.getTime()));
				Date validityTo = cert.getNotAfter();
				record.setValidityTo(validityTo == null ? null : new Timestamp(validityTo.getTime()));
			} catch (Exception e) {
				throw new SignedXmlException("Проблем при опит за прочитане на електронно подписаните данни!", e);
			}
    		db.insertRecord(record);
		} catch (SQLException e) {
			System.out.println();
			throw Utils.logException(e);
		} 
    }
    /**
     * tozi method se polzva v ExtApplicationImpl!
     */
    public ExtESignedInformation getESignedInformation(int applicationId) {
    	try {
			ExtESignedInformationRecord record = db.getESignedInformationRecord(applicationId);
			return record == null ? null : new ExtESignedInformationImpl(record);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    	
    }
    public boolean isESigned(int applicationId) {
    	return getESignedInformation(applicationId) == null ? false : true;
    }
    
    private ExtApplicationsXml generateExternalApplicationXml(int applicationId) {
    	try {
    		
    		//adding application record
    		ExtApplicationRecord applicationRecord = db.selectRecord(new ExtApplicationRecord(), applicationId);
    		if (applicationRecord == null) {
    			return null;
    		}
    		ExtApplicationsXml result = new ObjectFactory().createExtApplicationsXml();
    		result.setExtApplicationRecord(applicationRecord);
    		//end of adding application records
    		
    		//adding training course records
    		ExtTrainingCourseDataProviderImpl extTrainingCourseDataProviderImpl = nacidDataProvider.getExtTrainingCourseDataProvider();
    		ExtTrainingCourseRecord trainingCourseRecord = extTrainingCourseDataProviderImpl.getExtTrainingCourseRecord(applicationRecord.getTrainingCourseId());
    		int trainingCourseId = trainingCourseRecord.getId();
    		result.setExtTrainingCourseRecord(trainingCourseRecord);
    		//end of adding training course records 
    		
    		//adding diploma issuer records
    		List<ExtDiplomaIssuerRecord> issuers = extTrainingCourseDataProviderImpl.getDiplomaIssuerRecords(trainingCourseId);
    		if (issuers.size() > 0) {
    			result.getExtDiplomaIssuerRecord().addAll(issuers);
    		}
    		//end of adding diploma issuer records
    		
    		
    		//adding training location records
    		List<ExtTrainingCourseTrainingLocationRecord> extTrainingLocationRecords = extTrainingCourseDataProviderImpl.getTrainingCourseTrainingLocationRecords(trainingCourseId);
    		if (extTrainingLocationRecords.size() > 0) {
    			result.getExtTrainingCourseTrainingLocationRecord().addAll(extTrainingLocationRecords);
    		}
    		
    		//adding person record
    		result.setExtPersonRecord(nacidDataProvider.getExtPersonDataProvider().getDb().getExtPerson(applicationRecord.getApplicantId()));
    		//end of adding person record
    		
    		//adding attached docs
    		List<ExtApplicationAttachmentRecord> attachmentRecords = nacidDataProvider.getExtApplicationAttachmentDataProvider().getExtAttachmentRecordsForXml(applicationRecord.getId());
    		if (attachmentRecords.size() > 0) {
    			result.getExtApplicationAttachmentRecord().addAll(attachmentRecords);	
    		}
    		//end of adding attached docs
    		
    		
    		//adding purpose of recognition records
    		List<ExtPurposeOfRecognitionRecord> extPurposeOfRecognitionRecords = extTrainingCourseDataProviderImpl.getExtPurposesOfRecognitionRecords(applicationId);
    		if (extPurposeOfRecognitionRecords.size() > 0) {
    			result.getExtPurposeOfRecognitionRecord().addAll(extPurposeOfRecognitionRecords);
    		}
    		//end of adding purpose of recognition records
    		
    		
    		//adding of graduation way records
    		List<ExtGraduationWayRecord> graduationWayRecords = extTrainingCourseDataProviderImpl.getExtGraduationWayRecords(trainingCourseId);
    		if (graduationWayRecords.size() > 0) {
    			result.getExtGraduationWayRecord().addAll(graduationWayRecords);
    		}
    		//end of adding graduation way records
    		
    		//adding training form records
    		List<ExtTrainingFormRecord> trainingFormRecords = extTrainingCourseDataProviderImpl.getExtTrainingFormRecords(trainingCourseId);
    		if (trainingFormRecords.size() > 0) {
    			result.getExtTrainingFormRecord().addAll(trainingFormRecords);
    		}
    		//end of adding training form records
    		return result;
    		
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    	
    }
    
    private boolean isExternalApplicationSignedXmlCorrect(int applicationId, String signedXml) {
    	try {
    		JAXBContext jc = JAXBContext.newInstance( "com.nacid.data.external.applications.xml" );
    		
    		Unmarshaller u = jc.createUnmarshaller();
    		ExtApplicationsXml signedXmlData = (ExtApplicationsXml) u.unmarshal(new ByteArrayInputStream(signedXml.getBytes()));
    		
    		
    		/**
    		 * ot dannite v bazata generiram XML, ot kojto pak generiram ExtApplicationsXml, tyi kato zabelqzoh edin sy6testvan problem
    		 * v bazata timeOfCreation e 2010-09-14 14:16:07.397, a podpisaniq xml ima timeOfCreation=2010-09-14 14:16:07.000, tyi kato sa otrqzani milisekundite mu
    		 * 
    		 */
    		
    		String dataBaseStringXml = getApplicationXml(applicationId);
    		ExtApplicationsXml databaseXmlData = (ExtApplicationsXml) u.unmarshal(new ByteArrayInputStream(dataBaseStringXml.getBytes()));
    		
    		
    		System.out.println("ExtApplicationRecord:" + signedXmlData.getExtApplicationRecord().equals(databaseXmlData.getExtApplicationRecord()));
    		System.out.println("ExtPersonRecord:" + signedXmlData.getExtPersonRecord().equals(databaseXmlData.getExtPersonRecord()));
    		System.out.println("ExtTrainingCourseRecord:" + signedXmlData.getExtTrainingCourseRecord().equals(databaseXmlData.getExtTrainingCourseRecord()));
    		System.out.println("getExtApplicationAttachmentRecord:" + signedXmlData.getExtApplicationAttachmentRecord().equals(databaseXmlData.getExtApplicationAttachmentRecord()));
    		System.out.println("getExtDiplomaIssuerRecord:" + signedXmlData.getExtDiplomaIssuerRecord().equals(databaseXmlData.getExtDiplomaIssuerRecord()));
    		System.out.println("getExtGraduationWayRecord:" + signedXmlData.getExtGraduationWayRecord().equals(databaseXmlData.getExtGraduationWayRecord()));
    		
    		System.out.println("getExtPurposeOfRecognitionRecord:" + signedXmlData.getExtPurposeOfRecognitionRecord().equals(databaseXmlData.getExtPurposeOfRecognitionRecord()));
    		System.out.println("getExtTrainingCourseTrainingLocationRecord:" + signedXmlData.getExtTrainingCourseTrainingLocationRecord().equals(databaseXmlData.getExtTrainingCourseTrainingLocationRecord()));
    		System.out.println("getExtTrainingFormRecord:" + signedXmlData.getExtTrainingFormRecord().equals(databaseXmlData.getExtTrainingFormRecord()));
    		
    		return signedXmlData != null && databaseXmlData != null && signedXmlData.equals(databaseXmlData);
 		} catch (JAXBException e) {
			return false;
 			//throw Utils.logException(e);
		}
    	
    }
    


    @Override
    public List<ExtApplication> getApplicationsByStatus(List<Integer> statuses) {
        try {
            return generateApplications(db.getApplicationsByStatus(statuses));
        }
        catch(SQLException e) {
            throw Utils.logException(e);
        }
    }


	@Override
	public ExtApplication getApplicationByInternalApplicationId(int internalApplicationId) {
		try {
			List<ExtApplicationRecord> records = db.getApplicationRecordsByInternalApplicationId(internalApplicationId);
			if (records.size() == 0) {
				return null;
			}
			return new ExtApplicationImpl(nacidDataProvider, records.get(0));
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

    @Override
    public List<EApplyApplication> getEAppliedApplications(List<Integer> numSeriesIds) {

        try {
            List<EApplyApplicationRecord> records = db.getEapplyApplicationRecords(numSeriesIds);
            return records.size() == 0 ? null : records.stream().map(r -> r).collect(Collectors.toList());

        } catch (SQLException e) {
            throw Utils.logException(e);
        }


    }

    public ExtAddress getContactDetails(int contactDetailsId) {
        try {
            ExtAddressRecord rec = db.selectRecord(ExtAddressRecord.class, "id = ?", contactDetailsId);
            return rec == null ? null : new ExtAddressImpl(nacidDataProvider, rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public ExtDocumentRecipient getDocumentRecipient(int applicationId) {
        try {
            ExtDocumentRecipientRecord res = db.getDocumentRecipient(applicationId);
            return res == null ? null : new ExtDocumentRecipientImpl(res, nacidDataProvider.getNomenclaturesDataProvider());
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

    @Override
    public List<ExtApplicationCommentExtended> getApplicationComments(int applicationId) {
        try {
            return db.getApplicationCommentExtendedRecords(applicationId).stream().map(r -> (ExtApplicationCommentExtended)r).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void saveApplicationComment(int applicationId, String comment, boolean sendEmail, Integer emailId, boolean systemMessage, int userCreated) {
        try {
            ExtApplicationCommentRecord rec = new ExtApplicationCommentRecord(0, applicationId, comment, sendEmail ? 1 : 0, emailId, systemMessage ? 1 : 0, new Timestamp(new Date().getTime()), userCreated);
            db.insertRecord(rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public ExtApplicationCommentExtended getApplicationComment(int id) {
        try {
            return db.getApplicationCommentExtendedRecord(id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public static void main(String[] args) {
    	ExtApplicationsDataProviderImpl applicationsDataProvider = new ExtApplicationsDataProviderImpl(new NacidDataProviderImpl(new StandAloneDataSource()));
    	String applicationXml = applicationsDataProvider.getApplicationXml(18);
    	System.out.println(applicationXml + "\n\n\n");
    }
}
