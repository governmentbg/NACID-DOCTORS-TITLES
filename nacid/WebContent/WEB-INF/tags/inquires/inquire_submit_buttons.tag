<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="formName" required="true"%>
<nacid:hasUserAccess operationname="print">
    <input type="submit" value="XLS" name="sbmt" class="save flt_rgt w90" onclick="javascript:if (typeof validateFormInquireForm == 'function' && !validateFormInquireForm()) return false; $('print_type').value = 1;" />
</nacid:hasUserAccess>
<nacid:hasUserAccess operationname="list">
    <input type="submit" value="Word" name="sbmt" class="save flt_rgt w90" onclick="javascript:if (typeof validateFormInquireForm == 'function' && !validateFormInquireForm()) return false; $('print_type').value = 2;return generateWordScreenReport(this, $('InquireForm'));" />
    <input type="button" value="Екран" name="sbmt" class="save flt_rgt w90" onclick="javascript:if (typeof validateFormInquireForm != 'function' || validateFormInquireForm()) {generateScreen(this, $('InquireForm'), '${pathPrefix }/control/${formName}/screen');}" />
    <select class="flt_rgt mt5 brd w190" onchange="updateScreenSelect(this);" name="scr_type" >
        <option value="1">Екран-Вариант 1</option>
        <option value="2">Екран-Вариант 2</option>
        <option value="3">Екран-Вариант 3</option>
        <option value="4">Екран-Вариант 4</option>
        <option value="5">Екран-Избираем формат</option>
    </select>
    <input type="button" value="Филтрирай" name="sbmt" class="save flt_rgt w140" onclick="javascript:if (typeof validateFormInquireForm != 'function' || validateFormInquireForm()) {generateTable($('InquireForm'), '${pathPrefix }/control/${formName}/list?reload=1');}" />
</nacid:hasUserAccess>
<input type="button" value="Изчисти" name="sbmt" class="save flt_rgt w110" onclick="document.location.href='${pathPrefix}/control/${formName}/view';" />
