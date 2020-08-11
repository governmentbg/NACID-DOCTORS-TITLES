<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>

<%@ attribute name="person" required="true" rtexprvalue="true" type="com.ext.nacid.web.model.applications.report.ExtPersonForReportWebModel"%>
<%@ attribute name="backofficePerson" required="true" rtexprvalue="true" type="com.nacid.web.model.applications.PersonWebModel"%>
<%@ attribute name="fieldName" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="backofficePersonsCount" required="true" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="documentData" required="false" rtexprvalue="true" type="com.ext.nacid.regprof.web.model.applications.report.ExtPersonDocumentForReportWebModel"%>
<%@ attribute name="addDocumentData" required="true" rtexprvalue="true" type="java.lang.Boolean"%>
<%@ attribute name="birthCountryAttributeName" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="citizenshipAttributeName" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="civildIdTypeAttributeName" required="true" rtexprvalue="true" type="java.lang.String"%>

<input type="hidden" name="${fieldName}_record_id" id="${fieldName}_record_id" value="${backofficePerson.id}" />
<c:choose>
    <c:when test="${not empty person and backofficePersonsCount == 0}">
        <p class="inputFieldPadding">
            <a href="#" onclick="copy${fieldName}Data(); return false;">
                <img src="/nacid/img/icon_add.png">
            </a>
            <span class="msg">Това лице е ново</span>
        </p>
    </c:when>
    <c:when test="${backofficePersonsCount > 1}">
        <p class="inputFieldPadding">
            <span class="red">Съществува повече от едно лице с този идентификатор</span>
        </p>
    </c:when>
</c:choose>
<div class="clr"><!--  --></div>
<input type="hidden" id="${fieldName}_copied_data" name="${fieldName}_copied_data" value="" />
<p><span class="fieldlabel"><label for="${fieldName}_personal_id_type">Тип персонален идентификатор</label></span>
    <nacid:radiobutton name="${fieldName}_personal_id_type" singleline="true" attributeName="${civildIdTypeAttributeName}" onclick="emptyPersonalId('${fieldName}');"/>
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.civilIdType}</span>)
</p>
<div class="clr"><!--  --></div>

<p><span class="fieldlabel"><label for="${fieldName}_personal_id">${messages.civilIdType}</label></span>
    <v:textinput class="brd w200" maxlength="255" name="${fieldName}_personal_id" id="${fieldName}_personal_id"  value="${backofficePerson.civilId}"
                 onkeydown="if(!isSpecialKeyPressed(event)) personalIdChanged('${fieldName}');" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.civilId}</span>)
</p>
<div class="clr"><!--  --></div>


<p><span class="fieldlabel"><label for="${fieldName}_first_name">Име</label></span>
    <v:textinput class="brd w500" maxlength="100" name="${fieldName}_first_name" id="${fieldName}_first_name"  value="${backofficePerson.firstName}" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.firstName}</span>)
</p>
<div class="clr"><!--  --></div>

<p><span class="fieldlabel"><label for="${fieldName}_middle_name">Презиме</label></span>
    <v:textinput class="brd w500" maxlength="100" name="${fieldName}_middle_name" id="${fieldName}_middle_name"  value="${backofficePerson.surName}" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.surName}</span>)
</p>
<div class="clr"><!--  --></div>

<p><span class="fieldlabel"><label for="${fieldName}_last_name">Фамилия</label></span>
    <v:textinput class="brd w500" maxlength="100" name="${fieldName}_last_name" id="${fieldName}_last_name"  value="${backofficePerson.lastName}" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.lastName}</span>)
</p>
<div class="clr"><!--  --></div>

<p><span class="fieldlabel"><label for="${fieldName}_birth_place_country">${messages.birthCountry}</label></span>
    <nacid:combobox name="${fieldName}_birth_place_country" id="${fieldName}_birth_place_country" attributeName="${birthCountryAttributeName}" style="brd w308" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.birthCountry}</span>)
</p>
<div class="clr"><!--  --></div>

<p><span class="fieldlabel"><label for="${fieldName}_birth_place_location">${messages.birthCity}</label></span>
    <v:textinput class="brd w500" maxlength="30" name="${fieldName}_birth_place_location" id="${fieldName}_birth_place_location"
                 value="${backofficePerson.birthCity}" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.birthCity}</span>)
</p>
<div class="clr"><!--  --></div>

<p><span class="fieldlabel"><label for="${fieldName}_birth_date">${messages.birthDate}</label></span>
    <v:dateinput id="${fieldName}_birth_date" style="brd w200" name="${fieldName}_birth_date" value="${backofficePerson.birthDate}" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.birthDate}</span>)
