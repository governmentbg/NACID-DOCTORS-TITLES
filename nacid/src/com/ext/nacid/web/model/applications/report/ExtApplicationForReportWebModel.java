package com.ext.nacid.web.model.applications.report;

import com.ext.nacid.web.model.applications.ExtAddressWebModel;
import com.ext.nacid.web.model.applications.ExtApplicationKindWebModel;
import com.ext.nacid.web.model.applications.ExtDocumentRecipientWebModel;
import com.ext.nacid.web.model.applications.ExtESignedInformationWebModel;
import com.nacid.bl.external.ExtAddress;
import com.nacid.bl.external.ExtApplicationKind;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.applications.*;
import com.nacid.web.DeliveryTypeHelper;
import com.nacid.web.model.applications.base.DocumentRecipientBaseWebModel;
import com.nacid.web.model.applications.report.base.ApplicationForReportBaseWebModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExtApplicationForReportWebModel extends ApplicationForReportBaseWebModel {
	private ExtESignedInformationWebModel esignedInformation;
    private ExtAddressWebModel contactDetails;
    private List<ExtApplicationKindWebModel> applicationKinds;
    private String representativeType;
    private String outgoingNumber;
    private String internalNumber;
    private String paymentType;
    private String deliveryType;
    private String isExpress;
    private int applicationType;
    private boolean differentApplicantRepresentative;

	public ExtApplicationForReportWebModel(ExtApplication a, ExtTrainingCourseDataProvider extTrainingCourseDataProvider) {
		super(a);
		ExtPerson person = a.getApplicant();
        this.applicant = person == null ? null : new ExtPersonForReportWebModel(person);
        ExtPerson repr = a.getRepresentative();
        this.representative = repr == null ? null : new ExtPersonForReportWebModel(repr);
        this.email = person == null ? null : person.getEmail();
		ExtTrainingCourse tc = a.getTrainingCourse();
        this.trainingCourseWebModel = tc == null ? null : new ExtTrainingCourseForReportWebModel(tc, extTrainingCourseDataProvider);
        
        
        
        List<String> lst = new ArrayList<String>();
		List<ExtPurposeOfRecognition> recognitionPurposesList = extTrainingCourseDataProvider.getExtPurposesOfRecognition(a.getId());
		if (recognitionPurposesList == null) {
			this.recognitionPurposes = "";
		} else {
			for (ExtPurposeOfRecognition gw:recognitionPurposesList) {
				lst.add(gw.getRecognitionPurposeNotes());
			}
			this.recognitionPurposes = StringUtils.join(lst, ", ");	
		}
		ExtESignedInformation info = a.getESignedInformation();
		if (info != null) {
			this.esignedInformation = new ExtESignedInformationWebModel(info);
		}
        this.applicantCompany = a.getApplicantCompanyId() == null ? null : new ExtCompanyForReportWebModel(a.getApplicantCompany());
        ExtAddress contactDetails =  a.getContactDetails();
        this.contactDetails = contactDetails == null ? null : new ExtAddressWebModel(contactDetails);
        List<ExtApplicationKind> applicationKindList = a.getApplicationKinds();
        if (applicationKindList != null && applicationKindList.size() > 0) {
            applicationKinds = new ArrayList<ExtApplicationKindWebModel>();
            for (ExtApplicationKind extApplicationKind : applicationKindList) {
                applicationKinds.add(new ExtApplicationKindWebModel(extApplicationKind));
            }
        }
        this.representativeType = a.getRepresentativeType();
        this.outgoingNumber = a.getOutgoingNumber();
        this.internalNumber = a.getInternalNumber();
        this.isExpress = a.getIsExpress() == null ? null : (a.getIsExpress() ? "Да" : "Не");
        this.paymentType = a.getPaymentType() == null ? null : a.getPaymentType().getName();
        this.deliveryType = DeliveryTypeHelper.getDeliveryTypeName(a.getDeliveryType());
        this.applicationType = a.getApplicationType();
        this.documentRecipient = a.getDocumentRecipient() == null ? null : new ExtDocumentRecipientWebModel(a.getDocumentRecipient());
        this.differentApplicantRepresentative = !Objects.equals(a.getRepresentativeId(), a.getApplicantId());

	}
	public ExtPersonForReportWebModel getApplicant() {
		return (ExtPersonForReportWebModel) applicant;
	}
	public ExtTrainingCourseForReportWebModel getTrainingCourseWebModel() {
		return (ExtTrainingCourseForReportWebModel) trainingCourseWebModel;
	}
	public ExtESignedInformationWebModel getEsignedInformation() {
		return esignedInformation;
	}

    public ExtCompanyForReportWebModel getApplicantCompany() {
        return (ExtCompanyForReportWebModel)applicantCompany;
    }

    public ExtPersonForReportWebModel getRepresentative() {
        return (ExtPersonForReportWebModel)representative;
    }

    public ExtAddressWebModel getContactDetails() {
        return contactDetails;
    }

    public List<ExtApplicationKindWebModel> getApplicationKinds() {
        return applicationKinds;
    }

    public String getRepresentativeType() {
        return representativeType;
    }

    public String getOutgoingNumber() {
        return outgoingNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public String getIsExpress() {
        return isExpress;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public int getApplicationType() {
        return applicationType;
    }

    @Override
    public ExtDocumentRecipientWebModel getDocumentRecipient() {
        return (ExtDocumentRecipientWebModel) documentRecipient;
    }

    public boolean isDifferentApplicantRepresentative() {
        return differentApplicantRepresentative;
    }
}
