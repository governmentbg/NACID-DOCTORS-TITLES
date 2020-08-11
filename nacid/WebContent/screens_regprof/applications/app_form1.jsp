<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_common_taglib.tld" prefix="nct" %> 
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!-- RayaWritten -->
<script type="text/javascript">
var defaultCountryId = <%= NomenclaturesDataProvider.COUNTRY_ID_BULGARIA %>;
function toggleBulgariaContact(select) {
	  if (select[select.selectedIndex].value == defaultCountryId) {
		  $("bulgaria_contact").hide();
		} else {
			$("bulgaria_contact").show();
		}
}

function updateApplicantData(value, data) {
	updateCivilIdData(data, "applicant");
	$("applicationDetails.applicantId").value = data.evalJSON(true).recordid;
}
function updateRepresentativeData(value, data) {
	  updateCivilIdData(data, "representative");
	  $("applicationDetails.representativeId").value = data.evalJSON(true).recordid;
}
function updateCivilIdData(data, name) {
	var d = data.evalJSON(true);
	$(name + '.id').value = d.recordid;
	$(name + '.fName').value = d.fname;
	$(name + '.sName').value = d.sname;
	$(name + '.lName').value = d.lname;
	clearInputError($(name + '.fName'));
	clearInputError($(name + '.lName'));
	if (name == "applicant") {
		$(name + '.birthCountryId').value = d.birthcountryid;
	  $(name + '.birthCity').value = d.birthcity;
	  $(name + '.citizenshipId').value = d.citizenshipid;
	  $(name + '.birthDate').value = d.birthdate;
	  $(name+"Documents.dateOfIssue").value = d.dateofissue;
	  $(name+"Documents.id").value = d.documentid;
	  $(name+"Documents.number").value = d.number;
	  $(name+"Documents.issuedBy").value = d.issuedby;
	  setApplicantDocumentIdInDetails();
	  saveDocumentValues();
	}
	
	disablePersonInputs(name);

	//Slaga ignore na EGN validator-a, zashtoto ako e izbran zapis ot bazata s greshno EGN nqma nujda ot validaciq na egn-to!!!
	$(name + '.civilIdIgnoreEGNValidation').checked = true;
	$(name + '.civilIdIgnoreLNCHValidation').checked = true;
}

function disablePersonInputs(name) {
	$(name + '.fName').disable();
	$(name + '.sName').disable();
	$(name + '.lName').disable();
	if (name == "applicant") {
	  $(name + '.birthCountryId').disable();
	  $(name + '.birthCity').disable();
	  $(name + '.citizenshipId').disable();
	  $(name + '.birthDate').disable();
	}
}
function getRadioChecked(radio) {
	for (var i = 0; i < radio.length; i++) {
	    if (radio[i].checked) {
	     return radio[i].value;
	    }    
	}
	return null;
}
function personalIdChanged(name) {
	if ($(name + '.id').value != "") {
	  enablePersonInputs(name);
	}
	clearInputError($(name + '.civilId'));
  initPersonalIdIgnoreValidation(name);  
}


	function enablePersonInputs(name) {
		//Maha ignore na EGN validator-a
		$(name + '.civilIdIgnoreEGNValidation').checked = false;
		$(name + '.civilIdIgnoreLNCHValidation').checked = false;

		$(name + '.id').clear();
		$(name + '.fName').clear();
		$(name + '.sName').clear();
		$(name + '.lName').clear();

		$(name + '.fName').enable();
		$(name + '.sName').enable();
		$(name + '.lName').enable();
		if (name == "applicant") {
			$(name + '.birthCountryId').value = '-';
			$(name + '.birthCity').clear();
			$(name + '.birthCountryId').enable();
			$(name + '.birthCity').enable();

			$(name + '.citizenshipId').value = '-';
			$(name + '.birthDate').clear();
			$(name + '.citizenshipId').enable();
			$(name + '.birthDate').enable();
			$(name + "Documents.dateOfIssue").clear();
			$(name + "Documents.id").clear();
			$(name + "Documents.number").clear();
			$(name + "Documents.issuedBy").clear();
			setApplicantDocumentIdInDetails();
		}
	}
	function initPersonalIdIgnoreValidation(name) {
		$(name + '.civilIdIgnoreEGNValidation').checked = !($(name + '.civilIdType_rad_0').checked) || $(name + '.id').value != '';
		$(name + '.civilIdIgnoreEGNValidation').ancestors().first().hide();
		$(name + '.civilIdIgnoreLNCHValidation').checked = !($(name + '.civilIdType_rad_1').checked) || $(name + '.id').value != '';
		$(name + '.civilIdIgnoreLNCHValidation').ancestors().first().hide();
	}
	function showHideRepresentativeData() {
		if ($('representative_data').checked) {
			$('representative_data_div').show();
		} else {
			$('representative_data_div').hide();
		}
		refreshRepresentativeAdressAndCompany();
	}
	function clearDataIfUnchecked() {
		if ($('representative_data').checked == false) {
			$('applicationDetails.representativeId').value = "";
			$('applicationDetails.repPhone').value = "";
			$('applicationDetails.repAddrDetails').value = "";
			$('applicationDetails.repEmail').value = "";
			$('representative.civilId').value = "";
			$('representative.fName').value = "";
			$('representative.sName').value = "";
			$('representative.lName').value = "";
			$('repr_from_company').checked = false;
			$('applicationDetails.repFromCompany').value = "-";
		}
		if ($('repr_from_company').checked == true
				&& $('representative_data').checked == true) {
			$('applicationDetails.repAddrDetails').value = "";
			$('applicationDetails.repEmail').value = "";
		}

	}
	function showHideTrainingCourseData() {
		if ($('applicationDetails.namesDontMatch').checked) {
			$("names_dont_match_div").show();
		} else {
			$("names_dont_match_div").hide();
		}
	}
	function emptyPersonalId(name) {
		$(name + '.civilId').clear();
		personalIdChanged(name);
	}
	function setApplicantDocumentIdInDetails() {
		if ($('applicantDocuments.id').value != null) {
			$('applicationDetails.applicantDocumentsId').value = $('applicantDocuments.id').value;
		}
	}
	function additionalEndDateValidation() {
		var ret = true;
		var endDate = getDateFromFormat($('applicationDetails.endDate').value,
				"d.m.yyyy");
		var today = new Date().getTime();
		if (endDate <= today) {
			setErrorOnInput($('applicationDetails.endDate'),
					"Датата не може да бъде по-малка от днешната");
			ret = false;
		}
		return ret;
	}
	/*function validateResponsibleUser() {
		var ret = true;
		if ($('responsibleUserCombo').value == "-"
				&& ($('responsibles_table') == null || $('responsibles_table').rows.length < 1)
				&& window.location.pathname.lastIndexOf('new') == -1) {
			setErrorOnInput($('responsibleUserCombo'),
					"Трябва да изберете поне един отговорник!");
			ret = false;
		}
		return ret;
	}*/
	function documentReceiveMethodChanged(el) {
		var hasDocumentRecipient = el.getAttribute("attr-has-document-recipient") === "true";
		if (hasDocumentRecipient) {
			$("documentRecipientDiv").show();
		} else {
			$("documentRecipientDiv").hide();
		}
	}
	function copyContactAddress() {
		$("documentRecipient.address").value = $("applicationDetails.applicantAddrDetails").value;
		$("documentRecipient.postCode").value = "";
		$("documentRecipient.city").value = $("applicationDetails.applicantCity").value;
		$("documentRecipient.mobilePhone").value = $("applicationDetails.applicantPhone").value;
		$("documentRecipient.countryId").value = $("applicationDetails.applicantCountryId").value;
		$("documentRecipient.district").value = "";
		$("documentRecipient.name").value = $("applicant.fName").value + " " + ($("applicant.sName").value == "" ? "" : $("applicant.sName").value + " ") + $("applicant.lName").value;
	}
