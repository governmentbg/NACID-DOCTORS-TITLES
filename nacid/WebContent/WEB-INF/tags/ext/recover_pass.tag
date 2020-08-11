<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!-- *************************** begin page content -->
<div class="RegisterBase"><!-- NEW, 16.8.2010 -->

<!-- <h3 class="title"><span>Регистрация в системата</span></h3> OLD -->
    <div class="titleLogin">Забравена парола</div><!-- NEW, 16.8.2010 -->

<div class="RegisterBaseMdl"><!-- NEW, 16.8.2010 -->
<div class="RegisterBaseBtm"><!-- NEW, 16.8.2010 -->


<div class="w600 single_column">
<nacid:systemmessage />
<v:form name="passRecoverForm" method="post" 
	action="${pathPrefix }/control/recover_pass/save"
	skipsubmitbuttons="true">

	<div class="clr20"></div>
	<div class="clr20"></div>
	<p><span class="fieldlabel2"><label for="email">Електронна поща</label><br /></span>
		 <v:textinput class="brd w300" name="email" id="email" 
			value="" />
	</p>
	<div class="clr"><!--  --></div>

	<p><span class="fieldlabel2"><label for="username">Потребителско име</label><br /></span>
		 <v:textinput class="brd w300" name="username" id="username" 
			value="" />
	</p>	
	<div class="clr"></div>
	
	<div class="clr10"><!--  --></div><!-- CHANGED, 16.8.2010 -->
	<p class="cc">
		<input class="loginBtn" type="button" onclick="document.location.href='${pathPrefix}/control/login';" value="НАЗАД"/>
		<input class="loginBtn" type="submit" value="ИЗПРАТИ" />
	</p>
	<div class="clr20"><!--  --></div><!-- NEW, 16.8.2010 -->
	
</v:form>
</div><!-- // single_column -->
<div class="clr"><!--  --></div>

</div><!-- // RegisterBaseBtm --><!-- NEW, 16.8.2010 -->
</div><!-- // RegisterBaseMdl --><!-- NEW, 16.8.2010 -->
</div><!-- // RegisterBase -->
<!-- end of loginBase --> <!-- ******************************************* end of page content -->