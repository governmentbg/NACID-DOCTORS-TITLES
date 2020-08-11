<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib uri="/META-INF/nacid_common_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ attribute name="name" required="true" rtexprvalue="true"%>
<%@ attribute name="attributeName" required="false" rtexprvalue="true"%>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="onclick" required="false" rtexprvalue="true"%>
<%@ attribute name="onchange" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="disabled" required="false" rtexprvalue="true"%>
<%@ attribute name="path" required="false" rtexprvalue="true"%>
<%@ attribute name="selectedValue" required="false" rtexprvalue="true"%>
<nacid:combobox attributeName="${pageScope.attributeName}" style="${pageScope.style}"
	onclick="${pageScope.onclick}" onchange="${pageScope.onchange }" id="${pageScope.id}"
	disabled="${pageScope.disabled}" path="${pageScope.path }"
    selectedValue="${pageScope.selectedValue}">
	<select name="${pageScope.name }"${comboattributes } >
	<nacid:comboboxOptions>
		<option ${ selected } value="${ key }"
				<c:forEach items="${additionalAttributes}" var="arg">
					<c:out value="${arg.key}"/>="<c:out value="${arg.value}"/>"
				</c:forEach>
		>${ value }</option>
	</nacid:comboboxOptions>
	</select>
</nacid:combobox>