</script>

<h3 class="title"><span>${messages.applicationdata }</span></h3>
<nacid:systemmessage name="applicationStatusMessage"/>
<h3 class="names">${application_header }</h3>

<v:form action="${pathPrefix }/control/regprofapplication/save" method="post" 
			name="appform1" backurl="${pathPrefix }/control/regprofapplication/list?getLastTableState=1&appNumber=${sessionScope.app_number_filter_value }"
			 modelAttribute="com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl" onsubmit="clearDataIfUnchecked();" 
			 additionalvalidation="additionalEndDateValidation()">
			 
		<v:textValidator input="applicant.fName" maxLength="50" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="applicant.sName" maxLength="50" regex="${validations.name}" errormessage="${messages.err_name}"/>
		<v:textValidator input="applicant.lName" maxLength="50" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="applicant.birthCity" maxLength="30" required="false" regex="${validations.city}" errormessage="${messages.err_city}" />
		<v:textValidator input="applicant.civilId" maxLength="($('applicant.civilIdType_rad_1').checked ? 10 : 50)" required="true"/>
    	<v:dateValidator input="applicant.birthDate" format="d.m.yyyy" required="false" beforeToday="true"  />
    	<v:dateValidator input="applicationDetails.appDate" format="d.m.yyyy" />
		<v:dateValidator input="applicationDetails.endDate" format="d.m.yyyy"/>
																	
		<v:textValidator input="applicationDetails.applicantEmail" maxLength="100" required="false" regex="${validations.email }" />
		<v:textValidator input="applicationDetails.applicantPhone" maxLength="50" />
   		<v:textValidator input="applicationDetails.applicantAddrDetails" maxLength="255" />
   		<v:textValidator input="applicationDetails.applicantCity" maxLength="30" />
   		
   		<v:textValidator input="representative.fName" maxLength="50" required="$('representative_data').checked" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="representative.sName" maxLength="50" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="representative.lName" maxLength="50" required="$('representative_data').checked" regex="${validations.name}" errormessage="${messages.err_name}" />				
    	<v:textValidator input="representative.civilId" maxLength="($('representative.civilIdType_rad_1').checked ? 10 : 50)" required="$('representative_data').checked" />
    	
   		<v:textValidator input="applicationDetails.repEmail" maxLength="100" required="false" regex="${validations.email }" />
    	<v:textValidator input="applicationDetails.repPhone" maxLength="50"  />
    	<v:textValidator input="applicationDetails.repAddrDetails" maxLength="255" />
    	<v:comboBoxValidator input="applicationDetails.repFromCompany" required="($('representative_data').checked == true && $('repr_from_company').checked == true)" 
    						 errormessage="Моля, въведете фирма от списъка"/>
    	
	    	<v:textValidator input="applicantDocuments.number" maxLength="50" required="$('document_data').checked && (($('applicantDocuments.dateOfIssue').value != 'дд.мм.гггг' && $('applicantDocuments.dateOfIssue').value != '') || $('applicantDocuments.issuedBy').value != '')"/>
	    	<v:textValidator input="applicantDocuments.issuedBy" maxLength="100" required="$('document_data').checked && (($('applicantDocuments.dateOfIssue').value != 'дд.мм.гггг' && $('applicantDocuments.dateOfIssue').value != '') || $('applicantDocuments.number').value != '')"/>
	    	<v:dateValidator input="applicantDocuments.dateOfIssue" format="d.m.yyyy" required="$('document_data').checked && ($('applicantDocuments.issuedBy').value != '' || $('applicantDocuments.number').value != '')" beforeToday="true"  />
			<v:comboBoxValidator input="applicationDetails.applicantPersonalIdDocumentType" required="true" errormessage="Моля въведете тип на документа за самоличност"/>
		
		<v:textValidator input="trCourseDocumentPersonDetails.documentFname" maxLength="50" required="$('applicationDetails.namesDontMatch').checked" regex="${validations.name}"
						 errormessage="${messages.err_name}" />
		<v:textValidator input="trCourseDocumentPersonDetails.documentSname" maxLength="50" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="trCourseDocumentPersonDetails.documentLname" maxLength="50" required="$('applicationDetails.namesDontMatch').checked" regex="${validations.name}" 
						 errormessage="${messages.err_name}" />				
    	<v:textValidator input="trCourseDocumentPersonDetails.documentCivilId" maxLength="($('trCourseDocumentPersonDetails.documentCivilIdType_rad_1').checked ? 10 : 50)"
    					 required="$('applicationDetails.namesDontMatch').checked"/>
    					 
   		<v:comboBoxValidator input="applicationDetails.serviceTypeId" required="true"/>
   		<v:comboBoxValidator input="applicationDetails.applicationCountryId" required="true"/>

	<v:textValidator input="documentRecipient.name" maxLength="255" required="$('documentRecipientDiv').visible()"/>
	<v:textValidator input="documentRecipient.district" maxLength="100" required="$('documentRecipientDiv').visible()"/>
	<v:textValidator input="documentRecipient.city" maxLength="100" required="$('documentRecipientDiv').visible()"/>
	<v:textValidator input="documentRecipient.address" required="$('documentRecipientDiv').visible()"/>
	<v:comboBoxValidator input="documentRecipient.countryId" required="$('documentRecipientDiv').visible()"/>
    	
		<nacid:systemmessage name="applicationSystemMessage"/>
		<v:hidden id="activeForm" value="1" name="activeForm"/>
		<v:hidden id="applicationDetails.id" path="applicationDetails.id" name="applicationDetails.id" />
		<fieldset><legend>Основни данни</legend>
			<div class="clr"><!--  --></div>
			
			<v:hidden id="applicant.id" path="applicant.id" name="applicant.id" />
			<v:hidden id="applicationDetails.applicantId" path="applicationDetails.applicantId" name="applicationDetails.applicantId" />			
			<v:hidden id="applicationDetails.appNum" path="applicationDetails.appNum" name="applicationDetails.appNum" />
			<div style="display: none;">
				<v:dateinput id="applicationDetails.appDate" path="applicationDetails.appDate" name="applicationDetails.appDate" />
			</div>
			<v:hidden id="applicationDetails.trainingCourseId" path="applicationDetails.trainingCourseId" name="applicationDetails.trainingCourseId" />
			
			<p><span class="fieldlabel2"><label for="applicant.civilIdType">Тип персонален идентификатор</label><br/></span>
				<nacid:radiobutton name="applicant.civilIdType" attributeName="applicantCivilIdType" singleline="true" onclick="emptyPersonalId('applicant');"/>
			</p>
			<div class="clr"><!--  --></div> 						
			
           <p><span class="fieldlabel2"><label for="applicant.civilId">${messages.civilIdType}</label><br/></span>
               <v:textinput id="applicant.civilId" name="applicant.civilId" path="applicant.civilId" 
               				onkeydown="if(!isSpecialKeyPressed(event)) personalIdChanged('applicant');" class="brd w200"/>
            </p>
            <div class="clr"><!--  --></div>
            
            <v:egnLnchValidator input="applicant.civilId" />
			<div class="clr"><!--  --></div>
			
            <script type="text/javascript">
	 		   new Autocomplete('applicant.civilId', { 
	    	    serviceUrl:'/nacid_regprof/control/civilidsuggestion',
	    	    // callback function:
	    	    onSelect: updateApplicantData,
	    	    width:500,
	    	    dynamicParameters: function (){
	    	      return {civilidtype : getRadioChecked($('appform1')['applicant.civilIdType'])};
	    	    }
	    	  });
	  		</script> 
	  		
			<p><span class="fieldlabel2"><label for="applicant.fName">Име</label><br/></span>
         		<v:textinput id="applicant.fName" class="brd w500" name="applicant.fName" path="applicant.fName" />
      		</p>
      		<div class="clr"><!--  --></div>   
      		   
      		<p><span class="fieldlabel2"><label for="applicant.sName">Презиме</label><br/></span>
         		<v:textinput id="applicant.sName" class="brd w500" name="applicant.sName" path="applicant.sName" />
      		</p>
      		<div class="clr"><!--  --></div> 
      		
      		<p><span class="fieldlabel2"><label for="applicant.lName">Фамилия</label><br/></span>
         		<v:textinput id="applicant.lName" class="brd w500" name="applicant.lName" path="applicant.lName" />
      		</p>
      		<div class="clr"><!--  --></div> 
      		
      		<p><span class="fieldlabel2"><label for="applicant.birthDate">${messages.birthDate}</label><br/></span>
	        	<v:dateinput id="applicant.birthDate" class="brd w200" name="applicant.birthDate" path="applicant.birthDate" />
	    	</p>
      		<div class="clr"><!--  --></div>
      		
      		<p><span class="fieldlabel2"><label for="applicant.birthCountryId">${messages.birthCountry}</label><br/></span>
                <nacid:combobox name="applicant.birthCountryId" attributeName="applicant.birthCountryId" path="applicant.birthCountryId" id="applicant.birthCountryId" style="brd w500"/>
            </p>
            <div class="clr"><!--  --></div>
      		
      		<p><span class="fieldlabel2"><label for="applicant.birthCity">${messages.birthCity}</label><br/></span>
                <v:textinput id="applicant.birthCity" class="brd w500" name="applicant.birthCity" path="applicant.birthCity" />
            </p>
            <div class="clr"><!--  --></div>
            
            <p><span class="fieldlabel2"><label for="applicant.citizenshipId">Гражданин на</label><br/></span>
                <nacid:combobox name="applicant.citizenshipId" attributeName="applicant.citizenshipId" path="applicant.citizenshipId" id="applicant.citizenshipId" style="brd w500"/>
            </p>
            <div class="clr"><!--  --></div>
            
            
            <fieldset><legend>Данни за документ за самоличност</legend>
			<div class="clr"><!--  --></div>
            	<v:hidden id="applicantDocuments.id" path="applicantDocuments.id" name="applicantDocuments.id"/>
            	<v:hidden id="applicationDetails.applicantDocumentsId" path="applicationDetails.applicantDocumentsId" name="applicationDetails.applicantDocumentsId"/>
            		<p class="checkbox"><input value="1" type="checkbox" id="document_data" name="document_data"
         			onclick="refreshDocumentFillForm();"/>
            		<label for="document_data">Въведи нов документ</label></p>
            		<div class="clr"><!--  --></div>
            		<p><span class="fieldlabel2"><label for="applicantDocuments.number">Номер на документ</label><br/></span>
         				<v:textinput id="applicantDocuments.number" class="brd w500" name="applicantDocuments.number" path="applicantDocuments.number" />
      				</p>
      				
      				<div class="clr"><!--  --></div>   
      		  	 	
      				<p><span class="fieldlabel2"><label for="applicantDocuments.dateOfIssue">Дата на издаване</label><br/></span>
         				<v:dateinput id="applicantDocuments.dateOfIssue" class="brd w500" name="applicantDocuments.dateOfIssue" path="applicantDocuments.dateOfIssue" />
      				</p>
      				<div class="clr"><!--  --></div> 
      				
      				<p><span class="fieldlabel2"><label for="applicantDocuments.issuedBy">Издаден от</label><br/></span>
         				<v:textinput id="applicantDocuments.issuedBy" class="brd w500" name="applicantDocuments.issuedBy" path="applicantDocuments.issuedBy" />
      				</p>
      				<div class="clr"><!--  --></div>
					<p><span class="fieldlabel2"><label for="applicationDetails.applicantPersonalIdDocumentType">Тип на документа за самоличност</label><br/></span>
						<nacid:combobox name="applicationDetails.applicantPersonalIdDocumentType" path="applicationDetails.applicantPersonalIdDocumentType" id="applicationDetails.applicantPersonalIdDocumentType" attributeName="applicationDetails.applicantPersonalIdDocumentType" style="brd w500"/>
					</p>
					<div class="clr"><!--  --></div>
      	
            </fieldset>
            <script type="text/javascript">           
	            var docid = "";
				var docnumber = "";
				var docdateOfIssue = "";
				var docissuedBy = "";
				var doctype = "";
	            	function refreshDocumentFillForm(){
	            		if($('document_data').checked){
	            			$("applicantDocuments.id").value = null;
	            			$("applicantDocuments.number").value = "";
	            			$("applicantDocuments.dateOfIssue").value = "";
	            			$("applicantDocuments.issuedBy").value = "";
	            			$("applicantDocuments.number").enable();
	            			$("applicantDocuments.dateOfIssue").enable();
	            			$("applicantDocuments.issuedBy").enable();
	            		} else {
	            			$("applicantDocuments.id").value = docid;
	            			$("applicantDocuments.number").value = docnumber;
	            			$("applicantDocuments.dateOfIssue").value = docdateOfIssue;
	            			$("applicantDocuments.issuedBy").value = docissuedBy;
	            			$("applicantDocuments.number").disable();
	            			$("applicantDocuments.dateOfIssue").disable();
	            			$("applicantDocuments.issuedBy").disable();

	            		}
	            	}
	            	function saveDocumentValues(){
	            		docid = $("applicantDocuments.id").value;
	         			docnumber = $("applicantDocuments.number").value;
	         			docdateOfIssue = $("applicantDocuments.dateOfIssue").value;
	         			docissuedBy = $("applicantDocuments.issuedBy").value;
	            	}
	            	saveDocumentValues();
	            	setApplicantDocumentIdInDetails();
	            	refreshDocumentFillForm();
            	</script>           
		</fieldset>
		
		<fieldset><legend>За контакт</legend>
			<v:hidden id="applicationDetails.status" path="applicationDetails.status" name="applicationDetails.status" />
            <v:hidden id="applicationDetails.docflowStatusId" path="applicationDetails.docflowStatusId" name="applicationDetails.docflowStatusId" />
			<p><span class="fieldlabel2"><label for="applicationDetails.applicantEmail">Електронна поща</label><br/></span>
                <v:textinput id="applicationDetails.applicantEmail" class="brd w500" name="applicationDetails.applicantEmail" path="applicationDetails.applicantEmail" onkeyup="cummunicateByEmailRefresh();"/>
            </p>
            <div class="clr"><!--  --></div>
            
            <p><span class="fieldlabel2"><label for="applicationDetails.applicantPhone">Телефон</label><br/></span>
                <v:textinput id="applicationDetails.applicantPhone" class="brd w500" name="applicationDetails.applicantPhone" path="applicationDetails.applicantPhone" />
            </p>
            <div class="clr"><!--  --></div>
            
            <p><span class="fieldlabel2"><label for="applicationDetails.applicantCountryId">Държава</label><br/></span>
                <nacid:combobox name="applicationDetails.applicantCountryId" attributeName="applicationDetails.applicantCountryId" path="applicationDetails.applicantCountryId" id="applicationDetails.applicantCountryId" style="brd w500"/>
            </p>
            <div class="clr"><!--  --></div>
            
            <p><span class="fieldlabel2"><label for="applicationDetails.applicantArea">Населено място</label><br/></span>
                <v:textinput id="applicationDetails.applicantCity" class="brd w500" name="applicationDetails.applicantCity" path="applicationDetails.applicantCity" />
            </p>
            <div class="clr"><!--  --></div>
            
			<p><span class="fieldlabel2"><label for="applicationDetails.applicantAddrDetails">Адрес за контакт</label><br/></span>
          		<v:textarea id="applicationDetails.applicantAddrDetails" class="brd w500" name="applicationDetails.applicantAddrDetails" path="applicationDetails.applicantAddrDetails" />
          	</p>       	 
		</fieldset>			
		<div class="clr20"><!--  --></div>
		
		<p class="checkbox"><input ${names_dont_match} type="checkbox" name="applicationDetails.namesDontMatch" 
		id="applicationDetails.namesDontMatch" value="1"  onclick="showHideTrainingCourseData();" path="applicationDetails.namesDontMatch"/>
		<label for="names_dont_match"><b>Имената по документ за самоличност и документ за призната квалификация не съвпадат</b></label></p>		
		<div class="clr"><!--  --></div>
		<div id="names_dont_match_div">
			<fieldset><legend>Данни от документ за призната квалификация</legend> 
				<v:hidden id="trCourseDocumentPersonDetails.id" name="trCourseDocumentPersonDetails.id" path="trCourseDocumentPersonDetails.id" />
				<p><span class="fieldlabel2"><label for="trCourseDocumentPersonDetails.documentCivilIdType">Тип персонален идентификатор</label><br/></span>
					<nacid:radiobutton name="trCourseDocumentPersonDetails.documentCivilIdType" attributeName="documentCivilIdType" singleline="true" />
				</p>
				<div class="clr"><!--  --></div> 						
			
           		<p><span class="fieldlabel2"><label for="trCourseDocumentPersonDetails.documentCivilId">${messages.civilIdType}</label><br/></span>
               		<v:textinput id="trCourseDocumentPersonDetails.documentCivilId" name="trCourseDocumentPersonDetails.documentCivilId" path="trCourseDocumentPersonDetails.documentCivilId"  class="brd w200"/>
            	</p>
            	<div class="clr"><!--  --></div>
            	<p><span class="fieldlabel2"><label for="trCourseDocumentPersonDetails.documentFName">Име</label><br/></span>
         			<v:textinput id="trCourseDocumentPersonDetails.documentFname" class="brd w500" name="trCourseDocumentPersonDetails.documentFname" path="trCourseDocumentPersonDetails.documentFname" />
      			</p>
      			<div class="clr"><!--  --></div>   
      		   
      			<p><span class="fieldlabel2"><label for="trCourseDocumentPersonDetails.documentSname">Презиме</label><br/></span>
         			<v:textinput id="trCourseDocumentPersonDetails.documentSname" class="brd w500" name="trCourseDocumentPersonDetails.documentSname" path="trCourseDocumentPersonDetails.documentSname" />
      			</p>
      			<div class="clr"><!--  --></div> 
      		
      			<p><span class="fieldlabel2"><label for="trCourseDocumentPersonDetails.documentLName">Фамилия</label><br/></span>
         			<v:textinput id="trCourseDocumentPersonDetails.documentLname" class="brd w500" name="trCourseDocumentPersonDetails.documentLname" path="trCourseDocumentPersonDetails.documentLname" />
      			</p>
			</fieldset>
		</div>
		<div class="clr20"><!--  --></div>
		
		<p class="checkbox"><input ${has_representative} type="checkbox" name="representative_data" id="representative_data" value="1"  onclick="showHideRepresentativeData();" />
		<label for="representative_data"><b>Заявителят и подателят са различни лица</b></label></p>
		<div id="representative_data_div">
			<fieldset><legend>Лични данни на подателя</legend> 

				<v:egnLnchValidator input="representative.civilId"/>
				
				<v:hidden id="applicationDetails.representativeId" name="applicationDetails.representativeId" path="applicationDetails.representativeId" />
				<v:hidden id="representative.id" name="representative.id" path="representative.id" />
				<p><span class="fieldlabel2"><label for="representative.civilIdType">Тип персонален идентификатор</label><br/></span>
					<nacid:radiobutton name="representative.civilIdType" attributeName="representativeCivilIdType" singleline="true" onclick="emptyPersonalId('representative');"/>
				</p>
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel2"><label for="representative.civilId">${messages.civilIdType}</label><br/></span>
	            	 <v:textinput class="brd w200" id="representative.civilId" name="representative.civilId" path="representative.civilId" 
	            	 onkeydown="if(!isSpecialKeyPressed(event)) personalIdChanged('representative');" />
	            </p>
	            <div class="clr"><!--  --></div>
	            	             
	            <script type="text/javascript">
		 		   new Autocomplete('representative.civilId', { 
		    	    serviceUrl:'${pathPrefix}/control/civilidsuggestion',
		    	    // callback function:
		    	    onSelect: updateRepresentativeData,
		    	    width:500,
		    	    dynamicParameters: function (){
		    	      return {civilidtype : getRadioChecked($('appform1')['representative.civilIdType'])};
		    	    }
		    	  });
		  		</script> 
	           
				<p><span class="fieldlabel2"><label for="representative.fName">Име</label><br/></span>
	         		<v:textinput id="representative.fName" class="brd w500" name="representative.fName" path="representative.fName" />
	      		</p>
	      		<div class="clr"><!--  --></div>   
	      		   
	      		<p><span class="fieldlabel2"><label for="representative.sName">Презиме</label><br/></span>
	         		<v:textinput id="representative.sName" class="brd w500" name="representative.sName" path="representative.sName" />
	      		</p>
	      		<div class="clr"><!--  --></div> 
	      		
	      		<p><span class="fieldlabel2"><label for="representative.lName">Фамилия</label><br/></span>
	         		<v:textinput id="representative.lName" class="brd w500" name="representative.lName" path="representative.lName" />
	      		</p>
	      		<div class="clr"><!--  --></div>
	      		<p><span class="fieldlabel2"><label for="applicationDetails.repPhone">Телефон</label><br/></span>
	                <v:textinput id="applicationDetails.repPhone" class="brd w500" name="applicationDetails.repPhone" path="applicationDetails.repPhone" />
	            </p>
	            <div class="clr"><!--  --></div> 
	      		
	      		<div id="repr_person_details" >
	      		<p><span class="fieldlabel2"><label for="applicationDetails.repAddrDetails">Адрес за контакт</label><br/></span>
	          		<v:textarea id="applicationDetails.repAddrDetails" class="brd w500" name="applicationDetails.repAddrDetails" path="applicationDetails.repAddrDetails" />
	          	</p> 
	      		<div class="clr"><!--  --></div> 
	      		
	      		<p><span class="fieldlabel2"><label for="applicationDetails.repEmail">Електронна поща</label><br/></span>
	                <v:textinput id="applicationDetails.repEmail" class="brd w500" name="applicationDetails.repEmail" path="applicationDetails.repEmail" />
	            </p>
	            </div>
	            
	            <p class="checkbox"><input ${rep_company} value="1" type="checkbox" id="repr_from_company" name="repr_from_company" 
	         		onclick="refreshRepresentativeAdressAndCompany();"/>
	            <label for="repr_from_company">Подателят е представител на фирма</label></p>
	            <div class="clr"><!--  --></div> 
	            <div id="repr_company_details" >
		            <p><span class="fieldlabel2"><label for="applicationDetails.repFromCompany">Фирма</label><br/></span>
		          		<nacid:combobox name="applicationDetails.repFromCompany" id="applicationDetails.repFromCompany" path="applicationDetails.repFromCompany"
		          		 attributeName="companiesComboBox" style="w308 brd" />
		          	</p> 
	            </div>
	       		<script type="text/javascript">
					function refreshRepresentativeAdressAndCompany() {
						if($('repr_from_company').checked) {
							$('repr_company_details').show();
							$('repr_person_details').hide();
						}
						else {
							$('repr_company_details').hide();
							$('repr_person_details').show();
						}
					}
					refreshRepresentativeAdressAndCompany();
				</script>
	      		<div class="clr"><!--  --></div> 
			</fieldset>
			
		</div>
		
		<div class="clr20"><!--  --></div>
		<p><span class="fieldlabel"><label for="applicationDetails.serviceTypeId">Тип услуга</label><br/></span>
			<nacid:combobox name="applicationDetails.serviceTypeId" attributeName="applicationDetails.serviceTypeId"
						path="applicationDetails.serviceTypeId" id="applicationDetails.serviceTypeId" style="brd w308" onchange="calculateExecutionEndDate(this.value);"/>
						<label style="margin:10px;" for="applicationDetails.endDate">Kраен срок</label>
						<v:dateinput name="applicationDetails.endDate" id="applicationDetails.endDate" path="applicationDetails.endDate" class="brd w200;" value="${endDate }"/>									
    	</p> 
    			
		<div class="clr20"><!--  --></div>
        <p class="checkbox" id="pCommunicateByEmail"><input id="applicationDetails.personalEmailInforming" name="applicationDetails.personalEmailInforming" path="applicationDetails.personalEmailInforming" value="1" type="checkbox" ${personal_email_informing } /><label
            for="applicationDetails.personalEmailInforming">Съгласен съм да получавам информация и уведомления на посочения електронен адрес</label></p>
		<div class="clr"><!--  --></div>
        <p class="checkbox"><input id="applicationDetails.personalDataUsage" ${personal_data_usage} name="applicationDetails.personalDataUsage" path="applicationDetails.personalDataUsage" value="1" type="checkbox" /><label
            for="applicationDetails.personalDataUsage">Съгласен съм да се ползват лични данни за целите на проверката</label></p>
		<div class="clr"><!--  --></div>
        <p class="checkbox"><input id="applicationDetails.dataAuthentic" ${personal_declaration} name="applicationDetails.dataAuthentic" path="applicationDetails.dataAuthentic" value="1" type="checkbox" ${data_authentic } /><label
            for="applicationDetails.dataAuthentic">Декларирам, че данните посочени в заявлението за издаване на удостоверение за придобита проф. квалификация на територията на Р България, необходимо за достъп или за упражняване на регулирана професия на територията на друга държава членка, са истински и автентични
            </label>
        </p>
	<fieldset>
		<legend>Начин на получаване на уведомления</legend>
		<p>
			<nacid:radiobutton name="applicationDetails.documentReceiveMethodId" attributeName="documentReceiveMethod" singleline="false"  onclick="documentReceiveMethodChanged(this)"/>
		</p>
		<div id="documentRecipientDiv" style="display: ${empty requestScope["com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl"].documentRecipient ? 'none' : 'block'}">
			<v:hidden name="documentRecipient.id" id="documentRecipient.id" path="documentRecipient.id"/>
			<fieldset>
				<legend>Адрес на получател</legend>
				<p>
					<a href="javascript:void(0);" onclick="copyContactAddress()" class="flt_rgt">Копирай адреса за контакт</a>
				</p>
				<div class="clr"><!-- --></div>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.name">Име</label></span>
					<v:textinput class="brd w500" maxlength="255" name="documentRecipient.name" id="documentRecipient.name" path="documentRecipient.name"/>
				</p>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.address">Улица №</label></span>
					<v:textarea class="brd w500" rows="3" cols="40" name="documentRecipient.address" id="documentRecipient.address" path="documentRecipient.address" />
				</p>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.district">Област</label></span>
					<v:textinput class="brd w500" maxlength="100" name="documentRecipient.district" id="documentRecipient.district" path="documentRecipient.district"/>
				</p>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.postCode">Пощенски код</label></span>
					<v:textinput class="brd w500" maxlength="255" name="documentRecipient.postCode" id="documentRecipient.postCode" path="documentRecipient.postCode"/>
				</p>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.city">Град</label></span>
					<v:textinput class="brd w500" maxlength="100" name="documentRecipient.city" id="documentRecipient.city" path="documentRecipient.city"/>
				</p>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.countryId">Държава</label></span>
					<nacid:combobox name="documentRecipient.countryId" id="documentRecipient.countryId" attributeName="documentRecipient.countryId" style="w308 brd"/>
				</p>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.mobilePhone">Мобилен телефон</label></span>
					<v:textinput class="brd w500" maxlength="50" name="documentRecipient.mobilePhone" id="documentRecipient.mobilePhone" path="documentRecipient.mobilePhone"/>
				</p>
			</fieldset>
		</div>
	</fieldset>
        
        <div class="clr10"><!--  --></div>
             
        <p><span class="fieldlabel"><label for="applicationDetails.applicationCountryId">Кандидатства за държава</label><br/></span>
			<nacid:combobox name="applicationDetails.applicationCountryId" attributeName="applicationDetails.applicationCountryId"
						path="applicationDetails.applicationCountryId" id="applicationDetails.applicationCountryId" style="brd w500" onchange="blockRegulatedExaminations();"/>			
    	</p>   
    	
    	<div class="clr10"><!--  --></div>

        <p><span class="fieldlabel"><label for="applicationDetails.apostilleApplication">Заявление за апостил</label><br/></span>
            <v:checkboxinput id="applicationDetails.apostilleApplication" name="applicationDetails.apostilleApplication" path="applicationDetails.apostilleApplication" style="brd w500" />
        </p>

        <div class="clr10"><!--  --></div>
    	 	
    	<div id="regulated_examination_div">	
			<fieldset><legend>Регулирана в посочената държава</legend>
		    	<v:hidden id="activeForm" value="2" name="activeForm"/>
		 		<v:hidden id="regulatedExaminationId" name="regulatedExaminationId" value="${regulatedExaminationId}"/>
		    	<!--<nacid:systemmessage name="regulatedExaminationSystemMessage" /> -->
				<div style="display:none;" id="correct_message_reg_div" class="correct">Данните бяха успешно въведени!</div>
		    	<div style="display:none;" id="error_message_reg_div" class="error">Получи се грешка при опит за запис!</div>
		  		
			    <div class="clr10"><!--  --></div>
		 		<p><span class="fieldlabel2"><label for="regulatedInCountry">Регулирана в посочената държава</label><br/></span>
		   		<span id="regulatedInCountry">${regulatedInCountry}</span>       
		   		<div class="clr20"><!--  --></div>
				<span class="fieldlabel">
		  			<a href="${europeanCommissionWebSite}" target="_blank">Link към сайта на ЕК</a><br/>
		  		</span> 
		  
		   		<div class="clr20"><!--  --></div>
				<p>
		  		<span class="fieldlabel">
		  			<a id="newRegExamination" href="javascript: void(0);" onclick="editRegData(null);">Добави нова проверка</a><br/>
		  		</span> 
		 		</p>
		  		<div class="clr20"><!--  --></div>    
					<nacid:list attribute="regulatedValiditiesList" skipFilters="true" tablePrefix="regulatedValiditiesTable" />  
				<div class="clr20"><!--  --></div>
				
		   		<div class="clr20"><!--  --></div>
		 		<span class="fieldlabel2"><label for="regulatedExaminationNotes">Бележки към проверката:</label></span>
		 		<v:textarea class="brd w600" rows="3" cols="40" name="regulatedExaminationNotes" id="regulatedExaminationNotes" value="${regulatedExaminationNotes}"/>
		 		<div class="clr20"><!--  --></div>    
			</fieldset>
		</div>	

        <div class="clr10"><!--  --></div>

        <div id="responsibles_list" style="display:none;">
			<p><span class="fieldlabel"><label>Избрани отговорници: </label></span>
				<table id="responsibles_table">
				<c:if test="${responsibleUsers != null }">
					<c:forEach items="${responsibleUsers}" var="resp">
						<tr>
							<td>${resp.fullName}</td>
							<td style="display:none;"><v:hidden name="responsibleUser" value="${resp.userId}"/></td>
							<td><a href="javascript:void(0);" onclick="removeResponsibleFromList(this.parentNode.parentNode);">
							<img src="${pathPrefix}/img/icon_delete.png"/></a></td>
						</tr>
					</c:forEach>
				</c:if>
				</table>
			</p>
		</div>
		<p><span class="fieldlabel"><label for="responsibleUserCombo">Отговорник</label><br /></span> 
			<nacid:combobox name="responsibleUserCombo" id="responsibleUserCombo"  
							attributeName="responsibleUsersCombo" style="brd w308" />
			<a href="javascript: void(0);" onclick="addResponsibleToList();" style="margin-left: 10px;">Добави отговорник</a>
		</p>

		<div class="clr"><!--  --></div>

		<fieldset><legend>Бележки</legend>
			 <p><span class="fieldlabel2"><label for="applicationDetails.notes">Бележки към заявлението</label><br/></span>
          		<v:textarea id="applicationDetails.notes" class="brd w500" name="applicationDetails.notes" path="applicationDetails.notes"  />
          	</p>
		</fieldset>
	<v:hidden id="applicationDetails.externalSystemId" name="applicationDetails.externalSystemId" path="applicationDetails.externalSystemId" />

