<%@ page contentType="application/javascript; charset=utf-8" pageEncoding="utf-8"%>
function updateUniversity(value, data, el) {
    var id = el.id;
    var d = data.evalJSON(true);
    $('universityId').value = d.id;
}
function universityChanged(el) {
    $(el.id.replace('universityName','universityId')).value = '';
}
function disableCommissionCalendar(el, type) {
    if (el.value != '') {
        if (type == 'number') {
            $('commissionDateFrom').disable();
            $('commissionDateTo').disable();
        } else {
            $('start_session_id').disable();
            $('end_session_id').disable();
        }
    } else {
        if (type == 'number') {
            if ($('start_session_id').value == "" && $('end_session_id').value == "") {
                $('commissionDateFrom').enable();
                $('commissionDateTo').enable();
            }
        } else if ( (($('commissionDateFrom').value == "" || $('commissionDateFrom').value == "дд.мм.гггг") && ($('commissionDateTo').value == "" || $('commissionDateTo').value == "дд.мм.гггг")) ) {
            $('start_session_id').enable();
            $('end_session_id').enable();
        } 
    }
}

function doRefresh(form, table, updatechecked) {
    doRefresh2(form, table, updatechecked);
    return true;
}
function getUniversities(el) {
    var universityId = el.id.replace("country","");
    var countryId = el.options[el.selectedIndex].value;
    new Ajax.Request('${pathPrefix}/control/diploma_type_university?university_number=' + universityId + '&countryId=' + countryId, {
        onComplete: function(oXHR) {
            $("university_span" + universityId).update(oXHR.responseText);
        },
        method: 'GET'
  });
    
}

/*function addUniversity() {
    var el = $('university').clone(true);
    el.id = el.id + universities;
    $('afterUniversityDiv').insert({before:el});
    el.show();
    // Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq
    $$('#university' + universities + ' select', '#university' + universities + ' span').each(function(element) {
        
        
        if (element.name != null && element.name != "") {
            element.name = element.name + universities;
        }
        if (element.id != null && element.id != "") {
            element.id = element.id + universities ;
        }
    });
    //Preobrazuva href-a na linka "premahni"
    $$('#university' + universities + ' a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('university" + universities + "').remove();enableDisableJointDegreeCheckBox();");
     
    universities++;
    $('universities_count').value = universities;
  }*/