</p>
<div class="clr"><!--  --></div>

<p><span class="fieldlabel"><label for="${fieldName}_citizenship">гражданин на</label></span>
    <nacid:combobox name="${fieldName}_citizenship" id="${fieldName}_citizenship" attributeName="${citizenshipAttributeName}" style="brd w308" />
    (<span class="${backofficePersonsCount != 1 ? 'red' : ''}">${person.citizenship}</span>)
</p>
<div class="clr"><!--  --></div>
<c:if test="${not empty addDocumentData and addDocumentData eq true}">
    <v:hidden name="documentData" value="1" />
    <fieldset><legend>Данни за документ за самоличност</legend>
        <div class="clr"><!--  --></div>
        <v:hidden id="documentData.id" name="documentData.id"/>
        <p class="checkbox"><input value="1" type="checkbox" id="document_data" name="document_data" onclick="refreshDocumentFillForm();"/>
            <label for="document_data">Въведи нов документ</label></p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel2"><label for="documentData.number">Номер на документ</label></span>
            <v:textinput id="documentData.number" class="brd w500" name="documentData.number" value="" />(${documentData.number})
        </p>

        <div class="clr"><!--  --></div>

        <p><span class="fieldlabel2"><label for="documentData.dateOfIssue">Дата на издаване</label></span>
            <v:dateinput id="documentData.dateOfIssue" class="brd w500" name="documentData.dateOfIssue" value="" /> (${documentData.dateOfIssue })
        </p>
        <div class="clr"><!--  --></div>

        <p><span class="fieldlabel2"><label for="documentData.issuedBy">Издаден от</label></span>
            <v:textinput id="documentData.issuedBy" class="brd w500" name="documentData.issuedBy" path="" /> (${documentData.issuedBy })
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
                $("documentData.id").value = null;
                $("documentData.number").value = "";
                $("documentData.dateOfIssue").value = "";
                $("documentData.issuedBy").value = "";
                $("documentData.number").enable();
                $("documentData.dateOfIssue").enable();
                $("documentData.issuedBy").enable();
            } else {
                $("documentData.id").value = docid;
                $("documentData.number").value = docnumber;
                $("documentData.dateOfIssue").value = docdateOfIssue;
                $("documentData.issuedBy").value = docissuedBy;
                $("documentData.number").disable();
                $("documentData.dateOfIssue").disable();
                $("documentData.issuedBy").disable();
            }
        }
        function saveDocumentValues(){
            docid = $("documentData.id").value;
            docnumber = $("documentData.number").value;
            docdateOfIssue = $("documentData.dateOfIssue").value;
            docissuedBy = $("documentData.issuedBy").value;
        }

    </script>

</c:if>

<script type="text/javascript">
    function update${fieldName}Data(value, data) {
        updateCivilIdData(data, "${fieldName}");
    }
    function copy${fieldName}Data() {
        $('${fieldName}_citizenship').value = '${person.citizenshipId}';
        $('${fieldName}_birth_date').value = '${person.birthDate }';
        $('${fieldName}_birth_place_location').value = '${person.birthCity }';
        $('${fieldName}_birth_place_country').value = '${person.birthCountryId}';
        $('${fieldName}_last_name').value = '${person.lastName }';
        $('${fieldName}_middle_name').value = '${person.surName }';
        $('${fieldName}_first_name').value = '${person.firstName }';
        $('${fieldName}_personal_id').value = '${person.civilId}';
        $('${fieldName}_copied_data').value = '1';
        $$('input[name="${fieldName}_personal_id_type"]').each(function(el) {
            if (el.value == '${person.civilIdTypeId}') {
                el.checked=true;
            }
        });

        if ('${fieldName}' == 'applicant' && $('document_data')) {
            $('documentData.id').value = '';
            $('documentData.number').value='${documentData.number}';
            $('documentData.dateOfIssue').value='${documentData.dateOfIssue}';
            $('documentData.issuedBy').value='${documentData.issuedBy}';
        }
    }
    new Autocomplete('${fieldName}_personal_id', {
        serviceUrl:'${pathPrefix }/control/civilidsuggestion',
        // callback function:
        onSelect: update${fieldName}Data,
        width:500,
        dynamicParameters: function (){
            return {civilidtype : getRadioChecked(document.transferForm.${fieldName}_personal_id_type)};
        }
    });
    initPersonalIdIgnoreValidation('${fieldName}');
    if ($("${fieldName}_record_id").value != '') disablePersonInputs("${fieldName}");
</script> 