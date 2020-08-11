<%@ tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ attribute name="attributePrefix" required="false" rtexprvalue="true"%>
<%@ attribute name="hideAttachments" required="false" rtexprvalue="true"%>
<%@ attribute name="attachmentPrefix" required="true" rtexprvalue="true"%>
<c:set var="appl_Id" scope="page" value="${attributePrefix}applicationId" />
<nacid_ext:applicationReportView attributePrefix="${attributePrefix }">
       <fieldset><legend class="noForm">Данни от заявлението</legend>
            <nacid_ext:applicationViewReportPerson person="applicant">
                <nacid_ext:report_subelement name="Тип персонален идентификатор" value="${person_civilid_type}"/>
                <nacid_ext:report_subelement name="Персонален идентификатор" value="${person_civil_id}"/>
                <nacid_ext:report_subelement name="Име" value="${person_first_name}"/>
                <nacid_ext:report_subelement name="Презиме" value="${person_middle_name}"/>
                <nacid_ext:report_subelement name="Фамилия" value="${person_last_name}"/>
                <nacid_ext:report_subelement name="${messages.birthCountry}" value="${person_birth_country}"/>
                <nacid_ext:report_subelement name="${messages.birthCity}" value="${person_birth_city}"/>
                <nacid_ext:report_subelement name="${messages.birthDate}" value="${personBirthDate}"/>
                <nacid_ext:report_subelement name="гражданин на" value="${person_citizenship}"/>
            </nacid_ext:applicationViewReportPerson>
               
            <c:if test="${show_diploma_names}">
                <fieldset><legend class="noForm">Данни от дипломата</legend>
                    <nacid_ext:report_subelement name="Име" value="${diploma_firstName}"/>
                    <nacid_ext:report_subelement name="Презиме" value="${diploma_middleName}"/>
                    <nacid_ext:report_subelement name="Фамилия" value="${diploma_lastName}"/>
                </fieldset>
            </c:if>
            <p>Съгласен съм да се ползват лични данни за целите на проверката: ${is_personal_data_usage ? "ДА" : "НЕ" }</p>
            <p>${doctorateApplication ? messages.dataAuthenticDeclarationDoctorate : messages.dataAuthenticDeclaration}: ${is_data_authentic ? "ДА" : "НЕ" }</p>
        </fieldset>
        <fieldset><legend class="noForm">За контакт</legend>
            <nacid_ext:report_subelement name="Електронна поща" value="${email}"/>
            <nacid_ext:report_subelement name="Държава" value="${home_country}"/>
            <nacid_ext:report_subelement name="Населено място" value="${home_city}"/>
            <nacid_ext:report_subelement name="Пощенски код" value="${home_post_code}"/>
            <nacid_ext:report_subelement name="Адрес" value="${home_address_details}"/>
            <nacid_ext:report_subelement name="Телефон" value="${homePhone}"/>
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
            <c:if test="${not empty email}">
                <p>Съгласен съм да получавам информация и уведомления на посочения електронен адрес: ${is_official_email_communication ? "ДА" : "НЕ" }</p>
                <div class="clr"><!--  --></div>
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
        <c:if test="${show_representative}">
            <fieldset><legend class="noForm">Лични данни на подателя</legend>
                 <nacid_ext:applicationViewReportPerson person="representative">
                    <nacid_ext:report_subelement name="Тип персонален идентификатор" value="${person_civilid_type}"/>
                    <nacid_ext:report_subelement name="Персонален идентификатор" value="${person_civil_id}"/>
                    <nacid_ext:report_subelement name="Име" value="${person_first_name}"/>
                    <nacid_ext:report_subelement name="Презиме" value="${person_middle_name}"/>
                    <nacid_ext:report_subelement name="Фамилия" value="${person_last_name}"/>
                    <nacid_ext:report_subelement name="Телефон" value="${reprPhone}"/>
                    <nacid_ext:report_subelement name="Държава" value="${reprCountry}"/>
                    <nacid_ext:report_subelement name="Град" value="${reprCity}"/>
                    <nacid_ext:report_subelement name="Пощенски код" value="${reprPcode}"/>
                    <nacid_ext:report_subelement name="Адрес" value="${reprAddressDetails}"/>
                    <nacid_ext:report_subelement name="Фирма" value="${reprCompany}"/>
                    <p>Адресът за контакт в България е на пълномощника: ${is_bg_address_onwer ? "ДА" : "НЕ" }</p>
                    <p>Декларирам, че заявлението за признаване на придобито висше образование в чуждестранно висше училище и документите към него са ми предоставени от заявителя: ${is_representative_authorized ? "ДА" : "НЕ" }</p>
                    <div class="clr"><!--  --></div>
                </nacid_ext:applicationViewReportPerson>
            </fieldset>
        </c:if>
        <div class="clr20"><!--  --></div>
        <nacid_ext:applicationReportTrainingCourseView attributePrefix="${attributePrefix}">
        <fieldset><legend class="noForm">Информация за курса на обучение</legend>
             <c:forEach items="${universities}" var="u">
                <fieldset><legend class="noForm">Данни за чуждестранното висше училище</legend>    
                    <nacid_ext:report_subelement name="Наименование на български на чуждестранното висше училище" value="${u.bgName}"/>
                    <nacid_ext:report_subelement name="Оригинално наименование" value="${u.orgName}"/>
                    <nacid_ext:report_subelement name="Държава" value="${u.country}"/>
                    <nacid_ext:report_subelement name="Град" value="${u.city}"/>
                    <nacid_ext:report_subelement name="Адрес за кореспонденция" value="${u.addrDetails}"/>
                </fieldset>
            </c:forEach>
            <div class="clr10"><!--  --></div>
            <nacid_ext:report_subelement name="Диплома серия" value="${diploma_series}"/>
            <nacid_ext:report_subelement name="Диплома номер" value="${diploma_number}"/>
            <nacid_ext:report_subelement name="Диплома регистрационен номер" value="${diploma_registration_number}"/>
            <nacid_ext:report_subelement name="Дата" value="${diploma_date}"/>
            <nacid_ext:report_subelement name="Вид диплома" value="${diploma_type}"/>
        </fieldset>
        <fieldset><legend class="noForm">Обучение</legend>
            <nacid_ext:trainingCourseTrainingLocationForReportView attributePrefix="${attributePrefix }">
            <fieldset><legend class="noForm">Място на провеждане</legend>
                <nacid_ext:report_subelement name="Държава" value="${training_location_country}"/>
                <nacid_ext:report_subelement name="Град" value="${training_location_city}"/>
            </fieldset>
            </nacid_ext:trainingCourseTrainingLocationForReportView>
            <nacid_ext:report_subelement name="Специалност" value="${speciality}"/>
            <nacid_ext:report_subelement name="Начало на обучението" value="${training_start}"/>
            <nacid_ext:report_subelement name="Край на обучението" value="${training_end}"/>
            <nacid_ext:report_subelement name="Продължителност" value="${training_duration}"/>
            <nacid_ext:report_subelement name="Форма на обучение" value="${training_form}"/>
            <nacid_ext:report_subelement name="Начин на дипломиране" value="${graduation_ways}"/>
            <nacid_ext:report_subelement name="Получена степен" value="${edu_level_name}"/>
            <nacid_ext:report_subelement name="Придобити кредити" value="${credits}"/>
            <nacid_ext:report_subelement name="Придобита квалификация" value="${qualification}"/>
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
            <nacid_ext:report_subelement name="Държава" value="${prevDiplCountry}"/>
            <nacid_ext:report_subelement name="Град" value="${prevDiplCity}"/>
            <nacid_ext:report_subelement name="Придобита степен" value="${prevDiplEduLevel}"/>
            <nacid_ext:report_subelement name="Специалност" value="${prevDiplSpeciality}"/>
            <nacid_ext:report_subelement name="Година на дипломиране" value="${prevDiplGraduationDate}"/>
            <nacid_ext:report_subelement name="Бележки" value="${prevDiplNotes}"/>
        </fieldset>
        </c:if>
        <nacid_ext:report_subelement name="Цел на признаването" value="${recognition_purposes}"/>
        </nacid_ext:applicationReportTrainingCourseView>
        <c:if test="${show_application_attachments && not hideAttachments}">
            <fieldset><legend class="noForm">Прикачени документи</legend>
            <nacid_ext:applicationAttachmentsList type="applications" attributePrefix="${attributePrefix}">
                <a target="_blank" href="${attachmentPrefix }/${file_name }?attachment_id=${id }&amp;att_type=${type}&amp;application_id=${requestScope[appl_Id]}" >${document_type }</a><br />
            </nacid_ext:applicationAttachmentsList>
            </fieldset>
        </c:if>
</nacid_ext:applicationReportView>
