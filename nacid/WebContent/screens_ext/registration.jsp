<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="pageTitle" value="Регистрация" scope="request"/>
<%@ include file="/screens_ext/header_short.jsp"%>
<ext_nacid:registration />
<%@ include file="/screens_ext/footer_short.jsp"%>