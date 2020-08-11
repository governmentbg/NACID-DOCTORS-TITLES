<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%@page import="com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl"%><h3 class="title"><span>${messages.applicationdata }</span></h3>
<script type="text/javascript">

function setApplicantDocumentIdInDetails(){
	if($('applicantDocuments.id').value != null){
		$('applicationDetails.applicantDocumentsId').value = $('applicantDocuments.id').value;
	}
}
function toggleNamesDontMatchDiv() {
	var dontMatch = $('applicationDetails.namesDontMatch').checked;
	if (dontMatch) {
		$('names_dont_match_div').show();
	} else {
		$('names_dont_match_div').hide();
	}
	$$('#names_dont_match_div input').each(function(element) {
	    if (element.name != 'trainingCourseDetails.id') {
	        if (dontMatch) {
	            element.enable();
	        } else {
	            element.disable();
	        }
	    }
   });
}

function representativePersonalIdTypeChanged() {
    $('representative.civilId').clear();
    clearInputError($('representative.civilId'));
	initRepresentativePersonalIdIgnoreValidation();
}

function initRepresentativePersonalIdIgnoreValidation() {
    $('representative.civilIdIgnoreEGNValidation').checked = !($('representative.civilIdType_rad_0').checked);
	$('representative.civilIdIgnoreEGNValidation').ancestors().first().hide();

	$('representative.civilIdIgnoreLNCHValidation').checked = !($('representative.civilIdType_rad_1').checked);
	$('representative.civilIdIgnoreLNCHValidation').ancestors().first().hide();
}
function documentPersonalIdTypeChanged() {
	$('trainingCourseDetails.documentCivilId').clear();
	clearInputError($('trainingCourseDetails.documentCivilId'));
	initDocumentPersonalIdIgnoreValidation();
}

function initDocumentPersonalIdIgnoreValidation() {
	$('trainingCourseDetails.documentCivilIdIgnoreEGNValidation').checked = !($('trainingCourseDetails.documentCivilIdType_rad_0').checked);
	$('trainingCourseDetails.documentCivilIdIgnoreEGNValidation').ancestors().first().hide();

	$('trainingCourseDetails.documentCivilIdIgnoreLNCHValidation').checked = !($('trainingCourseDetails.documentCivilIdType_rad_1').checked);
	$('trainingCourseDetails.documentCivilIdIgnoreLNCHValidation').ancestors().first().hide();
}

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
	$("documentRecipient.name").value = $("differentApplicantRepresentative").checked ? ($("applicant.fname").value + ($("applicant.sname").value == '' ? "" : " " + $("applicant.sname").value)  + " " + $("applicant.lname").value) : $("extPerson.fullName").innerHTML;
}

function applicantPersonalIdTypeChanged() {
	$('applicant.id').value = "";
	$('applicant.civilId').clear();
	clearInputError($('applicant.civilId'));
	initApplicantPersonalIdIgnoreValidation();
	loadApplicantDetails();
}
function applicantPersonalIdChanged() {
	$('applicant.id').value = '';
	loadApplicantDetails();
}

function initApplicantPersonalIdIgnoreValidation() {
	$('applicant.civilIdIgnoreEGNValidation').checked = !($('applicant.civilIdType_rad_0').checked);
	$('applicant.civilIdIgnoreEGNValidation').ancestors().first().hide();

	$('applicant.civilIdIgnoreLNCHValidation').checked = !($('applicant.civilIdType_rad_1').checked);
	$('applicant.civilIdIgnoreLNCHValidation').ancestors().first().hide();
}
function showHideApplicant(resetApplicantDetails) {
	if ($("differentApplicantRepresentative").checked) {
		$("different_applicant_representative_div").show();
		$("representative_birth_fields").hide();
		if (resetApplicantDetails) {
			clearApplicantDetails();
			clearDocumentFillForm();
		}
	}	else {
		$("different_applicant_representative_div").hide();
		clearApplicantDetails();
		$("representative_birth_fields").show();
		if (resetApplicantDetails) {
			readDocumentDataFromServer();
		}

	}
}
function clearApplicantDetails() {
	$("applicant.id").value = "";
	$("applicant.civilId").value = "";
	$("applicant.fname").value = "";
	$("applicant.sname").value = "";
	$("applicant.lname").value = "";
	$("applicant.email").value = "";
	$("applicant.birthDate").value = "";
	$("applicant.birthCity").value = "";
	$("applicant.birthCountryId").value = "-";
	$("applicant.citizenshipId").value = "-";
}
function readPersonDetails(_civilIdType, _civilId, _loggedUser) {
	var result = {};
	var form = $("appform1");
	new Ajax.Request('${pathPrefix }/control/ext_person_ajax', {
		parameters: {
			civilId: _civilId ,
			civilIdType: _civilIdType,
			loggedUser: _loggedUser
		} ,
		asynchronous: false,
		onCreate : function(oXHR) {
			// setLoading(form);
		},
		onSuccess: function(transport){
			// removeLoading(form);
			var json = transport.responseText.evalJSON(true);
			result = json;
		},
		onFailure: function(){
			// removeLoading(form);
			alert("Проблем при зареждане на данните на заявителя");
		}
	});
	return result;
}
function getApplicantCivilIdType() {
	var civilIdTypeEl = $$('input:checked[type=radio][name=applicant.civilIdType]')[0];
	var _civilIdType = civilIdTypeEl == null ? null : civilIdTypeEl.value;
	return _civilIdType;
}

