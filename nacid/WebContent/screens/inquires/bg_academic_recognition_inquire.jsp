<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires"  prefix="inquires"%>
<%@ include file="/screens/header.jsp"%>
<script type="text/javascript" src="${pathPrefix }/js/inquires.jsp"></script>
<h3 class="title"><span>Справка</span></h3>
<script type="text/javascript">
    var universities = ${universities_count};
</script>
<v:form action="${pathPrefix }/control/bg_academic_recognition_inquire/print"
        method="post" name="InquireForm"
        backurl="${pathPrefix }/control/home" skipsubmitbuttons="true">
    <nacid:hasUserAccess operationname="list">
        <input type="button" value="Филтрирай" name="sbmt" class="save flt_rgt w140" onclick="javascript:if (typeof validateFormInquireForm != 'function' || validateFormInquireForm()) {generateTable($('InquireForm'), '${pathPrefix }/control/bg_academic_recognition_inquire/list?reload=1');}" />
    </nacid:hasUserAccess>
    <input type="button" value="Изчисти" name="sbmt" class="save flt_rgt w110" onclick="document.location.href='${pathPrefix}/control/bg_academic_recognition_inquire/view';" />

    <div class="clr10"><!-- --></div>
    <inquires:inquire_suggestion_subelement title="Собственик на дипломата" chosenTitle="Собственик на дипломата" labelTitle="Собственик на дипломата" serviceUrl="'${pathPrefix}/control/bgacademicrecognitionsuggest?type=applicant'" type="owner" />
    <div class="clr10"><!-- --></div>
    <inquires:inquire_suggestion_subelement title="Гражданство" chosenTitle="Гражданство" labelTitle="Гражданство" serviceUrl="'${pathPrefix}/control/bgacademicrecognitionsuggest?type=citizenship'" type="citizenship" />
    <div class="clr10"><!-- --></div>
    <fieldset><legend>Висше училище по диплома</legend>
        <inquires:inquire_suggestion_subelement title="Висше училище" chosenTitle="Висше училище" labelTitle="Висше училище" serviceUrl="'${pathPrefix}/control/bgacademicrecognitionsuggest?type=university'" type="university" />
        <div class="clr10"><!-- --></div>
        <inquires:inquire_suggestion_subelement title="Държава" chosenTitle="Държава" labelTitle="Държава" serviceUrl="'${pathPrefix}/control/bgacademicrecognitionsuggest?type=university_country'" type="universityCountry" />
        <div class="clr10"><!-- --></div>
    </fieldset>
    <fieldset><legend>Специалност/ОКС по диплома</legend>
        <inquires:inquire_suggestion_subelement title="Специалност" chosenTitle="Специалност" labelTitle="Специалност" serviceUrl="'${pathPrefix}/control/bgacademicrecognitionsuggest?type=diploma_speciality'" type="diplomaSpeciality" />
        <div class="clr10"><!-- --></div>
        <inquires:inquire_suggestion_subelement title="Образователно-квалифиакционна степен" chosenTitle="Образователно-квалифиакционна степен" labelTitle="Образователно-квалифиакционна степен" serviceUrl="'${pathPrefix}/control/bgacademicrecognitionsuggest?type=education_level'" type="diplomaEducationLevel" />
        <div class="clr10"><!-- --></div>
    </fieldset>
    <fieldset><legend>Решение на ВУ</legend>
        <p>
            <span class="fieldlabel2"><label for="protocol_number">Номер на протокол</label></span>
            <span><v:textinput  class="brd w400" name="protocol_number" value="" /></span>
        </p>
        <div class="clr10"><!-- --></div>
        <p>
            <span class="fieldlabel2"><label for="denial_protocol_number">Решение на АС на ВУ (отказ/ професионален бакалавър / бакалавър / магистър)</label></span>
            <span><v:textinput  class="brd w400" name="denial_protocol_number" value="" /></span>
        </p>
        <div class="clr10"><!-- --></div>
        <inquires:inquire_suggestion_subelement title="Призната специалност" chosenTitle="Призната специалност" labelTitle="Призната специалност" serviceUrl="'${pathPrefix}/control/bgacademicrecognitionsuggest?type=recognized_speciality'" type="recognizedSpeciality" />

        <inquires:university_fieldset skipJointDegree="true"/>

        <div class="clr10"><!-- --></div>
        <p>
            <span class="fieldlabel2"><label for="input_number">Входящ номер</label></span>
            <span><v:textinput  class="brd w400" name="input_number" value="" /></span>
        </p>
        <div class="clr10"><!-- --></div>

        <p>
            <span class="fieldlabel2"><label for="output_number">Изходящ номер</label></span>
            <span><v:textinput  class="brd w400" name="output_number" value="" /></span>
        </p>
        <div class="clr10"><!-- --></div>

        <inquires:commission_inquire_subelement chosenTitle="Избрани статуси" labelTitle="Статус" type="recognitionStatus" comboAttribute="recognitionStatusesCombo" elementIds="recognitionStatusIds" title="Статус" selectedElements="selectedRecognitionStatuses" />

    </fieldset>

    <input type="hidden" name="universities_count" id="universities_count" value="${universities_count }" />

    <div class="clr10"><!-- --></div>
    <nacid:hasUserAccess operationname="list">
        <input type="button" value="Филтрирай" name="sbmt" class="save flt_rgt w140" onclick="javascript:if (typeof validateFormInquireForm != 'function' || validateFormInquireForm()) {generateTable($('InquireForm'), '${pathPrefix }/control/bg_academic_recognition_inquire/list?reload=1');}" />
    </nacid:hasUserAccess>
    <input type="button" value="Изчисти" name="sbmt" class="save flt_rgt w110" onclick="document.location.href='${pathPrefix}/control/bg_academic_recognition_inquire/view';" />
</v:form>

<script type="text/javascript">
    showHideDiv('university0');
    showHideDiv('university0Names');
    showHideDiv('org_university0Names');
    resetUniversityCountry(0);
    var origAddUniversity = addUniversity;
    //perdefiniram funckiqta za da moga da sett-na country-to na Bulgaria!
    addUniversity = function() {
        origAddUniversity();
        resetUniversityCountry(universities - 1);

    }
    function resetUniversityCountry(universityCnt) {
        $('university' + universityCnt + 'Country').value = <%=NomenclaturesDataProvider.COUNTRY_ID_BULGARIA%>;
        $('university' + universityCnt + 'Country').setAttribute("disabled", "disabled");
    }
</script>
<div id="table_result">
</div>
<%@ include file="/screens/footer.jsp"%>