function addUniversity() {
    var el = $('university_div').clone(true);
    el.id = el.id.replace("university", "university" + universities);
    $('afterUniversityDiv').insert({before:el});
    el.show();
    /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq*/
    $$('#university' + universities + '_div select', '#university' + universities + '_div span', '#university' + universities + '_div input', '#university' + universities + '_div div', '#university' + universities + '_div table',
        '#org_university' + universities + '_div select', '#org_university' + universities + '_div span', '#org_university' + universities + '_div input', '#org_university' + universities + '_div div', '#org_university' + universities + '_div table').each(function(element) {
        //console.log(element.id);
        if (element.name != null && element.name != "") {
            element.name = element.name.replace("university", "university" + universities);
        }
        if (element.id != null && element.id != "") {
            element.id = element.id.replace("university", "university" + universities) ;
        }
    });
    /*Preobrazuva href-a na linka "premahni"*/
    $$('#university' + universities + '_div a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('university" + universities + "_div').remove();void(0);");
    
    $('university' + universities).setAttribute('onkeydown', "if(!isSpecialKeyPressed(event)) inquireUniversityChanged('university" + universities + "');");
    $('org_university' + universities).setAttribute('onkeydown', "if(!isSpecialKeyPressed(event)) inquireUniversityChanged(org_'university" + universities + "');");
    //Linka-a na dobavi
    $$('#university' + universities + '_div a[href="#"]')[0].setAttribute('href', "javascript:addInquireUniversity('university" + universities + "'); void(0);");
    

    var val = 'university' + universities + 'Country';
    new Autocomplete('university' + universities, { 
        serviceUrl:'${pathPrefix}/control/universitysuggest',
        width:500,
        onSelect:updateInquireUniversity,
        useBadQueriesCache:false,
        dynamicParameters: function (){
            return {country_id : $(val).value};
        }
    });
    new Autocomplete('org_university' + universities, {
        serviceUrl:'${pathPrefix}/control/universitysuggest',
        width:500,
        onSelect:updateInquireUniversity,
        useBadQueriesCache:false,
        dynamicParameters: function (){
            return {country_id : $(val).value, nametype : 2};
        }
    });

    universities++;
    $('universities_count').value = universities;
}
function addInstitution() {
    var el = $('institution_div').clone(true);
    el.id = el.id.replace("institution", "institution" + institutions);
    $('afterInstitutionDiv').insert({before:el});
    el.show();
    $$('#institution' + institutions + '_div select', '#institution' + institutions + '_div span', '#institution' + institutions + '_div input', '#institution' + institutions + '_div div', '#institution' + institutions + '_div table').each(function(element) {
        //console.log(element.id);
        if (element.name != null && element.name != "") {
            element.name = element.name.replace("institution", "institution" + institutions);
        }
        if (element.id != null && element.id != "") {
            element.id = element.id.replace("institution", "institution" + institutions) ;
        }
    });
    /*Preobrazuva href-a na linka "premahni"*/
    $$('#institution' + institutions + '_div a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('institution" + institutions + "_div').remove();void(0);");

    $('institution' + institutions).setAttribute('onkeydown', "if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution" + institutions + "');");
    //Linka-a na dobavi
    $$('#institution' + institutions + '_div a[href="#"]')[0].setAttribute('href', "javascript:addNomenclature('institution" + institutions + "'); $('institution" + institutions + "Country').disable(); void(0);");


    var val = 'institution' + institutions + 'Country';
    new Autocomplete('institution' + institutions, {
        serviceUrl:'${pathPrefix}/control/institutionsuggest',
        width:500,
        onSelect:updateNomenclature,
        useBadQueriesCache:false,
        dynamicParameters: function (){
            return {country_id : $(val).value};
        }
    });

    institutions++;
    $('institutions_count').value = institutions;
}
/**
 type - dali stava vypros za diplomaSpeciality ili za recognizedSpeciality
 currentValue - broq specialnosti kym momenta
*/
function addSpeciality(type, currentValue) {
    var el = $(type + '_div').clone(true);
    el.id = el.id.replace(type , type + currentValue);
    $('after' + type + 'Div').insert({before:el});
    el.show();
    $$('#' + type + currentValue + '_div select', '#' + type + currentValue + '_div span', '#' + type + currentValue + '_div input', '#' + type + currentValue + '_div div', '#' + type + currentValue + '_div table').each(function(element) {
        if (element.name != null && element.name != "") {
            element.name = element.name.replace(type, type + currentValue);
        }
        if (element.id != null && element.id != "") {
            element.id = element.id.replace(type, type + currentValue) ;
        }
    });
    /*Preobrazuva href-a na linka "premahni"*/
    $$('#' + type + currentValue + '_div a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('" + type + currentValue + "_div').remove();void(0);");
    
    $(type + currentValue).setAttribute('onkeydown', "if(!isSpecialKeyPressed(event)) nomenclatureChanged('" + type + currentValue + "');");
    //Linka-a na dobavi
    $$('#' + type + currentValue + '_div a[href="#"]')[0].setAttribute('href', "javascript:addNomenclature('" + type + currentValue + "'); $('" + type + currentValue + "ProfGroup').disable(); void(0);");
    
    var val = type + currentValue + 'ProfGroup';
    new Autocomplete(type + currentValue, { 
        serviceUrl:'${pathPrefix}/control/specialitysuggest',
        width:500,
        onSelect:updateNomenclature,
        useBadQueriesCache:false,
        dynamicParameters: function (){
            return {group_id : $(val).value};
        }
    });

    currentValue++;
    $(type + '_count').value = currentValue;
}


function addStatus(type) {
    var typeName = typeof(type) != 'undefined' ? type : '';
    var elName = typeName+'status';
    var el = $(elName).clone(true);
    var statCount = $(elName + '_ids_count').value;
    el.id = el.id + statCount;
    $(typeName+'afterStatusDiv').insert({before:el});
    el.show();
    /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq*/
    $$('#' + elName + statCount + ' input', '#' + elName + statCount + ' select', '#' + elName + statCount + ' div', '#' + elName + statCount + ' label').each(function(element) {
        //console.debug(element);
        if (element.name != null && element.name != "") {
            element.name = element.name + statCount;
        }
        if (element.id != null && element.id != "") {
            element.id = element.id + statCount ;
        }
        if (element.getAttribute("for") != null && element.getAttribute("for") != "") {
            element.setAttribute("for", element.getAttribute("for") + statCount);
        }
    });
    /*Preobrazuva href-a na linka "premahni"*/
    $$('#' + elName + statCount + ' a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('" + elName + statCount + "').remove();void(0);");
    statCount++;
    $(elName + '_ids_count').value = statCount;
    
    
}
function updateLegalReasons(select, form, divName, row_id ) {
    divName = typeof(divName) != 'undefined' ? divName : 'legal_reason_div';
    row_id = typeof(row_id) != 'undefined' ? row_id : '';
    var appStatusId = select.options[select.selectedIndex].value;
    if (appStatusId == '-') {
        $(divName).update("");
    } else {
        new Ajax.Request('${pathPrefix}/control/legal_reason_ajax?applicationType=0&appStatusId=' + appStatusId + '&type=commission_inquire&row_id=' + row_id, {
            onCreate : function(oXHR) {
               setLoading(form);
            },
            onSuccess: function(oXHR) {
                $(divName).update(oXHR.responseText);
                removeLoading(form);
                showHideDiv('legalReason' + row_id);
            },
            onFailure : function(oXHR) {
                alert("Възникна грешка при опит за прочитане на правните основания, свързани с този статус на заявление. Моля опитайте по-късно..." + oXHR.statusText);
                $(divName).update("");
                removeLoading(form);
            },
            method: 'GET'
          });
    }
}

function updateDirectiveArticleItems(select, form, divName, row_id ) {
    divName = typeof(divName) != 'undefined' ? divName : 'directive_article_item_div';
    row_id = typeof(row_id) != 'undefined' ? row_id : '';
    var directiveId = select.options[select.selectedIndex].value;
    if (directiveId == '-') {
        $(divName).update("");
    } else {
        new Ajax.Request('${pathPrefix}/control/article_item_ajax?addinactive=1&articleId=' + directiveId + '&type=inquire&row_id=' + row_id, {
            onCreate : function(oXHR) {
               setLoading(form);
            },
            onSuccess: function(oXHR) {
                $(divName).update(oXHR.responseText);
                removeLoading(form);
                showHideDiv('directive_item_id' + row_id);
            },
            onFailure : function(oXHR) {
                alert("Възникна грешка при опит за прочитане на правните основания, свързани с този статус на заявление. Моля опитайте по-късно..." + oXHR.statusText);
                $(divName).update("");
                removeLoading(form);
            },
            method: 'GET'
          });
    }
}

function addElement(type) {
	var selectedOption = $(type).options[$(type).selectedIndex];
    var optionId = selectedOption.value;
    var optionTxt = selectedOption.innerHTML; 
    if(optionId == '-') {
        return;
    }
    _addElement(type, optionId, optionTxt);
    //$(type).selectedIndex = 0;
    
}
/*
tazi funkciq za dobavqne na element moje da se polzva kakto za dobavqne na dyrjavi/specialnosti/kvalifikacii/OKS, koito izglejdat po edin i sy6t na4in
taka i za universiteti!
 
*/
function _addElement(type, optionId, optionTxt ) {
    var lastRow = $$('#selected_' + type + ' tr:last-child')[0];
    var currentNumber = lastRow === undefined ? 0 : parseInt(lastRow.id.replace(type + '_row_id_', ''));
    currentNumber++;

    hash = inputValueToHash($(type + 'Ids').value);
    //Ako ima ve4e vyveden takyv element, togava ne se vyvejda za vtori pyt!
    if(hash.index(optionId) !== undefined) {
        return;
    }


    $(type + 'Ids').value = addElementIdToInput($(type + 'Ids').value, optionId);


    var tr = new Element('tr', {'id': type + '_row_id_' + currentNumber});

    var td0 = new Element('td');
    var inp = new Element('input');
    inp.value = optionId;
    inp.setAttribute("type", "hidden");
    inp.setAttribute("disabled", "disabled");

    td0.insert(inp);
    td0.setAttribute("style", "display:none;");

    var td1 = new Element('td');
    td1.insert(optionTxt);
    
    var td2 = new Element('td');
    var a2 = new Element('a', {'href': 'javascript:removeItem("' + type + '", "' + currentNumber + '");;void(0);'});
    var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
    a2.insert(img);
    td2.insert(a2);

    tr.insert(td0);
    tr.insert(td1);
    tr.insert(td2);
    
    $('selected_' + type).down().insert({bottom: tr});
    showHideDiv(type);
}
function removeItem(type, id) {
    var elementId = type + '_row_id_' + id;
    var value = $$('#' + elementId + ' input')[0].value;
    $(type + 'Ids').value = removeElementIdFromInput($(type + 'Ids').value, value);
    $(elementId).remove();
    showHideDiv(type);
}
function showHideDiv(type) {
    if ($(type+'Ids') == null) {
	    return;
    }
    //console.log("inside showHideDiv" + type);
    if($(type + 'Ids').value == '') {
        $(type + 'Div').hide();
    } else {
        $(type + 'Div').show();
    }
}
function generateTable(form, url) {
	ajaxGenerate(form, url, 'table_result');	
}
function generateScreen(el, form, url) {
    //alert($(el).next(0));
    form.screen.value = $(el).next(0).value;
    if (form.screen.value != 5) {
        ajaxGenerate(form, url, 'inquires_screens');
        $('screen_options_div').hide();
    } else {
        $('screen_options_div').show();
        $('inquires_screens').hide();
        $('screen_options_form').action = url;
        $('word_button').hide();
        $('show_button').show();
    }
}
function generateWordScreenReport(el, form) {
    form.screen.value = $(el).next(1).value;
    if (form.screen.value != 5) {
        $('screen_options_div').hide();
        return true;
    } else {
        $('screen_options_div').show();
        $('inquires_screens').hide();
        $('word_button').show();
        $('show_button').hide();
        return false;
    }
}
function updateScreenSelect(el) {
    var val = el.options[el.selectedIndex].value;
    $('InquireForm').select('select[name="scr_type"]').each(function(element) {
        element.value = val;
    });
}
function ajaxGenerate(form, url, divName, form2) {
    new Ajax.Request(url, {
        method :"POST",
        parameters :Form.serialize(form),
        asynchronous :true,
        onFailure : function(oXHR) {
            alert("Възникна грешка при опит за записване на данните в базата. Моля опитайте по-късно..." + oXHR.statusText);
            removeLoading(form);
            if (typeof(form2) != 'undefined') {
                removeLoading(form2);
            }
        },
        onCreate : function(oXHR) {
            setLoading(form);
            if (typeof(form2) != 'undefined') {
                setLoading(form2);
            }
        },
        onSuccess : function(oXHR) {
            removeLoading(form);
            if (typeof(form2) != 'undefined') {
                removeLoading(form2);
            }
            $(divName).update(oXHR.responseText);
            $(divName).show();

            //funkciq, koqto shte se execute-va sled ajax refresh!
            if (typeof (window.postAjaxRefresh) == 'function') {
                postAjaxRefresh();
            }
        }
      });
}
function inquireUniversityChanged(type) {
    var isOriginalName = false;
    if (type.startsWith("org_")) {
        isOriginalName = true;
    }
    //console.log('OriginalName:' + isOriginalName);
    var bgUniverstiyInput = isOriginalName ? type.replace('org_', '') : type;
    var orgUniversityInput = isOriginalName ? type : 'org_' + type;
    //console.log(bgUniverstiyInput + '...' + orgUniversityInput);
    $(type.replace('org_', '') + 'Id').value = "";
    if (isOriginalName) {
        $(bgUniverstiyInput).value = '';
    } else {
        $(orgUniversityInput).value = '';
    }
}
function updateInquireUniversity(value, data, el) {
    var id = el.id;
    var isOriginalName = false;
    if (id.startsWith("org_")) {
        isOriginalName = true;
    }
    //console.log('Inside updateInquireUniversity...isOriginalName=' + isOriginalName);
    var d = data.evalJSON(true);
    id = id.replace('org_', '');
    $(id + "Id").value = d.id;
    if (isOriginalName) {
        //console.log('Updating ' + id + ' with value: ' + d.bgName);
        $(id).value = d.bgName;
    } else {
        //console.log('Updating org_' + id + ' with value: ' + d.orgName);
        $('org_' + id).value = d.orgName;
    }

}
<%-- premestih gi v functions.js
function updateNomenclature(value, data, el) {
    var id = el.id;
    var d = data.evalJSON(true);
    $(id + "Id").value = d.id;
    
}
//Vika se pri promqna na diplomaSpeciality ili recognizedSpeciality
function nomenclatureChanged(type) {
    $(type + 'Id').value = ""; 
} --%>
function addNomenclature(type) {
    var id = $(type + 'Id').value;
    var name = $(type).value;
    if (id == "" && name == "") {
        return;
    }
    //Ако последно въведения символ е *, го маха!
    if (id == "" && name.charAt(name.length - 1) == '*') {
        name = name.substring(0, name.length - 1);
    }

    //Ako e vyvedeno ID, togava to se zapisva v spisyka s id-ta (pr:diplomaSpecialityIds), a ako e vyvedeno name, to se zapisva v spisyka s names (pr:diplomaSpecialityNamesIds)
    var optionId = id == "" ? name : id;
    var optionTxt = id == "" ? name + "*" : name; 
    _addElement(id == "" ? type + 'Names' : type, optionId, optionTxt);

    $(type).value = "";
    $(type + 'Id').value = "";
}
function addInquireUniversity(type) {
    var id = $(type + 'Id').value;
    var name = $(type).value;
    if (id != '' || name != '') {
        addNomenclature(type);
    } else {
        orgName = $('org_' + type).value;
        if (orgName.charAt(orgName.length - 1) != '*') {
            orgName = orgName + '*';
        }
        _addElement('org_' + type + 'Names', orgName, orgName);
    }
    $('org_' + type).value = "";
    $(type + 'Country').disable();
    return false;
}

function validateInquireDates() {
    var result = true;
    $$('input.dateFrom').each(function(dateFromInput) {
        if (!dateFromInput.up(1).visible() || dateFromInput.id == 'dateFrom') {
            return;
        } else {
            var dateToInput = $(dateFromInput.id.replace("From","To"));
            result = result && validateDate(dateFromInput,'d.m.yyyy',null,false,true);
            result = result && validateDate(dateToInput,'d.m.yyyy',null,false,true);
            result = result && validateDateInterval(dateFromInput,dateToInput,'d.m.yyyy',null,'',false);
        }
    });
    return result;
}
function toggleInquireDates(select, statusType) {
    var statusId = select.options[select.selectedIndex].value;
    var el = $(select.id.replace(statusType + '_id', statusType + '_dates_div'));
    if (statusId == '-') {
        el.hide();
    } else {
        el.show();
    }
}