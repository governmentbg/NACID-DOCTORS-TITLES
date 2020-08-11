package com.nacid.bl.impl.external.applications;

import com.nacid.bl.external.*;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtESignedInformation;
import com.nacid.bl.external.applications.ExtTrainingCourse;
import com.nacid.bl.external.applications.ExtTrainingCourseDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.external.applications.ExtApplicationRecord;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtApplicationImpl implements ExtApplication {
    private ExtApplicationRecord record;
    private NacidDataProviderImpl nacidDataProvider;
    private Boolean esignedChecked;
    private ExtESignedInformation esignedInformation;
    private ExtAddress contactDetails;
    private List<ExtApplicationKind> applicationKinds;
    private String applicationNumber;
    private Date applicationDate;
    private ExtDocumentRecipient documentRecipient;
    private boolean documentRecipientRead;
    private ExtTrainingCourse trainingCourse = null;


    public ExtApplicationImpl(NacidDataProviderImpl nacidDataProvider, ExtApplicationRecord record) {
        this.record = record;
        this.nacidDataProvider = nacidDataProvider;
        String entryNum = record.getEntryNum();
        applicationNumber = entryNum == null ? null : entryNum.split("/")[0];
        applicationDate = entryNum == null ? null : DataConverter.parseDate(entryNum.split("/")[1]);
    }

    public int getId() {
        return record.getId();
    }
    public Integer getApplicantId() {
    	return record.getApplicantId();
    }
    public ExtPerson getApplicant() {
        return record.getApplicantId() == null ? null : nacidDataProvider.getExtPersonDataProvider().getExtPerson(record.getApplicantId());
    }
    public synchronized ExtTrainingCourse getTrainingCourse() {
        if (trainingCourse == null) {
            ExtTrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getExtTrainingCourseDataProvider();
            trainingCourse = trainingCourseDataProvider.getExtTrainingCourse(record.getTrainingCourseId());
        }
        return trainingCourse;

    }
    public int getTrainingCourseId() {
        return record.getTrainingCourseId();
    }
    public int getHomeCountryId() {
    	return record.getHomeCountryId();
    }
    public Country getHomeCountry() {
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getCountry(record.getHomeCountryId());
    }

    public String getHomeCity() {
        return record.getHomeCity();
    }

    public String getHomePostCode() {
        return record.getHomePostCode();
    }

    public String getHomeAddressDetails() {
        return record.getHomeAddressDetails();
    }

    public boolean homeIsBg() {
        return record.getHomeIsBg() != null && record.getHomeIsBg() == 1 ? true : false;
    }

    public String getBgCity() {
        return record.getBgCity();
    }

    public String getBgPostCode() {
        return record.getBgPostCode();
    }

    public String getBgAddressDetails() {
        return record.getBgAddressDetails();
    }

    public Date getTimeOfCreation() {
        return record.getTimeOfCreation();
    }

    public boolean differentApplicantAndDiplomaNames() {
        return record.getDifferentDiplomaNames() == 1 ? true : false;
    }

/*    public List<ApplicationRecognitionPurpose> getApplicationRecoginitionPurposes() {
        return applicationsDataProvider.getApplicationRecognitionPurposes(getId());
    }*/
    
    public String getBgPhone() {
        return record.getBgPhone();
    }

    public String getHomePhone() {
        return record.getHomePhone();
    }
    public String getSummary() {
    	return record.getSummary();
    }

	public Integer getInternalApplicationId() {
		return record.getApplicationId();
	}

	public int getApplicationStatus() {
		return record.getApplicationStatus();
	}

	public Boolean isPersonalDataUsage() {
		return record.getPersonalDataUsage() == null ? null : DataConverter.parseBoolean(record.getPersonalDataUsage() + "");
	}
	public synchronized boolean isESigned() {
		if (esignedChecked == null) {
			ExtApplicationsDataProviderImpl extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
			esignedInformation = extApplicationsDataProvider.getESignedInformation(getId());
			esignedChecked = true;
		}
		return esignedInformation != null;
	}
	public synchronized ExtESignedInformation getESignedInformation() {
		//Tova se vika za da se inicializira esignedInformation!
		isESigned();
		return esignedInformation;
	}

	public Boolean getDataAuthentic() {
		return DataConverter.parseIntegerToBoolean(record.getDataAuthentic());
	}

    @Override
    public int getApplicantType() {
        return record.getApplicantType();
    }

    public Integer getApplicantCompanyId() {
        return record.getCompanyId();
    }
    public ExtCompany getApplicantCompany() {
        if (record.getCompanyId() == null) {
            return null;
        }
        return nacidDataProvider.getExtCompanyDataProvider().getCompany(record.getCompanyId());
    }

    public Boolean getDeputy() {
        return record.getDeputy();
    }

    public Integer getRepresentativeId() {
        return record.getRepresentativeId();
    }

    @Override
    public ExtPerson getRepresentative() {
        return record.getRepresentativeId() == null ? null : nacidDataProvider.getExtPersonDataProvider().getExtPerson(record.getRepresentativeId());
    }

    public String getRepresentativeType() {
        return record.getRepresentativeType();
    }

    public Integer getContactDetailsId() {
        return record.getContactDetailsId();
    }

    public ExtAddress getContactDetails() {
        if (getContactDetailsId() != null && contactDetails == null) {
            contactDetails = nacidDataProvider.getExtApplicationsDataProvider().getContactDetails(record.getContactDetailsId());
        }
        return contactDetails;
    }

    public Short getTypePayment() {
        return record.getTypePayment();
    }

    public Integer getDeliveryType() {
        return record.getDeliveryType();
    }

    public Boolean getDeclaration() {
        return record.getDeclaration();
    }

    public String getCourierNameAddress() {
        return record.getCourierNameAddress();
    }

    public String getOutgoingNumber() {
        return record.getOutgoingNumber();
    }

    public String getInternalNumber() {
        return record.getInternalNumber();
    }

    public Boolean getIsExpress() {
        return record.getIsExpress();
    }

    @Override
    public List<ExtApplicationKind> getApplicationKinds() {
        if (applicationKinds == null) {
            applicationKinds = nacidDataProvider.getExtApplicationsDataProvider().getApplicationKindsPerApplication(getId());
        }
        return applicationKinds;
    }


    @Override
    public int getApplicationType() {
        Set<Integer> statuteAuthenticityRecommendationEntryNums = Stream.of(NumgeneratorDataProvider.STATUTE_SERIES_ID, NumgeneratorDataProvider.AUTHENTICITY_SERIES_ID, NumgeneratorDataProvider.RECOMMENDATION_SERIES_ID).collect(Collectors.toSet());
        ExtApplicationKind applicationKind = getApplicationKinds().get(0);
        return NumgeneratorDataProvider.ENTRY_NUM_SERIES_TO_APPLICATION_TYPE.get(applicationKind.getEntryNumSeriesId());
    }



    @Override
    public com.nacid.bl.nomenclatures.FlatNomenclature getPaymentType() {
        return getTypePayment() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PAYMENT_TYPE, getTypePayment().intValue());
    }


    @Override
    public String getApplicationNumber() {
        return applicationNumber;
    }

    @Override
    public Date getApplicationDate() {
        return applicationDate;
    }

    @Override
    public String getDocFlowNumber() {
        return record.getEntryNum();
    }


    @Override
    public String getApplicantNames() {
        if (getApplicant() != null) {
            return getApplicant().getFullName();
        } else {
            return getApplicantCompany().getName();
        }
    }

    @Override
    public Integer getDocumentReceiveMethodId() {
        return record.getDocumentReceiveMethodId();
    }

    @Override
    public DocumentReceiveMethod getDocumentReceiveMethod() {
        return record.getDocumentReceiveMethodId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(record.getDocumentReceiveMethodId());
    }

    @Override
    public ExtDocumentRecipient getDocumentRecipient() {
        if (!documentRecipientRead) {
            documentRecipient = nacidDataProvider.getExtApplicationsDataProvider().getDocumentRecipient(record.getId());
            documentRecipientRead = true;
        }
        return documentRecipient;
    }
    @Override
    public Integer getApplicantPersonalIdDocumentTypeId() {
        return record.getApplicantPersonalIdDocumentType();
    }
    public FlatNomenclature getApplicantPersonalIdDocumentType() {
        return record.getApplicantPersonalIdDocumentType() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, record.getApplicantPersonalIdDocumentType());
    }
}
