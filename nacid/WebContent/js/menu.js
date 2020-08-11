function clickMenu(pathPrefix, menuId, reload) {
	if(reload) {
		makeRequest(pathPrefix, menuId);
		return true;
	}
	else {
		makeRequest(pathPrefix, menuId);
		changeStyles(menuId);
		return false;
	}
}

function changeStyles(menuId) {
	var menu = document.getElementById('menu'+menuId);
	var childs = document.getElementById('childs'+menuId);
	if(childs.style.display=='none') { 
		childs.style.display='block';
		menu.className = 'expanded';
	}
	else {
		childs.style.display='none';
		menu.className = 'colapsed';
	}
}

function makeRequest(pathPrefix, menuId) {
	if (window.XMLHttpRequest)
	{
		xmlHttp=new XMLHttpRequest();
	}
	else // for older IE 5/6
	{
		xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	var url= pathPrefix +"/ajax/menu?menu=" + menuId;
	xmlHttp.open("GET",url,false);
	xmlHttp.send(null); 
}