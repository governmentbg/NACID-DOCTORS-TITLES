<%@ page import="com.nacid.bl.nomenclatures.ApplicationDocflowStatus" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>

<!-- RayaWritten -->
<script type="text/javascript">
var oldArchiveNumber = "${archiveNumber}";
var archiveAppStatus = <%= ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE %>;
var emptyArchiveNumber = "${archiveNumberPrefix}";

function fillExperiencePeriod() {
	var years = ${requestScope["com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl"].experienceRecord.years + 0};
	var months = ${requestScope["com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl"].experienceRecord.months + 0};
	var days = ${requestScope["com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl"].experienceRecord.days + 0};
	//$('years').innerHTML = years;
	//$('months').innerHTML = months;
	//$('days').innerHTML = days;
	//if (years > 1) $('yearsCount')
	if (years == 0 && months == 0 && days == 0) {
		$('experiencePeriod').innerHTML = 'Няма въведена информация за професионален стаж';
	}
	else {
		var experiencePeriod = years == 1 ? years+" година, " : years+" години, ";
		experiencePeriod += months == 1 ? months+" месец и " : months+" месеца и ";
		experiencePeriod += days == 1 ? days+" ден" : days+" дни";
		$('experiencePeriod').innerHTML = experiencePeriod;
	}
}

/*function toggleExperienceCheckbox() {
	if ($('is_unrestricted').checked == true) {
		$('experience_recognized').enable();
		$('experience_recognized').title = '';
	} else {
		$('experience_recognized').disable();
		$('experience_recognized').title = 'За да бъде признат стажът, е необходимо правото за упражняване на професията да не е отнето' +
			' и да няма наложени административни наказания във връзка с упражняване на професията';
		$('experience_recognized').checked = false;
	}
}*/

</script>


<h3 class="title"><span>Проверки</span></h3>
<h3 class="names">${application_header }</h3>
<div class="clr15"><!--  --></div>

<p class="cc">
	<input class="back" type="button"
		onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1&appNumber=${sessionScope.app_number_filter_value }';"
		value="Назад" />
</p>
<nacid:systemmessage />
<v:form skipsubmitbuttons="true" method="post" name="application_status_form" action="${pathPrefix }/control/regprofapplication/save?appId=${id}&amp;type=status&amp;activeForm=4"
		additionalvalidation="validateArchiveNumber($('docflow_status'), $('archive_number'))">
	<fieldset>
		<legend>Статус</legend>
		<nacid:systemmessage name="applicationStatusMsg" />
		<div class="clr"><!--  --></div>
		<div>
			<span class="fieldlabel2"><label for="status">Статус</label><br /></span>
			<nacid:combobox id="status" name="status" attributeName="statusCombo" path="status" style="brd w308"  /><br />
            <span class="fieldlabel2"><label for="status">Деловоден статус</label></span>
            <nacid:combobox id="docflow_status" name="docflow_status" attributeName="docflowStatusCombo" path="docflowStatusId" style="brd w308"
                            onchange="toggleArchiveNumber(this, $('archive_number_container'), $('archive_number'));" />


            <div class="clr"><!--  --></div>
     		<div id="archive_number_container">
      			<span class="fieldlabel2"><label for="archive_number">Номенклатурен номер :</label><br /></span>
				<v:textinput class="brd w200" maxlength="50" name="archive_number" id="archive_number"  value="${archiveNumber }" />
	  		</div>
		</div>
		<span class="fieldlabel">
			<a href="#" onclick="$('statusesDiv').toggle(); return false;">История на статусите</a><br />
		</span>
		<div class="clr10"><!--  --></div>
        <div id="statusesDiv" style="display: none">
            <table id="statusHistory" cellpadding="3">
                <tr>
                    <th colspan="2" align="left">Статус</th>
                </tr>
                <c:forEach items="${statusHistory }" var="item">
                    <tr <c:if test="${item.legalStatus}">class="bld"</c:if> >
                        <td>${item.statusName }</td>
                        <td>${item.dateAssignedFormatted }</td>
                    </tr>
                </c:forEach>
            </table>
            <div class="clr20">&nbsp;</div>
            <table id="docflowStatusHistory" cellpadding="3">
                <tr>
                    <th colspan="2" align="left">Деловоден статус</th>
                </tr>
                <c:forEach items="${docflowStatusesHistory }" var="item">
                    <tr>
                        <td>${item.applicationDocflowStatusName }</td>
                        <td>${item.dateAssignedFormatted }</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
		<div class="clr20"><!--  --></div>
		<v:submit value="Промени статус" />
		<div class="clr">
			<!--  -->
		</div>
	</fieldset>
	<div class="clr20"><!--  --></div>
</v:form>

