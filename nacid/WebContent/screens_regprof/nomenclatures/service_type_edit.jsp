<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<v:form
	action="${pathPrefix }/control/nom_servicetype/save" method="post"
	name="serviceTypeForm"
	backurl="${pathPrefix }/control/nom_servicetype/list?getLastTableState=1">
	<input type="hidden" name="id" value="${serviceType.id }" />
	<v:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true"/>
	<v:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
	<v:textValidator input="executionDays" required="true" regex="${validations.number}" errormessage="Must be a number!"/>
	<v:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
	<v:textValidator input="name" minLength="1" maxLength="100" required="true" 
	regex="${validations.nomenclature_name_with_fullstop_digits}" errormessage="${messages.err_nomenclature_name_with_fullstop_digits}" />
	<v:textValidator input="price" required="true"/>
	<div class="clr10"><!--  --></div>
	<fieldset>
		<div class="clr20"><!--  --></div>
		<nacid:systemmessage />
		<p>
			<span class="fieldlabel"><label for="name">Наименование</label><br /></span> 
			<v:textinput class="brd w600" maxlength="100" name="name"  value="${serviceType.name }" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="executionDays">Брой календарни дни</label><br /></span> 
			<v:textinput class="brd w300" maxlength="50" name="executionDays" id="executionDays" value="${serviceType.executionDays }"/>
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="price">Цена на услугата</label><br /></span> 
			<v:textinput class="brd w300" maxlength="50" name="price" id="price" value="${serviceType.servicePrice }"/>
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateFrom">от дата</label><br /></span>
			<v:dateinput name="dateFrom" id="dateFrom" value="${serviceType.formattedDateFrom }" style="brd w200" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateTo">до дата</label></span>
			<v:dateinput name="dateTo" id="dateTo" value="${serviceType.formattedDateTo }" style="brd w200" />
		</p>
		<div class="clr"><!--  --></div>
	</fieldset>
</v:form>
<%@ include file="/screens_regprof/footer.jsp"%>