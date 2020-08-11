<%@ tag trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs" %>
<%@ taglib uri="/META-INF/nacid_common_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="name" required="true" rtexprvalue="true"%>
<%@ attribute name="attributeName" required="false" rtexprvalue="true"%>
<%@ attribute name="style" required="false" rtexprvalue="true"%>
<%@ attribute name="onclick" required="false" rtexprvalue="true"%>
<%@ attribute name="singleline" required="false" rtexprvalue="true" description="if set, no line break tag is added at the end of every option" %>
<nacid:radiobutton attributeName="${attributeName}" style="${style}"
	onclick="${onclick}">
	<input name="${name }" type="radio"
		${radiostyle } ${radioonclick } value="${key }" ${selected } id="${name }_rad_${cnt}"
			<c:forEach var="attrEntry" items="${dynamicAttrs}">
           		${attrEntry.key}="${attrEntry.value}" 
    		</c:forEach>
			<c:forEach var="attrEntry" items="${additionalAttributes}">
				${attrEntry.key}="${attrEntry.value}"
			</c:forEach>
	/><label for="${name }_rad_${cnt}">${value }</label>
		<c:if test="${not singleline}">
		  <br />
		</c:if>
</nacid:radiobutton>