<v:form skipsubmitbuttons="true" method="post" name="institution_examination_form" 
  		action="${pathPrefix }/control/regprofapplication/save?appId=${id}&amp;type=piexamination&amp;activeForm=4#institution_examination_form">
	  	<fieldset id="examination_institution_field"><legend>Проверка за легитимност на институция</legend>
	  	<nacid:systemmessage name="institutionValiditySystemMessage" />
  		<div style="display:none;" id="correct_message_div" class="correct">Данните бяха успешно въведени!</div>
        <div style="display:none;" id="error_message_div" class="error">Получи се грешка при опит за запис!</div>
        
  		<v:hidden id="examinationId" name="examinationId" value="${instExamWebModel.examId}"/>
  		<p><span class="fieldlabel2"><label>Наименование на институцията: </label></span>${instExamWebModel.profInst }</p>
	    <div class="clr"><!--  --></div>
	    <p><span class="fieldlabel2"><label>Тип на институцията: </label></span>${instExamWebModel.profInstType }</p>
	    <div class="clr"><!--  --></div>
	    <p><span class="fieldlabel2"><label>Професионална квалификация: </label></span>${instExamWebModel.qualBg }</p>
	    <div class="clr20"><!--  --></div>	    	    
	    <p>
      	<span class="fieldlabel">
      		<a id="newExamination" href="javascript: void(0);" onclick="editData(null);">Добави нова проверка</a><br/>
      	</span> 
     	</p>
      	<div class="clr20"><!--  --></div>    
	   		<nacid:list attribute="profInstitutionValiditiesList" skipFilters="true" tablePrefix="validitiesTable" />  
	    <div class="clr20"><!--  --></div>
	    <p>
	      <span class="fieldlabel2"><label for="examinationNotes">Бележки:</label></span>
	      <textarea class="brd w600" rows="3" cols="40" name="examinationNotes" id="examinationNotes">${instExamWebModel.examNotes}</textarea>
	    </p>
	    <div class="clr20"><!--  --></div>
	    <v:submit value="Запис на данните" />
  	</fieldset>
  	<div class="clr20"><!--  --></div>
</v:form>
    
  <!-- Hidden form fields so that I can use spring to get the properties I need -->
<v:form skipsubmitbuttons="true" method="post" name="institution_validation_form" action="${pathPrefix }/control/pivalidity_ajax/save?appId=${id}" 
  modelAttribute="com.nacid.bl.impl.regprof.ProfessionalInstitutionValidityImpl">
  	<v:hidden id="hiddenValidityId" name="id" path="id"/>
  	<v:hidden id="examinationDate" name="examinationDate" path="examinationDate"/>
  	<v:hidden id="professionalInstitutionId" value="${instExamWebModel.profInstId }" name="professionalInstitutionId" path="professionalInstitutionId"/>
  	<v:hidden id="qualificationBulgariaHighSdkId" name="qualificationBulgariaHighSdkId" path="qualificationBulgariaHighSdkId" value="${instExamWebModel.qualBgHighSdkId}"/>
  	<v:hidden id="qualificationBulgariaSecId" name="qualificationBulgariaSecId" path="qualificationBulgariaSecId" value="${instExamWebModel.qualBgSecId}"/>
  	<v:hidden id="hasRightsEducate" name="hasRightsEducate" path="hasRightsEducate"/>
  	<v:hidden id="isLegitimate" name="isLegitimate" path="isLegitimate"/>
  	<v:hidden id="notes" name="notes" path="notes"/>  
</v:form>


