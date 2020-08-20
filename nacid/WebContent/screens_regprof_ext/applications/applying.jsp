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
<c:if test="${!operationView && !hideApplyButton}">
	<form style="display: none" id="request-form" method="post" action="${signRequestUrl}">
		<input type="hidden" name="requestSignature" value="<c:out value="${requestJson}"/>" />
	</form>
</c:if>
<v:form name="appform4" action="${pathPrefix }/control/applications/save" method="post" skipsubmitbuttons="true">
	 <v:comboBoxValidator input="serviceTypeId" required="true"/>
	 <v:comboBoxValidator input="paymentTypeId" required="true"/>
	  	<%@include file="apply_submit_buttons.jsp" %>
        <nacid:systemmessage name="applyingStatusMessage"/>	  	
	  	<input name="activeForm" value="4" type="hidden" />
	  	<input name="applicationId" value="${id }" type="hidden" />
	  	
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
	  	<c:if test="${!operationView && !hideApplyButton}">
		  	<fieldset><legend>Плащане</legend>
		  		<p><span class="fieldlabel"><label for="serviceTypeId">Тип услуга</label><br/></span>
					<nacid:combobox name="serviceTypeId" attributeName="serviceType" id="serviceTypeId" style="brd w308"/>									
	    		</p> 
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel"><label for="paymentTypeId">Начин на плащане</label><br/></span>
					<nacid:combobox name="paymentTypeId" attributeName="paymentType" id="paymentTypeId" style="brd w308"/>									
	    		</p> 
				<div class="clr10"><!--  --></div>
		  	</fieldset>
	  	</c:if>
	  	<div class="clr10"><!--  --></div>
	  	<%@include file="apply_submit_buttons.jsp" %>
</v:form>