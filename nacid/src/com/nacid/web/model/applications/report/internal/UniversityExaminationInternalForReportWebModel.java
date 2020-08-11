package com.nacid.web.model.applications.report.internal;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.applications.CompetentInstitutionDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityExamination;
import com.nacid.bl.applications.UniversityValidity;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.model.applications.report.base.AttachmentForReportBaseWebModel;

public class UniversityExaminationInternalForReportWebModel {
	private String uniBgName;
	private String uniOriginalName;
	private String examinationDate;
	private boolean isRecognized;
	private boolean communicated;
	private boolean hasJointDegree;
	private String trainingLocation;
	private String notes;
	private String trainingForm;
	private boolean isExaminated;
	private String universityValidatyNotes;
	private List<CompetentInstitutionInternalForReportWebModel> competentInstitutions;
	private List<AttachmentForReportBaseWebModel> attachments;

	public UniversityExaminationInternalForReportWebModel(University u, UniversityExamination ex, NacidDataProvider nacidDataProvider) {
		this.uniBgName = u.getBgName();
		this.uniOriginalName = u.getOrgName();
		isExaminated = ex != null;
		if (!isExaminated) {
			return;
		}
		this.notes = ex.getNotes();
		
		// examinationDate i isRecognized se vzemat ot UniversityExamination,
		// tui kato moje da sa razli4ni ot tezi v universityValidity, a
		// communicated se vzema ot UniversityValidity
		this.examinationDate = DataConverter.formatDate(ex.getExaminationDate(), "");
		this.isRecognized = ex.isRecognized();
		UniversityValidity validity = ex.getUniversityValidity();

		if (validity != null) {
			this.universityValidatyNotes = validity.getNotes();
			this.communicated = validity.isComunicated();
			this.hasJointDegree = validity.isHasJoinedDegrees();
			FlatNomenclature trainingLocation = validity.getTrainingLocation();
			this.trainingLocation = trainingLocation == null ? "" : trainingLocation.getName();
			if (hasJointDegree) {
				this.trainingLocation += ("".equals(this.trainingLocation) ? "" : "; ") + "съвместна образователна програма";
			}

			// Generating training forms
			Object[] trainingForms = validity.getTrainingForms();
			if (trainingForms == null) {
				trainingForm = "";
			} else {
				NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
				for (Object tf : trainingForms) {
					String current;
					if (tf instanceof Integer) {
						current = nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM,
								(Integer) tf).getName();
					} else {
						current = tf.toString();
					}
					if (trainingForm == null) {
						trainingForm = current;
					} else {
						trainingForm = trainingForm + ", " + current;
					}

				}
			}

			// Adding competent institutions
			CompetentInstitutionDataProvider competentInstitutionDataProvider = nacidDataProvider.getCompetentInstitutionDataProvider();
			List<CompetentInstitution> competentInstitutions = competentInstitutionDataProvider.getCompetentInstitutionsByUniversityValidityId(
					validity.getId(), false);
			if (competentInstitutions != null) {
				this.competentInstitutions = new ArrayList<CompetentInstitutionInternalForReportWebModel>();
				for (CompetentInstitution i : competentInstitutions) {
					this.competentInstitutions.add(new CompetentInstitutionInternalForReportWebModel(i));
				}
			}

			// adding attached dogs of university validity
			AttachmentDataProvider uniExamAttachmentDataProvider = nacidDataProvider.getUniExamAttachmentDataProvider();
			List<? extends Attachment> uniValidityAttachments = uniExamAttachmentDataProvider.getAttachmentsForParent(validity.getId());
			if (uniValidityAttachments != null && uniValidityAttachments.size() > 0) {
				attachments = new ArrayList<AttachmentForReportBaseWebModel>();
				for (Attachment a : uniValidityAttachments) {
					attachments.add(new AttachmentForReportBaseWebModel(a));
				}
			}

		}

	}

	public String getExaminationDate() {
		return examinationDate;
	}

	public boolean isRecognized() {
		return isRecognized;
	}

	public boolean isCommunicated() {
		return communicated;
	}

	public String getTrainingLocation() {
		return trainingLocation;
	}

	public String getNotes() {
		return notes;
	}

	public String getTrainingForm() {
		return trainingForm;
	}

	public boolean isExaminated() {
		return isExaminated;
	}

	public String getUniBgName() {
		return uniBgName;
	}

	public String getUniOriginalName() {
		return uniOriginalName;
	}

	public List<CompetentInstitutionInternalForReportWebModel> getCompetentInstitutions() {
		return competentInstitutions;
	}

	public List<AttachmentForReportBaseWebModel> getAttachments() {
		return attachments;
	}

	public String getUniversityValidatyNotes() {
		return universityValidatyNotes;
	}
}