function loadApplicantDetails() {
	var _civilIdType = getApplicantCivilIdType();
	var _civilId = $("applicant.civilId").value;
	if (_civilIdType != null && _civilId.length > 3) {
		var json = readPersonDetails(_civilIdType, _civilId, false);
		if (json.id === undefined) {
			clearDocumentFillForm();
		} else {
			$("applicant.id").value = json.id;
			$("applicant.fname").value = json.fname;
			$("applicant.sname").value = json.sname;
			$("applicant.lname").value = json.lname;
			$("applicant.email").value = json.email;
			fillApplicantDocument(json);
		}
	} else {
		clearDocumentFillForm();
	}
}
function fillApplicantDocument(json) {
	if (json["document.id"] === undefined) {
		clearDocumentFillForm();
	} else {
		$("document_data").checked = false;
		$("applicantDocuments.id").value = json["document.id"];
		setApplicantDocumentIdInDetails();
		$("applicantDocuments.active").value = json["document.active"];
		$("applicantDocuments.number").value = json["document.number"];
		$("applicantDocuments.dateOfIssue").value = json["document.dateOfIssue"];
		$("applicantDocuments.issuedBy").value = json["document.issuedBy"];

		disableApplicantDocumentFields();
	}
}
function disableApplicantDocumentFields() {
	$("applicantDocuments.number").disable();
	$("applicantDocuments.dateOfIssue").disable();
	$("applicantDocuments.issuedBy").disable();
}

