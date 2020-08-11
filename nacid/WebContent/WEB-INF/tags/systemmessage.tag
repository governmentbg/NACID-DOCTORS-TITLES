<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib uri="/META-INF/nacid_common_taglib.tld" prefix="nacid"%>
<%@ attribute name="name" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<nacid:systemmessage name="${name }" id="${id }">
	<div class="${style }" ${divId }>
    ${title }
    <nacid:systemmessageattribute>
		<div>${attribute }</div>
	</nacid:systemmessageattribute>
	</div>
</nacid:systemmessage>
