<%@ page contentType="application/javascript; charset=utf-8" pageEncoding="utf-8"%>
/**
 * Created by georgi.georgiev on 6.8.2015 г..
 */

function clearSelectEditFlatNomenclatureMessages(id) {
    $('correct_message_' + id).hide();
    $('error_message_' + id).hide();
    $('present_message_' + id).hide();
}

function showSubwindowForSelectFlatNomenclature(id) {
    var sw = windowLevel[id] == 1 ? "subwindow" : "subwindow" + windowLevel[id];
    if ($$('.' + sw + '_open').length >= 1) {
        return;
    }
    clearSelectEditFlatNomenclatureMessages(id);
    $('new_flat_nomeclature_' + id).addClassName(sw );
    $('new_flat_nomeclature_' + id).addClassName(sw + '_open');
    $(id + '_new').hide();
    $('new_flat_nomeclature_' + id).show();
}

function hideSubwindowForSelectFlatNomenclature(id) {
    var sw = windowLevel[id] == 1 ? "subwindow" : "subwindow" + windowLevel[id];
    $('new_flat_nomeclature_' + id).removeClassName(sw);
    $('new_flat_nomeclature_' + id).removeClassName(sw + '_open');
    $(id + '_new').show();
    $('new_flat_nomeclature_' + id).hide();
    $('error_message_' + id).hide();
}

function saveSelectFlatNomenclature(id, nomenclatureType, onSuccess) {
    var input = $("input_" + id);
    if (validateText(input, true, 2, 100, null, '') == true) {
        new Ajax.Request('${pathPrefix}/control/flat_nomenclature_ajax/save', {
            method: 'post',
            parameters: {type: nomenclatureType, name: input.value},
            onSuccess: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON();
                if (parseInt(json.id) > 0) {
                    clearInputError(input);
                    $('correct_message_' + id).show();
                    $('error_message_' + id).hide();
                    $('present_message_' + id).hide();

                    var cls = $("flat_nomenclature_id_" + id + "_combo").firstDescendant().getAttribute("class");
                    new Ajax.Request('${pathPrefix}/control/flat_nomenclature_ajax/view?type=' + nomenclatureType + '&addEmpty=true&name=' + id + '&defaultValue=' + json.id + '&style=' + cls, {
                        asynchronous: false,
                        onComplete: function(oXHR) {
                            $("flat_nomenclature_id_" + id + "_combo").update(oXHR.responseText);
                        },
                        method: 'GET'
                    });

                    if (onSuccess != '') {
                        eval(onSuccess);
                    }
                    hideSubwindowForSelectFlatNomenclature(id);
                }
                else if (parseInt(json.id) == 0) {
                    $('correct_message_' + id).hide();
                    $('error_message_' + id).hide();
                    $('present_message_' + id).show();
                }
                else {
                    $('correct_message_' + id).hide();
                    $('error_message_' + id).show();
                    $('present_message_' + id).hide();
                }
            },
            onFailure: function() {
                $('correct_message_' + id).hide();
                $('error_message_' + id).show();
                $('present_message_' + id).hide();
            }
        });
    }
}






/**flatnomenclatureinput javascripts
 **/

function clearFlatNomInputMessages(id) {
    $('correct_message_' + id).hide();
    $('error_message_' + id).hide();
    $('present_message_' + id).hide();
}

function showFlatNomInputSubwindow(id, windowLevel) {
    var sw = windowLevel == 1 ? "subwindow" : "subwindow" + windowLevel;
    if ($$('.' + sw + '_open').length >= 1) {
        return;
    }
    clearFlatNomInputMessages(id);
    $('flat_nomenclature_' + id).addClassName(sw );
    $('flat_nomenclature_' + id).addClassName(sw + '_open');
    $(id + '_new').hide();
    $('new_flat_nomeclature_' + id).show();
    if($('Autocomplete_'+id) != null){ //RayaAdded--------------------------------------------------------------
        $('Autocomplete_'+id).parentNode.style.display = "none";
    }
}

function hideFlatNomInputSubwindow(id, windowLevel) {
    var sw = windowLevel == 1 ? "subwindow" : "subwindow" + windowLevel;
    $('flat_nomenclature_' + id).removeClassName(sw);
    $('flat_nomenclature_' + id).removeClassName(sw + '_open');
    $(id + '_new').show();
    $('new_flat_nomeclature_' + id).hide();
    //$('correct_message_' + id).hide();
    $('error_message_' + id).hide();
    if($('Autocomplete_'+id) != null){ //RayaAdded--------------------------------------------------------------
        $('Autocomplete_'+id).parentNode.style.display = "block";
    }
}

function saveFlatNomenclature(id, nomenclatureType, onSuccess, maxLength, windowLevel) {
    if (validateText($(id), true, 2, maxLength, null, '') == true) {
        new Ajax.Request('${pathPrefix}/control/flat_nomenclature_ajax/save', {
            method: 'post',
            parameters: {type: nomenclatureType, name: $(id).value},
            onSuccess: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON();
                if (parseInt(json.id) > 0) {
                    clearInputError(id);
                    $('record_id_' + id).innerHTML = json.id;
                    $('correct_message_' + id).show();
                    $('error_message_' + id).hide();
                    $('present_message_' + id).hide();
                    if (onSuccess != '') {
                        eval(onSuccess);
                    }
                    hideFlatNomInputSubwindow(id, windowLevel);
                }
                else if (parseInt(json.id) == 0) {
                    $('correct_message_' + id).hide();
                    $('error_message_' + id).hide();
                    $('present_message_' + id).show();
                }
                else {
                    $('correct_message_' + id).hide();
                    $('error_message_' + id).show();
                    $('present_message_' + id).hide();
                }
            },
            onFailure: function() {
                $('correct_message_' + id).hide();
                $('error_message_' + id).show();
                $('present_message_' + id).hide();
            }
        });
    }
}