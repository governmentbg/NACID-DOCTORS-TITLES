<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="onchange" required="false" rtexprvalue="true"%>
<%@ attribute name="onSuccess" required="false" rtexprvalue="true"%>



<div id="university_faculty_new_div" style="display:none;" class="" style="width: 900px; height: auto;">
    <div style="display:none;" id="university_faculty_new_correct_message_div" class="correct">Данните за факултта бяха успешно въведени!</div>
    <div style="display:none;" id="university_faculty_new_error_message_div" class="error">Получи се грешка при опит за запис на данните за факултета!</div>

	<fieldset class="subwindow_f"><legend class="subwindow_l">Данни за факултет</legend>
        <v:hidden value="" id="university_faculty_id" name="university_faculty_id"/>
        <v:hidden value="" id="university_faculty_university_id" name="university_faculty_university_id"/>
        <p>
            <span class="fieldlabel"><label for="university_faculty_name">Наименование</label></span>
            <v:textinput class="brd w500" name="university_faculty_name" id="university_faculty_name"  />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="university_faculty_original_name">Оригинално наименование</label></span>
            <v:textinput class="brd w500" name="university_faculty_original_name" id="university_faculty_original_name"  value="" />
        </p>
        <div class="clr"><!--  --></div>

        <v:hidden id="university_faculty_date_from" name="university_faculty_date_from" value=""/>
        <v:hidden id="university_faculty_date_to" name="university_faculty_date_to" value=""/>

        <a href="javascript:void(0);" onclick="" style="margin-left: 45%;" id="saveUniFacultyLnk">Запис</a>
        <a href="javascript:void(0);" onclick="" style="margin-left: 20px;" id="cancelUniversityFacultyEdit">Отказ</a>
	</fieldset>
</div>

<script>
    function showHideUniversityFacultyEditBox(isEdit, elementAfter, level, universityFacultyId, universityId, onSaveFunction) {
        var editDiv = $('university_faculty_new_div');
        $('university_faculty_university_id').value = universityId;
        var openClass;
        var windowClass;
        if (level == 1) {
            openClass = "subwindow_open";
            windowClass = "subwindow";
        } else if (level == 2) {
            openClass = "subwindow_open2";
            windowClass = "subwindow2";
        }


        if (editDiv.visible() == false) {
            if ($$('.' + openClass).length >= 1) {
                return;
            }
            $('university_faculty_new_correct_message_div').hide();
            $('university_faculty_new_error_message_div').hide();

            elementAfter.insert({after : editDiv});
            editDiv.addClassName(openClass);
            editDiv.addClassName(windowClass);
            editDiv.show();
            clearUniversityFacultyEditionDiv();
            setUniversityFacultyDetails(isEdit, universityFacultyId);
            $("cancelUniversityFacultyEdit").setAttribute("onClick", "showHideUniversityFacultyEditBox(" + isEdit + ", " + null + ", " + level + ", 0, 0)");
            $("saveUniFacultyLnk").setAttribute("onClick", "saveNewUniversityFaculty(" + level + ", '" + onSaveFunction + "')");
        } else {
            clearUniversityFacultyEditionDiv();
            editDiv.hide();
            editDiv.removeClassName(openClass);
            editDiv.removeClassName(windowClass);
        }


    }

    function setUniversityFacultyDetails(isEdit, universityFacultyId){
        if (universityFacultyId != '' && universityFacultyId != '-' && isEdit) {
            new Ajax.Request('/nacid/control/university_faculty_ajax/view', {
                method: 'post',
                parameters: {
                    id : universityFacultyId
                },
                onSuccess: function(transport) {
                    var response = transport.responseText || "no response text";
                    var json = response.evalJSON(true);
                    if (json.error) {
                        alert(json.errorMessage);
                    } else {
                        if (parseInt(json.faculty_id) > 0) {
                            $('university_faculty_id').value = json.faculty_id;
                            $('university_faculty_name').value = json.faculty_name;
                            $('university_faculty_original_name').value = json.faculty_original_name;
                            $('university_date_from').value = json.faculty_date_from;
                            $('university_date_to').value = json.faculty_date_to;

                        } else {
                        }
                    }
                },
                onFailure: function(response) {
                    alert(response);
                }
            });
        }
    }
    function clearUniversityFacultyEditionDiv() {
        $('university_faculty_id').value = "";
        $('university_faculty_name').value="";
        $('university_faculty_original_name').value="";
        $('university_date_from').value="";
        $('university_date_to').value="";
        clearInputError($('university_faculty_name'));
        clearInputError($('university_faculty_original_name'));
        clearInputError($('university_date_from'));
        clearInputError($('university_date_to'));

    }
    function saveNewUniversityFaculty(level, onSaveFunction) {
        var ret = true;
        ret = validateText($('university_faculty_name'), true, -1, 255, null, '') && ret;
        ret = validateText($('university_faculty_original_name'), false, -1, 255, ${validations.allButQuote}, '${messages.err_allButQuote}') && ret;
        if (!ret) {
            alert("Моля коригирайте полетата в червено!");
        } else {
            new Ajax.Request('/nacid/control/university_faculty_ajax/save', {
                method: 'post',
                parameters: {
                    id : $('university_faculty_id').value,
                    universityId : $('university_faculty_university_id').value,
                    name : $('university_faculty_name').value,
                    originalName : $('university_faculty_original_name').value,
                    dateFrom : $('university_faculty_date_from').value,
                    dateTo : $('university_faculty_date_to').value
                },
                onSuccess: function(transport) {
                    $$('#university_faculty_new_div').each(function(element) {
                        $(element).removeClassName('errorinput');
                        $(element).removeAttribute('title');
                    });
                    var response = transport.responseText || "no response text";
                    var json = response.evalJSON(true);
                    if (json.error) {
                        alert(json.errorMessage);
                        $('university_faculty_new_correct_message_div').hide();
                        $('university_faculty_new_error_message_div').show();
                    } else {
                        $('university_faculty_new_correct_message_div').show();
                        $('university_faculty_new_error_message_div').hide();

                        if (onSaveFunction === undefined) {
                        } else {
                            eval(onSaveFunction);
                            //window[onSaveFunction](json);
                        }
                        showHideUniversityFacultyEditBox(false, null, level, 0, 0);
                    }

                },
                onFailure: function(transport) {
                    alert(transport.responseText);
                    // showHideUniversityFacultyEditBox(false, null, level, 0, 0);
                    $('university_faculty_new_correct_message_div').hide();
                    $('university_faculty_new_error_message_div').show();
                }
            });
        }
    }

</script>