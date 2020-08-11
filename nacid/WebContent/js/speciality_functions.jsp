<%@ page contentType="application/javascript; charset=utf-8" pageEncoding="utf-8"%>
//RayaWritten--------------------------------------------------------------
function showHideSpecialityDivs(specInputId){
	var newSpecDivName = "new_speciality_div_"+specInputId;
	var oldSpecDivName = "speciality_inner_div_"+specInputId;
	var outerSpecDivName = "speciality_outer_div_"+specInputId;
	var afterSpecialityDivName = "after_speciality_div_"+specInputId;
	var specSave = "specSave_"+specInputId;
	
	var addSpecName = "addSpeciality_"+specInputId;
	if($(newSpecDivName).visible() == false){
		if ($$('.subwindow_open').length >= 1) {
			return;
		}
		var specInputIdClasses = $(specInputId).classNames();
		$(newSpecDivName).insert({top:$(oldSpecDivName)});
		$('specialityClass_'+specInputId).value = specInputIdClasses;
		$(specInputId).removeClassName(specInputIdClasses);
		$(specInputId).addClassName("brd w500");
		$(specInputId).addClassName("subwindow_open");
		$(newSpecDivName).show();
		if($(addSpecName) != null){
			$(addSpecName).hide();
		}
		$(specSave).hide();
		if($('Autocomplete_'+specInputId) != null){
			$('Autocomplete_'+specInputId).parentNode.style.display = "none";
		}
	} else {
		$(newSpecDivName).hide();
		$(specInputId).removeClassName("brd w500");
		$(specInputId).removeClassName("subwindow_open");
		$(specInputId).addClassName($('specialityClass_'+specInputId).value);
		$(afterSpecialityDivName).insert({before:$(oldSpecDivName)});
		if($(addSpecName) != null){
			$(addSpecName).show();
		}
		$(specSave).show();
		if($('Autocomplete_'+specInputId) != null){
			$('Autocomplete_'+specInputId).parentNode.style.display = "block";
		}
	}
}

function saveSpeciality(specInputId, specIdField, chooseMultiple){
	var group = "profession_group_id_"+specInputId;
	if(specialityAdditionalInputValidation(specInputId)){
		new Ajax.Request('/nacid/control/speciality_ajax/save', {
			parameters: {name: $(specInputId).value , group: $(group) == null? null:$(group).value,
				specialityId: specIdField.value == ""? 0:specIdField.value } ,
			onSuccess: function(transport){
				var json = transport.responseText.evalJSON(true);
				var present = json.present;
				$(specInputId).value = json.specialityName;
				specIdField.value = json.specialityId;
				if(present){
					$('present_message_'+specInputId).show();
					$('correct_message_'+specInputId).hide();
				} else {
					$('correct_message_'+specInputId).show();
					$('present_message_'+specInputId).hide();
				}
				$('error_message_'+specInputId).hide();
				showHideSpecialityDivs(specInputId);
				if(chooseMultiple){
					//addSpecialityToList(specInputId, specIdField);
				}
			},
			onFailure: function(){
				$('correct_message_'+specInputId).hide();
				$('error_message_'+specInputId).show();
				$('present_message_'+specInputId).hide();
			}
		});
	} else {
		alert('Моля, коригирайте полетата в червено!');
	}
}

function addSpecialityToList(specInputId, specIdField, originalSpecInputId, originalSpecIdField){
	var table = $('specialityListTable_'+specInputId);
	var listSize = table.rows.length;
    var specCountInput = $(specInputId + '_specialities_count');
    var specialitiesCount = specCountInput.value;
	if($(specIdField).value != ""){

        var specialityName = $(specInputId).value;
        var originalSpecialityName = $(originalSpecInputId) == null ? "" : $(originalSpecInputId).value;


        specialitiesCount = parseInt(specialitiesCount) + 1;
        var row = table.insertRow(listSize);
    	row.id = "specialityListrow_"+specInputId+""+specIdField.value;
    	var cell1 = row.insertCell(0);
    	cell1.innerHTML = specialityName + (originalSpecialityName == "" ? "" : " / " + originalSpecialityName);
    	var cell2 = row.insertCell(1);
    	cell2.style.display = "none";
    	var input = document.createElement("input");
    	input.value = specIdField.value;
    	input.name = "specialitiesListItem_" + specInputId + "_" + specialitiesCount;
    	cell2.appendChild(input);

        if (originalSpecIdField != null) {
            var input = document.createElement("input");
            input.value = originalSpecIdField.value;
            input.name = "specialitiesListItem_" + originalSpecInputId + "_" + specialitiesCount;
            cell2.appendChild(input);
        }


    	var cell3 = row.insertCell(2);
    	var a = document.createElement("a");
    	a.href = "javascript:void(0);";
    	a.onclick = function(){
    		removeSpecialityFromList(a.parentNode.parentNode, specInputId);
    	};
    	var img = document.createElement("img");
    	img.src = "/nacid/img/icon_delete.png";
    	a.appendChild(img);
    	cell3.appendChild(a);
    	specIdField.value = "";
        $(specInputId).value = "";

        if (originalSpecIdField != null) {
            originalSpecIdField.value = "";
            $(originalSpecInputId).value = "";
        }

        specCountInput.value = specialitiesCount;
	}
	showHideSpecialityList(specInputId);
}   

