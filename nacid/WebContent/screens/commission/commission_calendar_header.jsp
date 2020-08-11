<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<ul id="formbuttons" class="tabs">
<nacid:commissionCalendarHeader formid="1">
	<li><a href="${pathPrefix }/control/commission_calendar/${action}/?id=${session_id}" id="a_form1" onclick="javascript:${onclick};" class="m1 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Обща информация</b><b class="p3"><!--  --></b></a></li>
</nacid:commissionCalendarHeader> 
<nacid:commissionCalendarHeader formid="2">
  <li><a href="${pathPrefix }/control/commission_applications/${action}/?calendar_id=${session_id}" id="a_form2" onclick="javascript:${onclick};" class="m2 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Заявления</b><b class="p3"><!--  --></b></a></li>
</nacid:commissionCalendarHeader> 
<nacid:commissionCalendarHeader formid="3">
  <li><a href="${pathPrefix }/control/commission_members_list/${action}/?calendar_id=${session_id}" id="a_form3" onclick="javascript:${onclick};" class="m3 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Списък комисия</b><b class="p3"><!--  --></b></a></li>	
</nacid:commissionCalendarHeader> 
<nacid:commissionCalendarHeader formid="4">
  <li><a href="${pathPrefix }/control/comission_calendar_process/${action}/?calendar_id=${session_id}" id="a_form4" onclick="javascript:${onclick};" class="m4 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Обработка</b><b class="p3"><!--  --></b></a></li>	
</nacid:commissionCalendarHeader> 
</ul>