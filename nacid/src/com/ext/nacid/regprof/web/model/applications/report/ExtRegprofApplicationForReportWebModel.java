package com.ext.nacid.regprof.web.model.applications.report;

import java.util.List;

import com.ext.nacid.web.model.applications.report.ExtPersonForReportWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.payments.Liability;
import com.nacid.bl.regprof.external.ExtRegprofESignedInformation;
import com.nacid.bl.regprof.external.ExtRegprofTrainingCourse;
import com.nacid.data.regprof.external.ExtRegprofProfessionExperienceRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseSpecialitiesRecord;
import com.nacid.regprof.web.model.applications.report.base.RegprofApplicationForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofDocumentRecipientBaseWebModel;
import com.nacid.web.payments.LiabilityWebModel;
//RayaWritten-------------------------------------------------------------------------------------
public class ExtRegprofApplicationForReportWebModel extends RegprofApplicationForReportBaseWebModel{
    private LiabilityWebModel liability;
    private ExtRegprofESignedInformationWebModel esignedInfo;
    public ExtRegprofApplicationForReportWebModel(ExtRegprofApplicationImpl app, NacidDataProvider nacidDataProvider, ExtPerson extPerson, ExtRegprofESignedInformation esignedInfo) {
        super(app.getApplicationDetails(), nacidDataProvider);
        super.applicant = extPerson == null ? null : new ExtPersonForReportWebModel(extPerson);
        super.applicantDocument = app.getApplicantDocuments() == null ? null : new ExtPersonDocumentForReportWebModel(app.getApplicantDocuments());
        super.trainingCourseWebModel = new ExtRegprofTrainingCourseForReportWebModel(app.getTrainingCourseDetails(), nacidDataProvider);
        ExtRegprofTrainingCourse extTrCourse = nacidDataProvider.getExtRegprofApplicationsDataProvider().getTrainingCourse(
                app.getApplicationDetails().getId());
        ExtRegprofProfessionExperienceRecord exprRecord = extTrCourse.getExperienceRecord();
        super.professionExperience = new ExtRegprofProfessionExprienceForReportWebModel(exprRecord, nacidDataProvider);
        List<ExtRegprofTrainingCourseSpecialitiesRecord> specs = extTrCourse.getSpecialities();
        if(specs != null && specs.size() >0){
            for(ExtRegprofTrainingCourseSpecialitiesRecord s: specs){
                super.specialities.add(new ExtRegprofTrainingCourseSpecialitiesForReportWebModel(s, nacidDataProvider));
            }
        }
        Liability l = nacidDataProvider.getPaymentsDataProvider().getLiabilityByExternalApplicationId(app.getApplicationDetails().getId());
        if (l != null) {
            liability = new LiabilityWebModel(l, nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PAYMENT_TYPE, l.getPaymentType()));    
        }
        if (esignedInfo != null) {
            this.esignedInfo = new ExtRegprofESignedInformationWebModel(esignedInfo);    
        }
        this.documentRecipient = app.getDocumentRecipient() == null ? null : new RegprofDocumentRecipientBaseWebModel(nacidDataProvider.getNomenclaturesDataProvider(), app.getDocumentRecipient());
    }
    public LiabilityWebModel getLiability() {
        return liability;
    }
    public ExtRegprofESignedInformationWebModel getEsignedInfo() {
        return esignedInfo;
    }
}
//------------------------------------------------------------------------------------------------
