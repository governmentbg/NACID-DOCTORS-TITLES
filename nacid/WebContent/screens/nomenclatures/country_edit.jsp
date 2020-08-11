<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } държава</span></h3>
<nacid:country>
	<validation:form action="${pathPrefix }/control/nom_country/save" method="post" name="countryForm"
		backurl="${pathPrefix }/control/nom_country/list?getLastTableState=1">
		<input type="hidden" name="id" value="${id }" />
		<fieldset><legend>Данни за държава</legend> <nacid:systemmessage />
		<p><span class="fieldlabel"><label for="name">Наименование</label><br />
		</span> <validation:textinput class="brd w600" maxlength="50" name="name"  value="${name }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="iso3166Code">ISO 3166 Код</label><br />
		</span> <validation:textinput class="brd w20" maxlength="2" name="iso3166Code"  value="${iso3166Code }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="officialName">Официално наименование</label><br />
		</span> <validation:textinput class="brd w600" maxlength="50" name="officialName"  value="${officialName }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="dateFrom">от дата</label><br />
		</span> <validation:dateinput name="dateFrom" id="dateFrom" value="${dateFrom }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="dateTo">до дата</label></span>
		  <validation:dateinput name="dateTo" id="dateTo" value="${dateTo }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		</fieldset>
		<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
		<validation:textValidator input="name" minLength="1" maxLength="50" required="true" regex="${validations.nomenclature_name}" errormessage="${messages.err_nomenclature_name}" />
		<validation:textValidator input="iso3166Code" minLength="1" maxLength="2" required="true" regex="${validations.nomenclature_iso3166_code}" errormessage="${messages.err_iso3166_code}" />
		<validation:textValidator input="officialName" minLength="1" maxLength="50" required="false" regex="${validations.nomenclature_name}" errormessage="${messages.err_nomenclature_name}" />
	</validation:form>
</nacid:country>
<%@ include file="/screens/footer.jsp"%>
