package com.nacid.regprof.web.model.inquires;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.regprof.RegprofApplicationStatusFromCommonInquire;
import com.nacid.data.DataConverter;
import com.nacid.web.model.inquires.InquiresUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.nacid.web.model.inquires.CommonInquireWebModel.generateApplicationStatusesForCommonInquire;

public class CommonInquireWebModel {
	/**
	 * samo zaqvleniq minali prez komisiq
	 */
	private boolean eSubmited;
	private Boolean eSigned;
	private Date appDateFrom;
	private Date appDateTo;
	private List<Integer> representativeCompanyIds;
	private List<Integer> applicantCountryIds;
	private Map<Integer, List<Integer>> professionaInstitutions;
	private Date diplomaDateFrom;
	private Date diplomaDateTo;
	private Integer educationType;
	private List<Integer> secQualificationIds;
    private List<Integer> higherQualificationIds;
	
    private List<Integer> secSpecialityIds;
	private List<Integer> higherSpecialityIds;
	private List<Integer> sdkSpecialityIds;
	
	private List<Integer> recognizedHigherEduLevelIds;
	private List<Integer> recognizedSecondaryQualificationDegrees;
	private List<Integer> recogniedProfessions;
	private List<Integer> experienceProfessionIds;
	private Map<Integer, List<Integer>> directiveArticleIds;
	private List<RegprofApplicationStatusFromCommonInquire> applicationStatuses;
	private List<String> imiCorrespondences;
	private Integer serviceType;
	private Date serviceTypeDateTo;
    private Boolean apostilleApplication;


