<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="com.nacid.bl.nomenclatures.ApplicationStatus"%>

<script type="text/javascript">
var postponedStatusCode = <%=ApplicationStatus.APPLICATION_POSTPONED_STATUS_CODE %>;
function updateLegalReasons(select) {
    var appStatusId = select.options[select.selectedIndex].value;
    if (appStatusId == '-') {
        $("legal_reason").update("");
    } else {
        new Ajax.Request('${pathPrefix}/control/legal_reason_ajax?onlyActive=1&appStatusId=' + appStatusId + '&applicationType=' + applicationType, {
            onCreate : function(oXHR) {
               setLoading($("form_appl_motives"));
            },
            onSuccess: function(oXHR) {
                $("legal_reason").update(oXHR.responseText);
                removeLoading($("form_appl_motives"));              
            },
            onFailure : function(oXHR) {
                alert("Възникна грешка при опит за прочитане на правните основания, свързани с този статус на заявление. Моля опитайте по-късно..." + oXHR.statusText);
                $("legal_reason").update("");
                removeLoading($("form_appl_motives"));
            },
            method: 'GET'
          });
    }
}
function validateLegalReason() {
	return $('legalReason') ? validateComboBox($('legalReason'), true, 'Трябва да изберете правно основание') : true;
}
function toggleApplicantInfo(select) {
    if (select[select.selectedIndex].value == postponedStatusCode) {
        $("applicant_info_container").show();
        $("applicant_info").enable();
    } else {
        if (oldApplicantInfo == "") {
            $("applicant_info_container").hide();   
        } else {
            $("applicant_info_container").show();
            $("applicant_info").disable();
            $("applicant_info").value = oldApplicantInfo;
        }
        
    }
}
</script>
<h3 class="title"><span>${operationStringForScreens }  мотиви</span></h3>
<nacid:applicationMotivesEdit>
<script type="text/javascript">
  var oldApplicantInfo = "${applicantInfo}";
  var applicationType = ${applicationType};
