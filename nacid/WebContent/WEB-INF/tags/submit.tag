<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ attribute name="value" required="true" rtexprvalue="true"%>
<%@ attribute name="onclick" required="false" rtexprvalue="true"%>
<%@ attribute name="type" required="false" rtexprvalue="true"%>
<v:submittag onclick="${onclick }" type="${type }">
	<input class="save" name="sbmt" type="${tp }" value="${value }" ${onclck }/>
	<input name="sub" value="1" type="hidden" />
</v:submittag>
