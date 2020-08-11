<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>

<h3 class="title"><span>${operationStringForScreens } заявители</span></h3>

<v:form action="${pathPrefix }/control/person/save" method="post" name="form_person" backurl="${pathPrefix }/control/person/list?getLastTableState=1">

	<v:textValidator input="applicant_first_name" maxLength="50" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
	<v:textValidator input="applicant_middle_name" maxLength="50" regex="${validations.name}" errormessage="${messages.err_name}" />
	<v:textValidator input="applicant_last_name" maxLength="50" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
	
	<v:textValidator input="applicant_personal_id" maxLength="($('rad_0').checked ? 10 : 50)" required="true"/>

	<v:textValidator input="applicant_birth_place_location" maxLength="30" required="false" regex="${validations.city}" errormessage="${messages.err_city}" />
	<v:dateValidator input="applicantBirthDate" format="d.m.yyyy" required="false" beforeToday="true"  />
	
	<nacid:applicationPersonEdit person="applicant">
		<nacid:systemmessage />
		<fieldset><legend>Лични данни</legend> <input type="hidden" name="applicant_record_id" id="applicant_record_id"
			value="${person_record_id }" />
		<p><span class="fieldlabel"><label for="applicant_personal_id_type">Тип персонален идентификатор</label><br />
		</span> <nacid:applicationCivilIdRadio>
			<input ${checked } id="rad_${value_id }" name="applicant_personal_id_type" value="${radio_id }" onclick="personalIdTypeChanged();" type="radio" />
			<label for="rad_${value_id }">${label }</label>
		</nacid:applicationCivilIdRadio></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel"><label for="applicant_personal_id">${messages.civilIdType}</label><br />
		</span> <v:textinput class="brd w200" maxlength="50" name="applicant_personal_id" id="applicant_personal_id"  value="${person_civil_id }" /></p>
		<div class="clr"><!--  --></div>
		<v:egnLnchValidator input="applicant_personal_id" required="true"/>
		
		<p><span class="fieldlabel"><label for="applicant_first_name">Име</label><br />
		</span> <v:textinput class="brd w500" maxlength="50" name="applicant_first_name" id="applicant_first_name"  value="${person_first_name }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="applicant_middle_name">Презиме</label><br />
		</span> <v:textinput class="brd w500" maxlength="50" name="applicant_middle_name" id="applicant_middle_name"  value="${person_middle_name }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="applicant_last_name">Фамилия</label><br />
		</span> <v:textinput class="brd w500" maxlength="50" name="applicant_last_name" id="applicant_last_name"  value="${person_last_name }" /></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel"><label for="applicant_birth_place_country">${messages.birthCountry}</label><br />
		</span> <nacid:combobox name="applicant_birth_place_country" id="applicant_birth_place_country" attributeName="applicantBirthCountry" style="brd w308" /></p>
		<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel"><label for="applicant_birth_place_location">${messages.birthCity}</label><br />
		</span> <v:textinput class="brd w500" maxlength="30" name="applicant_birth_place_location" id="applicant_birth_place_location" 
			value="${person_birth_city }" /></p>
		<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel"><label for="applicantBirthDate">${messages.birthDate}</label><br/></span>
	        <v:dateinput style="brd w200" name="applicantBirthDate" value="${personBirthDate }" />
	    </p>
      	<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel"><label for="applicantCitizenshipId">гражданин на</label><br />
		</span> <nacid:combobox name="applicantCitizenshipId" id="applicantCitizenshipId" attributeName="applicantCitizenshipId" style="brd w308" /></p>
		<div class="clr"><!--  --></div>

		
		</fieldset>


	</nacid:applicationPersonEdit>
</v:form>

<script type="text/javascript">
	function personalIdTypeChanged() {
		$('applicant_personal_id').clear();
		clearInputError($('applicant_personal_id'));
		initPersonalIdIgnoreValidation();
	}

	function initPersonalIdIgnoreValidation() {
		$('applicant_personal_idIgnoreEGNValidation').checked = !($('rad_0').checked);
		$('applicant_personal_idIgnoreEGNValidation').ancestors().first().hide();

		$('applicant_personal_idIgnoreLNCHValidation').checked = !($('rad_1').checked);
		$('applicant_personal_idIgnoreLNCHValidation').ancestors().first().hide();
	}
	initPersonalIdIgnoreValidation();
</script>

<%@ include file="/screens_regprof/footer.jsp"%>
