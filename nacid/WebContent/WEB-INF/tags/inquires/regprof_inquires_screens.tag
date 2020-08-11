<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires" prefix="inquires"%>
<fieldset><legend class="noForm">Екран</legend>
	   <c:forEach items="${screenWebModels}" var="screenWebModel">
	   <fieldset><legend class="noForm">Информация за заявление</legend>
	       <inquires:inquire_subelement name="Деловоден номер и дата:" value="${screenWebModel.docflowNumber}" test="${elements.docflowNumber}" />
	       <inquires:inquire_subelement name="Име, Презиме и Фамилия на Заявителя:" value="${screenWebModel.applicantNames}" test="${elements.applicantNames}" />
	       <inquires:inquire_subelement name="Електронно подадено:" value="${screenWebModel.electronicallyApplied ? 'Да' : 'Не'}" test="${elements.electronicallyApplied}" />
	       <inquires:inquire_subelement name="Тип на услугата:" value="${screenWebModel.serviceType}" test="${elements.serviceType}" />
	       <inquires:inquire_subelement name="Статус на заявлението:" value="${screenWebModel.status}" test="${elements.status}" />
	       <inquires:inquire_subelement name="Деловоден статус:" value="${screenWebModel.docflowStatus}" test="${elements.docflowStatus}" />
	       <inquires:inquire_subelement name="Гражданство на заявителя:" value="${screenWebModel.citizenshipName}" test="${elements.citizenshipName}" />
	       <inquires:inquire_subelement name="Кандидатства за държава:" value="${screenWebModel.applicationCountry}" test="${elements.applicationCountry}" />

           <c:if test="${screenWebModel.hasEducation}">
               <inquires:inquire_subelement name="Вид обучение:" value="${screenWebModel.educationType}" test="${elements.educationType}" />
               <inquires:inquire_subelement name="Година на издаване на дипломата:" value="${screenWebModel.documentDate}" test="${elements.documentDate}" />
               <c:choose>
                    <c:when test="${screenWebModel.secondaryEducationType}">
                        <inquires:inquire_subelement name="Обучаваща институция - ново име:" value="${screenWebModel.professionalInstitutionName}" test="${elements.professionalInstitutionName}" />
                        <inquires:inquire_subelement name="Обучаваща институция - старо име:" value="${screenWebModel.professionalInstitutionFormerName}" test="${elements.professionalInstitutionFormerName && not empty screenWebModel.professionalInstitutionFormerName}" />
                        <inquires:inquire_subelement name="Професионална квалификация по документ:" value="${screenWebModel.secondaryProfessionalQualification}" test="${elements.professionalQualification}" />
                        <inquires:inquire_subelement name="Специалност по документи:" value="${screenWebModel.secondarySpecialities}" test="${elements.specialities}" />
                    </c:when>
                   <c:when test="${screenWebModel.highEducationType}">
                       <inquires:inquire_subelement name="Обучаваща институция - ново име:" value="${screenWebModel.professionalInstitutionName}" test="${elements.professionalInstitutionName}" />
                       <inquires:inquire_subelement name="Обучаваща институция - старо име:" value="${screenWebModel.professionalInstitutionFormerName}" test="${elements.professionalInstitutionFormerName && not empty screenWebModel.professionalInstitutionFormerName}" />
                       <inquires:inquire_subelement name="Професионална квалификация по документ:" value="${screenWebModel.highProfessionalQualification}" test="${elements.professionalQualification}" />
                       <inquires:inquire_subelement name="Специалност по документи:" value="${screenWebModel.higherSpecialities}" test="${elements.specialities}" />
                   </c:when>
                   <c:when test="${screenWebModel.sdkEducationType}">
                       <inquires:inquire_subelement name="Обучаваща институция - ново име:" value="${screenWebModel.professionalInstitutionName}" test="${elements.professionalInstitutionName}" />
                       <inquires:inquire_subelement name="Обучаваща институция - старо име:" value="${screenWebModel.professionalInstitutionFormerName}" test="${elements.professionalInstitutionFormerName && not empty screenWebModel.professionalInstitutionFormerName}" />
                       <inquires:inquire_subelement name="Професионална квалификация по документ:" value="${screenWebModel.highProfessionalQualification}" test="${elements.professionalQualification}" />
                       <inquires:inquire_subelement name="Специалност по документи:" value="${screenWebModel.higherSpecialities}" test="${elements.specialities}" />


                       <inquires:inquire_subelement name="СДК - Обучаваща институция - нов име:" value="${screenWebModel.sdkProfessionalInstitutionName}" test="${elements.professionalInstitutionName}" />
                       <inquires:inquire_subelement name="СДК - Обучаваща институция - старо име:" value="${screenWebModel.sdkProfessionalInstitutionFormerName}" test="${elements.professionalInstitutionFormerName && not empty screenWebModel.sdkProfessionalInstitutionFormerName}" />
                       <inquires:inquire_subelement name="СДК - Професионална квалификация по документ:" value="${screenWebModel.sdkProfessionalQualification}" test="${elements.professionalQualification}" />
                       <inquires:inquire_subelement name="СДК - Специалност по документи:" value="${screenWebModel.sdkSpecialities}" test="${elements.specialities}" />
                   </c:when>
                </c:choose>
           </c:if>
           <c:if test="${screenWebModel.hasExperience}">
               <inquires:inquire_subelement name="Стаж - Професия:" value="${screenWebModel.professionExperienceName}" test="${elements.professionExperienceName}" />
               <inquires:inquire_subelement name="Стаж - документи:" value="${screenWebModel.professionExperienceDocumentsStr}" test="${elements.professionExperienceDocuments}" />
               <inquires:inquire_subelement name="Изчислен професионален стаж:" value="${screenWebModel.professionExperienceDuration}" test="${elements.professionExperienceDuration}" />

           </c:if>
	       <inquires:inquire_subelement name="Удостоверено квалификационно ниво:" value="${screenWebModel.recognizedEducationLevel}" test="${elements.recognizedEducationLevel}" />
	       <inquires:inquire_subelement name="Удостоверено квалификационно ниво:" value="${screenWebModel.recognizedQualificationDegree}" test="${elements.recognizedEducationLevel}" />
           <inquires:inquire_subelement name="По член от Директивата:" value="${screenWebModel.recognizedArticle}" test="${elements.recognizedArticle}" />
	       <inquires:inquire_subelement name="Удостоверена професия:" value="${screenWebModel.recognizedProfession}" test="${elements.recognizedProfession}" />
	       <inquires:inquire_subelement name="Свързани преписки по IMI:" value="${screenWebModel.imiCorrespondence}" test="${elements.imiCorrespondence}" />

        </fieldset>
        </c:forEach>
        ${emptyData}
</fieldset>

