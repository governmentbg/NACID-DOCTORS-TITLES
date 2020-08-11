<%@ tag import="com.nacid.web.ApplicantTypeHelper" %>
<%@ tag import="com.nacid.bl.nomenclatures.ApplicationType" %>
<%@ tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="attributePrefix" required="false" rtexprvalue="true"%>
<%@ attribute name="attachmentPrefix" required="true" rtexprvalue="true"%>
<c:set var="appl_Id" scope="page" value="${attributePrefix}applicationId" />
<nacid_ext:extApplicationReportView attributePrefix="${attributePrefix }">
    <c:set var="attributeName" value="${attributePrefix}applicationForReportWebModel"/>
    <c:set var="application" value="${requestScope[attributeName]}"/>
       <c:set var="sarApplicationType" value="<%=ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE%>" />
       <c:if test="${application.applicationType == sarApplicationType}">
       <fieldset><legend class="noForm">Вид Услуга</legend>
           <table>
            <tr>
                <th width="200" align="right">Вид услуга</th>
                <th width="250" align="right">Номер</th>
                <th width="100" align="right">Цена</th>
            </tr>
           <c:forEach var="item" items="${application.applicationKinds}">
            <tr>
                <td align="right">${item.entryNumSeriesName}</td>
                <td align="right">${item.entryNum}</td>
                <td align="right">${item.price}</td>
            </tr>
           </c:forEach>
           </table>
           <nacid_ext:report_subelement name="Бърза услуга" value="${application.isExpress}"/>
           <nacid_ext:report_subelement name="Ваш изходящ номер към нацид" value="${application.outgoingNumber}"/>
           <nacid_ext:report_subelement name="Входящ номер на вашия заявител" value="${application.internalNumber}"/>
           <nacid_ext:report_subelement name="Начин на плащане" value="${application.paymentType}"/>
           <nacid_ext:report_subelement name="Начин на доставка" value="${application.deliveryType}"/>
       </fieldset>
       </c:if>
       <fieldset><legend class="noForm">Заявител</legend>
           <nacid_ext:report_subelement name="Тип заявител" value="${applicant_type}"/>
           <c:if test="${not empty requestScope[attributeName].applicantCompany}">
               <c:set var="company" value="${requestScope[attributeName].applicantCompany}"/>
               <nacid_ext:report_subelement name="Булстат" value="${company.eik}"/>
               <nacid_ext:report_subelement name="Име" value="${company.name}"/>
               <nacid_ext:report_subelement name="Държава" value="${company.countryName}"/>
               <nacid_ext:report_subelement name="Град" value="${company.city.name}"/>
               <nacid_ext:report_subelement name="Град" value="${company.cityName}"/>
               <nacid_ext:report_subelement name="Пощенски код" value="${company.postCode}"/>
               <nacid_ext:report_subelement name="Адрес" value="${company.addressDetails}"/>
               <nacid_ext:report_subelement name="Телефон" value="${company.phone}"/>
            </c:if>
           <c:if test="${not empty requestScope[attributeName].applicant}">
               <c:set var="applicant" value="${requestScope[attributeName].applicant}"/>
               <nacid_ext:report_subelement name="Тип персонален идентификатор" value="${applicant.civilIdType}"/>
               <nacid_ext:report_subelement name="Персонален идентификатор" value="${applicant.civilId}"/>
               <nacid_ext:report_subelement name="Име" value="${applicant.firstName}"/>
               <nacid_ext:report_subelement name="Презиме" value="${applicant.surName}"/>
               <nacid_ext:report_subelement name="Фамилия" value="${applicant.lastName}"/>
               <nacid_ext:report_subelement name="${messages.birthCountry}" value="${applicant.birthCountry}"/>
               <nacid_ext:report_subelement name="${messages.birthCity}" value="${applicant.birthCity}"/>
               <nacid_ext:report_subelement name="${messages.birthDate}" value="${applicant.birthDate}"/>
               <nacid_ext:report_subelement name="гражданин на" value="${applicant.citizenship}"/>
           </c:if>
       </fieldset>

    <c:set var="representative" value="${requestScope[attributeName].representative}" />
    <c:if test="${not empty representative}">
    <fieldset><legend class="noForm">Представител</legend>
        <nacid_ext:report_subelement name="В качеството му на/длъжност" value="${application.representativeType}"/>
        <nacid_ext:report_subelement name="Тип персонален идентификатор" value="${representative.civilIdType}"/>
        <nacid_ext:report_subelement name="Персонален идентификатор" value="${representative.civilId}"/>
        <nacid_ext:report_subelement name="Име" value="${representative.firstName}"/>
        <nacid_ext:report_subelement name="Презиме" value="${representative.surName}"/>
        <nacid_ext:report_subelement name="Фамилия" value="${representative.lastName}"/>
        <nacid_ext:report_subelement name="${messages.birthCountry}" value="${representative.birthCountry}"/>
        <nacid_ext:report_subelement name="${messages.birthCity}" value="${representative.birthCity}"/>
        <nacid_ext:report_subelement name="${messages.birthDate}" value="${representative.birthDate}"/>
        <nacid_ext:report_subelement name="гражданин на" value="${representative.citizenship}"/>
    </fieldset>
    </c:if>
       <fieldset><legend class="noForm">Данни от диплома</legend>
           <c:set var="owner" value="${requestScope[attributeName].trainingCourseWebModel.owner}"/>

           <nacid_ext:report_subelement name="Тип персонален идентификатор" value="${owner.civilIdType}"/>
           <nacid_ext:report_subelement name="Персонален идентификатор" value="${owner.civilId}"/>
           <nacid_ext:report_subelement name="Име" value="${owner.firstName}"/>
           <nacid_ext:report_subelement name="Презиме" value="${owner.surName}"/>
           <nacid_ext:report_subelement name="Фамилия" value="${owner.lastName}"/>
           <nacid_ext:report_subelement name="${messages.birthCountry}" value="${owner.birthCountry}"/>
           <nacid_ext:report_subelement name="${messages.birthCity}" value="${owner.birthCity}"/>
           <nacid_ext:report_subelement name="${messages.birthDate}" value="${owner.birthDate}"/>
           <nacid_ext:report_subelement name="гражданин на" value="${owner.citizenship}"/>

	        <c:if test="${show_diploma_names}">
	            <fieldset><legend class="noForm">Имена по диплома</legend>
	                <nacid_ext:report_subelement name="Име" value="${diploma_firstName}"/>
	                <nacid_ext:report_subelement name="Презиме" value="${diploma_middleName}"/>
	                <nacid_ext:report_subelement name="Фамилия" value="${diploma_lastName}"/>
	            </fieldset>
	        </c:if>
	       <c:if test="${is_personal_data_usage != null}">
            <p>Съгласен съм да се ползват лични данни за целите на проверката: ${is_personal_data_usage ? "ДА" : "НЕ" }</p>
           </c:if>
           <c:if test="${is_data_authentic != null}">
            <p>${doctorateApplication ? messages.dataAuthenticDeclarationDoctorate : messages.dataAuthenticDeclaration}: ${is_data_authentic ? "ДА" : "НЕ" }</p>
           </c:if>
        </fieldset>

        <c:set var="contactDetails" value="${application.contactDetails}" />
        <c:choose>
            <c:when test="${not empty contactDetails}">
                <fieldset><legend class="noForm">За контакт</legend>
                    <nacid_ext:report_subelement name="Електронна поща" value="${contactDetails.email}"/>
                    <nacid_ext:report_subelement name="Държава" value="${contactDetails.countryName}"/>
                    <nacid_ext:report_subelement name="Населено място" value="${contactDetails.cityName}"/>
                    <nacid_ext:report_subelement name="Населено място" value="${contactDetails.foreignCity}"/>
                    <nacid_ext:report_subelement name="Пощенски код" value="${contactDetails.postalCode}"/>
                    <nacid_ext:report_subelement name="Адрес" value="${contactDetails.address}"/>
                    <nacid_ext:report_subelement name="Телефон" value="${contactDetails.phone}"/>
                    <nacid_ext:report_subelement name="Факс" value="${contactDetails.fax}"/>
                    <nacid_ext:report_subelement name="Начин на получаване на уведомления" value="${document_receive_method}"/>
                    <c:if test="${not empty document_recipient_data}">
                        <fieldset><legend class="noForm">Адрес на получател</legend>
                            <nacid_ext:report_subelement name="Име" value="${document_recipient_name}"/>
                            <nacid_ext:report_subelement name="Улица №" value="${document_recipient_address}"/>
                            <nacid_ext:report_subelement name="Област" value="${document_recipient_district}"/>
                            <nacid_ext:report_subelement name="Пощенски код" value="${document_recipient_post_code}"/>
                            <nacid_ext:report_subelement name="Град" value="${document_recipient_city}"/>
                            <nacid_ext:report_subelement name="Държава" value="${document_recipient_country}"/>
                            <nacid_ext:report_subelement name="Мобилен телефон" value="${document_recipient_mobile_phone}"/>
                        </fieldset>
                    </c:if>
                </fieldset>
            </c:when>
            <c:otherwise>
                <fieldset><legend class="noForm">За контакт</legend>
                    <nacid_ext:report_subelement name="Електронна поща" value="${email}"/>
                    <nacid_ext:report_subelement name="Държава" value="${home_country}"/>
                    <nacid_ext:report_subelement name="Населено място" value="${home_city}"/>
                    <nacid_ext:report_subelement name="Пощенски код" value="${home_post_code}"/>
                    <nacid_ext:report_subelement name="Адрес" value="${home_address_details}"/>
                    <nacid_ext:report_subelement name="Телефон" value="${homePhone}"/>
                    <nacid_ext:report_subelement name="Начин на получаване на уведомления " value="${document_receive_method}"/>
                    <c:if test="${not empty document_recipient_data}">
                        <fieldset><legend class="noForm">Адрес на получател</legend>
                            <nacid_ext:report_subelement name="Име" value="${document_recipient_name}"/>
                            <nacid_ext:report_subelement name="Улица №" value="${document_recipient_address}"/>
                            <nacid_ext:report_subelement name="Област" value="${document_recipient_district}"/>
                            <nacid_ext:report_subelement name="Пощенски код" value="${document_recipient_post_code}"/>
                            <nacid_ext:report_subelement name="Град" value="${document_recipient_city}"/>
                            <nacid_ext:report_subelement name="Държава" value="${document_recipient_country}"/>
                            <nacid_ext:report_subelement name="Мобилен телефон" value="${document_recipient_mobile_phone}"/>
                        </fieldset>
                    </c:if>
                </fieldset>
                <c:if test="${!home_is_bg}">
                    <fieldset><legend class="noForm">За контакт в България</legend>
                        <nacid_ext:report_subelement name="Населено място" value="${bg_city}"/>
                        <nacid_ext:report_subelement name="Пощенски код" value="${bg_post_code}"/>
                        <nacid_ext:report_subelement name="Адрес" value="${bg_address_details}"/>
                        <nacid_ext:report_subelement name="Телефон" value="${bgPhone}"/>
                    </fieldset>
                </c:if>
            </c:otherwise>
        </c:choose>

        
        <div class="clr20"><!--  --></div>
        <nacid_ext:extApplicationReportTrainingCourseView attributePrefix="${attributePrefix}">
        <fieldset><legend class="noForm">Информация за курса на обучение</legend>
            
            <c:forEach items="${universities}" var="u">
                <fieldset><legend class="noForm">Данни за чуждестранното висше училище</legend>    
                    <nacid_ext:report_subelement name="Наименование на български на чуждестранното висше училище" value="${u.bgName}"/>
                    <nacid_ext:report_subelement name="Оригинално наименование" value="${u.orgName}"/>
                    <nacid_ext:report_subelement name="Държава" value="${u.country}"/>
                    <nacid_ext:report_subelement name="Град" value="${u.city}"/>
                    <nacid_ext:report_subelement name="Адрес за кореспонденция" value="${u.addrDetails}"/>
                    <nacid_ext:report_subelement name="Наименование на български на чуждестранното висше училище (ново)" value="${u.universityTxt}"/>
                </fieldset>
            </c:forEach>
            <div class="clr10"><!--  --></div>
            <nacid_ext:report_subelement name="Диплома серия" value="${diploma_series}"/>
            <nacid_ext:report_subelement name="Диплома номер" value="${diploma_number}"/>
            <nacid_ext:report_subelement name="Диплома регистрационен номер" value="${diploma_registration_number}"/>
            <nacid_ext:report_subelement name="Дата" value="${diploma_date}"/>
            
        </fieldset>
        <fieldset><legend class="noForm">Обучение</legend>
            <nacid_ext:extTrainingCourseTrainingLocationForReportView attributePrefix="${attributePrefix }">
            <fieldset><legend class="noForm">Място на провеждане</legend>
                <nacid_ext:report_subelement name="Държава" value="${training_location_country}"/>
                <nacid_ext:report_subelement name="Град" value="${training_location_city}"/>
            </fieldset>
            </nacid_ext:extTrainingCourseTrainingLocationForReportView>
            <nacid_ext:report_subelement name="Специалност" value="${speciality}"/>
            <nacid_ext:report_subelement name="Специалност (ново)" value="${speciality_txt}"/>
            <nacid_ext:report_subelement name="Начало на обучението" value="${training_start}"/>
            <nacid_ext:report_subelement name="Край на обучението" value="${training_end}"/>
            <nacid_ext:report_subelement name="Продължителност" value="${training_duration}"/>
            <nacid_ext:report_subelement name="Форма на обучение" value="${training_form}"/>
            <nacid_ext:report_subelement name="Начин на дипломиране" value="${graduation_ways}"/>
            <nacid_ext:report_subelement name="Получена степен" value="${edu_level_name}"/>
            <nacid_ext:report_subelement name="Придобити кредити" value="${credits}"/>
            <nacid_ext:report_subelement name="Придобита квалификация" value="${qualification}"/>
            <nacid_ext:report_subelement name="Придобита квалификация (ново)" value="${qualification_txt}"/>
        </fieldset>
        <c:if test="${doctorateApplication}">
            <fieldset><legend class="noForm">Дисертация</legend>
                <nacid_ext:report_subelement name="Тема на дисертацията" value="${thesisTopic}"/>
                <nacid_ext:report_subelement name="Тема на дисертацията на английски" value="${thesisTopicEn}"/>
                <nacid_ext:report_subelement name="Дата на защитата" value="${thesisDefenceDate}"/>
                <nacid_ext:report_subelement name="Език на основния текст" value="${thesisLanguage}"/>
                <nacid_ext:report_subelement name="Библиография(бр. заглавия)" value="${thesisBibliography}"/>
                <nacid_ext:report_subelement name="Обем на дисертационния труд(бр. страници)" value="${thesisVolume}"/>
                <nacid_ext:report_subelement name="Анотация на български език" value="${thesisAnnotation}"/>
                <nacid_ext:report_subelement name="Анотация на английски език " value="${thesisAnnotationEn}"/>
                <nacid_ext:report_subelement name="Професионално направление по заявление" value="${profGroupName}"/>
                <nacid_ext:report_subelement name="Област на образованието " value="${eduAreaName}"/>

            </fieldset>

        </c:if>
        <c:if test="${is_school_diploma_preset}">
        <fieldset><legend class="noForm">Диплома за средно образование</legend>
            <nacid_ext:report_subelement name="Държава" value="${schoolCountry}"/>
            <nacid_ext:report_subelement name="Град" value="${schoolCity}"/>
            <nacid_ext:report_subelement name="Училище" value="${schoolName}"/>
            <nacid_ext:report_subelement name="Година на дипломиране" value="${schoolGraduationDate}"/>
            <nacid_ext:report_subelement name="Бележки" value="${schoolNotes}"/>
        </fieldset>
        </c:if>
         <c:if test="${is_previous_bg_diploma_preset}">
        <fieldset><legend class="noForm">Диплома за предходна образователна степен за висше образование</legend>
            <nacid_ext:report_subelement name="Висше училище" value="${prevDiplUniversityName}"/>
            <nacid_ext:report_subelement name="Висше училище (ново)" value="${prev_diploma_university_txt}"/>
            <nacid_ext:report_subelement name="Държава" value="${prevDiplCountry}"/>
            <nacid_ext:report_subelement name="Град" value="${prevDiplCity}"/>
            <nacid_ext:report_subelement name="Придобита степен" value="${prevDiplEduLevel}"/>
            <nacid_ext:report_subelement name="Специалност" value="${prevDiplSpeciality}"/>
            <nacid_ext:report_subelement name="Специалност (ново)" value="${prevDiplSpecialityTxt}"/>
            <nacid_ext:report_subelement name="Година на дипломиране" value="${prevDiplGraduationDate}"/>
            <nacid_ext:report_subelement name="Бележки" value="${prevDiplNotes}"/>
        </fieldset>
        </c:if>
        <nacid_ext:report_subelement name="Цел на признаването" value="${recognition_purposes}"/>
        </nacid_ext:extApplicationReportTrainingCourseView>
        <c:if test="${show_application_attachments}">
            <fieldset><legend class="noForm">Прикачени документи</legend>
            <nacid_ext:applicationAttachmentsList type="applications" attributePrefix="${attributePrefix}">
                <a target="_blank" href="${attachmentPrefix }/${file_name }?attachment_id=${id }&amp;att_type=${type}&amp;application_id=${requestScope[appl_Id]}" >${document_type }</a><br />
            </nacid_ext:applicationAttachmentsList>
            </fieldset>
        </c:if>
        <nacid_ext:extApplicationReportESignInformation>
            <fieldset><legend class="noForm">Електронно подписани данни</legend>
                <nacid_ext:report_subelement name="Издател" value="${issuer}"/>
                <nacid_ext:report_subelement name="Имена" value="${name}"/>
                <nacid_ext:report_subelement name="email" value="${email}"/>
                <nacid_ext:report_subelement name="ЕГН/ЛНЧ" value="${civilId}"/>
                <nacid_ext:report_subelement name="ЕИК" value="${unifiedIdCode}"/>
                <nacid_ext:report_subelement name="Валиден от" value="${validityFrom}"/>
                <nacid_ext:report_subelement name="Валиден до" value="${validityTo}"/>
            </fieldset>
        </nacid_ext:extApplicationReportESignInformation>
            
            
        
</nacid_ext:extApplicationReportView>
