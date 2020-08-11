<%@ tag pageEncoding="utf-8"%>

<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } електронни писма</span></h3>

<c:set var="back_onclick" value="document.location.href='${pathPrefix }/control/${groupName }/list?getLastTableState=1';" />
<p class="cc">
	<input class="back" type="button" onclick="${back_onclick }" value="Назад" />
</p>

<nacid:mailAdminEdit>
	<fieldset><legend class="noForm">Данни за писмото</legend>
	
		<p><span class="fieldlabel"><label>Дата:</label><br /></span> 
			${date }
		</p>
		<div class="clr"><!--  --></div>
	
		<p><span class="fieldlabel"><label>Статус:</label><br /></span> 
			${status }
		</p>
		<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel"><label>Тип:</label><br /></span> 
			${type }
		</p>
		<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel"><label>Получател:</label><br /></span> 
			${recipient } 
		</p>
		<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel"><label>Относно:</label><br /></span> 
			${subject }
		</p>
		<div class="clr"><!--  --></div>
	
	</fieldset>
	
	<fieldset><legend class="noForm">Съдържание на писмото</legend>
		${body }
	</fieldset>
</nacid:mailAdminEdit>
<div class="clr20"><!--  --></div>
<p class="cc">
	<input class="back" type="button" onclick="${back_onclick }" value="Назад" />
</p>