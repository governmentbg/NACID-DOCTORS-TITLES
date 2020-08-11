<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ attribute name="value" required="false" rtexprvalue="true"%>
<%@ attribute name="path" required="false" rtexprvalue="true"%>
<v:tinput path="${pageScope.path }" value="${pageScope.value }">
<input type="hidden" value="<c:out value='${value }' />"
	<c:forEach var="attrEntry" items="${dynamicAttrs}">
           ${attrEntry.key}="${attrEntry.value}" 
    </c:forEach>
/>
</v:tinput>