</v:form>

<div class="clr20"><!--  --></div>
<v:form name="examination_form" action="${pathPrefix }/control/regulated_validity_ajax?appId=${id}" method="post" skipsubmitbuttons="true"
	modelAttribute="com.nacid.bl.impl.regprof.RegulatedProfessionValidityImpl">
	<v:hidden id="regulatedValidityId" name="id" path="id"/>
	<v:hidden id="regulatedProfession" name="profession" path="profession"/>
	<v:hidden id="regulatedExaminationDate" name="examinationDate" path="examinationDate"/>
	<v:hidden id="regulatedNotes" name="notes" path="notes"/>
	<v:hidden id="isRegulated" name="isRegulated" path="isRegulated"/>
</v:form>
<!-- RayaWritten -->
<script type="text/javascript">
if("${regData}" != "noData" && window.location.pathname.lastIndexOf('view') == -1 ){
	$$('#regulatedValiditiesTable' + 'main_table tbody > tr > td:nth-child(2)').each(function(element) {
	    var select = element.childElements()[0];
	    var id = element.up(0).down(0).next(1).innerHTML;
	    select.enable();
	    select.name = "chosenValidityId";
	    select.value = id;
	});
	clearHiddenFieldsRegForm();
	changeRegOperationEdit();
	addEditRegRowToTable();
}
if(window.location.pathname.lastIndexOf('view') != -1){
	///Make link inactive so that adding new examinations is not available in view mode or when there is no data
	$('newRegExamination').href = "javascript: void(0);";
	$('newRegExamination').onclick = function(){};
	$('regulatedExaminationNotes').disable();
}
if("${regData}" == "noData"){
	$('regulated_examination_div').hide();
}
function calculateExecutionEndDate(serviceTypeId){
	var targetInput = $('applicationDetails.endDate');
	var applicationDate = $('applicationDetails.appDate').value;
	new Ajax.Request("${pathPrefix}/control/service_type_ajax?serviceId="+serviceTypeId+"&applicationDate="+applicationDate ,{
		method: 'get',
  	  	onSuccess: function(transport) {
  	  		var response = transport.responseText;
  	  		targetInput.value = response;
  	  	},
  	  	onFailure: function(){
  	  		alert("Възникна проблем при изчислението на крайния срок");
  	  	}
	});
}
var blocked = false;
function blockRegulatedExaminations(){
	if(!blocked){
		$('newRegExamination').href = "javascript: void(0);";
		$('newRegExamination').onclick = function(){};
		$('regulatedExaminationNotes').disable();
		$$('#regulatedValiditiesTable' + 'main_table tbody > tr ').each(function(element) {
			$$('#'+element.id+' td:nth-child(2) > input ')[0].disable();
			$$('#'+element.id+' td:nth-child(8) > a ')[0].onclick = function(){};
	 	});
		blocked = true;
	}
}
function addEditRegRowToTable(){
 	var insertionString = "<tr id='editRegRow' style='display: none;' onmouseout='this.style.backgroundColor=\"#ffffff\";' onmouseover='this.style.backgroundColor=\"#e0e0e0\";'"+
 	"style='background-color: rgb(255, 255, 255);'><td></td><td><input id='chosenValidityId' type='radio' disabled='disabled'/></td>"+
 	"<td style='display:none' id='regValidityId'></td><td><input type='text' id='regExamDate' class='brd w100'/>"+
 	"</td><td><input type='checkbox' id='regValid' onchange='refreshHiddenFieldsRegForm();'/></td>"+
 	"<td><input type='text' id='profession' class='brd w100'/></td><td><input type='text' id='regValidityNotes' class='brd w100' /></td>"+
 	"<td><a id='edit_reg_link' href='javascript: void(0);'><img src='${pathPrefix}/img/icon_edit.png'>"+
 	"</a><a id='cancel_reg_link' href='javascript: void(0);'><img src='${pathPrefix}/img/icon_delete.png'></a></td>";
 	if($$('#regulatedValiditiesTablemain_table > tbody')[0]!=null){
 		$$('#regulatedValiditiesTablemain_table > tbody')[0].insert({top: insertionString});	
 	} else {
 		$('regulatedValiditiesTablemain_table').insert({top: insertionString});	
 	}
}
 //Make some changes so that edition is possible
