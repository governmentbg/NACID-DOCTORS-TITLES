<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<script type="text/javascript">
    function addInstitution() {
        if ($('institutions').value != '-') {
            $('institution').value = addElementIdToInput($('institution').value, $('institutions').value);
            refreshSelectedTable();
        }
    }
    function refreshSelectedTable() {
        var hidden = $('institution');
        var selected = hidden.value.split(";");
        var tableHtml = '';
        if(hidden.value.length > 0) {
            selected.each(function(item){
                var text = '<a target="_blank" href="${pathPrefix }/control/competent_institution/view?id=' + item + '" >'
                        + getNameFromId(item) + '</a>';
                tableHtml += '<tr><td>' + text + '</td><td><a href="#" onclick="removeItem(\'';
                tableHtml += item + '\'); return false;" ><img src="${pathPrefix}/img/icon_delete.png" /></a></td></tr>';
            });
        }
        if(tableHtml.length > 0) {
            $('selectedDiv').show();
            $('selectedInstitutions').update(tableHtml);
        }
        else {
            $('selectedDiv').hide();
        }

    }
    function getNameFromId(id) {
        var options = $('institutions').options;
        for(var i = 0; i < options.length; i++){
            if(options[i].value == id) {
                return options[i].text;
            }
        }
        return '';
    }
    function removeItem(id) {
        $('institution').value = removeElementIdFromInput($('institution').value, id);
        refreshSelectedTable();
    }
    function resetNewInstitutionForm() {
        clearAllErrors(null);
        $('institution_id').value = "";
        $('institution_name').value = "";
        $('institution_original_name').value = "";
        $('institution_country_id').value = "-";
        $('institution_address_details').value = "";
        $('institution_phone').value = "";
        $('institution_fax').value = "";
        $('institution_email').value = "";
        $('institution_url').value = "";
        $('institution_date_from').value = "";
        $('institution_date_to').value = "";

    }

    function showHideNewInstitutionDiv(isEdit) {

        var editDiv = $('institution_new_div');
        var openClass = "subwindow_open";
        var windowClass = "subwindow";

        if (editDiv.visible() == false) {
            if ($$('.' + openClass).length >= 1) {
                return;
            }
            resetNewInstitutionForm();
            editDiv.addClassName(openClass);
            editDiv.addClassName(windowClass);
            editDiv.show();
            if (isEdit) {
                new AjaxJsonFieldsUpdater({
                    elements: [
                        'institution_id',
                        'institution_name',
                        'institution_original_name',
                        'institution_country_id',
                        'institution_address_details',
                        'institution_phone',
                        'institution_fax',
                        'institution_email',
                        'institution_url',
                        'institution_date_from',
                        'institution_date_to'
                    ]

                }).update('${pathPrefix}/control/competent_institution_ajax/view?id=' + $("institutions").value);
            } else {
                $('institution_country_id').value = ${universityValidityWebModel.universityCountryId};
            }
        } else {
            editDiv.hide();
            editDiv.removeClassName(openClass);
            editDiv.removeClassName(windowClass);
        }
    }



    function saveInstitution() {
        var validated = validateInstitution();
        if (!validated) {
            return;
        }
        new Ajax.Request('${pathPrefix}/control/competent_institution_ajax/save', {
            method: 'post',
            parameters: {
                id : $('institution_id').value,
                name : $('institution_name').value,
                original_name : $('institution_original_name').value,
                country : $('institution_country_id').value,
                address_details : $('institution_address_details').value,
                phone : $('institution_phone').value,
                fax : $('institution_fax').value,
                email : $('institution_email').value,
                url : $('institution_url').value,
                dateFrom : $('institution_date_from').value,
                dateTo : $('institution_date_to').value
            },
            onSuccess: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON(true);

                new Ajax.Request('${pathPrefix}/control/competent_institution_ajax/list', {
                    method: 'post',
                    parameters: {
                        countryId : universityCountryId,
                        elementId : 'institutions',
                        elementName : 'institutions'
                    },
                    onSuccess: function(transp) {
                        var resp = transp.responseText || "no response text";
                        $("institutions").replace(resp);
                        $("institutions").value = json.institution_id;

                    }
                });


                showHideNewInstitutionDiv();
            },
            onFailure : function(transport) {
                alert("Възникна грешка. Моля опитайте по-късно..." + oXHR.statusText);
            }
        });
    }


    function validateInstitution() {
        var ret = true;
        ret = validateText($('institution_name'), true, -1, 255, ${validations.allButQuote}, '${messages.err_allButQuote}') && ret;
        ret = validateText($('institution_original_name'), false, -1, 255, ${validations.original_name}, '${messages.err_original_name}') && ret;
        ret = validateComboBox($('institution_country_id'), true, '') && ret;
        ret = validateText($('institution_phone'), false, -1, 255, null, '') && ret;
        ret = validateText($('institution_fax'), false, -1, 255, null, '') && ret;
        ret = validateText($('institution_url'), false, -1, 255, null, '') && ret;
        ret = validateText($('institution_email'), false, -1, 255, ${validations.email}, '') && ret;

        if (!ret) {
            alert("Моля коригирайте полетата в червено!");
        }
        return ret;
    }

    function toggleInstitutionButton() {
        if ($('institutions').value == '-') {
            $('editInstitutionLnk').hide();
        } else {
            $('editInstitutionLnk').show();
        }
    }

    var universityCountryId = ${universityValidityWebModel.universityCountryId};

