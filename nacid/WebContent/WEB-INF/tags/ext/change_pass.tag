<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>Смяна на парола</span></h3>

<v:form action="${pathPrefix }/control/${handlerId}/save"
		method="post" name="change_pass_form"
		backurl="${pathPrefix }/control/home"> 

	<v:textValidator input="oldPass" required="true" />
	<v:textValidator input="newPass" required="true" regex="new RegExp($('newPass1').value)" errormessage="Паролите не съвпадат"/>
	<v:textValidator input="newPass1" required="true" regex="new RegExp($('newPass').value)" errormessage="Паролите не съвпадат"/>
	

	<nacid:systemmessage />
	
	<p><span class="fieldlabel"><label for="oldPass">Стара парола</label><br /></span> 
		<input class="brd w300" name="oldPass"
			id="oldPass" type="password" value="" />
	</p>
	<div class="clr20"><!--  --></div>
	
	
	<p><span class="fieldlabel"><label for="newPass">Нова парола</label><br /></span> 
		<input class="brd w300" name="newPass"
			id="newPass" type="password" value="" />
	</p>
	<div class="clr"><!--  --></div>
	
	<p><span class="fieldlabel"><label for="newPass1">Повторете паролата</label><br /></span> 
		<input class="brd w300" name="newPass1"
			id="newPass1" type="password" value="" />
	</p>
	<div class="clr"><!--  --></div>

</v:form>
