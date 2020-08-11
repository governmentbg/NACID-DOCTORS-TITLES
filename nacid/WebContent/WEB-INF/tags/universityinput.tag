<%@ tag import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider" %>
<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="countryComboAttribute" required="true" rtexprvalue="true"%>
<%@ attribute name="onchange" required="false" rtexprvalue="true"%>
<%@ attribute name="onSuccess" required="false" rtexprvalue="true"%>
<%@ attribute name="universityLabel" required="true" rtexprvalue="true"%>

<div style="display:none;" id="university_new_correct_message_div" class="correct">Данните бяха успешно въведени!</div>
<div style="display:none;" id="university_new_error_message_div" class="error">Получи се грешка при опит за запис!</div>

<div id="univeristy_new_div" style="display:none;" class="" style="width: 900px; height: auto;">
	<fieldset class="subwindow_f"><legend class="subwindow_l">Данни за чуждестранното ${fn:toLowerCase(universityLabel)}</legend>
        <v:hidden value="" id="university_id" name="university_id"/>
        <p>
            <span class="fieldlabel"><label for="university_original_name">Оригинално наименование</label></span>
            <v:textinput class="brd w500" name="university_original_name" id="university_original_name"  />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="university_name_bg">Наименование на български</label></span>
            <v:textinput class="brd w500" name="university_name_bg" id="university_name_bg"  value="" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME) %>"
                                     fieldName="Генерично наименование" onkeydown="if(!isSpecialKeyPressed(event)){ $('record_id_university_generic_name').innerHTML = ''; };"
                                     id="university_generic_name" class="brd w450 flt_lft" maxLength="400" windowLevel="3"/>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="university_country_id">Държава</label></span>
            <nacid:combobox name="university_country_id" id="university_country_id" attributeName="${countryComboAttribute}" style="w308 brd" />
        </p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="university_city">Град</label></span>
            <v:textinput class="brd w500" maxlength="100" name="university_city" id="university_city"  />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="university_address">Адрес за кореспонденция</label></span>
            <textarea class="brd w500" rows="3" cols="40" name="university_address" id="university_address"></textarea>
        </p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="university_phone">Телефон</label><br/></span>
            <v:textinput id="university_phone" class="brd w500" name="university_phone"  value="" />
        </p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="university_fax">Факс</label><br/></span>
            <v:textinput id="university_fax" class="brd w500" name="university_fax"  value="" />
        </p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="university_email">Електронна поща</label><br/></span>
            <v:textinput id="university_email" class="brd w500" name="university_email"  value="" />
        </p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="university_webSite">Интернет страница</label><br/></span>
            <v:textinput id="university_webSite" class="brd w500" name="university_webSite"  value="" />
        </p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel"><label for="university_urlDiplomaRegister">Официален регистър за дипломи</label><br/></span>
            <v:textarea id="university_urlDiplomaRegister" rows="3" cols="40" class="brd w500" name="university_urlDiplomaRegister"  value="" />
        </p>
        <div class="clr"><!--  --></div>
        <v:hidden id="university_date_from" name="university_date_from" value=""/>
        <v:hidden id="university_date_to" name="university_date_to" value=""/>

        <a href="javascript:void(0);" onclick="" style="margin-left: 45%;" id="saveUniLnk">Запис</a>
        <a href="javascript:void(0);" onclick="" style="margin-left: 20px;" id="cancelUnviersityEdit">Отказ</a>
	</fieldset>
</div>

