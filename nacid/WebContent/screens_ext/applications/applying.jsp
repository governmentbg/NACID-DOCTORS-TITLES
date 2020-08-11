<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<script type="text/javascript">
function changeSubmitClass() {
	$$('#appform4 input[type=submit]').each(function(element) {
	    element.addClassName('sign');
	    element.removeClassName('save');
	});
}
</script>
<v:form name="appform4" action="${pathPrefix }/control/application/save" method="post" skipsubmitbuttons="true">
	  <c:if test="${!operationView && !hideApplyButton}">
	  	 <input name="xmlContent" value="<c:out value="${applicationXml}"/>" type="hidden" />
         <input name="signedXml" value="" type="hidden" />
	  </c:if>
	  	<%@include file="apply_submit_buttons.jsp" %>
        <nacid:systemmessage name="applyingStatusMessage"/>	  	
	  	<input name="activeForm" value="4" type="hidden" />
	  	<input name="applicationId" value="${applicationId }" type="hidden" />
	  	
	  	<c:if test="${empty requestScope.applyingStatusMessage || not empty param.showtab}">
	  	<fieldset><legend>Необходима информация, за да се подаде заявлението</legend>
	  		<ul>
	  			<c:forEach var="sm" items="${smList }">
	  				<li class="${sm.style}" style="text-align: left;">${sm.title}
						<c:if test="${not empty sm.attributes}">
							<ul>
								<c:forEach var="attr" items="${sm.attributes}">
									<li class="${sm.style}" style="text-align: left;">${attr}</li>
								</c:forEach>
							</ul>
						</c:if>
					</li>

	  			</c:forEach>

	  		</ul>
	  	</fieldset>
	  	</c:if>
	  	<%@include file="apply_submit_buttons.jsp" %>
</v:form>