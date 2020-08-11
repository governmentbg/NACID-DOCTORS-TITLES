<%@ page import="static com.nacid.web.ApplicationTypeHelper.APPLICATION_TYPE_ID_TO_NAME" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<nacid:flatNomenclature>
	
	<h3 class="title"><span>${operationStringForScreens }  ${nomenclatureName }</span></h3>
	<validation:form action="${pathPrefix }/control/${url }/save" method="post" name="flatNomenclatureForm" backurl="${pathPrefix }/control/${url }/list?getLastTableState=1">
		<input type="hidden" name="id" value="${id }" />
		<fieldset><legend>Редактиране на ${nomenclatureName }</legend> 
		  <nacid:systemmessage />
			<p><span class="fieldlabel"><label for="name">Наименование</label></span>
      <validation:textinput  class="brd w600" maxlength="${nomenclatureNameLength }" name="name"   value="${name }" /></p>
			<div class="clr"><!--  --></div>
		  <p><span class="fieldlabel"><label for="dateFrom">от дата</label> </span>
        <validation:dateinput name="dateFrom" id="dateFrom" value="${dateFrom }" style="brd w200" />
      </p>
		  <div class="clr"><!--  --></div>
			<p>
        <span class="fieldlabel"><label for="dateTo">до дата</label></span> <validation:dateinput name="dateTo" id="dateTo" value="${dateTo }" style="brd w200" />
      </p>
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
		<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true"/>
		<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
		<validation:textValidator input="name" minLength="1" maxLength="${nomenclatureNameLength }" required="true" regex="${validationString}" errormessage="${validationErrorMessage}" />
	</validation:form>
</nacid:flatNomenclature>
<%@ include file="/screens/footer.jsp"%>