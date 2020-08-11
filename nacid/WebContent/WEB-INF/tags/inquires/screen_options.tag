<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div id="screen_options_div" style="display:none;">
<form name="screen_options_form" id="screen_options_form" action="">
    <fieldset><legend>Настройки</legend>
		<p class="checkbox">
			<input name="screen_options" value="docFlowNumber" type="checkbox" />
			<label for="docFlowNumber">Деловоден номер и дата</label>
		</p>
        <div class="clr"><!--  --></div>
        <p class="checkbox">
            <input name="screen_options" value="applicantNames" type="checkbox" />
            <label for="applicantNames">Имена на заявителя</label>
        </p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="ownerNames" type="checkbox" />
			<label for="ownerNames">Име, Презиме и Фамилия на собственика на дипломата</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="ownerCitizenship" type="checkbox" />
			<label for="ownerCitizenship">Гражданство на заявителя</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="universityName" type="checkbox" />
			<label for="universityName">Наименование на ВУ</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="universityCountry" type="checkbox" />
			<label for="universityCountry">Държава по седалище на ВУ</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="universityExaminationRecognized" type="checkbox" />
			<label for="universityExaminationRecognized">Статус на висшето училище</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="trainingLocation" type="checkbox" />
			<label for="trainingLocation">Място на обучение</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="diplomaEduLevel" type="checkbox" />
			<label for="diplomaEduLevel">ОКС по диплома</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="diplomaSpeciality" type="checkbox" />
			<label for="diplomaSpeciality">Специалност по диплома</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="diplomaQualification" type="checkbox" />
			<label for="diplomaQualification">Професионална квалификация по диплома</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="diplomaDate" type="checkbox" />
			<label for="diplomaDate">Диплома – дата</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="trainingStart" type="checkbox" />
			<label for="trainingStart">Начало на обучението</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="trainingEnd" type="checkbox" />
			<label for="trainingEnd">Край на обучението</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="trainingForm" type="checkbox" />
			<label for="trainingForm">Форма на обучение</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="schoolDiplomaInfo" type="checkbox" />
			<label for="schoolDiplomaInfo">Предходно образование – средно образование</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="previousUniDiplomaInfo" type="checkbox" />
			<label for="previousUniDiplomaInfo">Предходно образование – предходна образователна степен за висше образование</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="commissionStatuses" type="checkbox" />
			<label for="commissionStatuses">Статус на заявлението от Комисия</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="commissionCalendars" type="checkbox" />
			<label for="commissionCalendars">Номер на заседание на Комисията</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="motives" type="checkbox" />
			<label for="motives">Мотиви</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="recognizedEduLevel" type="checkbox" />
			<label for="recognizedEduLevel">Призната ОКС</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="recognizedSpecialities" type="checkbox" />
			<label for="recognizedSpecialities">Призната специалност</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="recognizedQualification" type="checkbox" />
			<label for="recognizedQualification">Призната квалификация</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="applicationStatus" type="checkbox" />
			<label for="applicationStatus">Актуален статус на заявлението</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="docflowStatus" type="checkbox" />
			<label for="docflowStatus">Деловоден статус</label>
		</p>
		<div class="clr"><!--  --></div>
        <input id="show_button" type="button" onclick="submitScreenOptions();" value="Покажи" name="sbmt" class="save flt_rgt w110"/>
        <input id="word_button" type="button" value="Word" name="sbmt" class="save flt_rgt w90" onclick="javascript: updateScreenOptionsForms();$('InquireForm').submit();"/>
    </fieldset>
</form>
</div>
<script type="text/javascript">
function submitScreenOptions() {
	updateScreenOptionsForms();
	ajaxGenerate($('InquireForm'), $('screen_options_form').action, 'inquires_screens', $('screen_options_form'));
}
function updateScreenOptionsForms() {
	var h = new Hash();
	$('screen_options_form').getInputs('checkbox', 'screen_options').each(function(element){
	    if (element.checked) {
	        h.set(element.value, element.value);
	    }
	});
	$('screen_options').value = h.values().join(";");
}
</script>