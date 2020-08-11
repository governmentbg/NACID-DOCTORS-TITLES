function setErrorOnInput(input, text) {
	$(input).addClassName('errorinput');
	$(input).removeClassName('brd');
	$(input).setAttribute('title', text);
}

function alertError(errors) {
	if (errors) {
		alert("Моля коригирайте полетата в червено!");
	}
}


function clearAllErrors(form) {
	
	$$(".errorinput").each(function (input) {
		clearInputError(input);
	});
	$$(".error_brd").each(function (element) {
		$(element).removeClassName("error_brd");
	});
}

function clearInputError(input) {
	$(input).addClassName('brd');
	$(input).removeClassName('errorinput');
	$(input).removeAttribute('title');
}

function isLeapYear(yyyy) {
	if (yyyy % 400 == 0) { return true; }
	if (yyyy % 100 == 0) { return false; }
	if (yyyy % 4 == 0) { return true; }

	return false;
}

function checkRequired(input, required) {
	if(required) {
		setErrorOnInput(input, 'полето е задължително');
		return false;
	}
	else {
		return true;
	}
}

function showIgnorecheckBox(ignoreErr) {
	$(ignoreErr).ancestors().first().show();
}

function validateLNCH(input, required, ignoreErr) {
	if (input == null) {
		printStackTrace();
		return false;
	}
	var val = input.value;

	if(val == null || val.length == 0) {
		return checkRequired(input, required);
	} else {
		if(ignoreErr.checked) {
			return true;
		}

		var digits = [], coeffs = [21, 19, 17, 13, 11, 9, 7, 3, 1];

		for (var i = 0; i < val.length; i++) {
			var digit = parseInt(val.charAt(i), 10);
			if (isNaN(digit)) { break; }
			digits[i] = digit;
		}

		if (10 != digits.length) {
			setErrorOnInput(input, "Моля, въведете 10 ЦИФРИ.");
			showIgnorecheckBox(ignoreErr);
			return false;
		}

		var checksum = 0;
		for (var j = 0; j < coeffs.length; j++) {
			checksum = checksum + (digits[j] * coeffs[j]);
		}
		checksum %= 10;
		if (digits[9] != checksum) {
			setErrorOnInput(input, "Грешна контролна цифра на въведеният ЛНЧ. Позиция 10.");
			showIgnorecheckBox(ignoreErr);
			return false;
		}
		return true;
	}


}
function validateEGN(input, required, ignoreErr) {
	console.log("Inside validateEGN..." + input.name + " Required:" + required + " IgnoreErr:" + ignoreErr.name + "..." + ignoreErr.checked);
	if (input == null) {
		printStackTrace();
		return false;
	}
	var val = input.value;

	if(val == null || val.length == 0) {
		return checkRequired(input, required);
	}
	else {
		
		if(ignoreErr.checked) {
			return true;
		}
		
		var digits = [],
		coeffs = [2, 4, 8, 5, 10, 9, 7, 3, 6],
		days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

		for (var i = 0; i < val.length; i++)
		{
			var digit = parseInt(val.charAt(i), 10);

			if (isNaN(digit)) { break; }
			digits[i] = digit;
		}

		if (10 != digits.length) { 
			setErrorOnInput(input, "Моля, въведете 10 ЦИФРИ.");
			showIgnorecheckBox(ignoreErr);
			return false;
		}

		var dd = digits[4] * 10 + digits[5],
			mm = digits[2] * 10 + digits[3],
			yy = digits[0] * 10 + digits[1],
			yyyy = null;

		if (mm >= 1 && mm <= 12) { 
			yyyy = 1900 + yy; 
		}
		else if (mm >= 21 && mm <= 32) { 
			mm -= 20; yyyy = 1800 + yy; 
		}
		else if (mm >= 41 && mm <= 52) { 
			mm -= 40; yyyy = 2000 + yy; 
		}
		else {
			setErrorOnInput(input, "Некоректен месец. Позиции 3 и 4.");
			showIgnorecheckBox(ignoreErr);
			return false;
		}

		days[1] += isLeapYear(yyyy) ? 1 : 0;

		if (!(dd >= 1 && dd <= days[mm - 1])) { 
			setErrorOnInput(input, "Некоректен ден. Позиции 5 и 6."); 
			showIgnorecheckBox(ignoreErr);
			return false;
		}

		// Gregorian calendar adoption. 31 Mar 1916 was followed by 14 Apr 1916.
		if (yyyy == 1916 && mm == 4 && (dd >= 1 && dd < 14)) {
			setErrorOnInput(input, "Грешна дата (от 01.04.1916 до 13.04.1916 г. - Григориански календар).");
			showIgnorecheckBox(ignoreErr);
			return false;
		}

		var checksum = 0;

		for (var j = 0; j < coeffs.length; j++) { 
			checksum = checksum + (digits[j] * coeffs[j]); 
		}
		checksum %= 11;
		if (10 == checksum) { 
			checksum = 0; 
		}

		if (digits[9] != checksum) { 
			setErrorOnInput(input, "Грешна контролна цифра на въведеният ЕГН. Позиция 10."); 
			showIgnorecheckBox(ignoreErr);
			return false; 
		}
	}
	return true;
}

