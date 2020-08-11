<script>
  window.location.href = "${loginUrl}";
</script>
<%--
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="com.ext.nacid.regprof.web.handlers.impl.RegprofPublicRegisterHandler"%>
<c:set var="pageTitle" value="НАЦИД – Професионални квалификации" scope="request"/>
<%@ include file="/screens_regprof_ext/header_short.jsp"%>  

<hr class="none" />
<div class="topAbout">
  <div class="About">
    <h2>Добре дошли!</h2>
    <p>От модула NoRQ можете да направите справки в публичните регистри или да подадете заявление онлайн.</p>
	<p>Повече информация можете да получите от <a class="underline" href="${pathPrefix }/resources/instruction_guide_regprof_1_0.pdf" title="инструкция за потребителя">инструкция за потребителя</a>.</p>
  </div><!-- //Links -->
</div>
<hr class="none" />
<div class="topLinks">
  <div class="Links">
    <h2>Достъп до публични регистри</h2>
    
    <ul>
      
      <li><a class="underline" href="${pathPrefix }/control/public_register/list?type=<%=RegprofPublicRegisterHandler.REGISTER_TYPE_IZDADENI %>" title="издадени удостоверения">издадени удостоверения</a></li>
      <li><a class="underline" href="${pathPrefix }/control/public_register/list?type=<%=RegprofPublicRegisterHandler.REGISTER_TYPE_OBEZSILENI %>" title="обезсилени удостоверения">обезсилени удостоверения</a></li>
      <li><a class="underline" href="${pathPrefix }/control/public_register/list?type=<%=RegprofPublicRegisterHandler.REGISTER_TYPE_OTKAZI %>" title="откази">откази</a></li>
      <!-- 
      <li><a href="${pathPrefix }/control/public_register_bguniversity/list" title="">признати дипломи от български университети</a></li>
        -->
    </ul>
    
  </div><!-- //Links -->
</div>


<div class="clr"><!--  --></div>

<hr class="none" />
<h2 class="off_jws">Форма за електронно подаване</h2>

<div class="loginBase">
<div class="login">
	<div class="titleLogin">Електронни заявления</div>
    <p style="margin:50px 0px 0px 0px;text-align:center;font-size:25px;">
        <a href="${loginUrl}">Вход</a>
    </p>
</div><!-- //login -->
<nacid:systemmessage />
<div class="msg">
	От тук можете да подавате заявления по електронен път,<br/>
	както и да проследявате статуса на <br/>
	заявления, подадени по електронен път. 
</div>
<div class="clr h50"><!--  --></div>
</div>

<hr class="none" />
<h2 class="off_jws">Форма за справки</h2>
<div class="searchBase">
  <div class="search">
    <div class="titleLogin">Справка за заявление/удостоверение</div>
    <form method="post" action="${pathPrefix }/control/public_report/view" id="search">
      <div class="clr10"></div> 
      <div class="rowLogin"><label class="login_lbl">1. ЕГН/ЛНЧ/личен документ</label> <v:textinput class="login"  name="personalId" maxlength="20" /></div>
      <div class="rowLogin"><label class="login_lbl">2. Входящ №</label> <v:textinput class="login"  name="appNum" /></div>
      <div class="rowOR">или</div>
      <div class="rowLogin"><label class="login_lbl">Удостоверение №</label> <v:textinput class="login"  name="certnum" /></div>
      <div class="flt_rgt">(формат на №: номер/дата)</div>
      <div class="clr"></div> 
      <div class="rowLoginBtn"><input class="loginBtn" type="submit" value="ТЪРСЕНЕ" name="enter" title="Търсене"/></div>
      <div class="clr20"></div> 
    </form>
  </div><!-- //search -->
  <div class="msg">
	Тук можете да направите подробна справка както за заявления,<br />
	независимо от начина на подаване, така и за издадени удостоверения.
  </div>
</div>
<hr class="none" />

<div class="clr"><!--  --></div>

<div class="clr"><!--  --></div>

<%@ include file="/screens_regprof_ext/footer_short.jsp"%>
--%>