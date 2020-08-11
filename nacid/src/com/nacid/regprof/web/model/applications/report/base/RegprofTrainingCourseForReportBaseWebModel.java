package com.nacid.regprof.web.model.applications.report.base;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsBaseImpl;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.db.utils.StandAloneDataSource;

//RayaWtitten---------------------------------------------------------
public class RegprofTrainingCourseForReportBaseWebModel {
    protected String id;
    protected String documentFname;
    protected String documentLname;
    protected String documentSname;
    protected String documentCivilId;
    protected String documentCivilIdType;
        
    protected String documentNumber;
    protected String documentDate;
    protected String sdkDocumentNumber;
    protected String sdkDocumentDate;
    protected String documentType;
    protected String sdkDocumentType;
    protected String documentSeries;
    protected String documentRegNumber;
    protected String sdkDocumentSeries;
    protected String sdkDocumentRegNumber;
    
    protected String secProfQualification;   
    protected String sdkProfQualification;   
    protected String highProfQualification;
    protected String certificateProfQualification;
    
    protected String highEduLevel;
    
    protected String educationType;
    protected boolean hasExperience;
    protected boolean hasEducation;
    
    protected String profInstitution;
    protected String sdkProfInstitution;
    protected String profInstitutionOrgName;
    protected String sdkProfInstitutionOrgName;
   
    protected String secCaliber;
   
    protected boolean high;
    protected boolean sdk;
    protected boolean sec;
    
    protected boolean notRestricted;
    protected boolean regulatedEducationTraining;

