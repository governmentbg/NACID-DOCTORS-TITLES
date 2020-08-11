<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } професионално направление</span></h3>
<nacid:professionGroup>
	<validation:form action="${pathPrefix }/control/nom_professiongroup/save" method="post" name="profGroupForm"
		backurl="${pathPrefix }/control/nom_professiongroup/list?getLastTableState=1">
		<input type="hidden" name="id" value="${id }" />
		<fieldset><legend>Данни за професионални направления</legend> <nacid:systemmessage />
		<p><span class="fieldlabel"><label for="name">Наименование</label><br />
		</span> <validation:textinput class="brd w600" maxlength="50" name="name"  value="${name }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="edu_area">Област на образованието</label><br />
		</span> <nacid:combobox name="edu_area_id" style="w400" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="dateFrom">от дата</label><br />
		</span> <validation:dateinput name="dateFrom" id="dateFrom" value="${dateFrom }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="dateTo">до дата</label><br />
		</span> <validation:dateinput name="dateTo" id="dateTo" value="${dateTo }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		</fieldset>
		<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true"/>
		<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
		<validation:textValidator input="name" minLength="1" maxLength="50" required="true"  regex="${validations.nomenclature_name_with_fullstop_digits}" errormessage="${messages.err_nomenclature_name_with_fullstop_digits}"/>
	</validation:form>
</nacid:professionGroup>
<%@ include file="/screens/footer.jsp"%>
