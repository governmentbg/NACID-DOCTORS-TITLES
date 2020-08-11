package com.nacid.regprof.web.model.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.ProfessionalInstitutionExamination;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.applications.regprof.RegprofTrainingCourseDetailsBase;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.nomenclatures.regprof.ProfessionalInstitutionType;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;

public class RegprofProfInstExamWebModel {
    private String qualBg;
    private Integer qualBgSecId;
    private Integer qualBgHighSdkId;
    private Integer profInstId;
    private String profInst;
    private String profInstType;
    private String profInstData;
    private Integer examId;
    private String examNotes;


    public RegprofProfInstExamWebModel(RegprofTrainingCourseDetailsBase trainingCourseDetails, NacidDataProvider nacidDataProvider, RegprofApplication appRecord){
        profInstId = 0;
        qualBg = null;

        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();
        ProfessionalInstitutionDataProvider piDp = nacidDataProvider.getProfessionalInstitutionDataProvider();

        Integer eduTypeId = trainingCourseDetails.getEducationTypeId() == null ? null : trainingCourseDetails.getEducationTypeId();
        if(eduTypeId == null){
            profInstData = "noData";
        } else {
            if(eduTypeId == EducationType.EDU_TYPE_SECONDARY || eduTypeId == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL){
                qualBgSecId = trainingCourseDetails.getSecProfQualificationId();
                qualBg = qualBgSecId != null ? nomDp.getSecondaryProfessionalQualification(qualBgSecId).getName() : "";
                profInstId = trainingCourseDetails.getProfInstitutionId();
            } else if(eduTypeId == EducationType.EDU_TYPE_HIGH){
                qualBgHighSdkId = trainingCourseDetails.getHighProfQualificationId();
                qualBg = qualBgHighSdkId != null ? nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, qualBgHighSdkId).getName() : "";
                profInstId = trainingCourseDetails.getProfInstitutionId();
            } else if(eduTypeId == EducationType.EDU_TYPE_SDK){
                qualBgHighSdkId = trainingCourseDetails.getSdkProfQualificationId();
                qualBg = qualBgHighSdkId != null ? nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, qualBgHighSdkId).getName() : "";           
                profInstId = trainingCourseDetails.getSdkProfInstitutionId();
            } 
        }

        ProfessionalInstitution institution = profInstId == null ? null : piDp.getProfessionalInstitution(profInstId);
        ProfessionalInstitutionExamination examination = appDp.getProfessionalInstitutionExaminationForApp(appRecord.getApplicationDetails().getId());

        ProfessionalInstitutionType institutionType = institution != null ? (ProfessionalInstitutionType) nomDp.getFlatNomenclature(NomenclaturesDataProvider.
                FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE, institution.getProfessionalInstitutionTypeId()) : null ;
        profInst = institution != null ? institution.getBgName() : "Няма въведена информация за институция";
        profInstType = institutionType != null ? institutionType.getName() : "Няма въведена информация за институция";
        if(institution == null){
            profInstData = "noData";
        }
        if(qualBg == null){
            qualBg = "Няма въведена информация за професионална квалификация";           
        }

        if(examination != null){
            examId = examination.getId();
            examNotes = examination.getNotes();            
        }
    }

    public String getQualBg() {
        return qualBg;
    }

    public Integer getQualBgSecId() {
        return qualBgSecId;
    }

    public Integer getQualBgHighSdkId() {
        return qualBgHighSdkId;
    }

    public Integer getProfInstId() {
        return profInstId;
    }

    public String getProfInst() {
        return profInst;
    }

    public String getProfInstType() {
        return profInstType;
    }

    public String getProfInstData() {
        return profInstData;
    }

    public Integer getExamId() {
        return examId;
    }

    public String getExamNotes() {
        return examNotes;
    }

}
