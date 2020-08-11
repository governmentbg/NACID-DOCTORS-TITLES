<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<div class="RegisterBase"><!-- NEW, 16.8.2010 -->

<!-- <h3 class="title"><span>Регистрация в системата</span></h3> OLD -->
    <div class="titleLogin">Регистрация в системата</div><!-- NEW, 16.8.2010 -->

<div class="RegisterBaseMdl"><!-- NEW, 16.8.2010 -->
<div class="RegisterBaseBtm"><!-- NEW, 16.8.2010 -->

<div class="w600 single_column">

<script type="text/javascript" >
	function reloadImg() {
		var time = (new Date()).getTime();
		$('captcha').src = '${pathPrefix }/control/registration/view/' + time; 
		return false;
	}
	function personalIdTypeChanged() {
		$('personalId').clear();
		clearInputError($('personalId'));
		initPersonalIdIgnoreValidation();
	}

	function initPersonalIdIgnoreValidation() {
		$('personalIdIgnoreEGNValidation').checked = !($('rad_0').checked);
		$('personalIdIgnoreEGNValidation').ancestors().first().hide();

		$('personalIdIgnoreLNCHValidation').checked = !($('rad_1').checked);
		$('personalIdIgnoreLNCHValidation').ancestors().first().hide();
	}
	
</script>

