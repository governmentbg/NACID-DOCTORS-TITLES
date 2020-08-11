/*
 * podava se input ot vida 1;2;3; a tazi funkciq vry6ta hash element
 */
function inputValueToHash(input) {
	var elements = input.split(";");
	var result = new Hash();
	for (var i = 0; i < elements.length; i++) {
		var el = elements[i]; 
		if (el != "") {
			result.set(el, el);
		}
	}
	return result;
}
//Podava se input ot vida "1;2;3" i id = 2 i tazi funkciq vry6ta 1;3 
function removeElementIdFromInput(input, id) {
	var hash = inputValueToHash(input);
	hash.unset(id);
	return hashToInput(hash);
}
/*
 * podava se input ot vida 1;2;3 i id = 10 i tazi funckiq vry6ta 1;2;3;10
 */
function addElementIdToInput(input, id) {
	var hash = inputValueToHash(input);
	hash.set(id, id);
	return hashToInput(hash);
}
function hashToInput(hash) {
	return hash.values().join(";");
}

//Izchstiva sydyrjanieto na div-a s text "dannite bqha zapisani"
function clearCorrectMesageDiv() {
	$$('div.correct').each(function(element) {
		element.update("");
	});
}

function isSpecialKeyPressed(e) {
	var key;
	var isSpecialKey;
	var isCtrlKey = false;
	if (window.event) {
		key = window.event.keyCode; //IE
		if (window.event.ctrlKey 
				|| window.event.altKey 
				|| window.event.shiftKey 
				|| window.event.keyCode == 20 //caps lock key 
				|| window.event.keyCode == 9 //tab key
				|| window.event.keyCode == 27 //esc key
				) {
			isSpecialKey = true;
			isCtrlKey = window.event.ctrlKey ? true : false;
		}
		else
			isSpecialKey = false;
	} else {
		key = e.which; //firefox
		if (e.ctrlKey || e.altKey || e.shiftKey || e.keyCode == 20 || e.keyCode == 9 || e.keyCode == 27) {
			isSpecialKey = true;
			isCtrlKey = e.ctrlKey ? true : false;
		}
			
		else
			isSpecialKey = false;
	}
	//nqma da ignorira kombinaciqta ctrl + tezi klavishi!
	var notDisabledCtrlKeys = new Hash();
	notDisabledCtrlKeys.set('x', true);
	notDisabledCtrlKeys.set('v', true);
	notDisabledCtrlKeys.set('ь', true);
	notDisabledCtrlKeys.set('ж', true);
	if (!isSpecialKey //Ako NE e natisnat specialen kalvish vry6ta false
			|| (isCtrlKey && (notDisabledCtrlKeys.get(String.fromCharCode(key).toLowerCase()))) //Ako e natisnata kombinaciqta ctrl+ nqkoi ot izbroenite v notDisabledCtrlKeys, to tazki kombinaciq se ignorira!
			) {
		return false;
	}
	return true;
	/*
    //list all CTRL + key combinations you want to disable
    var forbiddenKeys = new Hash();
    forbiddenKeys.set('a', true);
    forbiddenKeys.set('x', true);
    forbiddenKeys.set('c', true);
	//if ctrl is pressed check if other key is in forbidenKeys array
	if (isCtrl) {
		if (forbiddenKeys.get(String.fromCharCode(key).toLowerCase())) {
			 alert('Key combination CTRL + '
			                     +String.fromCharCode(key)
			                     +' has been disabled.');
			return false;
		}
	}
	return true;*/
}


function updateNomenclature(value, data, el) {
    var id = el.id;
    var d = data.evalJSON(true);
    $(id + "Id").value = d.id;
    
}
//Vika se pri promqna na speciality ili qualification
function nomenclatureChanged(type) {
    $(type + 'Id').value = ""; 
}

function resetField(field) {
	$(field).value = '';
}
//pravi daden div invisible, kato pravi i vsichki input/select/ elementi vytre v nego disabled!
function toggleDivElements(div, enabled) {
    $(div).toggle();
    var id = $(div).getAttribute('id');
    $$('#' + id + ' input', '#' + id + ' select').each(function(element) {
        if (enabled == false) {
        	  element.setAttribute('disabled', 'disabled');
        } else {
        	  element.removeAttribute('disabled');
        }
    });
}

function viewMode() {
	if (window.location.pathname.lastIndexOf('view') != -1) {
		for (var i = 0; i < arguments.length; i++) {
			arguments[i].hide();
		}
	}
}

function getSelectedItemText(checkbox) {
	return checkbox.options[checkbox.selectedIndex].innerHTML;
}

function docflowConfirmation() {
	return confirm('Сигурни ли сте, че искате да генерирате деловоден номер?');
}

/**
 * @param select - prof_group select box
 * pri promqna na profGroup, tyrsi element s id=idto na prof_group-a + _edu_area_id -> ako ima takyv element (toi trqbva da e checkbox) mu selectva opciqta syotvetstvashta na eduAreata na profGroup-a
 */
function updateProfGroupEduArea(select) {
	var opt = select.options[select.selectedIndex];
	var elementId = select.getAttribute("id");
	var eduAreaComboEl = $(elementId + "_edu_area_id");
	if (eduAreaComboEl != null) {
		var eduAreaId = opt.getAttribute("attr-edu-area-id");
		selectOption(eduAreaComboEl, eduAreaId == null ? '-' : eduAreaId);
	}
}

/**
 * selects an option in select box.
 * @param id - the select box
 * @param option - the option that should be selected!
 */
function selectOption(id, option) {
	$(id).childElements().each(function (item){
		if(item.value == option) {
			item.selected = true;
		}
	});
}