</script>


<h3 class="title"><span>${operationStringForScreens }  ${messages.university_check}</span></h3>
<nacid:universityValidityEdit>
	<v:form action="${pathPrefix }/control/university_validity/save" method="post"
		name="form_university_validity"
		backurl="${pathPrefix }/control/${appGroup }/${appOper }?id=${appId }&activeForm=4">

		<nacid:systemmessage name="universityValiditySystemMessage" />

		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="appId" type="hidden" name="applID" value="${appId }" />
		<input id="appOper" type="hidden" name="applOper" value="${appOper }" />
		<input id="applGroup" type="hidden" name="applGroup" value="${appGroup }" />
		<input id="universityId" type="hidden" name="universityId" value="${universityId }" />
			
		<div class="clr"><!--  --></div>

		<fieldset><legend>Общи данни</legend>
			<p><span class="fieldlabel2"><label for="examinationDate">Към дата</label><br /></span> 
				<v:dateinput name="examinationDate" id="examinationDate" value="${examinationDate }" style="brd w200" />
			</p>
			<v:dateValidator format="d.m.yyyy" input="examinationDate" required="true"/>
			<div class="clr"><!--  --></div>
		</fieldset>

		<fieldset><legend>Висше училище</legend>

			<p><span class="fieldlabel2"><label>Висше училище</label><br /></span> 
				<a target="_blank" href="${pathPrefix }/control/university/view?id=${universityId }" >${university }</a>
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel2"><label for="recognized">Признато по седалище</label><br /></span> 
				<input id="recognized" name="recognized" type="checkbox" value="1" ${recognized }/>
			</p>
			<div class="clr"><!--  --></div>

			<p><span class="fieldlabel2"><label for="communicated">${messages.university_communicated}</label><br /></span> 
				<input id="communicated" name="communicated" type="checkbox" value="1" ${communicated }/>
			</p>
			<div class="clr"><!--  --></div>

			<div class="flt_lft w_prcnt49">
				<div class="flt_lft fieldlabel"><b>Провежда обучение</b></div>
				<div class="flt_lft">		
					<c:forEach var="location" items="${trainingLocations }">
						<input name="trainingLocation" value="${location.value}" type="radio" ${location.selected }/>${location.name}<br />
					</c:forEach>	
					<input name="hasJoinedDegrees" value="1" type="checkbox" ${hasJoinedDegrees }/>съвместна образователна <br />програма<br />
				</div>
			</div>
			
      <v:checkBoxValidator input="trainingForm" required="false" />
			<div class="flt_lft w_prcnt50">
				<div class="flt_lft fieldlabel"><b>Начин на обучение</b></div>
				<div class="flt_lft">
					<c:forEach var="form" items="${trainingForms}">
						<input name="trainingForm" value="${form.value}" type="checkbox" ${form.selected }/>${form.name } <br />
					</c:forEach>
					<input id="trainingFormOther" name="trainingFormOther" value="1" onclick="$('training_type_other_name').toggle();" type="checkbox" ${otherFormSelected }/>
					друго <br />
					<v:textinput name="trainingFormOtherText" id="training_type_other_name" class="brd w200"  value="${trainingFormOtherText }" />
					<v:textValidator input="trainingFormOtherText" maxLength="255" required="$('trainingFormOther').checked"/>
				</div>
			</div>
			<div class="clr"><!--  --></div>
		</fieldset>
		<div class="clr"><!--  --></div>
		<fieldset><legend>Институция, компетентна организация на национално ниво</legend> 
			<input id="institution" type="hidden" name="institution" value="${institution }" />
			
			<div id="selectedDiv">
				<p><span class="fieldlabel2"><label for="institutions">Избрани институции</label><br /></span> 
					<table id="selectedInstitutions" ></table>
				</p>
				<div class="clr10"><!--  --></div>
			</div>
			<p><span class="fieldlabel2"><label for="institutions">Институции</label><br /></span> 
				<nacid:combobox id="institutions" name="institutions" attributeName="institutionsCombo" style="brd w500" onchange="toggleInstitutionButton();"/>
                <span class="flt_rgt">
                    <a onclick="showHideNewInstitutionDiv(true);" href="javascript: void(0);" id="editInstitutionLnk">Редакция</a>&nbsp;
                    <a onclick="showHideNewInstitutionDiv(false);" href="javascript: void(0);" id="newInstitutionLnk">Нов</a>
                </span>
			</p>
            <div class="clr"><!--  --></div>
            <a class="flt_rgt" href="#" onclick="addInstitution(); return false;">Добави към списъка</a>
            <div class="clr"><!--  --></div>
		</fieldset>






        <div id="institution_new_div" style="display:none;" class="" style="width: 900px; height: auto;">
            <fieldset class="subwindow_f"><legend class="subwindow_l">Данни за компетентна институция на национално ниво</legend>
                <v:hidden value="" id="institution_id" name="institution_id"/>
                <p>
                    <span class="fieldlabel"><label for="institution_name">Наименование</label></span>
                    <v:textinput class="brd w500" name="institution_name" id="institution_name"  />
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="institution_original_name">Оригинално наименование</label></span>
                    <v:textinput class="brd w500" name="institution_original_name" id="institution_original_name"  />
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="institution_country_id">Държава</label></span>
                    <nacid:combobox name="institution_country_id" id="institution_country_id" attributeName="newInstitutionCountry" style="w308 brd" />
                </p>

                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="institution_address_details">Адрес</label></span>
                    <v:textarea class="brd w500" name="institution_address_details" id="institution_address_details"  value="" />
                </p>

                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="institution_phone">Телефон</label></span>
                    <v:textinput class="brd w500" name="institution_phone" id="institution_phone"  value="" />
                </p>

                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="institution_fax">Факс</label></span>
                    <v:textinput class="brd w500" name="institution_fax" id="institution_fax"  value="" />
                </p>

                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="institution_email">Електронна поща</label></span>
                    <v:textinput class="brd w500" name="institution_email" id="institution_email"  value="" />
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel"><label for="institution_url">Интернет страница</label></span>
                    <v:textinput class="brd w500" name="institution_url" id="institution_url"  value="" />
                </p>

                <v:hidden id="institution_date_from" name="institution_date_from" value=""/>
                <v:hidden id="institution_date_to" name="institution_date_to" value=""/>

                <a href="javascript:void(0);" onclick="saveInstitution();" style="margin-left: 45%;" id="saveInstitutionLnk">Запис</a>
                <a href="javascript:void(0);" onclick="showHideNewInstitutionDiv();" style="margin-left: 20px;" id="cancelInstitutionLnk">Отказ</a>
            </fieldset>
        </div>



        <div class="clr"><!--  --></div>





		<fieldset><legend>Бележки</legend>
		<p><span class="fieldlabel2"><label for="notes"><!--  --></label><br /></span> 
                <textarea name="notes" id="notes" class="brd w600" rows="3" cols="40" >${notes }</textarea>
            </p>
            <div class="clr"><!--  --></div>
		</fieldset>
	</v:form>
