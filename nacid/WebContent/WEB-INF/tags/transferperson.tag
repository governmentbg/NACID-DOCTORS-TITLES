<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ tag import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@ tag import="com.nacid.web.ApplicantTypeHelper" %>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="applicant" required="false" rtexprvalue="true" type="com.ext.nacid.web.model.applications.report.ExtPersonForReportWebModel"%>
<%@ attribute name="representative" required="false" rtexprvalue="true" type="com.ext.nacid.web.model.applications.report.ExtPersonForReportWebModel"%>
<%@ attribute name="owner" required="false" rtexprvalue="true" type="com.ext.nacid.web.model.applications.report.ExtPersonForReportWebModel"%>
<%@ attribute name="applicantCompany" required="false" rtexprvalue="true" type="com.ext.nacid.web.model.applications.report.ExtCompanyForReportWebModel"%>
<%@ attribute name="transferUniversities" required="true" rtexprvalue="true" type="java.lang.Boolean"%>




<%@ attribute name="applicantType" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="applicantTypeId" required="true" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="allowApplicantTypeChange" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<%@ attribute name="documentData" required="true" rtexprvalue="true" type="com.ext.nacid.regprof.web.model.applications.report.ExtPersonDocumentForReportWebModel"%>
<%@ attribute name="extAppId" required="true" rtexprvalue="true"%>
<%@ attribute name="addDocumentData" required="false" rtexprvalue="true" type="java.lang.Boolean"%>
<%@ attribute name="showOwnerDetails" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<script type="text/javascript">
    var defaultCountryId = <%= NomenclaturesDataProvider.COUNTRY_ID_BULGARIA %>;
    function initPersonalIdIgnoreValidation(name) {
        $(name + '_personal_idIgnoreEGNValidation').checked = !($(name + '_personal_id_type_rad_0').checked) || $(name + '_record_id').value != '';
        $(name + '_personal_idIgnoreLNCHValidation').checked = !($(name + '_personal_id_type_rad_1').checked) || $(name + '_record_id').value != '';
        $(name + '_personal_idIgnoreEGNValidation').ancestors().first().hide();
        $(name + '_personal_idIgnoreLNCHValidation').ancestors().first().hide();
    }
    function copyCompanyData() {
        $('applicant_company_eik').value = '<c:out value="${applicantCompany.eik}" escapeXml="true"/>';
        $('applicant_company_name').value = '<c:out value="${applicantCompany.name}" escapeXml="true"/>';
        $('applicant_company_country_id').value = '<c:out value="${applicantCompany.countryId}" escapeXml="true"/>';
        $('applicant_company_city').value = '<c:out value="${empty applicantCompany.city.name ? applicantCompany.cityName : applicantCompany.city.name}" escapeXml="true"/>';
        $('applicant_company_pcode').value = '<c:out value="${applicantCompany.postCode}" escapeXml="true"/>';
        $('applicant_company_address').value = '<c:out value="${applicantCompany.addressDetails}" escapeXml="true"/>';
    }
    function updateLegalPersonData(value, data) {
        updateCompanyData(data, "applicant_company");
    }

    function updateCompanyData(data, name) {

        var d = data.evalJSON(true);
        $(name + "_id").value = d.id;
        $(name + "_eik").value = d.eik;
        $(name + "_name").value = d.name;
        $(name + "_city").value = d.cityName;
        $(name + "_country_id").value = d.countryId;
        $(name + "_pcode").value = d.pcode;
        $(name + "_address").value = d.addressDetails;
        disableCompanyFields(name);
    }
    function disableCompanyFields(name) {
        $(name + "_city").disable();
        $(name + "_country_id").disable();
        $(name + "_pcode").disable();
        $(name + "_address").disable();
    }
    function enableCompanyFields(name) {
        $(name + "_city").enable();
        $(name + "_country_id").enable();
        $(name + "_pcode").enable();
        $(name + "_address").enable();
    }
    function clearCompanyFields(name, fieldChanged) {
        var oldValue = $(name + "_id").value;
        if (oldValue != '') {
            $(name + "_id").value = '';
            $(name + "_city").value = '';
            $(name + "_country_id").value = defaultCountryId;
            $(name + "_pcode").value = '';
            $(name + "_address").value = '';
            var fieldChangedName = fieldChanged.getAttribute("name");
            if (fieldChangedName.indexOf("eik") > -1) {
                $(name + "_name").value = '';
            } else if (fieldChangedName.indexOf("name") > -1) {
                $(name + "_eik").value = '';
            }
        }




    }



    function updateCivilIdData(data, name) {
        var d = data.evalJSON(true);
        $(name + '_record_id').value = d.recordid;
        $(name + '_first_name').value = d.fname;
        $(name + '_middle_name').value = d.sname;
        $(name + '_last_name').value = d.lname;
        clearInputError($(name + '_first_name'));
        clearInputError($(name + '_last_name'));
        $(name + '_birth_place_country').value = d.birthcountryid;
        $(name + '_birth_place_location').value = d.birthcity;
        $(name + '_citizenship').value = d.citizenshipid;
        $(name + '_birth_date').value = d.birthdate;
        if (name == "applicant") {
            if ($('documentData.id') != null) {
                $('document_data').checked = false;
                $("documentData.dateOfIssue").value = d.dateofissue;
                $("documentData.id").value = d.documentid;
                $("documentData.number").value = d.number;
                $("documentData.issuedBy").value = d.issuedby;
                clearInputError($("documentData.number"));
                clearInputError($("documentData.dateOfIssue"));
                clearInputError($("documentData.issuedBy"));
                saveDocumentValues();
                refreshDocumentFillForm();
            }

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
        $(name + '_birth_place_country').disable();
        $(name + '_birth_place_location').disable();
        $(name + '_citizenship').disable();
        $(name + '_birth_date').disable();

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
        $(name + '_birth_place_country').value = defaultCountryId;
        $(name + '_birth_place_location').clear();
        $(name + '_birth_place_country').enable();
        $(name + '_birth_place_location').enable();

        $(name + '_citizenship').value = '-';
        $(name + '_birth_date').clear();
        $(name + '_citizenship').enable();
        $(name + '_birth_date').enable();
        if ($('documentData.id') != null) {
            refreshDocumentFillForm();
        }

    }
    function emptyPersonalId(name) {
        $(name + '_personal_id').clear();
        personalIdChanged(name);
    }


    function applicantTypeChanged() {
        $("applicant_physical_person").hide();
        $("applicant_legal_person").hide();
        var applicantType = getApplicantTypeValue();
        if (applicantType == 0) {
            $("applicant_physical_person").show();
        } else if (applicantType == 2 || applicantType == 5) {
            $("applicant_legal_person").show();
        }
    }
    function getApplicantTypeValue() {
        var res = $$('input:checked[type=radio][name=applicant_type]')[0].value;
        return res;
    }
    function additionalInputsValidation() {
        return true;
    }
    function submitFinalizationForm() {
        if(confirm("Сигурни ли сте, че искате да приключите заявлението? След приключване на заявлението, то няма да може да бъде прехвърлено!")){
            $("eapplyingFinalizationForm").submit();
        }
    }
</script>
<v:form name="transferForm" method="POST" action="${pathPrefix }/control/e_applying/save"
		skipsubmitbuttons="true" additionalvalidation="additionalInputsValidation()">
	<v:textValidator input="applicant_first_name" maxLength="15" required="(getApplicantTypeValue() == 0)" regex="${validations.name}" errormessage="${messages.err_name}" />
	<v:textValidator input="applicant_middle_name" maxLength="15" regex="${validations.name}" errormessage="${messages.err_name}"/>
	<v:textValidator input="applicant_last_name" maxLength="30" required="(getApplicantTypeValue() == 0)" regex="${validations.name}" errormessage="${messages.err_name}" />
	<v:textValidator input="applicant_birth_place_location" maxLength="30" required="false" regex="${validations.city}" errormessage="${messages.err_city}" />
   	<v:textValidator input="applicant_personal_id" maxLength="($('applicant_personal_id_type_rad_1').checked ? 10 : 50)" required="(getApplicantTypeValue() == 0)"/>
	<v:dateValidator input="applicant_birth_date" format="d.m.yyyy" required="false" beforeToday="true"  />
    <c:if test="${not empty addDocumentData and addDocumentData eq true}">
   		<v:textValidator input="documentData.number" required="$('document_data').checked && (($('applicantDocuments.dateOfIssue').value != 'дд.мм.гггг' && $('applicantDocuments.dateOfIssue').value != '') || $('applicantDocuments.issuedBy').value != '')"/>
    	<v:textValidator input="documentData.issuedBy" required="$('document_data').checked && (($('applicantDocuments.dateOfIssue').value != 'дд.мм.гггг' && $('applicantDocuments.dateOfIssue').value != '') || $('applicantDocuments.number').value != '')"/>
    	<v:dateValidator input="documentData.dateOfIssue" format="d.m.yyyy" required="$('document_data').checked && ($('applicantDocuments.issuedBy').value != '' || $('applicantDocuments.number').value != '')" beforeToday="true"  />
	</c:if>

    <v:textValidator input="applicant_company_name" maxLength="100" required="(getApplicantTypeValue() == 2 || getApplicantTypeValue() == 5)" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
    <v:textValidator input="applicant_company_eik" maxLength="15" required="false" regex="${validations.number}" errormessage="${messages.err_number}" />
    <v:textValidator input="applicant_company_country_id" maxLength="15" required="false" regex="${validations.number}" errormessage="${messages.err_number}" />



	<fieldset><legend>Лични данни на заявителя</legend>

        <p><span class="fieldlabel"><label>Тип заявител</label></span>
        <c:forEach items="<%=ApplicantTypeHelper.APPLICANT_TYPE_ID_TO_NAME%>" var="item" varStatus="status">
            <input type="radio" id="applicant_type_${status.count}" value="${item.key}" name="applicant_type"
                    <c:if test="${applicantTypeId == item.key}">checked="checked" </c:if>
                    <c:if test="${!allowApplicantTypeChange}">disabled="disabled" </c:if>
            onchange="applicantTypeChanged();">
            <label for="applicant_type_${status.count}">${item.value}</label>
        </c:forEach>
        (${applicantType})
        </p>
        <c:if test="${!allowApplicantTypeChange}">
            <input type="hidden" name="applicant_type" value="${applicantTypeId}"/>
        </c:if>
        <input type="hidden" name="entryNumSeries" id="entryNumSeries" value="${param.entryNumSeries}" />
        <input type="hidden" name="id" value="${extApplId }" />
        <div id="applicant_physical_person">
            <v:egnLnchValidator input="applicant_personal_id" required="(getApplicantTypeValue() == 0 && $('applicant_record_id').value =='' && $('applicant_personal_id_type_rad_1').checked)"/>
            <nacid:transferphysicalperson backofficePerson="${backofficeApplicant}" person="${applicant}" fieldName="applicant" backofficePersonsCount="${backofficeApplicantCount}" addDocumentData="${addDocumentData}" documentData="${documentData}"
                                          civildIdTypeAttributeName="civilIdTypeApplicant" citizenshipAttributeName="citizenshipApplicant" birthCountryAttributeName="birthCountryApplicant"/>
        </div>
        <div id="applicant_legal_person">
            <c:choose>
                <c:when test="${backofficeCompanyCount == 0}">
                    <p class="inputFieldPadding">
                        <a href="#" onclick="copyCompanyData(); return false;">
                            <img src="/nacid/img/icon_add.png">
                        </a>
                        <span class="msg">Това ЮЛ е ново</span>
                    </p>
                </c:when>
                <c:when test="${backofficeCompanyCount > 1}">
                    <p class="inputFieldPadding">
                        <span class="red">Съществува повече от едно ЮЛ с този ЕИК</span>
                    </p>
                </c:when>
            </c:choose>
            <input type="hidden" name="applicant_company_id" id="applicant_company_id" value="${backofficeApplicantCompany.id}"/>
            <p><span class="fieldlabel"><label for="applicant_company_eik">Булстат/ЕИК</label></span>
                <v:textinput class="brd w200"  id="applicant_company_eik" name="applicant_company_eik" maxlength="255" value="${backofficeApplicantCompany.eik}" autocomplete="off" onchange="clearCompanyFields('applicant_company', this);enableCompanyFields('applicant_company');"/>
                (<span class="${backofficeCompanyCount != 1 ? 'red' : ''}">${applicantCompany.eik}</span>)
            </p>
            <p><span class="fieldlabel"><label for="applicant_company_name">Наименование</label></span>
                <v:textinput class="brd w200"  id="applicant_company_name" name="applicant_company_name" maxlength="255" value="${backofficeApplicantCompany.name}" autocomplete="off" onchange="clearCompanyFields('applicant_company', this);enableCompanyFields('applicant_company');" />
                (<span class="${backofficeCompanyCount != 1 ? 'red' : ''}">${applicantCompany.name}</span>)
            </p>
            <p><span class="fieldlabel"><label for="applicant_company_country_id">Държава</label></span>
                <nacid:combobox name="applicant_company_country_id" id="applicant_company_country_id" attributeName="applicantCompanyCountry" style="brd w308" />
                (<span class="${backofficeCompanyCount != 1 ? 'red' : ''}">${applicantCompany.countryName}</span>)
            </p>
            <p><span class="fieldlabel"><label for="applicant_company_city">Град</label></span>
                <v:textinput class="brd w200"  id="applicant_company_city" name="applicant_company_city" maxlength="255" value="${backofficeApplicantCompany.city}" autocomplete="off" />
                (<span class="${backofficeCompanyCount != 1 ? 'red' : ''}">${empty applicantCompany.city.name ? applicantCompany.cityName : applicantCompany.city.name}</span>)
            </p>
            <p><span class="fieldlabel"><label for="applicant_company_pcode">Пощенски код</label></span>
                <v:textinput class="brd w200"  id="applicant_company_pcode" name="applicant_company_pcode" maxlength="255" value="${backofficeApplicantCompany.pcode}" autocomplete="off" />
                (<span class="${backofficeCompanyCount != 1 ? 'red' : ''}">${applicantCompany.postCode}</span>)
            </p>
            <p><span class="fieldlabel"><label for="applicant_company_address">Адрес</label></span>
                <v:textinput class="brd w200"  id="applicant_company_address" name="applicant_company_address" maxlength="255" value="${backofficeApplicantCompany.addressDetails}" autocomplete="off" />
                (<span class="${backofficeCompanyCount != 1 ? 'red' : ''}">${applicantCompany.addressDetails}</span>)
            </p>
        </div>
	</fieldset>
    <c:if test="${not empty representative}">

        <v:textValidator input="representative_first_name" maxLength="15" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
        <v:textValidator input="representative_middle_name" maxLength="15" regex="${validations.name}" errormessage="${messages.err_name}"/>
        <v:textValidator input="representative_last_name" maxLength="30" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
        <fieldset><legend>Лични данни на представителя</legend>
            <v:egnLnchValidator input="representative_personal_id" required="true"/>
            <nacid:transferphysicalperson backofficePerson="${backofficeRepresentative}" person="${representative}" fieldName="representative" backofficePersonsCount="${backofficeRepresentativeCount}" addDocumentData="false"
                                          civildIdTypeAttributeName="civilIdTypeRepresentative" citizenshipAttributeName="citizenshipRepresentative" birthCountryAttributeName="birthCountryRepresentative"/>
        </fieldset>
    </c:if>
    <c:if test="${showOwnerDetails  && not empty owner}">


        <v:textValidator input="owner_first_name" maxLength="50" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
        <v:textValidator input="owner_middle_name" maxLength="50" regex="${validations.name}" errormessage="${messages.err_name}"/>
        <v:textValidator input="owner_last_name" maxLength="50" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />

        <fieldset><legend>Лични данни на собственика на дипломата</legend>
            <v:egnLnchValidator input="owner_personal_id" required="true"/>
            <nacid:transferphysicalperson backofficePerson="${backofficeOwner}" person="${owner}" fieldName="owner" backofficePersonsCount="${backofficeOwnerCount}" addDocumentData="false"
                                          civildIdTypeAttributeName="civilIdTypeOwner" citizenshipAttributeName="citizenshipOwner" birthCountryAttributeName="birthCountryOwner"/>
        </fieldset>
    </c:if>
    <c:if test="${transferUniversities}">
        <script>

            function updateUniversity(value, data, el) {
                var id = el.id;
                var itemId = id.replace("university", "").replace("_original_name", "").replace("_name_bg", "");
                var d = data.evalJSON(true);
                $('university' + itemId + '_id').value = d.id;
                $('university' + itemId + '_country_id').value = d.countryId;
                $('university' + itemId + '_city').value = d.city;
                $('university' + itemId + '_address').value = d.address;
                $('university' + itemId + '_name_bg').value = d.bgName;
                $('university' + itemId + '_original_name').value = d.orgName;

                $('university' + itemId + '_country_id').setAttribute('disabled', true);
                $('university' + itemId + '_city').setAttribute('disabled', true);
                $('university' + itemId + '_address').setAttribute('disabled', true);
            }

            function universityChanged(lang, itemId) {
                if ($('university' + itemId + '_id').value == '' || $('university' + itemId + '_id').value == '0') {
                    return;
                }

                $('university' + itemId + '_country_id').removeAttribute('disabled');
                $('university' + itemId + '_city').removeAttribute('disabled');
                $('university' + itemId + '_address').removeAttribute('disabled');


                $('university' + itemId + '_id').value = '';
                $('university' + itemId + '_country_id').value = '-';
                $('university' + itemId + '_city').value = '';
                $('university' + itemId + '_address').value = '';
                if (lang == 'bg') {
                    $('university' + itemId + '_original_name').value = '';
                } else {
                    $('university' + itemId + '_name_bg').value = '';
                }


            }
            var universities = [];
            function copyUniversityData(cnt) {
                $('university' + cnt + '_id').value = 'x'; //za da moje da mine validaciqta v universityChanged()
                universityChanged('bg', cnt);
                $('university' + cnt + '_name_bg').value = universities[cnt]['bg_name'];
                $('university' + cnt + '_city').value = universities[cnt]['city'];
                $('university' + cnt + '_country_id').value = universities[cnt]['country_id'];

            }

        </script>
        <input type="hidden" name="universities_count" value="${fn:length(universities)}"/>
        <c:forEach items="${universities}" var="item" varStatus="status">
            <fieldset><legend>Данни за чуждестранното висше училище </legend>
                <c:if test="${not item.standardUniversity}">
                    <p class="inputFieldPadding">
                        Име: ${item.universityTxt}<br  />
                        <a onclick="copyUniversityData(${status.count}); return false;" href="#">
                            <img src="/nacid/img/icon_add.png">
                        </a>
                        <span class="msg">Този университет е нов</span>

                    </p>
                </c:if>
                <div class="clr"><!--  --></div>
                <v:textValidator input="university${status.count}_name_bg" required="true" />
                <v:comboBoxValidator input="university${status.count}_country_id" required="true" />


                <input  type="hidden" name="university${status.count}_id" id="university${status.count}_id" value="${item.id}"/>
                <p>
                    <span class="fieldlabel"><label for="university${status.count}_original_name">Оригинално наименование</label></span>
                    <v:textinput class="brd w400 flt_lft" name="university${status.count}_original_name" id="university${status.count}_original_name"  value="${item.orgName }" onkeydown="if (!isSpecialKeyPressed(event)) universityChanged('en', ${status.count});" />
                    <span class="flt_lft w200 pl10">(<span class="${item.standardUniversity ? '' : 'red'}">${item.orgName}</span>)</span>
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="university${status.count}_name_bg">Наименование на български</label></span>
                    <v:textinput class="brd w400 flt_lft" name="university${status.count}_name_bg" id="university${status.count}_name_bg"  value="${item.standardUniversity ? item.bgName : ''}" onkeydown="if (!isSpecialKeyPressed(event)) universityChanged('bg', ${status.count});"/>
                    <span class="flt_lft w200 pl10">(<span class="${item.standardUniversity ? '' : 'red'}">${item.bgName}</span>)</span>
                </p>
                <div class="clr"><!--  --></div>

                <p>
                    <span class="fieldlabel"><label for="university${status.count}_country_id">Държава</label></span>

                    <nacid:combobox disabled="disabled" name="university${status.count}_country_id" id="university${status.count}_country_id" attributeName="universityCountry" style="w308 brd flt_lft" selectedValue="${item.standardUniversity ? item.countryId : ''}"/>
                    <span class="flt_lft w200 pl10">(<span class="${item.standardUniversity ? '' : 'red'}">${item.country}</span>)</span>
                </p>
                <div class="clr"><!--  --></div>
                <p><span class="fieldlabel"><label for="university${status.count}_city">Град</label></span>
                    <v:textinput disabled="disabled" class="brd w400 flt_lft" maxlength="100" name="university${status.count}_city" id="university${status.count}_city"  value="${item.standardUniversity ? item.city : ''}" />
                    <span class="flt_lft w200 pl10">(<span class="${item.standardUniversity ? '' : 'red'}">${item.city}</span>)</span>
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="university${status.count}_address">Адрес за кореспонденция</label></span>
                    <textarea disabled="disabled" class="brd w400" rows="3" cols="40" name="university${status.count}_address" id="university${status.count}_address">${item.addrDetails }</textarea>
                </p>
                <div class="clr"><!--  --></div>
            </fieldset>
            <div class="clr20"><!--  --></div>
            <script type="text/javascript">
                new Autocomplete('university${status.count}_name_bg', {
                    serviceUrl:'${pathPrefix}/control/universitysuggest?nametype=1',
                    width:600,
                    onSelect:updateUniversity
                });
                new Autocomplete('university${status.count}_original_name', {
                    serviceUrl:'${pathPrefix}/control/universitysuggest?nametype=2',
                    width:600,
                    onSelect:updateUniversity
                });
                universities[${status.count}] = [];
                universities[${status.count}]['bg_name'] = '<c:out value="${item.bgName}" escapeXml="true"></c:out>';
                universities[${status.count}]['city'] = '<c:out value="${item.city}" escapeXml="true"></c:out>';
                universities[${status.count}]['country_id'] = '<c:out value="${item.countryId}" escapeXml="true"></c:out>';
                if ($('university${status.count}_id').value == '0') {
                    universityChanged('bg', ${status.count});
                }
            </script>

        </c:forEach>


    </c:if>

    <c:if test="${not empty specialities}">
    <fieldset><legend>Специалност</legend>
        <script>
            var specialities = [];
            function copySpecialityData(cnt) {
                showHideSpecialityDivs('newSpeciality' + cnt);
                $('newSpeciality' + cnt).value = specialities[cnt]['name'];
            }
            additionalInputsValidation = function() {
                var ret = true;
                for (var i = 1; i <= $('specialities_count').value; i++) {
                    if ($('newSpeciality' + i + 'Id').value == '') {
                        setErrorOnInput($('newSpeciality' + i), "Трябва да въведете специалност!");
                    }
                }
                return ret;
            }
        </script>
        <input type="hidden" name="specialities_count" id="specialities_count" value="${fn:length(specialities)}"/>
        <c:forEach items="${specialities}" var="item" varStatus="status">
            <c:if test="${empty item.specialityId}">
                <p class="inputFieldPadding">
                    <a onclick="copySpecialityData(${status.count}); return false;" href="#">
                        <img src="/nacid/img/icon_add.png">
                    </a>
                    <span class="msg">Тази специалност е нова</span>
                </p>
            </c:if>
            <input type="hidden" name="newSpeciality${status.count}Id" id="newSpeciality${status.count}Id" value="${item.specialityId}" />
            <v:textValidator input="newSpeciality${status.count}Id" required="true" />
            <v:specialityinput id="newSpeciality${status.count}" value="${item.specialityName}" comboAttribute="professionGroupCombo" class="brd w500"
                               chooseMultiple="false" specialityIdInput="$('newSpeciality${status.count}Id')"
                               specialityOnkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('newSpeciality${status.count}');"
                               addOriginalSpeciality="false" />
            <span class="fieldlabel">&nbsp;</span>
            <span  style="font-size:80%">(<span class="${empty item.specialityId ? 'red' : ''}">${item.specialityTxt}${item.specialityName}</span>)</span>
            <script type="text/javascript">
                new Autocomplete('newSpeciality${status.count}', {
                    serviceUrl:'${pathPrefix}/control/specialitysuggest?only_active=1',
                    width:600,
                    onSelect:updateNomenclature
                });
                specialities[${status.count}] = [];
                specialities[${status.count}]['name'] = '${item.specialityTxt}';
            </script>
        </c:forEach>
    </fieldset>
    </c:if>

    <fieldset><legend>Данни за дипломата</legend>
        <p>
            <span class="fieldlabel"><label for="diploma_series">Серия:</label></span>
            <v:textinput class="brd w308" maxlength="15" name="diploma_series" id="diploma_series"  value="${diploma_series }" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="diploma_number">Номер:</label></span>
            <v:textinput class="brd w308" maxlength="15" name="diploma_number" id="diploma_number"  value="${diploma_number }" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="diploma_registration_number">Регистрационен номер:</label></span>
            <v:textinput class="brd w308" maxlength="15" name="diploma_registration_number" id="diploma_registration_number"  value="${diploma_registration_number }" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="diploma_date">Дата:</label><br /></span>
            <v:dateinput name="diploma_date" id="diploma_date" value="${diploma_date }" style="brd w308" />
        </p>
    </fieldset>

	<div class="clr10"><!--  --></div>
	<p class="cc">
		<input class="w124 save" type="submit" value="Прехвърли" id="submitButton" />
        <input style="color:red" class="w124 save flt_rgt" type="button" value="Приключи" id="finishApplicationButton" onclick="submitFinalizationForm()"/>
	</p>
</v:form>
<form method="POST" action="${pathPrefix }/control/e_applying_finalization/save" id="eapplyingFinalizationForm">
    <input type="hidden" name="id" value="${extApplId }" />
</form>
<script type="text/javascript">
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

    if ($("applicant_company_id").value != '') disableCompanyFields("applicant_company");
</script> 