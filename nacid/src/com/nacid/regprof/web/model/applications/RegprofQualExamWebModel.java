package com.nacid.regprof.web.model.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.applications.regprof.RegprofQualificationExamination;
import com.nacid.bl.applications.regprof.RegprofTrainingCourseDataProvider;
import com.nacid.bl.applications.regprof.RegprofTrainingCourseDetailsBase;
import com.nacid.bl.nomenclatures.EducationLevel;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleItem;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;

import java.util.List;

//RayaWritten---------------------------------------------
public class RegprofQualExamWebModel {
    private String eduType;
    private String eduTypeShort;
    private String qualDegree = "";
    private String recognizedProfession;
    private String qualData;
    private String qualBg;
    private Integer recognizedQualDegree;
    private Integer recognizedQualLevel;
    //private String isQualRecognized;
    private Integer recognizedProfessionId;
    private Integer qualExaminationId;
    private List<FlatNomenclature> qualDegreesComboAttrs;
    private List<FlatNomenclature> qualLevelsComboAttrs;
    private Integer articleId;
    private Integer itemId;
    private boolean recognizedQualificationTeacher;
    private Integer ageRange;
    private Integer schoolType;
    private Integer grade;

    //public RegprofQualExamWebModel(NacidDataProvider nacidDataProvider, RegprofTrainingCourse trainingCourse){
    public RegprofQualExamWebModel(NacidDataProvider nacidDataProvider, RegprofTrainingCourseDetailsBase trainingCourseDetails) {
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofTrainingCourseDataProvider tcDp = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        //RegprofTrainingCourseDetails trainingCourseDetails = trainingCourse.getDetails();
        Integer eduTypeId = trainingCourseDetails.getEducationTypeId() == null ? null : trainingCourseDetails.getEducationTypeId();
        FlatNomenclature eduTypeNom = eduTypeId != null ? nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_TYPE, eduTypeId) : null;
        eduType = eduTypeNom != null ? eduTypeNom.getName() : "Няма въведена информация за вид обучение";

        RegprofQualificationExamination qualExamination = appDp.getRegprofQualificationExaminationForTrainingCourse(trainingCourseDetails.getId());
        if (qualExamination != null) {
            this.recognizedQualificationTeacher = qualExamination.getRecognizedQualificationTeacher() == 1;
            this.ageRange = qualExamination.getAgeRange();
            this.schoolType = qualExamination.getSchoolType();
            this.grade = qualExamination.getGrade();
        }


