<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.nacid.bl.nomenclatures.ApplicationType"%>
<%@ page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider" %>
<%@ page import="com.nacid.web.ApplicantTypeHelper" %>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
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
}
function updateOwnerData(value, data) {
    updateCivilIdData(data, "owner");
}
function updateRepresentativeData(value, data) {
	  updateCivilIdData(data, "representative");
	}
function updateCivilIdData(data, name) {
	var d = data.evalJSON(true);
	$(name + '_record_id').value = d.recordid;
	$(name + '_first_name').value = d.fname;
	$(name + '_middle_name').value = d.sname;
	$(name + '_last_name').value = d.lname;
	clearInputError($(name + '_first_name'));
	clearInputError($(name + '_last_name'));
	if (name == "applicant" || name == 'owner') {
	  $(name + '_birth_place_country').value = d.birthcountryid;
	  $(name + '_birth_place_location').value = d.birthcity;
	  $(name + '_citizenship').value = d.citizenshipid;
	  $(name + '_birth_date').value = d.birthdate;
	}

	disablePersonInputs(name);

	//Slaga ignore na EGN validator-a, zashtoto ako e izbran zapis ot bazata s greshno EGN nqma nujda ot validaciq na egn-to!!!
	$(name + '_personal_idIgnoreEGNValidation').checked = true;
	$(name + '_personal_idIgnoreLNCHValidation').checked = true;
}
function disablePersonInputs(name) {
	$(name + '_first_name').disable();
	$(name + '_middle_name').disable();
	$(name + '_last_name').disable();
	if (name == "applicant" || name == 'owner') {
	  $(name + '_birth_place_country').disable();
	  $(name + '_birth_place_location').disable();
	  $(name + '_citizenship').disable();
	  $(name + '_birth_date').disable();
	}
}
function getRadioChecked(radio) {
//alert("Inside radio checked....");
	for (var i = 0; i < radio.length; i++) {
	    if (radio[i].checked) {
	     return radio[i].value;
	    }    
	}
	return null;
}

function personalIdChanged(name) {
	if ($(name + '_record_id').value != "") {
	  enablePersonInputs(name);
	}
	clearInputError($(name + '_personal_id'));
  initPersonalIdIgnoreValidation(name);
  
}
function enablePersonInputs(name) {
	//Maha ignore na EGN validator-a
  $(name + '_personal_idIgnoreEGNValidation').checked = false;
  $(name + '_personal_idIgnoreLNCHValidation').checked = false;

  $(name + '_record_id').clear();
  $(name + '_first_name').clear();
  $(name + '_middle_name').clear();
  $(name + '_last_name').clear();

	$(name + '_first_name').enable();
  $(name + '_middle_name').enable();
  $(name + '_last_name').enable();
  if (name == "applicant" || name == 'owner') {
    $(name + '_birth_place_country').value = '-';
    $(name + '_birth_place_location').clear();
    $(name + '_birth_place_country').enable();
    $(name + '_birth_place_location').enable();

    $(name + '_citizenship').value = '-';
    $(name + '_birth_date').clear();
    $(name + '_citizenship').enable();
    $(name + '_birth_date').enable();
  }
}
function emptyPersonalId(name) {
	$(name + '_personal_id').clear();
	personalIdChanged(name);
}
function showHideDiplomaNames() {
	if ($('diploma_names').checked) {
		$('diploma_names_div').show();
	} else {
		$('diploma_names_div').hide();
	}
}
function showHideRepresentativeData() {
	if ($('representative_data').checked) {
	  $('representative_data_div').show();
	} else {
	  $('representative_data_div').hide();
	}
}
function initPersonalIdIgnoreValidation(name) {
    $(name + '_personal_idIgnoreEGNValidation').checked = !($(name + '_rad_0').checked) || $(name + '_record_id').value != '';
    $(name + '_personal_idIgnoreLNCHValidation').checked = !($(name + '_rad_1').checked) || $(name + '_record_id').value != '';
    $(name + '_personal_idIgnoreEGNValidation').ancestors().first().hide();
    $(name + '_personal_idIgnoreLNCHValidation').ancestors().first().hide();
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
    $("recipient_address").value = $("applicant_address").value;
    $("recipient_post_code").value = $("applicant_location_post_code").value;
    $("recipient_city").value = $("applicant_location").value;
    $("recipient_mobile_phone").value = $("homePhone").value;
    $("recipient_country").value = $("applicant_location_country").value;
    $("recipient_district").value = "";
    $("recipient_name").value = $("applicant_first_name").value + " " + ($("applicant_middle_name").value == "" ? "" : $("applicant_middle_name").value + " ") + $("applicant_last_name").value;

}


