package com.nacid.regprof.web.model.applications.report.base;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsBaseImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
import com.nacid.web.model.applications.report.base.PersonForReportBaseWebModel;

//RayaWritten--------------------------------------------------
public class RegprofApplicationForReportBaseWebModel {    
    protected String id;
    protected String applicantCity;
    protected String applicantAddrDetails;
    protected String applicantPhone;
    protected String applicantCountry;

    protected boolean personalDataUsage;    
    protected boolean namesDontMatch;

    protected String applicationCountry;
    protected String serviceType;

    protected String applicantEmail;
    protected boolean personalEmailInforming;
    protected String documentReceiveMethod;
    protected String applicantPersonalIdDocumentType;
    protected PersonForReportBaseWebModel applicant;
    protected PersonDocumentForReportBaseWebModel applicantDocument;
    protected RegprofTrainingCourseForReportBaseWebModel trainingCourseWebModel;
    protected RegprofProfessionExperienceForReportBaseWebModel professionExperience;
    protected List<RegprofTrainingCourseSpecialityForReportBaseWebModel> specialities;
    protected RegprofDocumentRecipientBaseWebModel documentRecipient;

    public RegprofApplicationForReportBaseWebModel(RegprofApplicationDetailsBaseImpl appDetails, NacidDataProvider nacidDataProvider) {        
        id = appDetails.getId() + "";

        applicantCity = appDetails.getApplicantCity();
        applicantAddrDetails = appDetails.getApplicantAddrDetails();
        applicantPhone = appDetails.getApplicantPhone();
        Country c = null;
        if(appDetails.getApplicantCountryId() != null){
            c = nacidDataProvider.getNomenclaturesDataProvider().getCountry(appDetails.getApplicantCountryId());            
        }
        applicantCountry = c != null ? c.getName() : null; 
        personalDataUsage = appDetails.getPersonalDataUsage() == 1 ? true : false; 
        //dataAuthentic = appDetails.getDataAuthentic() == 1 ? true : false; //TODO: ???
        namesDontMatch = appDetails.getNamesDontMatch() == 1 ? true : false;
        Country ac  = null;
        if(appDetails.getApplicationCountryId() != null){
            ac = nacidDataProvider.getNomenclaturesDataProvider().getCountry(appDetails.getApplicationCountryId());            
        }
        applicationCountry = ac != null ? ac.getName() : null; 
        ServiceType st = null;
        if(appDetails.getServiceTypeId() != null){
            st = nacidDataProvider.getNomenclaturesDataProvider().getServiceType(appDetails.getServiceTypeId());        
        }
        serviceType = st != null ? st.getName() : null;
        specialities = new ArrayList<RegprofTrainingCourseSpecialityForReportBaseWebModel>();
        this.documentReceiveMethod = appDetails.getDocumentReceiveMethodId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(appDetails.getDocumentReceiveMethodId()).getName();
        this.applicantPersonalIdDocumentType = appDetails.getApplicantPersonalIdDocumentType() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, appDetails.getApplicantPersonalIdDocumentType()).getName();
    }

    public PersonForReportBaseWebModel getApplicant() {
        return applicant;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getId() {
        return id;
    }

    public String getApplicantCity() {
        return applicantCity;
    }

    public String getApplicantAddrDetails() {
        return applicantAddrDetails;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public String getApplicantCountry() {
        return applicantCountry;
    }

    public boolean isPersonalDataUsage() {
        return personalDataUsage;
    }

    public boolean isPersonalEmailInforming() {
        return personalEmailInforming;
    }

    public boolean isNamesDontMatch() {
        return namesDontMatch;
    }

    public String getApplicationCountry() {
        return applicationCountry;
    }

    public String getServiceType() {
        return serviceType;
    }

    public RegprofTrainingCourseForReportBaseWebModel getTrainingCourseWebModel() {
        return trainingCourseWebModel;
    }

    public RegprofProfessionExperienceForReportBaseWebModel getProfessionExperience() {
        return professionExperience;
    }

    public List<RegprofTrainingCourseSpecialityForReportBaseWebModel> getSpecialities() {
        return specialities;
    }

    public PersonDocumentForReportBaseWebModel getApplicantDocument() {
        return applicantDocument;
    }

    public String getDocumentReceiveMethod() {
        return documentReceiveMethod;
    }

    public RegprofDocumentRecipientBaseWebModel getDocumentRecipient() {
        return documentRecipient;
    }

    public String getApplicantPersonalIdDocumentType() {
        return applicantPersonalIdDocumentType;
    }
}
//---------------------------------------------------------------