<fieldset id="examination_document_field" style="width: 870px; margin-left: 20px;"><legend class="noForm">Проверка за автентичност на издаден документ</legend>
<v:form skipsubmitbuttons="true" method="post" name="document_examination_form" enctype="multipart/form-data"
 	 action="${pathPrefix }/control/regprofapplication/save?appId=${id}&amp;type=documentexamination&amp;activeForm=4#examination_document_field">
 	 
 	 <v:dateValidator input="documentExaminationDate" format="d.m.yyyy" required="true" beforeToday="true"/>
 	 <v:comboBoxValidator input="source" required="true"/>
  		<nacid:systemmessage name="documentExaminationSystemMessage" />
  		<v:hidden id="documentExaminationId" name="documentExaminationId" value="${docExam.id }" />  		
  		<p><span class="fieldlabel2"><label>Наименование на инситуцията, издала документа: </label></span>${instExamWebModel.profInst }</p>
	    <div class="clr"><!--  --></div>
	    <p><span class="fieldlabel2"><label>Вид документ: </label></span>${doc_type_name }</p>
	    <div class="clr"><!--  --></div>
	    <p><span class="fieldlabel2"><label>Документ: </label></span>${document_num_date}</p>
	    <div class="clr"><!--  --></div> 
        <p><span class="fieldlabel2"><label for="source">Източник на проверка</label><br/></span>
         	<nacid:combobox  name="source" attributeName="sourcesCombo" path="source" style="w308 brd" onchange="showHideDocExamInfoBySource(this);"/>
         	<v:hidden id="alternativeSource" name="alternativeSource" value=""/>
        </p>
        <div class="clr"><!--  --></div>
        <div id="docExaminationDateDiv"> 
        <p><span class="fieldlabel2"><label for="documentExaminationDate">Дата на проверката</label><br/></span>
        	<v:dateinput id="documentExaminationDate" name="documentExaminationDate" value="${docExam.documentExaminationDateFormatted}" class="brd w80"/>
        </p>
        </div>
        
        <div id="documentExaminationScreenshot">
        	<v:hidden name="screenshotId" id="screenshotId" value="${screenshotId }" />
        	<p><span class="fieldlabel2"><label for="screenshot">Файл</label></span>
        		<input class="brd w240" disabled="disabled" name="screenshot" id="screenshot" type="file" value="" />&nbsp;&nbsp;${screenshotName }
        	</p>
        	<c:if test="${screenshotId != null }">
        		<div class="clr"><!--  --></div>
        		<a class="flt_rgt" href="${pathPrefix }/control/doc_exam_attachment/${screenshotName }?id=${screenshotId }&amp;original=1" target="_blank">Свали прикачения файл</a>
        	</c:if>
        </div>
        <div class="clr"><!--  --></div>

	    <v:submit value="Запис на данните" />
	    </v:form>
	    
      	<div  id="documentExamAttachments" style="display:none;" class="formClass">
      	<div class="clr20"><!--  --></div>
       	<c:if test="${docExamTableWebModel != null }">
         	<nacid:list attribute="docExamTableWebModel" tablePrefix="docExamAttch" />  
        <script type="text/javascript">
		/*
 		* маха линковете за сортиране на таблицата
 		*/
 		$$('#documentExamAttachments fieldset').each(function (fieldset) { // razshirqva 2-ta fieldset-a nad i pod tablicata
 			fieldset.style.width = '100%';
 		});
 		
 		$$('#docExamAttchmain_table td[class="dark"] > a').each(function(anchor) {
  			anchor.up(0).innerHTML = anchor.innerHTML; 
 		});
		$$('#docExamAttchmain_table a[title="Преглед"]').each(function(link) {
			var cell = link.ancestors()[0];
			var row = cell.ancestors()[0];
			var fileName = (row.childElements())[4].innerHTML;

  			var old = link.href;
  			var ids = old.indexOf('id=') + 3;
  			var id = old.substring(ids, old.length);
  			link.href = '${pathPrefix }/control/doc_exam_attachment/'+fileName+'?id='+id;

			var src = '${pathPrefix }/control/doc_exam_attachment/view?width=${messages.imgPreviewWidth }&amp;id='+id;
			(row.childElements())[6].innerHTML = '<img src="'+src+'" alt=""/>';
		});
		</script>
		</c:if>
        </div>
</fieldset>  

<div class="clr20"><!--  --></div>

