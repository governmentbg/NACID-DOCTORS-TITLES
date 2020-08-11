package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl;
import com.nacid.bl.impl.regprof.ProfessionalInstitutionImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.ProfessionExperienceDocumentType;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.utils.regprof.EducationTypeUtils;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.ProfessionalInstitutionNamesRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.regprof.web.model.applications.RegprofTrainingCourseSpecialityWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class RegprofTrainingCourseHandler extends RegProfBaseRequestHandler {

    private final static String ATTRIBUTE_NAME = RegprofTrainingCourseImpl.class.getName();
    private final static String NEXT_SCREEN = "regprofapplication";
    private final static int MAX_SPECIALITIES_COUNT = 10; // 4 ?

    private static final List<Integer> workdayDurations = new ArrayList<Integer>();
    static {
        workdayDurations.add(2);
        workdayDurations.add(4);
        workdayDurations.add(6);
        workdayDurations.add(8);
    }

    private ServletContext servletContext;

    public RegprofTrainingCourseHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInteger(request.getParameter("appId"), -1);
        if (applicationId < 0) {
            applicationId = DataConverter.parseInteger(request.getParameter("id"), -1);
            if (applicationId < 0) {
                applicationId = (Integer) request.getAttribute("id");
                if (applicationId == null || applicationId < 0) {
                    throw new UnknownRecordException("Unknown regprof application ID:" + applicationId);
                }
            }
        }
        RegprofTrainingCourse newTrainingCourse = new RegprofTrainingCourseImpl();

        newExperience(request, newTrainingCourse, applicationId);

        RegprofTrainingCourseDataProvider trainingCourseDataProvider = getNacidDataProvider().getRegprofTrainingCourseDataProvider();   
        NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();

        RegprofTrainingCourseDetailsImpl details = trainingCourseDataProvider.getTrainingCourseDetails(applicationId);
        if (details != null) {
            newTrainingCourse.setDetails(details);
        } else { // ne sym siguren dali e vyzmojno da se vleze v tozi else
            RegprofTrainingCourseDetailsImpl newDetails = new RegprofTrainingCourseDetailsImpl();
            newDetails.setId(0);
            newDetails.setHasEducation(1);
            newTrainingCourse.setDetails(newDetails);
        }

        //TODO: ako e vyzmojno proverkata da syshtestvuva predi training course-a, da se izvlicha ot DB-to
        // newTrainingCourse.getValidityRecord().setApplicationId(applicationId);

        generateComboBoxes(request, nDP, null, null, null, null, null, null, null);

        request.setAttribute(ATTRIBUTE_NAME, newTrainingCourse);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInt(request.getParameter("appId"), -1); // regprof application id
        if (applicationId <= 0) {
            applicationId = (Integer) request.getAttribute("id");
            if (applicationId == null || applicationId <= 0) {
                throw new UnknownRecordException("Unknown regprof application ID:" + applicationId);
            }
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofTrainingCourseDataProvider dp = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        RegprofTrainingCourse trainingCourse = dp.getRegprofTrainingCourse(applicationId);

        //NomenclaturesDataProvider ndp = nacidDataProvider.getNomenclaturesDataProvider();

        if (trainingCourse != null) {
            if (trainingCourse.getExperienceRecord() != null) {
                NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
                FlatNomenclature profession = trainingCourse.getExperienceRecord().getNomenclatureProfessionExperienceId() == null ? null : nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, trainingCourse.getExperienceRecord().getNomenclatureProfessionExperienceId());
                String professionName = profession == null ? null : profession.getName();
                request.setAttribute("professionName", professionName);
                
                /*for (int i = 0; i < trainingCourse.getDatesRecords().size(); i++) {
                    String comboName = "datesRecords[" + i + "].workdayDuration";
                    ComboBoxWebModel combo = ComboBoxUtils.generateComboBox(trainingCourse.getDatesRecords().get(i).getWorkdayDuration(), workdayDurations, request,
                            "datesRecords[" + i + "].workdayDuration", true, "intValue", "intValue");
                    request.setAttribute(comboName, combo);
                }*/
                /*if (trainingCourse.getExperienceRecord().isNotRestricted() == 1) {
                    request.setAttribute("is_unrestricted", "checked=\"checked\"");
                }*/
            }
            else {
                newExperience(request, trainingCourse, applicationId);
            }

            if (trainingCourse.getDetails() != null) {
                RegprofTrainingCourseDetailsBase details = trainingCourse.getDetails();
                Integer educationTypeId = details.getEducationTypeId();
                Integer professionalQualificationId = details.getSecProfQualificationId();
                Integer highEducationLevelId = details.getHighEduLevelId();
                Integer documentType = details.getDocumentType();
                Integer sdkDocumentType = details.getSdkDocumentType();
                Integer secCaliberId = details.getSecCaliberId();
                Integer certificateProfessionalQualificationId = details.getCertificateProfQualificationId();

                //Integer profExperienceDocTypeId = trainingCourse.getExperienceRecord().getNomenclatureProfExperienceDocTypeId();

                NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();

                if (certificateProfessionalQualificationId != null) {
                    FlatNomenclature certificateQualification = 
                            nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, certificateProfessionalQualificationId);
                    request.setAttribute("certificate_qualification", certificateQualification.getName());
                }

                generateComboBoxes(request, nDP, educationTypeId, professionalQualificationId, null, highEducationLevelId, documentType, 
                        sdkDocumentType, secCaliberId/*, profExperienceDocTypeId*/);
                Integer secondarySpecialityId = null;

                if (educationTypeId != null) {
                    ProfessionalInstitutionDataProvider pidp = nacidDataProvider.getProfessionalInstitutionDataProvider();
                    ProfessionalInstitution institution = details.getProfInstitutionId() == null ? null : pidp.getProfessionalInstitution(details.getProfInstitutionId());
                    
                    List<RegprofTrainingCourseSpecialityRecord> specialities = dp.getTrainingCourseSpecialities(trainingCourse.getDetails().getId());
                    List<RegprofTrainingCourseSpecialityWebModel> specialitiesList = new ArrayList<RegprofTrainingCourseSpecialityWebModel>();
                    for (RegprofTrainingCourseSpecialityRecord speciality : specialities) {
                        specialitiesList.add(new RegprofTrainingCourseSpecialityWebModel(nDP, speciality));
                    }
                    String institutionBgName = institution == null ? "" : institution.getBgName();
                    if (educationTypeId == EducationTypeUtils.HIGHER_EDUCATION_TYPE) {
                        request.setAttribute("highBgName", institutionBgName);
                        request.setAttribute("highOrgName", pidp.getProfessionalInstitutionFormerName(details.getProfInstitutionOrgNameId()));
                        if (details.getHighProfQualificationId() != null) {
                            FlatNomenclature qualification = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getHighProfQualificationId());
                            request.setAttribute("high_qualification", qualification.getName());
                            request.setAttribute("qualificationAsProfession", qualification.getName());
                        }
                        //FlatNomenclature speciality = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, details.getHighSpecialityId());
                        //request.setAttribute("high_speciality", speciality.getName());
                        request.setAttribute("higherSpecialitiesList", specialitiesList);
                        request.setAttribute("higherSpecialitiesCount", specialitiesList.size());
                    } else if (educationTypeId == EducationTypeUtils.SECONDARY_EDUCATION_TYPE || educationTypeId == EducationTypeUtils.SECONDARY_PROFESSIONAL_EDUCATION_TYPE) {
                        request.setAttribute("secBgName", institutionBgName);
                        request.setAttribute("secOrgName", pidp.getProfessionalInstitutionFormerName(details.getProfInstitutionOrgNameId()));
                        //stepen na profesionalnata kvalifikaciq
                        SecondaryProfessionalQualification qualification = nDP.getSecondaryProfessionalQualification(details.getSecProfQualificationId());
                        request.setAttribute("qualificationAsProfession", qualification.getName());
                        request.setAttribute("secondarySpecialitiesList", specialitiesList);
                        request.setAttribute("secondarySpecialitiesCount", specialitiesList.size());
                        secondarySpecialityId = specialities.isEmpty() ? null : specialities.get(specialities.size() - 1).getSecondarySpecialityId(); // vzema poslednata ?
                    } else if (educationTypeId == EducationTypeUtils.SDK_EDUCATION_TYPE) {
                        ProfessionalInstitution sdkInstitution = details.getSdkProfInstitutionId() == null ? null : pidp.getProfessionalInstitution(details.getSdkProfInstitutionId());
                        request.setAttribute("highBgName", institutionBgName);
                        request.setAttribute("highOrgName", pidp.getProfessionalInstitutionFormerName(details.getProfInstitutionOrgNameId()));
                        request.setAttribute("sdkBgName", sdkInstitution == null ? "" : sdkInstitution.getBgName());
                        request.setAttribute("sdkOrgName", pidp.getProfessionalInstitutionFormerName(details.getSdkProfInstitutionOrgNameId()));
                        if (details.getHighProfQualificationId() != null) {
                            FlatNomenclature qualification = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getHighProfQualificationId());
                            request.setAttribute("high_qualification", qualification.getName());
                        }
                        if (details.getSdkProfQualificationId() != null) {
                            FlatNomenclature sdkQualification = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getSdkProfQualificationId());
                            request.setAttribute("sdk_qualification", sdkQualification.getName());
                            request.setAttribute("qualificationAsProfession", sdkQualification.getName());
                        }
                        //FlatNomenclature speciality = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, details.getHighSpecialityId());
                        //request.setAttribute("high_speciality", speciality.getName());
                        
                        //FlatNomenclature sdkSpeciality = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, details.getSdkSpecialityId());
                        //request.setAttribute("sdk_speciality", sdkSpeciality.getName());

                        List<RegprofTrainingCourseSpecialityWebModel> higherSpecialitiesList = new ArrayList<RegprofTrainingCourseSpecialityWebModel>();
                        List<RegprofTrainingCourseSpecialityWebModel> sdkSpecialitiesList = new ArrayList<RegprofTrainingCourseSpecialityWebModel>();
                        for (RegprofTrainingCourseSpecialityWebModel specialityWebModel : specialitiesList) {
                            if (specialityWebModel.getHigherSpecialityId() != null) {
                                higherSpecialitiesList.add(specialityWebModel);
                            } else if (specialityWebModel.getSdkSpecialityId() != null) {
                                sdkSpecialitiesList.add(specialityWebModel);
                            }
                        }
                        request.setAttribute("higherSpecialitiesList", higherSpecialitiesList);
                        request.setAttribute("higherSpecialitiesCount", higherSpecialitiesList.size());
                        request.setAttribute("sdkSpecialitiesList", sdkSpecialitiesList);
                        request.setAttribute("sdkSpecialitiesCount", sdkSpecialitiesList.size());

                    } else {
                        throw Utils.logException(new Exception("incorrect education type id"));
                    }
                }
            } else {
                trainingCourse.getDetails().setId(0);
                NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();
                generateComboBoxes(request, nDP, null, null, null, null, null, null, null);
            }

            /*User user = getLoggedUser(request, response);
            /*RegprofApplication application = nacidDataProvider.getRegprofApplicationDataProvider().getRegprofApplication(applicationId);
            if (application.getApplicationDetails().getResponsibleUser() != null && user.getUserId() == application.getApplicationDetails().getResponsibleUser()) {
                
                request.setAttribute("is_responsible_user_logged", 1);
                NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
                
                if (application.getApplicationStatusId() == RegprofApplicationStatus.APPLICATION_PODADENO_STATUS_CODE || 
                        application.getApplicationStatusId() == RegprofApplicationStatus.APPLICATION_FOR_EXAMINATION_STATUS_CODE) { 
                    FlatNomenclature podadeno = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_REGPROF_APP_STATUS, RegprofApplicationStatus.APPLICATION_PODADENO_STATUS_CODE);
                    FlatNomenclature forExamination = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_REGPROF_APP_STATUS, RegprofApplicationStatus.APPLICATION_FOR_EXAMINATION_STATUS_CODE);
                    List<FlatNomenclature> listOfStatuses = new ArrayList<FlatNomenclature>();
                    listOfStatuses.add(podadeno);
                    listOfStatuses.add(forExamination);
                    ComboBoxUtils.generateNomenclaturesComboBox(application.getApplicationStatusId(), listOfStatuses, true, request, "statusComboInTrainingCourse", true);
                } else {
                    FlatNomenclature currentStatus = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_REGPROF_APP_STATUS, application.getApplicationStatusId());
                    List<FlatNomenclature> singleStatusAsList = new ArrayList<FlatNomenclature>();
                    singleStatusAsList.add(currentStatus);
                    ComboBoxUtils.generateNomenclaturesComboBox(application.getApplicationStatusId(), singleStatusAsList, true, request, "statusComboInTrainingCourse", false);
                }
            }*/
            request.setAttribute(ATTRIBUTE_NAME, trainingCourse);
        } else { // new training course
            handleNew(request, response);
        }
    }

    private static void newExperience(HttpServletRequest request, RegprofTrainingCourse trainingCourse, int applicationId) {
        trainingCourse.setExperienceRecord(new RegprofProfessionExperienceRecord());
        trainingCourse.getExperienceRecord().setId(0);
        trainingCourse.getDetails().setNotRestricted(1);
        trainingCourse.getExperienceRecord().setTrainingCourseId(trainingCourse.getDetails().getId());
    }

    private static void saveSecondarySpecialities(HttpServletRequest request, int trainingCourseId, RegprofTrainingCourseDataProvider dp) {
        dp.deleteTrainingCourseSpecialities(trainingCourseId);
        List<RegprofTrainingCourseSpecialityRecord> specialities = new ArrayList<RegprofTrainingCourseSpecialityRecord>();
        List<Integer> specialitiesIds = new ArrayList<Integer>();
        Integer specialitiesCount = DataConverter.parseInteger(request.getParameter("secondary_specialities_count"), null);
        Integer singleSpecialityId = DataConverter.parseInteger(request.getParameter("secSpecialityId"), null);

        if (specialitiesCount != null && specialitiesCount > 0) {
            if (specialitiesCount > MAX_SPECIALITIES_COUNT) {
                specialitiesCount = MAX_SPECIALITIES_COUNT;
            }
            
            for (int i = 0; i < specialitiesCount; i++) {
                Integer specialityId = DataConverter.parseInteger(request.getParameter("speciality_id" + i), null);
                if (specialityId != null && !specialitiesIds.contains(specialityId)) {
                    specialitiesIds.add(specialityId);
                    RegprofTrainingCourseSpecialityRecord record = new RegprofTrainingCourseSpecialityRecord();
                    record.setTrainingCourseId(trainingCourseId);
                    record.setSecondarySpecialityId(specialityId);
                    specialities.add(record);
                }
            }
        }
        if (singleSpecialityId != null) {
            specialitiesIds.add(singleSpecialityId);
            RegprofTrainingCourseSpecialityRecord record = new RegprofTrainingCourseSpecialityRecord();
            record.setTrainingCourseId(trainingCourseId);
            record.setSecondarySpecialityId(singleSpecialityId);
            specialities.add(record);
        }
        dp.saveTrainingCourseSpecialities(specialities);
    }

    private static void saveHigherSpecialities(HttpServletRequest request, int trainingCourseId, RegprofTrainingCourseDataProvider dp, boolean sdk) {
        dp.deleteTrainingCourseSpecialities(trainingCourseId);
        
        List<RegprofTrainingCourseSpecialityRecord> specialities = new ArrayList<RegprofTrainingCourseSpecialityRecord>();
        List<Integer> specialitiesIds = new ArrayList<Integer>();

        Integer higherSpecialitiesCount = DataConverter.parseInteger(request.getParameter("higher_specialities_count"), null);  
        
        if (higherSpecialitiesCount != null && higherSpecialitiesCount > 0) {
            if (higherSpecialitiesCount > MAX_SPECIALITIES_COUNT) {
                higherSpecialitiesCount = MAX_SPECIALITIES_COUNT;
            }
            
            for (int i = 0; i < higherSpecialitiesCount; i++) {
                Integer specialityId = DataConverter.parseInteger(request.getParameter("speciality_id" + i), null);
                if (specialityId != null && !specialitiesIds.contains(specialityId)) {
                    specialitiesIds.add(specialityId);
                    RegprofTrainingCourseSpecialityRecord record = new RegprofTrainingCourseSpecialityRecord();
                    record.setTrainingCourseId(trainingCourseId);
                    record.setHigherSpecialityId(specialityId);
                    specialities.add(record);
                }
            }
        }
        Integer singleHigherSpecialityId = DataConverter.parseInteger(request.getParameter("highSpecialityId"), null);
        if (singleHigherSpecialityId != null) {
            specialitiesIds.add(singleHigherSpecialityId);
            RegprofTrainingCourseSpecialityRecord record = new RegprofTrainingCourseSpecialityRecord();
            record.setTrainingCourseId(trainingCourseId);
            record.setHigherSpecialityId(singleHigherSpecialityId);
            specialities.add(record);
        }
        
        if (sdk) {
            List<Integer> sdkSpecialitiesIds = new ArrayList<Integer>();
            Integer singleSdkSpecialityId = DataConverter.parseInteger(request.getParameter("sdkSpecialityId"), null);
            
            Integer sdkSpecialitiesCount = DataConverter.parseInteger(request.getParameter("sdk_specialities_count"), null);
            if (sdkSpecialitiesCount != null && sdkSpecialitiesCount > 0) {
                if (sdkSpecialitiesCount > MAX_SPECIALITIES_COUNT) {
                    sdkSpecialitiesCount = MAX_SPECIALITIES_COUNT;
                }
                
                for (int i = 0; i < sdkSpecialitiesCount; i++) {
                    Integer specialityId = DataConverter.parseInteger(request.getParameter("sdk_speciality_id" + i), null);
                    if (specialityId != null && !sdkSpecialitiesIds.contains(specialityId)) {
                        sdkSpecialitiesIds.add(specialityId);
                        RegprofTrainingCourseSpecialityRecord record = new RegprofTrainingCourseSpecialityRecord();
                        record.setTrainingCourseId(trainingCourseId);
                        record.setSdkSpecialityId(specialityId);
                        specialities.add(record);
                    }
                }
            }
            if (singleSdkSpecialityId != null) {
                sdkSpecialitiesIds.add(singleSdkSpecialityId);
                RegprofTrainingCourseSpecialityRecord record = new RegprofTrainingCourseSpecialityRecord();
                record.setTrainingCourseId(trainingCourseId);
                record.setSdkSpecialityId(singleSdkSpecialityId);
                specialities.add(record);
            }
        }
        dp.saveTrainingCourseSpecialities(specialities);
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
        
        RegprofTrainingCourseImpl trainingCourse = (RegprofTrainingCourseImpl) request.getAttribute(ATTRIBUTE_NAME);
        if (trainingCourse == null) {
            throw new UnknownRecordException("Unknown training course...");
        }
        Integer applicationId = DataConverter.parseInteger(request.getParameter("appId"), null);
        if (applicationId == null || applicationId < 0) {
            throw new UnknownRecordException("Unknown regprof application id...");
        }
        
        RegprofTrainingCourseDataProvider dp = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        User user = getLoggedUser(request, response);
        
        //experience and dates records
        boolean submitExperience = trainingCourse.getDetails().getHasExperience() == 1;
        if (submitExperience) {
            if (trainingCourse.getExperienceRecord() != null && trainingCourse.getExperienceRecord().getId() != null) {
                /*if (trainingCourse.getExperienceRecord().getNotRestricted() == null) {
                    trainingCourse.getExperienceRecord().setNotRestricted(0);
                }*/
                trainingCourse.getExperienceRecord().setUserId(user.getUserId()); // koi user go e syzdal
                
                String professionName = request.getParameter("professionName");
                professionName = professionName.trim();
                
                List<FlatNomenclature> professions = nDP.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, null, null);
                boolean isNew = true;
                for (FlatNomenclature item : professions) {
                    if (item.getName().equals(professionName)) {
                        trainingCourse.getExperienceRecord().setNomenclatureProfessionExperienceId(item.getId());
                        isNew = false;
                        break;
                    }
                }
                if (isNew) {
                    int newProfessionId = nDP.saveFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, 0, professionName, null, null);
                    trainingCourse.getExperienceRecord().setNomenclatureProfessionExperienceId(newProfessionId);
                }
            }    
        }
        
        //details
        Integer submitEducation = trainingCourse.getDetails().getHasEducation();
        if (trainingCourse.getDetails() != null && trainingCourse.getDetails().getId() != null && submitEducation != null && submitEducation == 1) {
            
            Integer educationType = trainingCourse.getDetails().getEducationTypeId();
            if (educationType != null) {
                ProfessionalInstitutionDataProvider pidp = nacidDataProvider.getProfessionalInstitutionDataProvider();
                //List<ProfessionalInstitutionImpl> institutions = pidp.getProfessionalInstitutions(null, null, trainingCourse.getDetails().getEducationTypeId());
                
                String orgName = "";
                String bgName = "";
                String sdkOrgName = "";
                String sdkBgName = "";
                
                if (educationType == EducationTypeUtils.HIGHER_EDUCATION_TYPE) {
                    orgName = DataConverter.parseString(request.getParameter("highOrgName").trim(), "");
                    bgName = DataConverter.parseString(request.getParameter("highBgName").trim(), "");
                    saveHigherSpecialities(request, trainingCourse.getDetails().getId(), dp, false);
                } else if (educationType == EducationTypeUtils.SECONDARY_EDUCATION_TYPE || educationType == EducationTypeUtils.SECONDARY_PROFESSIONAL_EDUCATION_TYPE) {
                    orgName = DataConverter.parseString(request.getParameter("secOrgName").trim(), "");
                    bgName = DataConverter.parseString(request.getParameter("secBgName").trim(), "");
                    saveSecondarySpecialities(request, trainingCourse.getDetails().getId(), dp);
                } else if (educationType == EducationTypeUtils.SDK_EDUCATION_TYPE) {
                    orgName = DataConverter.parseString(request.getParameter("highOrgName").trim(), "");
                    bgName = DataConverter.parseString(request.getParameter("highBgName").trim(), "");
                    sdkOrgName = DataConverter.parseString(request.getParameter("sdkOrgName").trim(), "");
                    sdkBgName = DataConverter.parseString(request.getParameter("sdkBgName").trim(), "");
                    saveHigherSpecialities(request, trainingCourse.getDetails().getId(), dp, true);
                }
                
                if (educationType == EducationTypeUtils.SDK_EDUCATION_TYPE) {
                    if (trainingCourse.getDetails().getProfInstitutionId() == null) {
                        saveNewProfessionalInstitution(trainingCourse, pidp, EducationTypeUtils.HIGHER_EDUCATION_TYPE, orgName, bgName);
                    }
                    if (trainingCourse.getDetails().getSdkProfInstitutionId() == null) {
                        saveNewProfessionalInstitution(trainingCourse, pidp, EducationTypeUtils.SDK_EDUCATION_TYPE, sdkOrgName, sdkBgName);
                    }
                    if (trainingCourse.getDetails().getSdkProfInstitutionOrgNameId() == null && !(sdkOrgName.isEmpty())) {
                        saveProfessionalInstitutionFormerName(trainingCourse, pidp, trainingCourse.getDetails().getSdkProfInstitutionId(), sdkOrgName, true);
                    }
                } else if (trainingCourse.getDetails().getProfInstitutionId() == null) {
                    saveNewProfessionalInstitution(trainingCourse, pidp, educationType, orgName, bgName);
                }
                
                if (trainingCourse.getDetails().getProfInstitutionOrgNameId() == null && !(orgName.isEmpty())) {
                    saveProfessionalInstitutionFormerName(trainingCourse, pidp, trainingCourse.getDetails().getProfInstitutionId(), orgName, false);
                }
            }
            resetDetailsFields(trainingCourse);
        }
        //end of details
        
        Calendar calendar = Calendar.getInstance();
        java.util.Date currentDate = calendar.getTime();
        java.sql.Date date = new java.sql.Date(currentDate.getTime());
        
        request.setAttribute("id", applicationId);
        
        String certificateQualification = request.getParameter("certificate_qualification").trim();
        int certificateQualificationId = 0;
        List<FlatNomenclature> listOfQualifications = nDP.getFlatNomenclatures(nDP.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, null, null);
        for (FlatNomenclature qualification : listOfQualifications) {
            if (qualification.getName().equalsIgnoreCase(certificateQualification)) {
                certificateQualificationId = qualification.getId();
                break;
            }
        }
        if (certificateQualificationId == 0) {
            certificateQualificationId = nDP.saveFlatNomenclature(nDP.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, 0, certificateQualification, null, null);
        }
        trainingCourse.getDetails().setCertificateProfQualificationId(certificateQualificationId);

        //RayaWritten-------------------------------------------------
        //Delete the examinations if changes are made before saving those changes
       // deleteExaminationsIfChanges(trainingCourse.getDetails(), applicationId);
        //--------------------------------------------------------------
        dp.updateTrainingCourseDetails(trainingCourse.getDetails());
        RegprofApplicationDetails appDetails = appDataProvider.getRegprofApplication(applicationId).getApplicationDetails();
        RegprofTrainingCourseDetailsBase details = trainingCourse.getDetails();
        
        if ((details.getHasEducation() == 1 || details.getHasExperience() == 1) && appDetails.getStatus() == ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE) {
            appDataProvider.saveStatusOnly(appDetails.getId(), ApplicationStatus.APPLICATION_FOR_EXAMINATION_STATUS_CODE, ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE,
                getLoggedUser(request, response).getUserId(), null);
        }
        if (submitExperience && trainingCourse.getExperienceRecord() != null && trainingCourse.getExperienceRecord().getId() != null) {
            trainingCourse.getExperienceRecord().setDateCreated(date);
            //RayaWritten----------------------------------------------
            //If there are changes in experience dates - delete the examination
            //deleteExperienceExaminationIfChanges(trainingCourse, applicationId);
            //--------------------------------------------------------------
            dp.saveRegprofProfessionExperienceRecords(trainingCourse);
            /*appDataProvider.saveStatusOnly(RegprofApplicationStatus.APPLICATION_FOR_EXAMINATION_STATUS_CODE, 
                    appDataProvider.getRegprofApplication(applicationId).getApplicationDetails(), getLoggedUser(request, response).getUserId());*/
        }
        else {
            dp.deleteRegprofProfessionExperienceRecords(trainingCourse.getDetails().getId());
        }

        //RegprofApplication regprofApplication = appDataProvider.getRegprofApplication(applicationId);

        /* promenq statusa ako e bil STAJ i v Obu4enie/Staj e promenen checkbox-a ili e iztrit staja */
        if (appDataProvider.getRegprofApplication(applicationId).getApplicationDetails().getStatus() == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE &&
                (trainingCourse.getDetails().getHasExperience() == 0 || trainingCourse.getExperienceRecord() == null ||
                (trainingCourse.getExperienceRecord() != null && trainingCourse.getDetails().getNotRestricted() == 0))) {
            appDataProvider.saveStatusOnly(applicationId, ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE, ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE, user.getUserId(), null);
        }
        /* krai */
        
        //if (id > 0) {
            SystemMessageWebModel systemMessageWebmodel = new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT); 
            request.setAttribute("trainingCourseSystemMessage", systemMessageWebmodel);
       // }
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_TRAINING_DATA);
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
        new RegprofApplicationHandler(servletContext).handleEdit(request, response);
    }
        
    private static void resetDetailsFields(RegprofTrainingCourse trainingCourse) {
        Integer educationTypeId = trainingCourse.getDetails().getEducationTypeId();
        if (educationTypeId != null) {
            if (educationTypeId == EducationTypeUtils.HIGHER_EDUCATION_TYPE) {
                resetSecondaryEducationFields(trainingCourse);
                resetSDKEducationFields(trainingCourse);
            }
            else if (educationTypeId == EducationTypeUtils.SECONDARY_EDUCATION_TYPE || educationTypeId == EducationTypeUtils.SECONDARY_PROFESSIONAL_EDUCATION_TYPE) {
                resetHigherEducationFields(trainingCourse);
                resetSDKEducationFields(trainingCourse);
            }
            else if (educationTypeId == EducationTypeUtils.SDK_EDUCATION_TYPE) {
                resetSecondaryEducationFields(trainingCourse);
            }
        }
        else {
            resetSecondaryEducationFields(trainingCourse);
            resetSDKEducationFields(trainingCourse);
            resetHigherEducationFields(trainingCourse);
            resetOtherFields(trainingCourse);
        }
    }
    
    private static void resetSecondaryEducationFields(RegprofTrainingCourse trainingCourse) {
        trainingCourse.getDetails().setSecProfQualificationId(null);
    }
    
    private static void resetSDKEducationFields(RegprofTrainingCourse trainingCourse) {
        trainingCourse.getDetails().setSdkDocumentDate(null);
        trainingCourse.getDetails().setSdkDocumentNumber(null);
        trainingCourse.getDetails().setSdkProfInstitutionId(null);
        trainingCourse.getDetails().setSdkProfQualificationId(null);
    }
    
    private static void resetHigherEducationFields(RegprofTrainingCourse trainingCourse) {
        trainingCourse.getDetails().setHighEduLevelId(null);
        trainingCourse.getDetails().setHighProfQualificationId(null);
    }
    
    private static void resetOtherFields(RegprofTrainingCourse trainingCourse) {
        trainingCourse.getDetails().setProfInstitutionId(null);
        trainingCourse.getDetails().setDocumentNumber(null);
        trainingCourse.getDetails().setDocumentDate(null);
    }
    
    public static void generateComboBoxes(HttpServletRequest request, NomenclaturesDataProvider nDP,
            Integer educationId, Integer secProfQualificationId, Integer secSpecialityId, Integer highEducationLevelId,
            Integer documentType, Integer sdkDocumentType, Integer secCaliberId/*, Integer profExperienceDocTypeId*/) {
        
        ComboBoxUtils.generateNomenclaturesComboBox(educationId, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_TYPE, nDP, true, request, "educationTypeCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(secProfQualificationId, NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_PROFESSIONAL_QUALIFICATION, nDP, true, request, "secProfQualificationCombo", null, true);
        if (secSpecialityId != null) {
            ComboBoxUtils.generateNomenclaturesComboBox(secSpecialityId, NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_SPECIALITY, nDP, true, request, "secSpecialityCombo", null, true);
        } else {
            ComboBoxUtils.generateNomenclaturesComboBox(null, new ArrayList<FlatNomenclature>(), true, request, "secSpecialityCombo", true);
        }
        ComboBoxUtils.generateNomenclaturesComboBox(highEducationLevelId, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, nDP, true, request, "highEduLevelCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(documentType, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, nDP, true, request, "documentDocTypeCombo",
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE,  NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        ComboBoxUtils.generateNomenclaturesComboBox(sdkDocumentType, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, nDP, true, request, "sdkDocTypeCombo", 
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE,  NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);
        ComboBoxUtils.generateNomenclaturesComboBox(secCaliberId, NomenclaturesDataProvider.FLAT_NOMENCLATURE_SECONDARY_CALIBER, nDP, true, request, "secondaryCaliberCombo", null, true);
        
        List<ProfessionExperienceDocumentType> docTypes = nDP.getProfessionExperienceDocumentTypes(null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(null, docTypes, true, request, "profExperienceDocTypeCombo", true);
        
        Map<Integer, Integer> docTypesMap = new HashMap<Integer, Integer>();
        for (ProfessionExperienceDocumentType dt : docTypes) {
            docTypesMap.put(dt.getId(), dt.isForExperienceCalculation() ? 1 : 0);
        }
        request.setAttribute("docTypeForExperienceCalculation", docTypesMap);
        ComboBoxUtils.generateComboBox(null, workdayDurations, request, "workdayDurations", true, "toString", "toString");
    }

    private static void saveNewProfessionalInstitution(RegprofTrainingCourse trainingCourse, ProfessionalInstitutionDataProvider pidp, Integer educationType, String orgName, String bgName) {
        List<ProfessionalInstitutionImpl> institutions = pidp.getProfessionalInstitutions(null, null, educationType);
        boolean isNewOrgName = true;
        boolean isNewBgName = true;
        int professionalInstitutionId = 0;
        int professionalInstitutionOrgNameId = 0;
        for (ProfessionalInstitutionImpl item : institutions) {
            if (item.getBgName().equalsIgnoreCase(bgName)) {
                isNewBgName = false;
                professionalInstitutionId = item.getId();
                break;
            }
        }
        if (isNewBgName) {
            if (!orgName.equals("")) {
                List<ProfessionalInstitutionNamesRecord> formerNames = pidp.getProfessionalInstitutionNames(null, null, true);
                for (ProfessionalInstitutionNamesRecord item : formerNames) {
                    if (item.getFormerName().equalsIgnoreCase(orgName)) {
                        isNewOrgName = false;
                        professionalInstitutionOrgNameId = item.getId();
                        professionalInstitutionId = item.getProfessionalInstitutionId();
                        break;
                    }
                }
            }
            
            if (isNewOrgName) {
                ProfessionalInstitution institution = new ProfessionalInstitutionImpl();
                institution.setId(null);
                institution.setProfessionalInstitutionTypeId(pidp.getInstitutionType(educationType));
                institution.setCountryId(Country.COUNTRY_ID_BULGARIA);//RayaChanged country only BG
                institution.setBgName(bgName);
                ProfessionalInstitution newInstitution = pidp.saveProfessionalInstitutionRecord(institution);
                professionalInstitutionId = newInstitution.getId();
                if (!orgName.isEmpty()) {
                    ProfessionalInstitutionNamesRecord record = new ProfessionalInstitutionNamesRecord(null, professionalInstitutionId, orgName, 1);
                    Integer newRecordId = pidp.saveProfessionalInstitutionName(record);
                    if (newRecordId != null) {
                        professionalInstitutionOrgNameId = newRecordId;
                    }
                }
            }
        }
        
        if (educationType == EducationTypeUtils.SDK_EDUCATION_TYPE) {
            trainingCourse.getDetails().setSdkProfInstitutionId(professionalInstitutionId);
            if (professionalInstitutionOrgNameId > 0) {
                trainingCourse.getDetails().setSdkProfInstitutionOrgNameId(professionalInstitutionOrgNameId);
            }
        }
        else {
            trainingCourse.getDetails().setProfInstitutionId(professionalInstitutionId);
            if (professionalInstitutionOrgNameId > 0) {
                trainingCourse.getDetails().setProfInstitutionOrgNameId(professionalInstitutionOrgNameId);
            }
        }
    }
    
    private static void saveProfessionalInstitutionFormerName(RegprofTrainingCourse trainingCourse, ProfessionalInstitutionDataProvider pidp, int professionalInstitutionId, String orgName, boolean sdk) {
        boolean isNewOrgName = true;
        Integer orgNameId = null;
        List<ProfessionalInstitutionNamesRecord> existingFormerNames = pidp.getProfessionalInstitutionNames(professionalInstitutionId, null, true);
        for (ProfessionalInstitutionNamesRecord record : existingFormerNames) {
            if (orgName.equalsIgnoreCase(record.getFormerName())) {
                isNewOrgName = false;
                orgNameId = record.getId();
                break;
            }
        }
        if (isNewOrgName) {
            ProfessionalInstitutionNamesRecord record = new ProfessionalInstitutionNamesRecord(null, professionalInstitutionId, orgName, 1);
            orgNameId = pidp.saveProfessionalInstitutionName(record);
            if (sdk) {
                trainingCourse.getDetails().setSdkProfInstitutionOrgNameId(orgNameId);
            }
            else {
                trainingCourse.getDetails().setProfInstitutionOrgNameId(orgNameId);
            }
        }
    }   

}