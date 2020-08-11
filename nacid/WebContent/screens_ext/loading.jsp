<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>

<nacid:loading>

	<div class="darkBg"></div>

	<div class="loadingContainer">
		<!--  <div class="loadingText">Файла се качва</div>
		<div style="" class="loadingFrame"></div>
		<div style="" class="loadingContent">${percents}</div>
		<div style="width: ${readed * 200 / total}px;" class="loadingBar"></div>
		<div class="loadingText">Моля изчакайте!</div> -->
		
		<div class="uploadMsg">Файлът се качва</div>  
        <div class="uploadStatus">${percents}</div> 
		<br style="clear:both; " />
        <div class="uploadBar">  
            <div class="uploadProgressBar" style="width: ${percents}"></div>  
        </div>  
        <div class="uploadInfo">Моля изчакайте!</div> 

	</div>

</nacid:loading>