function documentDataButtonClicked() {
	if ($("document_data").checked) {
		clearDocumentFillForm();
	} else {
		readDocumentDataFromServer();
	}
}
function readDocumentDataFromServer(){
	var differentApplicantRepresentative = $("differentApplicantRepresentative").checked;
	if (differentApplicantRepresentative) {
		var applicantId = $("applicant.id").value;
		if (applicantId != null) {
			var civilId = $("applicant.civilId").value;
			var civilIdType = getApplicantCivilIdType();
			var json = readPersonDetails(civilIdType, civilId);
			fillApplicantDocument(json);
		} else {
			clearDocumentFillForm();
		}
	} else {
		var json = readPersonDetails(null, null, true);
		fillApplicantDocument(json);
	}
}
function clearDocumentFillForm() {
	$('document_data').checked = true;
	$("applicantDocuments.id").value = "";
	$("applicationDetails.applicantDocumentsId").value = "";
	$("applicantDocuments.number").value = "";
	$("applicantDocuments.dateOfIssue").value = "дд.мм.гггг";
	$("applicantDocuments.issuedBy").value = "";
	$("applicantDocuments.number").enable();
	$("applicantDocuments.dateOfIssue").enable();
	$("applicantDocuments.issuedBy").enable();
}
</script>
<nacid:systemmessage name="applicationStatusMessage" />
<v:form name="appform1" action="${pathPrefix }/control/applications/save" method="post"
	backurl="${pathPrefix }/control/applications/list?getLastTableState=1" modelAttribute="<%=ExtRegprofApplicationImpl.class.getName() %>"
	id="appform1">
    
    <v:textValidator input="applicantDocuments.number" required="$('document_data').checked && (($('applicantDocuments.dateOfIssue').value != 'дд.мм.гггг' && $('applicantDocuments.dateOfIssue').value != '') || $('applicantDocuments.issuedBy').value != '')"/>
    <v:textValidator input="applicantDocuments.issuedBy" required="$('document_data').checked && (($('applicantDocuments.dateOfIssue').value != 'дд.мм.гггг' && $('applicantDocuments.dateOfIssue').value != '') || $('applicantDocuments.number').value != '')"/>
    <v:dateValidator input="applicantDocuments.dateOfIssue" format="d.m.yyyy" required="$('document_data').checked && ($('applicantDocuments.issuedBy').value != '' || $('applicantDocuments.number').value != '')" beforeToday="true"  />

	<v:comboBoxValidator input="applicationDetails.applicantCountryId" required="true" />
	<v:textValidator input="applicationDetails.applicantPhone" required="true" maxLength="50" />
   	<v:textValidator input="applicationDetails.applicantAddrDetails" required="true" maxLength="255" />
   	<v:textValidator input="applicationDetails.applicantCity" required="true" maxLength="255" />

   	<v:textValidator input="trainingCourseDetails.documentFname" maxLength="15" required="$('applicationDetails.namesDontMatch').checked" regex="${validations.name}" errormessage="${messages.err_name}" />
	<v:textValidator input="trainingCourseDetails.documentSname" maxLength="15" regex="${validations.name}" errormessage="${messages.err_name}" />
	<v:textValidator input="trainingCourseDetails.documentLname" maxLength="30" required="$('applicationDetails.namesDontMatch').checked" regex="${validations.name}"  errormessage="${messages.err_name}" />				
    <v:textValidator input="trainingCourseDetails.documentCivilId" maxLength="($('trainingCourseDetails.documentCivilIdType_rad_1').checked ? 10 : 50)" required="$('applicationDetails.namesDontMatch').checked"/>

	<v:comboBoxValidator input="documentRecipient.countryId" required="$('documentRecipientDiv').visible()"/>
    <v:hidden name="applicationDetails.id"  path="applicationDetails.id" />
    <v:hidden name="applicationDetails.trainingCourseId" path="applicationDetails.trainingCourseId" />


	<v:textValidator input="representative_birth_city" maxLength="255" required="!($('differentApplicantRepresentative').checked)" regex="${validations.city }" errormessage="${messages.err_city}" />
	<v:comboBoxValidator input="representative_birth_country_id" required="!($('differentApplicantRepresentative').checked)" />
	<v:dateValidator input="representative_birth_date" format="d.m.yyyy" beforeToday="true" required="!($('differentApplicantRepresentative').checked)" emptyString="дд.мм.гггг" />
	<v:comboBoxValidator input="representative_citizenship" required="!($('differentApplicantRepresentative').checked)" />

	<v:textValidator input="applicant.fname" maxLength="100" required="$('differentApplicantRepresentative').checked"  regex="${validations.name}" errormessage="${messages.err_name}"/>
	<v:textValidator input="applicant.sname" maxLength="100" required="false"  regex="${validations.name}" errormessage="${messages.err_name}"/>
	<v:textValidator input="applicant.lname" maxLength="100" required="$('differentApplicantRepresentative').checked"  regex="${validations.name}" errormessage="${messages.err_name}"/>
	<v:textValidator input="applicant.email" maxLength="100" required="false" regex="${validations.email }" />

	<v:textValidator input="applicant.birthCity" maxLength="255" required="($('differentApplicantRepresentative').checked)" regex="${validations.city }" errormessage="${messages.err_city}" />
	<v:comboBoxValidator input="applicant.birthCountryId" required="($('differentApplicantRepresentative').checked)" />
	<v:dateValidator input="applicant.birthDate" format="d.m.yyyy" beforeToday="true" required="($('differentApplicantRepresentative').checked)" emptyString="дд.мм.гггг" />

	<v:comboBoxValidator input="applicant.citizenshipId" required="($('differentApplicantRepresentative').checked)" />
	<v:comboBoxValidator input="applicationDetails.applicantPersonalIdDocumentType" required="true" />

	<fieldset><legend>Лични данни на подателя</legend>
	<div class="clr"><!--  --></div>
        <c:choose>
            <c:when test="${not empty extPerson.personalId}">
                <p>
                    <span class="fieldlabel"><label>Тип персонален идентификатор</label></span>
                    <span>${extPerson.personalIdType }</span>
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label>${messages.civilIdType}</label></span>
                    <span>${extPerson.personalId }</span>
                </p>
                <div class="clr"><!--  --></div>
            </c:when>
            <c:otherwise>
                <p>
                    <span class="fieldlabel"><label>Тип персонален идентификатор</label></span>
                    <span><nacid:radiobutton name="representative.civilIdType" attributeName="applicantCivilIdType" singleline="true" onclick="representativePersonalIdTypeChanged();"/></span>
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label>${messages.civilIdType}</label></span>
                    <span><v:textinput class="brd w500" maxlength="100" name="representative.civilId" id="representative.civilId"  value="" /></span>
                </p>
                <v:egnLnchValidator input="representative.civilId" required="true"/>

                <script>
                    initRepresentativePersonalIdIgnoreValidation();
                </script>
            </c:otherwise>
        </c:choose>

	<p><span class="fieldlabel"><label>Име</label></span> <span id="extPerson.fname">${extPerson.fname }</span></p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label>Презиме</label></span> <span id="extPerson.sname">${extPerson.sname }</span></p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label>Фамилия</label></span> <span id="extPerson.lname">${extPerson.lname }</span></p>
	<span style="display:none" id="extPerson.fullName">${extPerson.fullName}</span>
	<p><span class="fieldlabel"><label>Електронна поща</label></span> <span>${extPerson.email }</span></p>
       <div class="clr"><!--  --></div>
	<div class="clr"><!--  --></div>
		<div id="representative_birth_fields" style="display: ${requestScope["com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl"].differentApplicantRepresentative == 1 ? 'none' : 'block'}">
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthCountry}</label></span>
				<nacid:combobox name="representative_birth_country_id" id="representative_birth_country_id" attributeName="representativeBirthCountry" style="w308 brd" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthCity}</label></span>
				<span><v:textinput class="brd w500" maxlength="255" name="representative_birth_city" id="representative_birth_city"  value="${extPerson.birthCity}" /></span>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthDate}</label></span>
				<span><v:dateinput class="brd w500" name="representative_birth_date" id="representative_birth_date"  value="${extPerson.birthDate}" /></span>
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="representative_citizenship">гражданин на</label></span>
				<nacid:combobox name="representative_citizenship" id="representative_citizenship" attributeName="representativeCitizenship" style="brd w308"/>
			</p>
		</div>

	</fieldset>
	<fieldset><legend>За контакт</legend>
		   <p><span class="fieldlabel2"><label for="applicationDetails.applicantPhone">Телефон</label><br/></span>
               <v:textinput id="applicationDetails.applicantPhone" class="brd w500" name="applicationDetails.applicantPhone" path="applicationDetails.applicantPhone" />
           </p>
           <div class="clr"><!--  --></div>
           
           <p><span class="fieldlabel2"><label for="applicationDetails.applicantCountryId">Държава</label><br/></span>
               <nacid:combobox name="applicationDetails.applicantCountryId" attributeName="country" path="applicationDetails.applicantCountryId" id="applicationDetails.applicantCountryId" style="brd w500"/>
           </p>
           <div class="clr"><!--  --></div>
           
           <p><span class="fieldlabel2"><label for="applicationDetails.applicantCity">Населено място</label><br/></span>
               <v:textinput id="applicationDetails.applicantCity" class="brd w500" name="applicationDetails.applicantCity" path="applicationDetails.applicantCity" />
           </p>
           <div class="clr"><!--  --></div>
           
		<p><span class="fieldlabel2"><label for="applicationDetails.applicantAddrDetails">Адрес за контакт</label><br/></span>
         		<v:textarea id="applicationDetails.applicantAddrDetails" class="brd w500" name="applicationDetails.applicantAddrDetails" path="applicationDetails.applicantAddrDetails" />
         	</p>
	</fieldset>			
	<div class="clr20"><!--  --></div>

	<p class="checkbox">
		<v:checkboxinput name="differentApplicantRepresentative" id="differentApplicantRepresentative" path="differentApplicantRepresentative" onclick="showHideApplicant(true);" type="checkbox" />
		<label for="differentApplicantRepresentative"><b>Заявителят и подателят са различни лица</b></label>
	</p>

	<div id="different_applicant_representative_div" style="display: none;">
		<fieldset><legend>Лични данни на заявителя</legend>
			<p>
				Моля въведете имена по документ за самоличност. За български граждани е задължително въвеждане на ЕГН.
			</p>
			<v:hidden name="applicant.id" id="applicant.id" path="applicant.id" />
			<p>
				<span class="fieldlabel"><label>Тип персонален идентификатор</label></span>
				<span><nacid:radiobutton name="applicant.civilIdType" attributeName="applicantCivilIdType" singleline="true" onclick="applicantPersonalIdTypeChanged();"/></span>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.civilIdType}</label></span>
				<span><v:textinput class="brd w500" maxlength="100" name="applicant.civilId" id="applicant.civilId"  path="applicant.civilId" onchange="applicantPersonalIdChanged()" autocomplete="off"/></span>
			</p>
			<v:egnLnchValidator input="applicant.civilId" required="$('differentApplicantRepresentative').checked"/>

			<script type="text/javascript">
				initApplicantPersonalIdIgnoreValidation();
			</script>

			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="applicant.fname">Име</label></span>
				<v:textinput class="brd w500" maxlength="100" name="applicant.fname" id="applicant.fname"  path="applicant.fname" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="applicant.sname">Презиме</label></span>
				<v:textinput class="brd w500" maxlength="100" name="applicant.sname" id="applicant.sname"  path="applicant.sname"/>
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="applicant.lname">Фамилия</label></span>
				<v:textinput class="brd w500" maxlength="100" name="applicant.lname" id="applicant.lname" path="applicant.lname"/>
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="applicant.email">Електронна поща</label></span>
				<v:textinput class="brd w500" maxlength="100" name="applicant.email" id="applicant.email" path="applicant.email"/>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthCountry}</label></span>
				<nacid:combobox name="applicant.birthCountryId" id="applicant.birthCountryId" attributeName="applicantBirthCountry" style="w308 brd" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthCity}</label></span>
				<span><v:textinput class="brd w500" maxlength="255" name="applicant.birthCity" id="applicant.birthCity"  path="applicant.birthCity" /></span>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthDate}</label></span>
				<span><v:dateinput class="brd w500" name="applicant.birthDate" id="applicant.birthDate"  path="applicant.birthDate" /></span>
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="applicant.citizenshipId">гражданин на</label></span>
				<nacid:combobox name="applicant.citizenshipId" id="applicant.citizenshipId" attributeName="applicantCitizenship" style="brd w308"/>
			</p>
		</fieldset>
	</div>

	<div class="clr10"><!--  --></div>
	<fieldset><legend>Данни за документ за самоличност на заявителя</legend>
		<div class="clr"><!--  --></div>
		<v:hidden id="applicantDocuments.id" path="applicantDocuments.id" name="applicantDocuments.id"/>
		<v:hidden id="applicantDocuments.active" path="applicantDocuments.active" name="applicantDocuments.active"/>
		<v:hidden id="applicationDetails.applicantDocumentsId" path="applicationDetails.applicantDocumentsId" name="applicationDetails.applicantDocumentsId"/>
		<p class="checkbox"><input value="1" type="checkbox" id="document_data" name="document_data" onclick="documentDataButtonClicked();"/>
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
		<p><span class="fieldlabel2"><label for="applicationDetails.applicantPersonalIdDocumentType">Тип документ за самоличност</label></span>
			<nacid:combobox name="applicationDetails.applicantPersonalIdDocumentType" id="applicationDetails.applicantPersonalIdDocumentType" attributeName="applicantPersonalIdDocumentType" style="brd w308"/>
		</p>
		<div class="clr"><!--  --></div>
	</fieldset>
	<script type="text/javascript">


	</script>
	<div class="clr10"><!--  --></div>
	<p class="checkbox">
		<v:checkboxinput name="applicationDetails.namesDontMatch" id="applicationDetails.namesDontMatch" onclick="toggleNamesDontMatchDiv();" path="applicationDetails.namesDontMatch"/>
		<label for="applicationDetails.namesDontMatch"><b>Имената по документ за самоличност и документ за призната квалификация не съвпадат</b></label></p>		
		<div class="clr"><!--  --></div>
		<div id="names_dont_match_div">
			<fieldset><legend>Данни от документ за призната квалификация</legend> 
				<v:hidden id="trainingCourseDetails.id" name="trainingCourseDetails.id" path="trainingCourseDetails.id" />
				<p><span class="fieldlabel2"><label for="trainingCourseDetails.documentCivilIdType">Тип персонален идентификатор</label><br/></span>
					<nacid:radiobutton name="trainingCourseDetails.documentCivilIdType" attributeName="documentCivilIdType" singleline="true" onclick="documentPersonalIdTypeChanged();"/>
				</p>
				<div class="clr"><!--  --></div> 						
			
           		<p><span class="fieldlabel2"><label for="trainingCourseDetails.documentCivilId">${messages.civilIdType}</label><br/></span>
               		<v:textinput id="trainingCourseDetails.documentCivilId" name="trainingCourseDetails.documentCivilId" path="trainingCourseDetails.documentCivilId"  class="brd w200"/>
            	</p>
				<v:egnLnchValidator input="trainingCourseDetails.documentCivilId" required="$('applicationDetails.namesDontMatch').checked"/>

				<script>
					initDocumentPersonalIdIgnoreValidation();
				</script>
            	<div class="clr"><!--  --></div>
            	<p><span class="fieldlabel2"><label for="trainingCourseDetails.documentFname">Име</label><br/></span>
         			<v:textinput id="trainingCourseDetails.documentFname" class="brd w500" name="trainingCourseDetails.documentFname" path="trainingCourseDetails.documentFname" />
      			</p>
      			<div class="clr"><!--  --></div>   
      		   
      			<p><span class="fieldlabel2"><label for="trainingCourseDetails.documentSname">Презиме</label><br/></span>
         			<v:textinput id="trainingCourseDetails.documentSname" class="brd w500" name="trainingCourseDetails.documentSname" path="trainingCourseDetails.documentSname" />
      			</p>
      			<div class="clr"><!--  --></div> 
      		
      			<p><span class="fieldlabel2"><label for="trainingCourseDetails.documentLname">Фамилия</label><br/></span>
         			<v:textinput id="trainingCourseDetails.documentLname" class="brd w500" name="trainingCourseDetails.documentLname" path="trainingCourseDetails.documentLname" />
      			</p>
      		</fieldset>
		</div>
	<script type="text/javascript">
		toggleNamesDontMatchDiv();
	</script>
	<div class="clr10"><!--  --></div>
	<p class="checkbox">
    	<v:checkboxinput id="applicationDetails.personalDataUsage" name="applicationDetails.personalDataUsage" path="applicationDetails.personalDataUsage" />
    	<label for="applicationDetails.personalDataUsage">Съгласен съм да се ползват лични данни за целите на проверката</label>
    </p>




	<div class="clr"><!--  --></div>
	<fieldset>
		<legend>Начин на получаване на уведомления</legend>
		<p>
			<nacid:radiobutton name="applicationDetails.documentReceiveMethodId" attributeName="documentReceiveMethod" singleline="false"  onclick="documentReceiveMethodChanged(this)"/>
		</p>
		<div id="documentRecipientDiv" style="display: ${empty requestScope["com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl"].documentRecipient ? 'none' : 'block'}">
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
					<nacid:combobox name="documentRecipient.countryId" id="documentRecipient.countryId" attributeName="country" path="documentRecipient.countryId" style="w308 brd"/>
				</p>
				<p>
					<span class="fieldlabel2"><label for="documentRecipient.mobilePhone">Мобилен телефон</label></span>
					<v:textinput class="brd w500" maxlength="50" name="documentRecipient.mobilePhone" id="documentRecipient.mobilePhone" path="documentRecipient.mobilePhone"/>
				</p>
			</fieldset>
		</div>
	</fieldset>
	<%-- 
    <p class="checkbox">
    	<v:checkboxinput id="applicationDetails.dataAuthentic" name="applicationDetails.dataAuthentic" path="applicationDetails.dataAuthentic" />
    	<label for="applicationDetails.dataAuthentic">Декларирам, че данните посочени в заявлението за издаване на удостоверение за придобита проф. квалификация на територията на Р България, необходимо за достъп или за упражняване на регулирана професия на територията на друга държава членка, са истински и автентични</label>
    </p>     --%>   
	<input name="activeForm" value="1" type="hidden" />
</v:form>
<script type="text/javascript">
	disableApplicantDocumentFields();
	setApplicantDocumentIdInDetails();
	showHideApplicant(false);
</script>