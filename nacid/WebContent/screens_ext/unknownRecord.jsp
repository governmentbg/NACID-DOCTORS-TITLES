<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	isErrorPage="true"%>
<%@ include file="/screens_ext/header.jsp"%>
Error
<br />
${pageContext.exception.message}
<br>
<%@ include file="/screens_ext/footer.jsp"%>