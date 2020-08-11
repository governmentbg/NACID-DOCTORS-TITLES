<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="nacid_ext" uri="/META-INF/ext_nacid_taglib.tld"%>
<nacid:applicationEdit>
<h3 class="title"><span>Електронно подадени данни от заявителя</span></h3>

    <h3 class="names">${application_header }</h3>
    <div class="clr15"><!--  --></div>
    <p class="cc">
        <input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?application_type=${application_type}&getLastTableState=1';" value="Назад" />
    </p>
    <nacid_ext:report_applicant_external  attachmentPrefix="${pathPrefix }/control/e_applying_attachments"/>
    <p class="cc">
        <input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1';" value="Назад" />
    </p>
</nacid:applicationEdit>