function validateText(input, required, minLength, maxLength, regex, errormessage) {
	if (input == null) {
		printStackTrace();
		return false;
	}
	var val = input.value;

	if(val == null || val.length == 0) {
		return checkRequired(input, required);
	}
	else {
		if(minLength != -1 && val.length < minLength) {
			setErrorOnInput(input, errormessage == "" ? 'текстът трябва да е повече от ' + minLength + ' символа' :  errormessage);
			return false;
		}
		if(maxLength != -1 && val.length > maxLength) {
			setErrorOnInput(input, errormessage == "" ? 'текстът трябва да не е повече от ' + maxLength + ' символа' : "");
			return false;
		}
		if(regex != null) {
			if(!regex.test(val)) {
				setErrorOnInput(input, errormessage == "" ? 'недопустимо съдържание на полето' : errormessage);
				return false;
			}
		}
	}
			
	return true;
}
function printStackTrace() {
	try {
	    throw new Error("err");
	} catch (err) {
	    alert("Problem trying to validate Text...StackTrace:\n" + err.stack);
	}
}
function validateDate(dateInput, pattern, emptyString, required, beforeToday) {
	if (dateInput == null) {
		printStackTrace();
		return false;
	}
	var dateValue = dateInput.value;
	var now = new Date();
	var emptyDate = emptyString == null ? "дд.мм.гггг" : emptyString;
	// Tri proverki - 
	// 1 ako poleto e prazno no e required vry6ta false, ako ne e required - vry6ta true
	// 2. ako ne e prazno i ne e data, vry6ta false, 
	// 3. ako trqbva da e predi dnes no ne, pak vry6ta false. 
	// Ako ne vleze v nito edin if, vry6ta true
	if ((dateValue == "" || dateValue == emptyDate)) {
		if (required) {
			setErrorOnInput(dateInput, "Полето е задължително");
			return false;
		} 
		return true;
	} else if (!isDate(dateValue, pattern)) {
		setErrorOnInput(dateInput, "Грешно въведена дата:" + dateValue + " Разрешен формат:" + emptyDate);
		return false;
	} else if (beforeToday && ((now.getTime()+1) - getDateFromFormat(dateValue, pattern) < 0)) {
		setErrorOnInput(dateInput, "Датата не може да бъде по-голяма от днешната");
		return false;
	}
	return true;

}
//dateFrom - nachanla data; dateTo - krajna data; format - format na dannite (primerno дд.мм.гггг); message - съобщение при грешка; dontAllowEquals - ако true - датите не могат да са една и съща!
function validateDateInterval(dateFrom, dateTo, format, endFormat, message, dontAllowEquals) {
	if (dateFrom == null) {
		printStackTrace();
		return false;
	}
	if (dateTo == null) {
		printStackTrace();
		return false;
	}
	var errormessage = message == ""  ? "Крайният период не може да бъде преди началния!" : message;
	var dateFromValue = dateFrom.value;
    var dateToValue = dateTo.value;
    var error = false;
    if(endFormat == null || endFormat == '') {
    	endFormat = format;
    }
    var diff = compareDates(dateToValue, endFormat, dateFromValue, format);
    if (diff == null) {
        return true;
    }
    if (dontAllowEquals) {
    	error = diff <= 0;
    } else {
    	error = diff < 0;
    }
    if (error) {
     setErrorOnInput(dateFrom, errormessage);
     setErrorOnInput(dateTo,errormessage);
   }
   return !error;
}
function validateCheckBox(checkbox, required) {
	if (!required) {
		return true;
	}
	if (checkbox == null) {
		return true;
	}
	var checked = false;
	for (i = 0; i < checkbox.length; i++) {
		checked = checked | checkbox[i].checked;
	}
	if (!checked) {
		checkbox[0].up(1).addClassName("error_brd");
	}
	return checked ? true : false;
}
function validateComboBox(combobox, required, message) {
	var errormessage = message == ""  ? "Трябва да изберете опция от падащото меню" : message;
	if (!required) {
		return true;
	}
	if (combobox == null) {
		return true;
	}
	var result = combobox.options[combobox.selectedIndex].value != "-";
	if (!result) {
		setErrorOnInput(combobox, errormessage);
	}
	return result;
}