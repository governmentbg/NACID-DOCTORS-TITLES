<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } типове документи за стаж</span></h3>
<validation:form 
	action="${pathPrefix }/control/nom_profession_experience_document_type/save" 
	method="post" 
	name="docTypeForm" 
	backurl="${pathPrefix }/control/nom_profession_experience_document_type/list?getLastTableState=1">
	<validation:hidden name="id" value="${documentType.id}" />
	<div class="clr10"><!--  --></div>
		<fieldset>
		<div class="clr20"><!--  --></div>
		<nacid:systemmessage />
		<p>
			<span class="fieldlabel"><label for="name">Наименование</label></span>
			<validation:textinput class="brd w600" maxlength="50" name="name"  value="${documentType.name}" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="is_for_experience_calculation">Влиза за изчисление на стаж</label><br /></span>
			<nacid:combobox name="is_for_experience_calculation" attributeName="isForExperienceCalculationCombo" style="brd"/>
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateFrom">от дата</label></span>
			<validation:dateinput name="dateFrom" id="dateFrom" value="${documentType.dateFrom}" style="brd w200" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateTo">до дата</label></span>
			<validation:dateinput name="dateTo" id="dateTo" value="${documentType.dateTo}" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
	</fieldset>
	<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
	<validation:textValidator input="name" minLength="1" maxLength="50" required="true" regex="${validations.nomenclature_name_with_fullstop_digits}" errormessage="${messages.err_nomenclature_name_with_fullstop_digits}" />
</validation:form>
<%@ include file="/screens_regprof/footer.jsp"%>
