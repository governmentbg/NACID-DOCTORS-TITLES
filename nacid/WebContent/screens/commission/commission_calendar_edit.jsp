<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<%@ include file="commission_calendar_header.jsp"%>
<nacid:commissionCalendarEdit>
	<h3 class="title"><span>${messages.basic_information }</span></h3>
	
	<h3 class="names">${commission_calendar_header }</h3>
	
	<validation:form name="commission_calendar_form" action="${pathPrefix }/control/commission_calendar/save" method="post" backurl="${pathPrefix }/control/commission_calendar/list?getLastTableState=1">
		<input type="hidden" name="id" value="${id }" />
		<fieldset><legend>${messages.basic_information }</legend> 
		<nacid:systemmessage />
		<p><span class="fieldlabel"><label for="name">Дата и час</label></span> 
			<validation:dateinput style="brd w600" name="dateTime" value="${dateTime }" emptyString="дд.мм.гггг чч:мм"/>
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="notes">${messages.notes }:</label></span> 
        	<textarea class="brd w600" rows="3" cols="40" name="notes" id="notes">${notes }</textarea></p>
        <div class="clr"><!--  --></div>
		<p>
          <span class="fieldlabel"><label for="session_status">Статус на заседанието</label></span>
          <nacid:combobox name="session_status" id="session_status" attributeName="sessionStatusCombo" style="w600 brd" />
        </p>
		</fieldset>
		<validation:dateValidator format="d.m.yyyy H:n" emptyString="дд.мм.гггг чч:мм" input="dateTime" required="true" beforeToday="($('session_status').options[$('session_status').selectedIndex].value == 2)"/>
	</validation:form>
</nacid:commissionCalendarEdit>


<%@ include file="commission_calendar_footer.jsp"%>