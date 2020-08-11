<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } тип напомняне</span></h3>
<nacid:eventTypeEdit>
	<v:form action="${pathPrefix }/control/event_type/save" method="post" name="form_event_type"
		backurl="${pathPrefix }/control/event_type/list?getLastTableState=1">

		<v:textValidator input="eventName" required="true" maxLength="100"/>
		<v:textValidator input="dueDays" required="true" regex="/^\d+$/" />
		<v:textValidator input="reminderDays" regex="/^\d+$/" />

		<nacid:systemmessage />

		<input id="id" type="hidden" name="id" value="${id }" />

		<fieldset><legend>Основни данни</legend>

			<p><span class="fieldlabel2"><label for="eventName">Име</label><br /></span> 
				<v:textinput class="brd w600" id="eventName" name="eventName"  value="${eventName }" />
			</p>
			<div class="clr"><!--  --></div>
		
			<p><span class="fieldlabel2"><label for="reminderDays">Срок за напомняне</label><br /></span> 
				<v:textinput class="brd w200" id="reminderDays" name="reminderDays"  value="${reminderDays }" />
				дни
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="dueDays">Краен срок</label><br /></span> 
				<v:textinput class="brd w200" id="dueDays" name="dueDays"  value="${dueDays }" />
				дни
			</p>
			<div class="clr"><!--  --></div>
			
			
			<p><span class="fieldlabel2"><label for="reminderText">Текст на напомнянето</label><br /></span> 
				<textarea id="reminderText" class="brd w600" rows="3" cols="40" name="reminderText">${reminderText }</textarea>
			</p>
			<div class="clr"><!--  --></div>
		
		
		</fieldset>


	</v:form>
</nacid:eventTypeEdit>

<%@ include file="/screens/footer.jsp"%>
