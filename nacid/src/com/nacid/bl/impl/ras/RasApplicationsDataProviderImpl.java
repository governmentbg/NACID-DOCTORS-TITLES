package com.nacid.bl.impl.ras;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.ApplicationsDataProviderImpl;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.impl.ras.objects.*;
import com.nacid.bl.impl.ras.objects.Person;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.CivilIdType;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.DataConverter;
import com.nacid.db.applications.ApplicationsDB;
import com.nacid.db.nomenclatures.RasNomenclaturesMapDb;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.BasicConfigurator;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.nacid.bl.utils.UtilsDataProvider.RAS_SERVICE_URL;
import static com.nacid.db.nomenclatures.RasNomenclaturesMapDb.INSTITUTION_KEY_NACID;
import static com.nacid.db.nomenclatures.RasNomenclaturesMapDb.NOMENCLATURE_TYPE_INSTITUTION;

/**
 * User: ggeorgiev
 * Date: 23.10.2019 г.
 * Time: 13:44
 */
public class RasApplicationsDataProviderImpl implements com.nacid.bl.ras.RasApplicationsDataProvider {

    protected RasService rasRestService;
    private NacidDataProviderImpl nacidDataProvider;
    private ApplicationsDataProviderImpl applicationsDataProvider;
    private ApplicationsDB applicationsDB;
    private RasNomenclaturesMapDb rasNomenclaturesMapDb;

