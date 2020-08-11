<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<span class="fieldlabel"><label for="${empty param.elementName ? 'legalReason' : param.elementName}">Правно основание</label></span>
<nacid:combobox name="${empty param.elementName ? 'legalReason' : param.elementName}" id="${empty param.elementName ? 'legalReason' : param.elementName}" attributeName="legalReasonCombo" style="${empty param.style ? 'w400 brd' : param.style}" />