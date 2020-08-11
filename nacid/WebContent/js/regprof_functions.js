function updateProfInstitutionData(value, data) {
	var d = data.evalJSON(true);
	$('details.profInstitutionId').value = d.id;
	if (d.type == 1 || d.type == 2) {
		$('secBgName').value = d.name;
	} else if (d.type == 3) {
		$('high_bg_name').value = d.name;
		$('high_sdk_bg_name').value = d.name;
	}
}

function updateSDKProfInstitutionData(value, data) {
	var d = data.evalJSON(true);
	$('details.sdkProfInstitutionId').value = d.id;
	$('sdkBgName').value = d.name;
}
function updateProfInstitutionOrgNameId(value, data) {
	var d = data.evalJSON(true);
	$('details.profInstitutionOrgNameId').value = d.id;
}
function updateSdkProfInstitutionOrgNameId(value, data) {
	var d = data.evalJSON(true);
	$('details.sdkProfInstitutionOrgNameId').value = d.id;
}

function updateForExperienceCalculation(el) {
	var val = el[el.selectedIndex].value;
	if (val == '-') {
		return;
	}
	var isSelected = doctypesforcalc.get(val);
    var rowCnt = el.id.replace('experienceRecord.professionExperienceDocuments[', '');
    rowCnt = rowCnt.replace('].profExperienceDocTypeId', '');
    $('experienceRecord.professionExperienceDocuments[' + rowCnt + '].forExperienceCalculation').checked = isSelected;
	
}
function validateExperienceDocuments(validateDocumentDateAndNumber) {
    var ret = true;
	datesCount.keys().each(function(key) {
		if ($('experience_document_row'+key) != null) {
			var docType = $('experienceRecord.professionExperienceDocuments[' + key + '].profExperienceDocTypeId');
			
			var docNumber = $('experienceRecord.professionExperienceDocuments[' + key + '].documentNumber');
			var docDate = $('experienceRecord.professionExperienceDocuments[' + key + '].documentDate');
			var docIssuer = $('experienceRecord.professionExperienceDocuments[' + key + '].documentIssuer');
			ret = validateComboBox(docType,true,'') && ret;
			ret = validateText(docNumber,validateDocumentDateAndNumber ? parseInt(docType.value) == docTypeUP3 : false,2,255,null,'') && ret;
			ret = validateText(docDate,validateDocumentDateAndNumber ? parseInt(docType.value) == docTypeUP3 : false,2,50,null,'') && ret;
			ret = validateText(docIssuer,true,2,255,null,'') && ret;

		    for (var i = 0; i < datesCount.get(key); i++) {
		        if ($('document' + key + 'dates' + i) != null) {
		            var dateFrom = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].dateFrom');
		            var dateTo = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].dateTo');
		            var workdayDuration = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].workdayDuration');
		            ret = validateDate(dateFrom,'d.m.yyyy', null, true, true) && ret;
		            ret = validateDate(dateTo,'d.m.yyyy', null, true, true) && ret;
		            if (dateFrom.value && dateTo.value) {
		            	ret = validateDateInterval(dateFrom, dateTo, 'd.m.yyyy', 'd.m.yyyy', 'Грешен период', true) && ret;
		            }
		            /*if (docType[docType.selectedIndex].value == 2 && dateTo.value && docDate.value) { //up3
						ret = validateDateInterval(dateTo, docDate, 'd.m.yyyy', 'd.m.yyyy', 'Грешен период', false) && ret;
					} else if (docDate.value && dateFrom.value) {
						ret = validateDateInterval(docDate, dateFrom, 'd.m.yyyy', 'd.m.yyyy', 'Грешен период', false) && ret;
					}*/
		            ret = validateComboBox(workdayDuration, true, '') && ret;
		        }
		    }
		}
	});
	return ret;
}
/*function calculatePeriod(alertErr) {
	clearAllErrors(document.appform2);
	var correct = validateExperienceDocuments(false);
	if (!correct) {
		if (alertErr) {
			alertError(true);
		}
		$('totalDaysWorked').hide();
		$('periodWorked').hide();
		return;
	} else {
		var totalDays = 0;
		datesCount.keys().each(function(key) {
			if ($('experience_document_row'+key) != null) {
				var expCalc = $('experienceRecord.professionExperienceDocuments[' + key + '].forExperienceCalculation');
				if (expCalc.checked == true) {
					for (i = 0; i < datesCount.get(key); i++) {
				        if ($('document' + key + 'dates' + i) != null) {
				            var dateFrom = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].dateFrom').value;
				            var dateTo = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].dateTo').value;
							var workdayDuration = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].workdayDuration').value;
				            var daysDifference = parseInt(compareDates(dateTo,'d.m.yyyy',dateFrom,'d.m.yyyy') / 1000 / 60 / 60 / 24) + 1;
				            if (workdayDuration < 8) totalDays += parseInt(daysDifference * (workdayDuration / 8));
							else totalDays += daysDifference;
							
				        }
				        
				    }
				}
				
			}
		});
		
		$('total').innerHTML = totalDays + ' ';
		$('totalDaysWorked').show();
		var years = parseInt(totalDays / 360);
		totalDays %= 360;
		var months = parseInt(totalDays / 30);
		totalDays %= 30;
		$('experienceRecord.years').value = years;
		$('experienceRecord.months').value = months;
		$('experienceRecord.days').value = totalDays;
		
		var period = 'Общо <b>' + years + '</b> годин' + (years == 1 ? 'a' : 'и') + ', <b>' + months + '</b> месец' + (months == 1 ? '' : 'а') + ' и <b>' + totalDays + '</b>' 
			+ (totalDays == 1 ? ' ден' : ' дни');
		$('periodWorked').innerHTML = period + ' професионален стаж';
		$('periodWorked').show();
	}	
}*/
function calculatePeriod(alertErr) {
	clearAllErrors(document.appform2);
	var correct = validateExperienceDocuments(false);
	if (!correct) {
		if (alertErr) {
			alertError(true);
		}
		$('totalDaysWorked').hide();
		$('periodWorked').hide();
		return;
	} else {
		var totalDays = 0;
		var totalMonths = 0;
		var totalYears = 0;
		datesCount.keys().each(function(key) {
			if ($('experience_document_row'+key) != null) {
				var expCalc = $('experienceRecord.professionExperienceDocuments[' + key + '].forExperienceCalculation');
				if (expCalc.checked == true) {
					for (i = 0; i < datesCount.get(key); i++) {
				        if ($('document' + key + 'dates' + i) != null) {
				            var dateFrom = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].dateFrom').value;
				            var dateTo = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].dateTo').value;
							var workdayDuration = $('experienceRecord.professionExperienceDocuments['+key+'].dates['+i+'].workdayDuration').value;
                            var dateParts = dateFrom.split(".");
							var dateFromDay = parseInt(dateParts[0]);
							var dateFromMonth = parseInt(dateParts[1]);
							var dateFromYear = parseInt(dateParts[2]);

                            dateParts = dateTo.split(".");
							var dateToDay = parseInt(dateParts[0]);
							var dateToMonth = parseInt(dateParts[1]);
							var dateToYear = parseInt(dateParts[2]);

							var days = dateToDay - dateFromDay;
							var months = dateToMonth - dateFromMonth;
							var years = dateToYear - dateFromYear;
							if (days < 0) {
								days += 30;
								months -= 1;
							}
							if (months < 0) {
								months += 12;
								years -= 1;
							}
							if (workdayDuration < 8) {
								var ratio = workdayDuration / 8	;
								days = parseInt((days + 30 * months + 360 * years) * ratio);
								years = parseInt(days / 360);
								days %= 360;
								months = parseInt(days / 30);
								days %= 30;
							}
							totalDays += days;
							totalMonths += months;
							totalYears += years;
				        }
				    }
					totalMonths += parseInt(totalDays / 30);
					totalDays %= 30;
					totalYears += parseInt(totalMonths / 12);
					totalMonths %= 12;
				}
			}
		});
		
		totalMonths += parseInt(totalDays / 30);
		totalDays %= 30;
		totalYears += parseInt(totalMonths / 12);
		totalMonths %= 12;
		$('experienceRecord.years').value = totalYears;
		$('experienceRecord.months').value = totalMonths;
		$('experienceRecord.days').value = totalDays;
		
		var period = 'Общо <b>' + totalYears + '</b> годин' + (totalYears == 1 ? 'a' : 'и') + ', <b>' + totalMonths + '</b> месец' + (totalMonths == 1 ? '' : 'а') + ' и <b>' + totalDays + '</b>' 
			+ (totalDays == 1 ? ' ден' : ' дни');
		$('periodWorked').innerHTML = period + ' професионален стаж';
		$('periodWorked').show();
	}	
}
function addDatesRow(docNum) {
	var dateCount = datesCount.get(docNum);
	var element = $('dates_row').clone(true);
	element.id = 'document'  + docNum + 'dates' + dateCount;
	$('target'+docNum).insert({before:element});
	element.show();
	$$('#' + element.id + ' input').each(function(el) {
		//el.name = el.name.sub('-1', datesCount);
		//el.id = el.id.sub('-1', datesCount);
		el.name = 'experienceRecord.professionExperienceDocuments[' + docNum + '].dates[' + dateCount + '].' + el.name;
		el.id = 'experienceRecord.professionExperienceDocuments[' + docNum + '].dates[' + dateCount + '].' + el.id;
		el.disabled = false;
	});
	var selectBox = $$('#' + element.id + ' select')[0];
	//selectBox.name = selectBox.name.sub('-1', datesCount);
	selectBox.name = 'experienceRecord.professionExperienceDocuments[' + docNum + '].dates[' + dateCount + '].' + selectBox.name;
	selectBox.id = selectBox.name;
	selectBox.selectedIndex = 4; // 8 chasa
	selectBox.disabled = false;
	if (dateCount == 0) {
//		$$('#' + element.id + ' a[href="#"]')[0].remove();
	}
	//$$('#dates_row' + datesCount + ' a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('dates_row" + datesCount + "').remove();void(0);");
	datesCount.set(docNum, datesCount.get(docNum)+1);
}