    public RasApplicationsDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.nacidDataProvider = nacidDataProvider;
        this.applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        this.applicationsDB = nacidDataProvider.getApplicationsDataProvider().getApplicationsDB();
        this.rasNomenclaturesMapDb = new RasNomenclaturesMapDb(nacidDataProvider.getDataSource());
        initRasService();

    }

    private void initRasService() {
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJsonProvider());
        String url = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(RAS_SERVICE_URL);
        rasRestService = JAXRSClientFactory.create(url, RasService.class, providers);

        ClientConfiguration config = WebClient.getConfig(rasRestService);
        config.getInInterceptors().add(new LoggingInInterceptor(-1));
        config.getOutInterceptors().add(new LoggingOutInterceptor(-1));

        config.getHttpConduit().getClient().setAllowChunking(false);
    }
    @Override
    public boolean isApplicationTransferredInRas(int applicationId) {
        try {
            return applicationsDB.isApplicationTransferredInRas(applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public synchronized void registerRasDoctorateApplication(int applicationId) {
        try {
            if (applicationsDB.isApplicationTransferredInRas(applicationId)) {
                throw new RuntimeException("Already transferred!");
            }

            Application application = applicationsDataProvider.getApplication(applicationId);
            TrainingCourse tc = application.getTrainingCourse();
            UniversityWithFaculty baseUni = tc.getBaseUniversityWithFaculty();
            AttachmentDataProvider attachmentDataProvider = nacidDataProvider.getApplicationAttachmentDataProvider();
            List<Attachment> certAttachment = attachmentDataProvider.getAttachmentsForParentByTypes(applicationId, new ArrayList<>(DocumentType.RUDI_DOCTORATE_CERTFIFICATE_DOC_TYPES));
            if (CollectionUtils.isEmpty(certAttachment)) {
                throw new RuntimeException("certificate should exist");
            }
            if (certAttachment.get(0).getScannedContentType() == null) {
                throw new RuntimeException("application should have scanned certificate");
            }


            TrainingCourseTrainingLocation baseTrainingLocation = !CollectionUtils.isEmpty(tc.getTrainingCourseTrainingLocations()) ? tc.getTrainingCourseTrainingLocations().get(0) : null;
            TrainingInstitution trainingInstitution = baseTrainingLocation == null ? null : baseTrainingLocation.getTrainingInstitution();

            RegisterApplicationRequest rq = new RegisterApplicationRequest();
            Applicant applicant = new Applicant();

            applicant.setContactPhone(StringUtils.isEmpty(application.getBgPhone()) ? application.getHomePhone() : application.getBgPhone());
            applicant.setCorrespondentType(1);
            applicant.setFirstName("RUDi");
            applicant.setLastName("Admin");
            applicant.setUin("0000000000");
            applicant.setCorrespondentContacts(new ArrayList<>());
            CorrespondentContact cc = new CorrespondentContact();
            cc.setEmail(application.getEmail());
            applicant.getCorrespondentContacts().add(cc);

            rq.setApplicant(applicant);

            rq.setElectronicServiceUri(rasNomenclaturesMapDb.getNomenclatureValue(RasNomenclaturesMapDb.NOMENCLATURE_TYPE_RUDI_APPLICATION_TYPE, ApplicationType.DOCTORATE_APPLICATION_TYPE + ""));//код на електронната услуга за научна степен

            AcademicDegree academicDegree = new AcademicDegree();

            Integer eduLevelId = rasNomenclaturesMapDb.getNomenclatureValueAsInteger(RasNomenclaturesMapDb.NOMENCLATURE_TYPE_EDU_LEVEL, tc.getRecognizedEduLevelId() == null ? null : tc.getRecognizedEduLevelId().toString());
            academicDegree.setAcademicDegreeTypeId(eduLevelId);



            //Ot primerite, koito gledah, te slagat diplomaDate/diplomaNumber na udostoverenieto v NACID!Zatova i az taka go pravq, no da se obsydi s tqh!!!!
//            academicDegree.setDiplomaDate(application.getTrainingCourse().getDiplomaDate());
//            academicDegree.setDiplomaNumber(application.getTrainingCourse().getDiplomaNumber());

            String certNumber = application.getCertificateNumber();
            if (StringUtils.isEmpty(certNumber)) {
                throw new RuntimeException("Unknown certificate number...");
            }
            String[] parts = certNumber.split("/");
            if (parts.length != 2 || DataConverter.parseDate(parts[1]) == null) {
                throw new RuntimeException("Unknown certificate number!!! " + certNumber + "... Should be number/date");
            }
            academicDegree.setDiplomaNumber(parts[0]);
            academicDegree.setDiplomaDate(DataConverter.parseDate(parts[1]));
            academicDegree.setCertificateNumber(academicDegree.getDiplomaNumber());
            academicDegree.setCertificateDate(academicDegree.getDiplomaDate());


            academicDegree.setActive(true);
            academicDegree.setInstitutionId(rasNomenclaturesMapDb.getNomenclatureValueAsInteger((NOMENCLATURE_TYPE_INSTITUTION), INSTITUTION_KEY_NACID));
//            academicDegree.setIndicatorsSum(null);
//            academicDegree.setTotalSumChecked(null);
            Integer rasResearchArea = rasNomenclaturesMapDb.getNomenclatureValueAsInteger(RasNomenclaturesMapDb.NOMENCLATURE_TYPE_PROF_GROUP, tc.getRecognizedProfGroupId() == null ? null : tc.getRecognizedProfGroupId().toString());
            if (rasResearchArea == null) {
                throw new RuntimeException("Unknown mapping for professionGroup:" + tc.getRecognizedProfGroupId());
            }
            academicDegree.setResearchAreaId(rasResearchArea);
            academicDegree.setGraduatedAbroad(true);

            //countryId-to se vzema ot baseTrainingLocation. Ako nqma base trainingLocation - ot university-to

            Integer nacidCountryId = baseTrainingLocation != null && baseTrainingLocation.getTrainingLocationCountryId() != null ? baseTrainingLocation.getTrainingLocationCountryId() : (baseUni != null ? baseUni.getUniversity().getCountryId() : null);
            Integer rasCountryId = rasNomenclaturesMapDb.getNomenclatureValueAsInteger(RasNomenclaturesMapDb.NOMENCLATURE_TYPE_COUNTRY, nacidCountryId + "");
            academicDegree.setCountryId(rasCountryId);


            //za city-to - pyrvo se proverqba dali ima vyveden trainingLocation-> city. Ako nqma, se vzema trainingInstitution's city. Ako i tam nqma, se vzema ot baseUni-to
            String nacidForeignTown = baseTrainingLocation != null && !StringUtils.isEmpty(baseTrainingLocation.getTrainingLocationCity()) ? baseTrainingLocation.getTrainingLocationCity() : (trainingInstitution != null && !StringUtils.isEmpty(trainingInstitution.getCity()) ? trainingInstitution.getCity() : (baseUni == null ? null : baseUni.getUniversity().getCity()));
            academicDegree.setForeignTown(nacidForeignTown);

            if (baseUni != null) {
                academicDegree.setForeignInstitution(baseUni.getUniversity().getBgName());
                academicDegree.setForeignInstitutionAlt(baseUni.getUniversity().getOrgName());
            }

            Dissertation dissertation = new Dissertation();
            academicDegree.setDissertation(dissertation);


            dissertation.setAnnotation(tc.getThesisAnnotation());
            dissertation.setAnnotationAlt(tc.getThesisAnnotationEn());
            dissertation.setDateOfAcquire(tc.getThesisDefenceDate());
            dissertation.setDissertationIsNotDeposited(true);//Дисертацията не подлежи на депозиране в НАЦИД//TODO:Spored men trqbva da e true!!!!
            dissertation.setLanguageId(tc.getThesisLanguageId() == null ? null : rasNomenclaturesMapDb.getNomenclatureValueAsInteger(RasNomenclaturesMapDb.NOMENCLATURE_TYPE_LANGUAGE, tc.getThesisLanguageId().toString()));
            dissertation.setNumberOfBibliography(tc.getThesisBibliography());
            dissertation.setNumberOfPages(tc.getThesisVolume());
            dissertation.setTitle(tc.getThesisTopic());
            dissertation.setTitleAlt(tc.getThesisTopicEn());


            StructuredData structuredData = new StructuredData();
            rq.setStructuredData(structuredData);
            structuredData.setAcademicDegree(academicDegree);


            Person person = new Person();

            com.nacid.bl.applications.Person owner = tc.getOwner();
            person.setBirthDate(owner.getBirthDate());
            person.setFirstName(owner.getFName());
            person.setMiddleName(owner.getSName());
            person.setLastName(owner.getLName());
//            person.setFirstNameAlt(null);//nqmame imena na anglijski
//            person.setMiddleNameAlt(null);
//            person.setLastNameAlt(null);

            person.setType(owner.getCivilIdTypeId() == CivilIdType.CIVIL_ID_TYPE_EGN ? 1 : 2);
            String uin = owner.getCivilId();
            person.setUin(uin);
            structuredData.setPerson(person);


            RasFile rasCert = createRasFile(attachmentDataProvider.getAttachment(certAttachment.get(0).getId(), false, true), true);
            structuredData.setCertificateFile(rasCert);

            List<Attachment> dissertationDocs = attachmentDataProvider.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_DISSERTATION_WORK);
            if (!CollectionUtils.isEmpty(dissertationDocs)) {
                RasFile rasDissertationFile = createRasFile(attachmentDataProvider.getAttachment(dissertationDocs.get(0).getId(), true, true), false);
                structuredData.setDissertationFile(rasDissertationFile);
            }
            List<Attachment> abstractDocs = attachmentDataProvider.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_ABSTRACT);
            if (!CollectionUtils.isEmpty(abstractDocs)) {
                RasFile rasAbstractFile = createRasFile(attachmentDataProvider.getAttachment(abstractDocs.get(0).getId(), true, true), false);
                structuredData.setSummaryFile(rasAbstractFile);
            }

            RegisterApplicationResponse rs = rasRestService.registerApplication(rq);
            applicationsDB.updateRasKey(applicationId, rs.getCode());

        } catch (SQLException | IOException e) {
            throw Utils.logException(e);
        }
    }

    //ako e vdignat flaga scanned, zarejda samo scannedData, ako ne e vdignat, pyrvo se proverqva dali ima scanned, i ako nqma se zarejda original data-ta
    private RasFile createRasFile(Attachment att, boolean scanned) throws IOException {
        if (att != null) {
            RasFile rasFile = new RasFile();
            rasFile.setFileName(scanned ? att.getScannedFileName() : (att.getScannedContentType() == null ? att.getFileName() : att.getScannedFileName()) );
            rasFile.setMimeType(scanned ? att.getScannedContentType() : (att.getScannedContentType() == null ? att.getContentType() : att.getScannedContentType()) );
            rasFile.setContent((IOUtils.toByteArray(scanned ? att.getScannedContentStream() : att.getScannedContentType() == null ? att.getContentStream() : att.getScannedContentStream())));
            return rasFile;
        } else {
            return null;
        }


    }


    public RasService getRasRestService() {
        return rasRestService;
    }


    public static void main(String[] args) {
        BasicConfigurator.configure();
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
        RasApplicationsDataProviderImpl rasApplicationsDataProvider = new RasApplicationsDataProviderImpl((NacidDataProviderImpl) nacidDataProvider);

        RasService rasRestService = rasApplicationsDataProvider.rasRestService;
//        CheckUinResponse response = rasRestService.checkUin("1010101010");
//        System.out.println(response.isExists());

        rasApplicationsDataProvider.registerRasDoctorateApplication(10431);
    }
}