function removeSpecialityFromList(row, specInputId){
	var table = $('specialityListTable_'+specInputId);
	if(confirm("Сигурни ли сте, че искате да премахнете специалността от списъка?")){
		row.remove();
	}
	var rowsLeft = table.rows.length;
	if(rowsLeft == 0){
		$('specialityList_'+specInputId).hide();
	}
	showHideSpecialityList(specInputId);
}
function showHideSpecialityList(specInputId){
	if($('specialityListTable_'+specInputId).rows.length == 0){
		$('specialityList_'+specInputId).hide();
	} else {
		$('specialityList_'+specInputId).show();
	}
}

function specialityAdditionalInputValidation(specInputId){
	var ret = true;
	if ($(specInputId).value != "" && $(specInputId+"Id").value == "" && $('new_speciality_div_'+specInputId).visible == false) {
        setErrorOnInput($(specInputId), "Трябва да изберете специалност от падащото меню или да маркирате специалността за нов запис!");
        ret = false;
    } else if($(specInputId).value == "" && $(specInputId+"Id").value == "" && $('new_speciality_div_'+specInputId).visible == true){
    	setErrorOnInput($(specInputId), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Специалности\"!");
        ret = false;
    }
	if(!validateText($(specInputId), $('new_speciality_div_'+specInputId).visible() == true , 3, 150, ${validations.nomenclature_name_with_fullstop_digits}, "${messages.err_nomenclature_name_with_fullstop_digits}")){
		ret = false;
    } else {
  	  clearInputError($(specInputId));
    }
	return ret;
}
//---------------------------------------------------------------------------------





function originalSpecialityAdditionalInputValidation(specInputId){
    var ret = true;
    /*if ($(specInputId).value != "" && $(specInputId+"Id").value == "" && $('new_original_speciality_div_'+specInputId).visible == false) {
        setErrorOnInput($(specInputId), "Трябва да изберете специалност от падащото меню или да маркирате специалността за нов запис!");
        ret = false;
    } else if($(specInputId).value == "" && $(specInputId+"Id").value == "" && $('new_speciality_div_'+specInputId).visible == true){
        setErrorOnInput($(specInputId), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Специалности\"!");
        ret = false;
    }*/
    if(!validateText($(specInputId), $('new_speciality_div_'+specInputId).visible() == true , 3, 255, ${validations.allButQuote}, "${messages.allButQuote}")){
        ret = false;
    } else {
        clearInputError($(specInputId));
    }
    return ret;
}
function saveOriginalSpeciality(specInputId, specIdField, chooseMultiple){

    if(originalSpecialityAdditionalInputValidation(specInputId)){
        new Ajax.Request('/nacid/control/original_speciality_ajax/save', {
            parameters: {
                name: $(specInputId).value
            } ,
            onSuccess: function(transport){
                var json = transport.responseText.evalJSON(true);
                var present = json.present;
                $(specInputId).value = json.specialityName;
                specIdField.value = json.specialityId;
                if(present){
                    $('present_message_'+specInputId).show();
                    $('correct_message_'+specInputId).hide();
                } else {
                    $('correct_message_'+specInputId).show();
                    $('present_message_'+specInputId).hide();
                }
                $('error_message_'+specInputId).hide();
                showHideOriginalSpecialityDivs(specInputId);
                if(chooseMultiple){
                    //addSpecialityToList(specInputId, specIdField);
                }
            },
            onFailure: function(){
                $('correct_message_'+specInputId).hide();
                $('error_message_'+specInputId).show();
                $('present_message_'+specInputId).hide();
            }
        });
    } else {
        alert('Моля, коригирайте полетата в червено!');
    }
}

function showHideOriginalSpecialityDivs(specInputId){
    var newSpecDivName = "new_speciality_div_"+specInputId;
    var oldSpecDivName = "speciality_inner_div_"+specInputId;
    var outerSpecDivName = "speciality_outer_div_"+specInputId;
    var afterSpecialityDivName = "after_speciality_div_"+specInputId;
    var specSave = "specSave_"+specInputId;

    //var addSpecName = "addSpeciality_"+specInputId;
    if($(newSpecDivName).visible() == false){
        if ($$('.subwindow_open').length >= 1) {
            return;
        }

        $(newSpecDivName).insert({top:$(oldSpecDivName)});
        var specInputIdClasses = $(specInputId).classNames();
        $('specialityClass_'+specInputId).value = specInputIdClasses;
        $(specInputId).removeClassName(specInputIdClasses);
        $(specInputId).addClassName("subwindow_open brd w500");

        $(newSpecDivName).show();

        $(specSave).hide();
        if($('Autocomplete_'+specInputId) != null){
            $('Autocomplete_'+specInputId).parentNode.style.display = "none";
        }
    } else {
        $(newSpecDivName).hide();
        var specInputIdClasses = $(specInputId).classNames();
        $(specInputId).removeClassName(specInputIdClasses);
        $(specInputId).addClassName($('specialityClass_'+specInputId).value);

        $(afterSpecialityDivName).insert({before:$(oldSpecDivName)});
        $(specSave).show();
        if($('Autocomplete_'+specInputId) != null){
            $('Autocomplete_'+specInputId).parentNode.style.display = "block";
        }
    }
}