<nacid:systemmessage />
<ext_nacid:extPersonEditTag>
<v:form name="registrationForm" method="post" 
	action="${pathPrefix }/control/registration/save"
	skipsubmitbuttons="true">

	<v:textValidator input="email" maxLength="100" required="true" regex="${validations.email }" />
	<v:textValidator input="fname" maxLength="15" required="true" regex="${validations.name }" errormessage="${messages.err_name }" />
    <v:textValidator input="sname" maxLength="15" regex="${validations.name}" errormessage="${messages.err_name}" />
    <v:textValidator input="lname" maxLength="30" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
    <v:textValidator input="birthCity" maxLength="30" />
    <v:textValidator input="username" maxLength="30" required="true"/>
    
    <v:textValidator input="personalId" 
    		maxLength="($('rad_1').checked ? 10 : 50)" required="true"/>
		
    
	<v:textValidator input="password1" required="true" regex="new RegExp($('password2').value)" errormessage="Паролите не съвпадат"/>
	<v:textValidator input="password2" required="true" regex="new RegExp($('password1').value)" errormessage="Паролите не съвпадат"/>
	<v:dateValidator input="birthDate" format="d.m.yyyy" beforeToday="true"  />
	<v:textValidator input="personalId" maxLength="($('rad_1').checked ? 10 : 50)" />
	<v:textValidator input="jcaptcha" required="true" />
	
	<div class="clr20"></div>
	<p><span class="fieldlabel2"><label for="personalIdType">Тип персонален идентификатор</label><br /></span> 
		<c:forEach items="${personalIdTypes }" var="personalIdType">
			<input ${personalIdType.checked } 
				id="rad_${personalIdType.index }" 
				name="personalIdType" value="${personalIdType.id }" 
				onclick="personalIdTypeChanged();" type="radio" />
			<label for="rad_${personalIdType.index }">${personalIdType.name }</label>
		</c:forEach>
	</p>
	<div class="clr5"></div>
	
	<p><span class="fieldlabel2"><label for="personalId">${messages.civilIdType}</label><br /></span>
		<v:textinput class="brd w200" name="personalId" id="personalId"  value="${personalId }" />
	</p>
	<div class="clr"><!--  --></div>
	<v:egnLnchValidator input="personalId" required="true"/>
	<script type="text/javascript">
		initPersonalIdIgnoreValidation();
	</script>
	
	<p><span class="fieldlabel2"><label for="fname">Име</label><br /></span> 
		<v:textinput class="brd w300" name="fname" id="fname"  value="${fname }" />
	</p>
	<div class="clr"><!--  --></div>
	
	<p><span class="fieldlabel2"><label for="sname">Презиме</label><br /></span> 
		<v:textinput class="brd w300" name="sname" id="sname"  value="${sname }" />
	</p>
	<div class="clr"><!--  --></div>
	
	<p><span class="fieldlabel2"><label for="lname">Фамилия</label><br /></span> 
		<v:textinput class="brd w300" name="lname" id="lname"  value="${lname }" />
	</p>
	<div class="clr"><!--  --></div>

	<p><span class="fieldlabel2"><label for="birthCountry">Място на раждане</label><br /></span>
		<nacid:combobox name="birthCountry" id="birthCountry" attributeName="birthCountry" style="brd w308" />
	</p>
	<div class="clr"><!--  --></div>
		
	<p><span class="fieldlabel2"><label for="birthCity">Населено място</label><br /></span>
		 <v:textinput class="brd w300" name="birthCity" id="birthCity" 
			value="${birthCity }" />
	</p>
	<div class="clr"><!--  --></div>
		
	<p><span class="fieldlabel2"><label for="birthDate">Дата на раждане</label><br/></span>
	    <v:dateinput style="brd w200" name="birthDate" value="${birthDate }" />
	</p>
    <div class="clr"><!--  --></div>
		
	<p><span class="fieldlabel2"><label for="citizenship">гражданин на</label><br /></span> 
		<nacid:combobox name="citizenship" id="citizenship" attributeName="citizenship" style="brd w308" />
	</p>
	<div class="clr"><!--  --></div>
	
	<p><span class="fieldlabel2"><label for="email">Електронна поща</label><br /></span>
		 <v:textinput class="brd w300" name="email" id="email" 
			value="${email }" />
	</p>
	<div class="clr"><!--  --></div>

	<p><span class="fieldlabel2"><label for="username">Потребителско име</label><br /></span>
		 <v:textinput class="brd w300" name="username" id="username" 
			value="${username }" />
	</p>
	<div class="clr"><!--  --></div>
	
	<p><span class="fieldlabel2"><label for="password1">Парола</label><br /></span>
		 <input class="brd w300" name="password1" id="password1" type="password"
			value="" />
	</p>
	<div class="clr"><!--  --></div>
	
	<p><span class="fieldlabel2"><label for="password2">Повтори паролата</label><br /></span>
		 <input class="brd w300" name="password2" id="password2" type="password"
			value="" />
	</p>
	<div class="clr"><!--  --></div>
		

	<p><span class="fieldlabel2"><br /></span>
		<a href="#" onclick="return reloadImg();" title="Натиснете тук за друга картинка">
			<img id="captcha" src="${pathPrefix }/control/registration/view" />
		</a>
	</p>
	<p><span class="fieldlabel2"><label for="name">Въведете текста от картинката</label><br /></span> 
		<v:textinput class="brd w200" name="jcaptcha"  value="" /> 
	</p>
	
	<div class="clr"></div>
	
	<div class="clr10"><!--  --></div><!-- CHANGED, 16.8.2010 -->

	<p class="cc"><!-- REMOVED tag br, 16.8.2010 -->
		<input class="loginBtn" type="button" onclick="document.location.href='${pathPrefix}/control/login';" value="НАЗАД"/>
		<input class="loginBtn" type="submit" value="ИЗПРАТИ" />
	</p>
  <div class="clr20"><!--  --></div><!-- NEW, 16.8.2010 -->

</v:form>
</ext_nacid:extPersonEditTag>
</div><!-- // single_column -->
<div class="clr"><!--  --></div>

</div><!-- // RegisterBaseBtm --><!-- NEW, 16.8.2010 -->
</div><!-- // RegisterBaseMdl --><!-- NEW, 16.8.2010 -->
</div><!-- // RegisterBase -->
<!-- end of loginBase --> <!-- ******************************************* end of page content -->