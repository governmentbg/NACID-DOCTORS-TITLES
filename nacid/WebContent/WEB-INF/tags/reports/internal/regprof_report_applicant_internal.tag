<%@ tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_regprof_nacid_taglib.tld" prefix="regprof_ext"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ attribute name="showCertificate" required="false" rtexprvalue="true"%>
<%@ attribute name="attributePrefix" required="false" rtexprvalue="true"%>
<%@ attribute name="hideAttachments" required="false" rtexprvalue="true"%>
<%@ attribute name="attachmentPrefix" required="true" rtexprvalue="true"%>

<c:set var="appl_Id" scope="page" value="${attributePrefix}applicationId" />
<regprof_ext:applicationReportView attributePrefix="${attributePrefix }">
		<fieldset><legend class="noForm">Данни от заявлението</legend>
            <regprof_ext:applicationViewReportPerson person="applicant">
                <nacid_ext:report_subelement name="Тип персонален идентификатор" value="${person_civilid_type}"/>
                <nacid_ext:report_subelement name="Персонален идентификатор" value="${person_civil_id}"/>
                <nacid_ext:report_subelement name="Име" value="${person_first_name}"/>
                <nacid_ext:report_subelement name="Презиме" value="${person_middle_name}"/>
                <nacid_ext:report_subelement name="Фамилия" value="${person_last_name}"/>
                <nacid_ext:report_subelement name="${messages.birthCountry}" value="${person_birth_country}"/>
                <nacid_ext:report_subelement name="${messages.birthCity}" value="${person_birth_city}"/>
                <nacid_ext:report_subelement name="${messages.birthDate}" value="${personBirthDate}"/>
                <nacid_ext:report_subelement name="гражданин на" value="${person_citizenship}"/>
            </regprof_ext:applicationViewReportPerson>
            <fieldset><legend class="noForm">Документ за самоличност</legend>
            	<nacid_ext:report_subelement name="Номер" value="${applicant_document_number}"/>
                <nacid_ext:report_subelement name="Дата на издаване" value="${applicant_document_date}"/>
                <nacid_ext:report_subelement name="Издаден от" value="${applicant_document_issued_by}"/>
				<nacid_ext:report_subelement name="Тип документ за самоличност" value="${applicant_personal_id_document_type}"/>
            </fieldset>
            <div class="clr10"><!--  --></div>            
            <p>Съгласен съм да се ползват лични данни за целите на проверката: ${is_personal_data_usage ? "ДА" : "НЕ" }</p>
        	<p>Декларирам, че данните посочени в заявлението за издаване на удостоверение за придобита проф. квалификация 
         	на територията на Р България, необходимо за достъп или за упражняване на регулирана професия на 
         	територията на друга държава членка, са истински и автентични: ${is_data_authentic ? "ДА" : "НЕ" }</p>  
		</fieldset>
		<fieldset><legend class="noForm">За контакт</legend>
            <nacid_ext:report_subelement name="Електронна поща" value="${email}"/>
            <nacid_ext:report_subelement name="Държава" value="${applicant_country}"/>
            <nacid_ext:report_subelement name="Населено място" value="${applicant_city}"/>
            <nacid_ext:report_subelement name="Адрес" value="${applicant_addr_details}"/>
            <nacid_ext:report_subelement name="Телефон" value="${applicant_phone}"/>
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
                <p>Съгласен съм да получавам информация и уведомления на посочения електронен адрес: ${personal_email_informing ? "ДА" : "НЕ" }</p>
                <div class="clr"><!--  --></div>
            </c:if>
        </fieldset>
		<c:if test="${namesDontMatch}">
            <fieldset><legend class="noForm">Данни от документ</legend>
           		<nacid_ext:report_subelement name="Тип персонален идентификатор" value="${document_civil_id_type}"/>
           		<nacid_ext:report_subelement name="Персонален идентификатор" value="${document_civil_id}"/>
               	<nacid_ext:report_subelement name="Име" value="${document_firstName}"/>
               	<nacid_ext:report_subelement name="Презиме" value="${document_middleName}"/>
               	<nacid_ext:report_subelement name="Фамилия" value="${document_lastName}"/>
            </fieldset>
        </c:if>              
        <c:if test="${show_representative}">
           <fieldset><legend class="noForm">Лични данни на подателя</legend>
                <regprof_ext:applicationViewReportPerson person="representative">
                   <nacid_ext:report_subelement name="Тип персонален идентификатор" value="${person_civilid_type}"/>
                   <nacid_ext:report_subelement name="Персонален идентификатор" value="${person_civil_id}"/>
                   <nacid_ext:report_subelement name="Име" value="${person_first_name}"/>
                   <nacid_ext:report_subelement name="Презиме" value="${person_middle_name}"/>
                   <nacid_ext:report_subelement name="Фамилия" value="${person_last_name}"/>
                   <nacid_ext:report_subelement name="Телефон" value="${repPhone}"/>
                   <c:if test="${show_repCompany == false }">
                   		<nacid_ext:report_subelement name="Адрес" value="${repAddressDetails}"/>
                   		<nacid_ext:report_subelement name="Електронна поща" value="${repEmail}"/>
                   </c:if>
                   <c:if test="${show_repCompany == true }">
                   		<nacid_ext:report_subelement name="Фирма" value="${repCompany}"/>
                   </c:if>
                   <div class="clr"><!--  --></div>
               </regprof_ext:applicationViewReportPerson>
           </fieldset>
       </c:if>
       <fieldset><legend class="noForm">Тип услуга и държава на кандидатурата</legend>
       		<nacid_ext:report_subelement name="Избран тип услуга" value="${service_type}"/>
            <nacid_ext:report_subelement name="Кандидатства за държава" value="${application_country}"/>
       </fieldset>
       <regprof_ext:applicationReportTrainingCourseView attributePrefix="${attributePrefix}">
       		<c:if test="${has_education}">
	       		<fieldset><legend class="noForm">Информация за курса на обучение</legend>
	       			<c:if test="${sec}">
	       				<fieldset><legend class="noForm">${education}</legend>
	       					<nacid_ext:report_subelement name="Обучаваща институция" value="${prof_institution_name}"/>
	       					<nacid_ext:report_subelement name="Обучаваща институция старо име" value="${prof_institution_org_name}"/>
	       					<nacid_ext:report_subelement name="Професионална квалификация по документи" value="${sec_prof_qualification}"/>
	       					<regprof_ext:applicationSpecialitiesView attributePrefix="${attributePrefix}">
	       						<nacid_ext:report_subelement name="Специалност" value="${sec_speciality}"/>
	       					</regprof_ext:applicationSpecialitiesView>
	       					<nacid_ext:report_subelement name="Разред" value="${sec_caliber}"/>
	       					<nacid_ext:report_subelement name="Вид на документ" value="${document_type}"/>
	       					<nacid_ext:report_subelement name="Серия на документ" value="${document_number}"/>
	       					<nacid_ext:report_subelement name="Номер на документ" value="${document_series}"/>
	       					<nacid_ext:report_subelement name="Рег. номер на документ" value="${document_reg_num}"/>
	       					<nacid_ext:report_subelement name="Дата на документ" value="${document_date}"/>
	       				</fieldset>
	       			</c:if>
	       			<c:if test="${high || sdk}">
	       				<fieldset><legend class="noForm">Висше образование</legend>
	       					<nacid_ext:report_subelement name="Обучаваща институция" value="${prof_institution_name}"/>
	       					<nacid_ext:report_subelement name="Обучаваща институция старо име" value="${prof_institution_org_name}"/>
	       					<nacid_ext:report_subelement name="Професионална квалификация по документи" value="${high_prof_qualification}"/>
	       					<regprof_ext:applicationSpecialitiesView attributePrefix="${attributePrefix}">
	       						<nacid_ext:report_subelement name="Специалност" value="${high_speciality}"/>
	       					</regprof_ext:applicationSpecialitiesView>
	       					<nacid_ext:report_subelement name="ОКС" value="${high_edu_level}"/>
	       					<nacid_ext:report_subelement name="Вид на документ" value="${document_type}"/>
	       					<nacid_ext:report_subelement name="Серия на документ" value="${document_number}"/>
	       					<nacid_ext:report_subelement name="Номер на документ" value="${document_series}"/>
	       					<nacid_ext:report_subelement name="Рег. номер на документ" value="${document_reg_num}"/>
	       					<nacid_ext:report_subelement name="Дата на документ" value="${document_date}"/>
	       				</fieldset>
	       			</c:if>
	       			<c:if test="${sdk}">
	       				<fieldset><legend class="noForm">СДК</legend>
	       					<nacid_ext:report_subelement name="Обучаваща институция" value="${sdk_prof_institution_name}"/>
	       					<nacid_ext:report_subelement name="Обучаваща институция старо име" value="${sdk_prof_institution_org_name}"/>
	       					<nacid_ext:report_subelement name="Професионална квалификация по документи" value="${sdk_prof_qualification}"/>
	       					<regprof_ext:applicationSpecialitiesView attributePrefix="${attributePrefix}">
	       						<nacid_ext:report_subelement name="Специалност" value="${sdk_speciality}"/>
	       					</regprof_ext:applicationSpecialitiesView>
	       					<nacid_ext:report_subelement name="Вид на документ" value="${sdk_document_type}"/>
	       					<nacid_ext:report_subelement name="Серия на документ" value="${sdk_document_number}"/>
	       					<nacid_ext:report_subelement name="Номер на документ" value="${sdk_document_series}"/>
	       					<nacid_ext:report_subelement name="Рег. номер на документ" value="${sdk_document_reg_num}"/>
	       					<nacid_ext:report_subelement name="Дата на документ" value="${sdk_document_date}"/>
	       				</fieldset>
	       			</c:if>
	        	</fieldset> 
        	</c:if>
        	<c:if test="${has_experience}">      	
	        	<fieldset><legend class="noForm">Информация за стаж</legend>
	        		<regprof_ext:applicationReportExperienceView attributePrefix="${attributePrefix}">
	        			<nacid_ext:report_subelement name="Професия" value="${profession}"/>
	        			<fieldset><legend class="noForm">Документи</legend>
		        			<regprof_ext:applicationExperienceDocumentView attributePrefix="${attributePrefix}">	        			
		       					<fieldset><legend class="noForm">Документ</legend>
		       						<nacid_ext:report_subelement name="Тип документ" value="${prof_expr_document_type}"/>
		       						<nacid_ext:report_subelement name="Номер надокумент" value="${document_number}"/>
		       						<nacid_ext:report_subelement name="Дата на документ" value="${document_date}"/>
		       						<nacid_ext:report_subelement name="Издаваща институция" value="${document_issuer}"/>
		       						<p>Влиза в изчисление: ${dates_count ? "ДА" : "НЕ" }</p>
		       						<fieldset><legend class="noForm">Дати</legend>
		       							<regprof_ext:applicationExperienceDatesView attributePrefix="${attributePrefix}" documentId="${doc_id}">						
		       								<nacid_ext:report_subelement name="Работил за периода" value="${single_date}"/>
		       							</regprof_ext:applicationExperienceDatesView>
		       						</fieldset>
		       					</fieldset>	        			
		        			</regprof_ext:applicationExperienceDocumentView>
	        			</fieldset>
	        			<div class="clr10"><!--  --></div>
	        			<nacid_ext:report_subelement name="Изчислен професионален стаж" value="${period}"/>
	        			<p>Не е отнето правото за упражняване на професията и липса на наложени административни наказания във връзка
	        			 с упражняване на професията: ${not_restricted ? "ДА" : "НЕ" }</p>     				
	        		</regprof_ext:applicationReportExperienceView>
	        	</fieldset>
	        	<fieldset><legend class="noForm">Професионална квалификация, за която се иска удостоверение</legend>
	        		<nacid_ext:report_subelement name="Професионална квалификация" value="${certificate_prof_qual}"/>
	        	</fieldset>
        	</c:if>
       </regprof_ext:applicationReportTrainingCourseView>
       
       <fieldset><legend class="noForm">Статус</legend> 
			<nacid_ext:report_subelement name="Статус на заявлението" value="${application_status_for_applicant}" /> 
			<nacid_ext:report_subelement name="Деловоден статус" value="${docflow_status}" />
			<c:if test="${show_archive_number}">
				<nacid_ext:report_subelement name="Архивен номер" value="${archive_number}" />
			</c:if>
			<c:if test="${showCertificate and not empty regprofApplicatioAttachmentForReportWebModel }">
	            <p>
	                <span><label>Удостоверение ${ certificate_number}</label></span>
	                <regprof_ext:applicationAttachmentsList>
	                    <a target="_blank" href="${attachmentPrefix }/?id=${id }&amp;att_type=${type}&amp;application_id=${requestScope[appl_Id]}" ><img src="${pathPrefix }/img/icon_view.png"></a><br />
	                </regprof_ext:applicationAttachmentsList>
	            </p>
	            <div class="clr"><!--  --></div>
	        </c:if>
	        <c:if test="${recognized}">
	            <nacid_ext:report_subelement name="Удостоверен стаж" value="${recognized_experience}" />
	            <nacid_ext:report_subelement name="Удостоверена професия" value="${recognized_profession}" />
	            <nacid_ext:report_subelement name="Удостоверени за държава" value="${application_country}" />
	        </c:if>
	        <div class="clr"><!--  --></div>
		</fieldset>
	
       	<c:if test="${show_application_attachments && not hideAttachments}">
            <fieldset><legend class="noForm">Прикачени документи</legend>
            <regprof_ext:applicationAttachmentsList attributePrefix="${attributePrefix}">
                <a target="_blank" href="${attachmentPrefix }/?id=${id }&amp;att_type=${type}&amp;application_id=${requestScope[appl_Id]}" >${document_type }</a><br />
            </regprof_ext:applicationAttachmentsList>
            </fieldset>
       	</c:if>
        
</regprof_ext:applicationReportView>