function editRegData(row){
 	var editRow = $('editRegRow');
 	editRow.show();	
 	if (row != null) {
 		row.insert({after: editRow});
 		row.hide();
 		$$("#editRegRow>td:nth-child(1)")[0].innerHTML = $$('#'+row.id+' >td:nth-child(1)')[0].innerHTML;
 		$('chosenValidityId').checked = $$('#'+row.id+' >td:nth-child(2)>input')[0].checked;
 		$('regValidityId').innerHTML = $$('#'+row.id+' >td:nth-child(3)')[0].innerHTML;
 		$('regExamDate').value = $$('#'+row.id+' >td:nth-child(4)')[0].innerHTML;
 		$('regValid').checked = $$('#'+row.id+' >td:nth-child(5) > input')[0].checked;
 		$('profession').value= $$('#'+row.id+' >td:nth-child(6) ')[0].innerHTML;
 		$('regValidityNotes').value = $$('#'+row.id+' >td:nth-child(7)')[0].innerHTML;
 		$('cancel_reg_link').onclick = function(){cancelRegEdition(row.id);};
 		$('edit_reg_link').innerHTML = '';
 		$('edit_reg_link').insert(new Element('img', {'src' : '${pathPrefix}/img/16x16_save.png', 'style':"margin-right:10px;"}));
 		$('edit_reg_link').onclick = function(){refreshHiddenFieldsRegForm(); saveRegData(row.id);};
 	} else {		
 		$$('#regulatedValiditiesTablemain_table > tbody')[0].insert({bottom: editRow});
 		$('regExamDate').value = getToday();
 		$('cancel_reg_link').onclick = function(){cancelRegEdition("new");};
 		$('edit_reg_link').innerHTML = '';
 		$('edit_reg_link').insert(new Element('img', {'src' : '${pathPrefix}/img/16x16_save.png', 'style':"margin-right:10px;"}));
 		$('edit_reg_link').onclick = function(){refreshHiddenFieldsRegForm(); saveRegData("new");};
 	}
 	makeOtherRegRowsInactive(row);
}
//Make edition for every row other than editRow inactive
function makeOtherRegRowsInactive(row){
 	$$('#regulatedValiditiesTable' + 'main_table tbody > tr ').each(function(element) {
 		if(element != row && element != $('editRegRow')){
 			$$('#'+element.id+' td:nth-child(2) > input ')[0].disable();
 			$$('#'+element.id+' td:nth-child(8) > a ')[0].onclick = function(){};
 		}
 	});
}
//Make edition for every row active
function makeRegRowsActive(){
 	$$('#regulatedValiditiesTable' + 'main_table tbody > tr ').each(function(element) {
 		$$('#'+element.id+' td:nth-child(2) > input ')[0].enable();
 		$$('#'+element.id+' td:nth-child(8) > a ')[0].onclick = function(){editRegData(this["parentNode"]["parentNode"]);};
 	});
}
//Change the edit button of every row
function changeRegOperationEdit(){
 	$$('#regulatedValiditiesTable' + 'main_table tbody > tr > td:nth-child(8)> a').each(function(element) {	
 		element.onclick = function(){editRegData(this["parentNode"]["parentNode"]);};
 		element.href = 'javascript: void(0);';
 		
 	});
}
//Clear changes made for edition
function cancelRegEdition(rowId){
 	var editRegRow = $('editRegRow');	
 	if(rowId != 'new'){			
 		$(rowId).show();
 	} 
 	$('regValidityId').innerHTML = "";
 	$('regExamDate').value = "";
 	$('regValid').checked = false;
 	$('profession').value = "";
 	$('regValidityNotes').value = "";
 	$('edit_reg_link').onclick = function(){};
 	$('edit_reg_link').innerHTML = '';
 	$('edit_reg_link').insert(new Element('img', {'src' : '${pathPrefix}/img/icon_edit.png'}));
 	$('cancel_reg_link').onclick = function(){};
 	clearHiddenFieldsRegForm();
 	editRegRow.hide();
 	makeRegRowsActive();
}
//Get the data from the inserted row and put it in the form
function refreshHiddenFieldsRegForm(){
 	$('regulatedValidityId').value = $('regValidityId').innerHTML;
 	$('regulatedExaminationDate').value = $('regExamDate').value;
 	$('regulatedProfession').value = $('profession').value;
 	$('isRegulated').value = $('regValid').checked?1:0;
 	$('regulatedNotes').value = $('regValidityNotes').value;
}
//When the data in the form is no longer needed
function clearHiddenFieldsRegForm(){
 	$('regulatedValidityId').value = "";
 	$('regulatedExaminationDate').value = "";
 	$('regulatedProfession').value = "";
 	$('isRegulated').value = "";
 	$('regulatedNotes').value = "";
}
//Check if date is ok and submit the hidden form
function saveRegData(rowId){
	var dateValid = validateDate($("regulatedExaminationDate"), 'd.m.yyyy', false, true, true);
	var professionValid = validateText($('regulatedProfession'), true, 3, 100, null, "");
 	var res = dateValid && professionValid; 
 	if(res == true){
 		$('examination_form').request({
 			onSuccess: function(transport) { 
 				$('correct_message_reg_div').show();
 				$('error_message_reg_div').hide();
 				var json = transport.responseText.evalJSON(true);
 				///add permanently the row to the table or change permanently the existing row
 				makeRegChangesPermanent(rowId, json);
 			},
 			onFailure: function() {
 				$('correct_message_reg_div').hide();
 				$('error_message_reg_div').show();
 				//remove edit row and stop the changes
 				$('editRegRow').hide();
 				clearHiddenFieldsRegForm();
 			}		
 		});
 	} else {
 		if(!dateValid) {
 			$('regExamDate').style.borderColor = 'red';
 		}
 		if(!professionValid){
 			$('profession').style.borderColor = 'red';
 		}
 		alert("Моля, коригирайте полетата в червено!");
 	}
 	
}
function makeRegChangesPermanent(rowId, j) {	
	if(rowId == "new"){
		var rowNum = $('regulatedValiditiesTablemain_table').rows.length;
		var newRow = $('regulatedValiditiesTablemain_table').insertRow(rowNum-1);
		newRow.id = "regulatedValiditiesTablerow_"+(rowNum-1);
		var cell1 = newRow.insertCell(0);
		cell1.innerHTML = rowNum-1;
		var cell2 = newRow.insertCell(1);
		var radio = document.createElement("input");
		radio.type = "radio";
		radio.disabled = "disabled";
		radio.name = "chosenValidityId";
		radio.value = j.validation_returned_id;
		cell2.appendChild(radio);
		var cell3 = newRow.insertCell(2);
		cell3.innerHTML = j.validation_returned_id;
		cell3.id = "regid"+rowNum;
		$("regid"+rowNum).hide();
		var cell4 = newRow.insertCell(3);
		cell4.innerHTML = $('regulatedExaminationDate').value;
		var cell5 = newRow.insertCell(4);
		var checkbox1 = document.createElement("input");
		checkbox1.type = "checkbox";        		
		checkbox1.disabled = "disabled";
		cell5.appendChild(checkbox1);
		var cell6 = newRow.insertCell(5);
		cell6.innerHTML = $("regulatedProfession").value;
		var cell7 = newRow.insertCell(6);
		cell7.innerHTML = $('regulatedNotes').value;
		var cell8 = newRow.insertCell(7);
		var a = document.createElement('a');
		var pic = document.createElement("img");
		pic.src = "${pathPrefix}/img/icon_edit.png";
		a.appendChild(pic);
		a.href = "javascript:void(0);";
		a.onclick= function(){editRegData(this["parentNode"]["parentNode"]);};
		cell8.appendChild(a);
		checkbox1.checked = $('isRegulated').value ==1?true:false;
	} else {
		var changedRow = $(rowId);
		$$('#'+rowId+' >td:nth-child(4)')[0].innerHTML = $('regulatedExaminationDate').value;
		$$('#'+rowId+'>td:nth-child(5) > input')[0].checked = $('isRegulated').value == 1 ? "checked" : "";
		$$('#'+rowId+'>td:nth-child(6) ')[0].innerHTML = $('regulatedProfession').value;
		$$('#'+rowId+'>td:nth-child(7) ')[0].innerHTML = $('regulatedNotes').value;
	}
	cancelRegEdition(rowId);
}                
</script>
<!-- End of RayaWritten -->
<script type="text/javascript">
  if ($('applicant.civilId').value != '') disablePersonInputs('applicant');
  if ($('representative.id').value != '') disablePersonInputs('representative');
  showHideRepresentativeData();
  showHideTrainingCourseData();
  //Inicializirane na ignore-a za ЛНЧ/друго validation
  initPersonalIdIgnoreValidation('applicant');
  initPersonalIdIgnoreValidation('representative');

  function cummunicateByEmailRefresh() {
	  if($('applicationDetails.applicantEmail').value != null && $('applicationDetails.applicantEmail').value != '') {
		  $('pCommunicateByEmail').show();
	  }
	  else {
		  $('pCommunicateByEmail').hide();
		  $('applicationDetails.personalEmailInforming').checked = false;
	  }
  }

  cummunicateByEmailRefresh();
  
