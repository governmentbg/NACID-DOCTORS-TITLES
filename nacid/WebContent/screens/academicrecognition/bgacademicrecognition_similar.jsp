<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<div id="similars_div">
	<jsp:include page="bgacademicrecognition_similar_part.jsp">
		<jsp:param value="${similar}" name="similar"/>
	</jsp:include>
	<input type="hidden" value="${similarSelectedId }" id="similarSelectedHidden"/>
	<input type="hidden" value="${index }" id="similarOfIndex"/>
</div>