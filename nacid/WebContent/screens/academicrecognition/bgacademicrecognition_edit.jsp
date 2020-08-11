<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ include file="/screens/header.jsp" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } диплома</span></h3>

	<v:form action="${pathPrefix }/control/bgacademicrecognition/save" method="post" 
			name="form_bgacademicrecognition" backurl="${pathPrefix }/control/bgacademicrecognition/list?getLastTableState=1" modelAttribute="com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionInfoImpl">

		<nacid:systemmessage />
		
		<v:textValidator input="applicant" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" required="true" />
		<v:textValidator input="citizenship" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="university" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="universityCountry" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="educationLevel" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="diplomaSpeciality" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="diplomaNumber" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="diplomaDate" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="protocolNumber" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="denialProtocolNumber" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="recognizedSpeciality" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="inputNumber" maxLength="150" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" required="true"/>
		<v:textValidator input="outputNumber" maxLength="150" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" required="true"/>
		<v:textValidator input="notes" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:comboBoxValidator input="recognizedUniversityId" required="true" />
		<v:hidden id="id" name="id" path="id" />
		<fieldset><legend>Основни данни</legend>
			
      		<p><span class="fieldlabel2"><label for="applicant">Заявител</label><br/></span>
         		<v:textinput id="applicant" class="brd w500" name="applicant" path="applicant" />
      		</p>
      		<div class="clr"><!--  --></div>      
      		<p><span class="fieldlabel2"><label for="citizenship">Гражданство</label><br/></span>
                <v:textinput id="citizenship" class="brd w500" name="citizenship" path="citizenship" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="university">Наименование на чуждестранното висше училище, издало дипломата</label><br/></span>
                <v:textinput id="university" class="brd w500" name="university" path="university" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="universityCountry">Държава по седалище на ЧВУ, издало дипломата</label><br/></span>
                <v:textinput id="universityCountry" class="brd w500" name="universityCountry" path="universityCountry" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="educationLevel">Придобита ОКС</label><br/></span>
                <v:textinput id="educationLevel" class="brd w500" name="educationLevel" path="educationLevel" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="diplomaSpeciality">Придобита специалност</label><br/></span>
                <v:textinput id="diplomaSpeciality" class="brd w500" name="diplomaSpeciality" path="diplomaSpeciality" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="diplomaNumber">Диплома Номер</label><br/></span>
                <v:textinput id="diplomaNumber" class="brd w500" name="diplomaNumber" path="diplomaNumber" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="diplomaDate">Диплома, дата на издаване</label><br/></span>
                <v:textinput id="diplomaDate" class="brd w500" name="diplomaDate" path="diplomaDate" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="protocolNumber">Решение на АС на ВУ (протокол номер/дата)</label><br/></span>
                <v:textinput id="protocolNumber" class="brd w500" name="protocolNumber" path="protocolNumber" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="denialProtocolNumber">Решение на АС на ВУ (отказ/ професионален бакалавър / бакалавър / магистър)</label><br/></span>
                <v:textinput id="denialProtocolNumber" class="brd w500" name="denialProtocolNumber" path="denialProtocolNumber" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="recognizedSpeciality">Призната специалност</label><br/></span>
                <v:textinput id="recognizedSpeciality" class="brd w500" name="recognizedSpeciality" path="recognizedSpeciality" />
            </p>
           <%--
           <div class="clr"><!--  --></div>
            <p>
              <span class="fieldlabel2"><label>${messages.University }</label></span> 
              <span id="university_span"> 
                   <v:hidden name="recognizedUniversityId" id="recognizedUniversityId" path="recognizedUniversityId" />
                   <v:textinput class="brd w500" name="recognizedUniversity" id="recognizedUniversity" />
              </span>
                 <script type="text/javascript">
               new Autocomplete('recognizedUniversity', { 
                     serviceUrl:'${pathPrefix}/control/universitysuggest',
                     width:500,
                     onSelect:updateNomenclature,
                     useBadQueriesCache:false,
                     dynamicParameters: function (){
                         return {country_id : 34};
                     }
               });
               
           </script>
           --%>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="recognizedUniversityId">Български университет, признал дипломата</label><br/></span>
                <nacid:combobox name="recognizedUniversityId" attributeName="recognizedUniversities" path="recognizedUniversityId" id="recognizedUniversityId" style="brd w500"/>
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="inputNumber">Входящ номер</label><br/></span>
               <v:textinput id="inputNumber" class="brd w240" name="inputNumber" path="inputNumber" />
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="outputNumber">Изходящ номер</label><br/></span>
               <v:textinput id="outputNumber" class="brd w240" name="outputNumber" path="outputNumber" />
           </p>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="recognitionStatusId">Статус</label><br/></span>
           		<nacid:combobox name="recognitionStatusId" attributeName="recognitionStatuses" path="recognitionStatusId" id="recognitionStatusId" style="brd w240"/>
           </p>
           <div class="clr"><!--  --></div>
           <div>
           		<br/>
           		<div id="errorSimilar" style="display:none" class="error">Изберете сходна диплома</div>
           		<jsp:include page="bgacademicrecognition_similar_part.jsp">
					<jsp:param value="${similar}" name="similar"/>
				</jsp:include>
				<br/>
           </div>
           <div class="clr"><!--  --></div>
           <p><span class="fieldlabel2"><label for="notes">Забележки</label><br/></span>
               <v:textarea id="notes" class="brd w500" name="notes" path="notes" />
           </p>
           <p style="display:none">
           		<v:dateinput id="createdDate"  name="createdDate" path="createdDate" pattern="yyyy-MM-dd HH:mm:ss.SSS"/>
           </p>
           <v:hidden id="relatedRecognitionId"  name="relatedRecognitionId" path="relatedRecognitionId"></v:hidden>
		</fieldset>

	</v:form>
	<script type="text/javascript">
		setInitialChosen();
	
		function setInitialChosen(){
			var val = $("relatedRecognitionId").value;	
			$$(".similar_checkbox").each(function(ch){
				if(ch.value+"" == val+""){
					ch.checked = true;
				}
			});
		}
		
		function setChosen(newVal){
			$("relatedRecognitionId").value = newVal;
			setChosenChecked(newVal);
		}
		
		function setChosenChecked(chosen){
			$$(".similar_checkbox").each(function(ch){
				if(ch.value+"" == chosen+""){
					if(ch.checked == true){
						$("relatedRecognitionId").value = chosen;
					} else {
						$("relatedRecognitionId").value = "";
					}
				} else {
					ch.checked = false;
				}
			});
		}
		 
		function validateSimilarSelected() {
			var count = $$(".similar_checkbox").length;
			var ret = true;
			var val = $("relatedRecognitionId").value;
			if(count > 0 && val == ""){
				$("errorSimilar").show();
				ret = false;
			} else {
				$("errorSimilar").hide();
			}
			return ret;
		}
	</script>

<%@ include file="/screens/footer.jsp" %>