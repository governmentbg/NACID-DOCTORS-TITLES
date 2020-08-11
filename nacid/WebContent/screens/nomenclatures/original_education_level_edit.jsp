<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } оригинална степен на образование</span></h3>

<validation:form action="${pathPrefix }/control/nom_original_education_level/save" method="post" name="originalEduLevel"
    backurl="${pathPrefix }/control/nom_original_education_level/list?getLastTableState=1" >
    <input type="hidden" name="id" value="${originalEduLevel.id }" />
    <fieldset><legend>Данни за оригинална степен на образование</legend> <nacid:systemmessage />
    <p>
        <span class="fieldlabel"><label for="name">Наименование</label></span>
        <validation:textinput class="brd w600" maxlength="50" name="name"  value="${originalEduLevel.name }" />
    </p>
    <div class="clr"><!--  --></div>
    <p>
        <span class="fieldlabel"><label for="name">Превод</label></span>
        <validation:textinput class="brd w600" maxlength="250" name="name_translated"  value="${originalEduLevel.nameTranslated }" />
    </p>
    <div class="clr"><!--  --></div>
    <p>
        <span class="fieldlabel"><label for="country_id">Държава</label></span>
        <nacid:combobox name="country_id" style="brd w600" attributeName="countries" />
    </p>
    <div class="clr"><!--  --></div>
    <p>
        <span class="fieldlabel"><label for="edu_level_id">Степен на образование</label></span>
        <nacid:combobox name="edu_level_id" style="brd w600" attributeName="eduLevels" />
    </p>

    <div class="clr"><!--  --></div>
    <p>
        <span class="fieldlabel"><label for="dateFrom">от дата</label></span>
        <validation:dateinput name="dateFrom" id="dateFrom" value="${originalEduLevel.dateFrom }" style="brd w200" />
    </p>
    <div class="clr"><!--  --></div>
    <p>
        <span class="fieldlabel"><label for="dateTo">до дата</label></span>
        <validation:dateinput name="dateTo" id="dateTo" value="${originalEduLevel.dateTo }" style="brd w200" />
    </p>
    <div class="clr"><!--  --></div>
    </fieldset>
    <validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
    <validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
    <validation:textValidator input="name" minLength="1" maxLength="50" required="true" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
    <validation:textValidator input="name_translated" minLength="0" maxLength="250" required="false" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
    <validation:comboBoxValidator input="country_id" required="true" errormessage="${messages.must_choose_country}" />
    <validation:comboBoxValidator input="edu_level_id" required="true"  errormessage="${messages.must_choose_edu_level}"/>
</validation:form>

<%@ include file="/screens/footer.jsp"%>
