package com.nacid.bl.impl.regprof.external.applications;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.docflow.DocFlowException;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.applications.SignedXmlException;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.*;
import com.nacid.bl.impl.external.ExtPersonDataProviderImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
import com.nacid.bl.payments.Liability;
import com.nacid.bl.regprof.external.*;
import com.nacid.bl.signature.CertificateInfo;
import com.nacid.bl.signature.SuccessSign;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.applications.PersonRecord;
import com.nacid.data.common.StringValue;
import com.nacid.data.external.applications.ExtApplicationAttachmentRecord;
import com.nacid.data.external.applications.ExtPersonDocumentRecord;
import com.nacid.data.external.applications.ExtPersonRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceDatesRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;
import com.nacid.data.regprof.applications.RegprofDocumentRecipientRecord;
import com.nacid.data.regprof.external.*;
import com.nacid.data.regprof.external.applications.ExtRegprofApplicationCommentRecord;
import com.nacid.data.regprof.external.applications.ExtRegprofApplicationForListRecord;
import com.nacid.data.regprof.external.applications.ExtRegprofDocumentRecipientRecord;
import com.nacid.data.regprof.external.applications.ExtRegprofESignedInformationRecord;
import com.nacid.data.regprof.external.applications.xml.ExtRegprofApplicationsXml;
import com.nacid.db.external.ExtPersonDB;
import com.nacid.db.regprof.external.ExtRegprofApplicationsDB;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.web.exceptions.UnknownRecordException;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExtRegprofApplicationsDataProviderImpl implements ExtRegprofApplicationsDataProvider {
    private NacidDataProviderImpl nacidDataProvider;
    private ExtRegprofApplicationsDB db;

    public ExtRegprofApplicationsDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new ExtRegprofApplicationsDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    public ExtRegprofApplicationImpl createEmtpyRegprofApplication(int personId) {
        try {
            ExtRegprofApplicationImpl result = new ExtRegprofApplicationImpl();
            //result.setApplicant(db.selectRecord(new ExtPersonRecord(), personId));
            List<ExtPersonDocumentRecord> documentRecords = db.selectRecords(ExtPersonDocumentRecord.class, "person_id = ? and active = ?", personId, 1);
            if (documentRecords.size() > 0) {
                result.setApplicantDocuments(documentRecords.get(0));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    public ExtRegprofApplicationImpl getExtRegprofApplication(int applicationId) {
        try {
            List<ExtRegprofApplicationDetailsImpl> details = db.selectRecords(ExtRegprofApplicationDetailsImpl.class, "id = ? ", applicationId);
            if (details.size() == 0) {
                return null;
            }
            return fillExtRegprofApplication(details.get(0));
        } catch (SQLException e) {
            throw Utils.logException(e);   
        }
    }
    @Override
    public ExtRegprofApplicationImpl getExtRegprofApplicationByInternalApplicationId(int internalApplicationId) {
        try {
            ExtRegprofApplicationDetailsImpl details = db.getExtRegprofApplicationByInternalApplicationId(internalApplicationId);
            if (details == null) {
                return null;
            }
            return fillExtRegprofApplication(details);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    private ExtRegprofApplicationImpl fillExtRegprofApplication(ExtRegprofApplicationDetailsImpl details) {
        try {
            ExtRegprofApplicationImpl result = new ExtRegprofApplicationImpl();
            result.setApplicationDetails(details);

            //result.setApplicant(db.selectRecord(new ExtPersonRecord(), extPersonId));
            if (result.getApplicationDetails().getApplicantDocumentsId() != null) {
                result.setApplicantDocuments(db.selectRecord(new ExtPersonDocumentRecord(), result.getApplicationDetails().getApplicantDocumentsId()));    
            }
            result.setApplicant(db.selectRecord(new ExtPersonRecord(), details.getApplicantId()));
            result.setTrainingCourseDetails(db.selectRecord(new ExtRegprofTrainingCourseRecord(), result.getApplicationDetails().getTrainingCourseId()));
            result.setDocumentRecipient(db.getDocumentRecipient(result.getApplicationDetails().getId()));
            return result;    
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    @Override
    public List<ExtRegprofApplicationForList> getExtRegprofApplicationByExtPerson(int extPerson) {
        try {
            List<ExtRegprofApplicationForListRecord> records = db.getExtRegprofApplicationsByExtPerson(extPerson);
            if (records.size() == 0) {
                return null;
            }
            List<ExtRegprofApplicationForList> result = new ArrayList<ExtRegprofApplicationForList>();
            result.addAll(records);
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    public List<ExtRegprofApplicationForList> getExtRegprofApplicationsByStatuses(List<Integer> statuses) {
        try {
            List<ExtRegprofApplicationForListRecord> records = db.getExtRegprofApplicationsByStatuses(statuses);
            if (records.size() == 0) {
                return null;
            }
            List<ExtRegprofApplicationForList> result = new ArrayList<ExtRegprofApplicationForList>();
            result.addAll(records);
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    @Override
    public void saveExtRegprofApplication(ExtRegprofApplicationImpl app) {
        try {

            saveAndSetApplicationApplicant(app);

            ExtRegprofApplicationDetailsImpl appDetails = app.getApplicationDetails();
            if (appDetails.getId() == null) { //new record!
                app.setTrainingCourseDetails(db.insertRecord(app.getTrainingCourseDetails()));
                appDetails.setStatus(ExtRegprofApplicationImpl.STATUS_EDITABLE);
                //setting foreign keys...
                appDetails.setTrainingCourseId(app.getTrainingCourseDetails().getId());
                saveApplicantDocumentsIfAny(app);
                app.setApplicationDetails(db.insertRecord(appDetails));
             
            } else {
                ExtRegprofApplicationDetailsImpl oldAppDetails = db.selectRecord(new ExtRegprofApplicationDetailsImpl(), appDetails.getId());
                if (oldAppDetails.getTrainingCourseId().intValue() != appDetails.getTrainingCourseId() || oldAppDetails.getTrainingCourseId().intValue() != app.getTrainingCourseDetails().getId()) {
                    throw new RuntimeException ("Someone has changed training course's ID....");
                }
                
                db.updateBaseTrainingCourseDetails(app.getTrainingCourseDetails());
                saveApplicantDocumentsIfAny(app);

                db.updateBaseApplicationDetails(appDetails);
                app.setApplicationDetails(getApplicationDetails(appDetails.getId()));
            }

            DocumentReceiveMethod documentReceiveMethod = app.getApplicationDetails().getDocumentReceiveMethodId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(app.getApplicationDetails().getDocumentReceiveMethodId());
            if (documentReceiveMethod == null || !documentReceiveMethod.hasDocumentRecipient()) {
                db.deleteDocumentRecipient(app.getApplicationDetails().getId());
                app.setDocumentRecipient(null);
            } else if (documentReceiveMethod != null){
                app.getDocumentRecipient().setApplicationId(app.getApplicationDetails().getId());
                db.saveDocumentRecipient(app.getDocumentRecipient());
            }

        } catch (SQLException e) {
            throw Utils.logException(e);
        }   
    }
    private void saveApplicantDocumentsIfAny(ExtRegprofApplicationImpl app) {
        if (app.getApplicantDocuments().getId() == null) { //must insert applicantDocuments and setApplicantDocument's id to applicationDetails.applicantDocumentsId...
            if ((!StringUtils.isEmpty(app.getApplicantDocuments().getIssuedBy()) && !StringUtils.isEmpty(app.getApplicantDocuments().getNumber()) && app.getApplicantDocuments().getDateOfIssue() != null)) {
                ExtRegprofApplicationDetailsImpl appDetails = app.getApplicationDetails();
                app.getApplicantDocuments().setPersonId(appDetails.getApplicantId());
                app.setApplicantDocuments(nacidDataProvider.getExtPersonDataProvider().insertNewExtPersonDocument(app.getApplicantDocuments()));
                appDetails.setApplicantDocumentsId(app.getApplicantDocuments().getId());
            }
        }
    }

    private void saveAndSetApplicationApplicant(ExtRegprofApplicationImpl app) throws SQLException {
        if (app.getDifferentApplicantRepresentative() == 1) {
            ExtPersonRecord applicantRecord = app.getApplicant();
            ExtPersonDB epdb = nacidDataProvider.getExtPersonDataProvider().getDb();
            ExtPersonRecord dbPerson = null;
            if (applicantRecord.getId() != 0) {
                dbPerson = epdb.getExtPerson(applicantRecord.getId());
            } else if (applicantRecord.getId() == 0 && !StringUtils.isEmpty(applicantRecord.getCivilId()) && applicantRecord.getCivilIdType() != null) {
                dbPerson = epdb.getExtPerson(applicantRecord.getCivilIdType(), applicantRecord.getCivilId());
            }

            ExtPersonRecord applicant = new ExtPersonRecord(dbPerson == null ? 0 : dbPerson.getId(), applicantRecord.getFname(), applicantRecord.getSname(), applicantRecord.getLname(), applicantRecord.getCivilId(), applicantRecord.getCivilIdType(), applicantRecord.getBirthCountryId(),  applicantRecord.getBirthCity(),
                    applicantRecord.getBirthDate(), applicantRecord.getCitizenshipId(), applicantRecord.getEmail(), null, dbPerson == null ? null : dbPerson.getUserId());
            int applicantId = epdb.persistPerson(applicant);
            app.getApplicationDetails().setApplicantId(applicantId);
        } else {
            app.getApplicationDetails().setApplicantId(app.getApplicationDetails().getRepresentativeId());
        }
    }
    public void saveExtRegprofTrainingCourse(ExtRegprofTrainingCourse tc) {
        try {
            ExtRegprofTrainingCourseRecord details = tc.getDetails();
            ExtRegprofProfessionExperienceRecord experienceRecord = tc.getExperienceRecord();

            if (experienceRecord != null) {
                RegprofTrainingCourseDataProviderImpl.updateDocumentExperienceDates(experienceRecord);
            }
            
            //malko obrabotka na dannnite. Chudih se dali tuk j e mqstoto ili v handler-a....
            if (details.getProfInstitutionId() != null) {
                details.setProfInstitutionNameTxt(null);
            }
            if (details.getProfInstitutionOrgNameId() != null) {
                details.setProfInstitutionNameTxt(null);
            }
            if (details.getHighProfQualificationId() != null) {
                details.setHighProfQualificationTxt(null);
            }
            if (details.getSdkProfInstitutionId() != null) {
                details.setSdkProfInstitutionNameTxt(null);
            }
            if (details.getSdkProfInstitutionOrgNameId() != null) {
                details.setSdkProfInstitutionOrgNameTxt(null);
            }
            if (details.getSdkProfQualificationId() != null) {
                details.setSdkProfQualificationTxt(null);
            }
            if(details.getSecProfQualificationId() != null){
                details.setSecProfQualificationTxt(null);
            }
            if (details.getCertificateProfQualificationId() != null) {
                details.setCertificateProfQualificationTxt(null);
            } else if (details.getCertificateProfQualificationTxt() != null){
                //ako ima certificateProfQualificationTxt, proverqva dali v bazata ima kvalifikaciq s takova ime (tyj kato tova pole se popylva v zavisimost ot drugi nqkolko poleta i v obshtiq sluchaj nqma da ima set-nato certQualId). Ako ima, togava settva certQualId!
                String certQualName = details.getCertificateProfQualificationTxt();
                certQualName = certQualName.trim();
                List<FlatNomenclature> noms = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, certQualName, null, null);
                if (noms != null && noms.size() != 0) {
                    for (FlatNomenclature nom:noms) {
                        if (nom.getName().equalsIgnoreCase(certQualName)) {
                            details.setCertificateProfQualificationId(nom.getId());
                            details.setCertificateProfQualificationTxt(null);
                            break;
                        }
                    }
                }
            }
            if (experienceRecord != null && experienceRecord.getNomenclatureProfessionExperienceId() != null) {
                experienceRecord.setProfessionExperienceTxt(null);
            }
            
            //saving details
            ExtRegprofTrainingCourseRecord old = db.selectRecord(new ExtRegprofTrainingCourseRecord(), details.getId());
            //adding some of the fields which are not in the second tab...
            details.setDocumentCivilId(old.getDocumentCivilId());
            details.setDocumentCivilIdType(old.getDocumentCivilIdType());
            details.setDocumentFname(old.getDocumentFname());
            details.setDocumentSname(old.getDocumentSname());
            details.setDocumentLname(old.getDocumentLname());
            db.updateRecord(details);
            //end of saving details...
            
            //saving specialities
            List<ExtRegprofTrainingCourseSpecialitiesRecord> specialities = tc.getSpecialities();
            List<ExtRegprofTrainingCourseSpecialitiesRecord> newSpecs = null;
            db.deleteSpecialitiesByTrainingCourse(details.getId());
            if (specialities != null && specialities.size() > 0) {
                newSpecs = new ArrayList<ExtRegprofTrainingCourseSpecialitiesRecord>();
                for (ExtRegprofTrainingCourseSpecialitiesRecord rec:specialities) {
                    if (rec.getId() != null) {
                        rec.setTrainingCourseId(details.getId());
                        newSpecs.add(db.insertRecord(rec));
                    }
                }
            }
            tc.setSpecialities(newSpecs);
            //end of saving specialities

            //saving experience record
            if (details.getHasExperience() == 0) {
                db.deteleProfessionalExperienceRecord(details.getId());
            } else if (experienceRecord.getId() == null || experienceRecord.getId() == 0){
                experienceRecord.setTrainingCourseId(details.getId());
                experienceRecord = db.insertRecord(experienceRecord);
                int id = experienceRecord.getId();
                if (id > 0) {
                    if (experienceRecord.getProfessionExperienceDocuments() != null) {
                        for (ExtRegprofProfessionExperienceDocumentRecord rec:experienceRecord.getProfessionExperienceDocuments()) {
                            if (rec.getId() == null) {//empty row!
                                continue;
                            }
                            rec.setProfExperienceId(id);
                            rec = db.insertRecord(rec);
                            if (rec.getDates() != null) {
                                for (ExtRegprofProfessionExperienceDatesRecord r:rec.getDates()) {
                                    if (r.getId() == null) { //empty row
                                        continue;
                                    }
                                    r.setProfExperienceDocumentId(rec.getId());
                                    db.insertRecord(r);
                                }
                            }
                        }
                    }
                }
            } else {
                experienceRecord.setTrainingCourseId(details.getId());
                db.updateRecord(experienceRecord);
                int id = experienceRecord.getId();
                
                List<Integer> documentRecordsNotToDelete = new ArrayList<Integer>();
                if (experienceRecord.getProfessionExperienceDocuments() != null) {
                    for (ExtRegprofProfessionExperienceDocumentRecord rec:experienceRecord.getProfessionExperienceDocuments()) {
                        if (rec.getId() == null) { //empty row!
                            continue;
                        }
                        int docId;
                        rec.setProfExperienceId(experienceRecord.getId());
                        if (rec.getId() == 0) {
                            ExtRegprofProfessionExperienceDocumentRecord newRec = db.insertRecord(rec);
                            docId = newRec.getId();
                        } else {
                            db.updateRecord(rec);
                            docId = rec.getId();
                        }
                        documentRecordsNotToDelete.add(docId);
                        db.deleteProfessionExperienceDatesRecords(docId, null);
                        if (rec.getDates() != null) {
                            for (ExtRegprofProfessionExperienceDatesRecord r:rec.getDates()) {
                                if (r.getId() == null) { //empty row!
                                    continue;
                                }
                                r.setProfExperienceDocumentId(docId);
                                db.insertRecord(r);
                            }
                        }
                    }
                    if (documentRecordsNotToDelete.size() > 0) {
                        db.deleteProfessionExperienceDocumentRecords(id, documentRecordsNotToDelete);
                    }
                } else {
                    db.deleteAllProfessionExperienceDocumentRecords(experienceRecord.getId());
                }
            }
            db.updateApplicationCountryAndApostille(tc.getDetails().getId(), tc.getApplicationCountryId(), tc.getApostilleApplication());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
        
        
    }
    public ExtRegprofTrainingCourseImpl getTrainingCourse(int applicationId) {
        try {
            ExtRegprofTrainingCourseImpl result = new ExtRegprofTrainingCourseImpl();
            result.setDetails(db.getExtRegprofTrainingCourseRecord(applicationId));
            //adding experience Records
            ExtRegprofProfessionExperienceRecord experienceRecord = db.getExtRegprofExperienceRecordByTrainingCourse(result.getDetails().getId());
            if (experienceRecord != null) {
                experienceRecord.setProfessionExperienceDocuments(db.getRegprofProfessionExperienceDocumentRecordsByExperienceId(experienceRecord.getId()));
                result.setExperienceRecord(experienceRecord);    
            }
            ExtRegprofApplicationDetailsImpl applicationDetails = db.selectRecord(ExtRegprofApplicationDetailsImpl.class, "id = ? ", applicationId);
            result.setApplicationCountryId(applicationDetails.getApplicationCountryId());
            result.setApostilleApplication(applicationDetails.getApostilleApplication());

            result.setSpecialities(db.getSpeicialitiesByTrainingCourse(result.getDetails().getId()));
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
        
    }
    public void checkApplicationAccess(int applicationId, int userId, int operationType) throws NotAuthorizedException {
         try {
             Integer applicationStatus = db.getApplicationStatus(applicationId, userId);
             if (applicationStatus == null) {
                 throw new NotAuthorizedException("User has no rights to access given application");
             }
             if (!ExtRegprofUserAccessUtils.ACTION_TO_STATUS.get(operationType).contains(applicationStatus)) {
                 throw new NotAuthorizedException("User cannot perform this operation over the given applicationId");
             }
             
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public ExtRegprofApplicationDetailsImpl getApplicationDetails(int applicationId) {
        try {
            return db.selectRecord(new ExtRegprofApplicationDetailsImpl(), applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public String getExtRegprofApplicationXml(int applicationId) {

        try {
            ExtRegprofApplicationsXml result = generateExtRegprofApplicationsXml(applicationId);
            StringWriter stringWriter = new StringWriter();
            JAXBContext jc = JAXBContext.newInstance("com.nacid.data.regprof.external.applications.xml");
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal( result, stringWriter );
            return stringWriter.toString();     
        } catch (Exception e) {
            throw Utils.logException(e);
        }
        
        
    }
    private ExtRegprofApplicationsXml generateExtRegprofApplicationsXml(int applicationId) throws SQLException {
        ExtRegprofApplicationsXml result = new ExtRegprofApplicationsXml();
        ExtRegprofApplicationImpl app = getExtRegprofApplication(applicationId);
        
        //app.getApplicantDocuments()
        ExtRegprofTrainingCourseImpl tc = getTrainingCourse(applicationId);
        result.setExtRegprofApplicationRecord(app.getApplicationDetails());
        result.setExtRegprofTrainingCourseRecord(tc.getDetails());
        result.setExtRegprofProfessionExperienceRecord(tc.getExperienceRecord());
        result.setExtPersonDocumentRecord(app.getApplicantDocuments());
        result.setTrainingCourseSpecialities(tc.getSpecialities());
        result.setPerson(db.selectRecord(ExtPersonRecord.class, " id = ? ", app.getApplicationDetails().getApplicantId()));
        
        
        List<ExtApplicationAttachmentRecord> attachmentRecords = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider().getExtAttachmentRecordsForXml(app.getApplicationDetails().getId());
        if (attachmentRecords.size() > 0) {
            result.setAttachments(new ArrayList<ExtApplicationAttachmentRecord>());
            result.getAttachments().addAll(attachmentRecords);   
        }
        
        return result;
    }
    public void saveSignedApplicationXml(int userId, int applicationId, SuccessSign successSign) throws SignedXmlException {
        String signedXmlContent = successSign.getXmlSigned();
        String unsidnedContent = getExtRegprofApplicationXml(applicationId);
        boolean notEqual; 
        try {
            notEqual = !compareSignedUnsignedXml(signedXmlContent, unsidnedContent);
        } catch (Exception e) {
            throw new SignedXmlException(e);
        }
        
        if (notEqual) {
            throw new SignedXmlException("Има разлика между подписания и неподписания xml!");   
        }
        
        
        ExtRegprofESignedInformationRecord record = new ExtRegprofESignedInformationRecord(0,userId, applicationId, signedXmlContent);
        try {

            try {
                CertificateInfo certInfo = successSign.getCertificateInfo();
                record.setIssuer(certInfo.getIssuer());
                record.setName(certInfo.getName());
                record.setEmail(certInfo.getEmail());
                record.setCivilId(certInfo.getCivilId());
                Date validityFrom = certInfo.getValidFrom();
                record.setValidityFrom(validityFrom == null ? null : new Timestamp(validityFrom.getTime()));
                Date validityTo = certInfo.getValidTo();
                record.setValidityTo(validityTo == null ? null : new Timestamp(validityTo.getTime()));
            } catch (Exception e) {
                throw new SignedXmlException("Проблем при опит за прочитане на електронно подписаните данни!", e);
            }
            db.updateApplicationXml(applicationId, unsidnedContent);
            db.insertRecord(record);
        } catch (SQLException e) {
            System.out.println(e);
            throw Utils.logException(e);
        } 
        
        
        
        
    }
    public ExtRegprofESignedInformation getEsignedInformation(int applicationId) {
        try {
            ExtRegprofESignedInformationRecord rec = db.selectRecord(ExtRegprofESignedInformationRecord.class, "ext_app_id = ?", applicationId);
            return rec == null ? null : new ExtRegprofESignedInformationImpl(rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    @Override
    public void submitExtRegprofApplication(int applicationId, Date dateUpdated, Integer applicationStatus, Integer serviceTypeId, Integer paymentType) {
            try {
                ServiceType st = nacidDataProvider.getNomenclaturesDataProvider().getServiceType(serviceTypeId);
                Liability l = nacidDataProvider.getPaymentsDataProvider().saveLiability(0, st.getServicePrice(), Liability.LIABILITY_NOT_PAID, new Date(), null, Liability.CURRENCY_BGN, paymentType);
                db.linkLiabilityToExternalApplication(applicationId, l.getId());
                db.updateApplicationStatusOnApplicationApplying(applicationId, applicationStatus, new java.sql.Timestamp(dateUpdated.getTime()), serviceTypeId);
            } catch (SQLException e) {
                throw Utils.logException(e);
            }
        
    }
    
    public synchronized RegprofApplication transferApplicationToIntDb(int extApplicationId, int userId, int applicantId, Integer representativeId, Integer personDocumentId) throws DocFlowException {
        try {
            RegprofApplicationDataProviderImpl appDP = nacidDataProvider.getRegprofApplicationDataProvider();
            ExtRegprofApplicationAttachmentDataProviderImpl extAttDP = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider();
            ExtPersonDataProviderImpl extPersonDataProvider = nacidDataProvider.getExtPersonDataProvider();
            RegprofTrainingCourseDataProviderImpl tcDP = nacidDataProvider.getRegprofTrainingCourseDataProvider();
            RegprofApplicationAttachmentDataProvider attDP = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
            
            ExtRegprofApplicationImpl extAppl = getExtRegprofApplication(extApplicationId);
            ExtRegprofTrainingCourseImpl extTceImpl = getTrainingCourse(extApplicationId);
            if(extAppl == null) {
                throw new UnknownRecordException("no ext application id=" + extApplicationId);
            }
            if (extAppl.getApplicationDetails().getRegprofApplicationId() != null) {
                throw new RuntimeException("Already transferred....");
            }
            
            
            ExtPerson extPerson = extPersonDataProvider.getExtPerson(extAppl.getApplicationDetails().getApplicantId());
            ExtRegprofTrainingCourseRecord extTrainingCourseRecord = extAppl.getTrainingCourseDetails();
            
            RegprofTrainingCourseDetailsImpl trainingCourse = new RegprofTrainingCourseDetailsImpl(
                    null, 
                    extTrainingCourseRecord.getDocumentFname(), 
                    extTrainingCourseRecord.getDocumentLname(), 
                    extTrainingCourseRecord.getDocumentSname(), 
                    extTrainingCourseRecord.getDocumentCivilId(), 
                    extTrainingCourseRecord.getDocumentCivilIdType(), 
                    extTrainingCourseRecord.getProfInstitutionId(), 
                    extTrainingCourseRecord.getDocumentNumber(), 
                    extTrainingCourseRecord.getDocumentDate(), 
                    extTrainingCourseRecord.getSecProfQualificationId(), 
                    extTrainingCourseRecord.getHighProfQualificationId(), 
                    extTrainingCourseRecord.getHighEduLevelId(), 
                    extTrainingCourseRecord.getSdkProfInstitutionId(), 
                    extTrainingCourseRecord.getSdkProfQualificationId(), 
                    extTrainingCourseRecord.getSdkDocumentNumber(), 
                    extTrainingCourseRecord.getSdkDocumentDate(), 
                    extTrainingCourseRecord.getEducationTypeId(), 
                    extTrainingCourseRecord.getHasExperience(), 
                    extTrainingCourseRecord.getHasEducation(), 
                    extTrainingCourseRecord.getDocumentType(), 
                    extTrainingCourseRecord.getSdkDocumentType(), 
                    extTrainingCourseRecord.getProfInstitutionOrgNameId(), 
                    extTrainingCourseRecord.getSdkProfInstitutionOrgNameId(), 
                    extTrainingCourseRecord.getSecCaliberId(), 
                    extTrainingCourseRecord.getDocumentSeries(), 
                    extTrainingCourseRecord.getDocumentRegNumber(), 
                    extTrainingCourseRecord.getSdkDocumentSeries(), 
                    extTrainingCourseRecord.getSdkDocumentRegNumber(), 
                    extTrainingCourseRecord.getCertificateProfQualificationId(), 1, extTrainingCourseRecord.getRegulatedEducationTraining());
            ExtRegprofApplicationDetailsImpl extApplicationDetails = extAppl.getApplicationDetails();
            String externalSystemId = db.getExternalSystemId(extApplicationId);
            RegprofApplicationDetailsImpl applicationDetails = new RegprofApplicationDetailsImpl(
                    null, 
                    null, //appNum
                    null, //appDate
                    applicantId,
                    representativeId,
                    extPerson.getEmail(), 
                    extApplicationDetails.getApplicantPhone(), 
                    extApplicationDetails.getApplicantCity(), 
                    extApplicationDetails.getApplicantAddrDetails(), 
                    extApplicationDetails.getApplicantCountryId(), 
                    null, //repFromCompany, 
                    null, //repPhone, 
                    null, //repAddrDetails, 
                    null, //repEmail, 
                    personDocumentId, //applicantDocumentsId 
                    null, //notes, 
                    ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE,
                    ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE,
                    1, //personalEmailInforming,
                    extApplicationDetails.getPersonalDataUsage(),  
                    1,  //dataAuthentic
                    extApplicationDetails.getNamesDontMatch(), 
                    trainingCourse.getId(),  
                    extApplicationDetails.getApplicationCountryId(), 
                    extApplicationDetails.getServiceTypeId(),  
                    appDP.calculateApplicationEndDate(null, extApplicationDetails.getServiceTypeId()),//end date
                    null,
                    extApplicationDetails.getApostilleApplication(),
                    null,
                    extApplicationDetails.getDocumentReceiveMethodId(),
                    externalSystemId,
                    extApplicationDetails.getApplicantPersonalIdDocumentType());
           RegprofApplicationImpl app = new RegprofApplicationImpl();
           app.setApplicant(db.selectRecord(new PersonRecord(), applicantId));
           app.setRepresentative(representativeId == null ? new PersonRecord() : db.selectRecord(new PersonRecord(), applicantId));
           app.setApplicantDocuments(db.selectRecord(new PersonDocumentRecord(), personDocumentId));
           app.setApplicationDetails(applicationDetails);
           app.setTrCourseDocumentPersonDetails(trainingCourse);

            //address na poluchatelq na udostoverenieto
            if (extAppl.getApplicationDetails().getDocumentReceiveMethodId() != null && nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(extAppl.getApplicationDetails().getDocumentReceiveMethodId()).hasDocumentRecipient() && extAppl.getDocumentRecipient() != null) {
                ExtRegprofDocumentRecipientRecord extDocumentRecipientRecord = extAppl.getDocumentRecipient();
                RegprofDocumentRecipientRecord intDocumentRecipient = new RegprofDocumentRecipientRecord();
                intDocumentRecipient.setAddress(extDocumentRecipientRecord.getAddress());
                intDocumentRecipient.setCity(extDocumentRecipientRecord.getCity());
                intDocumentRecipient.setCountryId(extDocumentRecipientRecord.getCountryId());
                intDocumentRecipient.setDistrict(extDocumentRecipientRecord.getDistrict());
                intDocumentRecipient.setMobilePhone(extDocumentRecipientRecord.getMobilePhone());
                intDocumentRecipient.setName(extDocumentRecipientRecord.getName());
                intDocumentRecipient.setPostCode(extDocumentRecipientRecord.getPostCode());
                app.setDocumentRecipient(intDocumentRecipient);

            }
            //kraj na address na poluchatelq na udostoverenieto
           
           app = appDP.saveRegprofApplicationRecord(app, userId);
           
           trainingCourse.setId(app.getTrCourseDocumentPersonDetails().getId());
           tcDP.updateTrainingCourseDetails(trainingCourse);
           
           
           
           //prehvyrlqne na experienceRecord-a
           ExtRegprofProfessionExperienceRecord extExperienceRecord = extTceImpl.getExperienceRecord();
           if (extExperienceRecord != null) {
               List<RegprofProfessionExperienceDocumentRecord> docRecs = new ArrayList<RegprofProfessionExperienceDocumentRecord>();
               for (RegprofProfessionExperienceDocumentRecord extDocRec:extExperienceRecord.getProfessionExperienceDocuments()) {
                   List<RegprofProfessionExperienceDatesRecord> docDates = new ArrayList<RegprofProfessionExperienceDatesRecord>();
                   for (RegprofProfessionExperienceDatesRecord extDocDate:extDocRec.getDates()) {
                       docDates.add(new RegprofProfessionExperienceDatesRecord(0, extDocDate.getDateFrom(), extDocDate.getDateTo(), extDocDate.getWorkdayDuration(), extDocDate.getProfExperienceDocumentId()));
                   }
                   RegprofProfessionExperienceDocumentRecord docRec = new RegprofProfessionExperienceDocumentRecord(
                           0, 
                           extDocRec.getProfExperienceDocTypeId(), 
                           extDocRec.getDocumentNumber(), 
                           extDocRec.getDocumentDate(), 
                           extDocRec.getDocumentIssuer(),  
                           extDocRec.getProfExperienceId(),  
                           extDocRec.getForExperienceCalculation(), 
                           docDates);
                   docRecs.add(docRec);
               }
               RegprofProfessionExperienceRecord experienceRecord = new RegprofProfessionExperienceRecord(0, extExperienceRecord.getNomenclatureProfessionExperienceId(), userId, 
                       new Date(), extExperienceRecord.getYears(), extExperienceRecord.getMonths(), extExperienceRecord.getDays(), docRecs, trainingCourse.getId());
               RegprofTrainingCourseImpl tceImpl = new RegprofTrainingCourseImpl();
               tceImpl.setExperienceRecord(experienceRecord);
               tceImpl.setDetails(trainingCourse);
               tcDP.saveRegprofProfessionExperienceRecords(tceImpl);
           }
           //krai na prehvyrlqne na experienceRecord-a
           
           
           //prehvyrlqne na spcialities!!
           List<ExtRegprofTrainingCourseSpecialitiesRecord> extSpecialities = extTceImpl.getSpecialities();
           if (extSpecialities != null) {
               List<RegprofTrainingCourseSpecialityRecord> specs = new ArrayList<RegprofTrainingCourseSpecialityRecord>();
               for (ExtRegprofTrainingCourseSpecialitiesRecord r:extSpecialities) {
                   if (r.getSecondarySpecialityId() != null || r.getHigherSpecialityId() != null || r.getSdkSpecialityId() != null) {
                       specs.add(new RegprofTrainingCourseSpecialityRecord(null, trainingCourse.getId(), r.getSecondarySpecialityId(), r.getHigherSpecialityId(), r.getSdkSpecialityId()));    
                   }
               }
               tcDP.saveTrainingCourseSpecialities(specs);
           }
           //kraj na prehvyrlqne na specialities
           db.updateApplicationStatusOnApplicationSubmition(extApplicationId, applicationDetails.getId(), ExtRegprofApplicationImpl.STATUS_TRANSFERED);
           
           //prehvyrlqne na prika4enite dokumenti
           List<Attachment> extAttachments = extAttDP.getAttachmentsForApplication(extAppl.getApplicationDetails().getId());
           for (Attachment attachment : extAttachments) {
               try {
                   Attachment att = extAttDP.getApplicationAttacment(attachment.getId(), true);
                   attDP.saveAttachment(0, app.getId(), att.getDocDescr(), att.getDocTypeId(), att.getCopyTypeId(), null, null, att.getContentType(), 
                           att.getFileName(), att.getContentStream(), att.getContentStream().available(), null, null, null, 0, userId);
               } catch (IOException e) {
                   throw Utils.logException(e);
               }
           }
           //krai na prehvyrlqne na prika4enite dokumenti



           return app;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }

    @Override
    public void markApplicationFinished(int extApplicationId) {
        try {
            db.markApplicationFinished(extApplicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public ExtRegprofApplicationCommentExtended getApplicationComment(int id) {
        try {
            return db.getApplicationCommentExtendedRecord(id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<ExtRegprofApplicationCommentExtended> getApplicationComments(int applicationId) {
        try {
            return db.getApplicationCommentExtendedRecords(applicationId).stream().map(r -> (ExtRegprofApplicationCommentExtended)r).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void saveApplicationComment(int applicationId, String comment, boolean sendEmail, Integer emailId, boolean systemMessage, int userCreated) {
        try {
            ExtRegprofApplicationCommentRecord rec = new ExtRegprofApplicationCommentRecord(0, applicationId, comment, sendEmail ? 1 : 0, emailId, systemMessage ? 1: 0, new Timestamp(new Date().getTime()), userCreated);
            db.insertRecord(rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    private boolean compareSignedUnsignedXml(String signedXml, String unsignedXml) throws Exception {
        //pravi documents ot String-ovete, sled koeto maha signedNode-a ot podpisaniq dokument i pak pravi Stringove, koito sravnqva!
        Document unsignedDoc = generateDocumentFromByteArray(unsignedXml.getBytes());
        Document signedDoc = generateDocumentFromByteArray(signedXml.getBytes());

        signedDoc.getFirstChild().removeChild(signedDoc.getFirstChild().getLastChild());//removing signed node!
        signedXml = new String(generateByteArrayFromDocument(signedDoc));
        unsignedXml = new String(generateByteArrayFromDocument(unsignedDoc));
        return signedXml.equals(unsignedXml);

    }
    private static byte[] generateByteArrayFromDocument(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.transform(new DOMSource(doc), new StreamResult(os));
        return os.toByteArray();
    }
    private static Document generateDocumentFromByteArray(byte[] content) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(content));
    }
    public static void main(String[] args) throws Exception {
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
        
        
        ExtRegprofApplicationsDataProviderImpl dp = (ExtRegprofApplicationsDataProviderImpl)nacidDataProvider.getExtRegprofApplicationsDataProvider();
        //System.out.println(dp.getEsignedInformation(54));
        System.out.println(ExtRegprofESignedInformationRecord.class.getMethod("getValidityFrom"));
        //System.out.println(dp.getExtRegprofApplicationXml(54));
        
        
    }
}
