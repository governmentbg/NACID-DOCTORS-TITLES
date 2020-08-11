<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>

<h3 class="title"><span>${operationStringForScreens } обща променлива</span></h3>
<nacid:systemmessage />
<v:form name="commonvars" action="${pathPrefix }/control/commonvariables/save" method="post" backurl="${pathPrefix }/control/commonvariables/list?getLastTableState=1">
	<v:textValidator input="value" required="true" />
	<input type="hidden" name="id" id="id" value="${commonVarWebModel.id }" />
	<input type="hidden" name="name" id="name" value="${commonVarWebModel.name }" />
	<fieldset><legend>Обща променлива</legend>
		<p>
			<span class="fieldlabel"><label>Описание: </label></span>
			<v:textinput id="description" name="description" value="${commonVarWebModel.description }"   class="brd w600"/>
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="value">Стойност</label></span>
			<textarea class="brd w600 h300" rows="3" cols="40" name="value" id="value">${commonVarWebModel.value }</textarea>
		</p>
		<div class="clr"><!--  --></div>
	</fieldset>
</v:form>
<%@ include file="/screens_regprof/footer.jsp"%>
