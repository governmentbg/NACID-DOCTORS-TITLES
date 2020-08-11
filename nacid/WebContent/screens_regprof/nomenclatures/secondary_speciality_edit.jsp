<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h3 class="title">
	<span>${operationStringForScreens } Специалност - обучение</span>
</h3>
<validation:form
	action="${pathPrefix }/control/nom_secondary_speciality/save" method="post"
	name="secondarySpecialityForm"
	backurl="${pathPrefix }/control/nom_secondary_speciality/list?getLastTableState=1">
	<input type="hidden" name="id" value="${secondarySpeciality.id }" />
	<div class="clr10"><!--  --></div>
	<fieldset>
		<div class="clr20"><!--  --></div>
		
		<nacid:systemmessage />
		<p>
			<span class="fieldlabel"><label for="code">Код</label><br /></span> 
			<validation:textinput class="brd w100" maxlength="10" name="code"  value="${secondarySpeciality.code }" />
		</p>
		<p>
			<span class="fieldlabel"><label for="name">Наименование</label><br /></span> 
			<validation:textinput class="brd w600" maxlength="150" name="name"  value="${secondarySpeciality.name }" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="prof_qualification_id">Професионална квалификация</label><br /></span>
			<nacid:combobox name="prof_qualification_id" attributeName="prof_qualification_id" style="brd w600" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="qualification_degree_id">Степен на професионална квалификация</label><br /></span>
			<nacid:combobox name="qualification_degree_id" attributeName="qualification_degree_id"  style="brd w240" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateFrom">от дата</label><br /></span>
			<validation:dateinput name="dateFrom" id="dateFrom" value="${secondarySpeciality.dateFrom }" style="brd w200" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateTo">до дата</label></span>
			<validation:dateinput name="dateTo" id="dateTo" value="${secondarySpeciality.dateTo }" style="brd w200" />
		</p>
		<div class="clr"><!--  --></div>
	</fieldset>
	<validation:textValidator input="code" minLength="1" maxLength="10" required="false" regex="${validations.number}" errormessage="Полето трябва да съдържа само цифри" />
	<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
	<validation:textValidator input="name" minLength="1" maxLength="150" required="true" regex="${validations.nomenclature_name}" errormessage="${messages.err_nomenclature_name}" />
	<validation:comboBoxValidator input="prof_qualification_id" required="true"/>
	<validation:comboBoxValidator input="qualification_degree_id" required="true"/>
</validation:form>
<%@ include file="/screens_regprof/footer.jsp"%>
