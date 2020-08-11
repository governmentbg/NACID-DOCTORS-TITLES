<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } напомняне</span></h3>


	<v:form action="${pathPrefix }/control/application_event/save"
		method="post" name="formApplEvent"
		backurl="${backUrlEvents }" >

		<v:comboBoxValidator input="eventType" required="true"/>

		

		<nacid:systemmessage />
		
		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="applID" type="hidden" name="applID" value="${applID }" />
		
		
		<nacid:eventEditTag />
		
		<div class="clr20"><!--  --></div>


	</v:form>
<div class="clr20"><!--  --></div>

<%@ include file="/screens/footer.jsp"%>