<script>

    function showHideNewUniversityDiv(isEdit, elementAfter, level, universityId, onSaveFunction) {
        var editDiv = $('univeristy_new_div');
        var openClass = "subwindow_open" + (level == 1 ? '' : level);
        var windowClass = "subwindow" + (level == 1 ? '' : level);

        if (editDiv.visible() == false) {
            if ($$('.' + openClass).length >= 1) {
                return;
            }
            elementAfter.insert({after : editDiv});
            editDiv.addClassName(openClass);
            editDiv.addClassName(windowClass);
            editDiv.show();
            clearUniversityEditionDiv();
            setUniversityIds(isEdit, universityId);
            $("cancelUnviersityEdit").setAttribute("onClick", "showHideNewUniversityDiv(" + isEdit + ", " + null + ", " + level + ", 0)");
            $("saveUniLnk").setAttribute("onClick", "saveNewUniversity(" + level + ", '" + onSaveFunction + "')");
        } else {
            clearUniversityEditionDiv();
            editDiv.hide();
            editDiv.removeClassName(openClass);
            editDiv.removeClassName(windowClass);
        }


    }

    function setUniversityIds(isEdit, universityId){
        if (universityId != '' && universityId != '-' && isEdit) {
            new Ajax.Request('/nacid/control/university_ajax/view', {
                method: 'post',
                parameters: {
                    id : universityId
                },
                onSuccess: function(transport) {
                    var response = transport.responseText || "no response text";
                    var json = response.evalJSON(true);
                    if (json.error) {
                        alert(json.errorMessage);
                    } else {
                        if (parseInt(json.id) > 0) {
                            $('university_id').value = json.id;
                            $('university_name_bg').value = json.bgName;
                            $('university_original_name').value = json.orgName;
                            $('university_country_id').value = json.countryId;
                            $('university_city').value = json.city;
                            $('university_address').value = json.address;
                            $('university_phone').value = json.phone;
                            $('university_fax').value = json.fax;
                            $('university_email').value = json.email;
                            $('university_webSite').value = json.webSite;
                            $('university_urlDiplomaRegister').value = json.urlDiplomaRegister;
                            $('university_date_from').value = json.dateFrom;
                            $('university_date_to').value = json.dateTo;
                            $('university_generic_name').value = json.genericName;
                            $('record_id_university_generic_name').innerHTML = json.genericNameId;

                        } else {
                        }
                    }
                },
                onFailure: function() {
                }
            });
        } else if (universityId == '' || universityId != '-') {
//            $('university_name_bg').value = nameBgInput.value;
//            $('university_original_name').value = nameOrgInput.value;
        }
    }
    function clearUniversityEditionDiv() {
        $('university_id').value = "";
        $('university_name_bg').value="";
        $('university_original_name').value="";
        $('university_country_id').value="-";
        $('university_city').value="";
        $('university_phone').value="";
        $('university_fax').value="";
        $('university_email').value="";
        $('university_webSite').value="";
        $('university_urlDiplomaRegister').value="";
        $('university_address').value="";
        $('university_generic_name').value="";
        $('record_id_university_generic_name').innerHTML="";
        clearInputError($('university_name_bg'));
        clearInputError($('university_original_name'));
        clearInputError($('university_country_id'));
        clearInputError($('university_city'));
        clearInputError($('university_phone'));
        clearInputError($('university_fax'));
        clearInputError($('university_email'));
        clearInputError($('university_webSite'));
        clearInputError($('university_urlDiplomaRegister'));
        clearInputError($('university_generic_name'));


    }
    function saveNewUniversity(level, onSaveFunction) {
        var ret = true;
        ret = validateText($('university_name_bg'), true, -1, 255, null, '') && ret;
        ret = validateText($('university_original_name'), false, -1, 255, ${validations.allButQuote}, '${messages.err_allButQuote}') && ret;
        ret = validateComboBox($('university_country_id'), true, '') && ret;
        ret = validateText($('university_city'), false, -1, 30, null, '') && ret;
        ret = validateText($('university_phone'), false, -1, 255, null, '') && ret;
        ret = validateText($('university_fax'), false, -1, 255, null, '') && ret;
        ret = validateText($('university_email'), false, -1, 255, ${validations.email }, '') && ret;
        ret = validateText($('university_webSite'), false, -1, 255, null, '') && ret;
        // ret = validateText($('university_urlDiplomaRegister'), false, -1, 255, null, '') && ret;
        if ($('university_generic_name').value != "" && $('record_id_university_generic_name').innerHTML == "") {
            setErrorOnInput($('university_generic_name'), "Трябва да изберете генерично наименование от падащото меню!");
            ret = false;
        }

        if (!ret) {
            alert("Моля коригирайте полетата в червено!");
        } else {
            new Ajax.Request('/nacid/control/university_ajax/save', {
                method: 'post',
                parameters: {
                    id : $('university_id').value,
                    bgName : $('university_name_bg').value,
                    orgName : $('university_original_name').value,
                    country : $('university_country_id').value,
                    address : $('university_address').value,
                    city : $('university_city').value,
                    phone : $('university_phone').value,
                    fax : $('university_fax').value,
                    email : $('university_email').value,
                    webSite : $('university_webSite').value,
                    urlDiplomaRegister : $('university_urlDiplomaRegister').value,
                    dateFrom : $('university_date_from').value,
                    dateTo : $('university_date_to').value,
                    genericNameId : $('record_id_university_generic_name').innerHTML

                },
                onSuccess: function(transport) {
                    $$('#univeristy_new_div').each(function(element) {
                        $(element).removeClassName('errorinput');
                        $(element).removeAttribute('title');
                    });
                    var response = transport.responseText || "no response text";
                    var json = response.evalJSON(true);
                    if (json.error) {
                        alert(json.errorMessage);
                        $('university_new_correct_message_div').hide();
                        $('university_new_error_message_div').show();
                    } else {
                        if (onSaveFunction === undefined) {
                        } else {
                            eval(onSaveFunction);
                            //window[onSaveFunction](json);
                        }
                    }
                    showHideNewUniversityDiv(false, null, level, 0);
                },
                onFailure: function() {
                    showHideNewUniversityDiv(false, null, level, 0);
                    $('university_new_correct_message_div').hide();
                    $('university_new_error_message_div').show();
                }
            });
        }
    }

//    showHideEditUniversityButton();
</script>