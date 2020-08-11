<%@ tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ tag import="com.ext.nacid.web.handlers.impl.applications.ExpertReportAttachmentsHandler"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ attribute name="attributePrefix" required="false" rtexprvalue="true"%>
<%@ attribute name="hideAttachments" required="false" rtexprvalue="true"%>
<%@ attribute name="hideStatusHistory" required="false" rtexprvalue="true" description="if true - hides application status history information"%>
<%@ attribute name="hideExaminations" required="false" rtexprvalue="true" description="if true - hides university and diploma examinations"%>
<%@ attribute name="showCertificate" required="false" rtexprvalue="true" description="if true - shows certificate attachment"%>
<%@ attribute name="attachmentPrefix" required="true" rtexprvalue="true"%>
<%@ attribute name="isApplicantViewer" required="false" rtexprvalue="true" description="if true - this means applicant is watching his own application or for public report"%>

<c:set var="appl_Id" scope="page" value="${attributePrefix}applicationId" />
<nacid_ext:report_applicant_internal attributePrefix="${attributePrefix}" attachmentPrefix="${attachmentPrefix}" hideAttachments="${hideAttachments }"/>

<nacid_ext:applicationReportView attributePrefix="${attributePrefix}">
	<fieldset><legend class="noForm">${isApplicantViewer ? 'Статус' : 'Проверки - статус' }</legend> 
		<nacid_ext:report_subelement name="Статус на заявлението" value="${isApplicantViewer ? application_status_for_applicant : application_status}" /> 
		<c:if test="${show_archive_number}">
			<nacid_ext:report_subelement name="Архивен номер" value="${archive_number}" />
		</c:if>
		<c:if test="${showCertificate and not empty applicationAttchForReportWebModel }">
            <p>
                <span><label>Удостоверение ${ certificate_number}</label></span>
                <nacid_ext:applicationAttachmentsList type="applications">
                    <a target="_blank" href="${attachmentPrefix }/${file_name }?attachment_id=${id }&amp;att_type=${type}&amp;application_id=${requestScope[appl_Id]}" ><img src="${pathPrefix }/img/icon_view.png"></a><br />
                </nacid_ext:applicationAttachmentsList>
            </p>
            <div class="clr"><!--  --></div>
        </c:if>
        
        <nacid_ext:report_subelement name="Информация за заявлителя" value="${applicantInfo}" />
        <c:if test="${empty isApplicantViewer or !isApplicantViewer}">
        <nacid_ext:report_subelement name="Представени документи" value="${submittedDocs}" />
        </c:if>
        <c:if test="${recognized_and_contains_protocol}">
            <nacid_ext:report_subelement name="Призната степен" value="${recognized_edulevel}" />
            <nacid_ext:report_subelement name="Призната специалност" value="${recognized_specialities}" escapeXml="false"/>
            <nacid_ext:report_subelement name="Призната квалификация" value="${recognized_qualification}" />
        </c:if>
        <div class="clr"><!--  --></div>
	</fieldset>

    <c:if test="${not hideStatusHistory}">
	<fieldset><legend class="noForm">История на статусите</legend>
		<c:forEach items="${appStatusHistoryWebmodelList }" var="item">
			<nacid_ext:report_subelement name="${item.statusName }" value="${item.dateAssigned }${not empty item.legalReason ? ' на основание ' : ''}${item.legalReason }" />
		</c:forEach>
	</fieldset>
	</c:if>
    <c:if test="${not hideExaminations}">
	<nacid_ext:expertReportUniversityExamination>
	   <fieldset><legend class="noForm">Проверка на висше училище по седалище</legend>
           <nacid_ext:report_subelement name="Наименование на български" value="${uni_bg_name}" />
           <nacid_ext:report_subelement name="Оригинално наименование" value="${uni_original_name}" />
	       <c:choose>
	           <c:when test="${is_examinated}">
		       <nacid_ext:report_subelement name="Към дата" value="${examinationDate}" />
		       
		       <nacid_ext:report_subelement name="Признато по седалище" value="${is_recognized ? \"ДА\" : \"НЕ\" }" />
		       <nacid_ext:report_subelement name="Комуникирана институция" value="${is_communicated ? \"ДА\" : \"НЕ\" }" />
	           <nacid_ext:report_subelement name="Провежда обучение" value="${training_location}" />
	           <nacid_ext:report_subelement name="Начин на обучение" value="${training_form}" />
	           <nacid_ext:report_subelement name="Бележки" value="${notes}" />
               <nacid_ext:report_subelement name="Бележки към проверката 'към дата'" value="${universityValidityNotes}" />
	           
	           <c:if test="${competentInstitutions != null}">
	            <fieldset><legend class="noForm">Институция, компетентна организация на национално ниво</legend>
	                <c:forEach items="${competentInstitutions }" var="item">
	                    <nacid_ext:report_subelement name="&nbsp;" value="${item.name }, ${item.country }" />
	                </c:forEach>
	            </fieldset>
	           </c:if>
	           <div class="clr10"><!--  --></div>
	           <c:if test="${not empty attachments && not hideAttachments}">
	            <fieldset><legend class="noForm">Прикачени документи</legend>
	                <c:set var="uniExameAttchWebModel" scope="request" value="${attachments}" />
	                <nacid_ext:applicationAttachmentsList type="univalidity" attributePrefix="${attributePrefix}">
	                    <a target="_blank" href="${attachmentPrefix }/${file_name }?attachment_id=${id }&amp;att_type=${type}&amp;application_id=${requestScope[appl_Id]}" >${document_type }</a><br />
	                </nacid_ext:applicationAttachmentsList>
	            </fieldset>
	            </c:if>
	            
	            
	            
	            </c:when>
	            <c:otherwise>
	               Няма извършена проверка за това висше училище!
	            </c:otherwise>
	        </c:choose>
	   
	   </fieldset>
	</nacid_ext:expertReportUniversityExamination>
	
	<fieldset><legend class="noForm">Проверка на висше училище по място на провеждане на обучение</legend>
           <nacid_ext:expertReportUniversityExaminationByPlace> 
                 <fieldset><legend class="noForm">Проведено обучение</legend>
                    <nacid_ext:report_subelement name="Място на провеждане" value="${trainingLocation}" />
                    <nacid_ext:report_subelement name="Институцията, провела обучението" value="${trainingInstitution}" />
                </fieldset>
           </nacid_ext:expertReportUniversityExaminationByPlace>
           <div class="clr10"><!--  --></div>
           <nacid_ext:report_subelement name="Легитимно по място на обучение" value="${uni_validity_place_recognized ? 'ДА' : 'НЕ' }" />
    </fieldset>
	
	<nacid_ext:expertReportDiplomaExamination>
       <fieldset><legend class="noForm">Проверка на дипломата</legend>
           <nacid_ext:report_subelement name="Дата на проверката" value="${examination_date}" />
           <nacid_ext:report_subelement name="Компетентна институция на национално ниво" value="${competent_institution}" escapeXml="false" />
           <c:forEach items="${diploma_examination_universities}" var="uniName"> 
           <nacid_ext:report_subelement name="Регистър на висшето училище" value="${uniName}" escapeXml="false" />
           </c:forEach>
           <nacid_ext:report_subelement name="Осъществена е комуникация с институцията на национално ниво" value="${institutionCommunicated ? \"ДА\" : \"НЕ\" }" />
           <nacid_ext:report_subelement name="Осъществена е комуникация с висшето училище" value="${institutionCommunicated ? \"ДА\" : \"НЕ\" }" />
           <nacid_ext:report_subelement name="Намерено в регистър/библиотека" value="${foundInRegister ? \"ДА\" : \"НЕ\" }" />
           <nacid_ext:report_subelement name="Дипломата е автентична" value="${recognized ? \"ДА\" : \"НЕ\" }" />
           <nacid_ext:report_subelement name="Бележки" value="${notes}" />
           <div class="clr10"><!--  --></div>
           <c:if test="${show_diploma_examination_attached_docs && not hideAttachments}">
            <fieldset><legend class="noForm">Прикачени документи</legend>
                
                <nacid_ext:applicationAttachmentsList type="diplexam" attributePrefix="${attributePrefix}">
                    <a target="_blank" href="${attachmentPrefix }/${file_name }?attachment_id=${id }&amp;att_type=${type}&amp;application_id=${requestScope[appl_Id]}" >${document_type }</a><br />
                </nacid_ext:applicationAttachmentsList>
            </fieldset>
            </c:if>
       </fieldset>
    </nacid_ext:expertReportDiplomaExamination>
	</c:if>
</nacid_ext:applicationReportView>