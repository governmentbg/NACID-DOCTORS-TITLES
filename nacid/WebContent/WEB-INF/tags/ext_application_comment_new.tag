<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } електронни писма</span></h3>

<v:form method="post" action="${pathPrefix }/control/${groupName}/save" backurl="${backUrl }">

	<input type="hidden" name="applicationId" value="${applicationId }" />
	<input type="hidden" name="entryNumSeries" value="${param.entryNumSeries}" />

	<p>
		<span class="fieldlabel2"><label for="comment">Коментар/съобщение</label><br /></span>
		<textarea class="brd w600" rows="10" cols="40" name="comment" id="comment"></textarea>
	</p>
	<div class="clr"><!--  --></div>
	<p>
		<span class="fieldlabel2"><label for="send_email">Изпращане на email</label><br /></span>
		<v:checkboxinput value="1" name="send_email" id="send_email"></v:checkboxinput>
	</p>
	<div class="clr"><!--  --></div>
		
</v:form>