<v:form skipsubmitbuttons="true" method="post" name="qualification_examination_form" 
  		  action="${pathPrefix }/control/regprofapplication/save?appId=${id}&amp;type=qualificationexamination&amp;activeForm=4#qualification_examination_form">
  <fieldset id="examination_qualification_field"><legend>Удостоверена квалификация</legend>
  	<nacid:systemmessage name="qualificationExaminationSystemMessage" />
  		<v:comboBoxValidator input="recognizedQualificationDegreeId" required="false"/>
  		<v:comboBoxValidator input="recognizedQualificationLevel" required="$('higherQualificationLevelDiv').visible()&& ${qualExamWebModel.qualDegree!= null}"/>
      <%--
      <v:comboBoxValidator input="grade" required="$('recognizedQualificationTeacher').checked"/>
          <v:comboBoxValidator input="ageRange" required="$('recognizedQualificationTeacher').checked"/>
          <v:comboBoxValidator input="schoolType" required="$('recognizedQualificationTeacher').checked"/>
          --%>
  		<v:textValidator input="recognizedProfession" minLength="2" maxLength="600" required="true"/>
  		<v:hidden id="recognizedProfessionId" name="recognizedProfessionId" value="${qualExamWebModel.recognizedProfessionId }"/>
  		<v:hidden id="qualificationExaminationId" name="qualificationExaminationId" value="${qualExamWebModel.qualExaminationId }"/>
  		<span class="fieldlabel2">
  			<a href="${navetWebSite}" target="_blank">Link към сайта на НАПОО</a>
  		</span>
  		<div class="clr20"><!--  --></div>
   		<p><span class="fieldlabel"><label>Вид обучение: </label></span>${qualExamWebModel.eduType}</p>
	    <div class="clr5"><!--  --></div>
	    <p><span class="fieldlabel"><label>Професионална квалификация по документи: </label></span>${qualExamWebModel.qualBg}</p>
	    <div class="clr5"><!--  --></div>
	    <div id="qualificationLevelByDocumentDiv">
	    	<p><span class="fieldlabel"><label>Квалификационно ниво по документи: </label></span>${qualExamWebModel.qualDegree}</p>
	    	<div class="clr5"><!--  --></div>
	    </div>
	    <div id="secondaryQualificationDegreeDiv" style="display:none;">
	    	<p id="qualification_degree_p"><span class="fieldlabel"><label for="recognizedQualificationDegree">Удостоверено квалификационно ниво</label><br/></span>
	    		<nacid:combobox name="recognizedQualificationDegreeId" attributeName="recognizedQualificationDegree" path="recognizedQualificationDegreeId" style="w200 brd"/>	    		
	    	</p>
	    	<div class="clr5"><!--  --></div>
	    </div>
	    <div id="higherQualificationLevelDiv" style="display:none;">
	    	<p id="qualification_level_p"><span class="fieldlabel"><label for="recognizedQualificationLevel">Удостоверено квалификационно ниво</label><br/></span>
        		<nacid:combobox name="recognizedQualificationLevelId" attributeName="recognizedQualificationLevel" path="recognizedQualificationLevelId" style="w500 brd"/>
       	 	</p>    
       	 	<div class="clr5"><!--  --></div>   	           
	    </div>
	    <div class="clr5"><!--  --></div>	    
	    <p>
            <span class="fieldlabel"><label for="recognizedProfession">Удостоверена професия</label></span>
        	<v:textinput id="recognizedProfession" name="recognizedProfession" value="${qualExamWebModel.recognizedProfession }" class="brd w590" onkeydown="if(!isSpecialKeyPressed(event)) recognizedProfessionIdChanged();"/>
        </p>

        <p><span class="fieldlabel"><label for="recognizedQualificationTeacher">Учител</label></span>
          <v:checkboxinput name="recognizedQualificationTeacher" id="recognizedQualificationTeacher" value="1" checked="${qualExamWebModel.recognizedQualificationTeacher }" style="brd w200" onchange="teacherCheckboxChanged();" />
        </p>
        <div id="teacherDiv">
            <v:flatNomenclatureSelect labelclass="fieldlabel" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADE) %>"
                                      fieldName="Клас"
                                      id="grade" class="brd w600 flt_lft" value="${qualExamWebModel.grade}" windowLevel="1"
                                      nomenclatureValues="gradeCombo" inputClass="brd w600"/>
            </p>
            <p>
                <v:flatNomenclatureSelect labelclass="fieldlabel" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_SCHOOL_TYPE) %>"
                                          fieldName="Вид училище"
                                          id="schoolType" class="brd w600 flt_lft" value="${qualExamWebModel.schoolType}" windowLevel="1"
                                          nomenclatureValues="schoolTypeCombo" inputClass="brd w600"/>
            </p>
            <p>
                <v:flatNomenclatureSelect labelclass="fieldlabel" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_AGE_RANGE) %>"
                                          fieldName="Възрастови групи"
                                          id="ageRange" class="brd w600 flt_lft" value="${qualExamWebModel.ageRange}" windowLevel="1"
                                          nomenclatureValues="ageRangeCombo" inputClass="brd w600"/>

            </p>
        </div>

        <span id="directive_related_span" style="display: none;" >
	    	<label for="articleDirectiveId" class="fieldlabel">по член от Директивата</label>
	    	<nacid:combobox name="articleDirectiveId" attributeName="articleDirectiveCombo" path="articleDirectiveId" style="brd w100"
	    					onchange="updateArticleItems(this.value, $('article_item_combo_span'));"/>
	    	<v:comboBoxValidator input="articleDirectiveId" required="true"/>
	    	<span id="article_item_combo_span">
	    		<nacid:combobox name="articleItemId" attributeName="articleItemCombo" path="articleItemId" style="brd w240" />
	    		<v:comboBoxValidator input="articleItemId" required="true"/>
	    	</span>
	    </span>
        <div class="clr"><!--  --></div>
        <script type="text/javascript">
        var eduType = "${qualExamWebModel.eduTypeShort}";
        showEduTypeDependingDiv(eduType);
        hideQualificationLevelDivIfSDK(eduType);
        new Autocomplete('recognizedProfession', { 
    	    serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=36',
    	    onSelect: updatePofessionData,
    	    width:500
    	});
        function updatePofessionData(value, data) {
        	var d = data.evalJSON(true);
        	$('recognizedProfessionId').value = d.id;
        }
        function recognizedProfessionIdChanged() {
        	var recognizedProfessionId = $('recognizedProfessionId');
        	if(recognizedProfessionId.value != ""){
        		recognizedProfessionId.value = "";
        	}
        }
        function showEduTypeDependingDiv(eduType) {
        	if(eduType == "sec"){
        		$('secondaryQualificationDegreeDiv').show();
        		$('secondaryQualificationDegreeDiv').insert({bottom: $('directive_related_span')});
        	} else if(eduType == "high"){
        		$('higherQualificationLevelDiv').show();
        		$('higherQualificationLevelDiv').insert({bottom: $('directive_related_span')});        		
        	}
        	if(eduType == "sdk"){
        		$$('#directive_related_span label')[0].style.margin = "";
        	}
        	$('directive_related_span').show();
        }
        function updateArticleItems(articleId, updateSpan) {
        	new Ajax.Request('${pathPrefix}/control/article_item_ajax?articleId=' + articleId, {
                onSuccess: function(oXHR) {
                    updateSpan.update(oXHR.responseText);                   
                    if(updateSpan = $('article_item_combo_span_expr')){
                    	$$('#article_item_combo_span_expr select')[0].name = "articleItemExprId";
                    }
                },
                onFailure : function(oXHR) {
        			alert("Възникна грешка при опит за прочитане точките и буквите към този член. Моля опитайте по-късно..." + oXHR.statusText);
        			$("article_item_combo_span").update("");
        		},
                method: 'GET'
              });
        }
  		function hideQualificationLevelDivIfSDK(eduType) {
  			if (eduType == "sdk") {
  				$('qualificationLevelByDocumentDiv').hide();
  			}
  		}
        function teacherCheckboxChanged() {
            if ($('recognizedQualificationTeacher').checked) {
                $('teacherDiv').show();
            } else {
                $('teacherDiv').hide();
            }
        }
        teacherCheckboxChanged();
        </script>
	    <div class="clr"><!--  --></div>
	    <div class="clr20"><!--  --></div>
        <v:submit value="Запис на данните" /> 	    
  	</fieldset>
  	<div class="clr20"><!--  --></div>