function addExperienceDocumentRow() {
	var newDocNum = datesCount.keys().length;
	var element = $('experience_document_row').clone(true);
	element.id = element.id + newDocNum;
	$('after_last_document').insert({before:element});
	element.show();
	$$('#' + element.id + ' input', '#' + element.id + ' select').each(function(el) {
		//el.name = el.name.sub('-1', datesCount);
		//el.id = el.id.sub('-1', datesCount);
		if (el.name != null && el.name != "") {
			if (el.name == 'docId') { //tyi kato ima element s id="id" v dates, krystih tozi tuk docId, za da ne se poluchavat konflikti!
				el.name = 'id';
				el.id = 'id';
				if (newDocNum == 0) {//premahva butona "premahni" ako e pyrvi dobaven element!
					el.previous().remove();
				}
			}
			el.name = 'experienceRecord.professionExperienceDocuments[' + newDocNum + '].' + el.name;
		}
		if (el.id != null && el.id != "") {
			el.id = 'experienceRecord.professionExperienceDocuments[' + newDocNum + '].' + el.id;
		}
		
		el.disabled = false;
	});
	$$('#' + element.id + ' a[href="#"]')[0].setAttribute("href", "javascript:addDatesRow(" + newDocNum + ");void(0);");
	$$('#' + element.id + ' div[id="target"]')[0].id = 'target' + newDocNum;
	datesCount.set(newDocNum, 0);
	addDatesRow(newDocNum);
}