</script>
<h3 class="names">${application_header }</h3>
<div class="clr15"><!--  --></div>
	<v:form action="${pathPrefix }/control/application_motives/save" method="post"
		name="form_appl_motives"
		backurl="${pathPrefix }/control/comission_calendar_process/${backOper }?calendar_id=${calendarId }"
		additionalvalidation="validateLegalReason()">
         
        <%--
	        Тъй като тоя комбобокс не се вижда постоянно, той не трябва да се валидира докато не се вижда....
	        и поради тази причина не мога да ползвам стандартната валидация, и затова слагам във формата 
	        additionalvalidation="($('legalReason') ? validateComboBox(document.form_appl_motives.legalReason,true,'Трябва да изберете правно основание') : true)"
	        Странно в цялата работа е, че и да затрия с javascript input-a s name=legalReason, то document.form_appl_motives.legalReason връща обект!!!!
	        Не знам дали няма да изгърми някъде ако заменя document.form_appl_motives.legalReason с $('legalReason') във FormTag-a и затова го оставям по стария начин!
	        <v:comboBoxValidator input="legalReason" required="true" errormessage="Трябва да изберете правно основание"/>
        --%>
		<nacid:systemmessage />
		
		<fieldset><legend>Общи данни</legend>
		
			<input type="hidden" name="id" value="${applicationId}"/>
			<input type="hidden" name="calendar_id" value="${calendarId}"/>
			<input type="hidden" name="backOper" value="${backOper}"/>
			<p><span class="fieldlabel"><label for="motives">Мотиви</label><br /></span> 
				<textarea id="docDescr" class="brd w600" rows="10" cols="40"
						name="motives">${motives }</textarea>
			</p>
			<div class="clr"><!--  --></div>

			<fieldset><legend>Признати специалности</legend>
	             <input type="hidden" name="recognizedSpecialityId" id="recognizedSpecialityId" value="${recognizedSpecialityId }" />
	             <!-- RayaWritten -->
					<v:specialityinput id="recognizedSpeciality" class="brd w450" comboAttribute="professionGroupCombo"
					chooseMultiple="true" specialityIdInput="$('recognizedSpecialityId')" specialityList="${recognSpecsList}" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('recognizedSpeciality');"/>
				<!-- End of RayaWritten -->
	         </fieldset>
	         <div class="clr20"><!--  --></div>
	         <script type="text/javascript">
	                 new Autocomplete('recognizedSpeciality', { 
	                       serviceUrl:'${pathPrefix}/control/specialitysuggest?only_active=1',
	                       width:600,
	                       onSelect:updateNomenclature
	                 });
	                 
	         </script>
			
			
			
			
			<p>
				<span class="fieldlabel"><label for="recognizedEduLevel">Призната образователна степен</label></span> 
				<nacid:combobox id="recognizedEduLevel" name="recognizedEduLevel" attributeName="recognizedEduLevel" style="brd w600" />
			</p>
			<div class="clr"><!--  --></div>

			<c:if test="${recognizedQualification != null }">
				<p>
		            <span class="fieldlabel"><label for="recognizedQualificationId">Призната квалификация</label></span> 
		            <input type="hidden" name="recognizedQualificationId" id="recognizedQualificationId" value="${recognizedQualificationId }" />
		            <v:textinput class="brd w600" name="recognizedQualification" id="recognizedQualification"  value="${recognizedQualification }" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('recognizedQualification');" />
		        </p>
		        <script type="text/javascript">
		                new Autocomplete('recognizedQualification', { 
		                    serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?only_active=1&type=' + qualificationNomenclatureId,
		                      width:600,
		                      onSelect:updateNomenclature
		                });
		                
		        </script>
				
				<div class="clr"><!--  --></div>
			</c:if>

			<p>
				<span class="fieldlabel"><label for="recognizedProfGroup">Признато професионално направление</label></span>
				<nacid:combobox id="recognizedProfGroup" name="recognizedProfGroup" attributeName="recognizedProfGroupCombo" style="brd w600" onchange="updateProfGroupEduArea(this)" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="recognizedProfGroup_edu_area_id">Област на образованието</label></span>
				<nacid:combobox name="recognizedProfGroup_edu_area_id" id="recognizedProfGroup_edu_area_id" attributeName="recognizedProfGroupEduAreaCombo" style="w600 brd" disabled="disabled"/>
			</p>
			<div class="clr"><!--  --></div>

			<p><span class="fieldlabel"><label for="application_status">Статус</label><br /></span> 
                <nacid:combobox name="application_status" id="application_status" attributeName="applicationStatus" style="brd w400" onchange="updateLegalReasons(this);toggleApplicantInfo(this);"/>
            </p>
            <p id="legal_reason">
                <c:if test="${not empty legalReason }">
                <span class="fieldlabel"><label for="legalReason">Правно основание</label></span>
                    <nacid:combobox name="legalReason" id="legalReason" attributeName="legalReason" style="brd w400" />
                </c:if>         
            </p>
            <p id="applicant_info_container">
                <span class="fieldlabel"><label for="applicant_info">Информация за заявителя</label><br /></span> 
                <textarea id="applicant_info" class="brd w600" rows="10" cols="40" name="applicant_info">${applicantInfo }</textarea>
            </p>
            
		</fieldset>
		<div class="clr"><!--  --></div>
	    <v:hidden name="recognizedSpeciality_specialities_count" id="recognizedSpeciality_specialities_count" value="${recognSpecsCount}" />
	</v:form>
</nacid:applicationMotivesEdit>

<c:if test="${!operationView }" >
     <v:form action="${pathPrefix }/control/application_motives" method="post" name="generate_document"  skipsubmitbuttons="true">
     <v:comboBoxValidator input="documentType" required="true" errormessage="Трябва да изберете тип документ" />
     <fieldset><legend>Генериране на документ</legend>
         <nacid:systemmessage name="generatedDocsMessage" />
         <input type="hidden" name="id" value="${applicationId}"/>
         <input type="hidden" name="calendar_id" value="${calendarId}"/>
         <input type="hidden" name="backOper" value="${backOper}"/>
         
         <p>
            <span class="fieldlabel"><label for="recognizedSpeciality">Тип документ</label></span> 
            <nacid:combobox id="documentType" name="documentType"  attributeName="documentType" style="w400 brd" />
         </p>
         <p class="cc">
             <input class="generate w280" type="submit" value="Генериране на документ" onclick="javascript:if (isFormChanged()) {alert('Трябва да запишете данните!');return false;} return true;"/>
         </p>
     </fieldset>
     </v:form>
</c:if>
<script>
    toggleApplicantInfo($('application_status'));
    initformvalues = $('form_appl_motives').serialize();
    function isFormChanged() {        
    	if ($('form_appl_motives').serialize() != initformvalues) {
    		return true;
    	} 
    	return false;
    }
</script>
<%@ include file="/screens/footer.jsp"%>