</v:form>

<fieldset id="profession_experience_examination_fieldset" style="width: 870px; margin-left: 20px;"><legend class="noForm">Стаж</legend>
	<v:form skipsubmitbuttons="true" method="post" name="profession_experience_examination_form" 
			action="${pathPrefix }/control/regprofapplication/save?appId=${id}&amp;activeForm=4#profession_experience_examination_fieldset">
		<v:hidden name="type" value="experience_examination"/>
		<v:hidden name="experience_examination_id" value="${experience_examination_id}"/>
		<v:hidden name="profession_experience_id" value="${profession_experience_id}"/>
		<v:hidden id="alternativeDocument_recognized" name="alternativeDocument_recognized" />
  		
  			<div class="clr"><!--  --></div>
  			<nacid:systemmessage name="professionExperienceSystemMessage" />
  			<div class="clr"><!--  --></div>
  			<span class="fieldlabel2">
  				<a href="${mlspWebSite}" target="_blank">Link към сайта на МТСП</a>
  			</span>
  			<div class="clr20"><!--  --></div>
  			<p><span class="fieldlabel2"><label>Професия: </label></span>
  			<c:choose>
  				<c:when test="${professionName == '' || professionName == null}">
  					<span>Няма въведена информация за професия</span>
  				</c:when>
  				<c:otherwise>
  					<span>${professionName}</span>
  				</c:otherwise>
  			</c:choose>
  			</p>
  			<div class="clr"><!--  --></div>
  			<p><span class="fieldlabel2"><label>Работил: </label></span>
  			<span id="experiencePeriod"></span>
  			</p>
  			
  			<div class="clr"><!--  --></div>
  			<p class="checkbox"><input ${not_restricted} type="checkbox" id="is_unrestricted" name="is_unrestricted" value="1" ${disable_experience_examination} />
  			<label for="is_unrestricted">Не е отнето правото за упражняване на професията и липса на наложени административни наказания във връзка с упражняване на професията</label></p>
  			<div class="clr"><!--  --></div>
  			<p class="checkbox"><input ${experience_doc_recognized} type="checkbox" id="document_recognized" name="document_recognized" value="1" ${disable_document_recognition}/>
  			<label for="document_recognized">Извършена е проверка на документ</label></p>
  			
  			<span id="directive_related_span_expr">
		    	<label for="articleDirectiveExprId" style="margin-left: 10px;">Член от Директивата</label>
		    	<nacid:combobox name="articleDirectiveExprId" attributeName="articleDirectiveExprCombo" path="articleDirectiveExprId" style="brd w100"
		    					onchange="updateArticleItems(this.value, $('article_item_combo_span_expr'));"/>
		    	<v:comboBoxValidator input="articleDirectiveExprId" required="!$('qualification_examination_form').visible()"/>
		    	<span id="article_item_combo_span_expr">
		    		<nacid:combobox name="articleItemId" attributeName="articleItemComboExpr" path="articleItemExprId" style="brd w200" />
		    		<v:comboBoxValidator input="articleItemExprId" required="!$('qualification_examination_form').visible()"/>
		    	</span>
	    	</span>
	    	
			<div class="clr"><!--  --></div>
			<div class="clr20"><!--  --></div>
			<v:submit value="Запис на данните" />
			<div class="clr20"><!--  --></div>
			</v:form>
			<div class="clr5"><!--  --></div>
			<%-- <c:if test="${experience_doc_recognized != null}">--%>
				<div id="experienceExamAttachments" style="display:none" class="formClass">
		        	<nacid:list attribute="experienceAttachedDocsTableWebModel" tablePrefix="professionExperienceAttachment"></nacid:list>
			        	<script type="text/javascript">
			        	/*
			     		* маха линковете за сортиране на таблицата
			     		*/
			     		$$('#experienceExamAttachments fieldset').each(function (fieldset) { // razshirqva 2-ta fieldset-a nad i pod tablicata
			     			fieldset.style.width = '100%';
			     		});
			     		
			     		$$('#professionExperienceAttachment'+'main_table td[class="dark"] > a').each(function(anchor) {
			      			anchor.up(0).innerHTML = anchor.innerHTML; 
			     		});
			    		$$('#professionExperienceAttachment'+'main_table a[title="Преглед"]').each(function(link) {
			    			var cell = link.ancestors()[0];
			    			var row = cell.ancestors()[0];
			    			var fileName = (row.childElements())[4].innerHTML;
			
			      			var old = link.href;
			      			var ids = old.indexOf('id=') + 3;
			      			var id = old.substring(ids, old.length);
			      			link.href = '${pathPrefix }/control/profession_experience_attachment/'+fileName+'?id='+id;
			
			    			var src = '${pathPrefix }/control/profession_experience_attachment/view?width=${messages.imgPreviewWidth }&amp;id='+id;
			    			(row.childElements())[6].innerHTML = '<img src="'+src+'" alt=""/>';
			    		});
			    		</script>
	    		</div>
	    		<div class="clr5"><!--  --></div>
    		<%--</c:if>--%>
