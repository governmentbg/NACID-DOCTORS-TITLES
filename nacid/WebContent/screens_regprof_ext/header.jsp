<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>
<%@ taglib uri="/META-INF/ext_regprof_nacid_taglib.tld" prefix="nacid_regprof_ext"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="bg" xml:lang="bg">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
<meta http-equiv="content-language" content="bg" />

<title>НАЦИД - Професионални квалификации</title>

<meta name="author" content="" />
<meta name="keywords" content="" />
<meta name="description" content="" />

<link type="text/css" rel="stylesheet" media="screen" href="${pathPrefix }/css/default.css" />
<link type="text/css" rel="stylesheet" media="screen" href="${pathPrefix }/css/forms.css" />
<link type="text/css" rel="stylesheet" media="screen" href="${pathPrefix }/css/autocomplete.css" />
<link type="text/css" rel="stylesheet" media="screen" href="${pathPrefix }/css/tabs.css" />
<!--[if IE 6]>
  <link type="text/css" rel="stylesheet" media="screen" href="${pathPrefix }/css/tabs_fix6.css" />
<![endif]-->
<script type="text/javascript" src="${pathPrefix }/js/prototype.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/menu.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/validation.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/datefunctions.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/autocomplete.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/table.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/ajax_update.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/functions.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/sortable.js"></script>
<script type="text/javascript" src="${pathPrefix }/js/regprof_functions.js"></script>
<script type="text/javascript"> document.documentElement.className += " js"</script>
</head>

<body>
<div id="noscript">Please enable JavaScript, then refresh this page. JavaScript is required on this site</div>
<div id="wrapper">
<div id="content"><nacid:loggeduser>
	<div id="banner">
	<div class="flt_rgt w_prcnt30">
	<ul class="rr">
		<li class="help"><a href="#">помощ</a></li>
		<li class="home"><a href="${pathPrefix }/control/home">начало</a></li>
		<li class="logout"><a href="${pathPrefix }/j_spring_security_logout">изход</a></li>
	</ul>
	</div>
	<div class="flt_lft w_prcnt30">
	<h1 class="">НАЦИД <span>система "NoRQ"</span></h1>
	</div>
	<div class="flt_lft w_prcnt30">
	<h1><span class="loggeduser">${username }</span></h1>
	</div>
	</div>
	<!-- end of banner -->
</nacid:loggeduser>
<div class="clr"><!--  --></div>

<div id="left_column">

<div class="left_column_top"><!--  --></div>
<div class="left_column_mdl">
<div class="clr10"><!--  --></div>
<!-- MENU START --> <nacid:menu /> <!-- MENU END --></div>
<!-- // left_column_mdl -->
<div class="left_column_btm"><!--  --></div>

</div>
<!-- end of left_column -->

<div id="right_column"><!-- *************************** begin page content -->