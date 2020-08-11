<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires"  prefix="inquires"%>
<script type="text/javascript" src="${pathPrefix }/js/inquires.jsp"></script>
<script type="text/javascript">
var universities = ${universities_count};
</script>
<h3 class="title"><span>Справка</span></h3>
    <v:form action="${pathPrefix }/control/inquiry_inquire/print"
        method="post" name="InquireForm"
        skipsubmitbuttons="true">
	<inquires:inquire_submit_buttons formName="inquiry_inquire" />
	<div class="clr10"><!--  --></div>
	
    
	<inquires:commission_inquire_subelement chosenTitle="Избрани Типове документи" labelTitle="Тип документ" type="documentType" comboAttribute="documentTypesCombo" elementIds="documentTypesIds" title="Типове документи" selectedElements="selectedDocumentTypes" />
    <inquires:university_fieldset />    
    
    <inquires:commission_inquire_subelement chosenTitle="Избрани статуси на напомнянето" labelTitle="Статус на напомнянето" type="eventStatus" comboAttribute="eventStatusesCombo" elementIds="eventStatusesIds" title="Статус на напомнянето" selectedElements="selectedEventStatuses" />
    
    <input type="hidden" name="screen" value="" />
    <input type="hidden" id="print_type" name="print_type" value="" />
    <input type="hidden" id="screen_options" name="screen_options" value="" />
    <input type="hidden" name="universities_count" id="universities_count" value="${universities_count }" />
    <inquires:inquire_submit_buttons formName="inquiry_inquire"></inquires:inquire_submit_buttons>
</v:form>
<div class="clr20"><!--  --></div>
<script type="text/javascript">
	showHideDiv('documentType');
	showHideDiv('eventStatus');
    showHideDiv('university0');
    showHideDiv('university0Names');
    showHideDiv('org_university0Names');
	

	
</script>
<div id="table_result">
</div>
<inquires:screen_options />
<div id="inquires_screens">
</div>