    public RegprofTrainingCourseForReportBaseWebModel(RegprofTrainingCourseDetailsBaseImpl trainingCourseDetails, NacidDataProvider nacidDataProvider){
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        id = trainingCourseDetails.getId()+"";
        documentFname = trainingCourseDetails.getDocumentFname();
        documentLname = trainingCourseDetails.getDocumentLname();
        documentSname = trainingCourseDetails.getDocumentSname();
        documentCivilId = trainingCourseDetails.getDocumentCivilId();
        FlatNomenclature civilIdTypeNom = null;
        if(trainingCourseDetails.getDocumentCivilIdType() != null){
            civilIdTypeNom = nomDp.getFlatNomenclature(
                    NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, trainingCourseDetails.getDocumentCivilIdType());
        }
        documentCivilIdType = civilIdTypeNom!= null ? civilIdTypeNom.getName() : null ;
        ////////////////////////////////////////
        ProfessionalInstitutionDataProvider pidp = nacidDataProvider.getProfessionalInstitutionDataProvider();
        ProfessionalInstitution pi = null;
        if(trainingCourseDetails.getProfInstitutionId() != null){
            pi = pidp.getProfessionalInstitution(trainingCourseDetails.getProfInstitutionId());
        }
        profInstitution = pi != null ? pi.getBgName() : null;
        Integer profInstitutionOrgNameId = trainingCourseDetails.getProfInstitutionOrgNameId();
        if (profInstitutionOrgNameId != null) {
            profInstitutionOrgName = pidp.getProfessionalInstitutionFormerName(profInstitutionOrgNameId);
        }
        ProfessionalInstitution piSDK = null;
        if(trainingCourseDetails.getSdkProfInstitutionId()!= null){
            piSDK = pidp.getProfessionalInstitution(trainingCourseDetails.getSdkProfInstitutionId());
        }
        sdkProfInstitution = piSDK != null ? piSDK.getBgName() : null;
        Integer sdkProfInstitutionOrgNameId = trainingCourseDetails.getSdkProfInstitutionOrgNameId();
        if (sdkProfInstitutionOrgNameId != null) {
            sdkProfInstitutionOrgName = pidp.getProfessionalInstitutionFormerName(sdkProfInstitutionOrgNameId);
        }
        /////////////////////////////////
        documentNumber = trainingCourseDetails.getDocumentNumber();
        documentDate = trainingCourseDetails.getDocumentDate();
        sdkDocumentNumber = trainingCourseDetails.getSdkDocumentNumber();
        sdkDocumentDate = trainingCourseDetails.getSdkDocumentDate();
        documentSeries = trainingCourseDetails.getDocumentSeries();
        documentRegNumber = trainingCourseDetails.getDocumentRegNumber();
        sdkDocumentSeries = trainingCourseDetails.getSdkDocumentSeries();
        sdkDocumentRegNumber = trainingCourseDetails.getSdkDocumentRegNumber();
        FlatNomenclature docType = null;
        if(trainingCourseDetails.getDocumentType()!= null){
            docType = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, trainingCourseDetails.getDocumentType());
        }
        documentType = docType != null ? docType.getName() : null;
        FlatNomenclature sdkDocType = null;
        if(trainingCourseDetails.getSdkDocumentType() != null){
            sdkDocType = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, trainingCourseDetails.getSdkDocumentType());
        }
        sdkDocumentType = sdkDocType != null ? sdkDocType.getName() : null;    
        ///////////////////////////////////////
        FlatNomenclature eduL = null;
        if(trainingCourseDetails.getHighEduLevelId() != null){
            eduL = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, trainingCourseDetails.getHighEduLevelId());
        }
        highEduLevel = eduL != null ? eduL.getName() : null;
        
        /////////////////////////////////
        FlatNomenclature secPQ = null;
        if(trainingCourseDetails.getSecProfQualificationId() != null){
            secPQ = nomDp.getSecondaryProfessionalQualification(trainingCourseDetails.getSecProfQualificationId());
        }
        secProfQualification = secPQ != null ? secPQ.getName() : null;
        FlatNomenclature hpq = null;
        if(trainingCourseDetails.getHighProfQualificationId() != null){
            hpq = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, trainingCourseDetails.getHighProfQualificationId());
        }
        highProfQualification = hpq != null ? hpq.getName() : null;
        FlatNomenclature sdkQual = null;
        if(trainingCourseDetails.getSdkProfQualificationId()!=null){
            sdkQual = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, trainingCourseDetails.getSdkProfQualificationId());
        }
        sdkProfQualification = sdkQual != null ? sdkQual.getName() : null;
        FlatNomenclature certQual = null;
        if(trainingCourseDetails.getCertificateProfQualificationId() != null){
            certQual = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, trainingCourseDetails.getCertificateProfQualificationId());
        }
        certificateProfQualification = certQual != null ? certQual.getName() : null;
        ////////////////////////////////////
        FlatNomenclature eduType = null;
        if(trainingCourseDetails.getEducationTypeId() != null){
            eduType = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_TYPE, trainingCourseDetails.getEducationTypeId());
        }
        educationType = eduType != null ? eduType.getName() : null;
        hasExperience = trainingCourseDetails.getHasExperience() == 1 ? true : false;
        hasEducation = trainingCourseDetails.getHasEducation() == 1 ? true : false;
        ///////////////////////////////////////
        FlatNomenclature secCal = null;
        if(trainingCourseDetails.getSecCaliberId() != null){
            secCal = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_SECONDARY_CALIBER, trainingCourseDetails.getSecCaliberId());
        }
        secCaliber = secCal != null ? secCal.getName() : null;
        ///////////////////////////////////////////////        
        high = trainingCourseDetails.getEducationTypeId() != null && trainingCourseDetails.getEducationTypeId() == EducationType.EDU_TYPE_HIGH;
        sdk = trainingCourseDetails.getEducationTypeId() != null && trainingCourseDetails.getEducationTypeId() == EducationType.EDU_TYPE_SDK;
        sec = trainingCourseDetails.getEducationTypeId() != null && (trainingCourseDetails.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY || trainingCourseDetails.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL);

        regulatedEducationTraining = trainingCourseDetails.getRegulatedEducationTraining() == 1;
    }



    public String getId() {
        return id;
    }



    public String getDocumentFname() {
        return documentFname;
    }



    public String getDocumentLname() {
        return documentLname;
    }



    public String getDocumentSname() {
        return documentSname;
    }



    public String getDocumentCivilId() {
        return documentCivilId;
    }



    public String getDocumentCivilIdType() {
        return documentCivilIdType;
    }



    public String getProfInstitution() {
        return profInstitution;
    }



    public String getDocumentNumber() {
        return documentNumber;
    }



    public String getDocumentDate() {
        return documentDate;
    }



    public String getSecProfQualification() {
        return secProfQualification;
    }



    public String getHighProfQualification() {
        return highProfQualification;
    }



    public String getHighEduLevel() {
        return highEduLevel;
    }



    public String getSdkProfInstitution() {
        return sdkProfInstitution;
    }



    public String getSdkProfQualification() {
        return sdkProfQualification;
    }



    public String getSdkDocumentNumber() {
        return sdkDocumentNumber;
    }



    public String getSdkDocumentDate() {
        return sdkDocumentDate;
    }



    public String getEducationType() {
        return educationType;
    }



    public boolean isHasExperience() {
        return hasExperience;
    }



    public boolean isHasEducation() {
        return hasEducation;
    }



    public String getDocumentType() {
        return documentType;
    }



    public String getSdkDocumentType() {
        return sdkDocumentType;
    }



    public String getProfInstitutionOrgName() {
        return profInstitutionOrgName;
    }



    public String getSdkProfInstitutionOrgName() {
        return sdkProfInstitutionOrgName;
    }



    public String getSecCaliber() {
        return secCaliber;
    }



    public String getDocumentSeries() {
        return documentSeries;
    }



    public String getDocumentRegNumber() {
        return documentRegNumber;
    }



    public String getSdkDocumentSeries() {
        return sdkDocumentSeries;
    }



    public String getSdkDocumentRegNumber() {
        return sdkDocumentRegNumber;
    }



    public String getCertificateProfQualification() {
        return certificateProfQualification;
    }

    public boolean isHigh() {
        return high;
    }



    public boolean isSdk() {
        return sdk;
    }



    public boolean isSec() {
        return sec;
    }

    public boolean isNotRestricted() {
        return notRestricted;
    }

    public boolean isRegulatedEducationTraining() {
        return regulatedEducationTraining;
    }

    public static void main(String args[]){
        NacidDataProviderImpl nacid = new NacidDataProviderImpl(new StandAloneDataSource());
        NomenclaturesDataProvider nomDp = nacid.getNomenclaturesDataProvider();
        FlatNomenclature civilIdTypeNom = nomDp.getFlatNomenclature(
                NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null);
        System.out.println(civilIdTypeNom);
    }
}
//------------------------------------------------------------------
