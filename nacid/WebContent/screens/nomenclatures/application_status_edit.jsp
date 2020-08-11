<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } статус на заявление</span></h3>
<validation:form action="${pathPrefix }/control/nom_appstatus/save" method="post" name="countryForm"
                 backurl="${pathPrefix }/control/nom_appstatus/list?getLastTableState=1">
    <input type="hidden" name="id" value="${applicationStatus.id }" />
    <fieldset><legend>Данни за държава</legend> <nacid:systemmessage />
        <p><span class="fieldlabel"><label for="name">Наименование</label><br />
		</span> <validation:textinput class="brd w600" maxlength="50" name="name"  value="${applicationStatus.name }" /></p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="dateFrom">от дата</label><br />
		</span> <validation:dateinput name="dateFrom" id="dateFrom" value="${applicationStatus.dateFrom }" style="brd w200" /></p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="dateTo">до дата</label></span>
            <validation:dateinput name="dateTo" id="dateTo" value="${applicationStatus.dateTo }" style="brd w200" /></p>
        <p><span class="fieldlabel"><label for="is_legal">Правен статус</label></span>
            <validation:checkboxinput name="is_legal" id="is_legal" value="1" checked="${applicationStatus.legal }" style="brd w200" /></p>
        <div class="clr"><!--  --></div>
    </fieldset>
    <validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
    <validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
    <validation:textValidator input="name" minLength="1" maxLength="50" required="true" regex="${validations.nomenclature_name}" errormessage="${messages.err_nomenclature_name}" />
</validation:form>
<%@ include file="/screens/footer.jsp"%>
