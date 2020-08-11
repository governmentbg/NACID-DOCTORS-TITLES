<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } език</span></h3>

<validation:form action="${pathPrefix }/control/nom_language/save" method="post" name="languageForm"
	backurl="${pathPrefix }/control/nom_language/list?getLastTableState=1">
	<input type="hidden" name="id" value="${language.id }" />
	<fieldset><legend>Данни за държава</legend> <nacid:systemmessage />
	<p><span class="fieldlabel"><label for="name">Наименование</label><br />
	</span> <validation:textinput class="brd w600" maxlength="50" name="name"  value="${language.name }" /></p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label for="iso639Code">ISO 639 Код</label><br />
	</span> <validation:textinput class="brd w20" maxlength="2" name="iso639Code"  value="${language.iso639Code }" /></p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label for="dateFrom">от дата</label><br />
	</span> <validation:dateinput name="dateFrom" id="dateFrom" value="${language.dateFrom }" style="brd w200" /></p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label for="dateTo">до дата</label></span>
	  <validation:dateinput name="dateTo" id="dateTo" value="${language.dateTo }" style="brd w200" /></p>
	<div class="clr"><!--  --></div>
	</fieldset>
	<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
	<validation:textValidator input="name" minLength="1" maxLength="50" required="true" regex="${validations.nomenclature_name}" errormessage="${messages.err_nomenclature_name}" />
	<validation:textValidator input="iso639Code" minLength="1" maxLength="2" required="true" regex="${validations.nomenclature_iso3166_code}" errormessage="${messages.err_iso3166_code}" />
</validation:form>

<%@ include file="/screens/footer.jsp"%>
