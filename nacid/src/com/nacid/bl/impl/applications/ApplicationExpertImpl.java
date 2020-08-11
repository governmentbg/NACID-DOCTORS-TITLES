package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.ApplicationExpert;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.data.applications.ApplicationExpertRecord;
import com.nacid.data.applications.ApplicationExpertSpecialityRecord;

import java.util.ArrayList;
import java.util.List;

public class ApplicationExpertImpl implements ApplicationExpert{
    private ApplicationExpertRecord record;
    private NacidDataProviderImpl nacidDataProvider;
    private NomenclaturesDataProvider nomenclaturesDataProvider;
    private List<ApplicationExpertSpecialityRecord> applicationExpertSpecialities;
    public ApplicationExpertImpl(NacidDataProviderImpl nacidDataProvider, ApplicationExpertRecord record) {
      this.record = record;
      this.nacidDataProvider = nacidDataProvider;
      this.nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
      
    }
    public int getId() {
        return record.getId();
    }
    public ComissionMember getExpert() {
        ComissionMemberDataProvider comissionMemberDataProvider = nacidDataProvider.getComissionMemberDataProvider();
        return comissionMemberDataProvider.getComissionMember(record.getExpertId());
    }
    public int getExpertId() {
        return record.getExpertId();
    }
    
    public String getNotes() {
        return record.getNotes();
    }
    @Override
    public int getProcessStat() {
        return record.getProcessStat();
    }

    @Override
    public String getCourseContent() {
        return record.getCourseContent();
    }

    public Integer getExpertPositionId() {
        return record.getExpertPosition();
    }
    @Override
    public ExpertPosition getExpertPosition() {
        return record.getExpertPosition() == null ? null : nomenclaturesDataProvider.getExpertPosition(record.getExpertPosition());
    }

    @Override
    public Integer getEduLevelId() {
        return record.getEduLevelId();
    }

    @Override
    public Integer getQualificationId() {
        return record.getQualificationId();
    }

    @Override
    public String getPreviousBoardDecisions() {
        return record.getPreviousBoardDecisions();
    }

    @Override
    public String getSimilarBulgarianPrograms() {
        return record.getSimilarBulgarianPrograms();
    }

    public FlatNomenclature getQualification() {
        return getQualificationId() == null ? null : nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION, getQualificationId());
    }
    public FlatNomenclature getEducationLevel() {
        return  getEduLevelId() == null ? null : nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getEduLevelId());
    }

    public Integer getLegalReasonId() {
        return record.getLegalReasonId();
    }
    public LegalReason getLegalReason() {
        return getLegalReasonId() == null ? null : nomenclaturesDataProvider.getLegalReason(getLegalReasonId());
    }
    @Override
    public List<Integer> getSpecialityIds() {
        if (applicationExpertSpecialities == null) {
            readApplicationExpertSpecialities();
        }
        List<Integer> result = null;
        if (applicationExpertSpecialities != null && applicationExpertSpecialities.size() > 0) {
            result = new ArrayList<Integer>();
            for (ApplicationExpertSpecialityRecord applicationExpertSpeciality : applicationExpertSpecialities) {
                result.add(applicationExpertSpeciality.getSpecialityId());
            }
        }
        return result;

    }

    @Override
    public List<Speciality> getSpecialities() {
        List<Integer> specialityIds = getSpecialityIds();
        List<Speciality> result = null;
        if (specialityIds != null){
            result = new ArrayList<Speciality>();
            for (Integer specialityId : specialityIds) {
                result.add(nomenclaturesDataProvider.getSpeciality(specialityId));
            }
        }
        return result;
    }
    private void readApplicationExpertSpecialities() {
        applicationExpertSpecialities = nacidDataProvider.getApplicationsDataProvider().getApplicationExpertSpecialities(record.getApplicationId(), record.getExpertId());
    }
}