function toggleSubmitDivs() {
	if ($('submit_education').checked == true) {
		$('education').show();
		enableEducationDiv('education');
	} else {
		$('details.educationTypeId').selectedIndex = 0;
		$('education').hide();
		disableEducationDiv('education');
	}
	if ($('submit_experience').checked == true) {
		$('experience').show();
		enableEducationDiv('experience');
	} else {
		$('experience').hide();
		disableEducationDiv('experience');
	}
	showHideEduDivs(); //v protiven slu4ai moje da se enable-nat skritite tabove i da se submitne nekorektna informaciq
	if (datesCount.size() == 0) {
		addExperienceDocumentRow();
	}
}	

/*function getSecSpecialities() {
	while ($('secSpecialityId').options.length != 0) {
		$('secSpecialityId').options[0] = null;
	}
	var emptyOption = document.createElement("option");
	emptyOption.text = '-';
	emptyOption.value = '-';
	$('secSpecialityId').options.add(emptyOption);
	
	new Ajax.Request('/nacid_regprof/control/trainingcoursesuggestion?type=sec_speciality&prof_qual_id='+$('details.secProfQualificationId').value, {
	  method: 'get',
	  onSuccess: function(transport) {
  		var response = transport.responseText || "no response text";
		var json = response.evalJSON();
		if (json.error == true) {
			return;
		}
		for (var i = 0; i < json.specialities.length; i++) {
			var newOption = document.createElement("option");
				newOption.text = json.specialities[i].name;
				newOption.value = json.specialities[i].id;
				$('secSpecialityId').options.add(newOption);
			}
	  }
	});
	if ($('secSpecialityId').value == '-') {
		$('sec_qualification_degree').innerHTML = 'Няма информация за степен на професионалната квалификация';
	}
}*/

function changeDegree() {
	new Ajax.Request('/nacid_regprof/control/trainingcoursesuggestion?type=sec_degree&sec_speciality_id='+$('secSpecialityId').value, {
  	  method: 'get',
  	  onSuccess: function(transport) {
    	var response = transport.responseText || "no response text";
			var json = response.evalJSON();
			$('sec_qualification_degree').innerHTML = json.degree;
  	  },
  	  onFailure: function() {
  		$('sec_qualification_degree').innerHTML = 'Няма информация за степен';
  	  }
  	});
	if ($('secSpecialityId').value == '') {
		$('sec_qualification_degree').innerHTML = 'Няма информация за степен';
	}
}

function toggleArchiveNumber(select, container, number) {
	if (select[select.selectedIndex].value == archiveAppStatus) {
		container.show();
		number.enable();
	} else {
		if (oldArchiveNumber == emptyArchiveNumber) {
			container.hide();
		} else {
			container.show();
			number.disable();
			number.value = oldArchiveNumber;
		}
	}
}

function validateArchiveNumber(status, archiveNumber) {
	  if ((status[status.selectedIndex].value == archiveAppStatus) && archiveNumber.value == emptyArchiveNumber) {
		   setErrorOnInput(archiveNumber, "Трябва да въведете номенклатурен номер!");
		   return false;
		}
		return true;
}