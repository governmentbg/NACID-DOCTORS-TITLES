<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ attribute name="name" required="false" rtexprvalue="true"%>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="value" required="false" rtexprvalue="true"%>
<%@ attribute name="emptyString" required="false" rtexprvalue="true"%>
<%@ attribute name="path" required="false" rtexprvalue="true"%>
<%@ attribute name="pattern" required="false" rtexprvalue="true"%>
<v:dinput name="${pageScope.name}" value="${pageScope.value}" style="${pageScope.style}" id="${pageScope.id}" emptyString="${pageScope.emptyString }" path="${pageScope.path }" pattern="${pageScope.pattern }">
	<input type="text" ${attributes } maxlength="20"
		onclick="if (value=='${eString}') value = ''"
		onblur="if (value=='') value='${eString}';" 
		<c:forEach var="attrEntry" items="${dynamicAttrs}">
            ${attrEntry.key}="${attrEntry.value}" 
        </c:forEach>
		/>
</v:dinput>