</fieldset>
<div class="clr20"><!--  --></div>

<div class="clr20"><!--  --></div> 
<p class="cc">
	<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1&appNumber=${sessionScope.app_number_filter_value }';" value="Назад" />
</p>

<c:if test="${experience_doc_recognized != null}">
	<script type="text/javascript">
		$('experienceExamAttachments').show();
	</script>
</c:if>
  	
<script type="text/javascript">
disableSourceComboIfAttachments();
disableCheckboxIfAttachments();
showHideDocExamInfoBySource($$('select[name="source"]')[0]);
if ("${instExamWebModel.profInstData}" != "noData" && window.location.pathname.lastIndexOf('view') == -1) {
	$$('#validitiesTable' + 'main_table tbody > tr > td:nth-child(2)').each(function(element) {
		var select = element.childElements()[0];
		var id = element.up(0).down(0).next(1).innerHTML;
		select.enable();
		select.name = "unregulatedbgValidityId";
		select.value = id;
	});
	if ($('documentExaminationId').value == "" || $('documentExaminationDate').value == "дд.мм.гггг") {
		$('documentExaminationDate').value = getToday();
	}
	clearHiddenFieldsForm();
	changeOperationEdit();
	addEditRowToTable();
}
if (window.location.pathname.lastIndexOf('view') != -1) {
	///Make link inactive so that adding new examinations is not available in view mode or when there is no data
	$('newExamination').href = "javascript: void(0);";
	$('newExamination').onclick = function(){};
	$('examinationNotes').disable();
}
if ("${instExamWebModel.profInstData}" == "noData" ) {	
	$("institution_examination_form").hide();
}
if ("${document_data}" == "noData" || "${instExamWebModel.profInstData}" == "noData" ) {
	$('examination_document_field').hide();
}
if ("${qualExamWebModel.qualData}" == "noData") {
	$('qualification_examination_form').hide();
}
if ("${requestScope['com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl'].details.hasExperience}" != 1) {
	$('profession_experience_examination_fieldset').hide();
}

