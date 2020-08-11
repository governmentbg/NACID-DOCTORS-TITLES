
function setLoading(element) {
	var div = getLoadingDivElement(element);
	var st = Object();
	if (div == null) {
		div = new Element("div");
		div.id = element.id + "_loadingdiv";
		$(element).insert( {
			after :div
		});
		st.position = 'absolute';
		st.opacity = 0.7;
		st.filter = 'alpha(opacity=70)';
		st.background = '#666';
		div.update("<center class='loading' style='color:white'><img src='/nacid/img/loading.gif' alt='' /> Моля изчакайте!<\/center>");
	}
	//Tezi opcii trqbva da se settivat vseki pyt, zashtoto na tablicata - v zavisimost ot vizualiziranite danni height-a e razlichen
	st.height = element.getHeight() + 'px';
	st.lineHeight = element.getHeight() + 'px';
	st.width = element.getWidth() + 'px';
	st.top = element.cumulativeOffset().top + 'px';
	st.left = element.cumulativeOffset().left + 'px';
	div.setStyle(st);
	div.show();
	return true;
}

function removeLoading(element) {
	var div = getLoadingDivElement(element);
	if (div != null) {
		div.hide();
	}
	return true;
}
function getLoadingDivElement(element) {
	var elname = element.id;
	if (elname == null) {
		elname = element.name;
	}
	return $(elname + "_loadingdiv");
}

function ajaxUpdateForm(form) {

	var oOptions = {
		method :"POST",
		parameters :Form.serialize(form),
		asynchronous :true,
		onFailure : function(oXHR) {
			alert("Възникна грешка при опит за записване на данните в базата. Моля опитайте по-късно..." + oXHR.statusText);
			removeLoading(form);
		},
		onCreate : function(oXHR) {
			setLoading(form);
		},
		onSuccess : function(oXHR) {
			removeLoading(form);
			alert(oXHR.responseText);
		}
	};
	var oRequest = new Ajax.Updater( {
		success :oOptions.onSuccess.bindAsEventListener(oOptions)
	}, form.action, oOptions);

}

/**
 * primer za polzvane
 * new AjaxJsonFieldsUpdater({
                    elements: [
                        'institution_id',
                        'institution_name',
                    ],
                    jsonFieldNames : {
                        'alabala' : 'alabala_id'
                    }
                }).update('${pathPrefix}/control/competent_institution_ajax/view?id=5';
    elements -> id na elementite, koito shte trqbva da se update-vat.
    jsonFieldNames -> Ako imenata na field-ovete ne syotvetstvat na json imenata, to togava se podava jsonFieldNames {'field_id', 'json_object_field'}
    callback -> NOT REQUIRED callback function, koqto priema edin parametyr: json object-a vyrnat sled izvikvaneto na URL-to

 */
var AjaxJsonFieldsUpdater = Class.create();
AjaxJsonFieldsUpdater.prototype = {
    initialize: function(options) {
        this.options = {
        };
        Object.extend(this.options, options || { });
    },
    update: function(url) {
        var opts = this.options;
        new Ajax.Request(url, {
            onSuccess: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON(true);
                var elements = opts.elements;
                var jsonFieldNames = opts.jsonFieldNames;


                var iterator = new Object();
                if (elements != null) {
                    for (var i = 0; i < elements.length; i++) {
                        iterator[elements[i]] = elements[i];
                    }
                }
                if (jsonFieldNames != null) {
                    for (var prop in jsonFieldNames) {
                        iterator[prop] = jsonFieldNames[prop];
                    }
                }

                for (var element in iterator) {
                    if ($(element) == null) {
                        alert("ERROR!!!!! The element " + element + " doesn't exist");
                        return;
                    }
                    if (json[iterator[element]] == null) {
                        alert("ERROR!!!! The JSON Object " + iterator[element] + " doesn't exist!");
                        return;
                    }
                    $(element).value = json[iterator[element]];
                }
                if (typeof opts.callback != 'undefined') {
                    opts.callback(json);
                }
            },
            onFailure : function(transport) {
                alert("Възникна грешка. Моля опитайте по-късно..." + oXHR.statusText);
            },
            method: 'GET'
        });
    }
};