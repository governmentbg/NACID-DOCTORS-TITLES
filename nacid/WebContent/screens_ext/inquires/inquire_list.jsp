<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<nacid:list />
<br />
<fieldset><legend class="noForm">Статус от комисия</legend>
<table width="100%" cellspacing="0" cellpadding="1" border="1" class="sort-table" >
        <thead>
            <tr>
                <td class="dark" width="20">No</td>
                <td class="dark">Статус</td>
                <td class="dark">Брой заявления</td>
            </tr>
        </thead>
        <nacid:applicationStatuses>
        <tr style="background-color: #ffffff;" onmouseover="this.style.backgroundColor='#e0e0e0';" onmouseout="this.style.backgroundColor='#ffffff';">
            <td>${row_no }</td>
            <td>${status }</td>
            <td>${apps_count }</td>
        </tr>
        </nacid:applicationStatuses>
</table>
</fieldset>
