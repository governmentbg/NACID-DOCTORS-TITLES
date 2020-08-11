<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>

<h3 class="title"><span>${operationStringForScreens } компетентна институция</span></h3>
<nacid:competentInstitutionEdit>
	<v:form action="${pathPrefix }/control/competent_institution/save"
		method="post" name="competent_institution"
		backurl="${pathPrefix }/control/competent_institution/list?getLastTableState=1">

		<nacid:systemmessage />

		<v:textValidator input="name" maxLength="255" required="true" />
		<v:textValidator input="original_name" maxLength="255" regex="${validations.original_name}" errormessage="${messages.err_original_name}" />
		<v:textValidator input="phone" maxLength="255" />
		<v:textValidator input="fax" maxLength="255" />
		<v:textValidator input="email" maxLength="255" regex="${validations.email }" />
		<v:textValidator input="url" maxLength="255" />
		<v:dateIntervalValidator format="d.m.yyyy" dateTo="dateTo" dateFrom="dateFrom" />
		<v:dateValidator format="d.m.yyyy" input="dateTo" beforeToday="true"/>
		<v:dateValidator format="d.m.yyyy" input="dateFrom" beforeToday="true" />

		<input id="id" type="hidden" name="id" value="${id }" />
		<fieldset><legend>Основни данни</legend>


		<p><span class="fieldlabel2"><label for="country">Държава</label><br />
		</span> <nacid:combobox id="country" name="country" style="brd"/></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="bgName">Наименование</label><br />
		</span> <v:textinput id="name" class="brd w500" name="name" 
			value="${name }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="original_name">Оригинално наименование</label><br />
		</span> <v:textinput id="original_name" class="brd w500" name="original_name"
			value="${original_name }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="address_details">Адрес</label><br />
		</span> <textarea id="address_details" class="brd w600" rows="3" cols="40"
			name="address_details">${address_details }</textarea></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="phone">Телефон</label><br />
		</span> <v:textinput id="phone" class="brd w500" name="phone" 
			value="${phone }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="fax">Факс</label><br />
		</span> <v:textinput id="fax" class="brd w500" name="fax" 
			value="${fax }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="email">Електронна
		поща</label><br />
		</span> <v:textinput id="email" class="brd w500" name="email" 
			value="${email }" /></p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel2"><label for="url">Интернет страница</label></span>
			<v:textinput id="webSite" class="brd w500" name="url"  value="${url }" />
			${urlLink }
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="dateFrom">От
		дата</label><br />
		</span> <v:dateinput name="dateFrom" style="brd w200" value="${dateFrom }" />
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="dateTo">До
		дата</label><br />
		</span> <v:dateinput name="dateTo" style="brd w200" value="${dateTo }" /></p>
		<div class="clr"><!--  --></div>
		</fieldset>

	</v:form>
</nacid:competentInstitutionEdit>

<%@ include file="/screens/footer.jsp"%>
