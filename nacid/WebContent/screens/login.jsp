<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="bg" xml:lang="bg">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8;" />
<!--  -->
<meta http-equiv="content-language" content="bg" />

<title>НАЦИД - Академично признаване</title>
<meta name="author" content="" />
<meta name="keywords" content="" />
<meta name="description" content="" />

<link type="text/css" rel="stylesheet" media="screen"
	href="${pathPrefix }/css/login.css" />

</head>

<body>

<div id="content">
<div id="banner">
<div class="flt_lft w_prcnt50">
<h1 class="">НАЦИД <span><!-- система "RUDi" --></span></h1>
</div>
<div class="flt_lft w_prcnt49">
<ul class="rr">
	<li class="help"><a href="#">помощ</a></li>
</ul>
</div>
</div>
<!-- end of banner -->
<div class="clr"><!--  --></div>

<!-- *************************** begin page content -->
<div class="loginBase">

<h3 class="title"><span>Вход в системата</span></h3>

<nacid:systemmessage />

<div class="login">
<div class="titleLogin">Въведете потребителско име и парола за
достъп</div>
<form method="post" action="${pathPrefix }/control/login" id="login">
<input type="hidden" name="sub" value="1" />
<div class="rowLogin"><label class="login_lbl">Потребителско
име</label> <v:textinput class="login"  name="username" maxlength="20" /></div>
<div class="rowLogin"><label class="login_lbl">Парола</label> <input
	class="login" type="password" name="password" /></div>
<!-- 
      <div class="rowLogin">
        <input type="checkbox" name="autologin" value="1" /><label>Автоматично влизане</label>
      </div>
       -->
<div class="rowLoginBtn"><input class="loginBtn" type="submit"
	value="ВХОД" name="enter" /></div>

</form>
</div>

<div class="clr h50"><!--  --></div>
</div>
<!-- end of loginBase --> <!-- ******************************************* end of page content -->

<div class="clr"><!--  --></div>

<div id="footer">
<div class="flt_lft w_prcnt50">
<p>&nbsp;<!-- some text in footer (if you like that) --></p>
</div>
</div>
<!-- end of footer --></div>
<!-- end of content -->

<%@include file="google_analytics.jsp" %>
</body>
</html>
