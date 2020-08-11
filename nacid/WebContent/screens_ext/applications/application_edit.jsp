<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@ page import="com.nacid.bl.nomenclatures.ApplicationType" %>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<script type="text/javascript">
var defaultCountryId = <%= NomenclaturesDataProvider.COUNTRY_ID_BULGARIA %>;
function isHomeCountryBulgaria() {
	var select = $("home_country_id");
	return select[select.selectedIndex].value == defaultCountryId
}
function toggleBulgariaContact(select) {
	  if (isHomeCountryBulgaria()) {
		  $("bulgaria_contact").hide();
		} else {
			$("bulgaria_contact").show();
		}
}
function showHideDiplomaNames() {
	if ($('diploma_names').checked) {
		$('diploma_names_div').show();
	} else {
		$('diploma_names_div').hide();
	}
}
    function representativePersonalIdTypeChanged() {
        $('representative_personal_id').clear();
        clearInputError($('representative_personal_id'));
        initRepresentativePersonalIdIgnoreValidation();
    }

    function initRepresentativePersonalIdIgnoreValidation() {
        $('representative_personal_idIgnoreEGNValidation').checked = !($('representative_personal_id_type_rad_0').checked);
		$('representative_personal_idIgnoreEGNValidation').ancestors().first().hide();

		$('representative_personal_idIgnoreLNCHValidation').checked = !($('representative_personal_id_type_rad_1').checked);
		$('representative_personal_idIgnoreLNCHValidation').ancestors().first().hide();
    }


	function applicantPersonalIdTypeChanged() {
		$('applicant_id').value = "";
		$('applicant_personal_id').clear();
		clearInputError($('applicant_personal_id'));
		initApplicantPersonalIdIgnoreValidation();
		loadApplicantDetails();
	}
	
	function initApplicantPersonalIdIgnoreValidation() {
		
		$('applicant_personal_idIgnoreEGNValidation').checked = !($('applicant_personal_id_type_rad_0').checked);
		$('applicant_personal_idIgnoreEGNValidation').ancestors().first().hide();

		$('applicant_personal_idIgnoreLNCHValidation').checked = !($('applicant_personal_id_type_rad_1').checked);
		$('applicant_personal_idIgnoreLNCHValidation').ancestors().first().hide();
	}
	function showHideApplicant() {
		if ($("different_applicant_representative").checked) {
			$("different_applicant_representative_div").show();
			$("representative_birth_fields").hide();
		}	else {
			$("different_applicant_representative_div").hide();
			$("representative_birth_fields").show();
		}

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
		$("recipient_address").value = $("home_addr_details").value;
		$("recipient_post_code").value = $("home_pcode").value;
		$("recipient_city").value = $("home_city").value;
		$("recipient_mobile_phone").value = $("homePhone").value;
		$("recipient_country").value = $("home_country_id").value;
		$("recipient_district").value = "";
		$("recipient_name").value = $("different_applicant_representative").checked ? ($("applicant_first_name").value + ($("applicant_second_name").value == '' ? "" : " " + $("applicant_second_name").value)  + " " + $("applicant_last_name").value) : $("representative_full_name").innerHTML ;
	}
	function loadApplicantDetails() {
		var civilIdTypeEl = $$('input:checked[type=radio][name=applicant_personal_id_type]')[0];
		var _civilIdType = civilIdTypeEl == null ? null : civilIdTypeEl.value;
		var _civilId = $("applicant_personal_id").value;
		var form = $("appform1");
		if (_civilIdType != null && _civilId.length > 3) {
			new Ajax.Request('${pathPrefix }/control/ext_person_ajax', {
				parameters: {civilId: _civilId ,
					civilIdType: _civilIdType } ,
				onCreate : function(oXHR) {
					// setLoading(form);
				},
				onSuccess: function(transport){
					// removeLoading(form);
					var json = transport.responseText.evalJSON(true);
					if (json.id === undefined) {

					} else {
						$("applicant_id").value = json.id;
						$("applicant_first_name").value = json.fname;
						$("applicant_second_name").value = json.sname;
						$("applicant_last_name").value = json.lname;
						$("applicant_email").value = json.email;
					}
				},
				onFailure: function(){
					// removeLoading(form);
					alert("Проблем при зареждане на данните на заявителя");
				}
			});
		}
	}
