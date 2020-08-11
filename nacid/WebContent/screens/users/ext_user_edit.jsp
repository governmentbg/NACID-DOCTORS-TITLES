<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>

<h3 class="title"><span>${operationStringForScreens } потребител</span></h3>
<nacid:systemmessage />
<v:form name="extUsersForm" action="${pathPrefix }/control/ext_users/save"
	method="post"
	backurl="${pathPrefix }/control/ext_users/list?getLastTableState=1">
	
	<nacid:extUserEdit>
		<input type="hidden" name="id" id="id" value="${id }" />
		<fieldset><legend>Основни данни</legend>
			<p><span class="fieldlabel"><label for="username">Потребителско име:</label><br /></span> 
				${username }
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel"><label for="fullname">Пълно име:</label><br /></span> 
				${fullname }
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel"><label for="fullname">Тип потребител:</label><br /></span> 
				${type }
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel"><label for="fullname">Електронна поща:</label><br /></span> 
				${email }
			</p>
			
			
			
			<div class="clr"><!--  --></div>

		</fieldset>
		
	</nacid:extUserEdit>
</v:form>
<%@ include file="/screens/footer.jsp"%>