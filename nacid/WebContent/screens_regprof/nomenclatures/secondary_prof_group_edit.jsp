<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } професионална квалификация</span></h3>
<validation:form action="${pathPrefix }/control/nom_secondaryprofessiongroup/save" method="post" name="profGroupForm"
		backurl="${pathPrefix }/control/nom_secondaryprofessiongroup/list?getLastTableState=1">
		<validation:hidden name="id" id="id" value="${secondaryProfessionGroup.id}" />
		<div class="clr10"><!--  --></div>
		<fieldset>
			<div class="clr20"><!--  --></div> 
			<nacid:systemmessage />
			<p><span class="fieldlabel"><label for="code">Код</label><br />
			</span> <validation:textinput class="brd w100" maxlength="100" name="code"  value="${secondaryProfessionGroup.code }" /></p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="name">Наименование</label><br />
			</span> <validation:textinput class="brd w600" maxlength="150" name="name"  value="${secondaryProfessionGroup.name }" /></p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="dateFrom">от дата</label><br />
			</span> <validation:dateinput name="dateFrom" id="dateFrom" value="${secondaryProfessionGroup.dateFrom }" style="brd w200" /></p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="dateTo">до дата</label></span>
			  <validation:dateinput name="dateTo" id="dateTo" value="${secondaryProfessionGroup.dateTo }" style="brd w200" /></p>
			<div class="clr"><!--  --></div>
		</fieldset>
		<validation:textValidator input="code" minLength="1" maxLength="10" required="false" regex="${validations.number}" errormessage="Полето трябва да съдържа само цифри" />
		<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
		<validation:textValidator input="name" minLength="1" maxLength="150" required="true" regex="${validations.nomenclature_name}" errormessage="${messages.err_nomenclature_name}" />
	</validation:form>
<%@ include file="/screens_regprof/footer.jsp"%>
