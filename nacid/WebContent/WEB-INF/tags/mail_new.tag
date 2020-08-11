<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } електронни писма</span></h3>

<v:form method="post" action="${pathPrefix }/control/applicant_mail/save"
	backurl="${pathPrefix }/control/e_applying/edit?id=${returnId }">

	<input type="hidden" name="returnId" value="${returnId }" />
	<input type="hidden" name="email" value="${email }" />
	
	<p><span class="fieldlabel2"><label for="mailBody">Текст на съобщението</label><br /></span> 
		<textarea class="brd w600" rows="10" cols="40" name="mailBody"></textarea>
	</p>
	<div class="clr"><!--  --></div>
		
</v:form>

