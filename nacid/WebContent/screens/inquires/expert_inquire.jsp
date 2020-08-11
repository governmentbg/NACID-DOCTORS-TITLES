<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires"  prefix="inquires"%>
<%@ include file="/screens/header.jsp"%>
<script type="text/javascript" src="${pathPrefix }/js/inquires.jsp"></script>
<script type="text/javascript">

    function _updateLegalReasons(select, form, type) {
        var additionalName = type=='final' ? "final_" : "";
        var id = select.id.replace(additionalName + 'status_id', '');
        var legalReasonDiv = select.id.replace('status_id', 'legal_reason_div');
        updateLegalReasons(select, form, legalReasonDiv, additionalName + id);
    }

</script>
<h3 class="title"><span>Справка експерти</span></h3>
<v:form action="${pathPrefix }/control/expert_inquire/print"
        method="post" name="InquireForm"
        backurl="${pathPrefix }/control/home" skipsubmitbuttons="true"
        additionalvalidation="validateInquireDates()">
    <v:dateValidator input="appDateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="appDateTo" format="d.m.yyyy" required="false" beforeToday="true"/>
    <v:dateIntervalValidator dateFrom="appDateFrom" dateTo="appDateTo" format="d.m.yyyy" />
    <nacid:hasUserAccess operationname="list">
        <input type="button" value="Филтрирай" name="sbmt" class="save flt_rgt w140" onclick="javascript:if (typeof validateFormInquireForm != 'function' || validateFormInquireForm()) {generateTable($('InquireForm'), '${pathPrefix }/control/expert_inquire/list?reload=1');}" />
    </nacid:hasUserAccess>
    <input type="button" value="Изчисти" name="sbmt" class="save flt_rgt w110" onclick="document.location.href='${pathPrefix}/control/expert_inquire/view';" />

    <div class="clr10"><!--  --></div>
    <inquires:application_type_fieldset />
    <div class="clr10"><!--  --></div>
    <inquires:commission_inquire_subelement chosenTitle="Избрани отговорници" labelTitle="Отговорник" type="responsibleUser" comboAttribute="responsibleUsersCombo" elementIds="responsibleUserIds" title="Отговорници" selectedElements="selectedResponsibleUsers" />
    <div class="clr10"><!--  --></div>
    <inquires:commission_inquire_subelement chosenTitle="Избрани потребители" labelTitle="Потребител" type="user" comboAttribute="usersCombo" elementIds="userIds" title="Потребители" selectedElements="selectedUsers" />

    <fieldset><legend>Данни от заявление</legend>
        <p>
            <span class="fieldlabel2"><label>Деловоден номер</label></span>
            <v:textinput  value="" id="application_number" name="application_number" class="brd w200" />
        </p>
        <p>
            <span class="fieldlabel2"><label>от дата</label></span>
            <v:dateinput value="дд.мм.гггг" id="appDateFrom" name="appDateFrom" style="brd w200 flt_lft" />
            <span class="fieldlabel2"><label>до дата</label></span>
            <v:dateinput value="дд.мм.гггг" id="appDateTo" name="appDateTo" style="brd w200 flt_lft" />
        </p>
    </fieldset>
    <inquires:status_fieldset addLegalReasons="true" />
    <div class="clr10"><!--  --></div>
    <input type="hidden" name="screen" value="" />
    <input type="hidden" id="print_type" name="print_type" value="" />
    <input type="hidden" id="screen_options" name="screen_options" value="" />
    <input type="hidden" name="status_ids_count" id="status_ids_count" value="${status_ids_count }" />

    <p class="checkbox flt_rgt">
        <input id="show_apps" name="show_apps" value="1" type="checkbox" onclick="if (document.tableForm1 != null) {document.tableForm1.show_apps.value = $('show_apps').checked ? 1 : 0}">
        <label for="show_apps">Покажи списъка със заявленията</label>
    </p>
    <div class="clr"><!--  --></div>
    <nacid:hasUserAccess operationname="list">
        <input type="button" value="Филтрирай" name="sbmt" class="save flt_rgt w140" onclick="javascript:if (typeof validateFormInquireForm != 'function' || validateFormInquireForm()) {generateTable($('InquireForm'), '${pathPrefix }/control/expert_inquire/list?reload=1');}" />
    </nacid:hasUserAccess>
    <input type="button" value="Изчисти" name="sbmt" class="save flt_rgt w110" onclick="document.location.href='${pathPrefix}/control/expert_inquire/view';" />

</v:form>
<div class="clr20"><!--  --></div>
<div id="table_result">
</div>
<script type="text/javascript">
    applicationTypeChanged();
</script>
<%@ include file="/screens/footer.jsp"%>
