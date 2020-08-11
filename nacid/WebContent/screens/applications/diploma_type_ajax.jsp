<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<nacid:combobox name="diploma_type" id="diploma_type" attributeName="diplomaTypeCombo" style="w500 brd" onchange="updateEduLevelName(this); showHideEditDiplomaTypeButton();" />