</nacid:universityValidityEdit>

<nacid:list />

<script type="text/javascript">
    refreshSelectedTable();
    if($('trainingFormOther').checked) {
        $('training_type_other_name').show();
    }
    else {
        $('training_type_other_name').hide();
    }
    toggleInstitutionButton();
<c:if test="${tableWebModel != null}">



		/*
 		* маха линковете за сортиране на таблицата
 		*/
 		$$('#main_table td[class="dark"] > a').each(function(anchor) {
  			anchor.up(0).innerHTML = anchor.innerHTML; 
 		});
		$$('a[title="Преглед"]').each(function(link) {

			var cell = link.ancestors()[0];
			var row = cell.ancestors()[0];
			var fileName = (row.childElements())[4].innerHTML;

			
  			var old = link.href;
  			var ids = old.indexOf('id=') + 3;
  			var id = old.substring(ids, old.length);
			link.href = '${pathPrefix }/control/uni_exam_attachment/'+fileName+'?id='+id;

			var src = '${pathPrefix }/control/uni_exam_attachment/view?width=${messages.imgPreviewWidth }&id='+id;
			(row.childElements())[6].innerHTML = '<img src="'+src+'" alt=""/>';
	
		});

		document.tableForm1.action = '${backUrlUniExam }';
		<%--document.tableForm2.action = '${backUrlUniExam }';--%>


</c:if>
</script>
<%@ include file="/screens/footer.jsp"%>
