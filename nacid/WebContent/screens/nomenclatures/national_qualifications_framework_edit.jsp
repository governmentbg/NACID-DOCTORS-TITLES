<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } национална квалификационна рамка</span></h3>

<validation:form action="${pathPrefix }/control/nom_national_qualifications_framework/save" method="post" name="nqf"
    backurl="${pathPrefix }/control/nom_national_qualifications_framework/list?getLastTableState=1" >
    <input type="hidden" name="id" value="${nqf.id }" />
    <fieldset><legend>Данни за национална квалификационна рамка</legend> <nacid:systemmessage />
    <p>
        <span class="fieldlabel"><label for="name">Наименование</label></span>
        <validation:textinput class="brd w600" maxlength="50" name="name"  value="${nqf.name }" />
    </p>
    <div class="clr"><!--  --></div>
    <p>
        <span class="fieldlabel"><label for="country_id">Държава</label></span>
        <nacid:combobox name="country_id" style="brd w600" attributeName="countries" />
    </p>
    <div class="clr"><!--  --></div>

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
    <validation:comboBoxValidator input="country_id" required="true" errormessage="${messages.must_choose_country}" />
</validation:form>

<%@ include file="/screens/footer.jsp"%>