//Insert a specific row (editRow) that will serve the edition
function addEditRowToTable() {
	var insertionString = "<tr id='editRow' style='display: none;' onmouseout='this.style.backgroundColor=\"#ffffff\";' onmouseover='this.style.backgroundColor=\"#e0e0e0\";'"+
	"style='background-color: rgb(255, 255, 255);'><td></td><td><input id='unnregulatedbgValidityId' type='radio' disabled='disabled'/></td>"+
	"<td style='display:none' id='validityId'></td><td><input type='text' id='examDate' class='brd w100'/>"+
	"</td><td><input type='checkbox' id='valid'/></td>"+
	"<td><input type='checkbox' id='hasRights'/></td><td><input type='text' id='validityNotes' class='brd w100' /></td>"+
	"<td><a id='edit_link' href='javascript: void(0);'><img src='${pathPrefix}/img/icon_edit.png'>"+
	"</a><a id='cancel_link' href='javascript: void(0);'><img src='${pathPrefix}/img/icon_delete.png'></a></td>";
	if ($$('#validitiesTablemain_table > tbody')[0] != null) {
		$$('#validitiesTablemain_table > tbody')[0].insert({top: insertionString});	
	} else {
		$('validitiesTablemain_table').insert({top: insertionString});	
	}
}
//Make some changes so that edition is possible
function editData(row) {
	var editRow = $('editRow');
	editRow.show();	
	if (row != null) {
		row.insert({after: editRow});
		row.hide();
		$$("#editRow>td:nth-child(1)")[0].innerHTML = $$('#'+row.id+' >td:nth-child(1)')[0].innerHTML;
		$('unnregulatedbgValidityId').checked = $$('#'+row.id+' >td:nth-child(2)>input')[0].checked;
		$('validityId').innerHTML = $$('#'+row.id+' >td:nth-child(3)')[0].innerHTML;
		$('examDate').value = $$('#'+row.id+' >td:nth-child(4)')[0].innerHTML;
		$('valid').checked = $$('#'+row.id+' >td:nth-child(5) > input')[0].checked;
		$('hasRights').checked = $$('#'+row.id+' >td:nth-child(6) > input')[0].checked;
		$('validityNotes').value = $$('#'+row.id+' >td:nth-child(7)')[0].innerHTML;
		$('cancel_link').onclick = function(){cancelEdition(row.id);};
		$('edit_link').innerHTML = '';
		$('edit_link').insert(new Element('img', {'src' : '${pathPrefix}/img/16x16_save.png', 'style':"margin-right:10px;"}));
		$('edit_link').onclick = function(){refreshHiddenFieldsForm(); saveData(row.id);};
	} else {		
		$$('#validitiesTablemain_table > tbody')[0].insert({bottom: editRow});
		$('examDate').value = getToday();
		$('cancel_link').onclick = function(){cancelEdition("new");};
		$('edit_link').innerHTML = '';
		$('edit_link').insert(new Element('img', {'src' : '${pathPrefix}/img/16x16_save.png', 'style':"margin-right:10px;"}));
		$('edit_link').onclick = function(){refreshHiddenFieldsForm(); saveData("new");};
	}
	makeOtherRowsInactive(row);
}
//Get today's date formatted
function getToday() {
	var today = new Date();
	return today.getDate()+"."+(today.getMonth()+1)+"."+today.getFullYear();
}
//Make edition for every row other than editRow inactive
function makeOtherRowsInactive(row) {
	$$('#validitiesTable' + 'main_table tbody > tr ').each(function(element) {
		if (element != row && element != $('editRow')) {
			$$('#'+element.id+' td:nth-child(2) > input ')[0].disable();
			$$('#'+element.id+' td:nth-child(8) > a ')[0].onclick = function(){};
		}
	});
}
//Make edition for every row active
function makeRowsActive() {
	$$('#validitiesTable' + 'main_table tbody > tr ').each(function(element) {
		$$('#'+element.id+' td:nth-child(2) > input ')[0].enable();
		$$('#'+element.id+' td:nth-child(8) > a ')[0].onclick = function(){editData(this["parentNode"]["parentNode"]);};
	});
}
//Change the edit button of every row
function changeOperationEdit() {
	$$('#validitiesTable' + 'main_table tbody > tr > td:nth-child(8)> a').each(function(element) {	
		element.onclick = function(){editData(this["parentNode"]["parentNode"]);};
		element.href = 'javascript: void(0);';
		
	});
}
//Clear changes made for edition
function cancelEdition(rowId) {
	var editRow = $('editRow');	
	if(rowId != 'new'){			
		$(rowId).show();
	} 
	$('validityId').innerHTML = "";
	$('examDate').value = "";
	$('valid').checked = false;
	$('hasRights').checked = false;
	$('validityNotes').value = "";
	$('edit_link').onclick = function(){};
	$('edit_link').innerHTML = '';
	$('edit_link').insert(new Element('img', {'src' : '${pathPrefix}/img/icon_edit.png'}));
	$('cancel_link').onclick = function(){};
	editRow.hide();
	makeRowsActive();
}
//Get the data from the inserted row and put it in the form
function refreshHiddenFieldsForm() {
	$('hiddenValidityId').value = $('validityId').innerHTML;
	var dateInpt = $$('#institution_validation_form input[id="examinationDate"]')[0];
	var notes = $$('#institution_validation_form input[id="notes"]')[0];
	dateInpt.value = $('examDate').value;
	var hasRights = $('hasRights').checked ? 1 : 0;
	$('hasRightsEducate').value = hasRights;
	var valid = $('valid').checked ? 1 : 0;
	$('isLegitimate').value = valid;
	notes.value = $('validityNotes').value;
}
//When the data in the form is no longer needed
function clearHiddenFieldsForm() {
	$('hiddenValidityId').value = "";
	$('examinationDate').value = "";
	$('hasRightsEducate').value = "";
	$('isLegitimate').value = "";
	$('notes').value = "";
}
//Check if date is ok and submit the hidden form
function saveData(rowId) {
	var dateInpt = $$('#institution_validation_form input[id="examinationDate"]')[0];
	var res = validateDate(dateInpt, 'd.m.yyyy', false, true, true);
	if (res == true) {
		Form.request($('institution_validation_form'), {
			onSuccess: function(transport) { 
				$('correct_message_div').show();
				$('error_message_div').hide();
				var json = transport.responseText.evalJSON(true);
				///add permanently the row to the table or change permanently the existing row
				makeChangesPermanent(rowId, json);
			},
			onFailure: function() {
				$('correct_message_div').hide();
				$('error_message_div').show();
				//Hide edit row and stop the changes
				cancelEdition(rowId);
			}
		});
	} else {
		$('examDate').style.borderColor = 'red';
		alert("Моля, коригирайте полетата в червено!");
	}
}

