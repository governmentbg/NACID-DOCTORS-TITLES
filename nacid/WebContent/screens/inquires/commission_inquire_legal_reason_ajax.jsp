<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires" prefix="inquires"%>
<inquires:commission_inquire_subelement chosenTitle="Избрани Правни основания" labelTitle="Правно основание" type="legalReason${param.row_id}" comboAttribute="legalReasonCombo" elementIds="legalReasonIds" title="Правно основание" selectedElements="selectedLegalReasons" comboClass="brd w400"/>