        getDefaultRecognizedProfession(eduTypeId, nomDp, qualExamination, trainingCourseDetails);
        if(eduTypeId == null){
            qualData = "noData";
            qualDegree = "Няма въведена информация за квалификационно ниво";
        } else {
            if(eduTypeId == EducationType.EDU_TYPE_HIGH ){
                eduTypeShort = "high";
                if(trainingCourseDetails.getHighEduLevelId() != null){
                    FlatNomenclature eduLevelNom = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, trainingCourseDetails.getHighEduLevelId());
                    qualDegree = eduLevelNom != null ? eduLevelNom.getName() : null;       
                    recognizedQualLevel = qualExamination != null ? qualExamination.getRecognizedQualificationLevelId() : null;
                    if(recognizedQualLevel == null){
                        recognizedQualLevel = trainingCourseDetails.getHighEduLevelId();
                        if(recognizedQualLevel == EducationLevel.EDUCATION_LEVEL_VO_NO_LEVEL){
                            recognizedQualLevel = EducationLevel.EDUCATION_LEVEL_MASTER;
                        } else if(recognizedQualLevel == EducationLevel.EDUCATION_LEVEL_PVO_NO_LEVEL){
                            recognizedQualLevel = EducationLevel.EDUCATION_LEVEL_PROFESSIONAL_BACHELOR;
                        }
                    } 
                    qualBg = trainingCourseDetails.getHighProfQualificationId() == null ? "" : 
                        nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, trainingCourseDetails.getHighProfQualificationId()).getName();
                } else {
                    qualDegree = "Няма въведена информация за квалификационно ниво";
                    qualData = "noData";
                }  
                qualLevelsComboAttrs = nomDp.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, null, null, 
                        OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));

            } else if(eduTypeId == EducationType.EDU_TYPE_SDK){
                eduTypeShort = "sdk";
                //if(trainingCourseDetails.getSdkEduLevelId() != null){
                    //FlatNomenclature eduLevelNom = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, trainingCourseDetails.getSdkEduLevelId());
                    qualDegree = null; 
                    recognizedQualLevel = null;
                    /*recognizedQualLevel = qualExamination != null ? qualExamination.getRecognizedQualificationLevelId() : null;
                    if(recognizedQualLevel == null){
                        recognizedQualLevel = trainingCourseDetails.getSdkEduLevelId();
                    }*/
                    qualBg = trainingCourseDetails.getSdkProfQualificationId() == null ? "" :
                            nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, trainingCourseDetails.getSdkProfQualificationId()).getName();
                /*} else {
                    qualData = "noData";
                }
                qualLevelsComboAttrs = nomDp.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, null, null, 
                        OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
                 */
            } else if(eduTypeId == EducationType.EDU_TYPE_SECONDARY || eduTypeId == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL){
                List<RegprofTrainingCourseSpecialityRecord> specs = tcDp.getTrainingCourseSpecialities(trainingCourseDetails.getId());
                eduTypeShort = "sec";
                if(specs != null && specs.size() > 0){
                    for(RegprofTrainingCourseSpecialityRecord s:specs){
                        Integer degreeId = nomDp.getSecondarySpeciality(s.getSecondarySpecialityId()).getQualificationDegreeId();
                        FlatNomenclature qualDegreeNom = degreeId == null ? null : nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, degreeId);
                        qualDegree += qualDegreeNom != null ? qualDegreeNom.getName() + "; " : "";
                        recognizedQualDegree = qualExamination != null ? qualExamination.getRecognizedQualificationDegreeId() : null;
                        SecondaryProfessionalQualification secProfQual = nomDp.getSecondaryProfessionalQualification(trainingCourseDetails.getSecProfQualificationId());
                        qualBg = secProfQual.getName();
                    }
                } else {
                    qualDegree = "Няма въведена информация за квалификационно ниво";
                    qualData = "noData";
                }
                qualDegreesComboAttrs = nomDp.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, null, null,
                        OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
            }
            //isQualRecognized = (qualExamination != null && qualExamination.getIsQualificationRecognized() == 1) ? "checked=checked" : "";
            qualExaminationId = qualExamination != null ? qualExamination.getId() : null;
        }
        if(qualBg == null){
            qualBg = "Няма въведена информация за професионална квалификация";
        }
        if(qualExamination!= null && qualExamination.getArticleItemId()!= null){
            RegprofArticleItem item = nomDp.getRegprofArticleItem(qualExamination.getArticleItemId());
            itemId = item.getId();
            articleId = item != null ? item.getArticleDirectiveId(): null;
        }
    }
    private void getDefaultRecognizedProfession(Integer eduTypeId, NomenclaturesDataProvider nomDp, RegprofQualificationExamination qualExamination, 
            RegprofTrainingCourseDetailsBase trainingCourseDetails) {
        
        if (eduTypeId != null) {
            if (qualExamination != null && qualExamination.getRecognizedProfessionId() != null) {
                recognizedProfessionId = qualExamination.getRecognizedProfessionId();
                FlatNomenclature recProf = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION, qualExamination.getRecognizedProfessionId());
                recognizedProfession = recProf != null ? recProf.getName() : null;
            } else {
                if (eduTypeId == EducationType.EDU_TYPE_HIGH && trainingCourseDetails.getHighProfQualificationId() != null) {
                    FlatNomenclature highProfQual = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, trainingCourseDetails.getHighProfQualificationId());
                    recognizedProfession = highProfQual != null ? highProfQual.getName() : null;
                } else if (eduTypeId == EducationType.EDU_TYPE_SDK && trainingCourseDetails.getSdkProfQualificationId() != null) {
                    FlatNomenclature sdkProfQual = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, trainingCourseDetails.getSdkProfQualificationId());
                    recognizedProfession = sdkProfQual != null ? sdkProfQual.getName() : null;
                } else if (eduTypeId == EducationType.EDU_TYPE_SECONDARY || eduTypeId == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL) {
                    SecondaryProfessionalQualification secProfQual = nomDp.getSecondaryProfessionalQualification(trainingCourseDetails.getSecProfQualificationId());
                    recognizedProfession = secProfQual != null ? secProfQual.getName() : null;
                }
            }
        }
    }
    public String getEduType() {
        return eduType;
    }
    public String getEduTypeShort() {
        return eduTypeShort;
    }
    public String getQualDegree() {
        return qualDegree;
    }
    public String getRecognizedProfession() {
        return recognizedProfession;
    }
    public String getQualData() {
        return qualData;
    }
    public String getQualBg() {
        return qualBg;
    }
    public Integer getRecognizedQualDegree() {
        return recognizedQualDegree;
    }
    public Integer getRecognizedQualLevel() {
        return recognizedQualLevel;
    }
    /*public String getIsQualRecognized() {
        return isQualRecognized;
    }*/
    public Integer getRecognizedProfessionId() {
        return recognizedProfessionId;
    }
    public Integer getQualExaminationId() {
        return qualExaminationId;
    }
    public List<FlatNomenclature> getQualDegreesComboAttrs() {
        return qualDegreesComboAttrs;
    }
    public List<FlatNomenclature> getQualLevelsComboAttrs() {
        return qualLevelsComboAttrs;
    }
    public Integer getArticleId() {
        return articleId;
    }
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    public Integer getItemId() {
        return itemId;
    }
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public boolean isRecognizedQualificationTeacher() {
        return recognizedQualificationTeacher;
    }

    public Integer getAgeRange() {
        return ageRange;
    }

    public Integer getSchoolType() {
        return schoolType;
    }

    public Integer getGrade() {
        return grade;
    }
}
//------------------------------------------------------
