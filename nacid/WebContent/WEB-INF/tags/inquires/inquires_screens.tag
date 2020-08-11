<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires" prefix="inquires"%>
<fieldset><legend class="noForm">Екран</legend>
	   <c:forEach items="${screenWebModels}" var="screenWebModel">
	   <fieldset><legend class="noForm">Информация за заявление</legend>
	       <inquires:inquire_subelement name="Деловоден номер и дата:" value="${screenWebModel.docFlowNumber}" test="${elements.docFlowNumber}" />
	       <inquires:inquire_subelement name="Имена на заявителя:" value="${screenWebModel.applicantNames}" test="${elements.applicantNames}" />
	       <inquires:inquire_subelement name="Име, Презиме и Фамилия на собственика на дипломата:" value="${screenWebModel.ownerNames}" test="${elements.ownerNames}" />
	       <inquires:inquire_subelement name="Гражданство на заявителя:" value="${screenWebModel.ownerCitizenship}" test="${elements.ownerCitizenship}" />
	       <inquires:inquire_subelement name="Наименование на ВУ:" value="${screenWebModel.universityName}" test="${elements.universityName}" />
	       <inquires:inquire_subelement name="Държава по седалище на ВУ:" value="${screenWebModel.universityCountry}" test="${elements.universityCountry && screenWebModel.showUniversityCountry}" />
	       <inquires:inquire_subelement name="Статус на висшето училище:" value="${screenWebModel.universityExaminationRecognized}" test="${elements.universityExaminationRecognized && screenWebModel.showUniversityExamination}" />
	       <inquires:inquire_subelement name="Място на обучение:" value="${screenWebModel.trainingLocation}" test="${elements.trainingLocation}" />
	       <inquires:inquire_subelement name="ОКС по диплома:" value="${screenWebModel.diplomaEduLevel}" test="${elements.diplomaEduLevel}" />
	       <inquires:inquire_subelement name="Специалност по диплома:" value="${screenWebModel.diplomaSpeciality}" test="${elements.diplomaSpeciality}" />
	       <inquires:inquire_subelement name="Професионална квалификация по диплома:" value="${screenWebModel.diplomaQualification}" test="${elements.diplomaQualification}" />
	       <inquires:inquire_subelement name="Диплома – дата:" value="${screenWebModel.diplomaDate}" test="${elements.diplomaDate}" />
	       <inquires:inquire_subelement name="Начало на обучението:" value="${screenWebModel.trainingStart}" test="${elements.trainingStart}" />
	       <inquires:inquire_subelement name="Край на обучението:" value="${screenWebModel.trainingEnd}" test="${elements.trainingEnd}" />
	       <inquires:inquire_subelement name="Форма на обучение:" value="${screenWebModel.trainingForm}" test="${elements.trainingForm}" />
	       <inquires:inquire_subelement name="Предходно образование – средно образование:" value="${screenWebModel.schoolDiplomaInfo}" test="${elements.schoolDiplomaInfo}" />
	       <inquires:inquire_subelement name="Предходно образование – предходна образователна степен за висше образование:" value="${screenWebModel.previousUniDiplomaInfo}" test="${elements.previousUniDiplomaInfo}" />
	       <c:if test="${elements.commissionStatuses}">
            <p>
	           <span class="fieldlabel"><label>Статус на заявлението от Комисия:</label></span>
	           <c:choose>
	               <c:when test="${not empty screenWebModel.commissionStatuses}">
	                   <table cellpadding="3">
	                   <c:forEach items="${screenWebModel.commissionStatuses}" var="item">
	                       <tr>
	                           <td>${item.statusName }</td>
                               <td>${item.dateAssigned }</td>
                               <td>
                                  <c:if test="${not empty item.legalReason }" >
                                    на основание ${item.legalReason }
                                  </c:if>
                               </td>
                            </tr>
	                   </c:forEach>
	                   </table>
	               </c:when>
	               <c:otherwise>
	                   N/A
	               </c:otherwise>
	           </c:choose>
	        </p>
	        <div class="clr"><!--  --></div>
	       </c:if>
	       
	       
	        <c:if test="${elements.commissionCalendars}">
            <p>
               <span class="fieldlabel"><label>Заседания на Комисия:</label></span>
               <c:choose>
                   <c:when test="${not empty screenWebModel.commissionCalendars}">
                       <table>
                       <c:forEach items="${screenWebModel.commissionCalendars}" var="item">
                           <tr>
                            <td>${item.sessionNumber } - ${item.dateTime }</td>
                           </tr>
                       </c:forEach>
                       </table>
                   </c:when>
                   <c:otherwise>
                       N/A
                   </c:otherwise>
               </c:choose>
            </p>
            <div class="clr"><!--  --></div>
           </c:if>
           
	       <inquires:inquire_subelement name="Мотиви:" value="${screenWebModel.motives}" test="${elements.motives}" />
	       <inquires:inquire_subelement name="Призната ОКС:" value="${screenWebModel.recognizedEduLevel}" test="${elements.recognizedEduLevel}" />
	       <inquires:inquire_subelement name="Призната специалност:" value="${screenWebModel.recognizedSpecialities}" test="${elements.recognizedSpecialities}" />
	       <inquires:inquire_subelement name="Призната квалификация:" value="${screenWebModel.recognizedQualification}" test="${elements.recognizedQualification}" />
	       <inquires:inquire_subelement name="Актуален статус на заявлението:" value="${screenWebModel.applicationStatus}" test="${elements.applicationStatus}" />
		   <inquires:inquire_subelement name="Деловоден статус:" value="${screenWebModel.docflowStatus}" test="${elements.docflowStatus}" />
        </fieldset>
        </c:forEach>
        ${emptyData}
</fieldset>