function makeChangesPermanent(rowId, j) {	
	if(rowId == "new"){
		var rowNum = $('validitiesTablemain_table').rows.length;
		var newRow = $('validitiesTablemain_table').insertRow(rowNum-1);
		newRow.id = "validitiesTablerow_"+(rowNum-1);
		var cell1 = newRow.insertCell(0);
		cell1.innerHTML = rowNum-1;
		var cell2 = newRow.insertCell(1);
		var radio = new Element("input");
		radio.type = "radio";
		radio.disabled = "disabled";
		radio.name = "unregulatedbgValidityId";
		radio.value = j.validation_returned_id;
		cell2.appendChild(radio);
		var cell3 = newRow.insertCell(2);
		cell3.id = "piValId"+rowNum;
		cell3.innerHTML = j.validation_returned_id;
		$('piValId'+rowNum).hide();
		var cell4 = newRow.insertCell(3);
		cell4.innerHTML = $('examDate').value;
		var cell5 = newRow.insertCell(4);
		var checkbox1 = new Element("input", {'type': 'checkbox', 'disabled': 'disabled'});		
		cell5.appendChild(checkbox1);
		var cell6 = newRow.insertCell(5);
		var checkbox2 = new Element("input", {'type': 'checkbox', 'disabled': 'disabled'});	
		cell6.appendChild(checkbox2);
		var cell7 = newRow.insertCell(6);
		cell7.innerHTML = $('validityNotes').value;
		var cell8 = newRow.insertCell(7);
		var a = new Element('a');
		var pic = new Element("img");
		pic.src = "${pathPrefix}/img/icon_edit.png";
		a.appendChild(pic);
		a.href = "javascript:void(0);";
		a.onclick= function(){editData(this["parentNode"]["parentNode"]);};
		cell8.appendChild(a);
		checkbox1.checked = $('valid').checked;
		checkbox2.checked = $('hasRights').checked;
	} else {
		$$('#'+rowId+' >td:nth-child(4)')[0].innerHTML = $$('#institution_validation_form input[id="examinationDate"]')[0].value;
		$$('#'+rowId+'>td:nth-child(5) > input')[0].checked = $$('#institution_validation_form input[id="isLegitimate"]')[0].value == 1 ? true : false;
		$$('#'+rowId+'>td:nth-child(6) > input')[0].checked =  $$('#institution_validation_form input[id="hasRightsEducate"]')[0].value == 1 ? true : false;
		$$('#'+rowId+'>td:nth-child(7) ')[0].innerHTML = $$('#institution_validation_form input[id="notes"]')[0].value;
	}
	cancelEdition(rowId);
}

function showHideDocExamInfoBySource(select) {
	if (select[select.selectedIndex].value == 1) {
		$('documentExamAttachments').hide();
		$('documentExaminationScreenshot').show();
		$('screenshot').enable();
	} else if (select[select.selectedIndex].value == 2) {
		$('documentExamAttachments').show();
		$('documentExaminationScreenshot').hide();
		$('screenshot').disable();
	}
}

function disableSourceComboIfAttachments() {
	var attachmentCount;
	if ($('docExamAttchmain_table') != null) {
		attachmentCount = $('docExamAttchmain_table').rows.length-1;
		if (attachmentCount > 0 && $$('select[name="source"]')[0].value == 2) {
			$('alternativeSource').value = $$('select[name="source"]')[0].value;
			$$('select[name="source"]')[0].disable();
		} else if(attachmentCount == 0) {
			$$('select[name="source"]')[0].enable();
		}
	}	
}

function disableCheckboxIfAttachments(){
	var attachmentCount;
	if ($('professionExperienceAttachmentmain_table') != null) {
		attachmentCount = $('professionExperienceAttachmentmain_table').rows.length-1;
		if (attachmentCount > 0 && $('document_recognized').checked == true) {
			$('alternativeDocument_recognized').value = 1;
			$('document_recognized').disable();
		} else if(attachmentCount == 0) {
			$('document_recognized').enable();
		}
	}	
}
$$('#article_item_combo_span_expr select')[0].name = "articleItemExprId";
fillExperiencePeriod();
toggleArchiveNumber($('status'), $('archive_number_container'), $('archive_number'));
<%--document.observe("dom:loaded", function() {

	//toggleExperienceCheckbox();
});--%>

if (Prototype.Browser.IE && $('docExamAttchmain_table') != null) {
	$('docExamAttchmain_table').addClassName('flt_lft'); // IE7 layout fix
}
</script>
<!-- End of RayaWritten -->  