</script>
<h3 class="title"><span>${messages.applicationdata }</span></h3>
<nacid:systemmessage name="applicationStatusMessage"/>
<nacid:applicationEdit>
	<h3 class="names">${application_header }</h3>
	<v:form name="appform1" action="${pathPrefix }/control/applications/save" method="post"
		backurl="${pathPrefix }/control/${back_screen}/list?application_type=${application_type}&getLastTableState=1" addprintbutton="${!empty application_id}"
		printurl="${pathPrefix}/control/applications/print?id=${application_id}">
		
		<v:textValidator input="applicant_first_name" maxLength="50" required="getApplicantTypeValue() == 0" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="applicant_middle_name" maxLength="50" regex="${validations.name}" errormessage="${messages.err_name}"/>
		<v:textValidator input="applicant_last_name" maxLength="50" required="getApplicantTypeValue() == 0" regex="${validations.name}" errormessage="${messages.err_name}" />

		<v:textValidator input="diploma_firstName" maxLength="50" required="$('diploma_names').checked" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="diploma_middleName" maxLength="50" regex="${validations.name}" errormessage="${messages.err_name}"/>
		<v:textValidator input="diploma_lastName" maxLength="50" required="$('diploma_names').checked" regex="${validations.name}" errormessage="${messages.err_name}" />
		

		<v:textValidator input="representative_first_name" maxLength="15" required="$('representative_data').checked" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="representative_middle_name" maxLength="15" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="representative_last_name" maxLength="30" required="$('representative_data').checked" regex="${validations.name}" errormessage="${messages.err_name}" />
		
    <v:textValidator input="applicant_email" maxLength="100" required="false" regex="${validations.email }" />
    <v:textValidator input="applicant_location_post_code" maxLength="12" required="false" regex="${validations.post_code}" errormessage="${messages.err_post_code}" />
    <v:textValidator input="applicant_location" maxLength="30" required="false" regex="${validations.city}" errormessage="${messages.err_city}" />
    <v:textValidator input="bulgaria_contact_location_post_code" maxLength="12" required="false" regex="${validations.post_code}" errormessage="${messages.err_post_code}" />
    <v:textValidator input="bulgaria_contact_location" maxLength="30" required="false" regex="${validations.city}" errormessage="${messages.err_city}" />
    <v:textValidator input="applicant_birth_place_location" maxLength="30" required="false" regex="${validations.city}" errormessage="${messages.err_city}" />
    <v:comboBoxValidator input="applicant_birth_place_country" required="getApplicantTypeValue() == 0" />
    <v:textValidator input="applicant_personal_id_type" maxLength="10" required="false" />
    
    <v:textValidator input="bgPhone" maxLength="30" />
    <v:textValidator input="homePhone" maxLength="255" />
    <v:textValidator input="reprCity" maxLength="30" regex="${validations.city}" errormessage="${messages.err_city}" />
    <v:textValidator input="reprPcode" maxLength="12" regex="${validations.post_code}" errormessage="${messages.err_post_code}" />
    <v:textValidator input="reprPhone" maxLength="30" />
    
    <v:textValidator input="applicant_personal_id" maxLength="($('applicant_rad_1').checked ? 10 : 50)" required="getApplicantTypeValue() == 0"/>
    <v:textValidator input="representative_personal_id" maxLength="($('representative_rad_1').checked ? 10 : 50)" required="$('representative_data').checked" />

	<v:dateValidator input="applicant_birth_date" format="d.m.yyyy" required="false" beforeToday="true"  />
	<v:comboBoxValidator input="company" required="($('representative_data').checked == true && $('represent_is_company').checked == true)" errormessage="Моля въведете фирма от списъка"/>

        <v:textValidator input="recipient_name" maxLength="255" required="$('documentRecipientDiv').visible()"/>
        <v:textValidator input="recipient_district" maxLength="100" required="$('documentRecipientDiv').visible()"/>
        <v:textValidator input="recipient_city" maxLength="100" required="$('documentRecipientDiv').visible()"/>
        <v:textValidator input="recipient_address" required="$('documentRecipientDiv').visible()"/>
        <v:comboBoxValidator input="recipient_country" required="$('documentRecipientDiv').visible()"/>



    <input type="hidden" name="id" value="${application_id }" id="application_id"/>
    <input type="hidden" name="application_type" value="${application_type }" id="application_type"/>
        <input type="hidden" name="outgoing_number"  value="${outgoing_number}"/>
        <input type="hidden" name="internal_number"  value="${internal_number}"/>
        <c:set value="${isRudiApplicationType || isDoctorateApplicationType}" var="sameOwnerAndApplicant"/>
        <fieldset>
            <legend>Лични данни на заявителя</legend>



            <p><span class="fieldlabel"><label>Тип заявител</label></span>
                <c:forEach items="<%=ApplicantTypeHelper.APPLICANT_TYPE_ID_TO_NAME%>" var="item" varStatus="status">
                    <input type="radio" id="applicant_type_${status.count}" value="${item.key}" name="applicant_type"
                           <c:if test="${applicant_type == item.key}">checked="checked" </c:if>
                           <c:if test="${sameOwnerAndApplicant}">disabled="disabled" </c:if>
                           onchange="applicantTypeChanged();">
                    <label for="applicant_type_${status.count}">${item.value}</label>
                </c:forEach>
            </p>
            <c:if test="${sameOwnerAndApplicant}" >
                <input type="hidden" name="applicant_type" value="${applicant_type}"/>
            </c:if>
            <div class="clr"><!--  --></div>

                <div id="applicant_div">
                    <nacid:applicationPersonEdit person="applicant">
                    <input type="hidden" name="applicant_record_id" id="applicant_record_id"
                           value="${person_record_id }"/>
                    <p><span class="fieldlabel"><label for="applicant_personal_id_type">Тип персонален идентификатор</label></span>
                        <nacid:applicationCivilIdRadio>
                            <input ${checked } id="applicant_rad_${value_id }" name="applicant_personal_id_type" value="${radio_id }" onclick="emptyPersonalId('applicant');"
                                               type="radio"/>
                            <label for="applicant_rad_${value_id }">${label }</label>
                        </nacid:applicationCivilIdRadio></p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="applicant_personal_id">${messages.civilIdType}</label></span>
                        <v:textinput class="brd w200" maxlength="255" name="applicant_personal_id" id="applicant_personal_id" value="${person_civil_id }"
                                     onkeydown="if(!isSpecialKeyPressed(event)) personalIdChanged('applicant');"/></p>
                    <div class="clr"><!--  --></div>
                    <script type="text/javascript">
                        new Autocomplete('applicant_personal_id', {
                            serviceUrl: '/nacid/control/civilidsuggestion',
                            // callback function:
                            onSelect: updateApplicantData,
                            width: 500,
                            dynamicParameters: function () {
                                return {civilidtype: getRadioChecked(document.appform1.applicant_personal_id_type)};
                            }
                        });
                    </script>
                    <v:egnLnchValidator input="applicant_personal_id" required="getApplicantTypeValue() == 0"/>
                    <p><span class="fieldlabel"><label for="applicant_first_name">Име</label></span>
                        <v:textinput class="brd w500" maxlength="100" name="applicant_first_name" id="applicant_first_name" value="${person_first_name }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="applicant_middle_name">Презиме</label></span>
                        <v:textinput class="brd w500" maxlength="100" name="applicant_middle_name" id="applicant_middle_name" value="${person_middle_name }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="applicant_last_name">Фамилия</label></span>
                        <v:textinput class="brd w500" maxlength="100" name="applicant_last_name" id="applicant_last_name" value="${person_last_name }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="applicant_birth_place_country">${messages.birthCountry}</label></span>
                        <nacid:combobox name="applicant_birth_place_country" id="applicant_birth_place_country" attributeName="applicantBirthCountry" style="brd w308"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="applicant_birth_place_location">${messages.birthCity}</label></span>
                        <v:textinput class="brd w500" maxlength="30" name="applicant_birth_place_location" id="applicant_birth_place_location" value="${person_birth_city }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="applicant_birth_date">${messages.birthDate}</label></span>
                        <v:dateinput id="applicant_birth_date" style="brd w200" name="applicant_birth_date" value="${personBirthDate }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="applicant_citizenship">гражданин на</label></span>
                        <nacid:combobox name="applicant_citizenship" id="applicant_citizenship" attributeName="applicantCitizenship" style="brd w308"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    </nacid:applicationPersonEdit>

                    <p><span class="fieldlabel"><label for="applicant_personal_id_document_type">Тип документ за самоличност</label></span>
                        <nacid:combobox name="applicant_personal_id_document_type" id="applicant_personal_id_document_type" attributeName="applicantPersonalIdDocumentType" style="brd w308"/>
                    </p>
                    <div class="clr"><!--  --></div>
                </div>

            <div id="applicant_legal_person">
                <c:set var="applicantCompany" value="${applicationWebModel.applicantCompany}" />
                <input type="hidden" name="applicant_company_id" id="applicant_company_id" value="${applicantCompany.id}"/>
                <p><span class="fieldlabel"><label for="applicant_company_eik">Булстат/ЕИК</label></span>
                    <v:textinput class="brd w200"  id="applicant_company_eik" name="applicant_company_eik" maxlength="255" value="${applicantCompany.eik}" autocomplete="off" onchange="clearCompanyFields();enableCompanyFields();"/>
                </p>
                <p><span class="fieldlabel"><label for="applicant_company_name">Наименование</label></span>
                    <v:textinput class="brd w200"  id="applicant_company_name" name="applicant_company_name" maxlength="255" value="${applicantCompany.name}" autocomplete="off" onchange="clearCompanyFields();enableCompanyFields();" />
                </p>
                <p><span class="fieldlabel"><label for="applicant_company_country_id">Държава</label></span>
                    <nacid:combobox name="applicant_company_country_id" id="applicant_company_country_id" attributeName="applicantCompanyCountry" style="brd w308" />
                </p>
                <p><span class="fieldlabel"><label for="applicant_company_city">Град</label></span>
                    <v:textinput class="brd w200"  id="applicant_company_city" name="applicant_company_city" maxlength="255" value="${applicantCompany.city}" autocomplete="off" />
                </p>
                <p><span class="fieldlabel"><label for="applicant_company_pcode">Пощенски код</label></span>
                    <v:textinput class="brd w200"  id="applicant_company_pcode" name="applicant_company_pcode" maxlength="255" value="${applicantCompany.pcode}" autocomplete="off" />
                </p>
                <p><span class="fieldlabel"><label for="applicant_company_address">Адрес</label></span>
                    <v:textinput class="brd w200"  id="applicant_company_address" name="applicant_company_address" maxlength="255" value="${applicantCompany.addressDetails}" autocomplete="off" />
                </p>
            </div>



        </fieldset>
        <div class="clr20"><!-- --></div>
        <c:if test="${!sameOwnerAndApplicant}" >
        <fieldset>
            <legend>Лични данни на собственика на дипломата</legend>
            <nacid:applicationPersonEdit person="owner">
                <div id="applicant_div">
                    <input type="hidden" name="owner_record_id" id="owner_record_id"
                           value="${person_record_id }"/>
                    <p><span class="fieldlabel"><label>Тип персонален идентификатор</label></span>
                        <nacid:applicationCivilIdRadio>
                            <input ${checked } id="owner_rad_${value_id }" name="owner_personal_id_type" value="${radio_id }" onclick="emptyPersonalId('owner');"
                                               type="radio"/>
                            <label for="owner_rad_${value_id }">${label }</label>
                        </nacid:applicationCivilIdRadio></p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="owner_personal_id">${messages.civilIdType}</label></span>
                        <v:textinput class="brd w200" maxlength="255" name="owner_personal_id" id="owner_personal_id" value="${person_civil_id }"
                                     onkeydown="if(!isSpecialKeyPressed(event)) personalIdChanged('owner');"/></p>
                    <div class="clr"><!--  --></div>
                    <script type="text/javascript">
                        new Autocomplete('owner_personal_id', {
                            serviceUrl: '/nacid/control/civilidsuggestion',
                            // callback function:
                            onSelect: updateOwnerData,
                            width: 500,
                            dynamicParameters: function () {
                                return {civilidtype: getRadioChecked(document.appform1.owner_personal_id_type)};
                            }
                        });
                    </script>
                    <v:egnLnchValidator input="owner_personal_id" required="true"/>
                    <p><span class="fieldlabel"><label for="owner_first_name">Име</label></span>
                        <v:textinput class="brd w500" maxlength="100" name="owner_first_name" id="owner_first_name" value="${person_first_name }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="owner_middle_name">Презиме</label></span>
                        <v:textinput class="brd w500" maxlength="100" name="owner_middle_name" id="owner_middle_name" value="${person_middle_name }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="owner_last_name">Фамилия</label></span>
                        <v:textinput class="brd w500" maxlength="100" name="owner_last_name" id="owner_last_name" value="${person_last_name }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="owner_birth_place_country">${messages.birthCountry}</label></span>
                        <nacid:combobox name="owner_birth_place_country" id="owner_birth_place_country" attributeName="ownerBirthCountry" style="brd w308"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="owner_birth_place_location">${messages.birthCity}</label></span>
                        <v:textinput class="brd w500" maxlength="30" name="owner_birth_place_location" id="owner_birth_place_location" value="${person_birth_city }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="owner_birth_date">${messages.birthDate}</label></span>
                        <v:dateinput id="owner_birth_date" style="brd w200" name="owner_birth_date" value="${personBirthDate }"/>
                    </p>
                    <div class="clr"><!--  --></div>
                    <p><span class="fieldlabel"><label for="owner_citizenship">гражданин на</label></span>
                        <nacid:combobox name="owner_citizenship" id="owner_citizenship" attributeName="ownerCitizenship" style="brd w308"/>
                    </p>
                    <div class="clr"><!--  --></div>
                </div>
            </nacid:applicationPersonEdit>
        </fieldset>
        </c:if>
        <div class="clr20"><!--  --></div>
        <p class="checkbox"><input ${diploma_names_selected } id="diploma_names" name="diploma_names" value="1" onclick="showHideDiplomaNames();"
                                                              type="checkbox"/><label for="diploma_names"><b>Имената по документ за самоличност и диплома не съвпадат</b></label></p>

        <div id="diploma_names_div">
            <fieldset>
                <legend>Данни за дипломата</legend>
                <p><span class="fieldlabel"><label for="diploma_firstName">Име</label></span>
                    <v:textinput class="brd w500" maxlength="100" name="diploma_firstName" id="diploma_firstName" value="${diploma_firstName }"/></p>

                <div class="clr"><!--  --></div>
                <p><span class="fieldlabel"><label for="diploma_middleName">Презиме</label></span>
                    <v:textinput class="brd w500" maxlength="100" name="diploma_middleName" id="diploma_middleName" value="${diploma_middleName }"/></p>

                <div class="clr"><!--  --></div>
                <p><span class="fieldlabel"><label for="diploma_lastName">Фамилия</label></span>
                    <v:textinput class="brd w500" maxlength="100" name="diploma_lastName" id="diploma_lastName" value="${diploma_lastName }"/></p>

                <div class="clr"><!--  --></div>
            </fieldset>
        </div>
		<!-- // diploma_names_div -->
		<div class="clr20"><!--  --></div>

        <fieldset>
            <legend>За контакт</legend>
            <p><span class="fieldlabel2"><label for="applicant_email">Електронна поща</label><br/>
		</span> <v:textinput class="brd w500" maxlength="100" name="applicant_email" id="applicant_email" value="${email }" onkeyup="cummunicateByEmailRefresh();"/></p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="applicant_location_country">Държава</label><br/>
		</span> <nacid:combobox onchange="toggleBulgariaContact(this);" name="applicant_location_country" id="applicant_location_country"
                                attributeName="homeCountry" style="w308 brd"/></p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="applicant_location">Населено място</label><br/>
		</span> <v:textinput class="brd w500" maxlength="30" name="applicant_location" id="applicant_location" value="${home_city }"/></p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="applicant_location_post_code">Пощенски код</label><br/>
		</span> <v:textinput class="brd w300" maxlength="12" name="applicant_location_post_code" id="applicant_location_post_code"
                             value="${home_post_code }"/></p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="applicant_address">Адрес</label><br/>
		</span> <textarea class="brd w600" rows="3" cols="40" name="applicant_address" id="applicant_address">${home_address_details }</textarea></p>
            <p><span class="fieldlabel2"><label for="homePhone">Телефон</label><br/></span>
                <v:textinput class="brd w500" name="homePhone" value="${homePhone }" id="homePhone"/>
            </p>
            <div class="clr"><!--  --></div>
            <p id="pCommunicateByEmail" class="checkbox"><input id="communicate_by_email" name="communicate_by_email" value="1" type="checkbox" ${official_email_communication } /><label
                    for="communicate_by_email">Съгласен съм да получавам информация и уведомления на посочения електронен адрес</label></p>
            <div class="clr"><!-- --></div>
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
                            <span class="fieldlabel2"><label for="recipient_name">Име</label></span>
                            <v:textinput class="brd w500" maxlength="255" name="recipient_name" id="recipient_name" value="${applicationWebModel.documentRecipientWebModel.name }"/>
                        </p>
                        <p>
                            <span class="fieldlabel2"><label for="recipient_address">Улица №</label></span>
                            <textarea class="brd w500" rows="3" cols="40" name="recipient_address" id="recipient_address">${applicationWebModel.documentRecipientWebModel.address }</textarea>
                        </p>
                        <p>
                            <span class="fieldlabel2"><label for="recipient_district">Област</label></span>
                            <v:textinput class="brd w500" maxlength="100" name="recipient_district" id="recipient_district" value="${applicationWebModel.documentRecipientWebModel.district }"/>
                        </p>
                        <p>
                            <span class="fieldlabel2"><label for="recipient_post_code">Пощенски код</label></span>
                            <v:textinput class="brd w500" maxlength="255" name="recipient_post_code" id="recipient_post_code" value="${applicationWebModel.documentRecipientWebModel.postCode }"/>
                        </p>
                        <p>
                            <span class="fieldlabel2"><label for="recipient_city">Град</label></span>
                            <v:textinput class="brd w500" maxlength="100" name="recipient_city" id="recipient_city" value="${applicationWebModel.documentRecipientWebModel.city }"/>
                        </p>
                        <p>
                            <span class="fieldlabel2"><label for="recipient_country">Държава</label></span>
                            <nacid:combobox name="recipient_country" id="recipient_country" attributeName="recipientCountryCombo" style="w308 brd"/>
                        </p>
                        <p>
                            <span class="fieldlabel2"><label for="recipient_mobile_phone">Мобилен телефон</label></span>
                            <v:textinput class="brd w500" maxlength="50" name="recipient_mobile_phone" id="recipient_mobile_phone" value="${applicationWebModel.documentRecipientWebModel.mobilePhone }"/>
                        </p>
                    </fieldset>
                </div>


            </fieldset>
		<div class="clr20"><!--  --></div>
		<div id="bulgaria_contact">
		<fieldset><legend>За контакт в България</legend>
		<p><span class="fieldlabel2"><label for="bulgaria_contact_location">Населено място</label><br />
		</span> <v:textinput class="brd w500" maxlength="30" name="bulgaria_contact_location"  value="${bg_city }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="bulgaria_contact_location_post_code">Пощенски код</label><br />
		</span> <v:textinput class="brd w300" maxlength="12" name="bulgaria_contact_location_post_code" id="bulgaria_contact_location_post_code"
			value="${bg_post_code }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="bulgaria_contact_address">Адрес</label><br />
		</span> <textarea class="brd w600" rows="3" cols="40" name="bulgaria_contact_address">${bg_address_details }</textarea></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="bgPhone">Телефон</label><br /></span> 
				<v:textinput class="brd w500" name="bgPhone"  value="${bgPhone }" />
		</p>
		<div class="clr"><!--  --></div>
		</fieldset>
		</div>
		<div class="clr20"><!--  --></div>
		<p class="checkbox"><input ${representative_selected } name="representative_data" id="representative_data" value="1"
			onclick="showHideRepresentativeData();" type="checkbox" /><label for="representative_data"><b>Заявителят и подателят са различни лица</b></label></p>


		<div id="representative_data_div" style=""><nacid:applicationPersonEdit person="representative">
			<fieldset><legend>Лични данни на подателя</legend> 
			<input type="hidden" name="representative_record_id" id="representative_record_id"
				value="${person_record_id }" />
			
			
			<p><span class="fieldlabel"><label for="representative_personal_id_type">Тип персонален идентификатор</label></span>
				<nacid:applicationCivilIdRadio>
					<input ${checked } id="representative_rad_${value_id }" name="representative_personal_id_type" value="${radio_id }" onclick="emptyPersonalId('representative');"
						type="radio" />
					<label for="representative_rad_${value_id }">${label }</label>
				</nacid:applicationCivilIdRadio>
			</p>
			<div class="clr"><!--  --></div>

			<p><span class="fieldlabel"><label for="representative_personal_id">${messages.civilIdType}</label></span>
			<v:textinput class="brd w300" maxlength="255" name="representative_personal_id" id="representative_personal_id"  value="${person_civil_id }"
					onkeydown="if(!isSpecialKeyPressed(event)) personalIdChanged('representative');" />
			</p>
			<div class="clr"><!--  --></div>
			<script type="text/javascript">
            	new Autocomplete('representative_personal_id', { 
            	  		serviceUrl:'/nacid/control/civilidsuggestion',
            	  		// callback function:
                		onSelect: updateRepresentativeData,
                		width:500,
               		 	dynamicParameters: function (){
            	   			return {civilidtype : getRadioChecked(document.appform1.representative_personal_id_type)};
            	  		}
           		});
          	</script> 
          	<v:egnLnchValidator input="representative_personal_id" required="$('representative_data').checked"/>
			<p><span class="fieldlabel"><label for="representative_first_name">Име</label><br /></span> 
				<v:textinput class="brd w500" maxlength="100" name="representative_first_name" id="representative_first_name"  value="${person_first_name }" />
			</p>
			<div class="clr"><!--  --></div>
				
			<p><span class="fieldlabel"><label for="representative_middle_name">Презиме</label><br /></span> 
				<v:textinput class="brd w500" maxlength="100" name="representative_middle_name" id="representative_middle_name" 
					value="${person_middle_name }" />
			</p>
			<div class="clr"><!--  --></div>
				
			<p><span class="fieldlabel"><label for="representative_last_name">Фамилия</label><br /></span> 
				<v:textinput class="brd w500" maxlength="100" name="representative_last_name" id="representative_last_name"  value="${person_last_name }" />
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel"><label for="reprPhone">Телефон</label><br /></span> 
				<v:textinput class="brd w500" name="reprPhone"  value="${reprPhone }" />
			</p>
			<div class="clr"><!--  --></div>
			
			<div id="repr_person_details" >
				<p><span class="fieldlabel"><label for="reprCountryId">Държава</label><br /></span> 
					<nacid:combobox name="reprCountryId" id="reprCountryId" attributeName="reprCountryCombo" style="brd w308" />
				</p>
				<div class="clr"><!--  --></div>
				
				<p><span class="fieldlabel"><label for="reprCity">Град</label><br /></span> 
					<v:textinput class="brd w300" name="reprCity"  value="${reprCity }" />
				</p>
				<div class="clr"><!--  --></div>
		
				<p><span class="fieldlabel"><label for="reprPcode">Пощенски код</label><br /></span> 
					<v:textinput class="brd w100" name="reprPcode"  value="${reprPcode }" maxlength="12"/>
				</p>
				<div class="clr"><!--  --></div>
		
				<p><span class="fieldlabel"><label for="reprAddressDetails">Адрес</label><br /></span> 
					<textarea class="brd w600" rows="3" cols="40"
						name="reprAddressDetails" id="reprAddressDetails">${reprAddressDetails }</textarea>
				</p>
				<div class="clr"><!--  --></div>
		
			</div>
			<p class="checkbox">
				<input id="represent_is_company" name="represent_is_company" 
					value="1" type="checkbox" ${represent_is_company } onclick="refreshRepresentativeAdressAndCompany();" />
				<label for="represent_is_company">Подателят е представител на фирма</label>
			</p>
			<div id="repr_company_details">
				<p><span class="fieldlabel"><label for="company">Фирма</label><br /></span> 
					<nacid:combobox name="company" id="company"
						attributeName="companiesComboBox" style="w308 brd" />
				</p>
				<div class="clr"><!--  --></div>
			</div>
			<script type="text/javascript">
				function refreshRepresentativeAdressAndCompany() {
					if($('represent_is_company').checked) {
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
			
			<p class="checkbox">
				<input id="bg_address_owner" name="bg_address_owner" value="1" type="checkbox" ${bg_address_owner_selected } />
				<label for="bg_address_owner">Адресът за контакт в България е на пълномощника</label>
			</p>
			
			<div class="clr"><!--  --></div>
            <p class="checkbox">
                <input id="representative_authorized" name="representative_authorized" value="1" type="checkbox" ${representative_authorized } />
                <label for="representative_authorized">Декларирам, че заявлението за признаване на придобито висше образование в чуждестранно висше училище и документите към него са ми предоставени от заявителя</label>
            </p>
			</fieldset>
		</nacid:applicationPersonEdit></div>
		
		<div class="clr20"><!--  --></div>
        <p class="checkbox"><input id="personal_data_usage" name="personal_data_usage" value="1" type="checkbox" ${personal_data_usage } /><label
            for="personal_data_usage">Съгласен съм да се ползват лични данни за целите на проверката</label></p>
		<div class="clr"><!--  --></div>
        <p class="checkbox"><input id="data_authentic" name="data_authentic" value="1" type="checkbox" ${data_authentic } /><label
            for="data_authentic">${isDoctorateApplicationType ? messages.dataAuthenticDeclarationDoctorate : messages.dataAuthenticDeclaration}</label></p>
        <p><span class="fieldlabel"><label for="applicant_citizenship">Заявлението е въведено от:</label><br /></span>
            <nacid:combobox name="userCreated" id="userCreated" attributeName="userCreated" style="brd w308" disabled="disabled"/></p>
		<p><span class="fieldlabel"><label for="applicant_citizenship">Отговорник</label><br /></span>
		<input type="hidden" name="responsible_user" id="hidden_responsible_user" value="${responsibleUsers.selectedKey }" disabled="disabled"/>
		<nacid:combobox name="responsible_user" id="responsible_user" attributeName="responsibleUsers" style="brd w308" /></p>
		<fieldset><legend>Бележки</legend>
		      <p><span class="fieldlabel2"><label for="notes">Бележки към зявлението</label></span>
              <textarea class="brd w600" rows="3" cols="40" name="notes" id="notes">${notes }</textarea></p>
		</fieldset>
		<input name="activeForm" value="1" type="hidden" />
	</v:form>
</nacid:applicationEdit>

<script type="text/javascript">
  toggleBulgariaContact($('applicant_location_country'));
  if ($('applicant_record_id').value != '') disablePersonInputs('applicant');
  if ($('representative_record_id').value != '') disablePersonInputs('representative');
  if ($('owner_record_id') != null && $('owner_record_id').value != '') disablePersonInputs('owner');
  if ($('applicant_company_id').value != '') disableCompanyFields('owner');
  showHideDiplomaNames();
  showHideRepresentativeData();
  //Inicializirane na ignore-a za ЛНЧ/друго validation
  initPersonalIdIgnoreValidation('applicant');
  initPersonalIdIgnoreValidation('representative');
  if ($('owner_record_id') != null) {
      initPersonalIdIgnoreValidation('owner');
  }


  function cummunicateByEmailRefresh() {
	  if($('applicant_email').value != null && $('applicant_email').value != '') {
		  $('pCommunicateByEmail').show();
	  }
	  else {
		  $('pCommunicateByEmail').hide();
	  }
	  
  }

  cummunicateByEmailRefresh();

  if ($('responsible_user').selectedIndex != 0) {
	  $('responsible_user').disable();
	  $('hidden_responsible_user').enable();
  }

  function applicantTypeChanged() {
      $("applicant_div").hide();
      $("applicant_legal_person").hide();
      var applicantType = getApplicantTypeValue();
      if (applicantType == 0) {
          $("applicant_div").show();
      } else if (applicantType == 2 || applicantType == 5) {
          $("applicant_legal_person").show();
      }
  }
  function getApplicantTypeValue() {
      var res = $$('input:checked[type=radio][name=applicant_type]')[0].value;
      return res;
  }
  applicantTypeChanged();

  new Autocomplete('applicant_company_name', {
      serviceUrl:'${pathPrefix }/control/companysuggestion?type=name',
      // callback function:
      onSelect: updateLegalPersonData,
      width:500
  });
  new Autocomplete('applicant_company_eik', {
      serviceUrl:'${pathPrefix }/control/companysuggestion?type=eik',
      // callback function:
      onSelect: updateLegalPersonData,
      width:500
  });

  function updateLegalPersonData(value, data) {
      updateCompanyData(data);
  }

  function updateCompanyData(data) {
      var d = data.evalJSON(true);
      $("applicant_company_id").value = d.id;
      $("applicant_company_name").value = d.name;
      $("applicant_company_city").value = d.cityName;
      $("applicant_company_country_id").value = d.countryId;
      $("applicant_company_pcode").value = d.pcode;
      $("applicant_company_address").value = d.addressDetails;
      $("applicant_company_eik").value = d.eik;
      disableCompanyFields();
  }
  function disableCompanyFields() {
      $("applicant_company_city").disable();
      $("applicant_company_country_id").disable();
      $("applicant_company_pcode").disable();
      $("applicant_company_address").disable();
  }
  function enableCompanyFields() {
      $("applicant_company_city").enable();
      $("applicant_company_country_id").enable();
      $("applicant_company_pcode").enable();
      $("applicant_company_address").enable();
  }
  function clearCompanyFields() {
      $("applicant_company_id").value = '';
      $("applicant_company_name").value = '';
      $("applicant_company_city").value = '';
      $("applicant_company_country_id").value = defaultCountryId;
      $("applicant_company_pcode").value = '';
      $("applicant_company_address").value = '';
  }
  
</script>