function removeResponsibleFromList(row){
	if(confirm("Сигурни ли сте, че искате да премахнете отговорника от списъка?")){
		Element.remove(row);
		$('responsibleUserCombo').value = "-";
	}
	showHideResponsiblesList();
}
function addResponsibleToList(){
	var table = $('responsibles_table');
	var listSize = table.rows.length;
	var duplicate = userIsDuplicate();
	if($('responsibleUserCombo').value != "-" && !duplicate){
		var row = table.insertRow(listSize);
    	var cell1 = row.insertCell(0);
    	cell1.innerHTML = $('responsibleUserCombo').options[$('responsibleUserCombo').selectedIndex].text;
    	var cell2 = row.insertCell(1);
    	cell2.style.display = "none";
    	var input = document.createElement("input");
    	input.name = "responsibleUser";
    	input.value = $('responsibleUserCombo').value;
    	cell2.appendChild(input); 
    	var cell3 = row.insertCell(2);
    	var a = document.createElement("a");
    	a.href = "javascript:void(0);";
    	a.onclick = function(){
    		removeResponsibleFromList(a.parentNode.parentNode);
    	};
    	var img = document.createElement("img");
    	img.src = "${pathPrefix}/img/icon_delete.png";
    	a.appendChild(img);
    	cell3.appendChild(a);
    	$('responsibleUserCombo').value = "-";
    	$('responsibleUserCombo').style.borderColor = '#0000FF';
	} else {
		if(duplicate){
			alert("Този отговорник вече е добавен!");
		} else {
			$('responsibleUserCombo').style.borderColor = 'red';
			alert("Моля, изберете оговорник!");
		}
	}
	showHideResponsiblesList();
}
function showHideResponsiblesList(){
	if($('responsibles_table').rows.length == 0){
		$('responsibles_list').hide();
	} else {
		$('responsibles_list').show();
	}
}
function userIsDuplicate(){
	var duplicate = false;
	$$('#responsibles_table input').each(function(element){
		if(element.value == $('responsibleUserCombo').value+""){
			duplicate = true;
		}
	});
	return duplicate;
}
showHideResponsiblesList();
</script>
<!-- End of RayaWritten -->