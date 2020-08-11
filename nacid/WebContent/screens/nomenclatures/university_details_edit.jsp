<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<h3 class="title"><span>${operationStringForScreens } детайл на Университет</span></h3>
<nacid:systemmessage />
<v:form name="uniDetails" action="${pathPrefix }/control/universitydetails/save" method="post" backurl="${pathPrefix }/control/universitydetails/list?getLastTableState=1">
	<v:textValidator input="university_name" required="true" />
	<v:textValidator input="letter_recipient" required="true" />
	<v:textValidator input="salutation" required="true" />
	<input type="hidden" name="id" id="id" value="${universityDetailWebModel.id }" />
	<fieldset><legend>Обща променлива</legend>
        <p>
            <span class="fieldlabel"><label for="university_name">Университет: </label></span>
            <input class="brd w600" type="text" name="university_name" id="university_name" value="<c:out value='${universityDetailWebModel.universityName }' />" />
        </p>
        <p>
			<span class="fieldlabel"><label>Получател на съобшението: </label></span>
			<textarea id="letter_recipient" name="letter_recipient" rows="3" cols="40" class="brd w600 h300">${universityDetailWebModel.letterRecipient }</textarea>
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="salutation">Обръщение</label></span>
			<textarea class="brd w600 h300" rows="3" cols="40" name="salutation" id="salutation">${universityDetailWebModel.salutation }</textarea>
		</p>
		<div class="clr"><!--  --></div>
	</fieldset>
</v:form>
<%@ include file="/screens/footer.jsp"%>
