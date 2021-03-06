<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="test" required="true" rtexprvalue="true"%>
<%@ attribute name="name" required="true" rtexprvalue="true"%>
<%@ attribute name="value" required="true" rtexprvalue="true"%>
<%@ attribute name="escapeXml" required="false" %>
<c:if test="${test}">
    <p>
       <span class="fieldlabel"><label>${name }</label></span>
       <span><c:out value="${value}" escapeXml="${empty escapeXml ? true : escapeXml}"/></span>
    </p>
    <div class="clr"><!--  --></div>
</c:if>