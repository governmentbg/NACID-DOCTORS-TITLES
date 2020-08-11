<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="static com.nacid.web.ApplicationTypeHelper.APPLICATION_TYPE_ID_TO_NAME" %>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<h3 class="title"><span>${operationStringForScreens } правно основание</span></h3>

<validation:form action="${pathPrefix }/control/nom_legalreason/save" method="post" name="legalReasonForm"
	backurl="${pathPrefix }/control/nom_legalreason/list?getLastTableState=1">
	<input type="hidden" name="id" value="${legalReason.id }" />
	<fieldset><legend>Данни за правно основание</legend> <nacid:systemmessage />
	<p><span class="fieldlabel"><label for="name">Наименование</label><br />
	</span> <validation:textinput class="brd w600" maxlength="50" name="name"  value="${legalReason.name }" /></p>
	<div class="clr"><!--  --></div>
	<p>
		<span class="fieldlabel"><label for="app_status_id">Статус</label></span>
		<nacid:combobox name="app_status_id" style="brd" attributeName="appStatusCombo"/>
	</p>
	<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="ordinance_article">Член от Наредбата за държавните изисквания за признаване на придобито висше образование...</label></span>
			<validation:textinput name="ordinance_article" id="ordinance_article" class="brd w600" value="${legalReason.ordinanceArticle}" maxlength="255"/>
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="regulation_article">Член от Правилника за устройството и дейността на НАЦИД</label></span>
			<validation:textinput name="regulation_article" id="regulation_article" class="brd w600" value="${legalReason.regulationArticle}" maxlength="255"/>
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="regulation_text">Текст на члена от правилника</label></span>
			<validation:textarea rows="6" cols="40" name="regulation_text" id="regulation_text" class="brd w600" value="${legalReason.regulationText}"/>
		</p>
		<p>
			<span class="red">Ако текста от члена от правилника е различен за различните ОКС, то той се изписва във вида на [x]текст1[y]текст2, където x и y са ID-тата на съответната ОКС </span>
		</p>
		<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label for="dateFrom">от дата</label><br />
	</span> <validation:dateinput name="dateFrom" id="dateFrom" value="${legalReason.dateFrom }" style="brd w200" /></p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label for="dateTo">до дата</label></span>
	  <validation:dateinput name="dateTo" id="dateTo" value="${legalReason.dateTo }" style="brd w200" /></p>
	<div class="clr"><!--  --></div>
		<div class="flt_lft">
			<div class="flt_lft fieldlabel"><b>Типове заявления</b></div>
			<div class="clr"><!--  --></div>
			<div class="flt_lft">
				<c:forEach var="appType" items="<%=APPLICATION_TYPE_ID_TO_NAME%>">
					<input name="application_type" value="${appType.key}" id="at${appType.key}" type="checkbox" <c:if test="${not empty activeApplicationTypes[appType.key]}">checked="checked"</c:if> /><label for="at${appType.key}">${appType.value}</label><br>
				</c:forEach>
			</div>
			<div class="clr"><!--  --></div>
		</div>
	</fieldset>
	<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
	<validation:textValidator input="name" minLength="1" maxLength="50" required="true" regex="${validations.nomenclature_name_with_fullstop_digits}" errormessage="${messages.err_nomenclature_name_with_fullstop_digits}" />
	<validation:textValidator input="regulation_article" maxLength="255" required="false"  />
	<validation:textValidator input="ordinance_article" maxLength="255" required="false"  />
</validation:form>

<%@ include file="/screens/footer.jsp"%>