    private List<Integer> attachmentDocumentTypeIds;
    private List<Integer> educationDocumentTypeIds;
    private List<Integer> experienceDocumentTypeIds;
	public CommonInquireWebModel(HttpServletRequest request) {
		eSubmited = DataConverter.parseBoolean(request.getParameter("eSubmited"));
		Integer eSigned = DataConverter.parseInteger(request.getParameter("eSigned"), null);
		if (eSubmited && eSigned != null) {
			this.eSigned = DataConverter.parseBoolean(eSigned.toString());
		}
		
		
		
		representativeCompanyIds = InquiresUtils.generateElementList(request, "representativeCompanyIds", "representativeCompany");
		applicantCountryIds = InquiresUtils.generateElementList(request, "applicantCountryIds", "applicantCountry");
		
		int institutionsCount = DataConverter.parseInt(request.getParameter("institutions_count"), 0);
		for (int i = 1; i <= institutionsCount; i++) {
			Integer currentInstitutionId = DataConverter.parseInteger(request.getParameter("institution" + i + "NameId"), null);
			List<Integer> formerInstitutionNameIds = InquiresUtils.generateElementList(request, "institution" + i + "OldNameIds", "institution" + i + "OldName");
			if (currentInstitutionId != null || formerInstitutionNameIds != null) {
			    if (professionaInstitutions == null) {
			        professionaInstitutions = new HashMap<Integer, List<Integer>>();
			    }
			    List<Integer> formerInstitutionNamesByInstitution = professionaInstitutions.get(currentInstitutionId);
			    if (formerInstitutionNamesByInstitution != null && formerInstitutionNameIds != null) {
			        Set<Integer> set = new HashSet<Integer>(formerInstitutionNamesByInstitution);
			        set.addAll(formerInstitutionNameIds);
			        formerInstitutionNameIds = new ArrayList<Integer>(set);
			    }
			    professionaInstitutions.put(currentInstitutionId, formerInstitutionNameIds);
			}
		}
		
		educationType = DataConverter.parseInteger(request.getParameter("education_type"), null);
		
		
		if (educationType != null) {
		    if (educationType == EducationType.EDU_TYPE_SECONDARY || educationType == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL) {
	            /**Secondary Qualification **/
	            secQualificationIds = InquiresUtils.generateElementList(request, "secondaryQualificationIds", "secondaryQualificationId");
	            //Dobavqne na kvalifikaciite, izbrani s maska
	            secQualificationIds = InquiresUtils.addMaskedElements(request, "secondaryQualificationNamesIds", "secondaryQualification", secQualificationIds, NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_SPECIALITY);
	            
	            
	            /**Secondary Speciality **/
	            secSpecialityIds = InquiresUtils.generateElementList(request, "secondarySpecialityIds", "secondarySpecialityId");
	            //Dobavqne na specialnostite, izbrani s maska
	            secSpecialityIds = InquiresUtils.addMaskedElements(request, "secondarySpecialityNamesIds", "secondarySpeciality", secSpecialityIds, NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_PROFESSIONAL_QUALIFICATION);
	            
	            
	            recognizedSecondaryQualificationDegrees = InquiresUtils.generateElementList(request, "secondaryRecognizedQualificationDegreeIds", "secondaryRecognizedQualificationDegree");
	        } else {
	            /**Higher Qualification **/
	            higherQualificationIds = InquiresUtils.generateElementList(request, "higherQualificationIds", "higherQualificationId");
	            //Dobavqne na kvalifikaciite, izbrani s maska
	            higherQualificationIds = InquiresUtils.addMaskedElements(request, "higherQualificationNamesIds", "higherQualification", higherQualificationIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION);
	            
	            
	            List<Integer> specs;
	            /**Higher Speciality **/
	            specs = InquiresUtils.generateElementList(request, "higherSpecialityIds", "higherSpecialityId");
	            //Dobavqne na specialnostite, izbrani s maska
	            specs = InquiresUtils.addMaskedElements(request, "higherSpecialityNamesIds", "higherSpeciality", specs, NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY);
	            
	            if (educationType == EducationType.EDU_TYPE_HIGH) {
	                higherSpecialityIds = specs;
	            } else if (educationType == EducationType.EDU_TYPE_SDK) {
	                sdkSpecialityIds = specs;
	            }
	            
	            recognizedHigherEduLevelIds = InquiresUtils.generateElementList(request, "higherRecognizedEduLevelIds", "higherRecognizedEduLevel");
	            
	        }  
		}
		
		
		
		
		
		
		
		/** End of recognized speciality/qualification/edu level **/
		this.appDateFrom = DataConverter.parseDate(request.getParameter("appDateFrom"));
		this.appDateTo = DataConverter.parseDate(request.getParameter("appDateTo"));
		
		this.diplomaDateFrom = DataConverter.parseYear(request.getParameter("diplomaDateFrom"));
		this.diplomaDateTo = DataConverter.parseYear(request.getParameter("diplomaDateTo"));
		if (diplomaDateTo != null) {
		    Calendar cal = new GregorianCalendar();
		    cal.setTime(diplomaDateTo);
		    cal.add(Calendar.YEAR, 1);
		    cal.add(Calendar.SECOND, -1);
		    diplomaDateTo = cal.getTime();
		}
		
		this.experienceProfessionIds = InquiresUtils.generateElementList(request, "experienceProfessionIds", "experienceProfessionId");
        //dobavqne na profesiite, izbrani s maska!
		experienceProfessionIds = InquiresUtils.addMaskedElements(request, "experienceProfessionNamesIds", "experienceProfession", experienceProfessionIds, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE);
        
		
		this.recogniedProfessions = InquiresUtils.generateElementList(request, "recognizedProfessionIds", "recognizedProfessionId");
		//dobavqne na profesiite, izbrani s maska!
		recogniedProfessions = InquiresUtils.addMaskedElements(request, "recognizedProfessionNamesIds", "recognizedProfession", recogniedProfessions, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION);
		
		
		int articlesCount = DataConverter.parseInt(request.getParameter("directive_articles_count"), 0);
		for (int i = 0; i < articlesCount; i++) {
		    Integer articleId = DataConverter.parseInteger(request.getParameter("directive_article" + i), null);
		    if (articleId == null) {
                continue;
            }
		    if (directiveArticleIds == null) {
		        directiveArticleIds = new HashMap<Integer, List<Integer>>();
		    }
		    if (!directiveArticleIds.containsKey(articleId)) {
		        directiveArticleIds.put(articleId, null);   
		    }
		    List<Integer> lst = InquiresUtils.generateElementList(request, "directiveItem" + i +  "Ids", "directiveItem" + i);
		    if (lst != null) {
		        Set<Integer> set = new HashSet<Integer>();
		        set.addAll(lst);
		        if (directiveArticleIds.get(articleId) != null) {
		            set.addAll(directiveArticleIds.get(articleId));    
		        }
		        directiveArticleIds.put(articleId, new ArrayList<Integer>(set));
		    }
		}
        applicationStatuses = generateApplicationStatusesForCommonInquire(request).stream().map(r -> new RegprofApplicationStatusFromCommonInquire(r.isOnlyActualStatus(), r.getApplicationStatus() == null ? null : r.getApplicationStatus().getStatusId(), r.getApplicationStatusDateFrom(), r.getApplicationStatusDateTo(), r.getFinalApplicationStatus() == null ? null : r.getFinalApplicationStatus().getStatusId(), r.getFinalApplicationStatusDateFrom(), r.getFinalApplicationStatusDateTo(), r.getDocflowStatusId(), r.getDocflowStatusDateFrom(), r.getDocflowStatusDateTo())).collect(Collectors.toList());

		serviceType = DataConverter.parseInteger(request.getParameter("service_type"), null);
		serviceTypeDateTo = DataConverter.parseDate(request.getParameter("service_type_date"));
		
		//start of adding imiCorrespondences
		imiCorrespondences = new ArrayList<String>();
		String names = request.getParameter("imiCorrespondenceNamesIds");
        String param = request.getParameter("imiCorrespondence");
        
		if (!StringUtils.isEmpty(names)) {
            String[] namesArr = names.split(";");
            for (String s:namesArr) {
                imiCorrespondences.add(removeStarAtTheEnd(s));
            }
        }
        if (!StringUtils.isEmpty(param)) {
            imiCorrespondences.add(removeStarAtTheEnd(param));
        }
		imiCorrespondences = imiCorrespondences.size() == 0 ? null : imiCorrespondences;
        //end of adding imiCorrespondences

        attachmentDocumentTypeIds = InquiresUtils.generateElementList(request, "attachmentDocumentTypeIds", "attachmentDocumentType");

        this.apostilleApplication = DataConverter.parseIntegerToBoolean(DataConverter.parseInteger(request.getParameter("apostille_application"), null));

        educationDocumentTypeIds = InquiresUtils.generateElementList(request, "educationDocumentTypeIds", "educationDocumentType");
        experienceDocumentTypeIds = InquiresUtils.generateElementList(request, "experienceDocumentTypeIds", "experienceDocumentType");

	}
	private String removeStarAtTheEnd(String str) {
	    if (StringUtils.isEmpty(str)) {
	        return null;
	    }
	    if (str.endsWith("*")) {
	        return str.substring(0, str.length() - 1);
	    }
	    return str;
	}
    public boolean iseSubmited() {
        return eSubmited;
    }
    public void seteSubmited(boolean eSubmited) {
        this.eSubmited = eSubmited;
    }
    public Boolean geteSigned() {
        return eSigned;
    }
    public void seteSigned(Boolean eSigned) {
        this.eSigned = eSigned;
    }
    public Date getAppDateFrom() {
        return appDateFrom;
    }
    public void setAppDateFrom(Date appDateFrom) {
        this.appDateFrom = appDateFrom;
    }
    public Date getAppDateTo() {
        return appDateTo;
    }
    public void setAppDateTo(Date appDateTo) {
        this.appDateTo = appDateTo;
    }
    public List<Integer> getRepresentativeCompanyIds() {
        return representativeCompanyIds;
    }
    public void setRepresentativeCompanyIds(List<Integer> representativeCompanyIds) {
        this.representativeCompanyIds = representativeCompanyIds;
    }
    public List<Integer> getApplicantCountryIds() {
        return applicantCountryIds;
    }
    public void setApplicantCountryIds(List<Integer> applicantCountryIds) {
        this.applicantCountryIds = applicantCountryIds;
    }
    public Map<Integer, List<Integer>> getProfessionaInstitutions() {
        return professionaInstitutions;
    }
    public void setProfessionaInstitutions(
            Map<Integer, List<Integer>> professionaInstitutions) {
        this.professionaInstitutions = professionaInstitutions;
    }
    public Date getDiplomaDateFrom() {
        return diplomaDateFrom;
    }
    public void setDiplomaDateFrom(Date diplomaDateFrom) {
        this.diplomaDateFrom = diplomaDateFrom;
    }
    public Date getDiplomaDateTo() {
        return diplomaDateTo;
    }
    public void setDiplomaDateTo(Date diplomaDateTo) {
        this.diplomaDateTo = diplomaDateTo;
    }
    public Integer getEducationType() {
        return educationType;
    }
    public void setEducationType(Integer educationType) {
        this.educationType = educationType;
    }
    public List<Integer> getSecQualificationIds() {
        return secQualificationIds;
    }
    public void setSecQualificationIds(List<Integer> secQualificationIds) {
        this.secQualificationIds = secQualificationIds;
    }
    public List<Integer> getSecSpecialityIds() {
        return secSpecialityIds;
    }
    public void setSecSpecialityIds(List<Integer> secSpecialityIds) {
        this.secSpecialityIds = secSpecialityIds;
    }
    public List<Integer> getHigherQualificationIds() {
        return higherQualificationIds;
    }
    public void setHigherQualificationIds(List<Integer> higherQualificationIds) {
        this.higherQualificationIds = higherQualificationIds;
    }
    public List<Integer> getHigherSpecialityIds() {
        return higherSpecialityIds;
    }
    public void setHigherSpecialityIds(List<Integer> higherSpecialityIds) {
        this.higherSpecialityIds = higherSpecialityIds;
    }
    public List<Integer> getSdkSpecialityIds() {
        return sdkSpecialityIds;
    }
    public void setSdkSpecialityIds(List<Integer> sdkSpecialityIds) {
        this.sdkSpecialityIds = sdkSpecialityIds;
    }
    public List<Integer> getRecognizedHigherEduLevelIds() {
        return recognizedHigherEduLevelIds;
    }
    public void setRecognizedHigherEduLevelIds(
            List<Integer> recognizedHigherEduLevelIds) {
        this.recognizedHigherEduLevelIds = recognizedHigherEduLevelIds;
    }
    public List<Integer> getRecognizedSecondaryQualificationDegrees() {
        return recognizedSecondaryQualificationDegrees;
    }
    public void setRecognizedSecondaryQualificationDegrees(
            List<Integer> recognizedSecondaryQualificationDegrees) {
        this.recognizedSecondaryQualificationDegrees = recognizedSecondaryQualificationDegrees;
    }
    public List<Integer> getRecogniedProfessions() {
        return recogniedProfessions;
    }
    public void setRecogniedProfessions(List<Integer> recogniedProfessions) {
        this.recogniedProfessions = recogniedProfessions;
    }
    public Map<Integer, List<Integer>> getDirectiveArticleIds() {
        return directiveArticleIds;
    }
    public List<Integer> getExperienceProfessionIds() {
        return experienceProfessionIds;
    }
    public List<RegprofApplicationStatusFromCommonInquire> getApplicationStatuses() {
        return applicationStatuses;
    }
    public Integer getServiceType() {
        return serviceType;
    }
    public Date getServiceTypeDateTo() {
        return serviceTypeDateTo;
    }
    public List<String> getImiCorrespondences() {
        return imiCorrespondences;
    }

    public List<Integer> getAttachmentDocumentTypeIds() {
        return attachmentDocumentTypeIds;
    }

    public Boolean getApostilleApplication() {
        return apostilleApplication;
    }

    public List<Integer> getEducationDocumentTypeIds() {
        return educationDocumentTypeIds;
    }

    public List<Integer> getExperienceDocumentTypeIds() {
        return experienceDocumentTypeIds;
    }
}

