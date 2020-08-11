<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="regprof_ext" uri="/META-INF/ext_regprof_nacid_taglib.tld"%>
<h3 class="title"><span>Електронно подадени данни от заявителя</span></h3>
<h3 class="names">${application_header }</h3>
    <div class="clr15"><!--  --></div>
    <p class="cc">
        <input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1';" value="Назад" />
    </p>
    <regprof_ext:regprof_report_applicant_external attributePrefix="ext" attachmentPrefix="${pathPrefix }/control/e_applying_attachments"/>
    <div class="clr20"><!--  --></div>
    <p class="cc">
        <input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1';" value="Назад" />
    </p>