</script>
<h3 class="title"><span>${messages.applicationdata }</span></h3>
<nacid:systemmessage name="applicationStatusMessage" />
<ext_nacid:extApplicationEdit>
	
	<v:form name="appform1" action="${pathPrefix }/control/application/save" method="post"
		backurl="${pathPrefix }/control/application/list/${application_type}?getLastTableState=1" id="appform1">
        <v:textValidator input="diploma_firstName" maxLength="15" required="$('diploma_names').checked" regex="${validations.name}"
			errormessage="${messages.err_name}" />
		<v:textValidator input="diploma_middleName" maxLength="15" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="diploma_lastName" maxLength="30" required="$('diploma_names').checked" regex="${validations.name}"
			errormessage="${messages.err_name}" />


		<v:comboBoxValidator input="home_country_id" required="true" />

        <v:textValidator input="home_pcode" maxLength="12" required="true" regex="${validations.post_code}" errormessage="${messages.err_post_code}" />
        <v:textValidator input="home_city" maxLength="30" required="true" regex="${validations.city}" errormessage="${messages.err_city}" />
		<v:textValidator input="homePhone" maxLength="30" required="true"/>
		<v:textValidator input="home_addr_details" required="true"/>

		<v:textValidator input="bg_pcode" maxLength="12" required="!(isHomeCountryBulgaria())" regex="${validations.post_code}" errormessage="${messages.err_post_code}" />
		<v:textValidator input="bg_city" maxLength="30" required="!(isHomeCountryBulgaria())" regex="${validations.city}" errormessage="${messages.err_city}" />
		<v:textValidator input="bgPhone" maxLength="30" required="!(isHomeCountryBulgaria())"/>
		<v:textValidator input="bg_addr_details" required="!(isHomeCountryBulgaria())"/>



		<v:textValidator input="applicant_first_name" maxLength="100" required="$('different_applicant_representative').checked" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="applicant_second_name" maxLength="100" required="false" regex="${validations.name}" errormessage="${messages.err_name}"/>
		<v:textValidator input="applicant_last_name" maxLength="100" required="$('different_applicant_representative').checked" regex="${validations.name}" errormessage="${messages.err_name}"/>
		<v:textValidator input="applicant_email" maxLength="100" required="false" regex="${validations.email }" />

		<v:textValidator input="representative_birth_city" maxLength="255" required="!($('different_applicant_representative').checked)" regex="${validations.city }" errormessage="${messages.err_city}" />
		<v:comboBoxValidator input="representative_birth_country_id" required="!($('different_applicant_representative').checked)" />
		<v:comboBoxValidator input="representative_citizenship" required="!($('different_applicant_representative').checked)" />
		<v:dateValidator input="representative_birth_date" format="d.m.yyyy" beforeToday="true" required="!($('different_applicant_representative').checked)" emptyString="дд.мм.гггг" />
		<v:comboBoxValidator input="representative_personal_id_document_type" required="!($('different_applicant_representative').checked)" />

		<v:textValidator input="applicant_birth_city" maxLength="255" required="($('different_applicant_representative').checked)" regex="${validations.city }" errormessage="${messages.err_city}" />
		<v:comboBoxValidator input="applicant_birth_country_id" required="($('different_applicant_representative').checked)" />
		<v:comboBoxValidator input="applicant_citizenship" required="($('different_applicant_representative').checked)" />
		<v:dateValidator input="applicant_birth_date" format="d.m.yyyy" beforeToday="true" required="($('different_applicant_representative').checked)" emptyString="дд.мм.гггг" />
		<v:comboBoxValidator input="applicant_personal_id_document_type" required="($('different_applicant_representative').checked)" />



		<v:comboBoxValidator input="recipient_country" required="$('documentRecipientDiv').visible()"/>

		<input type="hidden" name="application_type" value="${application_type }" />
		<input type="hidden" name="id" value="${application_id }" />

		<fieldset><legend>Лични данни на подателя</legend>
		<div class="clr"><!--  --></div>

        <c:choose>
            <c:when  test="${edit_representative_personal_id}">
                <p>
                    <span class="fieldlabel"><label>Тип персонален идентификатор</label></span>
                    <span><nacid:radiobutton name="representative_personal_id_type" attributeName="representativeCivilIdType" singleline="true" onclick="representativePersonalIdTypeChanged();"/></span>
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label>${messages.civilIdType}</label></span>
                    <span><v:textinput class="brd w500" maxlength="100" name="representative_personal_id" id="representative_personal_id"  value="" /></span>
                </p>
                <v:egnLnchValidator input="representative_personal_id" required="true"/>

                <script>
                    initRepresentativePersonalIdIgnoreValidation();
                </script>
            </c:when>
            <c:otherwise>
                <p>
                    <span class="fieldlabel"><label>Тип персонален идентификатор</label></span>
                    <span>${representative_personal_id_type }</span>
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label>${messages.civilIdType}</label></span>
                    <span>${representative_personal_id }</span>
                </p>
                <div class="clr"><!--  --></div>
            </c:otherwise>
        </c:choose>

		<p><span class="fieldlabel"><label>Име</label></span> <span id="representative_first_name">${representative_first_name }</span></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label>Презиме</label></span> <span id="representative_middle_name">${representative_middle_name }</span></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label>Фамилия</label></span> <span id="representative_last_name">${representative_last_name }</span></p>
		<p><span class="fieldlabel"><label>Електронна поща</label></span> <span>${representative_email }</span></p>
        <div class="clr"><!--  --></div>
			<span style="display:none" id="representative_full_name">${representative_full_name}</span>

		<div id="representative_birth_fields" style="display: ${is_different_applicant_representative ? 'none' : 'block'}">
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthCountry}</label></span>
				<nacid:combobox name="representative_birth_country_id" id="representative_birth_country_id" attributeName="representativeBirthCountry" style="w308 brd" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthCity}</label></span>
				<span><v:textinput class="brd w500" maxlength="255" name="representative_birth_city" id="representative_birth_city"  value="${representative_birth_city}" /></span>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>${messages.birthDate}</label></span>
				<span><v:dateinput class="brd w500" name="representative_birth_date" id="representative_birth_date"  value="${representative_birth_date}" /></span>
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="representative_citizenship">гражданин на</label></span>
				<nacid:combobox name="representative_citizenship" id="representative_citizenship" attributeName="representativeCitizenship" style="brd w308"/>
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="representative_personal_id_document_type">Тип документ за самоличност</label></span>
				<nacid:combobox name="representative_personal_id_document_type" id="representative_personal_id_document_type" attributeName="applicantPersonalIdDocumentType" style="brd w308"/>
			</p>
		</div>
		</fieldset>
		<div class="clr20"><!--  --></div>
		<p class="checkbox">
			<input ${different_applicant_representative } id="different_applicant_representative" name="different_applicant_representative" value="1" onclick="showHideApplicant();" type="checkbox" />
			<label for="different_applicant_representative"><b>Заявителят и подателят са различни лица</b></label>
		</p>
		<div id="different_applicant_representative_div" ${different_applicant_representative_div_show}>
			<fieldset><legend>Лични данни на заявителя</legend>
				<p>
					Моля въведете имена по документ за самоличност. За български граждани е задължително въвеждане на ЕГН.
				</p>
				<v:hidden value="${applicant_id}" name="applicant_id" id="applicant_id" />
				<p>
					<span class="fieldlabel"><label>Тип персонален идентификатор</label></span>
					<span><nacid:radiobutton name="applicant_personal_id_type" attributeName="applicantCivilIdType" singleline="true" onclick="applicantPersonalIdTypeChanged();"/></span>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label>${messages.civilIdType}</label></span>
					<span><v:textinput class="brd w500" maxlength="100" name="applicant_personal_id" id="applicant_personal_id"  value="${applicant_personal_id}" onchange="$('applicant_id').value = '';loadApplicantDetails();" autocomplete="off"/></span>
				</p>
				<v:egnLnchValidator input="applicant_personal_id" required="$('different_applicant_representative').checked"/>

				<script>
					initApplicantPersonalIdIgnoreValidation();
				</script>

				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="applicant_first_name">Име</label></span>
					<v:textinput class="brd w500" maxlength="100" name="applicant_first_name" id="applicant_first_name" value="${applicant_first_name }"/>
				</p>
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel"><label for="applicant_second_name">Презиме</label></span>
					<v:textinput class="brd w500" maxlength="100" name="applicant_second_name" id="applicant_second_name" value="${applicant_second_name }"/>
				</p>
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel"><label for="applicant_last_name">Фамилия</label></span>
					<v:textinput class="brd w500" maxlength="100" name="applicant_last_name" id="applicant_last_name" value="${applicant_last_name }"/>
				</p>
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel"><label for="applicant_email">Електронна поща</label></span>
					<v:textinput class="brd w500" maxlength="100" name="applicant_email" id="applicant_email" value="${applicant_email }"/>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label>${messages.birthCountry}</label></span>
					<nacid:combobox name="applicant_birth_country_id" id="applicant_birth_country_id" attributeName="applicantBirthCountry" style="w308 brd" />
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label>${messages.birthCity}</label></span>
					<span><v:textinput class="brd w500" maxlength="255" name="applicant_birth_city" id="applicant_birth_city"  value="${applicant_birth_city}" /></span>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label>${messages.birthDate}</label></span>
					<span><v:dateinput class="brd w500" name="applicant_birth_date" id="applicant_birth_date"  value="${applicant_birth_date}" /></span>
				</p>
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel"><label for="applicant_citizenship">гражданин на</label></span>
					<nacid:combobox name="applicant_citizenship" id="applicant_citizenship" attributeName="applicantCitizenship" style="brd w308"/>
				</p>
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel"><label for="applicant_personal_id_document_type">Тип документ за самоличност</label></span>
					<nacid:combobox name="applicant_personal_id_document_type" id="applicant_personal_id_document_type" attributeName="applicantPersonalIdDocumentType" style="brd w308"/>
				</p>

			</fieldset>
		</div>

		<div class="clr20"><!--  --></div>
		<p class="checkbox">
		  <input ${diploma_names_selected } id="diploma_names" name="diploma_names" value="1" onclick="showHideDiplomaNames();" type="checkbox" />
		  <label for="diploma_names"><b>Имената по документ за самоличност и диплома не съвпадат</b></label>
		</p>
		<!-- // start of diploma_names_div -->
		<div id="diploma_names_div" ${ diploma_names_div_show }>
			<fieldset><legend>Данни за дипломата</legend>
			  <p>
			      <span class="fieldlabel"><label for="diploma_firstName">Име</label></span> 
			      <v:textinput class="brd w500" maxlength="100" name="diploma_firstName" id="diploma_firstName"  value="${diploma_firstName }" />
			  </p>
			  <div class="clr"><!--  --></div>
			  <p>
			      <span class="fieldlabel"><label for="diploma_middleName">Презиме</label></span>
			      <v:textinput class="brd w500" maxlength="100" name="diploma_middleName" id="diploma_middleName"  value="${diploma_middleName }" />
			  </p>
			  <div class="clr"><!--  --></div>
			  <p>
			      <span class="fieldlabel"><label for="diploma_lastName">Фамилия</label></span>
			      <v:textinput class="brd w500" maxlength="100" name="diploma_lastName" id="diploma_lastName"  value="${diploma_lastName }" />
			  </p>
			  <div class="clr"><!--  --></div>
			</fieldset>
		</div>
		<!-- // end of diploma_names_div -->
		<div class="clr20"><!--  --></div>
		<fieldset><legend>За контакт</legend>
		  <p>
		      <span class="fieldlabel2"><label for="home_country_id">Държава</label></span> 
		      <nacid:combobox onchange="toggleBulgariaContact();" name="home_country_id" id="home_country_id" attributeName="homeCountry" style="w308 brd" />
		  </p>
		  <div class="clr"><!--  --></div>
		  <p>
		      <span class="fieldlabel2"><label for="home_city">Населено място</label></span>
		      <v:textinput class="brd w500" maxlength="30" name="home_city" id="home_city"  value="${home_city }" />
		  </p>
		  <div class="clr"><!--  --></div>
		  <p>
		      <span class="fieldlabel2"><label for="home_pcode">Пощенски код</label></span>
		      <v:textinput class="brd w300" maxlength="12" name="home_pcode" id="home_pcode"  value="${home_post_code }" />
		  </p>
		  <div class="clr"><!--  --></div>
		  <p>
		      <span class="fieldlabel2"><label for="home_addr_details">Адрес</label></span>
		      <textarea class="brd w600" rows="3" cols="40" name="home_addr_details" id="home_addr_details">${home_address_details }</textarea>
		  </p>
		  <p>
		      <span class="fieldlabel2"><label for="homePhone">Телефон</label></span>
		      <v:textinput class="brd w500" name="homePhone"  id="homePhone" value="${homePhone }" />
		  </p>
		</fieldset>
		<div class="clr20"><!--  --></div>
		<div id="bulgaria_contact" ${bulgaria_contact_show }>
		  <fieldset><legend>За контакт в България</legend>
			  <p>
			      <span class="fieldlabel2"><label for="bg_city">Населено място</label></span>
			      <v:textinput class="brd w500" maxlength="30" name="bg_city"  value="${bg_city }" />
			  </p>
			  <div class="clr"><!--  --></div>
			  <p>
			      <span class="fieldlabel2"><label for="bg_pcode">Пощенски код</label></span>
			      <v:textinput class="brd w300" maxlength="12" name="bg_pcode" id="bg_pcode"  value="${bg_post_code }" />
			  </p>
			  <div class="clr"><!--  --></div>
		      <p>
		          <span class="fieldlabel2"><label for="bg_addr_details">Адрес</label></span> 
		          <textarea class="brd w600" rows="3" cols="40" name="bg_addr_details">${bg_address_details }</textarea>
		      </p>
		      <div class="clr"><!--  --></div>
		      <p>
		          <span class="fieldlabel2"><label for="bgPhone">Телефон</label></span>
		          <v:textinput class="brd w500" name="bgPhone"  value="${bgPhone }" />
		      </p>
		      <div class="clr"><!--  --></div>
		  </fieldset>
		</div>
		<div class="clr20"><!--  --></div>
        <p class="checkbox">
			<input id="personal_data_usage" name="personal_data_usage" value="1" type="checkbox" ${personal_data_usage } />
			<label for="personal_data_usage">Съгласен съм да се ползват лични данни за целите на проверката<br />(Моля изтеглете декларацията, подпишете я и я прикачете в раздела Приложения)</label>
			<a href="${dataUsageDeclarationUrl}">Декларация</a>
		</p>

		<p class="checkbox"><input id="data_authentic" name="data_authentic" value="1" type="checkbox" ${data_authentic } /><label
            for="data_authentic">${isDoctorateApplicationType ? messages.dataAuthenticDeclarationDoctorate : messages.dataAuthenticDeclaration}</label></p>
		<fieldset>
			<legend>Начин на получаване на уведомления</legend>
			<p>
				<nacid:radiobutton name="document_receive_method_id" attributeName="documentReceiveMethod" singleline="false" onclick="documentReceiveMethodChanged(this)"/>
			</p>
			<div id="documentRecipientDiv" style="display: ${not empty applicationWebModel.documentRecipientWebModel ? 'block' : 'none'}">
				<fieldset>
					<legend>Адрес на получател</legend>
					<p>
						<a href="javascript:void(0);" onclick="copyContactAddress()" class="flt_rgt">Копирай адреса за контакт</a>
					</p>
					<div class="clr"><!-- --></div>
					<p>
						<span class="fieldlabel2"><label for="recipient_name" class="required" >Име*</label></span>
						<v:textinput class="brd w500" maxlength="255" name="recipient_name" id="recipient_name" value="${applicationWebModel.documentRecipientWebModel.name }"/>
					</p>
					<p>
						<span class="fieldlabel2"><label for="recipient_address" class="required">Улица №*</label></span>
						<textarea class="brd w500" rows="3" cols="40" name="recipient_address" id="recipient_address">${applicationWebModel.documentRecipientWebModel.address }</textarea>
					</p>
					<p>
						<span class="fieldlabel2"><label for="recipient_district" class="required">Област*</label></span>
						<v:textinput class="brd w500" maxlength="100" name="recipient_district" id="recipient_district" value="${applicationWebModel.documentRecipientWebModel.district }"/>
					</p>
					<p>
						<span class="fieldlabel2"><label for="recipient_post_code" class="required">Пощенски код*</label></span>
						<v:textinput class="brd w500" maxlength="255" name="recipient_post_code" id="recipient_post_code" value="${applicationWebModel.documentRecipientWebModel.postCode }"/>
					</p>
					<p>
						<span class="fieldlabel2"><label for="recipient_city" class="required">Град*</label></span>
						<v:textinput class="brd w500" maxlength="100" name="recipient_city" id="recipient_city" value="${applicationWebModel.documentRecipientWebModel.city }"/>
					</p>
					<p>
						<span class="fieldlabel2"><label for="recipient_country" class="required">Държава</label></span>
						<nacid:combobox name="recipient_country" id="recipient_country" attributeName="recipientCountryCombo" style="w308 brd"/>
					</p>
					<p>
						<span class="fieldlabel2"><label for="recipient_mobile_phone" class="required">Мобилен телефон*</label></span>
						<v:textinput class="brd w500" maxlength="50" name="recipient_mobile_phone" id="recipient_mobile_phone" value="${applicationWebModel.documentRecipientWebModel.mobilePhone }"/>
					</p>
				</fieldset>
			</div>
		</fieldset>
		<input name="activeForm" value="1" type="hidden" />
	</v:form>
</ext_nacid:extApplicationEdit>
